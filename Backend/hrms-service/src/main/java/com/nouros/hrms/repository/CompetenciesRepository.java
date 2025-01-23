package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.Competencies;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The CompetenciesRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link Competencies} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
public interface CompetenciesRepository extends GenericRepository<Competencies> {


	 List<Competencies> findAll();
	
}
