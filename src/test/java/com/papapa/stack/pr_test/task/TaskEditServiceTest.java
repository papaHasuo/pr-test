package com.papapa.stack.pr_test.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.papapa.stack.pr_test.task.entity.TaskEntity;
import com.papapa.stack.pr_test.task.entity.TaskStatus;
import com.papapa.stack.pr_test.task.logic.TaskEditLogic;
import com.papapa.stack.pr_test.task.repository.TaskRepository;
import com.papapa.stack.pr_test.task.service.TaskEditService;
import com.papapa.stack.pr_test.task.service.TaskNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskEditServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskEditLogic taskEditLogic;

    @Test
    void findTask_shouldReturnExistingTask() {
        TaskEntity task = new TaskEntity(1L, "Old title", null, TaskStatus.OPEN);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        TaskEditService taskEditService = new TaskEditService(taskEditLogic, taskRepository);

        TaskEntity result = taskEditService.findTask(1L);

        assertThat(result).isSameAs(task);
    }

    @Test
    void findTask_shouldRejectMissingTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        TaskEditService taskEditService = new TaskEditService(taskEditLogic, taskRepository);

        assertThatThrownBy(() -> taskEditService.findTask(1L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found: 1");
    }

    @Test
    void editTask_shouldApplyLogicAndSaveTask() {
        TaskEntity task = new TaskEntity(1L, "Old title", null, TaskStatus.OPEN);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        TaskEditService taskEditService = new TaskEditService(taskEditLogic, taskRepository);

        TaskEntity result = taskEditService.editTask(1L, "New title", "New description");

        assertThat(result).isSameAs(task);
        verify(taskEditLogic).edit(task, "New title", "New description");
        verify(taskRepository).save(task);
    }

    @Test
    void editTask_shouldNotSaveWhenLogicRejectsEdit() {
        TaskEntity task = new TaskEntity(1L, "Old title", null, TaskStatus.DONE);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doThrow(new IllegalStateException("DONE task cannot be edited"))
                .when(taskEditLogic).edit(task, "New title", null);
        TaskEditService taskEditService = new TaskEditService(taskEditLogic, taskRepository);

        assertThatThrownBy(() -> taskEditService.editTask(1L, "New title", null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("DONE task cannot be edited");
    }
}
