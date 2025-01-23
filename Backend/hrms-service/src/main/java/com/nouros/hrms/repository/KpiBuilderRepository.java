package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.KpiBuilder;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The KpiBuilderRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link KpiBuilder} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface KpiBuilderRepository extends GenericRepository<KpiBuilder> {

	@Query(value = "select kpi from KpiBuilder kpi where kpi.role=?1")
	List<KpiBuilder> getKPIByRole(String roleName);


}
