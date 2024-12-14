package com.example.studygroup;

import java.util.Map;

public class Group {
    private String groupAdmin;
    private String groupName;
    private String groupFocus;
    private Map<String, String> members;

    public Group() {
        // Default constructor required for calls to DataSnapshot.getValue(Group.class)
    }

    public Group(String groupAdmin, String groupName, String groupFocus) {
        this.groupAdmin = groupAdmin;
        this.groupName = groupName;
        this.groupFocus = groupFocus;
    }

    public String getGroupAdmin() {
        return groupAdmin;
    }

    public void setGroupAdmin(String groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupFocus() {
        return groupFocus;
    }

    public void setGroupFocus(String groupFocus) {
        this.groupFocus = groupFocus;
    }

    public Map<String, String> getMembers() {
        return members;
    }

    public void setMembers(Map<String, String> members) {
        this.members = members;
    }
}
