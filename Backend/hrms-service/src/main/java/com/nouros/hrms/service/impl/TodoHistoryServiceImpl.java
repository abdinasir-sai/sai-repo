package com.nouros.hrms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.usermanagement.user.model.User;
import com.enttribe.usermanagement.user.rest.UserRest;
import com.enttribe.utils.Utils;
import com.nouros.hrms.model.Todo;
import com.nouros.hrms.model.TodoHistory;
import com.nouros.hrms.repository.TodoHistoryRepository;
import com.nouros.hrms.repository.TodoRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.TodoHistoryService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.wrapper.TodoHistoryData;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class TodoHistoryServiceImpl extends AbstractService<Integer, TodoHistory> implements TodoHistoryService {

    private static final Logger log = LogManager.getLogger(TodoServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    public TodoHistoryServiceImpl(GenericRepository<TodoHistory> repository) {
        super(repository, TodoHistory.class);
    }

    @Autowired
    private TodoHistoryRepository repository;

    @Autowired
    private TodoRepository todoRepository;

     @Autowired
    CustomerInfo customerInfo;

    @Autowired
	private UserRest userRest;

    private User getUserContext() {
        return userRest.byUserName(customerInfo.getUsername());
    }


    @Override
    public String softDelete(int id) {
        log.info("Inside method softDelete");
        try {
            Optional<TodoHistory> todoUpdateOptional = repository.findById(id);

            if (todoUpdateOptional.isPresent()) {
                TodoHistory todoUpdate = todoUpdateOptional.get();
                todoUpdate.setDeleted(true);
                repository.save(todoUpdate);
                log.info("TodoUpdate soft deleted successfully");
                return "Success";
            }
        } catch (Exception e) {
            log.error("Error while soft deleting TodoUpdate: ", e);
        }
        return "Failure";
    }

    @Override
    public void softBulkDelete(List<Integer> ids) {
        if (ids != null) {
            for (Integer id : ids) {
                softDelete(id);
            }
        }
    }

    @Override
    public TodoHistory create(TodoHistory todoHistory) {
        try {
            if(todoHistory.getId() != null){
                TodoHistory fetchTodoUpdate = findById(todoHistory.getId());
                fetchTodoUpdate.setRemark(todoHistory.getRemark());
                fetchTodoUpdate.setLastRemarkDate(new Date());
                fetchTodoUpdate.setTodo(todoHistory.getTodo());
                fetchTodoUpdate.setUser(todoHistory.getUser());
                setModifier(fetchTodoUpdate);
                fetchTodoUpdate = repository.save(fetchTodoUpdate);
                log.debug("TodoHistory updated successfully " + fetchTodoUpdate);
                return fetchTodoUpdate;
            }
            todoHistory.setLastRemarkDate(new Date());
            setCreator(todoHistory);
            TodoHistory savedTodoUpdate = repository.save(todoHistory);
            log.info("TodoHistory created successfully with ID: " + savedTodoUpdate.getId());
            return savedTodoUpdate;
        } catch (Exception e) {
            log.error("Error while creating TodoHistory: " + e.getMessage());
            return null;
        }
    }

    @Override
    public TodoHistory createByTodoId(TodoHistoryData todoHistoryData) {
        try {
            TodoHistory todoHistory = new TodoHistory();
            if (todoHistoryData.getTodoId()!=null){
               Optional<Todo> optTodo  = todoRepository.findById(todoHistoryData.getTodoId());
               if(optTodo.isPresent()){
                Todo todo = optTodo.get();
                todoHistory.setTodo(todo);
               }
            }
            todoHistory.setRemark(todoHistoryData.getRemark());
            todoHistory.setType(todoHistoryData.getType());
            User user = userRest.findUserById(todoHistoryData.getUserId());
            todoHistory.setUser(new com.nouros.hrms.model.User(user));
            setCreator(todoHistory);
            todoHistory = repository.save(todoHistory);
            log.debug("TodoHistory updated successfully todoHistory :{}" , todoHistory);
            return todoHistory;
        } catch (Exception e) {
            log.error("Error while creating TodoHistory: {} , Exception : {}", e.getMessage(), Utils.getStackTrace(e));
            throw new BusinessException("Getting error while creating TodoHistory Exception :{}",e.getMessage());
        }
    }

    private void setCreator(TodoHistory todoUpdate){
        User user = getUserContext();
        todoUpdate.setCreator(new com.nouros.hrms.model.User(user));
        todoUpdate.setCreatedTime(new Date());
        todoUpdate.setModifiedTime(new Date());
        todoUpdate.setLastModifier(new com.nouros.hrms.model.User(user));
    }

    private void setModifier(TodoHistory todoUpdate){
        User user = getUserContext();
        todoUpdate.setModifiedTime(new Date());
        todoUpdate.setLastModifier(new com.nouros.hrms.model.User(user));
    }

    @Override
    public TodoHistory update(TodoHistory todoHistory) {
        try {
            Optional<TodoHistory> existingTodoUpdateOptional = repository.findById(todoHistory.getId());

            if (existingTodoUpdateOptional.isPresent()) {
                TodoHistory existingTodoUpdate = existingTodoUpdateOptional.get();
                existingTodoUpdate.setRemark(todoHistory.getRemark());
                existingTodoUpdate.setTodo(todoHistory.getTodo());
                existingTodoUpdate.setUser(todoHistory.getUser());
                TodoHistory updatedTodoUpdate = repository.save(existingTodoUpdate);
                log.info("TodoUpdate updated successfully with ID: " + updatedTodoUpdate.getId());

                Todo todo = updatedTodoUpdate.getTodo();
            if (todo != null) {
                todo.setTodoUpdate(todoHistory.getRemark());
                todoRepository.save(todo);
                log.info("TodoUpdate remark updated successfully in Todo entity for task: " + todo.getTaskTitle());
            }
                return updatedTodoUpdate;
            } else {
                log.error("TodoUpdate with ID " + todoHistory.getId() + " not found");
                return null;
            }
        } catch (Exception e) {
            log.error("Error while updating TodoUpdate: " + e.getMessage());
            return null;
        }
    }


    @Override
    public List<TodoHistory> findTodoHistoryByTodoIdAndTodoAssignee(Integer todoId, Integer assigneeId) {
        log.debug("Inside @class TodoHistoryServiceImpl @method findTodoHistoryByTodoIdAndTodoAssignee todoId : {}, assigneeId : {} ", todoId , assigneeId);
        if (todoId == null || assigneeId == null) {
            log.warn("Invalid input: todoId or assigneeId is null.");
            return new ArrayList<>(); 
        }
        try {
            return repository.findTodoHistoryByTodoIdAndTodoAssignee(todoId, assigneeId); 
        } catch (Exception e) {
            log.error("Exception occurred while fetching TodoHistory for todoId: {} and assigneeId: {}. Error: {}", todoId, assigneeId, e.getMessage(), e);
            return new ArrayList<>(); 
        }
 
    }




   

}
