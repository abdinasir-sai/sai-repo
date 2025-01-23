package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nouros.hrms.controller.TodoAssignmentController;
import com.nouros.hrms.model.TodoAssignment;
import com.nouros.hrms.service.TodoAssignmentService;
import com.nouros.hrms.wrapper.TodoAssignmentDto;

@Primary
@RestController
@RequestMapping("/TodoAssignment")
public class TodoAssignmentControllerImpl implements TodoAssignmentController{

	private static final Logger log = LogManager.getLogger(TodoAssignmentControllerImpl.class);

	  @Autowired
	  private TodoAssignmentService todoAssignmentService;

	  @Override
	  //@TriggerBPMN(entityName = "TodoAssignment", appName = "HRMS_APP_NAME")
	  public TodoAssignment create(TodoAssignment todoAssignment) {
		  log.info("inside @class TodoAssignmentControllerImpl @method create");
	    return todoAssignmentService.create(todoAssignment);
	  }

	  
	  @Override
	  public Long count(String filter) {
	    return todoAssignmentService.count(filter);
	  }

	  @Override
	  public List<TodoAssignment> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return todoAssignmentService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  public TodoAssignment update(TodoAssignment todoAssignment) {
	    return todoAssignmentService.update(todoAssignment);
	  }

	  @Override
	  public TodoAssignment findById(Integer id) {
	    return todoAssignmentService.findById(id);
	  }

	  @Override
	  public List<TodoAssignment> findAllById(List<Integer> id) {
	    return todoAssignmentService.findAllById(id);
	  }

	  @Override
	  public String deleteById(Integer id) {
		 return todoAssignmentService.softDelete(id);
	  }


	@Override
	public TodoAssignment createTodo(TodoAssignmentDto todoAssignmentDto) {
		return todoAssignmentService.createTodo(todoAssignmentDto);
	}


	@Override
	public TodoAssignment updateTodo(TodoAssignmentDto todoAssignmentDto) {
		return todoAssignmentService.updateTodo(todoAssignmentDto);
	}
	

	
	
}
