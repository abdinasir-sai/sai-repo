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
import com.nouros.payrollmanagement.controller.GradeMetaInfoController;
import com.nouros.payrollmanagement.model.GradeMetaInfo;
import com.nouros.payrollmanagement.service.GradeMetaInfoService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the GradeMetaInfoController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the GradeMetaInfoController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the GradeMetaInfoService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(GradeMetaInfo GradeMetaInfo): creates an GradeMetaInfo and returns the newly created GradeMetaInfo.
count(String filter): returns the number of GradeMetaInfo that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of GradeMetaInfo that match the specified filter, sorted according to the specified orderBy
and orderType.
update(GradeMetaInfo GradeMetaInfo): updates an GradeMetaInfo and returns the updated GradeMetaInfo.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of GradeMetaInfo with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/GradeMetaInfo")
//@Tag(name="/GradeMetaInfo",tags="GradeMetaInfo",description="GradeMetaInfo")
public class GradeMetaInfoControllerImpl implements GradeMetaInfoController {

  @Autowired
  private GradeMetaInfoService gradeMetaInfoService;
  
  private static final Logger log = LogManager.getLogger(GradeMetaInfoControllerImpl.class);
	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public GradeMetaInfo create(GradeMetaInfo gradeMetaInfo) {
    return gradeMetaInfoService.create(gradeMetaInfo);
  }

  @Override
  public Long count(String filter) {
    return gradeMetaInfoService.count(filter);
  }

  @Override
  public List<GradeMetaInfo> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return gradeMetaInfoService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public GradeMetaInfo update(GradeMetaInfo gradeMetaInfo) {
    return gradeMetaInfoService.update(gradeMetaInfo);
  }

  @Override
  public GradeMetaInfo findById(Integer id) {
    return gradeMetaInfoService.findById(id);
  }

  @Override
  public List<GradeMetaInfo> findAllById(List<Integer> id) {
    return gradeMetaInfoService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    gradeMetaInfoService.softDelete(id);
  }
  
  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void bulkDelete(List<Integer> list) {
    gradeMetaInfoService.softBulkDelete(list);
  }

@Override
  public String importData(MultipartFile excelFile) throws IOException,InstantiationException, ClassNotFoundException  {
  return gradeMetaInfoService.importData(excelFile);
  }

  @Override
  public ResponseEntity<byte[]> export(String filter, Integer offset, Integer size, String orderBy, String orderType)  throws IOException {
      log.info("going to get list") ;
      List<GradeMetaInfo> result = gradeMetaInfoService.search(filter, 0, 10000000, orderBy, orderType);
      log.info("size of the list is :{},",result.size());
      byte[] workBook = gradeMetaInfoService.export(result);
      String fileName="GradeMetaInfo.xlsx";
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
	  return gradeMetaInfoService.auditHistory(id,limit,skip);
}

		
   
   
   
   
}
