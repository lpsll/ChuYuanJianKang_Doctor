package com.htlc.cykf.model;

/**
 * Created by sks on 2016/1/29.
 */
public class ContactBean implements Comparable<ContactBean> {
    public String userid;
    public String name;
    public String head;
    public String photo;
    public String group;

    @Override
    public int compareTo(ContactBean another) {
        return this.head.compareTo(another.head);
    }
}
