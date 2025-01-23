package com.nouros.hrms.service;

import java.util.Date;
import java.util.List;

import com.nouros.hrms.model.Todo;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.TodoDto;
import com.nouros.hrms.wrapper.TodoUpdateRequest;

public interface TodoService extends GenericService<Integer,Todo>{

    String softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public Todo create(Todo Todo);
	
	public Todo update(Todo Todo);
    
	void sendScheduledNotificationToAssignorDaily();

	public Object createTodoAndAssignee(List<TodoDto> todoDtos);

    String updateTodoStatus(Integer id,String status);

	String updateAssignee(Integer id, Integer oldAssigneeId, Integer newAssigneeId);

	public String updateDeadline(Integer id, Date newDueDate);

	public String updateTodo(TodoUpdateRequest todoUpdateRequest);

	Object findAllData(String referenceId, Integer assigneeId);

	List<Todo> search(String filter, Integer offset, Integer size, String orderBy, String orderType, Integer assignee);
   
}
