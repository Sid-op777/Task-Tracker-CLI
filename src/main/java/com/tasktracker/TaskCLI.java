package com.tasktracker;

import picocli.CommandLine;

import java.util.*;
import picocli.CommandLine.*;

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

    public static void main(String[] args) {
        CommandLine.run(new TaskCLI(), args);
    }

    @Command(name = "add", description = "Add a new task")
    private void addCommand(
            @Parameters(paramLabel = "DESCRIPTION", description = "The description of the task") String description,
            @Option(names = {"-s","--status"},description = "The status for the task")String status
    ){
        if(status==null){
            addTask(description);
        }
        else{
            addTask(description,status);
        }

    }

    @Command(name = "update", description = "Update a task")
    private void updateCommand(
            @Parameters(index = "0", paramLabel = "ID", description = "ID of the task you want to update") String id,
            @Option(names = {"-d", "--description"}, description = "Updated description") String newDescription,
            @Option(names = {"-s", "--status"}, description = "Updated status") String newStatus
    ){
        boolean isnewDesc = (newDescription!=null);
        boolean isnewStatus = (newStatus!=null);
        if (!isnewDesc && !isnewStatus){
            System.out.println("Nothing to update!");
        } else if (isnewDesc && !isnewStatus) {
            updateTask(id,newDescription);
        } else if (!isnewDesc) {
            //TODO
        } else{
            //TODO
        }

    }

    @Command(name = "delete",description = "Delete a task")
    private void deleteCommand(
            @Parameters(index = "0", paramLabel = "ID", description = "ID of the task you want to delete") String id
    ){
        deleteTask(id);
    }

    @Command(name = "list", description = "List tasks")
    private void listCommand(
            @Option(names = {"-s", "--status"}, description = "status filter") String statusFilter
    ){
        if(statusFilter==null){
            listTasks();
        }
        else {
            listTasksByStatus(statusFilter);
        }

    }

    private static void addTask(String description) {
        Task task = new Task(description);
        taskManager.addTask(task);
        System.out.println("Task added successfully (ID: " + task.getId() + ")");
    }

    private static void addTask(String description,String status) {
        Task task = new Task(description,status);
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
        List<Task> tasks = taskManager.listTasks();
        tasks.stream()
                .filter(task -> task.getStatus().equals(status))
                .forEach(System.out::println);
    }


//    private static void printHelp() {
//        System.out.println("Task Tracker CLI");
//        System.out.println("Usage:");
//        System.out.println("tcli add <description>"); //done
//        System.out.println("tcli update <id> <description>"); //done
//        System.out.println("tcli delete <id>"); //done
//        System.out.println("tcli mark-in-progress <id>");
//        System.out.println("tcli mark-done <id>");
//        System.out.println("tcli list"); //done
//        System.out.println("tcli list <status>  (Valid statuses: todo, in-progress, done)");
//    }

    @Override
    public void run() {
        //default case
        System.out.println("tcli --help");
    }
}
