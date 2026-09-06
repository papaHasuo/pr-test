package com.papapa.stack.pr_test.task.service;

import com.papapa.stack.pr_test.task.entity.TaskEntity;
import com.papapa.stack.pr_test.task.logic.TaskCompletionLogic;
import com.papapa.stack.pr_test.task.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskCompletionService {

    private final TaskCompletionLogic taskCompletionLogic;
    private final TaskRepository taskRepository;

    public TaskCompletionService(TaskCompletionLogic taskCompletionLogic,
                                 TaskRepository taskRepository) {
        this.taskCompletionLogic = taskCompletionLogic;
        this.taskRepository = taskRepository;
    }

    public TaskEntity completeTask(Long taskId) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        taskCompletionLogic.complete(task);
        return taskRepository.save(task);
    }
}
