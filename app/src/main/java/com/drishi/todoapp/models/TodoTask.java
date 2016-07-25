package com.drishi.todoapp.models;

/**
 * Created by drishi on 7/24/16.
 */
public class TodoTask {
    public int id;
    public String taskDescription;

    public TodoTask(String taskDescription) {
        this.taskDescription = taskDescription;
    }
}
