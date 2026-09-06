package com.papapa.stack.pr_test.task.service;

import com.papapa.stack.pr_test.task.entity.TaskEntity;
import com.papapa.stack.pr_test.task.logic.TaskEditLogic;
import com.papapa.stack.pr_test.task.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskEditService {

    private final TaskEditLogic taskEditLogic;
    private final TaskRepository taskRepository;

    public TaskEditService(TaskEditLogic taskEditLogic,
                           TaskRepository taskRepository) {
        this.taskEditLogic = taskEditLogic;
        this.taskRepository = taskRepository;
    }

    public TaskEntity findTask(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    public TaskEntity editTask(Long taskId, String title, String description) {
        TaskEntity task = findTask(taskId);
        taskEditLogic.edit(task, title, description);
        return taskRepository.save(task);
    }
}
