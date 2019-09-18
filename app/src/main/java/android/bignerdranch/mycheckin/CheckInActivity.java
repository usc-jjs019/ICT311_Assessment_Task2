package android.bignerdranch.mycheckin;


import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.Intent;
import java.util.UUID;

public class CheckInActivity extends SingleFragmentActivity {

    private static final String EXTRA_CHECKIN_ID = "com.bignerdranch.android.mycheckin.checkin_id";

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CheckInActivity.class);
        intent.putExtra(EXTRA_CHECKIN_ID, crimeId);
        return intent;
    }


    @Override
    protected Fragment createFragment() {
        UUID checkInID = (UUID)getIntent().getSerializableExtra(EXTRA_CHECKIN_ID);
        return CheckInFragment.newInstance(checkInID);
    }

}
