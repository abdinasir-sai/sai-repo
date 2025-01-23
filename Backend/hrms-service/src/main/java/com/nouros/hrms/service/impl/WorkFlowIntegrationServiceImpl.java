package com.nouros.hrms.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.utils.Utils;
import com.nouros.hrms.service.WorkFlowIntegrationService;
import com.nouros.hrms.util.APIConstants;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Service
public class WorkFlowIntegrationServiceImpl implements WorkFlowIntegrationService {

	private static final Logger log = LogManager.getLogger(WorkFlowIntegrationServiceImpl.class);

	@Autowired
	private EntityManager entityManager;

	@Override
	@Modifying
	@Transactional
	public String updateActions(String processInstanceId) {
		log.info("Inside method updateWorkFlowActions");
		try {
			String updateSql = "UPDATE WORKFLOW_ACTION w set w.ACTIVE = 0 WHERE w.PROCESS_INSTANCE_ID = :processInstanceId";
			Query updateQuery = entityManager.createNativeQuery(updateSql);

			updateQuery.setParameter("processInstanceId", processInstanceId);
			log.debug("processInstanceId attached in Query : {}", processInstanceId);

			int rowsUpdated = updateQuery.executeUpdate();
			log.debug("Number of Rows Updated after updateQuery is : {}", rowsUpdated);
			return APIConstants.SUCCESS_JSON;
		} catch (Exception ex) {
			log.error("Error while updating WorkFlowActions: ", Utils.getStackTrace(ex),ex);
			throw new BusinessException("Error while updating WorkFlowActions", ex.getMessage());
		}

	}

}
