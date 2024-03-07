package com.antimated.tasklist.tasks;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class TaskList {
	private final List<Task> tasks;

	public TaskList(List<Task> tasks) {
		this.tasks = tasks;
	}

	public Task getTaskById(int id) {
		return tasks.stream()
			.filter(task -> task.getId() == id)
			.findFirst()
			.orElse(null);
	}

	public List<Task> getTasksByType(TaskType type) {
		return tasks.stream()
			.filter(task -> task.getType() == type)
			.collect(Collectors.toList());
	}

	public List<Task> getTasksByTier(TaskTier tier) {
		return tasks.stream()
			.filter(task -> task.getTier() == tier)
			.collect(Collectors.toList());
	}

	public List<Task> getIncompleteTasks() {
		return tasks.stream()
			.filter(task -> !task.isCompleted())
			.collect(Collectors.toList());
	}

	public List<Task> getCompleteTasks() {
		return tasks.stream()
			.filter(Task::isCompleted)
			.collect(Collectors.toList());
	}
}
