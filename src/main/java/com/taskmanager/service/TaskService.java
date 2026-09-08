package com.taskmanager.service;

import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.security.CustomUserDetailsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Task CRUD scoped to the currently authenticated user.
 * Ownership is enforced in every query — never trust a client-supplied user id.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final CustomUserDetailsService userDetailsService;

    public TaskService(TaskRepository taskRepository, CustomUserDetailsService userDetailsService) {
        this.taskRepository = taskRepository;
        this.userDetailsService = userDetailsService;
    }

    private User currentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userDetailsService.loadDomainUser(username);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> listTasks() {
        Long ownerId = currentUser().getId();
        return taskRepository.findByOwnerId(ownerId).stream()
                .map(TaskResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getTask(Long id) {
        Task task = findOwned(id);
        return TaskResponse.from(task);
    }

    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        User owner = currentUser();
        Task task = new Task(request.getTitle(), request.getDescription(), owner);
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        return TaskResponse.from(taskRepository.save(task));
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = findOwned(id);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        return TaskResponse.from(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(Long id) {
        Task task = findOwned(id);
        taskRepository.delete(task);
    }

    private Task findOwned(Long id) {
        Long ownerId = currentUser().getId();
        return taskRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
    }
}
