package com.nouros.hrms.wrapper;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class TodoDto {

    private Integer id;
    private String taskTitle;
    private String description;
    private String priority;
    private String type;
    private Date dueDate;
    private String assignorUpdateFrequency;
    private Date assigneeLastNotification;
    private Boolean escalationNotification;
    private Boolean isSplit;
    private String status;
    private List<TodoDto> todoDtos;
    List<TodoAssigneeDto> todoAssignees;
    private Integer parentTaskId;
        
}
