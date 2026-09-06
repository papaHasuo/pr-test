package com.papapa.stack.pr_test.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.papapa.stack.pr_test.task.entity.TaskEntity;
import com.papapa.stack.pr_test.task.entity.TaskStatus;
import com.papapa.stack.pr_test.task.logic.TaskEditLogic;
import org.junit.jupiter.api.Test;

class TaskEditLogicTest {

    private final TaskEditLogic logic = new TaskEditLogic();

    @Test
    void edit_shouldUpdateOpenTaskAndTrimTitle() {
        TaskEntity task = new TaskEntity(1L, "Old title", "Old description", TaskStatus.OPEN);

        TaskEntity editedTask = logic.edit(task, "  New title  ", "New description");

        assertThat(editedTask.getTitle()).isEqualTo("New title");
        assertThat(editedTask.getDescription()).isEqualTo("New description");
    }

    @Test
    void edit_shouldUpdateInProgressTask() {
        TaskEntity task = new TaskEntity(1L, "Old title", null, TaskStatus.IN_PROGRESS);

        logic.edit(task, "New title", null);

        assertThat(task.getTitle()).isEqualTo("New title");
    }

    @Test
    void edit_shouldRejectDoneTask() {
        TaskEntity task = new TaskEntity(1L, "Old title", null, TaskStatus.DONE);

        assertThatThrownBy(() -> logic.edit(task, "New title", null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("DONE task cannot be edited");
    }

    @Test
    void edit_shouldRejectNullTask() {
        assertThatThrownBy(() -> logic.edit(null, "New title", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task must not be null");
    }

    @Test
    void edit_shouldRejectNullTitle() {
        TaskEntity task = new TaskEntity(1L, "Old title", null, TaskStatus.OPEN);

        assertThatThrownBy(() -> logic.edit(task, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Title must not be null");
    }

    @Test
    void edit_shouldRejectBlankTitle() {
        TaskEntity task = new TaskEntity(1L, "Old title", null, TaskStatus.OPEN);

        assertThatThrownBy(() -> logic.edit(task, " \t ", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Title must not be blank");
    }

    @Test
    void edit_shouldAcceptTitleWith100Characters() {
        TaskEntity task = new TaskEntity(1L, "Old title", null, TaskStatus.OPEN);

        logic.edit(task, "a".repeat(100), null);

        assertThat(task.getTitle()).hasSize(100);
    }

    @Test
    void edit_shouldRejectTitleWithMoreThan100Characters() {
        TaskEntity task = new TaskEntity(1L, "Old title", null, TaskStatus.OPEN);

        assertThatThrownBy(() -> logic.edit(task, "a".repeat(101), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Title must be at most 100 characters");
    }
}
