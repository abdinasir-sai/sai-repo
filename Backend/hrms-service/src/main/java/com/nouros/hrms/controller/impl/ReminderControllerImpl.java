package com.nouros.hrms.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.nouros.hrms.controller.ReminderController;
import com.nouros.hrms.model.Reminder;
import com.nouros.hrms.service.ReminderService;
import com.nouros.hrms.wrapper.UpdateStatusWrapper;

@RestController
public class ReminderControllerImpl implements ReminderController{
	
	@Autowired
	ReminderService reminderService;

	@Override
	public String updateStatus(UpdateStatusWrapper updateStatusWrapper) {
		return reminderService.updateStatus(updateStatusWrapper.getId(),updateStatusWrapper.getUpdateStatus());
	}

	@Override
	public String updateReminderDate(UpdateStatusWrapper updateStatusWrapper) {
		return reminderService.updateReminderDate(updateStatusWrapper.getId(),updateStatusWrapper.getReminderDate());
	}

	@Override
	public List<Reminder> getAllReminder() {
		return reminderService.getAllReminder();
	}

	@Override
	public void syncReminderUpdate() {
		reminderService.syncReminder();
	}

	@Override
	public boolean checkOrCreate(UpdateStatusWrapper updateStatusWrapper) {
		return reminderService.checkOrCreate(updateStatusWrapper.getReminderDate(),updateStatusWrapper.getTaskDetails(),updateStatusWrapper.getCategory());
	}

	@Override
	public void scheduleSyncReminder() {
		 reminderService.scheduleSyncReminder();
	}

	@Override
	public Reminder updatePinned(UpdateStatusWrapper updateStatusWrapper) {
		return reminderService.updatePinned(updateStatusWrapper.getId(),updateStatusWrapper.getPinned());
	}

}
