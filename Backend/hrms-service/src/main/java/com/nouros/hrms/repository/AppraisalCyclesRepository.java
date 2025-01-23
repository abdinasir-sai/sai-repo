package com.nouros.hrms.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.AppraisalCycles;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The AppraisalCyclesRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link AppraisalCycles} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
public interface AppraisalCyclesRepository extends GenericRepository<AppraisalCycles> {


}
