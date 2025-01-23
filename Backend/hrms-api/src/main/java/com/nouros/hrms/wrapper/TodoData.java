package com.nouros.hrms.wrapper;

import java.util.Date;
import java.util.List;

import com.nouros.hrms.model.User;

import lombok.Data;

@Data
public class TodoData {
    private Integer id ;
    private String referenceId;
    private String description;
    private String taskTitle;
    private User assignner;
    private String priority;
    // private Date dueDate;
    // private Date startDate;
    private String dueDate;
    private String startDate;
    private String assignnerUpdateFrequency;
    private String status;
    private String type;
    private List<TodoAssigneeData> todoAssigneeData;
    private List<TodoHistoryData> todoHistoryData;
}
