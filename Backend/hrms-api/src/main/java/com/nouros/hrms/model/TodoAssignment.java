package com.nouros.hrms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "TO_DO_ASSIGNMENT")
@Filters(value = { @Filter(name = "todoAssignmentUserIdInFilter",condition = "(ID IN (SELECT m.TO_DO_ASSIGNMENT_ID from  USER_TO_DO_ASSIGNMENT_MAPPING  m where m.USER_ID_PK = :userId)  OR CREATOR = :userId ) AND IS_DELETED=0")})
@FilterDefs(value = { @FilterDef(name = "todoAssignmentUserIdInFilter", parameters = {@ParamDef(name = "userId", type = Integer.class)})})
public class TodoAssignment extends BaseEntitySaaS{

	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	@Id
	@Column(columnDefinition = "INT")
	private Integer id;

	@NotNull
	@Size(max = 200)
	@Basic
	@Column(name = "TASK_NAME")
	private String taskName;

	
	@Basic
	@Column(name = "DUE_DATE")
	private Date dueDate;

	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	private Date startDate;
	
	@Size(max = 200)
	@Basic
	@Column(name = "PROGRESS")
	private String progress;

	
	@Size(max = 200)
	@Basic
	@Column(name = "PRIORITY")
	private String priority;

	
	@Size(max = 200)
	@Basic
	@Column(name = "REPEAT")
	private String repeat;

	
	@Size(max = 200)
	@Basic
	@Column(name = "CUSTOM_CONFIG")
	private String customConfig;

	
	@Size(max = 250)
	@Basic
	@Column(name = "ATTACHMENTS")
	private String attachments;

	@Basic
	@Column(columnDefinition = "LONGTEXT", name = "NOTES")
	private String notes;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BUCKET_ID", columnDefinition = "INT")
	private TodoBucket todoBucket;

	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH,orphanRemoval = true)
	@JoinTable(name = "USER_TO_DO_ASSIGNMENT_MAPPING",joinColumns = { @JoinColumn(name = "TO_DO_ASSIGNMENT_ID") }, inverseJoinColumns = {
	@JoinColumn(name = "USER_ID_PK", referencedColumnName = "ID") })
	private List<User> users = new ArrayList<>();
	
	 @Column(name = "IS_DELETED", columnDefinition = "bit(1) default 0", nullable = false)
	  private Boolean isDeleted;

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
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

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getRepeat() {
		return repeat;
	}

	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}

	public String getCustomConfig() {
		return customConfig;
	}

	public void setCustomConfig(String customConfig) {
		this.customConfig = customConfig;
	}

	public String getAttachments() {
		return attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public TodoBucket getTodoBucket() {
		return todoBucket;
	}

	public void setTodoBucket(TodoBucket todoBucket) {
		this.todoBucket = todoBucket;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	
	
}
