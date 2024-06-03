package com.example.together.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Group implements Parcelable {
    private int id;
    private String name;
    private String description;
    private Set<User> members;
    private Set<Task> sharedTasks;
    private Set<Habit> sharedHabits;
    private User manager;


    /**
     * Constructor used when creating a new group
     * @param name name for the group to be searched by
     * @param description added by users to define the group
     */
    public Group(String name, String description){
        this.name=name;
        this.description=description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public Set<Task> getSharedTasks() {
        return sharedTasks;
    }

    public void setSharedTasks(Set<Task> sharedTasks) {
        this.sharedTasks = sharedTasks;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }
    public void addSharedTask(Task task){
        this.sharedTasks.add(task);
    }

    public Set<Habit> getSharedHabits() {
        return sharedHabits;
    }

    public void setSharedHabits(Set<Habit> sharedHabits) {
        this.sharedHabits = sharedHabits;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Compares groups based on IDs
     * @param o should be instance of Group. Object to be compared to this group
     * @return true if obj is a Group with this id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id == group.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Parcelable implementation
    protected Group(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        // Read lists from Parcel
        ArrayList<User> membersList = in.readArrayList(User.class.getClassLoader());
        ArrayList<Task> sharedTasksList = in.readArrayList(Task.class.getClassLoader());
        ArrayList<Habit> sharedHabitsList = in.readArrayList(Habit.class.getClassLoader());

        // Convert lists to sets
        members = new HashSet<>(membersList);
        sharedTasks = new HashSet<>(sharedTasksList);
        sharedHabits = new HashSet<>(sharedHabitsList);
        manager = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeList(new ArrayList<>(members));
        dest.writeList(new ArrayList<>(sharedTasks));
        dest.writeList(new ArrayList<>(sharedHabits));
        dest.writeParcelable(manager, flags);
    }


    @Override
    public int describeContents() {
        return 0;
    }
}
