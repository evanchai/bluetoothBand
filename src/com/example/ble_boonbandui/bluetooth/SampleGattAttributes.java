/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.ble_boonbandui.bluetooth;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for
 * demonstration purposes.
 */
public class SampleGattAttributes {
	private static HashMap<String, String> attributes = new HashMap();
	public static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
	public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
	public static String Device_Information_Service = "0000180a-0000-1000-8000-00805f9b34fb";
	public static String READ_String = "0000ff02-0000-1000-8000-00805f9b34fb";
	public static String WRITE_String = "0000ff01-0000-1000-8000-00805f9b34fb";
	public static String AMOMCU = "0000fff6-0000-1000-8000-00805f9b34fb";
	// public static String
	// Read_Character="6e400003-b5a3-f393-e0a9-e50e24dcca9e";
	// public static String
	// Write_Character="6e400002-b5a3-f393-e0a9-e50e24dcca9e";
	// public static String Band_Service="6e400001-b5a3-f393-e0a9-e50e24dcca9e";

	static {
		// Sample Services.
		attributes.put("0000180d-0000-1000-8000-00805f9b34fb",
				"Heart Rate Service");
		attributes.put("0000180a-0000-1000-8000-00805f9b34fb",
				"Device Information Service");
		// Sample Characteristics.
		attributes.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement");
		attributes.put("00002a29-0000-1000-8000-00805f9b34fb",
				"Manufacturer Name String");
		attributes.put(Device_Information_Service, "bluetoothgatt service");
		attributes.put(READ_String, "charactoristic");
		attributes.put(WRITE_String, "charactoristic");
		// attributes.put(Band_Service, "band service");
		// attributes.put(Read_Character, "read character");
		// attributes.put(Write_Character, "write character");
	}

	public static String lookup(String uuid, String defaultName) {
		String name = attributes.get(uuid);
		return name == null ? defaultName : name;
	}
}
