package com.example.together.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User implements Parcelable {
    private int id;
    private String username;
    private String password;
    private String bio;
    private String icon;
    private Set<User> following; //people this user follows
    private Set<User> followers; //people who follow this user
    private Set<Task> tasks;
    private Set<Group> groups;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<User> getFollowing() {
        return following;
    }

    public void setFollowing(Set<User> following) {
        this.following = following;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return username;
    }

    /**
     * Compares users based on IDs
     * @param obj -> should be instaceof User. Object to be compared.
     * @return true if obj is a User object AND ids match
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) return false;
        return this.id==((User) obj).id;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    // Parcelable implementation
    protected User(Parcel in) {
        id = in.readInt();
        username = in.readString();
        password = in.readString();
        bio = in.readString();
        icon = in.readString();
        following = new HashSet<>();
        in.readList(new ArrayList<>(following), User.class.getClassLoader());
        followers = new HashSet<>();
        in.readList(new ArrayList<>(followers), User.class.getClassLoader());
        tasks = new HashSet<>();
        in.readList(new ArrayList<>(tasks), Task.class.getClassLoader());
        groups = new HashSet<>();
        in.readList(new ArrayList<>(groups), Group.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(bio);
        dest.writeString(icon);
        dest.writeList(new ArrayList<>(following));
        dest.writeList(new ArrayList<>(followers));
        dest.writeList(new ArrayList<>(tasks));
        dest.writeList(new ArrayList<>(groups));
    }
}
