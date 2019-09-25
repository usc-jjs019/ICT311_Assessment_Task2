package android.bignerdranch.mycheckin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CheckInListFragment extends Fragment {
    private RecyclerView mCheckInRecyclerView;
    private CheckInAdapter mAdapter;
    private boolean mSubtitleVisible;

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkin_list, container, false);

        mCheckInRecyclerView = (RecyclerView)view.findViewById(R.id.checkin_recycler_view);
        mCheckInRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_checkin_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item ) {
        switch (item.getItemId()) {
            case R.id.new_checkin:
                CheckIn checkIn = new CheckIn();
                CheckInList.get(getActivity()).addCheckIn(checkIn);
                Intent intent = CheckInActivity.newIntent(getActivity(), checkIn.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        CheckInList checkInList = CheckInList.get(getActivity());
        int checkInCount = checkInList.getCheckIns().size();
        String subtitle = getString(R.string.subtitle_format, checkInCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        CheckInList checkInList = CheckInList.get(getActivity());
        List<CheckIn> checkIns = checkInList.getCheckIns();

        if (mAdapter == null) {
            mAdapter = new CheckInAdapter(checkIns);
            mCheckInRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();

        mAdapter = new CheckInAdapter(checkIns);
        mCheckInRecyclerView.setAdapter(mAdapter);
    }

    private class CheckInHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mDetailsTextView;
        private CheckIn mCheckIn;

        public CheckInHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_checkin, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView)itemView.findViewById(R.id.checkin_title);
            mDateTextView = (TextView)itemView.findViewById(R.id.checkin_date);
            mDetailsTextView = (TextView)itemView.findViewById(R.id.checkin_detail);
        }

        @Override
        public void onClick(View view) {
            Intent intent = CheckInActivity.newIntent(getActivity(), mCheckIn.getId());
            startActivity(intent);
        }

        public void bind(CheckIn checkIn) {
            mCheckIn = checkIn;
            mTitleTextView.setText(mCheckIn.getTitle());
            mDateTextView.setText(mCheckIn.getDate().toString());
        }
    }

    private class CheckInAdapter extends RecyclerView.Adapter<CheckInHolder> {
        private List<CheckIn> mCheckIns;

        public CheckInAdapter(List<CheckIn> checkIns) {
            mCheckIns = checkIns;
        }

        @Override
        public CheckInHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new CheckInHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CheckInHolder holder, int position) {
            CheckIn checkIn = mCheckIns.get(position);
            holder.bind(checkIn);
        }

        @Override
        public int getItemCount() {
            return mCheckIns.size();
        }
    }
}
