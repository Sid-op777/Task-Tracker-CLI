package com.tasktracker;

/**
 * A simple data class to hold a Task and its Levenshtein distance to a search keyword.
 */
public class TaskMatch {
    private final Task task;
    private final int distance;

    public TaskMatch(Task task, int distance) {
        this.task = task;
        this.distance = distance;
    }

    public Task getTask() {
        return task;
    }

    public int getDistance() {
        return distance;
    }
}