package com.nouros.payrollmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.payrollmanagement.model.GradeMetaInfo;

/**

The GradeMetaInfoRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link GradeMetaInfo} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
public interface GradeMetaInfoRepository extends GenericRepository<GradeMetaInfo> {


	@Query("select rd from GradeMetaInfo rd where rd.grade = :gradeNumber ")
    List<GradeMetaInfo> getGradeByGradeNumber(@Param("gradeNumber") String gradeNumber);

}
