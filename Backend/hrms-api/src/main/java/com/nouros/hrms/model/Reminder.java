package com.nouros.hrms.model;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "REMINDER")
public class Reminder extends BaseEntitySaaS{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	private Integer id;
	
	@Size(max = 100)
	@Basic
	@Column(name="CATEGORY",length = 100)
	private String category;
	
	@Size(max = 255)
	@Basic
	@Column(name="TASK_DETAILS",length = 255)
	private String taskDetails;
	
	@Basic
	@Column(name="DUE_DATE")
	private Date dueDate;
	
	
	public enum Status {
        YET_TO_START,
        DONE,
        DELETED, ALLOCATED
    }
	
	@Enumerated(EnumType.STRING)
	@Column(name="STATUS")
	private Status status;
	
	@Basic
	@Column(name = "REMINDER_DATE", updatable = true)
    @CreatedDate
	private Date reminderDate;
	
	@Size(max = 255)
	@Basic
	@Column(name="OWNER_ROLE",length = 255)
	private String ownerRole;
	
	@Size(max = 255)
	@Basic
	@Column(name="QUESTION",length = 255)
	private String question;
	
	@Column(name="IS_PINNED")
	private Boolean isPinned;

}
