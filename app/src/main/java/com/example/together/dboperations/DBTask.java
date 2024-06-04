package com.example.together.dboperations;

import com.example.together.Utils;
import com.example.together.model.Habit;
import com.example.together.model.Task;

import java.io.*;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class DBTask {
    /**
     * Puts a new task in the tasks table (and, if it´s a habit, also in the habits one)
     * @param task new task to add to the table
     */
    public static void addTask(Task task) {
        try {
            URL url = new URL(Constants.addTask);
            StringBuilder postData = new StringBuilder();
            postData.append("name=").append(task.getName());
            if (task.getDate() != null) postData.append("&date=").append(task.getDate());
            if (task.getInfo() != null) postData.append("&info=").append(task.getInfo());

            //CHECK IF IT'S A GROUP TASK OR A USER TASK
            if (Utils.groupNewTask != null){
                postData.append("&group_id=").append(Utils.groupNewTask.getId());
                Utils.groupNewTask = null; //removes the selected group so that a new task, by default, is a user task
            }else { //if there is no group saved in that variable, the task is a personal one.
                postData.append("&user_id=").append(Utils.loggedInUser.getId());
            }


            if (task instanceof Habit) postData.append("&repeat=").append(((Habit) task).getRepetition());
            //postData.append("&repeat=").append(repeat != null ? repeat : "null"); // Include repeat field

            String response = new DBGeneral.PostTask().execute(url, postData.toString()).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates task in DB
     * @param task to update
     */
    public static void updateTask(Task task) {
        System.out.println("Updating task with id " + task.getId());
        try {
            URL url = new URL(Constants.updateTasks);
            StringBuilder postData = new StringBuilder();
            postData.append("task_id=").append(task.getId())
                    .append("&name=").append(task.getName())
                    .append("&date=").append(task.getDate() != null ? task.getDate().toString() : "")
                    .append("&info=").append(task.getInfo());
            if (task instanceof Habit){
                //System.out.println("REPEAT: " + ((Habit) task).getRepetition());
                postData.append("&repeat=").append(((Habit) task).getRepetition());
            } /*else {
                postData.append("&repeat=").append("");
            }*/
            String response = new DBGeneral.PostTask().execute(url, postData).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes task by its id
     * @param task Task to delete
     */
    public static void deleteTask(Task task) {
        int id = task.getId();
        System.out.println("Deleting task with id " + id);
        try {
            URL url = new URL(Constants.deleteTask);
            String postData = "id=" + id;
            String response = new DBGeneral.PostTask().execute(url, postData).get();
            System.out.println("Response when deleting: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets task as finished and adds the url for the assigned image
     * @param task to set as finished
     */
    public static void finishTask(Task task){
        try {
            URL url = new URL(Constants.finishTask);
            String postdata = String.format("task_id=%d&image=%s&finished=%s", task.getId(), task.getImage(), true);
            String response = new DBGeneral.PostTask().execute(url, postdata).get();
            System.out.printf("Response when finishing task with id $d: $s $n", task.getId(), response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all tasks from a given user from the database
     * @param userId current user identifying int
     * @param following if tasks are from a user that´s followed by the current user
     * @return StringBuilder json containing all tasks
     */
    public static String getTasks(int userId, boolean following) {
        try {
            URL url = new URL(Constants.getTasksFromUser + "?id=" + userId + "&following=" + following);
            return  new DBGeneral.GetTask().execute(url).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Deletes all tasks owned by the current user
     */
    public static void deleteAllTasks() {
        try {
            URL url = new URL(Constants.deleteAllTasks);
            String postdata = String.format("id=%d", Utils.loggedInUser.getId());
            String response = new DBGeneral.PostTask().execute(url, postdata).get();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
