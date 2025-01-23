package com.nouros.hrms.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nouros.hrms.model.Reminder;
import com.nouros.hrms.wrapper.UpdateStatusWrapper;

import jakarta.validation.Valid;

@RequestMapping("ReminderController")
public interface ReminderController {
	
	@PostMapping("updateStatus")
	String updateStatus(@Valid @RequestBody UpdateStatusWrapper updateStatusWrapper);
	
	@PostMapping("updateReminderDate")
	String updateReminderDate(@Valid @RequestBody UpdateStatusWrapper updateStatusWrapper);
	
	@GetMapping("getAllReminder")
	List<Reminder> getAllReminder();
	
	@GetMapping("syncReminderUpdate")
	void syncReminderUpdate();
	
	@GetMapping("scheduleSyncReminder")
	void scheduleSyncReminder();
	
	@PostMapping("checkOrCreate")
	boolean checkOrCreate(@Valid @RequestBody UpdateStatusWrapper updateStatusWrapper);
	
	@PostMapping("updatePinned")
	Reminder updatePinned(@RequestBody UpdateStatusWrapper updateStatusWrapper);
}
