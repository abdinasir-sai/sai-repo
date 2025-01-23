package com.nouros.hrms.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.JobBrief;
import com.nouros.hrms.repository.generic.GenericRepository;

@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface JobBriefRepository extends GenericRepository<JobBrief> {

	JobBrief findByPostingTitle(String postingTitle);
}
