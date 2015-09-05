package com.example.ble_boombandui.dao;

import android.provider.BaseColumns;

public final class WaveReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public WaveReaderContract() {}
    /* Inner class that defines the table contents */
    public static abstract class WaveEntry implements BaseColumns {
        public static final String TABLE_NAME = "WaveData";
        public static final String COLUMN_NAME_WAVE_ID = "id";
        public static final String COLUMN_NAME_WAVE_TYPE = "type";
        public static final String COLUMN_NAME_WAVE_DATA = "data";
    }
}