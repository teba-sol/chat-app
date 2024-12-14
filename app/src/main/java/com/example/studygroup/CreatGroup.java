package com.example.studygroup;

import android.os.Parcel;
import android.os.Parcelable;

public class CreatGroup implements Parcelable {
    private String groupId;
    private String groupName;
    private String groupFocus;
    private String groupAdmin;

    // Empty constructor for Firebase
    public CreatGroup() {}

    // Constructor with parameters
    public CreatGroup(String groupId, String groupName, String groupFocus, String groupAdmin) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupFocus = groupFocus;
        this.groupAdmin = groupAdmin;
    }

    // Constructor without groupId (useful when creating new groups in Firebase)
    public CreatGroup(String groupName, String groupFocus, String groupAdmin) {
        this.groupName = groupName;
        this.groupFocus = groupFocus;
        this.groupAdmin = groupAdmin;
    }

    protected CreatGroup(Parcel in) {
        groupId = in.readString();
        groupName = in.readString();
        groupFocus = in.readString();
        groupAdmin = in.readString();
    }

    public static final Creator<CreatGroup> CREATOR = new Creator<CreatGroup>() {
        @Override
        public CreatGroup createFromParcel(Parcel in) {
            return new CreatGroup(in);
        }

        @Override
        public CreatGroup[] newArray(int size) {
            return new CreatGroup[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(groupId);
        dest.writeString(groupName);
        dest.writeString(groupFocus);
        dest.writeString(groupAdmin);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupFocus() {
        return groupFocus;
    }

    public String getGroupAdmin() {
        return groupAdmin;
    }
}
