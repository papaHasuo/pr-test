package com.papapa.stack.pr_test.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class TaskCompletionUiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void listAndCompleteTask_shouldRenderTheUpdatedTaskState() throws Exception {
        TaskEntity task = taskRepository.save(
                new TaskEntity(null, "Buy milk", null, TaskStatus.OPEN));

        MvcResult listBeforeCompletion = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/list"))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString())
                        .contains("action=\"/tasks/" + task.getId() + "/complete\"")
                        .contains("Buy milk"))
                .andReturn();

        assertThat(listBeforeCompletion.getResponse().getContentAsString())
                .contains("Complete");

        mockMvc.perform(post("/tasks/{taskId}/complete", task.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        MvcResult listAfterCompletion = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/list"))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString())
                        .contains("DONE")
                        .doesNotContain("action=\"/tasks/" + task.getId() + "/complete\""))
                .andReturn();

        assertThat(listAfterCompletion.getResponse().getContentAsString())
                .contains("Buy milk");
    }

    @Test
    void inProgressTask_shouldBeDisplayedAndCanBeCompleted() throws Exception {
        TaskEntity task = taskRepository.save(
                new TaskEntity(null, "Prepare report", null, TaskStatus.IN_PROGRESS));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/list"))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString())
                        .contains("Prepare report")
                        .contains("IN_PROGRESS")
                        .contains("action=\"/tasks/" + task.getId() + "/complete\""));

        mockMvc.perform(post("/tasks/{taskId}/complete", task.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/list"))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString())
                        .contains("Prepare report")
                        .contains("DONE")
                        .doesNotContain("action=\"/tasks/" + task.getId() + "/complete\""));
    }
}
