package com.nouros.hrms.wrapper;

import java.util.Date;
import java.util.List;

public class TodoUpdateRequest {
    private Integer id;
    private List<Integer> oldAssigneeIds;
    private List<Integer> newAssigneeIds;
    private Date newDueDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getOldAssigneeIds() {
        return oldAssigneeIds;
    }

    public void setOldAssigneeIds(List<Integer> oldAssigneeIds) {
        this.oldAssigneeIds = oldAssigneeIds;
    }

    public List<Integer> getNewAssigneeIds() {
        return newAssigneeIds;
    }

    public void setNewAssigneeIds(List<Integer> newAssigneeIds) {
        this.newAssigneeIds = newAssigneeIds;
    }

    public Date getNewDueDate() {
        return newDueDate;
    }

    public void setNewDueDate(Date newDueDate) {
        this.newDueDate = newDueDate;
    }
}
