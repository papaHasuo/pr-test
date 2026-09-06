package com.papapa.stack.pr_test.task.controller;

import com.papapa.stack.pr_test.task.entity.TaskEntity;
import com.papapa.stack.pr_test.task.service.TaskCompletionService;
import com.papapa.stack.pr_test.task.service.TaskService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskCompletionService taskCompletionService;

    public TaskController(TaskService taskService,
                          TaskCompletionService taskCompletionService) {
        this.taskService = taskService;
        this.taskCompletionService = taskCompletionService;
    }

    @GetMapping
    public String listTasks(Model model) {
        List<TaskEntity> tasks = taskService.findAll();
        model.addAttribute("tasks", tasks);
        return "tasks/list";
    }

    @GetMapping("/new")
    public String showCreateForm() {
        return "tasks/form";
    }

    @PostMapping
    public String createTask(@RequestParam String title,
                             @RequestParam(required = false) String description) {
        taskService.createTask(title, description);
        return "redirect:/tasks";
    }

    @PostMapping("/{taskId}/complete")
    public String completeTask(@PathVariable Long taskId) {
        taskCompletionService.completeTask(taskId);
        return "redirect:/tasks";
    }
}
