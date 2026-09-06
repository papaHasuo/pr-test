package com.papapa.stack.pr_test.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.papapa.stack.pr_test.task.entity.TaskEntity;
import com.papapa.stack.pr_test.task.entity.TaskStatus;
import com.papapa.stack.pr_test.task.logic.TaskCompletionLogic;
import org.junit.jupiter.api.Test;

class TaskCompletionLogicTest {

    private final TaskCompletionLogic logic = new TaskCompletionLogic();

    @Test
    void complete_shouldChangeOpenTaskToDone() {
        TaskEntity task = new TaskEntity(1L, "Buy milk", null, TaskStatus.OPEN);

        TaskEntity completedTask = logic.complete(task);

        assertThat(completedTask.getStatus()).isEqualTo(TaskStatus.DONE);
    }

    @Test
    void complete_shouldChangeInProgressTaskToDone() {
        TaskEntity task = new TaskEntity(1L, "Buy milk", null, TaskStatus.IN_PROGRESS);

        logic.complete(task);

        assertThat(task.getStatus()).isEqualTo(TaskStatus.DONE);
    }

    @Test
    void complete_shouldRejectAlreadyDoneTask() {
        TaskEntity task = new TaskEntity(1L, "Buy milk", null, TaskStatus.DONE);

        assertThatThrownBy(() -> logic.complete(task))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Task is already completed");
    }

    @Test
    void complete_shouldRejectNullTask() {
        assertThatThrownBy(() -> logic.complete(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task must not be null");
    }
}
