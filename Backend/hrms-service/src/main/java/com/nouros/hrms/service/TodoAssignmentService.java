package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.TodoAssignment;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.TodoAssignmentDto;

public interface TodoAssignmentService extends GenericService<Integer,TodoAssignment>{

	
	String softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public TodoAssignment create(TodoAssignment todoAssignment);
	
	public TodoAssignment update(TodoAssignment todoAssignment);

	List<TodoAssignment> getByTodoBucketId(Integer id);

	TodoAssignment createTodo(TodoAssignmentDto todoAssignmentDto);

	TodoAssignment updateTodo(TodoAssignmentDto todoAssignmentDto);

	
}
