package com.nouros.hrms.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.AppraisalTemplates;
import com.nouros.hrms.repository.generic.GenericRepository;

/**

The AppraisalTemplatesRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
methods that are specific to the {@link AppraisalTemplates} entity.
see GenericRepository
see JpaRepository
*/
@Repository
@Transactional(readOnly = true)
public interface AppraisalTemplatesRepository extends GenericRepository<AppraisalTemplates> {


}
