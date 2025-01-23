package com.nouros.hrms.wrapper;



import java.util.Date;

import com.nouros.hrms.model.TodoHistory.Type;

import lombok.Data;

@Data
public class TodoHistoryData {

    private Integer id;
    private String remark;
    private Integer todoId;
    private Integer userId;
    private Date lastRemarkDate;
    private Type type;

}
