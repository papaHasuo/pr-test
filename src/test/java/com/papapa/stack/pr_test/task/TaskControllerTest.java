package com.papapa.stack.pr_test.task;

import static org.assertj.core.api.Assertions.assertThat;

import com.papapa.stack.pr_test.task.controller.TaskController;
import com.papapa.stack.pr_test.task.service.TaskCompletionService;
import com.papapa.stack.pr_test.task.service.TaskEditService;
import com.papapa.stack.pr_test.task.service.TaskService;
import com.papapa.stack.pr_test.task.entity.TaskEntity;
import com.papapa.stack.pr_test.task.entity.TaskStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.ui.ConcurrentModel;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private TaskCompletionService taskCompletionService;

    @Mock
    private TaskEditService taskEditService;

    @Test
    void listTasks_shouldReturnViewNameAndEmptyListWhenNoRepository() {
        TaskController controller = controller();
        Model model = new ConcurrentModel();

        String viewName = controller.listTasks(model);

        assertThat(viewName).isEqualTo("tasks/list");
        assertThat(model.getAttribute("tasks")).isNotNull();
    }

    @Test
    void completeTask_shouldCompleteTaskAndRedirectToList() {
        TaskController controller = controller();

        String viewName = controller.completeTask(1L);

        assertThat(viewName).isEqualTo("redirect:/tasks");
        verify(taskCompletionService).completeTask(1L);
    }

    @Test
    void showEditForm_shouldReturnTaskAndEditView() {
        TaskEntity task = new TaskEntity(1L, "Old title", "Old description", TaskStatus.OPEN);
        when(taskEditService.findTask(1L)).thenReturn(task);
        Model model = new ConcurrentModel();

        String viewName = controller().showEditForm(1L, model);

        assertThat(viewName).isEqualTo("tasks/form");
        assertThat(model.getAttribute("task")).isSameAs(task);
        assertThat(model.getAttribute("editMode")).isEqualTo(true);
    }

    @Test
    void editTask_shouldDelegateAndRedirectToList() {
        String viewName = controller().editTask(1L, "New title", "New description",
                new ConcurrentModel());

        assertThat(viewName).isEqualTo("redirect:/tasks");
        verify(taskEditService).editTask(1L, "New title", "New description");
    }

    @Test
    void editTask_shouldRenderFormWithErrorWhenServiceRejectsInput() {
        when(taskEditService.editTask(1L, " ", null))
                .thenThrow(new IllegalArgumentException("Title must not be blank"));
        Model model = new ConcurrentModel();

        String viewName = controller().editTask(1L, " ", null, model);

        assertThat(viewName).isEqualTo("tasks/form");
        assertThat(model.getAttribute("errorMessage")).isEqualTo("Title must not be blank");
        TaskEntity task = (TaskEntity) model.getAttribute("task");
        assertThat(task.getTitle()).isBlank();
    }

    private TaskController controller() {
        return new TaskController(taskService, taskCompletionService, taskEditService);
    }
}
