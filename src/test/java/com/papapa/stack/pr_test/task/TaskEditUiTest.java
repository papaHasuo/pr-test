package com.papapa.stack.pr_test.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.papapa.stack.pr_test.task.entity.TaskEntity;
import com.papapa.stack.pr_test.task.entity.TaskStatus;
import com.papapa.stack.pr_test.task.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TaskEditUiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void editTask_shouldRenderFormAndShowUpdatedTaskInList() throws Exception {
        TaskEntity task = taskRepository.save(
                new TaskEntity(null, "Old title", "Old description", TaskStatus.OPEN));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString())
                        .contains("href=\"/tasks/" + task.getId() + "/edit\""));

        mockMvc.perform(get("/tasks/{taskId}/edit", task.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/form"))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString())
                        .contains("value=\"Old title\"")
                        .contains("Old description")
                        .contains("Update"));

        mockMvc.perform(post("/tasks/{taskId}/edit", task.getId())
                        .param("title", "New title")
                        .param("description", "New description"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString())
                        .contains("New title")
                        .contains("New description"));
    }

    @Test
    void editTask_shouldRenderValidationError() throws Exception {
        TaskEntity task = taskRepository.save(
                new TaskEntity(null, "Old title", null, TaskStatus.OPEN));

        mockMvc.perform(post("/tasks/{taskId}/edit", task.getId())
                        .param("title", " "))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/form"))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString())
                        .contains("Title must not be blank"));
    }

    @Test
    void doneTask_shouldNotShowEditLink() throws Exception {
        TaskEntity task = taskRepository.save(
                new TaskEntity(null, "Completed task", null, TaskStatus.DONE));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString())
                        .doesNotContain("href=\"/tasks/" + task.getId() + "/edit\""));
    }
}
