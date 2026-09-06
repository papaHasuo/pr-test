package com.papapa.stack.pr_test.task.service;

import com.papapa.stack.pr_test.task.entity.TaskEntity;
import com.papapa.stack.pr_test.task.entity.TaskStatus;
import com.papapa.stack.pr_test.task.logic.TaskCreationLogic;
import com.papapa.stack.pr_test.task.repository.TaskRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskCreationLogic taskCreationLogic;
    private final TaskRepository taskRepository;

    public TaskService(TaskCreationLogic taskCreationLogic,
                       TaskRepository taskRepository) {
        this.taskCreationLogic = taskCreationLogic;
        this.taskRepository = taskRepository;
    }

    public List<TaskEntity> findAll() {
        return taskRepository.findAll();
    }

    public TaskEntity createTask(String title, String description) {
        TaskEntity task = taskCreationLogic.create(title, description);
        task.setStatus(TaskStatus.OPEN);
        return taskRepository.save(task);
    }
}
