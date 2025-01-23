package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.InterviewFeedback;
import com.nouros.hrms.repository.generic.GenericRepository;

@Repository
@Transactional(readOnly = true)
public interface InterviewFeedbackRepository extends GenericRepository<InterviewFeedback> {

	@Query("SELECT if FROM InterviewFeedback if WHERE if.interview.id = :interviewId ")
	List<InterviewFeedback> findAllByInterviewId(@Param("interviewId") Integer interviewId);

}
