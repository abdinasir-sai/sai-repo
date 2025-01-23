package com.nouros.hrms.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nouros.hrms.controller.TodoAssigneeController;
import com.nouros.hrms.model.TodoAssignee;
import com.nouros.hrms.service.TodoAssigneeService;

import jakarta.validation.Valid;

@Primary
@RestController
@RequestMapping("/TodoAssignee")
public class TodoAssigneeControllerImpl implements TodoAssigneeController{

    @Autowired
    private TodoAssigneeService assigneeService;

    @Override
    public TodoAssignee create(TodoAssignee todoAssignee) {
       return assigneeService.create(todoAssignee);
    }

    @Override
    public Long count(String filter) {
       return assigneeService.count(filter);
    }

    @Override
    public List<TodoAssignee> search(String filter, @Valid Integer offset, @Valid Integer size, String orderBy,
            String orderType) {
                return assigneeService.search(filter, offset, size, orderBy, orderType);
    }

    @Override
    public TodoAssignee update(TodoAssignee todoAssignee) {
        return assigneeService.update(todoAssignee);
    }

    @Override
    public String deleteById(@Valid Integer id) {
        return assigneeService.softDelete(id);
    }

    @Override
    public TodoAssignee findById(@Valid Integer id) {
       return assigneeService.findById(id);
    }

    @Override
    public List<TodoAssignee> findAllById(@Valid List<Integer> ids) {
        return assigneeService.findAllById(ids);
    }


}
