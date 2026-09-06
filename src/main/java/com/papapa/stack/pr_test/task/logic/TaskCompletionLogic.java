package com.papapa.stack.pr_test.task.logic;

import com.papapa.stack.pr_test.task.entity.TaskEntity;
import com.papapa.stack.pr_test.task.entity.TaskStatus;
import org.springframework.stereotype.Component;

@Component
public class TaskCompletionLogic {

    public TaskEntity complete(TaskEntity task) {
        if (task == null) {
            throw new IllegalArgumentException("Task must not be null");
        }
        if (task.getStatus() == TaskStatus.DONE) {
            throw new IllegalStateException("Task is already completed");
        }
        task.setStatus(TaskStatus.DONE);
        return task;
    }
}
