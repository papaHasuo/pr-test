package com.papapa.stack.pr_test.task.service;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long taskId) {
        super("Task not found: " + taskId);
    }
}
