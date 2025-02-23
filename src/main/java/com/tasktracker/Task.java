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
     * Constructs a new Task with the given description.
     * The status is set to {@link Status#TODO} by default.
     * The ID and creation timestamp are automatically generated.
     *
     * @param description A short description of the task.  Should not be null or empty.
     * @throws IllegalArgumentException if the description is null or empty.
     */
    public Task(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        this.id = generateID();
        this.description = description;
        this.status = Status.TODO;
        this.createdAt = getCurrentTime();
        this.updatedAt = this.createdAt;
    }

    /**
     * Constructs a new Task with the given description and status.
     * The ID and creation timestamp are automatically generated.
     *
     * @param description A short description of the task. Should not be null or empty.
     * @param status      The initial status of the task.  Must be a valid status string
     *                    (e.g., "TODO", "IN_PROGRESS", "DONE").
     * @throws IllegalArgumentException if the description is null or empty, or if the status is invalid.
     * @see Status#validateStatus(String)
     */
    public Task(String description, String status) throws IllegalArgumentException {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        this.id = generateID();
        this.description = description;
        this.status = Status.validateStatus(status);
        this.createdAt = getCurrentTime();
        this.updatedAt = this.createdAt;
    }

    /**
     * Constructs a new Task with the given ID, description, status, creation timestamp, and update timestamp.
     * This constructor is primarily intended for reconstructing a Task from persistent storage.
     *
     * @param id          The unique identifier for the task.
     * @param description A short description of the task. Should not be null or empty.
     * @param status      The status of the task. Must be a valid status string (e.g., "TODO", "IN_PROGRESS", "DONE").
     * @param createdAt   The timestamp when the task was created, in the format "yyyy-MM-dd HH:mm:ss".
     * @param updatedAt   The timestamp when the task was last updated, in the format "yyyy-MM-dd HH:mm:ss".
     * @throws IllegalArgumentException if the description is null or empty, or if the status is invalid.
     * @see Status#validateStatus(String)
     */
    public Task(String id, String description, String status, String createdAt, String updatedAt) throws IllegalArgumentException {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        this.id = id;
        this.description = description;
        this.status = Status.validateStatus(status);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Gets the current timestamp in the format "yyyy-MM-dd HH:mm:ss".
     *
     * @return The current timestamp as a string.
     */
    private static String getCurrentTime() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(dateFormat);
    }

    /**
     * Gets the unique identifier of the task.
     *
     * @return The task ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the description of the task.
     *
     * @return The task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the task.
     * The {@code updatedAt} timestamp is updated to the current time.
     *
     * @param description The new description for the task.  Should not be null or empty.
     * @throws IllegalArgumentException if the description is null or empty.
     */
    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        this.description = description;
        this.updatedAt = getCurrentTime();
    }

    /**
     * Gets the status of the task as a string.
     *
     * @return The task status as a string (e.g., "TODO", "IN_PROGRESS", "DONE").
     */
    public String getStatus() {
        return status.toString();
    }

    /**
     * Sets the status of the task.
     * The {@code updatedAt} timestamp is updated to the current time.
     *
     * @param status The new status for the task.  Must be a valid status string
     *               (e.g., "TODO", "IN_PROGRESS", "DONE").
     * @throws IllegalArgumentException if the status is invalid.
     * @see Status#validateStatus(String)
     */
    public void setStatus(String status) throws IllegalArgumentException {
        this.status = Status.validateStatus(status);
        this.updatedAt = getCurrentTime();
    }

    /**
     * Gets the creation timestamp of the task.
     *
     * @return The timestamp when the task was created, in the format "yyyy-MM-dd HH:mm:ss".
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets the timestamp when the task was last updated.
     *
     * @return The timestamp when the task was last updated, in the format "yyyy-MM-dd HH:mm:ss".
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Generates a unique ID for the task.
     *
     * <p>
     *  This method uses {@link UUID#randomUUID()} to generate a random UUID and then extracts the first 8 characters.
     *  While the possibility of collision is low, it is not guaranteed to be perfectly unique in all cases.
     * </p>
     *
     * @return A unique identifier for the task.
     */
    private static String generateID() {
        //maintain a json file for just hashes
        // or maybe build one during runtime
        // without it too though the chance of a collision is very low
        return UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Returns a string representation of the Task.
     * This is primarily for debugging purposes. The format is subject to change.
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
