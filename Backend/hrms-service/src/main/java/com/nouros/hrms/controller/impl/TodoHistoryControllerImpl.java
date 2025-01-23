package com.nouros.hrms.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nouros.hrms.controller.TodoHistoryController;
import com.nouros.hrms.model.TodoHistory;
import com.nouros.hrms.service.TodoHistoryService;
import com.nouros.hrms.wrapper.TodoHistoryData;

import jakarta.validation.Valid;



@Primary
@RestController
@RequestMapping("/TodoHistory")
public class TodoHistoryControllerImpl implements TodoHistoryController{

    @Autowired
    private TodoHistoryService todoHistoryService;

    @Override
    public TodoHistory create(@Valid TodoHistory todoHistory) {
        return todoHistoryService.create(todoHistory);
    }

    @Override
    public Long count(String filter) {
        return todoHistoryService.count(filter);
    }

    @Override
    public List<TodoHistory> search(String filter, @Valid Integer offset, @Valid Integer size, String orderBy, String orderType) {
        return todoHistoryService.search(filter, offset, size, orderBy, orderType);
    }

    @Override
    public TodoHistory update(@Valid TodoHistory todoHistory) {
        return todoHistoryService.update(todoHistory);
    }

  
    @Override
    public String deleteById(@Valid Integer id) {
        return todoHistoryService.softDelete(id);
    }

    @Override
    public TodoHistory findById(@Valid Integer id) {
        return todoHistoryService.findById(id);
    }

    @Override
    public List<TodoHistory> findAllById(@Valid List<Integer> id) {
        return todoHistoryService.findAllById(id);
    }

    @Override
    public TodoHistory createByTodoId(@Valid TodoHistoryData todoHistoryData) {
        return todoHistoryService.createByTodoId(todoHistoryData);
    }


}
