package android.bignerdranch.mycheckin.database;

import android.bignerdranch.mycheckin.CheckIn;
import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

public class CheckInCursorWrapper extends CursorWrapper {
    public CheckInCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public CheckIn getCheckIn() {
        String uuidString = getString(getColumnIndex(CheckInDbSchema.CheckInTable.Cols.UUID));
        String title = getString(getColumnIndex(CheckInDbSchema.CheckInTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CheckInDbSchema.CheckInTable.Cols.DATE));
        String place = getString(getColumnIndex(CheckInDbSchema.CheckInTable.Cols.PLACE));
        String details = getString(getColumnIndex(CheckInDbSchema.CheckInTable.Cols.DETAILS));

        CheckIn checkIn = new CheckIn(UUID.fromString(uuidString));
        checkIn.setTitle(title);
        checkIn.setDate(new Date(date));
        checkIn.setPlace(place);
        checkIn.setDetails(details);

        return checkIn;
    }
}
