package com.nouros.hrms.service;

import java.util.Date;
import java.util.List;

import com.nouros.hrms.model.Reminder;

public interface ReminderService {
	
	String updateStatus(Integer id,String status);
	
	String updateReminderDate(Integer id,Date reminderDate);
	
	List<Reminder> getAllReminder();
	
	void syncReminder();
	
	boolean checkOrCreate(Date reminderDate , String taskDetails,String category);

	void scheduleSyncReminder();

	void reviewJobOffer(Integer applicantId, Integer jobOpeningId, String jobOpeningPosition, String candiDateName);

	void reviewCandidateDecideYouWantToHire(Integer applicantId, Integer jobOpeningId, String gender,String candiDateName);
	
	Reminder updatePinned(Integer id,Boolean pinned);

}
