package com.papapa.stack.pr_test.task;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class TaskListTemplateTest {

    @Test
    void listTemplate_shouldProvideCompletionFormOnlyForIncompleteTasks() throws Exception {
        String template = Files.readString(Path.of(
                "src/main/resources/templates/tasks/list.html"));

        assertThat(template)
                .contains("th:if=\"${task.status.name() != 'DONE'}\"")
                .contains("th:action=\"@{/tasks/{taskId}/complete(taskId=${task.id})}\"")
                .contains(">Complete</button>");
    }
}
