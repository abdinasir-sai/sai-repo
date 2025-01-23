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
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.payrollmanagement.controller.HrmsSystemConfigController;
import com.nouros.payrollmanagement.model.HrmsSystemConfig;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the HrmsSystemConfigController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the HrmsSystemConfigController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the HrmsSystemConfigService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(HrmsSystemConfig HrmsSystemConfig): creates an HrmsSystemConfig and returns the newly created HrmsSystemConfig.
count(String filter): returns the number of HrmsSystemConfig that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of HrmsSystemConfig that match the specified filter, sorted according to the specified orderBy
and orderType.
update(HrmsSystemConfig HrmsSystemConfig): updates an HrmsSystemConfig and returns the updated HrmsSystemConfig.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of HrmsSystemConfig with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/HrmsSystemConfig")
//@Tag(name="/HrmsSystemConfig",tags="HrmsSystemConfig",description="HrmsSystemConfig")
public class HrmsSystemConfigControllerImpl implements HrmsSystemConfigController {

  @Autowired
  private HrmsSystemConfigService hrmsSystemConfigService;
  
  private static final Logger log = LogManager.getLogger(HrmsSystemConfigControllerImpl.class);
	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public HrmsSystemConfig create(HrmsSystemConfig hrmsSystemConfig) {
    return hrmsSystemConfigService.create(hrmsSystemConfig);
  }

  @Override
  public Long count(String filter) {
    return hrmsSystemConfigService.count(filter);
  }

  @Override
  public List<HrmsSystemConfig> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return hrmsSystemConfigService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public HrmsSystemConfig update(HrmsSystemConfig hrmsSystemConfig) {
    return hrmsSystemConfigService.update(hrmsSystemConfig);
  }

  @Override
  public HrmsSystemConfig findById(Integer id) {
    return hrmsSystemConfigService.findById(id);
  }

  @Override
  public List<HrmsSystemConfig> findAllById(List<Integer> id) {
    return hrmsSystemConfigService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    hrmsSystemConfigService.softDelete(id);
  }
  
  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void bulkDelete(List<Integer> list) {
    hrmsSystemConfigService.softBulkDelete(list);
  }

@Override
  public String importData(MultipartFile excelFile) throws IOException,InstantiationException, ClassNotFoundException  {
  return hrmsSystemConfigService.importData(excelFile);
  }

  @Override
  public ResponseEntity<byte[]> export(String filter, Integer offset, Integer size, String orderBy, String orderType)  throws IOException {
      log.info("going to get list") ;
      List<HrmsSystemConfig> result = hrmsSystemConfigService.search(filter, 0, 10000000, orderBy, orderType);
      log.info("size of the list is :{},",result.size());
      byte[] workBook = hrmsSystemConfigService.export(result);
      String fileName="HrmsSystemConfig.xlsx";
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
	  return hrmsSystemConfigService.auditHistory(id,limit,skip);
}

	@Override
	public String getValuebyKey(String key) {
		
		return hrmsSystemConfigService.getValue(key);
		}

	@Override
	public Object getHrmsKeyValue() {
	
		return hrmsSystemConfigService.getHrmsKeyValue();
	}

		
   
   
   
   
}
