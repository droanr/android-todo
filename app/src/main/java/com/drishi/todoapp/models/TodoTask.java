package com.drishi.todoapp.models;

/**
 * Created by drishi on 7/24/16.
 */
public class TodoTask {
    public int id;
    public String taskDescription;
    public String taskPriority;

    public TodoTask(String taskDescription, String taskPriority) {
        this.taskDescription = taskDescription;
        this.taskPriority = taskPriority;
    }
}
