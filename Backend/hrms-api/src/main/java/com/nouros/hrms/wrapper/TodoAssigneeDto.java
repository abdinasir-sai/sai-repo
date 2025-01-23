package com.nouros.hrms.wrapper;

import java.util.Date;

import com.enttribe.usermanagement.user.model.User;

import lombok.Data;

@Data
public class TodoAssigneeDto {

    private Integer id;
    private User assignee;
    private String assigneeNotificationFrequency;
    private Date assigneeLastNotification;
    private String status;
     
}
