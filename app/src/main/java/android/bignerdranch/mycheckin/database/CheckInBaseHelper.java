package android.bignerdranch.mycheckin.database;

import android.bignerdranch.mycheckin.database.CheckInDbSchema.CheckInTable;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CheckInBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "checkInBase.db";

    public CheckInBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CheckInTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CheckInTable.Cols.UUID + ", " +
                CheckInTable.Cols.TITLE + ", " +
                CheckInTable.Cols.DATE + ", " +
                CheckInTable.Cols.PLACE + ", " +
                CheckInTable.Cols.DETAILS + ", " +
                CheckInTable.Cols.LATITUDE + ", " +
                CheckInTable.Cols.LONGITUDE + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
