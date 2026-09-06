package com.papapa.stack.pr_test.task.service;

import com.papapa.stack.pr_test.task.entity.TaskEntity;
import com.papapa.stack.pr_test.task.entity.TaskStatus;
import com.papapa.stack.pr_test.task.logic.TaskCompletionLogic;
import com.papapa.stack.pr_test.task.logic.TaskCreationLogic;
import com.papapa.stack.pr_test.task.repository.TaskRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskCreationLogic taskCreationLogic;
    private final TaskCompletionLogic taskCompletionLogic;
    private final TaskRepository taskRepository;

    public TaskService(TaskCreationLogic taskCreationLogic) {
        this(taskCreationLogic, new TaskCompletionLogic(), null);
    }

    public TaskService(TaskCreationLogic taskCreationLogic, TaskRepository taskRepository) {
        this(taskCreationLogic, new TaskCompletionLogic(), taskRepository);
    }

    @org.springframework.beans.factory.annotation.Autowired
    public TaskService(TaskCreationLogic taskCreationLogic,
                       TaskCompletionLogic taskCompletionLogic,
                       TaskRepository taskRepository) {
        this.taskCreationLogic = taskCreationLogic;
        this.taskCompletionLogic = taskCompletionLogic;
        this.taskRepository = taskRepository;
    }

    public List<TaskEntity> findAll() {
        if (taskRepository == null) {
            return List.of();
        }
        return taskRepository.findAll();
    }

    public TaskEntity createTask(String title, String description) {
        TaskEntity task = taskCreationLogic.create(title, description);
        task.setStatus(TaskStatus.OPEN);
        if (taskRepository == null) {
            return task;
        }
        return taskRepository.save(task);
    }

    public TaskEntity completeTask(Long taskId) {
        Objects.requireNonNull(taskId, "Task ID must not be null");
        if (taskRepository == null) {
            throw new IllegalStateException("Task repository is not configured");
        }
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        taskCompletionLogic.complete(task);
        return taskRepository.save(task);
    }
}
