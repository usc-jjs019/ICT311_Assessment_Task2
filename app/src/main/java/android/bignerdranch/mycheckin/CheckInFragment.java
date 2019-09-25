package android.bignerdranch.mycheckin;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CheckInFragment extends Fragment {

    private static final String ARG_CHECKIN_ID = "checkIn_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 1;

    private CheckIn mCheckIn;
    private EditText mTitleField;
    private EditText mPlaceField;
    private EditText mDetailsField;
    private Button mDateButton;
    private File mPhotoFile;
    private TextView mLocationView;
    private Button mMapButton;
    private Button mShareButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Button mDeleteButton;

    private GoogleApiClient mClient;


    public static CheckInFragment newInstance(UUID checkInId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CHECKIN_ID, checkInId);

        CheckInFragment fragment = new CheckInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID checkInID = (UUID) getArguments().getSerializable(ARG_CHECKIN_ID);
        mCheckIn = CheckInList.get(getActivity()).getCheckIn(checkInID);
        mPhotoFile = CheckInList.get(getActivity()).getPhotoFile(mCheckIn);
        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        LocationRequest request = LocationRequest.create();
                        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        request.setNumUpdates(1);
                        request.setInterval(0);

                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        LocationServices.getFusedLocationProviderClient(getActivity())
                                .requestLocationUpdates(request, new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        Location location = locationResult.getLastLocation();
                                        setLocation(location);
                                    }
                                }, null);
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                    }
                })
                .build();
    }

    private void setLocation(Location location) {
        mCheckIn.setLat(location.getLatitude());
        mCheckIn.setLon(location.getLongitude());
        mLocationView.setText("latitude: " + mCheckIn.getLat() + " longtitude: " + mCheckIn.getLon());
    }

    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();

        CheckInList.get(getActivity()).updateCheckIn(mCheckIn);
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_checkin, parent, false);
        mTitleField = (EditText) v.findViewById(R.id.checkin_title);
        mTitleField.setText(mCheckIn.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mCheckIn.setTitle(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
            }

            public void afterTextChanged(Editable c) {
            }
        });

        mPlaceField = (EditText) v.findViewById(R.id.checkin_place);
        mPlaceField.setText(mCheckIn.getPlace());
        mPlaceField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mCheckIn.setPlace(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
            }

            public void afterTextChanged(Editable c) {
            }
        });

        mDetailsField = (EditText) v.findViewById(R.id.checkin_details);
        mDetailsField.setText(mCheckIn.getDetails());
        mDetailsField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mCheckIn.setDetails(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
            }

            public void afterTextChanged(Editable c) {
            }
        });

        mDateButton = (Button)v.findViewById(R.id.checkin_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCheckIn.getDate());
                dialog.setTargetFragment(CheckInFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mLocationView = (TextView) v.findViewById(R.id.checkin_location_label);
        String coordinates = ("latitude: " + mCheckIn.getLat() + " longtitude: " + mCheckIn.getLon());
        mLocationView.setText(coordinates);

        mMapButton = (Button) v.findViewById(R.id.checkin_location);
        mMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("latitude", mCheckIn.getLat());
                intent.putExtra("longitude", mCheckIn.getLon());
                startActivity(intent);
            }
        });

        mShareButton = (Button) v.findViewById(R.id.checkin_report);
        mShareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getShareMessage());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.checkin_report_subject));
                i = Intent.createChooser(i, getString(R.string.share_message));
                startActivity(i);
            }
        });

        mDeleteButton = (Button) v.findViewById(R.id.checkin_delete);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInList.get(getActivity()).deleteCheckIn(mCheckIn);

                getActivity().finish();
            }
        });

        PackageManager packageManager = getActivity().getPackageManager();

        mPhotoButton = (ImageButton) v.findViewById(R.id.checkin_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.bignerdranch.android.criminalintent.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.checkin_photo);
        updatePhotoView();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCheckIn.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.bignerdranch.android.criminalintent.fileprovider",
                    mPhotoFile);

            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
        }
    }

    private void updateDate() {
        mDateButton.setText(mCheckIn.getDate().toString());
    }

    private String getShareMessage() {
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCheckIn.getDate()).toString();

        String placeString = mCheckIn.getPlace();

        String report = getString(R.string.checkin_report, mCheckIn.getTitle(), dateString, placeString);

        return report;
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}
