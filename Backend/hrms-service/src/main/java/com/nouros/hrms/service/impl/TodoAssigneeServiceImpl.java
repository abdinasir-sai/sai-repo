package com.nouros.hrms.service.impl;


import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.usermanagement.user.model.User;
import com.enttribe.usermanagement.user.rest.UserRest;
import com.nouros.hrms.model.TodoAssignee;
import com.nouros.hrms.repository.TodoAssigneeRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.TodoAssigneeService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class TodoAssigneeServiceImpl extends AbstractService<Integer, TodoAssignee>
        implements TodoAssigneeService{

    private static final Logger log = LogManager.getLogger(TodoAssigneeServiceImpl.class);       

     @PersistenceContext
    private EntityManager entityManager;

    public TodoAssigneeServiceImpl(GenericRepository<TodoAssignee> assgineeRepository) {
        super(assgineeRepository, TodoAssignee.class);
    }

    @Autowired
    private UserRest userRest;

    @Autowired
    CustomerInfo customerInfo;

    private User getUserContext() {
		return userRest.byUserName(customerInfo.getUsername());
	}

    @Autowired
    private TodoAssigneeRepository assgineeRepository;

    @Override
    public String softDelete(int id) {
       log.info("Inside method softDelete");
        try {
            TodoAssignee todo = super.findById(id);

            if (todo != null) {

                TodoAssignee todo1 = todo;
                todo1.setDeleted(true);
                assgineeRepository.save(todo1);
                log.info("Todo soft deleted successfully");
                return APIConstants.SUCCESS_JSON;
            }
        } catch (Exception e) {
            log.debug("SomeThing Went Wrong While Deleting Todos");
        }
        return APIConstants.FAILURE_JSON;
    }

    @Override
    public void softBulkDelete(List<Integer> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                softDelete(list.get(i));
            }
        }
    }
 
    @Transactional
    @Override
    public TodoAssignee create(TodoAssignee todo) {
     try {
         if(todo.getId() !=null){
         TodoAssignee todoAssignee = findById(todo.getId());
         todoAssignee.setAssignee(todo.getAssignee()); 
         todoAssignee.setAssigneeNotificationFrequency(todo.getAssigneeNotificationFrequency());
         todoAssignee.setAssigneeLastNotification(todo.getAssigneeLastNotification());
         todo.setModifiedTime(new Date());
         todo.setLastModifier(new com.nouros.hrms.model.User(getUserContext()));
         TodoAssignee updatedTodoAssignee = assgineeRepository.save(todoAssignee);
         log.debug("TodoAssignee updated successfully with ID: " + updatedTodoAssignee.getId());
         return updatedTodoAssignee;
         }
         todo.setCreator(new com.nouros.hrms.model.User(getUserContext()));
         todo.setCreatedTime(new Date());
         todo.setModifiedTime(new Date());
         todo.setLastModifier(new com.nouros.hrms.model.User(getUserContext()));
         TodoAssignee savedTodoAssignee = assgineeRepository.save(todo);
         log.info("TodoAssignee created successfully with ID: " + savedTodoAssignee.getId());
         return savedTodoAssignee;
     } catch (Exception e) {
         log.error("Error while creating TodoAssignee: " + e.getMessage());
         return null; 
     }
    }
 
    @Override
 public TodoAssignee update(TodoAssignee todo) {
     try {
         TodoAssignee existingTodoAssignee = assgineeRepository.findById(todo.getId()).orElse(null);
         
         if (existingTodoAssignee == null) {
             log.error("TodoAssignee with ID " + todo.getId() + " not found");
             return null;
         }
         existingTodoAssignee.setAssignee(todo.getAssignee()); 
         existingTodoAssignee.setAssigneeNotificationFrequency(todo.getAssigneeNotificationFrequency());
         existingTodoAssignee.setAssigneeLastNotification(todo.getAssigneeLastNotification());
         
         TodoAssignee updatedTodoAssignee = assgineeRepository.save(existingTodoAssignee);
         log.info("TodoAssignee updated successfully with ID: " + updatedTodoAssignee.getId());
         
         return updatedTodoAssignee;
     } catch (Exception e) {
         log.error("Error while updating TodoAssignee: " + e.getMessage());
         return null;
     }
 }

    @Override
    public TodoAssignee findByTodoAssigneeByAssigneeId(Integer userId,Integer todoId) {
        log.info("Inside @Class TodoAssigneeServiceImpl @method findByTodoAssigneeByAssigneeId");
        TodoAssignee todoAssignee = assgineeRepository.findTodoAssigneeByUserId(userId,todoId);
        log.debug("todoAssignee :{}",todoAssignee);
        return todoAssignee;
    }
 
 

}
