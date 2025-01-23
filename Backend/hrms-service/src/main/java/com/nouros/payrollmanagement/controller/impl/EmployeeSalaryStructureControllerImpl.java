
package com.nouros.payrollmanagement.controller.impl;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enttribe.commons.io.excel.ExcelWriter;
import com.enttribe.commons.spring.rest.ResponseBuilder;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.payrollmanagement.controller.EmployeeSalaryStructureController;
import com.nouros.payrollmanagement.model.EmployeeSalaryStructure;
import com.nouros.payrollmanagement.model.PayrollRun;
import com.nouros.payrollmanagement.service.EmployeeSalaryStructureService;
import com.nouros.payrollmanagement.wrapper.PayrollRequestWrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the EmployeeSalaryStructureController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the EmployeeSalaryStructureController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the EmployeeSalaryStructureService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(EmployeeSalaryStructure EmployeeSalaryStructure): creates an EmployeeSalaryStructure and returns the newly created EmployeeSalaryStructure.
count(String filter): returns the number of EmployeeSalaryStructure that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of EmployeeSalaryStructure that match the specified filter, sorted according to the specified orderBy
and orderType.
update(EmployeeSalaryStructure EmployeeSalaryStructure): updates an EmployeeSalaryStructure and returns the updated EmployeeSalaryStructure.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of EmployeeSalaryStructure with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/EmployeeSalaryStructure")
//@Tag(name="/EmployeeSalaryStructure",tags="EmployeeSalaryStructure",description="EmployeeSalaryStructure")
public class EmployeeSalaryStructureControllerImpl implements EmployeeSalaryStructureController {

  @Autowired
  private EmployeeSalaryStructureService employeeSalaryStructureService;
  
  private static final Logger log = LogManager.getLogger(EmployeeSalaryStructureControllerImpl.class);
	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public EmployeeSalaryStructure create(EmployeeSalaryStructure employeeSalaryStructure) {
    return employeeSalaryStructureService.create(employeeSalaryStructure);
  }

  @Override
  public Long count(String filter) {
    return employeeSalaryStructureService.count(filter);
  }

  @Override
  public List<EmployeeSalaryStructure> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	  CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
	  List<String> departmentList = customerInfo.getDepartment();
	  log.debug("inside EmployeeSalaryStructureImpl customerInfo department :{}",departmentList);
	  if(departmentList!=null && !departmentList.isEmpty()) {
		  log.debug("inside EmployeeSalaryStructureImpl customerInfo department1 :{}",departmentList.get(0));
	  }
    return employeeSalaryStructureService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public EmployeeSalaryStructure update(EmployeeSalaryStructure employeeSalaryStructure) {
    return employeeSalaryStructureService.update(employeeSalaryStructure);
  }

  @Override
  public EmployeeSalaryStructure findById(Integer id) {
    return employeeSalaryStructureService.findById(id);
  }

  @Override
  public List<EmployeeSalaryStructure> findAllById(List<Integer> id) {
    return employeeSalaryStructureService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    employeeSalaryStructureService.softDelete(id);
  }
  
  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void bulkDelete(List<Integer> list) {
    employeeSalaryStructureService.softBulkDelete(list);
  }

@Override
  public String importData(MultipartFile excelFile) throws IOException,InstantiationException, ClassNotFoundException  {
  return employeeSalaryStructureService.importData(excelFile);
  }

  @Override
  public ResponseEntity<byte[]> export(String filter, Integer offset, Integer size, String orderBy, String orderType)  throws IOException {
      log.info("going to get list") ;
      List<EmployeeSalaryStructure> result = employeeSalaryStructureService.search(filter, 0, 10000000, orderBy, orderType);
      log.info("size of the list is :{},",result.size());
      byte[] workBook = employeeSalaryStructureService.export(result);
      String fileName="EmployeeSalaryStructure.xlsx";
      return ResponseBuilder.toResponse(workBook, fileName);
}
  
  @Override
  public ResponseEntity downloadTemplate(String fileName) throws IOException {
    InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("templates/reports/"+fileName);
    log.info("resourceAsStream :{}" ,resourceAsStream.available());
    XSSFWorkbook xssfWorkbook = new XSSFWorkbook(resourceAsStream);
    try (ExcelWriter excelWriter = new ExcelWriter(xssfWorkbook)) {
      byte[] workBook = excelWriter.toByteArray();
      return ResponseBuilder.toResponse(workBook, fileName);
    }
  }
    
    @Override
  public String auditHistory(int id, Integer limit, Integer skip) {
	  return employeeSalaryStructureService.auditHistory(id,limit,skip);
}

	@Override
	public PayrollRun payrollRun(PayrollRequestWrapper payrollRequestWrapper) {
		 log.info("going to get payroll execute") ;
		return employeeSalaryStructureService.executePayrollRun(payrollRequestWrapper);
	}

	@Override
	public EmployeeSalaryStructure getEmployeeMappedSalaryStructure(Integer employeeId) {
		log.info("inside EmployeeSalaryStructureController getEmployeeMappedSalaryStructure   employeeId :{} ",employeeId) ;
		return employeeSalaryStructureService.getEmployeeMappedSalaryStructure(employeeId);
	}

	@Override
	public Object verifyOneTimeComponentCalculated(Integer employeeId) {
		log.info("inside EmployeeSalaryStructureController verifyOneTimeComponentCalculated   employeeId :{} ",employeeId) ;
		return employeeSalaryStructureService.verifyOneTimeComponentCalculated(employeeId);
	}

	@Override 
	public List<EmployeeSalaryStructure> getEmployeeSalaryStructureByUserId() {
		
		return employeeSalaryStructureService.getEmployeeSalaryStructureByUserId();
	}

	@Override
	public List<EmployeeSalaryStructure> importEmployeeSalaryStructureData(MultipartFile excelFile) {
		
		return employeeSalaryStructureService.importEmployeeSalaryStructureData(excelFile);
	}

	
	
   
   
   
   
}
