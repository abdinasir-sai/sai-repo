package com.nouros.payrollmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.payrollmanagement.model.HrmsSystemConfig;
/**

The HrmsSystemConfigRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link HrmsSystemConfig} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
public interface HrmsSystemConfigRepository extends GenericRepository<HrmsSystemConfig> {

    @Query(value = "SELECT hr.KEY_VALUE FROM HRMS_SYSTEM_CONFIG hr WHERE hr.KEY_NAME = :keyName ", nativeQuery = true  )
   List<String> getKey(@Param("keyName") String keyName);

    @Query("Select hr.keyName , hr.keyValue from HrmsSystemConfig hr ")
    List<Object[]> getPairedValue();
}
