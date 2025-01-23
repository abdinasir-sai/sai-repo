package com.nouros.hrms.wrapper;

import java.util.Date;
import java.util.List;

import com.nouros.hrms.model.TodoBucket;

import lombok.Data;

@Data
public class TodoAssignmentDto {

    Integer id;
	String taskName;
	Date dueDate;
	Date startDate;
	String progress;
	String priority;
	String repeat;
	String customConfig;
	String attachments;
	String notes;
	TodoBucket todoBucket;
	Boolean isDeleted;
	List<String> userNames;

}
