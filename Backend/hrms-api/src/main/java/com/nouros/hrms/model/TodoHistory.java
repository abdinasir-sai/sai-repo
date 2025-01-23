package com.nouros.hrms.model;


import java.util.Date;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "TODO_HISTORY")
@Filters({@Filter(name = "UpdateTodoUserIdInFilter",condition = "CREATOR = :userId AND IS_DELETED = 0")})
@FilterDefs({@FilterDef(name = "UpdateTodoUserIdInFilter",parameters = {@ParamDef(name = "userId", type = Integer.class)})})
public class TodoHistory extends BaseEntitySaaS{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "REMARK", columnDefinition = "TEXT")
    private String remark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TODO_ID", referencedColumnName = "ID", nullable = false)
    private Todo todo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_FK", referencedColumnName = "ID", nullable = false)
    private User user;

    @Basic
    @Column(name = "IS_DELETED", columnDefinition = "BOOLEAN DEFAULT 0")
    private boolean isDeleted;

    @Column(name = "LAST_REMARK_DATE")
    private Date lastRemarkDate;

    @Size(max=250)
    @Column(name ="FROM_DATA")
    private String fromData;

    @Size(max=250)
    @Column(name ="TO_DATA")
    private String toData;

    public enum Type {
        ASSIGN,
        RE_ASSIGN,
        MARK_COMPLETION,
        PROGRESS_UPDATE,
        UPDATE_STATUS
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", columnDefinition = "ENUM")
    private Type type;

    public Date getLastRemarkDate() {
        return lastRemarkDate;
    }

    public void setLastRemarkDate(Date lastRemarkDate) {
        this.lastRemarkDate = lastRemarkDate;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFromData() {
        return fromData;
    }

    public void setFromData(String fromData) {
        this.fromData = fromData;
    }

    public String getToData() {
        return toData;
    }

    public void setToData(String toData) {
        this.toData = toData;
    }
}
