package com.nouros.payrollmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.payrollmanagement.model.OtherSalaryComponentAttachment;

@Repository
@Transactional
public interface OtherSalaryComponentAttachmentRepository extends JpaRepository<OtherSalaryComponentAttachment, Integer> {
}
