package com.tasktracker;

import org.apache.commons.text.similarity.LevenshteinDistance;
import picocli.CommandLine;

import java.util.*;

import picocli.CommandLine.*;



/**
 * The main class for the Task Manager CLI application.
 * <p>
 * Provides a command-line interface for managing tasks, including adding, updating, deleting,
 * and listing tasks. Uses the Picocli library for command-line argument parsing.
 * </p>
 */
@Command(
        name = "tcli",
        description = "\n" +
                "___________              __     _________ .__  .__ \n" +
                "\\__    ___/____    _____|  | __ \\_   ___ \\|  | |__|\n" +
                "  |    |  \\__  \\  /  ___/  |/ / /    \\  \\/|  | |  |\n" +
                "  |    |   / __ \\_\\___ \\|    <  \\     \\___|  |_|  |\n" +
                "  |____|  (____  /____  >__|_ \\  \\______  /____/__|\n" +
                "               \\/     \\/     \\/         \\/         \n"+
                "\n"+"Manage your tasks!"+"\n"
)
public class TaskCLI implements Runnable {

    private static final TaskManager taskManager = new TaskManager();

    @Option(names = {"-h", "--help"}, description = "Display help/usage information", usageHelp = true)
    boolean help;

    /**
     * Main method that starts the Task Manager CLI.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        CommandLine.run(new TaskCLI(), args);
    }

    /**
     * Command to add a new task.
     *
     * @param description The description of the task.
     * @param status      The status of the task. If null, defaults to TODO.
     */
    @Command(name = "add", description = "Add a new task")
    private void addCommand(
            @Parameters(paramLabel = "DESCRIPTION", description = "The description of the task") String description,
            @Option(names = {"-s", "--status"}, description = "The status for the task") String status
    ) {
        if (status == null) {
            try {
                addTask(description);
            }catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } else {
            addTask(description, status);
        }
    }

    /**
     * Command to update an existing task.
     *
     * @param id             The ID of the task to update.
     * @param newDescription The new description for the task (optional).
     * @param newStatus      The new status for the task (optional).
     */
    @Command(name = "update", description = "Update a task")
    private void updateCommand(
            @Parameters(index = "0", paramLabel = "ID", description = "ID of the task you want to update") String id,
            @Option(names = {"-d", "--description"}, description = "Updated description") String newDescription,
            @Option(names = {"-s", "--status"}, description = "Updated status") String newStatus
    ) {
        boolean isnewDesc = (newDescription != null);
        boolean isnewStatus = (newStatus != null);
        if (!isnewDesc && !isnewStatus) {
            System.out.println("Nothing to update!");
        } else if (isnewDesc && !isnewStatus) {
            try {
                updateTaskDescp(id, newDescription);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } else if (!isnewDesc) {
            try {
                updateTaskStatus(id, newStatus);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } else {
            try {
                updateTaskDescpStatus(id, newDescription, newStatus);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Command to delete a task.
     *
     * @param id The ID of the task to delete.
     */
    @Command(name = "delete", description = "Delete a task")
    private void deleteCommand(
            @Parameters(index = "0", paramLabel = "ID", description = "ID of the task you want to delete") String id
    ) {
        deleteTask(id);
    }

    /**
     * Command to list tasks.
     *
     * @param statusFilter Optional filter to list tasks by status.
     */
    @Command(name = "list", description = "List tasks")
    private void listCommand(
            @Option(names = {"-s", "--status"}, description = "status filter") String statusFilter
    ) {
        if (statusFilter == null) {
            listTasks();
        } else {
            try {
                listTasksByStatus(statusFilter);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Command to search tasks by keyword in their description, returning the top {@code k} matches.
     * <p>
     * This command searches the task descriptions for the given keyword, using the Levenshtein distance
     * to determine the similarity between the keyword and each task description. The command returns
     * the top {@code k} tasks with the smallest Levenshtein distance to the keyword. If a task description
     * contains the keyword exactly, it is considered a perfect match and will have the smallest distance (0).
     * </p>
     *
     * @param keyword The keyword to search for in task descriptions.
     * @param k    The number of top matching tasks to return. Defaults to 5 if not specified.
     */
    @Command(name = "search", description = "Search description")
    private void searchCommand(
            @Parameters(index = "0", paramLabel = "keyword", description = "Search description") String keyword,
            @Option(names = {"-k"}, description = "top k matches", defaultValue = "5") int k
    ){
        LevenshteinDistance ld = LevenshteinDistance.getDefaultInstance();
        List<Task> tasks = taskManager.listTasks();
        List<TaskMatch> taskMatches = tasks.stream()
                .map(task -> {
                    String description = task.getDescription();

                    if (description.toLowerCase().contains(keyword.toLowerCase())) {
                        return new TaskMatch(task, 0);
                    }

                    int distance = ld.apply(description.toLowerCase(), keyword.toLowerCase());

                    return new TaskMatch(task, distance);
                }).sorted(Comparator.comparingInt(TaskMatch::getDistance)).toList();


        taskMatches.stream()
                .limit((k))
                .map(TaskMatch::getTask)
                .forEach(System.out::println);
    }

    /**
     * Adds a new task with the given description.
     *
     * @param description The description of the task.
     */
    private static void addTask(String description) throws IllegalArgumentException {
        Task task = new Task(description);
        taskManager.addTask(task);
        System.out.println("Task added successfully (ID: " + task.getId() + ")");
    }

    /**
     * Adds a new task with the given description and status.
     *
     * @param description The description of the task.
     * @param status      The status of the task.
     */
    private static void addTask(String description, String status) {
        Task task = new Task(description, status);
        taskManager.addTask(task);
        System.out.println("Task added successfully (ID: " + task.getId() + ")");
    }

    /**
     * Updates the description of a task.
     *
     * @param id          The ID of the task to update.
     * @param description The new description for the task.
     * @throws IllegalArgumentException if the ID or description is invalid.
     */
    private static void updateTaskDescp(String id, String description) throws IllegalArgumentException {
        taskManager.updateTaskDescp(id, description);
    }

    /**
     * Updates the status of a task.
     *
     * @param id     The ID of the task to update.
     * @param status The new status for the task.
     * @throws IllegalArgumentException if the ID or status is invalid.
     */
    private static void updateTaskStatus(String id, String status) throws IllegalArgumentException {
        taskManager.updateTaskStatus(id, status);
    }

    /**
     * Updates both the description and status of a task.
     *
     * @param id          The ID of the task to update.
     * @param description The new description for the task.
     * @param status      The new status for the task.
     * @throws IllegalArgumentException if the ID, description, or status is invalid.
     */
    private static void updateTaskDescpStatus(String id, String description, String status) throws IllegalArgumentException {
        taskManager.updateTaskDecpStatus(id, description, status);
    }

    /**
     * Deletes a task.
     *
     * @param id The ID of the task to delete.
     */
    private static void deleteTask(String id) {
        taskManager.deleteTask(id);
    }

    /**
     * Marks a task as in progress.
     *
     * @param id The ID of the task to mark as in progress.
     */
    private static void markTaskInProgress(String id) {
        taskManager.markTaskAs(id, Status.IN_PROGRESS);
    }

    /**
     * Marks a task as done.
     *
     * @param id The ID of the task to mark as done.
     */
    private static void markTaskDone(String id) {
        taskManager.markTaskAs(id, Status.DONE);
    }

    /**
     * Lists all tasks.
     */
    private static void listTasks() {
        List<Task> tasks = taskManager.listTasks();
        tasks.forEach(System.out::println);
    }

    /**
     * Lists tasks filtered by status.
     *
     * @param status The status to filter by.
     * @throws IllegalArgumentException if the status is invalid.
     */
    private static void listTasksByStatus(String status) throws IllegalArgumentException {
        String validStatus = Status.validateStatus(status).toString();
        List<Task> tasks = taskManager.listTasks();
        tasks.stream()
                .filter(task -> task.getStatus().equals(validStatus))
                .forEach(System.out::println);
    }

    /**
     * Default method that runs when no command is specified.
     */
    @Override
    public void run() {
        //default case
        System.out.println("tcli --help");
    }
}
