package com.nouros.hrms.integration.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.orchestrator.dto.WorkorderDto;
import com.enttribe.orchestrator.execution.controller.IWorkorderController;
import com.enttribe.orchestrator.execution.controller.IWorkorderTaskController;
import com.enttribe.orchestrator.execution.model.Workorder;
import com.enttribe.orchestrator.execution.model.WorkorderTask;
import com.nouros.hrms.integration.service.WorkOrderService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implementation of the {@link WorkOrderService} interface that provides functionality
 * for managing work orders using an orchestrator project controller and task controller.
 */
@Service
public class WorkOrderServiceImpl implements WorkOrderService {
  
  private static final Logger log = LogManager.getLogger(WorkOrderServiceImpl.class);

  	/**
     * The orchestrator project controller used for managing work orders.
     */
 	@Autowired
	private IWorkorderController workorderController;
	
	/**
     * The orchestrator project task controller used for managing work order tasks.
     */
	@Autowired
	private IWorkorderTaskController workorderTaskController;
  
    /**
     * Deletes a work order with the specified reference ID.
     * @param referenceId The reference ID of the work order to be deleted.
     * @return A string indicating the result of the delete operation.
     */
  public String deleteWorkorder(String referenceId) {
    log.info("WorkOrderServiceImpl  deleteWorkorder:{}",referenceId);
    return workorderController.deleteWorkorder(referenceId);
  }

  /**
   * Creates a new orchestrator project based on the provided workflow work order wrapper.
   * @param workflowWorkorderWrapper The wrapper containing data for creating the orchestrator project.
   * @return The created orchestrator project.
   */
  @Override
  public Workorder create(WorkorderDto workflowWorkorderWrapper) {
    log.info("WorkOrderServiceImpl  create:{}",workflowWorkorderWrapper);
     return workorderController.create(workflowWorkorderWrapper);
  }

	/**
   * Finds an orchestrator project by its process instance ID.
   * @param procInstanceId The process instance ID of the orchestrator project to be found.
   * @return The found orchestrator project or null if not found.
   */
  @Override
  public Workorder findByProcessInstanceId(String procInstanceId) {
    log.info("WorkOrderServiceImpl  findByProcessInstanceId procInstanceId :{}",procInstanceId);
     return workorderController.findByProcessInstanceId(procInstanceId);
  }
	
  /**
   * Finds a list of orchestrator project tasks by their process instance ID and status.
   * @param processInstanceId The process instance ID of the orchestrator project tasks to be found.
   * @param status            The status of the orchestrator project tasks to be found.
   * @return The list of found orchestrator project tasks.
   */	
  @Override
  public List<WorkorderTask> findByProcInstaceIdAndStatus(String processInstanceId, Collection<String> status){
    log.info("WorkOrderServiceImpl  findByProcInstaceIdAndStatus procInstanceId :{} status:{}",processInstanceId,status);
    return workorderTaskController.findByProcInstaceIdAndStatus( processInstanceId, status);
  }
  
   /**
   * Finds a list of orchestrator project tasks by their work order number.
   * @param workorderNumber The work order number of the orchestrator project tasks to be found.
   * @return The list of found orchestrator project tasks.
   */
  @Override
  public List<WorkorderTask> findTaskByWorkorderNo(String workorderNumber)
  {
    log.info("WorkOrderServiceImpl  findTaskByWorkorderNo workorderNumber :{}",workorderNumber);
    return workorderTaskController.findByWorkorderNo(workorderNumber);
  }
  
  /**
   * Completes a task in the orchestrator project with the specified process instance ID and task definition key.
   * @param processInstanceId The process instance ID of the orchestrator project task to be completed.
   * @param taskDefKey        The task definition key of the orchestrator project task to be completed.
   * @param arg2              A map of additional arguments required for the completion process.
   * @return A string indicating the result of the completion operation.
   */
  @Override
  public String complete( String processInstanceId,String taskDefKey,  Map<String, Object> arg2){
    log.info("WorkOrderServiceImpl  complete processInstanceId :{} taskDefKey:{} arg2:{}",processInstanceId,taskDefKey,arg2);
    return    workorderTaskController.complete(processInstanceId, taskDefKey, arg2);
  }

}
