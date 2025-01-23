package com.nouros.hrms.wrapper;

import java.util.Date;

import lombok.Data;

@Data
public class UpdateStatusWrapper {

	Integer id;
	Boolean pinned;
	String updateStatus;
	Date reminderDate;
	String taskDetails;
	String category;
}
