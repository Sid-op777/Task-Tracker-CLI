package com.tasktracker;

import java.util.*;

public class TaskCLI {
    private static final TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }


        String command = args[0].toLowerCase();

        switch (command) {
            case "add":
                // TODO: add with status
                if (args.length < 2) {
                    System.out.println("Error: Description is required for adding a task.");
                    break;
                }
                StringBuilder desc = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    desc.append(args[i]).append(" ");
                }
                addTask(desc.toString().trim());
                break;

            case "update":
                if (args.length < 3) {
                    System.out.println("Error: Task ID and new description are required.");
                    break;
                }
                StringBuilder newDesc = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    newDesc.append(args[i]).append(" ");
                }
                updateTask(args[1], newDesc.toString().trim());
                break;

            case "delete":
                if (args.length < 2) {
                    System.out.println("Error: Task ID is required for deletion.");
                    break;
                }
                deleteTask(args[1]);
                break;

            case "mark-in-progress":
                if (args.length < 2) {
                    System.out.println("Error: Task ID is required to mark as in progress.");
                    break;
                }
                markTaskInProgress(args[1]);
                break;

            case "mark-done":
                if (args.length < 2) {
                    System.out.println("Error: Task ID is required to mark as done.");
                    break;
                }
                markTaskDone(args[1]);
                break;

            case "list":
                if (args.length == 1) {
                    listTasks();
                } else if (args.length == 2) {
                    // TODO: fuzzy search by description
                    listTasksByStatus(args[1]);
                } else {
                    System.out.println("Error: Invalid usage of 'list' command.");
                }
                break;

            case "help":
                if(args.length>2){
                    System.out.println("No such command");
                    System.out.println("Use 'tcli help'");
                    break;
                }
                printHelp();
                break;

            default:
                System.out.println("Unknown command: " + command);
                System.out.println("Use 'tcli help'");
        }
    }

    private static void addTask(String description) {
        Task task = new Task(description);
        taskManager.addTask(task);
        System.out.println("Task added successfully (ID: " + task.getId() + ")");
    }

    private static void updateTask(String id, String description) {
        taskManager.updateTask(id, description);
    }

    private static void deleteTask(String id) {
        taskManager.deleteTask(id);
    }

    private static void markTaskInProgress(String id) {
        taskManager.markTaskAs(id,Status.IN_PROGRESS);
    }

    private static void markTaskDone(String id) {
        taskManager.markTaskAs(id,Status.DONE);
    }

    private static void listTasks() {
        List<Task> tasks = taskManager.listTasks();
        tasks.forEach(System.out::println);
    }

    private static void listTasksByStatus(String status) {
        Status taskStatus = Status.valueOf(status.toUpperCase());
        List<Task> tasks = taskManager.listTasks();
        tasks.stream()
                .filter(task -> task.getStatus().equals(taskStatus.toString()))
                .forEach(System.out::println);
    }


    private static void printHelp() {
        System.out.println("Task Tracker CLI");
        System.out.println("Usage:");
        System.out.println("tcli add <description>");
        System.out.println("tcli update <id> <description>");
        System.out.println("tcli delete <id>");
        System.out.println("tcli mark-in-progress <id>");
        System.out.println("tcli mark-done <id>");
        System.out.println("tcli list");
        System.out.println("tcli list <status>  (Valid statuses: todo, in-progress, done)");
    }
}
