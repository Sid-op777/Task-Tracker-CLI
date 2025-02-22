package com.tasktracker;

/**
 * Represents the status of a task in the task manager CLI.
 * <p>
 * This enum defines three possible states for a task:
 * <ul>
 *   <li>{@code TODO}: The task is yet to be started</li>
 *   <li>{@code IN_PROGRESS}: The task is currently being worked on</li>
 *   <li>{@code DONE}: The task has been completed</li>
 * </ul>
 * <p>
 * It also provides a method to validate and convert string input to a {@code Status} enum.
 */

//work on this
public enum Status {
    TODO,
    IN_PROGRESS,
    DONE;

    /**
     * Validates and converts a string to a {@code Status} enum.
     *
     * @param status the string representation of the status
     * @return the corresponding {@code Status} enum value
     * @throws IllegalArgumentException if the input is null or not a valid status
     */
    public static Status validateStatus(String status) {
        if(status==null){
            throw new IllegalArgumentException("Status cannot be null");
        }

        try{
            return Status.valueOf(status.trim().toUpperCase());
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status + ". Valid statuses are: todo, in-progress, done");
        }
    }
}
