package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.LeaveType;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The LeaveTypeRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link LeaveType} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface LeaveTypeRepository extends GenericRepository<LeaveType> {

	@Query(value = "SELECT LT FROM LeaveType LT WHERE LT.applicableDepartment LIKE %?1% AND LT.applicableDesignation LIKE %?2% AND LT.applicableLocation LIKE %?3% ")
	List<LeaveType> findByDepartmentNameAndDesignationNameAndLocationName(@Param("departmentName") String departmentName,@Param("designationName") String designationName, @Param("locationName") String locationName);

	LeaveType findByNameAndCustomerId(@Param ("name") String name,@Param("customerId")Integer customerId);
	
}
