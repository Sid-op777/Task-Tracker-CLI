package com.tasktracker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Manages a collection of tasks, providing functionalities to add, update, delete,
 * and list tasks.  Tasks are persisted to a JSON file.
 * <p>
 * This class handles loading and saving tasks to a file named {@code tasks.json}.
 * The file is created if it doesn't exist.  It utilizes the {@link Task} class
 * to represent individual tasks.
 * </p>
 */
public class TaskManager {

    private static final String TASKS_FILE = "tasks.json";

    /**
     * Constructs a new TaskManager.  Initializes the tasks file if it doesn't exist.
     * If the file doesn't exist, it's created and initialized with an empty list of tasks.
     *
     * @throws RuntimeException if an IOException occurs while creating the file.
     */
    public TaskManager() {
        File tasksFile = new File(TASKS_FILE);
        if (!tasksFile.exists()) {
            try {
                if (tasksFile.createNewFile()) {
                    writeTasks(new ArrayList<>()); // Initialize with an empty list
                } else {
                    throw new IOException("Failed to create the tasks file.");
                }
            } catch (IOException e) {
                throw new RuntimeException("Error initializing TaskManager: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Loads tasks from the {@code tasks.json} file.
     *
     * <p>
     * Reads the contents of the JSON file, parses it into a JSONArray, and
     * creates {@link Task} objects from each JSON object. Handles potential
     * exceptions during file reading or JSON parsing.
     * </p>
     *
     * @return A list of {@link Task} objects loaded from the file.  Returns an empty list
     * if the file is empty or if an error occurs during loading.
     */
    private List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        try {
            String content = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(TASKS_FILE)));
            if (content.trim().isEmpty()) {
                return tasks; // Handle empty file
            }
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject taskJson = jsonArray.getJSONObject(i);
                Task task = new Task(
                        taskJson.getString("id"),
                        taskJson.getString("description"),
                        taskJson.getString("status"),
                        taskJson.getString("createdAt"),
                        taskJson.getString("updatedAt")
                );
                tasks.add(task);
            }
        } catch (Exception e) {
            System.err.println("Error loading tasks: " + e.getMessage()); // Log the error instead of printing to console.
            e.printStackTrace(); // Print stack trace for debugging purposes
        }
        return tasks;
    }

    /**
     * Writes the given list of tasks to the {@code tasks.json} file.
     *
     * <p>
     * Converts each {@link Task} object to a JSONObject and then writes the
     * resulting JSONArray to the file. Handles potential exceptions during file writing.
     * </p>
     *
     * @param tasks The list of {@link Task} objects to write to the file.  If tasks is null,
     *              nothing will be written to the file.
     * @throws RuntimeException if an IOException occurs while writing to the file.
     */
    private void writeTasks(List<Task> tasks) {
        if (tasks == null) {
            return;  // Handle null tasks list gracefully.  Perhaps log a warning here.
        }
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
            throw new RuntimeException("Error writing tasks to file: " + e.getMessage(), e); // Re-throw as RuntimeException.
        }
    }

    /**
     * Adds a new task to the task list and saves it to the file.
     *
     * @param task The {@link Task} object to add.  Must not be null.
     * @throws IllegalArgumentException if task is null.
     */
    public void addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null.");
        }
        List<Task> tasks = loadTasks();
        tasks.add(task);
        writeTasks(tasks);
    }

    /**
     * Updates the description of a task with the given ID.
     *
     * @param id          The ID of the task to update. Must not be null or empty.
     * @param description The new description for the task.  Must not be null or empty.
     * @throws IllegalArgumentException if id or description is null or empty.
     */
    public void updateTaskDescp(String id, String description) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Task ID cannot be null or empty.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }

        List<Task> tasks = loadTasks();
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                task.setDescription(description);
                writeTasks(tasks);
                System.out.println("Task description updated successfully (ID: " + id + ")");
                return;
            }
        }
        System.out.println("Task id: " + id + " not found");
    }

    /**
     * Updates the status of a task with the given ID.
     *
     * @param id     The ID of the task to update. Must not be null or empty.
     * @param status The new status for the task.  Must be a valid status string
     *               (e.g., "TODO", "IN_PROGRESS", "DONE").
     * @throws IllegalArgumentException if id is null or empty, or if the status is invalid.
     * @see Status#validateStatus(String)
     */
    public void updateTaskStatus(String id, String status) throws IllegalArgumentException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Task ID cannot be null or empty.");
        }
        List<Task> tasks = loadTasks();
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                task.setStatus(status);
                writeTasks(tasks);
                System.out.println("Task status updated successfully (ID: " + id + ")");
                return;
            }
        }
        System.out.println("Task id: " + id + " not found");
    }

    /**
     * Updates both the description and status of a task with the given ID.
     *
     * @param id          The ID of the task to update. Must not be null or empty.
     * @param description The new description for the task. Must not be null or empty.
     * @param status      The new status for the task. Must be a valid status string
     *               (e.g., "TODO", "IN_PROGRESS", "DONE").
     * @throws IllegalArgumentException if id or description is null or empty, or if the status is invalid.
     * @see Status#validateStatus(String)
     */
    public void updateTaskDecpStatus(String id, String description, String status) throws IllegalArgumentException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Task ID cannot be null or empty.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }

        List<Task> tasks = loadTasks();
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                task.setDescription(description);
                task.setStatus(status);
                writeTasks(tasks);
                System.out.println("Task description and status updated successfully (ID: " + id + ")");
                return;
            }
        }
        System.out.println("Task id: " + id + " not found");
    }

    /**
     * Deletes a task with the given ID.
     *
     * @param id The ID of the task to delete. Must not be null or empty.
     * @throws IllegalArgumentException if id is null or empty.
     */
    public void deleteTask(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Task ID cannot be null or empty.");
        }
        List<Task> tasks = loadTasks();
        if (tasks.removeIf(task -> task.getId().equals(id))) {
            writeTasks(tasks);
            System.out.println("Task deleted successfully (ID: " + id + ")");
        } else {
            System.out.println("Task id: " + id + " not found");
        }
    }

    /**
     * Marks a task as done by setting its status to the specified status.
     *
     * @param id     The ID of the task to mark as done. Must not be null or empty.
     * @param status The {@link Status} to set for the task.
     * @throws IllegalArgumentException if id is null or empty.
     */
    public void markTaskAs(String id, Status status) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Task ID cannot be null or empty.");
        }
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

    /**
     * Lists all tasks.
     *
     * @return A list of all {@link Task} objects.  Returns an empty list if no tasks exist.
     */
    public List<Task> listTasks() {
        return loadTasks();
    }
}