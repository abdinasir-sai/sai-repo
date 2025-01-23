package com.nouros.payrollmanagement.controller.impl;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.nouros.payrollmanagement.controller.EmployeeMonthlySalaryController;
import com.nouros.payrollmanagement.model.EmployeeMonthlySalary;
import com.nouros.payrollmanagement.service.EmployeeMonthlySalaryService;
import com.nouros.payrollmanagement.wrapper.PayrollTotals;


/**

This class represents the implementation of the EmployeeMonthlySalaryController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the EmployeeMonthlySalaryController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the EmployeeMonthlySalaryService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(EmployeeMonthlySalary EmployeeMonthlySalary): creates an EmployeeMonthlySalary and returns the newly created EmployeeMonthlySalary.
count(String filter): returns the number of EmployeeMonthlySalary that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of EmployeeMonthlySalary that match the specified filter, sorted according to the specified orderBy
and orderType.
update(EmployeeMonthlySalary EmployeeMonthlySalary): updates an EmployeeMonthlySalary and returns the updated EmployeeMonthlySalary.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of EmployeeMonthlySalary with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/EmployeeMonthlySalary")
//@Tag(name="/EmployeeMonthlySalary",tags="EmployeeMonthlySalary",description="EmployeeMonthlySalary")
public class EmployeeMonthlySalaryControllerImpl implements EmployeeMonthlySalaryController {

  @Autowired
  private EmployeeMonthlySalaryService employeeMonthlySalaryService;
  
  private static final Logger log = LogManager.getLogger(EmployeeMonthlySalaryControllerImpl.class);
	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public EmployeeMonthlySalary create(EmployeeMonthlySalary employeeMonthlySalary) {
    return employeeMonthlySalaryService.create(employeeMonthlySalary);
  }

  @Override
  public Long count(String filter) {
    return employeeMonthlySalaryService.count(filter);
  }

  @Override
  public List<EmployeeMonthlySalary> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return employeeMonthlySalaryService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public EmployeeMonthlySalary update(EmployeeMonthlySalary employeeMonthlySalary) {
    return employeeMonthlySalaryService.update(employeeMonthlySalary);
  }

  @Override
  public EmployeeMonthlySalary findById(Integer id) {
    return employeeMonthlySalaryService.findById(id);
  }

  @Override
  public List<EmployeeMonthlySalary> findAllById(List<Integer> id) {
    return employeeMonthlySalaryService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    employeeMonthlySalaryService.softDelete(id);
  }
  
  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void bulkDelete(List<Integer> list) {
    employeeMonthlySalaryService.softBulkDelete(list);
  }

@Override
  public String importData(MultipartFile excelFile) throws IOException,InstantiationException, ClassNotFoundException  {
  return employeeMonthlySalaryService.importData(excelFile);
  }

  @Override
  public ResponseEntity<byte[]> export(String filter, Integer offset, Integer size, String orderBy, String orderType)  throws IOException {
      log.info("going to get list") ;
      List<EmployeeMonthlySalary> result = employeeMonthlySalaryService.search(filter, 0, 10000000, orderBy, orderType);
      log.info("size of the list is :{},",result.size());
      byte[] workBook = employeeMonthlySalaryService.export(result);
      String fileName="EmployeeMonthlySalary.xlsx";
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
	  return employeeMonthlySalaryService.auditHistory(id,limit,skip);
}

@Override
public String getEmployeeMonthlySalary(Integer employeeId) {
	log.info("inside getEmployeeMonthlySalary   employeeId :{}",employeeId);
	//EmployeeMonthlySalaryAoAService  employeeMonthlySalaryAoAService=ApplicationContextProvider.getApplicationContext().getBean(EmployeeMonthlySalaryAoAService.class);
	return employeeMonthlySalaryService.getEmployeeMonthlySalary( employeeId);
	
} 
 
public ResponseEntity<byte[]> createWpsTxtFile(String payrollRunProcessInstanceId) {
	log.info("Inside @Class EmployeeMonthlySalaryControllerImpl @Method createWpsTxtFile");
	return  employeeMonthlySalaryService.createWpsTxtFile(payrollRunProcessInstanceId);
}

@Override
public ResponseEntity<byte[]> createGOSIReportFile(Integer payrollRunId) {
  log.info("Inside @Class EmployeeMonthlySalaryControllerImpl @Method createGOSIReportFile");
  return employeeMonthlySalaryService.createGOSIReportFile(payrollRunId);
}

@Override
public ResponseEntity<byte[]> createWpsTxtFileForBod(String processInstanceId) {
	 log.info("Inside @Class EmployeeMonthlySalaryControllerImpl @Method createWpsTxtFileForBod");
	return employeeMonthlySalaryService.createWpsTxtFileForOnBoarded(processInstanceId);
}

@Override
public ResponseEntity<byte[]> getWpsTxtFileForBod(String year, String month) {
	log.info("Inside @Class EmployeeMonthlySalaryControllerImpl @Method getWpsTxtFileForBod");
	return employeeMonthlySalaryService.downloadWpsFileForBoardedMember(year, month);
}

@Override
public PayrollTotals getPayrollSum(Integer payrollRunId) {
	log.info("Inside @Class EmployeeMonthlySalaryControllerImpl @Method getPayrollSum");
	return employeeMonthlySalaryService.getPayrollSum(payrollRunId);
}   


}
