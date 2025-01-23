package com.nouros.hrms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedBy;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Entity
@Table(name = "TODO_ASSIGNEE")
@Filters({@Filter(name = "todoAssigneeUserIdInFilter",condition = "CREATOR = :userId AND IS_DELETED = 0")})
@FilterDefs({@FilterDef(name = "todoAssigneeUserIdInFilter",parameters = {@ParamDef(name = "userId", type = Integer.class)})})
public class TodoAssignee extends BaseEntitySaaS{

    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	@Id
	@Column(columnDefinition = "INT", name = "ID")
	private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TODO_ID", nullable = false)
    private Todo todo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ASSIGNNEE", columnDefinition = "INT")
    private User assignee;

    public enum NotificationFrequency {
        DAILY,
        WEEKLY,
        TWICE,
        ALTERNATIVE
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "ASSIGNEE_NOTIFICATION_FREQUENCY", columnDefinition = "ENUM")
    private NotificationFrequency assigneeNotificationFrequency;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ASSIGNEE_LAST_NOTIFICATION")
    private Date assigneeLastNotification;

    @Basic
    @Column(name = "IS_DELETED", columnDefinition = "BOOLEAN DEFAULT 0")
    private boolean isDeleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Todo getTodo() {
        return todo;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public NotificationFrequency getAssigneeNotificationFrequency() {
        return assigneeNotificationFrequency;
    }

    public void setAssigneeNotificationFrequency(NotificationFrequency assigneeNotificationFrequency) {
        this.assigneeNotificationFrequency = assigneeNotificationFrequency;
    }

    public Date getAssigneeLastNotification() {
        return assigneeLastNotification;
    }

    public void setAssigneeLastNotification(Date assigneeLastNotification) {
        this.assigneeLastNotification = assigneeLastNotification;
    }


    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

}
