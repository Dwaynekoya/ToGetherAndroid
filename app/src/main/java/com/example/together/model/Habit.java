package com.example.together.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

public class Habit extends Task implements Parcelable {
    @SerializedName("task_id")
    private int id;
    private int repetition;

    /**
     * Constructor used to create a habit from scratch
     * @param id -> identifier
     * @param name -> Name for the task
     * @param date -> Date it should get completed in
     * @param info -> additional information
     * @param finished -> has this been completed?
     * @param image -> url to an image (added when task is completed)
     * @param  repetition-> number of days between each reocurrance of this task
     */

    public Habit(int id, String name, Date date, String info, boolean finished, String image, int repetition) {
        super(id, name, date, info, finished, image);
        this.setId(id);
        this.repetition = repetition;
    }

    /**
     * Constructor used for converting a given task into a habit
     * @param task -> task to get attributes from
     * @param repetition -> value that turns task to HABIT
     */
    public Habit(Task task, int repetition){
        super(task.getId(),task.getName(),task.getDate(), task.getInfo(), task.isFinished(), task.getImage());
        this.setId(task.getId()); //need to force this or else it gets assigned 0
        this.repetition = repetition;
    }

    /**
     * Used when completing a previous iteration of the habit
     * @param habit old iteration
     * @param habitDate new date to complete the habit by then
     */
    public Habit(Habit habit, Date habitDate) {
        super(habit.getId(), habit.getName(), habitDate, habit.getInfo(),false, habit.getImage());
        this.repetition = habit.getRepetition();
    }

    public int getRepetition() {
        return repetition;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
    // Parcelable implementation
    protected Habit(Parcel in) {
        super(in);
        repetition = in.readInt();
    }

    public static final Creator<Habit> CREATOR = new Creator<Habit>() {
        @Override
        public Habit createFromParcel(Parcel in) {
            return new Habit(in);
        }

        @Override
        public Habit[] newArray(int size) {
            return new Habit[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(repetition);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
