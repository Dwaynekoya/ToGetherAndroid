package com.example.together.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.sql.Date;

public class Task implements Parcelable {
    private int id;
    private String name;
    private Date date;
    private String info;
    private boolean finished;
    private String image; //image URL
    private User owner;
    /**
     * Constructor used to create a task from scratch
     * @param id -> identifier
     * @param name -> Name for the task
     * @param date -> Date it should get completed in
     * @param info -> additional information
     * @param finished -> has this been completed?
     * @param image -> url to an image (added when task is completed)
     */
    public Task(int id, String name, Date date, String info, boolean finished, String image) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.info = info;
        this.finished = finished;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    // Parcelable implementation
    protected Task(Parcel in) {
        id = in.readInt();
        name = in.readString();
        long dateMillis = in.readLong();
        date = new Date(dateMillis);
        info = in.readString();
        finished = in.readByte() != 0;
        image = in.readString();
        owner = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeLong(date.getTime());
        dest.writeString(info);
        dest.writeByte((byte) (finished ? 1 : 0));
        dest.writeString(image);
        dest.writeParcelable(owner, flags);
    }
}
