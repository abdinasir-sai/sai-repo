package com.nouros.hrms.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.namemanagement.model.CustomNumberValues.Status;
import com.enttribe.product.namemanagement.rest.ICustomNumberValuesRest;
import com.enttribe.product.namemanagement.utils.wrapper.NameGenerationWrapperV2;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.usermanagement.user.model.User;
import com.enttribe.usermanagement.user.rest.UserRest;
import com.nouros.hrms.model.Department;
import com.nouros.hrms.model.Designation;
import com.nouros.hrms.model.JobOpening;
import com.nouros.hrms.model.OrgChartDesignation;
import com.nouros.hrms.model.PlannedOrgChart;
import com.nouros.hrms.repository.DepartmentRepository;
import com.nouros.hrms.repository.DesignationRepository;
import com.nouros.hrms.repository.JobOpeningRepository;
import com.nouros.hrms.repository.OrgChartDesignationRepository;
import com.nouros.hrms.repository.PlannedOrgChartRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.OrgChartDesignationService;
import com.nouros.hrms.service.PlannedOrgChartService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.PlannedOrgChartDto;

import jakarta.validation.Valid;

@Service
public class PlannedOrgChartServiceImpl extends AbstractService<Integer, PlannedOrgChart>
		implements PlannedOrgChartService {

	public PlannedOrgChartServiceImpl(GenericRepository<PlannedOrgChart> repository) {
		super(repository, PlannedOrgChart.class);
	}

	@Autowired
	private PlannedOrgChartRepository plannedOrgChartRepository;

	@Autowired
	private OrgChartDesignationRepository orgChartDesignationRepository;

	@Autowired
	private DesignationRepository designationRepository;

	@Autowired
	ICustomNumberValuesRest customNumberValuesRest;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private JobOpeningRepository jobOpeningRepository;

	@Autowired
	CustomerInfo customerInfo;

	@Autowired
	private UserRest userRest;

	@Autowired
	private CommonUtils commonUtils;

	private static final Logger log = LogManager.getLogger(PlannedOrgChartServiceImpl.class);

	private User getUserContext() {
		return userRest.byUserName(customerInfo.getUsername());
	}

//	@Override
//	public PlannedOrgChart create(PlannedOrgChart plannedOrgChart) {
//		log.info("inside @class PlannedOrgChartServiceImpl @method create");
//		try {
//			int count = 0;
//			if (plannedOrgChart != null && plannedOrgChart.getDepartment() != null
//					&& plannedOrgChart.getDepartment().getId() != null) {
//				count = plannedOrgChartRepository
//						.countPlannedOrgChartDepartmentId(plannedOrgChart.getDepartment().getId());
//			}
//
//			log.info("Going to set Department");
//
//			Department department = null;
//
//			if (plannedOrgChart != null && plannedOrgChart.getDepartment() != null) {
//				department = departmentRepository.findByDepartmentId(plannedOrgChart.getDepartment().getId());
//			}
//
//			log.debug("Going to set Department found is : {}", department);
//
//			if (department != null) {
//				plannedOrgChart.setDepartment(department);
//			}
//
//			Designation designation = null;
//
//			if (plannedOrgChart != null && plannedOrgChart.getDesignation() != null) {
//				designation = designationRepository.findByDesignationId(plannedOrgChart.getDesignation().getId());
//			}
//			log.debug("Going to set Designation  found is : {}", designation);
//			if (designation != null) {
//				plannedOrgChart.setDesignation(designation);
//			}
//
//			log.debug("inside @class PlannedOrgChartServiceImpl @method create Department Count : {}", count);
//
//			if (plannedOrgChart != null && plannedOrgChart.getId() != null) {
//				log.info("Going to update EXISTING Planned org chart {}", plannedOrgChart.getId());
//				return update(plannedOrgChart);
//			} else if (count >= 1) {
//				log.error(
//						"inside @class PlannedOrgChartServiceImpl @method create PlannedOrgChart Already Created By Given Department Id");
//				throw new BusinessException("PlannedOrgChart Already Created By Given Department Id");
//			} else {
//
//				log.debug("Going to save  {}", plannedOrgChart);
//				plannedOrgChart = plannedOrgChartRepository.save(plannedOrgChart);
//
//				setPlannedOrgChartIdInOCDesignation(plannedOrgChart);
//
//				return plannedOrgChart;
//
//			}
//		} catch (BusinessException ex) {
//			throw new BusinessException("error while creating PlannedOrgChart ", ex.getMessage());
//		}
//	}

	@Override
	public PlannedOrgChart create(PlannedOrgChart plannedOrgChart) {
		log.info("inside @class PlannedOrgChartServiceImpl @method create");
		try {
			int count = 0;

			Integer userId = customerInfo.getUserId();

			log.debug("inside @class PlannedOrgChartServiceImpl @method create userId is : {}", userId);
			if (plannedOrgChart != null && plannedOrgChart.getDepartment() != null
					&& plannedOrgChart.getDepartment().getId() != null && userId != null) {

				log.debug(" Inside @create  customerId is : {}", commonUtils.getCustomerId());

				count = plannedOrgChartRepository.countPlannedOrgChartDepartmentIdAndUser(
						plannedOrgChart.getDepartment().getId(), userId, commonUtils.getCustomerId());
			}

			log.info("Going to set Department");

			Department department = null;

			if (plannedOrgChart != null && plannedOrgChart.getDepartment() != null) {
				department = departmentRepository.findByDepartmentId(plannedOrgChart.getDepartment().getId());
			}

			log.debug("Going to set Department found is : {}", department);

			if (department != null) {
				plannedOrgChart.setDepartment(department);
			}

			Designation designation = null;

			if (plannedOrgChart != null && plannedOrgChart.getDesignation() != null) {
				log.debug("inside @class PlannedOrgChartServiceImpl @method create customerId is : {}",
						commonUtils.getCustomerId());
				designation = designationRepository.findByDesignationId(plannedOrgChart.getDesignation().getId(),
						commonUtils.getCustomerId());
			}
			log.debug("Going to set Designation  found is : {}", designation);
			if (designation != null) {
				plannedOrgChart.setDesignation(designation);
			}

			log.debug("inside @class PlannedOrgChartServiceImpl @method create Department Count : {}", count);

			if (plannedOrgChart != null && plannedOrgChart.getId() != null) {
				log.info("Going to update EXISTING Planned org chart {}", plannedOrgChart.getId());
				return update(plannedOrgChart);
			} else if (count >= 1) {
				log.error(
						"inside @class PlannedOrgChartServiceImpl @method create PlannedOrgChart Already Created By Given Department Id");
				throw new BusinessException("PlannedOrgChart Already Created By Given User With Same Department Id");
			} else {

				log.debug("Going to save in else condition {}", plannedOrgChart);
				PlannedOrgChart plannedOrgChartUpdated = plannedOrgChartRepository.save(plannedOrgChart);

				setPlannedOrgChartIdInOCDesignation(plannedOrgChartUpdated);

				return plannedOrgChartUpdated;

			}
		} catch (BusinessException ex) {
			throw new BusinessException("error while creating PlannedOrgChart ", ex.getMessage());
		}
	}

	private void setPlannedOrgChartIdInOCDesignation(PlannedOrgChart plannedOrgChart) {

		log.info("inside @class PlannedOrgChartServiceImpl @method setPlannedOrgChartIdInOCDesignation");
		if (plannedOrgChart != null && plannedOrgChart.getDepartment() != null) {
//			List<OrgChartDesignation> orgChartDesignationList = orgChartDesignationRepository
//					.findByDepartmentId(plannedOrgChart.getDepartment().getId());

			Integer userId = customerInfo.getUserId();

			log.debug("inside  @method setPlannedOrgChartIdInOCDesignation userId is : {}", userId);

			log.debug(" Inside @setPlannedOrgChartIdInOCDesignation  customerId is : {}", commonUtils.getCustomerId());
			List<OrgChartDesignation> orgChartDesignationList = orgChartDesignationRepository
					.findOrgCharDesignationByDepartmentAndUserId(plannedOrgChart.getDepartment().getId(), userId,
							commonUtils.getCustomerId());

			log.debug("OrgChartDesignation List is  {}", orgChartDesignationList);

			if (orgChartDesignationList != null && !orgChartDesignationList.isEmpty()) {

				for (OrgChartDesignation orgChartDesignation : orgChartDesignationList) {

					if (orgChartDesignation.getOrgChartId() == null) {
						log.debug("Going to set PlannedOrgChart Id  {}", plannedOrgChart.getId());
						orgChartDesignation.setOrgChartId(plannedOrgChart);
						orgChartDesignationRepository.save(orgChartDesignation);
					}
				}
			}

		}
	}

	@Override
	public PlannedOrgChart update(PlannedOrgChart plannedOrgChart) {
		log.info("inside @class PlannedOrgChartServiceImpl @method update");
		try {
			if (plannedOrgChart != null && plannedOrgChart.getId() != null) {

				log.debug("inside @class PlannedOrgChartServiceImpl @method update id is : {} ",
						plannedOrgChart.getId());

				setPlannedOrgChartIdInOCDesignation(plannedOrgChart);

				return plannedOrgChartRepository.save(plannedOrgChart);
			}
			log.info("inside @class PlannedOrgChartServiceImpl @method update id is null");
			return null;

		} catch (BusinessException ex) {
			throw new BusinessException("error while updating PlannedOrgChart ", ex.getMessage());
		}

	}

	@Override
	public void softDelete(int id) {

		PlannedOrgChart plannedOrgChart = super.findById(id);

		if (plannedOrgChart != null) {

			PlannedOrgChart plannedOrgChart1 = plannedOrgChart;
			plannedOrgChartRepository.save(plannedOrgChart1);

		}
	}

	@Override
	public void softBulkDelete(List<Integer> list) {

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				softDelete(list.get(i));
			}
		}

	}

	@Override
	public List<PlannedOrgChart> searchPlannedOrgChartByDeparmentId(Integer departmentId) {
		log.info("inside @class PlannedOrgChartServiceImpl @method searchPlannedOrgChartByDeparmentId");

		try {
			log.debug("Deparment id is : {}", departmentId);
			log.debug(" Inside @searchPlannedOrgChartByDeparmentId  customerId is : {}", commonUtils.getCustomerId());
			List<PlannedOrgChart> plannedOrgChartList = plannedOrgChartRepository.findAllByDepartmentId(departmentId,
					commonUtils.getCustomerId());
			if (plannedOrgChartList.isEmpty()) {
				throw new BusinessException("No PlannedOrgChart records found for given departmentId");
			}

			return plannedOrgChartList;
		} catch (BusinessException ex) {
			throw new BusinessException("Error while searching Planned OrgChart By DepartmentId", ex.getMessage());
		}
	}

	@Override
	public PlannedOrgChartDto searchOrgChartDesignationsByDepartmentId(Integer departmentId) {
		log.info("inside @class PlannedOrgChartServiceImpl @method searchOrgChartDesignationsByDepartmentId");
		try {
			PlannedOrgChartDto plannedOrgChartDto = new PlannedOrgChartDto();

			log.info("Department Id is : {}", departmentId);

			log.info("Show Org chart to Self Department");
			Integer userId = customerInfo.getUserId();
			log.debug("User ID  is : {}", userId);

			log.debug(" Inside @searchOrgChartDesignationsByDepartmentId  customerId is : {}",
					commonUtils.getCustomerId());

			List<OrgChartDesignation> orgChartDesignationList = orgChartDesignationRepository
					.findOrgCharDesignationByDepartmentAndUserId(departmentId, userId, commonUtils.getCustomerId());
			log.debug("Orgchart designation list is  : {}", orgChartDesignationList);
			plannedOrgChartDto.setOrgChartDesignation(orgChartDesignationList);

			PlannedOrgChart plannedOrgChartExist = plannedOrgChartRepository.findByDepartmentIdAndUserId(departmentId,
					userId, commonUtils.getCustomerId());
			log.debug("plannedOrgChartExist is  : {}", plannedOrgChartExist);
			if (plannedOrgChartExist != null) {
				plannedOrgChartDto.setPlannedOrgChart(plannedOrgChartExist);
			}

			return plannedOrgChartDto;

		} catch (BusinessException ex) {
			throw new BusinessException("error while searchOrgChartDesignationsByDepartmentId ", ex.getMessage());
		}

	}

	@Override
	public String deletePlannedOrgChartByDepartmentId(Integer departmentId) {
		log.info("inside @class PlannedOrgChartServiceImpl @method deletePlannedOrgChartByDepartmentId");

		try {

			Integer userId = customerInfo.getUserId();
			log.debug("User ID  is : {}", userId);

			log.debug(" Inside @deletePlannedOrgChartByDepartmentId  customerId is : {}", commonUtils.getCustomerId());
			PlannedOrgChart plannedOrgChart = plannedOrgChartRepository.findByDepartmentIdAndUserId(departmentId,
					userId, commonUtils.getCustomerId());

			log.debug("plannedOrgChart  is : {}", plannedOrgChart);

			if (plannedOrgChart != null && plannedOrgChart.getId() != null) {

				List<OrgChartDesignation> orgChartDesignations = orgChartDesignationRepository
						.findByPlannedOrgChartId(plannedOrgChart.getId(), commonUtils.getCustomerId());
				log.debug("inside @class PlannedOrgChartServiceImpl orgChartDesignations is: {}", orgChartDesignations);

				OrgChartDesignationService orgChartDesignationService = ApplicationContextProvider
						.getApplicationContext().getBean(OrgChartDesignationService.class);

				if (orgChartDesignations != null) {
					for (OrgChartDesignation orgChartDesignation : orgChartDesignations) {
						orgChartDesignationService.deleteOrgChartDesignationById(orgChartDesignation.getId());
					}
				}
				plannedOrgChartRepository.delete(plannedOrgChart);
				log.info("Deleted PlannedOrgChart with departmentId: {}", departmentId);

				return APIConstants.SUCCESS_JSON;
			}
		} catch (BusinessException ex) {
			String errorMessage = "Error while deleting PlannedOrgChart by Department Id";
			throw new BusinessException(errorMessage, ex.getMessage());
		}

		return null;
	}

	@Override
	public String createDesignationByApprovedPlannedOrgChart(@Valid Integer plannedOrgChartId,
			@Valid String processInstanceId) {
		log.info("inside @class PlannedOrgChartServiceImpl @method createDesignationByApprovedPlannedOrgChart");

		try {
			PlannedOrgChart plannedOrgChart = null;
			log.debug(" Inside @createDesignationByApprovedPlannedOrgChart  customerId is : {}",
					commonUtils.getCustomerId());
			if (plannedOrgChartId != null) {
				plannedOrgChart = plannedOrgChartRepository.findPlannedOrgChartById(plannedOrgChartId,
						commonUtils.getCustomerId());
			} else {
				plannedOrgChart = plannedOrgChartRepository.findPlannedOrgChartByProcessInstanceId(processInstanceId,
						commonUtils.getCustomerId());
			}

			if (plannedOrgChart != null && plannedOrgChart.getDepartment() != null) {
				List<OrgChartDesignation> orgChartDesignationList = orgChartDesignationRepository
						.findByPlannedOrgChartId(plannedOrgChartId, commonUtils.getCustomerId());
				log.debug("List of OrgChartDesignation is : {}", orgChartDesignationList);
				log.debug("Size of OrgChartDesignationList is : {}", orgChartDesignationList.size());

				if (orgChartDesignationList != null && !orgChartDesignationList.isEmpty()) {
					for (OrgChartDesignation orgChartDesignation : orgChartDesignationList) {
						log.debug("OrgChartDesignation  is : {}", orgChartDesignation);

						String orgChartDesignationName = orgChartDesignation.getName();
						int orgChartDesignationDepartID = orgChartDesignation.getDepartment().getId();
						log.debug(
								"inside @class PlannedOrgChartServiceImpl createDesignationByApprovedPlannedOrgChart customerId is : {}",
								commonUtils.getCustomerId());
						Designation existingDesignation = designationRepository.findDesignationByNameAndDepartmentId(
								orgChartDesignationName, orgChartDesignationDepartID, commonUtils.getCustomerId());

						createNewDesignationByOCDesignationData(orgChartDesignation, orgChartDesignationName,
								orgChartDesignationDepartID, existingDesignation, orgChartDesignationList,
								plannedOrgChart);

					}
					return APIConstants.SUCCESS_JSON;
				}
			}

		} catch (BusinessException e) {
			throw new BusinessException("error while creating Designation by Approved PlannedOrgChart", e.getMessage());
		}
		return APIConstants.FAILURE_JSON;

	}

	private void createJobOpeningForNewJobRequisitionType(List<OrgChartDesignation> orgChartDesignationList,
			OrgChartDesignation orgChartDesignation, Designation designation) {
		if (orgChartDesignation.getRequisitionType() != null
				&& orgChartDesignation.getRequisitionType().equalsIgnoreCase("New Job Requisition")) {

			log.debug("orgChartDesignationList is :  {}", orgChartDesignationList);
			JobOpening jobOpening = new JobOpening();

			if (orgChartDesignation.getDepartment() != null) {
				log.debug("Department is  {}", orgChartDesignation.getDepartment());
				jobOpening.setDepartment(orgChartDesignation.getDepartment());
			}

			if (designation != null) {
				log.debug("Designation  is  {}", designation);
				jobOpening.setPostingTitle(designation);
			}

			if (orgChartDesignation.getCreator() != null) {
				log.debug("Going to set creator  is : {}", orgChartDesignation.getCreator());
				jobOpening.setCreator(orgChartDesignation.getCreator());
			}

			if (orgChartDesignation.getLastModifier() != null) {
				jobOpening.setLastModifier(orgChartDesignation.getLastModifier());
			}

			if (orgChartDesignation.getJobDescription() != null) {
				log.debug("Going to set JobDescription   is : {}", orgChartDesignation.getJobDescription());
				jobOpening.setDescriptionRequirements(orgChartDesignation.getJobDescription());
			}

			if (orgChartDesignation.getStartCompensationRange() != null) {
				Integer fromSalary = orgChartDesignation.getStartCompensationRange();

				log.debug("Going to set Salary : {}", orgChartDesignation.getStartCompensationRange());
				jobOpening.setFromSalary(fromSalary.toString());
			}

			if (orgChartDesignation.getEndCompensationRange() != null) {
				log.debug("Going to set EndSalary : {}", orgChartDesignation.getEndCompensationRange());
				Integer endSalary = orgChartDesignation.getEndCompensationRange();
				jobOpening.setEndSalary(endSalary.toString());
			}

			if (jobOpening != null) {
				generateJobIdForJobOpening(jobOpening);
				jobOpening.setJobOpeningStatus("inactive");
				log.debug("Going to create  JobOpening  {}", jobOpening);
				jobOpeningRepository.save(jobOpening);
			}

		}
	}

	private void createNewDesignationByOCDesignationData(OrgChartDesignation orgChartDesignation,
			String orgChartDesignationName, int orgChartDesignationDepartID, Designation existingDesignation,
			List<OrgChartDesignation> orgChartDesignationList, PlannedOrgChart plannedOrgChart) {
		if (orgChartDesignation != null && existingDesignation == null) {
			log.info("Going to create new Designation");
			Designation newDesignation = new Designation();
			setDataInMainDesignationObj(orgChartDesignation, newDesignation);

			if (orgChartDesignation.getParentDesignation() != null) {
				OrgChartDesignation parentOrgChartDesignation = orgChartDesignationRepository
						.findById(orgChartDesignation.getParentDesignation().getId()).orElse(null);

				log.debug("Inside createNewDesignationByOCDesignationData parentDesignation is : {}",
						parentOrgChartDesignation);

				if (parentOrgChartDesignation != null) {
					log.debug("Inside createNewDesignationByOCDesignationData customerId is : {}",
							commonUtils.getCustomerId());
					Designation parentDesignation = designationRepository.findDesignationByNameAndDepartmentId(
							parentOrgChartDesignation.getName(), parentOrgChartDesignation.getDepartment().getId(),
							commonUtils.getCustomerId());

					log.debug("parentDesignation is : {}", parentDesignation);

					if (parentDesignation == null) {
						log.info("Going to create new parentDesignation");

						parentDesignation = new Designation();

						setDataInMainDesignationObj(parentOrgChartDesignation, parentDesignation);
						generateNamingForDesignation(parentDesignation);
						designationRepository.save(parentDesignation);

					}

					log.debug("Set parentDesignation is : {}", parentDesignation);
					newDesignation.setParentDesignation(parentDesignation);
				}

			} else if (orgChartDesignation.getParentDesignation() == null) {

				log.info("In Case of Parent Designation Null");

				PlannedOrgChart plannedOrgChart1 = plannedOrgChartRepository.findById(plannedOrgChart.getId())
						.orElse(null);

				log.debug("plannedorgchart is : {} ", plannedOrgChart1);

				if (plannedOrgChart1 != null && plannedOrgChart1.getDesignation() != null
						&& plannedOrgChart1.getDesignation().getName() != null) {
					Designation parentDesignation = designationRepository.findDesignationByNameAndDepartmentId(
							plannedOrgChart1.getDesignation().getName(), plannedOrgChart1.getDepartment().getId(),
							commonUtils.getCustomerId());

					log.debug("Set parentDesignation, if parent designation null for Orgchartdesignation : {}",
							parentDesignation);
					newDesignation.setParentDesignation(parentDesignation);

				}

			}
			generateNamingForDesignation(newDesignation);
			Designation createdDesignation = designationRepository.save(newDesignation);

			log.info("Going to create JobOpening");
			createJobOpeningForNewJobRequisitionType(orgChartDesignationList, orgChartDesignation, createdDesignation);

		} else {
			log.debug("Designation already exists for name: {}, departmentId: {}, skipping save.",
					orgChartDesignationName, orgChartDesignationDepartID);

			if (orgChartDesignation != null) {
				createJobOpeningForNewJobRequisitionType(orgChartDesignationList, orgChartDesignation,
						existingDesignation);
			}
		}
	}

	private void setDataInMainDesignationObj(OrgChartDesignation orgChartDesignation, Designation newDesignation) {
		log.info("Inside method @setDataInMainDesignationObj");
		if (orgChartDesignation.getDepartment() != null) {
			newDesignation.setDepartmentId(orgChartDesignation.getDepartment());
		}
		if (orgChartDesignation.getName() != null) {
			newDesignation.setName(orgChartDesignation.getName());
		}
		if (orgChartDesignation.getWorkflowStage() != null) {
			newDesignation.setWorkflowStage("Approved");
		}

		if (orgChartDesignation.getProcessInstanceId() != null) {
			newDesignation.setProcessInstanceId(orgChartDesignation.getProcessInstanceId());
		}

		if (orgChartDesignation.getJobDescription() != null) {
			newDesignation.setJobDescription(orgChartDesignation.getJobDescription());
		}
	}

	/**
	 * @param newDesignation
	 */
	private void generateNamingForDesignation(Designation newDesignation) {
		log.info("Inside mmethod @generateNamingForDesignation");
		Map<String, String> mp = new HashMap<>();
		String generatedName = null;
		NameGenerationWrapperV2 nameGenerationWrapperV2 = null;
		try {
			nameGenerationWrapperV2 = customNumberValuesRest.generateNameAndFriendlyName("designationRule", mp,
					Status.ALLOCATED);
			log.info("nameGenerationWrapperV2: {}", nameGenerationWrapperV2);
			generatedName = nameGenerationWrapperV2.getGeneratedName();
			newDesignation.setDesignationCode(generatedName);
		} catch (Exception e) {
			logger.error("Failed to create/generate Naming Id For Designation");
		}
		log.debug("Saving new Designation: {}", newDesignation);
	}

	private void generateJobIdForJobOpening(JobOpening jobOpening) {
		log.info("Inside mmethod @generateJobIdForJobOpening");
		Map<String, String> mp = new HashMap<>();
		String generatedName = null;
		NameGenerationWrapperV2 nameGenerationWrapperV2 = null;
		try {
			nameGenerationWrapperV2 = customNumberValuesRest.generateNameAndFriendlyName("jobOpeningWithLocation", mp,
					Status.ALLOCATED);
			log.info("nameGenerationWrapperV2: {}", nameGenerationWrapperV2);
			generatedName = nameGenerationWrapperV2.getGeneratedName();
			jobOpening.setJobId(generatedName);
		} catch (Exception e) {
			logger.error("Failed to create/generate JobId for JobOpening");
		}

	}

//	@Override
//	public PlannedOrgChartDto searchOrgChartDesignationsByPlannedOrgChartId(Integer plannedOrgChartId) {
//		log.info("inside @class PlannedOrgChartServiceImpl @method searchOrgChartDesignationsByPlannedOrgChartId");
//		try {
//			PlannedOrgChartDto plannedOrgChartDto = new PlannedOrgChartDto();
//
//			log.info("PlannedOrgchart Id is : {}", plannedOrgChartId);
//
//			Integer userId = customerInfo.getUserId();
//			log.debug("User ID  is : {}", userId);
//
//			PlannedOrgChart plannedOrgChart = plannedOrgChartRepository
//					.findPlannedOrgChartByIdAndUser(plannedOrgChartId, userId);
//			log.debug("Planned org chart is plannedOrgChart : {}", plannedOrgChart);
//			if (plannedOrgChart != null) {
//				plannedOrgChartDto.setPlannedOrgChart(plannedOrgChart);
//			}
//
//			List<OrgChartDesignation> OrgChartDesignationList = orgChartDesignationRepository
//					.findByPlannedOrgChartIdAndUserId(plannedOrgChartId, userId);
//			plannedOrgChartDto.setOrgChartDesignation(OrgChartDesignationList);
//			return plannedOrgChartDto;
//		} catch (BusinessException ex) {
//			throw new BusinessException("error while searchOrgChartDesignationsByPlannedOrgChartId ", ex.getMessage());
//		}
//	}

	@Override
	public PlannedOrgChartDto searchOrgChartDesignationsByPlannedOrgChartId(Integer plannedOrgChartId) {
		log.info("inside @class PlannedOrgChartServiceImpl @method searchOrgChartDesignationsByPlannedOrgChartId");
		try {
			log.debug(" Inside @searchOrgChartDesignationsByPlannedOrgChartId  customerId is : {}",
					commonUtils.getCustomerId());
			PlannedOrgChartDto plannedOrgChartDto = new PlannedOrgChartDto();
			PlannedOrgChart plannedOrgChart = plannedOrgChartRepository.findPlannedOrgChartById(plannedOrgChartId,
					commonUtils.getCustomerId());
			log.debug("Planned org chart is plannedOrgChart : {}", plannedOrgChart);
			plannedOrgChartDto.setPlannedOrgChart(plannedOrgChart);
			List<OrgChartDesignation> OrgChartDesignationList = orgChartDesignationRepository
					.findByPlannedOrgChartId(plannedOrgChartId, commonUtils.getCustomerId());
			plannedOrgChartDto.setOrgChartDesignation(OrgChartDesignationList);
			return plannedOrgChartDto;
		} catch (BusinessException ex) {
			throw new BusinessException("error while searchOrgChartDesignationsByPlannedOrgChartId ", ex.getMessage());
		}
	}

	@Override
	public Designation createDesignationForSai(OrgChartDesignation orgChartDesignation) {
		log.info("inside @class PlannedOrgChartServiceImpl @method createDesignationForSai");

		Designation existingDesignation = designationRepository.findDesignationByNameAndDepartmentId(
				orgChartDesignation.getName(), orgChartDesignation.getDepartment().getId(),
				commonUtils.getCustomerId());

		if (existingDesignation != null) {
			log.debug("Designation already existed is  : {}", existingDesignation);
			if (existingDesignation.getJobDescription() != null && orgChartDesignation.getJobDescription() != null
					&& !existingDesignation.getJobDescription()
							.equalsIgnoreCase(orgChartDesignation.getJobDescription())) {
				log.debug("JobDescription Going to set for existing designation is , with changes in description : {}",
						orgChartDesignation.getJobDescription());
				existingDesignation.setJobDescription(orgChartDesignation.getJobDescription());
				designationRepository.save(existingDesignation);
			}
//To be implemented for Parent OrgChart desigantion
//			if (orgChartDesignation.getParentDesignation() != null) {
//				OrgChartDesignation parentOrgChartDesignation = orgChartDesignationRepository
//						.findById(orgChartDesignation.getParentDesignation().getId()).orElse(null);
//
//				log.debug("@method createDesignationForSai parentDesignation is : {}", parentOrgChartDesignation);
//
//				if (parentOrgChartDesignation != null) {
//					log.debug("inside @class PlannedOrgChartServiceImpl @method createDesignationForSai customerId is : {}",
//							commonUtils.getCustomerId());
//					Designation parentDesignation = designationRepository.findDesignationByNameAndDepartmentId(
//							parentOrgChartDesignation.getName(), parentOrgChartDesignation.getDepartment().getId(),
//							commonUtils.getCustomerId());
//
//					log.debug("parentDesignation is : {}", parentDesignation);
//
//					if (parentOrgChartDesignation.getJobDescription() != null && parentDesignation != null
//							&& parentDesignation.getJobDescription() != null && !parentOrgChartDesignation
//									.getJobDescription().equalsIgnoreCase(parentDesignation.getJobDescription())) {
//						log.debug("JobDescription Going to set for existing parentdesignation is : {}",
//								parentOrgChartDesignation.getJobDescription());
//						parentDesignation.setJobDescription(parentOrgChartDesignation.getJobDescription());
//						designationRepository.save(parentDesignation);
//
//					}
//
//					if (parentDesignation == null) {
//						log.info("Going to create new parentDesignation");
//
//						parentDesignation = new Designation();
//
//						setDataInMainDesignationObj(parentOrgChartDesignation, parentDesignation);
//						generateNamingForDesignation(parentDesignation);
//						designationRepository.save(parentDesignation);
//
//					}
//
//					log.debug("Set parentDesignation is : {}", parentDesignation);
//					existingDesignation.setParentDesignation(parentDesignation);
//				}
//
//			}
			return existingDesignation;

		}

		Designation newDesignation = new Designation();
		log.debug("OrgChartDesignation obj is : {}", orgChartDesignation);
		setDataInMainDesignationObj(orgChartDesignation, newDesignation);

		if (orgChartDesignation.getParentDesignation() != null) {
			OrgChartDesignation parentOrgChartDesignation = orgChartDesignationRepository
					.findById(orgChartDesignation.getParentDesignation().getId()).orElse(null);

			log.debug("@method createDesignationForSai parentDesignation is : {}", parentOrgChartDesignation);

			if (parentOrgChartDesignation != null) {
				log.debug("inside @class PlannedOrgChartServiceImpl @method createDesignationForSai customerId is : {}",
						commonUtils.getCustomerId());
				Designation parentDesignation = designationRepository.findDesignationByNameAndDepartmentId(
						parentOrgChartDesignation.getName(), parentOrgChartDesignation.getDepartment().getId(),
						commonUtils.getCustomerId());

				log.debug("parentDesignation is : {}", parentDesignation);

				if (parentOrgChartDesignation.getJobDescription() != null && parentDesignation != null
						&& parentDesignation.getJobDescription() != null && !parentOrgChartDesignation
								.getJobDescription().equalsIgnoreCase(parentDesignation.getJobDescription())) {
					log.debug("JobDescription Going to set for existing parentdesignation is : {}",
							parentOrgChartDesignation.getJobDescription());
					parentDesignation.setJobDescription(parentOrgChartDesignation.getJobDescription());
					designationRepository.save(parentDesignation);

				}

				if (parentDesignation == null) {
					log.info("Going to create new parentDesignation");

					parentDesignation = new Designation();

					setDataInMainDesignationObj(parentOrgChartDesignation, parentDesignation);
					generateNamingForDesignation(parentDesignation);
					designationRepository.save(parentDesignation);

				}

				log.debug("Set parentDesignation is : {}", parentDesignation);
				newDesignation.setParentDesignation(parentDesignation);
			}

		}
		generateNamingForDesignation(newDesignation);
		Designation designationCreated = designationRepository.save(newDesignation);

		log.debug("New Designation designationCreated : {}", designationCreated);

		return designationCreated;
	}

}
