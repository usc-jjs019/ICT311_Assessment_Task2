package android.bignerdranch.mycheckin;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CheckInList {
    private static CheckInList sCheckInList;
    private Context mAppContext;
    private List<CheckIn> mCheckIns;

    private CheckInList(Context context) {
        mAppContext = context;
        mCheckIns = new ArrayList<CheckIn>();
    }

    public static CheckInList get(Context context) {
        if (sCheckInList == null) {
            sCheckInList = new CheckInList(context);
        }
        return sCheckInList;
    }

    public void addCheckIn(CheckIn c) {
        mCheckIns.add(c);
    }

    public List<CheckIn> getCheckIns() {
        return mCheckIns;
    }

    public CheckIn getCheckIn(UUID id) {
        for (CheckIn checkIn : mCheckIns) {
            if (checkIn.getId().equals(id))
                return checkIn;
        }
        return null;
    }
}
