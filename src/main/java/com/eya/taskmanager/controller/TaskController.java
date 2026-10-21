
package com.eya.taskmanager.controller;

import com.eya.taskmanager.model.Task;
import com.eya.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    @GetMapping("/")
    public String showTasks(
            @RequestParam(defaultValue = "all") String filter,
            Model model) {

        var tasks = taskRepository.findAll();

        if (filter.equals("open")) {
            tasks = tasks.stream()
                    .filter(task -> !task.isCompleted())
                    .toList();
        } else if (filter.equals("done")) {
            tasks = tasks.stream()
                    .filter(Task::isCompleted)
                    .toList();
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("filter", filter);

        return "index";
    }


    @PostMapping("/add")
    public String addTask(@RequestParam String title,
                          @RequestParam String description) {

        Task task = new Task(title, description);
        taskRepository.save(task);

        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/toggle/{id}")
    public String toggleTask(@PathVariable Long id) {

        Task task = taskRepository.findById(id).orElseThrow();

        task.setCompleted(!task.isCompleted());

        taskRepository.save(task);

        return "redirect:/";
    }

    @PostMapping("/edit/{id}")
    public String editTask(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String description) {

        Task task = taskRepository.findById(id).orElseThrow();

        task.setTitle(title);
        task.setDescription(description);

        taskRepository.save(task);

        return "redirect:/";
    }




}

