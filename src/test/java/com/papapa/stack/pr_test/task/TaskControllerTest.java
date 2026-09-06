package com.papapa.stack.pr_test.task;

import static org.assertj.core.api.Assertions.assertThat;

import com.papapa.stack.pr_test.task.controller.TaskController;
import com.papapa.stack.pr_test.task.service.TaskCompletionService;
import com.papapa.stack.pr_test.task.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.ui.ConcurrentModel;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private TaskCompletionService taskCompletionService;

    @Test
    void listTasks_shouldReturnViewNameAndEmptyListWhenNoRepository() {
        TaskController controller = new TaskController(taskService, taskCompletionService);
        Model model = new ConcurrentModel();

        String viewName = controller.listTasks(model);

        assertThat(viewName).isEqualTo("tasks/list");
        assertThat(model.getAttribute("tasks")).isNotNull();
    }

    @Test
    void completeTask_shouldCompleteTaskAndRedirectToList() {
        TaskController controller = new TaskController(taskService, taskCompletionService);

        String viewName = controller.completeTask(1L);

        assertThat(viewName).isEqualTo("redirect:/tasks");
        org.mockito.Mockito.verify(taskCompletionService).completeTask(1L);
    }
}
