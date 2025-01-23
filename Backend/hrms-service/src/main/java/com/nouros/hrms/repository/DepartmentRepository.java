package com.nouros.hrms.repository;

import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.Department;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The DepartmentRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link Department} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface DepartmentRepository extends GenericRepository<Department> {

	public Department findByName(@Param("departmentName") String departmentName);

	@Query("SELECT d FROM Department d WHERE d.parentDepartment.id = :id")
	public List<Department> findDepartmentByIdDownLevel(@Param("id") int id);

	@Query("SELECT d.id FROM Department d WHERE d.division.id = :id")
	public List<Integer> getDepartmentIdsByDevisionId(@Param("id") Integer id);

	
	@Query(value = "SELECT * FROM DEPARTMENT WHERE ID =:id",nativeQuery = true)
	public Department findByDepartmentId(@Param("id") Integer id);
	
	@Query(value = "SELECT * FROM DEPARTMENT d WHERE  d.DEPARTMENT_LEAD IS NOT NULL",nativeQuery =true)
	 public List<Department> getDeparmentLeadList();

}
