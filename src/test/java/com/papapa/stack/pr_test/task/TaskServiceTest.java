package com.papapa.stack.pr_test.task;

import static org.assertj.core.api.Assertions.assertThat;

import com.papapa.stack.pr_test.task.entity.TaskEntity;
import com.papapa.stack.pr_test.task.entity.TaskStatus;
import com.papapa.stack.pr_test.task.logic.TaskCreationLogic;
import com.papapa.stack.pr_test.task.service.TaskService;
import org.junit.jupiter.api.Test;

class TaskServiceTest {

    @Test
    void createTask_shouldNormalizeTitleAndSetOpenStatus() {
        TaskService taskService = new TaskService(new TaskCreationLogic());

        TaskEntity task = taskService.createTask("  Buy milk  ", "Need for breakfast");

        assertThat(task.getTitle()).isEqualTo("Buy milk");
        assertThat(task.getDescription()).isEqualTo("Need for breakfast");
        assertThat(task.getStatus()).isEqualTo(TaskStatus.OPEN);
    }
}
