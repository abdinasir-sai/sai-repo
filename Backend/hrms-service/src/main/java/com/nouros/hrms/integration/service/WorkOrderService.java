package com.nouros.hrms.integration.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.enttribe.orchestrator.dto.WorkorderDto;
import com.enttribe.orchestrator.execution.model.Workorder;
import com.enttribe.orchestrator.execution.model.WorkorderTask;



/**
 * This interface defines the contract for managing work orders using an orchestrator project controller and task controller.
 * Implementing classes must provide concrete implementations for the specified methods.
 */
public interface WorkOrderService {
	
	/**
     * Deletes a work order with the specified reference ID.
     * @param referenceId The reference ID of the work order to be deleted.
     * @return A string indicating the result of the delete operation.
     */
    public  String deleteWorkorder(java.lang.String referenceId);
    
    /**
     * Creates a new orchestrator project based on the provided workflow work order wrapper.
     * @param workflowWorkorderWrapper The wrapper containing data for creating the orchestrator project.
     * @return The created orchestrator project.
     */
    public  Workorder create(WorkorderDto workflowWorkorderWrapper);
    
     /**
     * Finds a list of orchestrator project tasks by their process instance ID and status.
     * @param processInstanceId The process instance ID of the orchestrator project tasks to be found.
     * @param status The status of the orchestrator project tasks to be found.
     * @return The list of found orchestrator project tasks.
     */
    List<WorkorderTask> findByProcInstaceIdAndStatus(String processInstanceId, Collection<String> status);
    
    /**
     * Finds an orchestrator project by its process instance ID.
     * @param procInstanceId The process instance ID of the orchestrator project to be found.
     * @return The found orchestrator project or null if not found.
     */
    public Workorder findByProcessInstanceId(String procInstanceId) ;
    
    /**
     * Finds a list of orchestrator project tasks by their work order number.
     * @param workorderNumber The work order number of the orchestrator project tasks to be found.
     * @return The list of found orchestrator project tasks.
     */
    public List<WorkorderTask> findTaskByWorkorderNo(String workorderNumber);
    
    /**
     * Completes a task in the orchestrator project with the specified process instance ID and task definition key.
     * @param processInstanceId The process instance ID of the orchestrator project task to be completed.
     * @param taskDefKey The task definition key of the orchestrator project task to be completed.
     * @param arg2 A map of additional arguments required for the completion process.
     * @return A string indicating the result of the completion operation.
     */
    public String complete( String processInstanceId,String taskDefKey,  Map<String, Object> arg2);
}
