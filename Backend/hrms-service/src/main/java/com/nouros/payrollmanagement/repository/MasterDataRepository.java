package com.nouros.payrollmanagement.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.payrollmanagement.model.MasterData;

@Repository
@Transactional
public interface MasterDataRepository extends GenericRepository<MasterData> {
	
	  @Query("SELECT md.code FROM MasterData md WHERE md.name=:name and md.type=:type and md.subType=:subType AND md.code IS NOT NULL")
	    Integer getCodeByNameAndTypeAndSubType(@Param("name") String name, @Param("type") String type, @Param("subType") String subType);
     
	 
}
