package com.tasktracker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;


public class TaskManager {
    private static final String TASKS_FILE = "tasks.json";

    public TaskManager() {
        if(!new File(TASKS_FILE).exists()){
            try {
                new File(TASKS_FILE).createNewFile();
                writeTasks(new ArrayList<>());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        try {
            String content = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(TASKS_FILE)));
//            System.out.println(content);
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject taskJson = jsonArray.getJSONObject(i);
                Task task = new Task(taskJson.getString("id"), taskJson.getString("description"), taskJson.getString("status"),taskJson.getString("createdAt"),taskJson.getString("updatedAt"));
                tasks.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // Method to write tasks to tasks.json
    private void writeTasks(List<Task> tasks) {
        JSONArray jsonArray = new JSONArray();
        for (Task task : tasks) {
            JSONObject taskJson = new JSONObject();
            taskJson.put("id", task.getId());
            taskJson.put("description", task.getDescription());
            taskJson.put("status", task.getStatus());
            taskJson.put("createdAt", task.getCreatedAt());
            taskJson.put("updatedAt", task.getUpdatedAt());
            jsonArray.put(taskJson);
        }
        try (FileWriter file = new FileWriter(TASKS_FILE)) {
            file.write(jsonArray.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add a new task
    public void addTask(Task task) {
        List<Task> tasks = loadTasks();
        tasks.add(task);
        writeTasks(tasks);
    }

    // Update a task
    public void updateTask(String id, String description) {
        List<Task> tasks = loadTasks();
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                task.setDescription(description);
                task.setStatus(Status.TODO.toString());  // Reset status when updated
                writeTasks(tasks);
                System.out.println("Task updated successfully (ID: " + id + ")");
                return;
            }
        }
        System.out.println("Task id: " + id + " not found");
    }

    // Delete a task
    public void deleteTask(String id) {
        List<Task> tasks = loadTasks();
        if(tasks.removeIf(task -> task.getId().equals(id))){
            writeTasks(tasks);
            System.out.println("Task deleted successfully (ID: " + id + ")");
        }
        else {
            System.out.println("Task id: " + id + " not found");
        }
    }

    // Mark a task as done
    public void markTaskAs(String id, Status status) {
        List<Task> tasks = loadTasks();
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                task.setStatus(status.toString());
                writeTasks(tasks);
                System.out.println("Task marked as Done (ID: " + id + ")");
                return;
            }
        }
        System.out.println("Task id: " + id + " not found");
    }

    // List all tasks
    public List<Task> listTasks() {
        return loadTasks();
    }
}
