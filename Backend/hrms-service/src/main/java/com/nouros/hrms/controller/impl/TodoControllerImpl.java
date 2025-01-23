package com.nouros.hrms.controller.impl;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nouros.hrms.controller.TodoController;
import com.nouros.hrms.model.Todo;
import com.nouros.hrms.service.TodoService;
import com.nouros.hrms.wrapper.TodoDto;
import com.nouros.hrms.wrapper.TodoUpdateRequest;

import jakarta.validation.Valid;



@Primary
@RestController
@RequestMapping("/Todo")
public class TodoControllerImpl implements TodoController{

    private static final Logger log = LogManager.getLogger(TodoControllerImpl.class);

    @Autowired
	  private TodoService todoService;

    @Override
    public Todo create(Todo todo) {
        log.info("inside @class TodoControllerImpl @method create");
	    return todoService.create(todo);
    }

    @Override
    public Long count(String filter) {
        return todoService.count(filter);
    }

    @Override
    public List<Todo> search(String filter, @Valid Integer offset, @Valid Integer size, String orderBy,
            String orderType, Integer assignee) {
                return todoService.search(filter, offset, size, orderBy, orderType, assignee);
    }

    @Override
    public Todo update(Todo todo) {
        return todoService.update(todo);
    }

    @Override
    public String deleteById(@Valid Integer id) {
        return todoService.softDelete(id);
    }

    @Override
    public Todo findById(@Valid Integer id) {
        return todoService.findById(id);
    }

    @Override
    public List<Todo> findAllById(@Valid List<Integer> id) {
        return todoService.findAllById(id);
    }

    @Override
    public ResponseEntity<String> triggerScheduledNotifications() {
        try {
            todoService.sendScheduledNotificationToAssignorDaily();
          
            return ResponseEntity.ok("Scheduled notifications triggered successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to trigger scheduled notifications.");
        }
    }

   @Override
    public Object createTodoAndAssignee(List<TodoDto> todoDtos) {
        log.info("inside @Class TodoControllerImpl @Method createTodoAndAssignee");
        return todoService.createTodoAndAssignee(todoDtos);
    }

@Override
public String updateTodoStatus(Integer id,String status) {
    log.info("inside @Class TodoControllerImpl @Method updateTodoStatus");
    return todoService.updateTodoStatus(id,status);
}

@Override
public String updateAssignee(Integer id, Integer oldAssigneeId, Integer newAssigneeId) {
    log.info("inside @Class TodoControllerImpl @Method updateAssignee");
    return todoService.updateAssignee(id,oldAssigneeId,newAssigneeId);
}

@Override
public Object findAllData(String referenceId, Integer assigneeId) {
    log.info("inside @Class TodoControllerImpl @Method findAllData referenceId : {}, assigneeId :{}", referenceId, assigneeId);
    return todoService.findAllData(referenceId,assigneeId);
}

@Override
public String updateDeadline(Integer id, Date newDueDate) {
    log.info("inside @Class TodoControllerImpl @Method updateDeadline");
    return todoService.updateDeadline(id, newDueDate);
}

@Override
public String updateTodo(TodoUpdateRequest todoUpdateRequest) {
    return todoService.updateTodo(todoUpdateRequest);
}



}
