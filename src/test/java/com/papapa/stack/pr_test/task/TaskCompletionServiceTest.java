package com.papapa.stack.pr_test.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.papapa.stack.pr_test.task.entity.TaskEntity;
import com.papapa.stack.pr_test.task.entity.TaskStatus;
import com.papapa.stack.pr_test.task.logic.TaskCompletionLogic;
import com.papapa.stack.pr_test.task.repository.TaskRepository;
import com.papapa.stack.pr_test.task.service.TaskNotFoundException;
import com.papapa.stack.pr_test.task.service.TaskCompletionService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskCompletionServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskCompletionLogic taskCompletionLogic;

    @InjectMocks
    private TaskCompletionService taskService;

    @Test
    void completeTask_shouldCompleteAndSaveTask() {
        TaskEntity task = new TaskEntity(1L, "Buy milk", null, TaskStatus.OPEN);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        TaskEntity result = taskService.completeTask(1L);

        assertThat(result).isSameAs(task);
        verify(taskCompletionLogic).complete(task);
        verify(taskRepository).save(task);
    }

    @Test
    void completeTask_shouldRejectMissingTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.completeTask(1L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found: 1");
    }
}
