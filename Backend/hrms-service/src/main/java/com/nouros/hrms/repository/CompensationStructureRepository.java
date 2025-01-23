package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.CompensationStructure;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The CompensationStructureRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link CompensationStructure} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface CompensationStructureRepository extends GenericRepository<CompensationStructure> {

	List<CompensationStructure> findByDepartmentIdAndTitle(Integer departmentId, String title);

	@Query("select cs from CompensationStructure cs where cs.title=?1")
	CompensationStructure findByTitle(String title);

}
