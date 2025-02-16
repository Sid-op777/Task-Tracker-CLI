package com.tasktracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Represents a task in the task manager CLI.
 * <p>
 * Each task has the following properties:
 * <ul>
 *   <li>{@code id}: A unique identifier for the task.</li>
 *   <li>{@code description}: A short description of the task.</li>
 *   <li>{@code status}: The current status of the task (todo, in-progress, done).</li>
 *   <li>{@code createdAt}: The timestamp when the task was created.</li>
 *   <li>{@code updatedAt}: The timestamp when the task was last updated.</li>
 * </ul>
 */

public class Task {
    private final String id;
    private String description;
    private Status status;
    private final String createdAt;
    private String updatedAt;

    /**
     * Constructs a new Task with the given id and description.
     * The status is set to {@code TODO} by default.
     *
     * @param description a short description of the task
     */
    public Task(String description) {
        this.id = generateID();
        this.description = description;
        this.status = Status.TODO;
        this.createdAt = getCurrentTime();
        this.updatedAt = this.createdAt;
    }

    /**
     * Constructs a new Task with the given id, description, and status.
     *
     * @param description a short description of the task
     * @param status      the initial status of the task
     * @throws IllegalArgumentException if the status is invalid
     */
    public Task(String description, String status) {
        this.id = generateID();
        this.description = description;
        this.status = Status.validateStatus(status);
        this.createdAt = getCurrentTime();
        this.updatedAt = this.createdAt;
    }

    public Task(String id, String description, String status,String createdAt, String updatedAt){
        this.id = id;
        this.description = description;
        this.status = Status.validateStatus(status);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private static String getCurrentTime() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(dateFormat);
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = getCurrentTime();
    }

    public String getStatus() {
        return status.toString();
    }

    public void setStatus(String status) {
        this.status = Status.validateStatus(status);
        this.updatedAt = getCurrentTime();
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    private static String generateID() {
        //maintain a json file for just hashes
        // or maybe build one during runtime
        // without it too though the chance of a collision is very low
        return UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Returns a string representation of the Task.
     *
     * @return a string containing all task properties
     */
    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
