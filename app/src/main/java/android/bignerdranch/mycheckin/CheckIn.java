package android.bignerdranch.mycheckin;

import java.util.Date;
import java.util.UUID;

public class CheckIn {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private String mPlace;
    private String mDetails;
    private double mLat;
    private double mLong;

    public CheckIn() {
        this(UUID.randomUUID());
    }

    public CheckIn(UUID id) {
        mId = id;
        mDate = new Date();
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    public double getLon() {
        return mLong;
    }

    public void setLon(double lon) {
        mLong = lon;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    public String getPlace() {
        return mPlace;
    }

    public void setPlace(String place) {
        mPlace = place;
    }

    public String getDetails() {
        return mDetails;
    }

    public void setDetails(String details) {
        mDetails = details;
    }
}
