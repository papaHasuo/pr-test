package com.papapa.stack.pr_test.task;

import static org.assertj.core.api.Assertions.assertThat;

import com.papapa.stack.pr_test.task.controller.TaskController;
import com.papapa.stack.pr_test.task.logic.TaskCreationLogic;
import com.papapa.stack.pr_test.task.repository.TaskRepository;
import com.papapa.stack.pr_test.task.service.TaskService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskRepository taskRepository;

    @Test
    void listTasks_shouldReturnViewNameAndEmptyListWhenNoRepository() {
        TaskController controller = new TaskController(
                new TaskService(new TaskCreationLogic(), taskRepository));
        Model model = new ConcurrentModel();

        String viewName = controller.listTasks(model);

        assertThat(viewName).isEqualTo("tasks/list");
        assertThat(model.getAttribute("tasks")).isNotNull();
    }
}
