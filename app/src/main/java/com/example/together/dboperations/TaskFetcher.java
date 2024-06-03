package com.example.together.dboperations;

import com.example.together.model.Habit;
import com.example.together.model.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.sql.Date;
import java.util.*;

public class TaskFetcher extends Thread {
    private final int userId;
    private List<Task> taskList;
    private List<Habit> habitList;

    /**
     * Constructor
     * @param userId from the current user
     * @param taskList from the tasklist view controller, observablelist
     * @param habitList from the tasklist view controller, observablelist
     */
    public TaskFetcher(int userId, List<Task> taskList, List<Habit> habitList) {
        this.userId = userId;
        this.taskList = taskList;
        this.habitList = habitList;
    }

    @Override
    public void run() {
       while (true){
           fetchTasks();
           try {
               Thread.sleep(1000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
    }

    /**
     * Takes tasks and habits from database, converts the json data to Java objects and stores them in the lists provided
     */
    private void fetchTasks() {
        String json = DBTask.getTasks(userId, false);

        if (json==null) return;

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new SQLDateAdapter()) //adapts mysql dates
                .registerTypeAdapter(boolean.class, new BooleanTypeAdapter()) //adapts mysql booleans (0,1) to java ones
                .create();
//        System.out.println(json);
        JsonObject jsonObject = JsonParser.parseString(json.toString()).getAsJsonObject();

        List<Task> fetchedTasks = gson.fromJson(jsonObject.getAsJsonArray("tasks"), new TypeToken<List<Task>>() {}.getType());
        List<Habit> fetchedHabits = gson.fromJson(jsonObject.getAsJsonArray("habits"), new TypeToken<List<Habit>>() {}.getType());

        //Manually checking which tasks are habits, so they can be shown in the habits list and not in the tasks
        Map<Integer, Habit> habitMap = new HashMap<>();
        for (Habit habit : fetchedHabits) {
            habitMap.put(habit.getId(), habit);
        }
        Iterator<Task> taskIterator = fetchedTasks.iterator();
        while (taskIterator.hasNext()) {
            Task task = taskIterator.next();
//            System.out.println(task.getName() + "  " + task.isFinished());
            if (habitMap.containsKey(task.getId())) {
                Habit habit = habitMap.get(task.getId());
                //System.out.println("Matching Task and Habit ID: " + task.getId());

                // Update habit with matching task attributes
                habit.setName(task.getName());
                habit.setDate(task.getDate());
                habit.setInfo(task.getInfo());
                habit.setFinished(task.isFinished());

                taskIterator.remove();
            }
        }

            if (!taskList.equals(fetchedTasks)) {
                taskList = fetchedTasks;
            }

            if (!habitList.equals(fetchedHabits)) {
                habitList = fetchedHabits;
            }
    }
}
