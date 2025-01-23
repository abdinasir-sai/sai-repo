package com.nouros.hrms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedBy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "TODO")
@Filters({@Filter(name = "todoUserIdInFilter",condition = "CREATOR = :userId AND IS_DELETED = 0"),
          @Filter(name = "enableFilterForTodo", condition = " ID in(select TODO_ASSIGNEE.TODO_ID from TODO_ASSIGNEE where TODO_ASSIGNEE.ASSIGNNEE in (:assignee))")})
@FilterDefs({
            @FilterDef(name = "todoUserIdInFilter", parameters = {@ParamDef(name = "userId", type = Integer.class)}),
            @FilterDef(name = "enableFilterForTodo", parameters = {@ParamDef(name = "assignee", type = Integer.class)})
        })public class Todo extends BaseEntitySaaS{

    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
    @Column(columnDefinition = "INT",name = "ID")
    private Integer id;

	@NotNull
	@Size(max = 200)
	@Basic
	@Column(name = "TASK_TITLE")
	private String taskTitle;

    @Size(max = 50)
    @Basic
    @Column(name = "REFERENCE_ID")
    private String referenceId; 

    @Size(max = 200)
	@Basic
	@Column(name = "DESCRIPTION")
	private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ASSIGNOR", columnDefinition = "INT")
    private User assignner;
    
    
    @Basic
    @Column(name="PRIORITY", columnDefinition="ENUM")
    private String priority;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DUE_DATE")
	private Date dueDate;

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	private Date startDate;

    @Basic
    @Column(name="ASSIGNOR_UPDATE_FREQUENCY",columnDefinition="ENUM")
    private String assignnerUpdateFrequency;

    @Basic
	@Column(name = "ASSIGNOR_LAST_NOTIFICATION")
	private Date assignerLastNotification;

    @Basic
	@Column(name = "ESCALATION_NOTIFICATION")
	private boolean escalationNotification;

    @Basic
    @Column(name="TYPE", columnDefinition="ENUM")
    private String type;

    @Basic
    @Column(name="STATUS", columnDefinition="ENUM")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_TASK_ID")
    @JsonIgnore
    private Todo parentTask;

    @OneToMany(mappedBy = "parentTask", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Todo> subTasks = new ArrayList<>();

    @Basic
    @Column(name = "IS_DELETED", columnDefinition = "BOOLEAN DEFAULT 0")
    private boolean isDeleted;

    @Size(max=250)
    @Column(name ="TODO_UPDATE")
    private String todoUpdate;

    public String getTodoUpdate() {
        return todoUpdate;
    }


    public void setTodoUpdate(String todoUpdate) {
        this.todoUpdate = todoUpdate;
    }


    @JsonProperty("parentTaskId")
    public Integer getParentTaskId() {
    if (parentTask != null && Hibernate.isInitialized(parentTask)) {
        return parentTask.getId();
    }
    return null;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAssignner() {
        return assignner;
    }

    public void setAssignner(User assignner) {
        this.assignner = assignner;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getAssignnerUpdateFrequency() {
        return assignnerUpdateFrequency;
    }

    public void setAssignnerUpdateFrequency(String assignnerUpdateFrequency) {
        this.assignnerUpdateFrequency = assignnerUpdateFrequency;
    }


    public Date getAssignerLastNotification() {
        return assignerLastNotification;
    }
    
    public void setAssignerLastNotification(Date assignerLastNotification) {
        this.assignerLastNotification = assignerLastNotification;
    }
    

    public boolean isAscalationNotification() {
        return escalationNotification;
    }

    public void setEscalationNotification(boolean escalationNotification) {
        this.escalationNotification = escalationNotification;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Todo getParentTask() {
        return parentTask;
    }

    public void setParentTask(Todo parentTask) {
        this.parentTask = parentTask;
    }

    public List<Todo> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<Todo> subTasks) {
        this.subTasks = subTasks;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isEscalationNotification() {
        return escalationNotification;
    }

}
