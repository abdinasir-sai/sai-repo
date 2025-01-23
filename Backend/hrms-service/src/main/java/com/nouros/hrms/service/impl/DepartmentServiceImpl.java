package com.nouros.hrms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.platform.utility.notification.model.NotificationTemplate;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.utils.Utils;
import com.nouros.hrms.integration.service.NotificationIntegration;
import com.nouros.hrms.model.Department;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.repository.DepartmentRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.DepartmentService;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.JobApplicationService;
import com.nouros.hrms.service.JobOpeningService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.wrapper.DepartmentDetailsWrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 * This is a class named "DepartmentServiceImpl" which is located in the package
 * " com.nouros.hrms.service.impl", It appears to be an implementation of the
 * "DepartmentService" interface and it extends the "AbstractService" class,
 * which seems to be a generic class for handling CRUD operations for entities.
 * This class is annotated with @Service, indicating that it is a Spring Service
 * bean. This class is using Lombok's @Slf4j annotation which will automatically
 * generate an Slf4j based logger instance, so it is using the Slf4j API for
 * logging. The class has a constructor which takes a single parameter of
 * GenericRepository Department and is used to call the superclass's
 * constructor. This class have one public method public byte[] export(List of
 * Department Department) for exporting the Department data into excel file by
 * reading the template and mapping the Department details into it. It's using
 * Apache POI library for reading and writing excel files, and has methods for
 * parsing the json files for column names and identities , and it also used
 * 'ExcelUtils' for handling the excel operations. It also uses
 * 'ApplicationContextProvider' from 'com.enttribe.core.generic.utils' and
 * 'APIConstants' from 'com.nouros.hrms.util'
 */

@Service
public class DepartmentServiceImpl extends AbstractService<Integer,Department> implements DepartmentService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   Department entities.
	 */

	private static final Logger log = LogManager.getLogger(DepartmentServiceImpl.class);

	public DepartmentServiceImpl(GenericRepository<Department> repository) {
		super(repository, Department.class);
	}

	@Autowired
	private DepartmentRepository departmentRepository;

	/**
	 * Creates a new vendor.
	 *
	 * @param department The department object to create.
	 * @return The created vendor object.
	 */
	@Override
	public Department create(Department department) {
		CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
		Integer workspaceId = customerInfo.getActiveUserSpaceId();
		department.setWorkspaceId(workspaceId); // done done
		return departmentRepository.save(department);
	}

	@Override
	public Department findByName(String departmentName) {
		return departmentRepository.findByName(departmentName);
	}

	@Override
	public List<DepartmentDetailsWrapper> getAllChildDepartments(String name) {
		log.info("Inside Method getAllChildDepartments ");

		Department departmentWithIdL1 = findByName(name);
		List<DepartmentDetailsWrapper> departmentWrapperList = new ArrayList<>();

		if (departmentWithIdL1 != null) {
			log.debug("Department Object fetched on basis of Name is : {}", departmentWithIdL1);
			DepartmentDetailsWrapper departmentDetailsWrapper = setDepartmentHeirarchyData(departmentWithIdL1);
			departmentWrapperList.add(departmentDetailsWrapper);

			fetchDownLevelDepartments(departmentWithIdL1, departmentWrapperList);
		}
		log.debug("Final Department Objects are : {} ", departmentWrapperList);
		return departmentWrapperList;
	}
 
	private void fetchDownLevelDepartments(Department departmentWithId,
			List<DepartmentDetailsWrapper> departmentWrapperList) {
		List<Department> departmentL2 = findDepartmentByIdDownLevel(departmentWithId.getId());
		log.debug("Down level 2 Department Objects are : {} ", departmentL2);
		List<DepartmentDetailsWrapper> departmentWrapperDownLevel = new ArrayList<>();
		for (Department department : departmentL2) {
			DepartmentDetailsWrapper departmentDetailsWrapper = setDepartmentHeirarchyData(department);
			departmentWrapperDownLevel.add(departmentDetailsWrapper);
			log.debug("departmentDetailsWrapper inside loop is :  {}", departmentDetailsWrapper);
		}
		log.debug("departmentWrapperDownLevel is : {} ", departmentWrapperDownLevel);
		if (CollectionUtils.isNotEmpty(departmentL2)) {
			for (Department departmentL3 : departmentL2) {
				List<Department> departmentListL3 = findDepartmentByIdDownLevel(departmentL3.getId());
				log.debug("Down level 3 Department Object is : {}", departmentListL3);
				for (Department departmentdownLevel3 : departmentListL3) {
					DepartmentDetailsWrapper departmentDetailsWrapper2 = setDepartmentHeirarchyData(
							departmentdownLevel3);
					log.debug("departmentDetailsWrapper2 is : {} ", departmentDetailsWrapper2);
					departmentWrapperDownLevel.add(departmentDetailsWrapper2);
				}
			}
			departmentWrapperList.addAll(departmentWrapperDownLevel);
		}
	}

	private List<Department> findDepartmentByIdDownLevel(int id) {
		return departmentRepository.findDepartmentByIdDownLevel(id);
	}

	/**
	 * @param departmentWithId
	 * @return
	 */
	@Override
	public DepartmentDetailsWrapper setDepartmentHeirarchyData(Department departmentWithId) {
		log.info("Inside Method setDepartmentHeirarchyData");
		DepartmentDetailsWrapper departmentDetailsWrapper = new DepartmentDetailsWrapper();
		EmployeeService employeeService = ApplicationContextProvider.getApplicationContext()
				.getBean(EmployeeService.class);
		setDepartmentDetailsWrapper(departmentWithId, departmentDetailsWrapper);
		Employee employeeLead = new Employee();
		if (departmentWithId.getDepartmentLead() != null) {
			Employee departmentLead = employeeService.findById(departmentWithId.getDepartmentLead());
			if (departmentLead != null) {
				employeeLead = departmentLead;
			}
			
			if (employeeLead != null) {
			    if (employeeLead.getDesignation() != null && employeeLead.getDesignation().getName() != null) {
			        departmentDetailsWrapper.setDesignation(employeeLead.getDesignation().getName());
			    }
			
			    if (employeeLead.getFirstName() != null && employeeLead.getLastName() != null) {
			        departmentDetailsWrapper.setDepartmentLead(employeeLead.getFirstName() + " " + employeeLead.getLastName());
			    }
			}

			
		}
		setDepartmentWiseDetails(departmentWithId, departmentDetailsWrapper);
		return departmentDetailsWrapper;
	}

	private void setDepartmentDetailsWrapper(Department departmentWithId,
			DepartmentDetailsWrapper departmentDetailsWrapper) {
		if (departmentWithId.getName() != null) {
			departmentDetailsWrapper.setDepartmentName(departmentWithId.getName());
		}
		if (departmentWithId.getId() != null) {
			departmentDetailsWrapper.setDepartmentId(departmentWithId.getId());
		}
		if (departmentWithId.getParentDepartment() != null) {
			departmentDetailsWrapper.setParentDepartmentId(departmentWithId.getParentDepartment().getId());
		}
	}

	@Override
	public DepartmentDetailsWrapper setDepartmentHeirarchyDataWithEmployee(Department departmentWithId,Employee employee) {
	    log.info("Inside Method setDepartmentHeirarchyDataWithEmployee");
	    DepartmentDetailsWrapper departmentDetailsWrapper = new DepartmentDetailsWrapper();
	    EmployeeService employeeService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeService.class);
	    setDepartmentDetailsWrapper(departmentWithId, departmentDetailsWrapper);
	    Employee employeeLead = new Employee();
	     if(departmentWithId.getDepartmentLead()!=null) {
	     Employee departmentLead = employeeService.findById(departmentWithId.getDepartmentLead());
	 	 if (departmentLead != null) {
			employeeLead = departmentLead;
		 }
		 if (employee != null && employee.getDesignation() != null && employee.getDesignation().getName()!=null) {
	    	  departmentDetailsWrapper.setDesignation(employee.getDesignation().getName());
	     }
	    if ((employeeLead.getFirstName()) != null && (employeeLead.getLastName()) != null) {
	    	departmentDetailsWrapper.setDepartmentLead(employeeLead.getFirstName() + " " +  employeeLead.getLastName());
	     }
	    }
	    setDepartmentWiseDetails(departmentWithId,departmentDetailsWrapper);
	    return departmentDetailsWrapper;
	}

	/**
	 * 
	 */
	private void setDepartmentWiseDetails(Department departmentWithId,
			DepartmentDetailsWrapper departmentDetailsWrapper) {
		log.info("Inside Method setDepartmentWiseDetails");
		JobOpeningService jobOpeningService = ApplicationContextProvider.getApplicationContext()
				.getBean(JobOpeningService.class);
		EmployeeService employeeService = ApplicationContextProvider.getApplicationContext()
				.getBean(EmployeeService.class);
		JobApplicationService jobApplicationService = ApplicationContextProvider.getApplicationContext()
				.getBean(JobApplicationService.class);
		Integer jobOpeningNumber = jobOpeningService.getCountOfJobOpeningByDepartmentId(departmentWithId.getId());
		if (jobOpeningNumber != null) {
			log.debug("jobOpeningNumber fetched are : {}", jobOpeningNumber);
			departmentDetailsWrapper.setJobOpeningNumber(jobOpeningNumber);
		}
		Map<String,Integer> countMap = employeeService.getCountOfEmployeesByEmploymentType(departmentWithId.getId());
		departmentDetailsWrapper.setCountMap(countMap);
		Integer jobApplicationCount = jobApplicationService
				.getCountOfJobApplicationByDepartmentId(departmentWithId.getId());
		if (jobApplicationCount != null) {
			log.debug("jobApplicationCount fetched are : {}", jobApplicationCount);
			departmentDetailsWrapper.setJobApplicationCount(jobApplicationCount);
		}
	}

	/**
	 * 
	 */
	@Override
	public DepartmentDetailsWrapper getDepartmentWiseDetails(List<Integer> departmentIds) {
		log.info("Inside Method setDepartmentWiseDetails");
		DepartmentDetailsWrapper departmentDetailsWrapper = new DepartmentDetailsWrapper();
		JobOpeningService jobOpeningService = ApplicationContextProvider.getApplicationContext()
				.getBean(JobOpeningService.class);
		EmployeeService employeeService = ApplicationContextProvider.getApplicationContext()
				.getBean(EmployeeService.class);
		JobApplicationService jobApplicationService = ApplicationContextProvider.getApplicationContext()
				.getBean(JobApplicationService.class);
		Integer jobOpeningNumber = jobOpeningService.getCountOfJobOpeningByDepartmentIds(departmentIds);
		if (jobOpeningNumber != null) {
			log.debug("jobOpeningNumber fetched are : {}", jobOpeningNumber);
			departmentDetailsWrapper.setJobOpeningNumber(jobOpeningNumber);
		}
		Map<String,Integer> countMap = employeeService.getCountOfEmployeesByEmploymentType(departmentIds);
		departmentDetailsWrapper.setCountMap(countMap);
		Integer jobApplicationCount = jobApplicationService.getCountOfJobApplicationByDepartmentIds(departmentIds);
		if (jobApplicationCount != null) {
			log.debug("jobApplicationCount fetched are : {}", jobApplicationCount);
			departmentDetailsWrapper.setJobApplicationCount(jobApplicationCount);
		}
		return departmentDetailsWrapper;
	}
	
	@Override
	 public Integer sendNotificationToSpecificDepartment()
	 {
		 try
		 {
			 log.info("Inside @class DesignationServiceImpl @method sendNotificationToSpecificDesignation ");
	 List<Department> departmentLeadList =	departmentRepository.getDeparmentLeadList();	 
	EmployeeService employeeService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeService.class);
    NotificationIntegration notificationIntegration = ApplicationContextProvider.getApplicationContext().getBean(NotificationIntegration.class);
    NotificationTemplate template = notificationIntegration.getTemplte("Succession planning");
    String employeeEmails = "";
    Integer count = 0;
    JSONObject jsonObject = new JSONObject();

    for(Department department : departmentLeadList)
		  {
    	log.debug("Department Id :{} ",department.getId());
			 Integer userId =  department.getDepartmentLead();
		     log.debug("Value of user id for Department :{} :{} ",userId,department.getId());
		     if(department.getDepartmentLeadEmailId()!=null )
		     {
		    	 Employee employee = employeeService.getEmployeeByUserId(userId);
			       department.getDepartmentLeadEmailId() ;
			      
			     notificationIntegration.sendNotification(template, jsonObject,  department.getDepartmentLeadEmailId());
			     count++;
		     }
		  }
    log.debug("TO Sting of Emails :{} ",employeeEmails);
    notificationIntegration.sendNotification(template, jsonObject, employeeEmails);
		 return count;
		 }
		 catch(Exception e)
		 {
			 log.error("Error inside @class DesignationServiceImpl @method sendNotificationToSpecificDesignation :{} :{}  ",e.getMessage(),Utils.getStackTrace(e));
			 throw new BusinessException();
		 }
	 }


}
