package com.nouros.hrms.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.Successor;
import com.nouros.hrms.repository.generic.GenericRepository;

@Repository
@Transactional(readOnly = true)
public interface SuccessorRepository extends GenericRepository<Successor> {

}
