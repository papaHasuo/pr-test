package com.papapa.stack.pr_test.task.logic;

import com.papapa.stack.pr_test.task.entity.TaskEntity;
import com.papapa.stack.pr_test.task.entity.TaskStatus;
import org.springframework.stereotype.Component;

@Component
public class TaskEditLogic {

    static final int MAX_TITLE_LENGTH = 100;

    public TaskEntity edit(TaskEntity task, String title, String description) {
        if (task == null) {
            throw new IllegalArgumentException("Task must not be null");
        }
        if (task.getStatus() == TaskStatus.DONE) {
            throw new IllegalStateException("DONE task cannot be edited");
        }

        String normalizedTitle = normalizeTitle(title);
        task.setTitle(normalizedTitle);
        task.setDescription(description);
        return task;
    }

    private String normalizeTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("Title must not be null");
        }

        String normalizedTitle = title.trim();
        if (normalizedTitle.isEmpty()) {
            throw new IllegalArgumentException("Title must not be blank");
        }
        if (normalizedTitle.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("Title must be at most 100 characters");
        }
        return normalizedTitle;
    }
}
