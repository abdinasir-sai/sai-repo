package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.Skill;
import com.nouros.hrms.repository.generic.GenericRepository;

/**
 * 
 * The SkillRepository interface is a extension of {@link GenericRepository}.
 * It's purpose is to provide additional methods that are specific to the
 * {@link Skill} entity. see GenericRepository see JpaRepository
 */
@Repository
@Transactional(readOnly = true)
//@JaversSpringDataAuditable
public interface SkillRepository extends GenericRepository<Skill> {

	@Query(value = "SELECT * FROM SKILL WHERE NAME =:skill AND CUSTOMER_ID = :customerId", nativeQuery = true)
	Skill findByName(@Param("skill") String skill, @Param("customerId") Integer customerId);

}
