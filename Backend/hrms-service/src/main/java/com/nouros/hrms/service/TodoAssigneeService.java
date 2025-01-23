package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.TodoAssignee;
import com.nouros.hrms.service.generic.GenericService;

public interface TodoAssigneeService extends GenericService<Integer,TodoAssignee>{

    String softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public TodoAssignee create(TodoAssignee Todo);
	
	public TodoAssignee update(TodoAssignee Todo);

	TodoAssignee findByTodoAssigneeByAssigneeId(Integer userId,Integer todoId);

}
