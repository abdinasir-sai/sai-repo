package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.JobOpeningWeightageCriterias;
import com.nouros.hrms.repository.generic.GenericRepository;

@Repository
@Transactional(readOnly = true)
public interface JobOpeningWeightageCriteriasRepository extends GenericRepository<JobOpeningWeightageCriterias> {

	@Query("SELECT jc FROM JobOpeningWeightageCriterias jc WHERE jc.jobOpening.id = :id")
	List<JobOpeningWeightageCriterias> getAllWeightageCriteriasByJobOpening(@Param("id") Integer id);

}
