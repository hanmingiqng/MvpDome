package com.han.mvpdome.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class Ceshi implements Parcelable {
    String name;
    int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.id);
    }

    public Ceshi(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public Ceshi() {
    }

    protected Ceshi(Parcel in) {
        this.name = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<Ceshi> CREATOR = new Parcelable.Creator<Ceshi>() {
        @Override
        public Ceshi createFromParcel(Parcel source) {
            return new Ceshi(source);
        }

        @Override
        public Ceshi[] newArray(int size) {
            return new Ceshi[size];
        }
    };
}
