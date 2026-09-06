package com.papapa.stack.pr_test.task.controller;

import com.papapa.stack.pr_test.task.entity.TaskEntity;
import com.papapa.stack.pr_test.task.service.TaskCompletionService;
import com.papapa.stack.pr_test.task.service.TaskEditService;
import com.papapa.stack.pr_test.task.service.TaskService;
import com.papapa.stack.pr_test.task.service.TaskNotFoundException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskCompletionService taskCompletionService;
    private final TaskEditService taskEditService;

    public TaskController(TaskService taskService,
                          TaskCompletionService taskCompletionService,
                          TaskEditService taskEditService) {
        this.taskService = taskService;
        this.taskCompletionService = taskCompletionService;
        this.taskEditService = taskEditService;
    }

    @GetMapping
    public String listTasks(Model model) {
        List<TaskEntity> tasks = taskService.findAll();
        model.addAttribute("tasks", tasks);
        return "tasks/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("editMode", false);
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

    @GetMapping("/{taskId}/edit")
    public String showEditForm(@PathVariable Long taskId, Model model) {
        model.addAttribute("task", taskEditService.findTask(taskId));
        model.addAttribute("editMode", true);
        return "tasks/form";
    }

    @PostMapping("/{taskId}/edit")
    public String editTask(@PathVariable Long taskId,
                           @RequestParam String title,
                           @RequestParam(required = false) String description,
                           Model model) {
        try {
            taskEditService.editTask(taskId, title, description);
            return "redirect:/tasks";
        } catch (IllegalArgumentException | IllegalStateException exception) {
            model.addAttribute("task", new TaskEntity(taskId, title, description, null));
            model.addAttribute("editMode", true);
            model.addAttribute("errorMessage", exception.getMessage());
            return "tasks/form";
        }
    }

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleTaskNotFound(TaskNotFoundException exception) {
        return exception.getMessage();
    }
}
