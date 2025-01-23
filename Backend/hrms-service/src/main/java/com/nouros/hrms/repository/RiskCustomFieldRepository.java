package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.RiskCustomField;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The RiskCustomFieldRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link RiskCustomField} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
public interface RiskCustomFieldRepository extends GenericRepository<RiskCustomField> {

   	 //done done RiskCustomField
   
     @Query(value="select ccf from RiskCustomField ccf where ccf.entityId=:entityId")
    List<RiskCustomField> findAllByEntityId(@Param("entityId") Integer  entityId );
   	 


}
