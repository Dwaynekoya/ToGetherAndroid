package com.example.together.dboperations;

import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;

import com.example.together.model.Habit;
import com.example.together.model.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TaskFetcher extends Thread {
    private final int userId;
    private final ArrayAdapter<Object> adapter;
    private final Handler handler;

    public TaskFetcher(int userId, ArrayAdapter<Object> adapter) {
        this.userId = userId;
        this.adapter = adapter;
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void run() {
        while (true) {
            fetchTasks();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void fetchTasks() {
        String json = DBTask.getTasks(userId, false);

        if (json == null) return;

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new SQLDateAdapter())
                .create();

        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        List<Task> fetchedTasks = gson.fromJson(jsonObject.getAsJsonArray("tasks"), new TypeToken<List<Task>>() {}.getType());
        List<Habit> fetchedHabits = gson.fromJson(jsonObject.getAsJsonArray("habits"), new TypeToken<List<Habit>>() {}.getType());

        Map<Integer, Habit> habitMap = new HashMap<>();
        for (Habit habit : fetchedHabits) {
            habitMap.put(habit.getId(), habit);
        }

        Iterator<Task> taskIterator = fetchedTasks.iterator();
        while (taskIterator.hasNext()) {
            Task task = taskIterator.next();
            if (habitMap.containsKey(task.getId())) {
                Habit habit = habitMap.get(task.getId());
                habit.setName(task.getName());
                habit.setDate(task.getDate());
                habit.setInfo(task.getInfo());
                habit.setFinished(task.isFinished());
                taskIterator.remove();
            }
        }

        List<Object> combinedList = new ArrayList<>();
        combinedList.addAll(fetchedTasks);
        combinedList.addAll(fetchedHabits);

        handler.post(() -> {
            adapter.clear();
            adapter.addAll(combinedList);
        });
    }
}
