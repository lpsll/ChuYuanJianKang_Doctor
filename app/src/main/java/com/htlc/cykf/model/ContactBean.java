package com.htlc.cykf.model;

/**
 * Created by sks on 2016/1/29.
 */
public class ContactBean implements Comparable<ContactBean>{
    public String userId;
    public String userName;
    public String group;
    public String userPhoto;
    @Override
    public int compareTo(ContactBean another) {
        return this.group.compareTo(another.group);
    }

}
