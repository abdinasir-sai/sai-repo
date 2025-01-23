package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nouros.hrms.model.Reminder;
import com.nouros.hrms.repository.generic.GenericRepository;

@Repository
public interface ReminderRepository extends GenericRepository<Reminder> {

	@Query(value = "SELECT * FROM REMINDER WHERE REMINDER_DATE <= CURRENT_DATE AND STATUS= :status AND CUSTOMER_ID = :customerId ORDER BY IS_PINNED DESC, DUE_DATE;", nativeQuery = true)
	List<Reminder> getAllReminder(@Param("status") String status, @Param("customerId") Integer customerId);

	@Query(value = "SELECT * FROM REMINDER WHERE REMINDER_DATE =:reminderDate AND TASK_DETAILS = :taskDetails AND CUSTOMER_ID = :customerId", nativeQuery = true)
	List<Reminder> reminderByDateAndTaskDetails(@Param("reminderDate") String reminderDate,
			@Param("taskDetails") String taskDetails, @Param("customerId") Integer customerId);

}
