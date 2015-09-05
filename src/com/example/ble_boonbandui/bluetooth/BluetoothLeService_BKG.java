package com.example.ble_boonbandui.bluetooth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothGattService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.example.ble_boombandui.R;
import com.example.ble_boombandui.dao.WaveReaderContract.WaveEntry;
import com.example.ble_boombandui.dao.WaveDao;
import com.example.ble_boombandui.dao.WaveReaderDbHelper;
import com.example.ble_boombandui.model.WaveData;

//ble��̨����
public class BluetoothLeService_BKG extends Service {

	private BluetoothAdapter mBluetoothAdapter;
	private boolean mScanning;
	private boolean mBluetoothOpened = false;
	private ArrayList<BluetoothDevice> mLeDeviceList = new ArrayList<BluetoothDevice>();
	private final static String TAG = BluetoothLeService_BKG.class
			.getSimpleName();
	private String mBluetoothDeviceAddress;
	private BluetoothGatt mBluetoothGatt;
	private BluetoothManager mBluetoothManager;

	private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
	private BluetoothGattCharacteristic mNotifyCharacteristic;
	private BluetoothGattCharacteristic mWriteCharacteristic;
	private final String LIST_NAME = "NAME";
	private final String LIST_UUID = "UUID";

	private int mConnectionState = STATE_DISCONNECTED;

	private static final int STATE_DISCONNECTED = 0;
	private static final int STATE_CONNECTING = 1;
	private static final int STATE_CONNECTED = 2;

	public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
	public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
	public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";

	public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID
			.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);
	public final static UUID UUID_READ = // ����
	UUID.fromString(SampleGattAttributes.AMOMCU);
	public final static UUID UUID_WRITE = // д��
	UUID.fromString(SampleGattAttributes.AMOMCU);
	public final static String DEVICENAME = "ModiaTek BLE SBP";

	// public final static String DEVICENAME="AmoMcu.com";
	// public final static String DEVICENAME = "hello world";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if (!getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(getBaseContext(), R.string.ble_not_supported,
					Toast.LENGTH_SHORT).show();
			stopSelf();
		}

		// Initializes a Bluetooth adapter. For API level 18 and above, get a
		// reference to
		// BluetoothAdapter through BluetoothManager.
		mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = mBluetoothManager.getAdapter();

		// Checks if Bluetooth is supported on the device.
		if (mBluetoothAdapter == null) {
			Toast.makeText(getBaseContext(),
					R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT)
					.show();
			stopSelf();
			return;
		}

		// ������
		// Ensures Bluetooth is enabled on the device. If Bluetooth is not
		// currently enabled,
		// fire an intent to display a dialog asking the user to grant
		// permission to enable it.
		if (!mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.enable();
			Toast.makeText(getBaseContext(), "opening bluetooth ...",
					Toast.LENGTH_LONG).show();
			try {
				Thread.sleep(8000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// new Thread(new Runnable() {
		// @Override
		// public void run() {

		// while (true){
		if (!mBluetoothAdapter.isEnabled()) {
			mBluetoothOpened = false;

		} else {
			mBluetoothOpened = true;

			if (!mScanning && (mConnectionState == STATE_DISCONNECTED)) {
				// ɨ����û���豸
				Message msg = startScan_handler.obtainMessage();
				msg.obj = "scan";
				startScan_handler.sendMessage(msg);
			}
		}
	}

	public Handler startScan_handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String str = msg.obj.toString();
			if (str.equals("scan")) {
				// ɨ����û���豸
				scanLeDevice(true);
			} else if (str.equals("reconnect")) {
				while (true) {
					if (!mBluetoothAdapter.isEnabled()) {
						mBluetoothOpened = false;
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else {
						mBluetoothOpened = true;
						scanLeDevice(true);
						break;
					}
				}
			}
		}
	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		scanLeDevice(false);
		mLeDeviceList.clear();
		// super.onDestroy();
		Intent localIntent = new Intent();
		localIntent.setClass(this, BluetoothLeService_BKG.class); // ����ʱ��������Service
		this.startService(localIntent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// if(mBluetoothAdapter.isEnabled())
		// {
		// mBluetoothOpened=true;
		// if(!mScanning&&(mConnectionState==STATE_DISCONNECTED))
		// //ɨ����û���豸
		// scanLeDevice(true);
		// }
		// �����Զ�����
		return START_STICKY;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	// --------------------------------
	// �����O��
	private void scanLeDevice(final boolean enable) {
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			new Thread(new Runnable() {
				@Override
				public void run() {
					mScanning = false;
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
				}
			}).start();

			new Thread(new Runnable() {
				@Override
				public void run() {
					mScanning = true;
					while (mScanning) {
						mBluetoothAdapter.startLeScan(mLeScanCallback);
						try {
							Thread.sleep(15000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
			}).start();
		} else {
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
	}

	// Device scan callback.
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, int rssi,
				byte[] scanRecord) {
			mLeDeviceList.add(device);
			String devicename = device.getName();
			String deviceaddress = device.getAddress();
			if (devicename.equals(DEVICENAME)) // �����豸����ƥ��
			{
				mScanning = false;
				mBluetoothAdapter.stopLeScan(mLeScanCallback);
				// �����ֻ� ���͵�handler����
				Message msg = bleconnect_handler.obtainMessage();
				msg.obj = deviceaddress;
				bleconnect_handler.sendMessage(msg);
			}
		}

	};
	// �����ֻ�
	Handler bleconnect_handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String deviceaddress = msg.obj.toString();
			connect(deviceaddress);
		}
	};

	// �����ֻ�
	public boolean connect(final String address) {
		if (mBluetoothAdapter == null || address == null) {
			Log.w(TAG,
					"BluetoothAdapter not initialized or unspecified address.");
			return false;
		}

		// Previously connected device. Try to reconnect.
		if (mBluetoothDeviceAddress != null
				&& address.equals(mBluetoothDeviceAddress)
				&& mBluetoothGatt != null) {
			Log.d(TAG,
					"Trying to use an existing mBluetoothGatt for connection.");
			if (mBluetoothGatt.connect()) {
				mConnectionState = STATE_CONNECTING;
				// return true;
			} else {
				return false;
			}
		}

		final BluetoothDevice device = mBluetoothAdapter
				.getRemoteDevice(address);
		if (device == null) {
			Log.w(TAG, "Device not found.  Unable to connect.");
			return false;
		}
		// We want to directly connect to the device, so we are setting the
		// autoConnect
		// parameter to false.
		// ע�������ֻ��ص�����
		mBluetoothGatt = device.connectGatt(this, false, mGattCallback);

		Log.d(TAG, "Trying to create a new connection.");
		mBluetoothDeviceAddress = address;
		mConnectionState = STATE_CONNECTING;
		return true;
	}

	// �����ֻ��ص�����
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {

			if (newState == BluetoothProfile.STATE_CONNECTED) {

				mConnectionState = STATE_CONNECTED;
				Log.i(TAG, "Connected to GATT server.");
				// Attempts to discover services after successful connection.
				Log.i(TAG, "Attempting to start service discovery:"
						+ mBluetoothGatt.discoverServices());

			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {

				mConnectionState = STATE_DISCONNECTED;
				Log.i(TAG, "Disconnected from GATT server.");
				Message msg = startScan_handler.obtainMessage();
				msg.obj = "reconnect";
				startScan_handler.sendMessage(msg);
			}
		}

		// ����service�ص�����
		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				List<BluetoothGattService> bluetoothCharacteristicList = mBluetoothGatt
						.getServices();
				// ������ȡ����GattService
				resolveGattServices(bluetoothCharacteristicList);
			} else {
				Log.w(TAG, "onServicesDiscovered received: " + status);
			}
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				// ��ʾ������������
				displayInputData(ACTION_DATA_AVAILABLE, characteristic);
			}
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			displayInputData(ACTION_DATA_AVAILABLE, characteristic);
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			Log.e(TAG, "onCharacteristicWrite " + gatt.getDevice().getName()
					+ " write " + characteristic.getUuid().toString() + " -> "
					+ new String(characteristic.getValue()));

		}

	};

	private ByteArrayOutputStream bos = new ByteArrayOutputStream();
	private static boolean readyToReceive = false; // �Ƿ�ʼ����

	// private static int temp=0;
	// ��ʾ������������

	private void displayInputData(final String action,
			final BluetoothGattCharacteristic characteristic) {
		final Intent intent = new Intent(action);

		// This is special handling for the Heart Rate Measurement profile. Data
		// parsing is
		// carried out as per profile specifications:
		// http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
		if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
			int flag = characteristic.getProperties();
			int format = -1;
			if ((flag & 0x01) != 0) {
				format = BluetoothGattCharacteristic.FORMAT_UINT16;
				Log.d(TAG, "Heart rate format UINT16.");
			} else {
				format = BluetoothGattCharacteristic.FORMAT_UINT8;
				Log.d(TAG, "Heart rate format UINT8.");
			}
			final int heartRate = characteristic.getIntValue(format, 1);
			Log.d(TAG, String.format("Received heart rate: %d", heartRate));
			intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
		} else {
			// For all other profiles, writes the data formatted in HEX.
			final byte[] data = characteristic.getValue();
			if (data != null && data.length > 0) {
				final StringBuilder stringBuilder = new StringBuilder(
						data.length);
				for (byte byteChar : data)
					stringBuilder.append(String.format("%02X ", byteChar));
				intent.putExtra(EXTRA_DATA, stringBuilder.toString() + "\n");
				sendBroadcast(intent);
				// String resultString = Base64.encode(data);

				String[] resultArray = stringBuilder.toString().split(" ");
				// ��ʾ��
				Message msg = display_handler.obtainMessage();

				System.out.println(resultArray[0]);
				System.out.println(resultArray[resultArray.length - 1]);
				System.out.println(resultArray.length);

				if (!readyToReceive) {
					if (resultArray[0].equals("42")
							&& resultArray[resultArray.length - 1].equals("45")
							&& resultArray.length == 20) {
						readyToReceive = true;
						String cmdString = "READY";
						write(cmdString);
						msg.obj = "��ʼ����..."; // ��ͷ��ȷ����ʼ����
						display_handler.sendMessage(msg);
					} else { // ��ͷ�����ط�
						String cmdString = "RESEND";
						intent.putExtra(EXTRA_DATA, cmdString + "\n");
						sendBroadcast(intent);
						write(cmdString);
					}
				} else {
					try {
						bos.write(data);

						if (bos.size() >= 704) { // У�黹û�� RESEND
							// д����
							String cmdString = "SUCESS"; // д������
							write(cmdString);
							intent.putExtra(EXTRA_DATA, cmdString + "\n");
							sendBroadcast(intent);
							// ��ʾ
							String tempString = bos.toString() + "\n�յ�"
									+ bos.size() + "k�ֽڡ� �ظ���ǰʱ��:" + cmdString;
							msg.obj = "���ܳɹ�!";
							System.out.println(tempString);
							display_handler.sendMessage(msg);
							readyToReceive = false;

							// save to database
							new Thread(new Runnable() {
								@Override
								public void run() {
									WaveDao dao = new WaveDao(getApplicationContext());
									dao.save(new WaveData(1, 2, "3"));
								}
							}).start();
						}
						System.out.println(bos.size());

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}
	
	// д����
	private void write(String cmdString) {
		final int charaProp = mWriteCharacteristic.getProperties();
		UUID uuid = mWriteCharacteristic.getUuid(); // ����
													// 0000ff01-0000-1000-8000-00805f9b34fb

		if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
			// ע��: ���¶�ȡ��ֵ ͨ�� BluetoothGattCallback#onCharacteristicRead() ��������
			mWriteCharacteristic.setValue(cmdString);
			writeCharacteristic(mWriteCharacteristic);
		}
	}

	// ��ʾ��
	Handler display_handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String displayString = msg.obj.toString();
			System.out.println(displayString);
			Toast.makeText(getBaseContext(), displayString, Toast.LENGTH_LONG)
					.show();
		}
	};

	// ������ȡ�ķ���
	private void resolveGattServices(List<BluetoothGattService> gattServices) {
		if (gattServices == null)
			return;
		String uuid = null;
		String unknownServiceString = getResources().getString(
				R.string.unknown_service);
		String unknownCharaString = getResources().getString(
				R.string.unknown_characteristic);
		ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
		ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
		mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

		// Loops through available GATT Services.
		for (BluetoothGattService gattService : gattServices) {
			HashMap<String, String> currentServiceData = new HashMap<String, String>();
			uuid = gattService.getUuid().toString();
			currentServiceData.put(LIST_NAME,
					SampleGattAttributes.lookup(uuid, unknownServiceString));
			currentServiceData.put(LIST_UUID, uuid);
			gattServiceData.add(currentServiceData);

			ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();
			List<BluetoothGattCharacteristic> gattCharacteristics = gattService
					.getCharacteristics();
			ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();

			// Loops through available Characteristics.
			for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
				charas.add(gattCharacteristic);
				HashMap<String, String> currentCharaData = new HashMap<String, String>();
				uuid = gattCharacteristic.getUuid().toString();
				currentCharaData.put(LIST_NAME,
						SampleGattAttributes.lookup(uuid, unknownCharaString));
				currentCharaData.put(LIST_UUID, uuid);
				gattCharacteristicGroupData.add(currentCharaData);
			}
			mGattCharacteristics.add(charas);
			gattCharacteristicData.add(gattCharacteristicGroupData);
		}

		// ��ȡĳ����Characteristic��ֵ
		getCharacteristic();

	}

	// ��ȡĳ����Characteristic��ֵ
	public boolean getCharacteristic() {
		if (mGattCharacteristics != null) {
			BluetoothGattCharacteristic characteristic = null;
			for (ArrayList<BluetoothGattCharacteristic> list : mGattCharacteristics) {
				for (BluetoothGattCharacteristic chara : list) {
					UUID uiid = chara.getUuid();
					if (uiid.equals(UUID_READ)) // �Զ���uiid
					{
						characteristic = chara;
						mWriteCharacteristic = chara;
					} else if (uiid.equals(UUID_WRITE)) {
						// mWriteCharacteristic=chara;
					}
				}
			}

			final int charaProp = characteristic.getProperties();
			if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
				// If there is an active notification on a characteristic, clear
				// it first so it doesn't update the data field on the user
				// interface.
				if (mNotifyCharacteristic != null) {
					this.setCharacteristicNotification(mNotifyCharacteristic,
							false);
					mNotifyCharacteristic = null;
				}
				this.readCharacteristic(characteristic);
			}
			if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
				mNotifyCharacteristic = characteristic;
				this.setCharacteristicNotification(characteristic, true);
			}
			return true;
		}
		return false;
	}

	/**
	 * Request a read on a given {@code BluetoothGattCharacteristic}. The read
	 * result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
	 * callback.
	 * 
	 * @param characteristic
	 *            The characteristic to read from.
	 */
	public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.readCharacteristic(characteristic); // onRead

	}

	/**
	 * Enables or disables notification on a give characteristic.
	 * 
	 * @param characteristic
	 *            Characteristic to act on.
	 * @param enabled
	 *            If true, enable notification. False otherwise.
	 */
	public void setCharacteristicNotification(
			BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

		// This is specific to Heart Rate Measurement.
		if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
			BluetoothGattDescriptor descriptor = characteristic
					.getDescriptor(UUID
							.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
			descriptor
					.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			mBluetoothGatt.writeDescriptor(descriptor);
		}
	}

	// д������ֵ
	public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		boolean status = mBluetoothGatt.writeCharacteristic(characteristic);
	}

}
