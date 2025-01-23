/* (C)2024 */
package com.nouros.hrms.repository;

import com.nouros.hrms.model.Relocation;
import com.nouros.hrms.repository.generic.GenericRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * The RelocationRepository interface is a extension of {@link GenericRepository}. It's purpose is to provide additional
 * methods that are specific to the {@link Relocation} entity.
 * see GenericRepository
 * see JpaRepository
 */
@Repository
@Transactional(readOnly = true)
public interface RelocationRepository extends GenericRepository<Relocation> {}
