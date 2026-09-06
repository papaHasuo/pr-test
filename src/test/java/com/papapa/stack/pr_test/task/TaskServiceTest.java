package com.papapa.stack.pr_test.task;

import static org.assertj.core.api.Assertions.assertThat;

import com.papapa.stack.pr_test.task.entity.TaskEntity;
import com.papapa.stack.pr_test.task.entity.TaskStatus;
import com.papapa.stack.pr_test.task.logic.TaskCreationLogic;
import com.papapa.stack.pr_test.task.repository.TaskRepository;
import com.papapa.stack.pr_test.task.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Test
    void createTask_shouldNormalizeTitleAndSetOpenStatus() {
        TaskService taskService = new TaskService(new TaskCreationLogic(), taskRepository);
        when(taskRepository.save(any(TaskEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TaskEntity task = taskService.createTask("  Buy milk  ", "Need for breakfast");

        assertThat(task.getTitle()).isEqualTo("Buy milk");
        assertThat(task.getDescription()).isEqualTo("Need for breakfast");
        assertThat(task.getStatus()).isEqualTo(TaskStatus.OPEN);
    }
}
