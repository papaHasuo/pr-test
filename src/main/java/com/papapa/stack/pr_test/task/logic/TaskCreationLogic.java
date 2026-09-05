package com.papapa.stack.pr_test.task.logic;

import com.papapa.stack.pr_test.task.entity.TaskEntity;
import com.papapa.stack.pr_test.task.entity.TaskStatus;
import org.springframework.stereotype.Component;

@Component
public class TaskCreationLogic {

    public String normalizeTitle(String title) {
        if (title == null) {
            return "";
        }
        return title.trim();
    }

    public TaskEntity create(String title, String description) {
        return new TaskEntity(null, normalizeTitle(title), description, TaskStatus.OPEN);
    }
}
