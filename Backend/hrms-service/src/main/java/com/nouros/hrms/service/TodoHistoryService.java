package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.TodoHistory;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.TodoHistoryData;

public interface TodoHistoryService extends GenericService<Integer,TodoHistory>{
	
    String softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public TodoHistory create(TodoHistory todoHistory);
	
	public TodoHistory update(TodoHistory todoHistory);

	List<TodoHistory> findTodoHistoryByTodoIdAndTodoAssignee(Integer todoId , Integer assigneeId );

	public TodoHistory createByTodoId(TodoHistoryData todoHistoryData);

}
