package com.papapa.stack.pr_test.task.repository;

import com.papapa.stack.pr_test.task.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
