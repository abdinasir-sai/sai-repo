package com.nouros.hrms.service.impl;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.execution.controller.IWorkorderController;
import com.enttribe.orchestrator.execution.model.Workorder;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.product.storagesystem.rest.StorageRest;
import com.enttribe.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nouros.hrms.model.AccountDetails;
import com.nouros.hrms.model.BusinessExpense;
import com.nouros.hrms.model.EducationalBenefit;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeNationalIdentification;
import com.nouros.hrms.model.HealthClubBenefit;
import com.nouros.hrms.model.HrBenefits;
import com.nouros.hrms.model.OtherExpenseBankRequest;
import com.nouros.hrms.repository.BusinessExpenseRepository;
import com.nouros.hrms.repository.EducationalBenefitRepository;
import com.nouros.hrms.repository.EmployeeNationalIdentificationRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.AccountDetailsService;
import com.nouros.hrms.service.BusinessExpenseService;
import com.nouros.hrms.service.OtherExpenseBankRequestService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.BusinessExpenseDto;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;
import com.nouros.payrollmanagement.utils.PRConstant;

@Service
public class BusinessExpenseServiceImpl extends AbstractService<Integer,BusinessExpense> implements BusinessExpenseService {

	public BusinessExpenseServiceImpl(GenericRepository<BusinessExpense> repository) {
		super(repository, BusinessExpense.class);
	}
	@Autowired
	private BusinessExpenseRepository businessExpenseRepository;
	
	@Autowired
	private StorageRest storageRest;
	
	@Autowired
	 private CommonUtils commonUtils;
	
	  @Autowired
	  private EmployeeNationalIdentificationRepository employeeNationalIdentificationRepository;
	
		@Autowired
		private HrmsSystemConfigService hrmsSystemConfigService;
		
		@Autowired
		IWorkorderController workorderController;
		
		@Autowired
		WorkflowActionsController workflowActionsController;
	  
	
	@Value("${ROOT_DIR_HRMS_PAYROLL_FILE}")
	private String rootDirBucketName;
	
	 private static final String INSIDE_CLASS_LOG = "Inside @Class BusinessExpenseServiceImpl @Method";
	 private static final String ATTACHMENT = "attachment";
     private static final String BUSINESS_EXPENSE = "BusinessExpense";

	private static final Logger log = LogManager.getLogger(BusinessExpenseServiceImpl.class);

	@Override
	public BusinessExpense create(BusinessExpense businessExpense) {
		log.info("inside @class BusinessExpenseServiceImpl @method create");
		return businessExpenseRepository.save(businessExpense);
	}
	
	@Override
	public void softDelete(int id) {

		BusinessExpense businessExpense = super.findById(id);

		if (businessExpense != null) {

			BusinessExpense businessExpense1 = businessExpense;
			businessExpenseRepository.save(businessExpense1);

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
	 	public ResponseEntity<byte[]> createWpsTxtFileForBusinessExpense(String businessExpenseProcessInstanceId)
	{
		try {
			log.info("Inside @class BusinessExpenseServiceImpl @method createWpsTxtFile");
			if(businessExpenseProcessInstanceId!=null) {
				log.debug("Inside @class BusinessExpenseServiceImpl createWpsTxtFileForBusinessExpense customerId is : {}", commonUtils.getCustomerId());
				Optional<BusinessExpense> optinalBusinessExpense = businessExpenseRepository.findByProcessInstanceId(businessExpenseProcessInstanceId, commonUtils.getCustomerId());
		        if(optinalBusinessExpense.isPresent())
		        {
		        	BusinessExpense businessExpense =  optinalBusinessExpense.get(); 
		        	 	Employee employee = businessExpense.getEmployee();
			        	List<String[]> data = fetchData(employee,businessExpense);
			        	ByteArrayOutputStream out = generateTxt(data);
			    		byte[] txtBytes = out.toByteArray();
			    		String fileReference = getSystemTimeWithTimeStamp();
			    		String fileName = String.valueOf(businessExpense.getId())+"Wps" + fileReference+ ".txt";
			    		String filePath = "hrmswps/"+fileName;
			    		
			    		
			    		log.debug("Value of FileName :{}  ,fileRefernce :{} file path:{}",fileName,fileReference,filePath);
			    		
			    		log.info("Inside BusinessExpenseServiceImpl createWpsTxtFile rootDirBucketName :{}" , rootDirBucketName);
			    		InputStream inputStream = new ByteArrayInputStream(txtBytes);
			    		uploadFileInStorage(inputStream, fileName, filePath,rootDirBucketName);
			    		HttpHeaders headers = new HttpHeaders();
			            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			            headers.setContentDispositionFormData(ATTACHMENT, fileName);
			            //String fileDir = rootDirBucketName + filePath;
			            log.debug("The Path of File Dir to store is :{} ",filePath);
			            businessExpense.setFilePath(filePath);
			            businessExpenseRepository.save(businessExpense);
			            return new ResponseEntity<>(txtBytes, headers, HttpStatus.OK);
		        	 
		        }
		 	
		
			}
			return null;	
		} catch (Exception e) {
			log.error(INSIDE_CLASS_LOG+"createWpsTxtFile exception occurs :{}",e.getMessage());
			log.error(INSIDE_CLASS_LOG+"  createWpsTxtFile method ex : {}", Utils.getStackTrace(e), e);
			throw new BusinessException("Exception occurs inside fetchData");
			
		}	

	}
	
	

	private ByteArrayOutputStream generateTxt(List<String[]> data) {
		log.debug(INSIDE_CLASS_LOG + " generateTxt data :{}", data);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {
			if(data!=null) {
			for (String[] line : data) {
				writer.write(String.join(",", line));
				writer.newLine();
			}
			log.info(INSIDE_CLASS_LOG + " generateTxt data");
			writer.flush();
		}
		} catch (IOException e) {
			log.error("Error generating TXT content: {}", e.getMessage());
		}
		return out;
	}
		  
		  
	List<String[]> fetchData(Employee employee,BusinessExpense businessExpense)
	{
	  try {
		  List<String[]> data = new ArrayList<>();
			AccountDetailsService accountDetailsService = ApplicationContextProvider.getApplicationContext().getBean(AccountDetailsService.class);
			List<AccountDetails> accountDetails = accountDetailsService.findAccountDetailsByEmployeeId(employee.getId());
			  if (accountDetails == null) {
	                log.warn("No account details found for employee ID: {}", employee.getId());
	                return data;
	            }
			  log.debug("Object of Employee :{} ",employee);
			  log.debug("Object of BusinessExpenseServiceImpl :{} ",businessExpense);
			  String amountString = String.valueOf(businessExpense.getClaimAmount());
			log.info("Inside @class EducationBenefitService @method fetchData");
			 for (AccountDetails accountDetail : accountDetails) {
		        	log.debug( "Inside @class EducationalBenefitServiceImpl @method fetchData accountDetail Id :{}",accountDetail.getId());
		        	 log.debug("Object of Account details :{} ",accountDetails);
		        	   Map<String,String> mapHrmsSystemConfigMap  =    hrmsSystemConfigService.getHrmsKeyValue();  	 
		        	 String ifh =  mapHrmsSystemConfigMap.get("ifh");
		   	      String ifile = mapHrmsSystemConfigMap.get("ifile");
		   	      String csv =  mapHrmsSystemConfigMap.get("csv");
			      String connectId =  mapHrmsSystemConfigMap.get("connectId");
			      String customerId =  mapHrmsSystemConfigMap.get("customerId");
			      String sach =  mapHrmsSystemConfigMap.get("@sach@");
			      String bathdr =  mapHrmsSystemConfigMap.get("bathdr");
			      String achcr =  mapHrmsSystemConfigMap.get("achcr");
			      String count = "1";
			      String debitAccountNumber =  mapHrmsSystemConfigMap.get("debitAccountNumber");
			      String paymentPurposeCodeO = mapHrmsSystemConfigMap.get("paymentPurposeCodeO");
			      String paymentNarrationCompany =  mapHrmsSystemConfigMap.get("wpsPopTest");
			      String pcmHsbcnetTest =  mapHrmsSystemConfigMap.get("PcmHsbcnetTest");
			      String first =  mapHrmsSystemConfigMap.get("@1st@");
			      String sar =  mapHrmsSystemConfigMap.get("sar");
			      String molEstablisedId = mapHrmsSystemConfigMap.get("molEstablisedId");
			      String employerId = mapHrmsSystemConfigMap.get("employerId");
			      String p =  mapHrmsSystemConfigMap.get("p");
			      String one =  mapHrmsSystemConfigMap.get("one");
			      String secpty =  mapHrmsSystemConfigMap.get("secpty");
			      String n =  mapHrmsSystemConfigMap.get("n");	 
			      String currentDate = getCurrentDate();
					String currentTime = getCurrentTime();
					//String valueDate = getDateAfterDays(2);
					String changedDate = getDateAfterDaysC();
					String fileReference = "ILWPSOPOPVATEST"+getSystemTimeWithTimeStamp();
					String batchReference = "IWPSOPOP"+getSystemTimeWithTimeStampNew();
                 String inv = "INV";
			      String employeeReferenceId = employee.getEmployeeId();
			      String bankId = accountDetail.getBankId();
			      
			      String nationalIdentification = "";
			      log.debug("Inside @class BusinessExpenseServiceImpl fetchData  customerId is : {}", commonUtils.getCustomerId());
			      EmployeeNationalIdentification employeeNationalIdentification =  employeeNationalIdentificationRepository.findNationalIdentificationNumberByEmployeeId(employee.getId(), commonUtils.getCustomerId());
			        if(employeeNationalIdentification!=null){
			          nationalIdentification = employeeNationalIdentification.getIdentificationNumber();
			        }
			      
		        	 StringBuilder rowBuilder = new StringBuilder();
		        	 rowBuilder.append(secpty).append(",");
		            rowBuilder.append(accountDetail.getIban()).append(",");
		            rowBuilder.append(accountDetail.getBeneficiaryName()).append(",");
		           rowBuilder.append(employee.getId()).append(",");
		            rowBuilder.append(accountDetail.getBankId()).append(",").append(",");

		           rowBuilder.append(amountString);
		           
		          rowBuilder.append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(n).append(",").append(n).append(",").append(",").append(",").append(",").append(",").append(",");
		           rowBuilder.append(sach).append(",").append(accountDetail.getBeneficiaryId()); 
		            rowBuilder.append(one).append(",").append(one).append(",").append(one).append(",").append(one);
		           rowBuilder.append(paymentNarrationCompany).append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",");
	            rowBuilder.append(inv);
		            log.info("After add the data from object");
		            data.add(new String[] {ifh, ifile, csv, connectId, customerId, fileReference, currentDate, currentTime, p, one,"3"});
		            data.add(new String[] {bathdr, achcr, one,"", "", "", paymentPurposeCodeO,"" ,paymentNarrationCompany, "", first,changedDate,debitAccountNumber ,sar,amountString ,"", "", "", "", "", "", pcmHsbcnetTest ,molEstablisedId, employerId, "", "", "",batchReference});
		            data.add(rowBuilder.toString().split(","));
		        }

			
			
			return data;

	  }
	  catch(Exception e)
	  {
    	  log.error("Error Inside @class BusinessExpenseServiceImpl @method fetchData :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
    	  throw new BusinessException();  
	  }
	}
	
	@Override
	public ResponseEntity<byte[]> downloadWpsFile(String businessExpenseProcessInstanceId)  {
 
		log.info("Inside @class BusinessExpenseServiceImpl @method downloadWpsFile");
		if(businessExpenseProcessInstanceId!=null) {
			log.debug("Inside @class BusinessExpenseServiceImpl downloadWpsFile customerId is : {}", commonUtils.getCustomerId());
			Optional <BusinessExpense> optinalBusinessExpense = businessExpenseRepository.findByProcessInstanceId(businessExpenseProcessInstanceId, commonUtils.getCustomerId());
			
			if(optinalBusinessExpense.isPresent())
	        {
	        	BusinessExpense businessExpense =  optinalBusinessExpense.get(); 

  String filePath = businessExpense.getFilePath();
	    
	    		
	    log.info("Inside BusinessExpenseServiceImpl downloadWpsFile rootDirBucketName :{} ",rootDirBucketName);
		
	    String currentTime = getSystemTimeWithTimeStamp();
	  try {
	      log.debug("Inside BusinessExpenseServiceImpl @method downloadWpsFile :{} ",filePath );
	      InputStream inputStream = storageRest.read(rootDirBucketName, filePath);
	      log.debug("InputStream available bytes: {}", inputStream.available());
	      InputStreamResource resource = new InputStreamResource(inputStream);
	      byte[] fileData = resource.getContentAsByteArray();
	      HttpHeaders responseHeaders = new HttpHeaders();
	      responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	      responseHeaders.setContentDispositionFormData(ATTACHMENT, "Wps" + currentTime + ".txt");
	      return new ResponseEntity<>(fileData, responseHeaders, HttpStatus.OK);
	    }
	   catch (Exception e) {
		   log.error("Error Inside @class BusinessExpenseServiceImpl @method downloadWpsFile :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
	      throw new BusinessException("Something went wrong inside downloadWpsFile " );
	  }
			  
		}
			
		}
		return null;
	}
	
	
	private void uploadFileInStorage(InputStream in, String fileName, String filePath ,  String rootDir)
		      throws IOException {
		    log.debug("Inside uploadFileInStorage :: {}", fileName);
		    log.debug("fileName: {} filePath: {}",fileName, filePath); 
		    try (InputStream inputStream = new ByteArrayInputStream(in.readAllBytes())) {
		    
		  	  storageRest.createFile(inputStream, rootDir, filePath);
		    }
		}
	
	
	private String getSystemTimeWithTimeStamp() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	    return now.format(formatter);
	}

	@Override
	public BusinessExpense updateBusinessExpenseWorkflowStage(BusinessExpenseDto businessExpenseDto) {
		log.debug("Inside method updateBusinessExpenseWorkflowStage Id : {}", businessExpenseDto.getBusinessExpenseId());
		try {
			if (businessExpenseDto.getBusinessExpenseId() != null) {
				BusinessExpense optionalBusinessExpense = super.findById(businessExpenseDto.getBusinessExpenseId());
				if (optionalBusinessExpense != null) {
					BusinessExpense businessExpense = optionalBusinessExpense;
					if (businessExpenseDto.getWorkflowStage().equalsIgnoreCase("CANCELLED") && businessExpense != null
							&& businessExpense.getProcessInstanceId() != null) {
						log.debug("Inside method updateBusinessExpenseWorkflowStage ProcessInstanceId found is  : {}",
								businessExpense.getProcessInstanceId());
						Workorder wordorder = workorderController
								.findByProcessInstanceId(businessExpense.getProcessInstanceId());
						log.debug("Inside method updateBusinessExpenseWorkflowStage wordorder found is  : {}", wordorder);
						if (wordorder != null && wordorder.getReferenceId() != null) {
							try {
								String response = workorderController.deleteWorkorder(wordorder.getReferenceId());
								log.debug(
										"Inside method updateBusinessExpenseWorkflowStage response from  deleteWorkorder api is : {}",
										response);
								 workflowActionsController.notifyActions(businessExpense.getProcessInstanceId()); 
 							} catch (Exception e) {
								log.error("Facing error While deleting Workorder");
							}
						}
					}
					if(businessExpense!=null) {
					businessExpense.setWorkflowStage(businessExpenseDto.getWorkflowStage());}
					return businessExpenseRepository.save(businessExpense);
				} else {
					throw new BusinessException("BusinessExpense with ID " + businessExpenseDto.getBusinessExpenseId() + " not found");
				}
			}
		} catch (Exception e) {
			throw new BusinessException("error while updating BusinessExpense work flow stage", e.getMessage());
		}
		return null;
	}
	
	
	public ResponseEntity<byte[]> createWpsTxtFileForAllBusinessExpense()
	{
		log.info("inside @class BusinessExpenseServiceImpl @method createWpsTxtFileForAllBusinessExpense ");
		try
		{
		    LocalDate currDate = LocalDate.now();
		    LocalDate nextDate = currDate.plusDays(1);
		    Integer year = currDate.getYear();
		    LocalDate sixDaysAgo = currDate.minusDays(6);   
		     DayOfWeek dayOfWeek  = DayOfWeek.from(currDate); 
		      log.debug("the current day is :{} ",dayOfWeek.name()); 
		      WeekFields weekFields = WeekFields.of(Locale.getDefault());
		        int weekNumber = currDate.get(weekFields.weekOfWeekBasedYear());
		        log.debug("Week num is :{} ",weekNumber);
//			if(dayOfWeek.getValue()==6)
//			{
		        log.debug("Inside @class BusinessExpenseServiceImpl createWpsTxtFileForAllBusinessExpense customerId is : {}", commonUtils.getCustomerId());
				List<BusinessExpense> businessExpenses = businessExpenseRepository.getBusinessExpenseByWorkflowStageAndDate(PRConstant.APPROVED_BY_FINANCE_CONTROLLER, sixDaysAgo, nextDate,commonUtils.getCustomerId());	
	            
				 List<String[]> data = getData(businessExpenses);
					 ByteArrayOutputStream out = generateTxt(data);
		    		byte[] txtBytes = out.toByteArray();
		    		String fileReference = getSystemTimeWithTimeStamp();
		    		String fileName = "BE"+"Wps" + fileReference+ ".txt";
		    		String filePath = "hrmswps/"+fileName;
		    		
		    		
		    		log.debug("Value of FileName :{}  ,fileRefernce :{} file path:{}",fileName,fileReference,filePath);
		    		
		    		log.info("Inside BusinessExpenseServiceImpl createWpsTxtFile rootDirBucketName :{}" , rootDirBucketName);
		    		InputStream inputStream = new ByteArrayInputStream(txtBytes);
		    		uploadFileInStorage(inputStream, fileName, filePath,rootDirBucketName);
		    		HttpHeaders headers = new HttpHeaders();
		            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		            headers.setContentDispositionFormData(ATTACHMENT, fileName);
		            //String fileDir = rootDirBucketName + filePath;
		            log.debug("The Path of File Dir to store is :{} ",filePath);
		             List<Integer> employeeIdList = getEmployeeIdList(businessExpenses);
		             ObjectMapper objectMapper = new ObjectMapper();
		             String json = objectMapper.writeValueAsString(employeeIdList);
		            OtherExpenseBankRequest otherExpenseBankRequest = savePath(filePath,currDate,year,weekNumber,json);
		            saveValueInBusinessExpense(otherExpenseBankRequest , businessExpenses);
		            return new ResponseEntity<>(txtBytes, headers, HttpStatus.OK);

//			}
//			else
//			{
//				log.info("It's not saturday ");
//				return null;
//			}
		}
		catch(Exception e)
		{
			log.error("Error inside @class BusinessExpenseServiceImpl @method createWpsTxtFileForAllEducationalBenefit :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
	  throw new BusinessException("Something wrong in Creating wps for all Education Benefit ");
		}
 
	}

    
	List<String[]> getData(List<BusinessExpense> businessExpenses)
	{
		List<String[]> data = new ArrayList<>();
		try {
			log.info(INSIDE_CLASS_LOG+"getData");
		     Map<String,String> mapHrmsSystemConfigMap  =    hrmsSystemConfigService.getHrmsKeyValue();   
		     String ifh =  mapHrmsSystemConfigMap.get("ifh");
	   	      String ifile = mapHrmsSystemConfigMap.get("ifile");
	   	      String csv =  mapHrmsSystemConfigMap.get("csv");
		      String connectId =  mapHrmsSystemConfigMap.get("connectId");
		      String customerId =  mapHrmsSystemConfigMap.get("customerId");
		      String sach =  mapHrmsSystemConfigMap.get("@sach@");
		      String bathdr =  mapHrmsSystemConfigMap.get("bathdr");
		      String achcr =  mapHrmsSystemConfigMap.get("achcr");
			  String debitAccountNumber =  mapHrmsSystemConfigMap.get("debitAccountNumber");
		      String paymentPurposeCodeS = mapHrmsSystemConfigMap.get("paymentPurposeCodeS");
		      String paymentNarrationCompany =  mapHrmsSystemConfigMap.get("wpsPopTest");
		      String pcmHsbcnetTest =  mapHrmsSystemConfigMap.get("PcmHsbcnetTest");
		      String first =  mapHrmsSystemConfigMap.get("@1st@");
		      String sar =  mapHrmsSystemConfigMap.get("sar");
		      String molEstablisedId = mapHrmsSystemConfigMap.get("molEstablisedId");
		      String employerId = mapHrmsSystemConfigMap.get("employerId");
		      String p =  mapHrmsSystemConfigMap.get("p");
		      String one =  mapHrmsSystemConfigMap.get("one");
		      String secpty =  mapHrmsSystemConfigMap.get("secpty");
		      String n =  mapHrmsSystemConfigMap.get("n");	
			String inv = mapHrmsSystemConfigMap.get("INV");
		      log.debug(INSIDE_CLASS_LOG+"getData after all the data fetch from Hrms_System_Config mapHrmsSystemConfigMap :{} ",mapHrmsSystemConfigMap);
		
		    String currentDate = getCurrentDate();
			String currentTime = getCurrentTime();
			String valueDate = getDateAfterDays(1);
			String fileReference = "ILWPSOPOPVATEST"+getSystemTimeWithTimeStamp();
			String batchReference = "IWPSOPOP"+getSystemTimeWithTimeStampNew();
			
			 log.info(INSIDE_CLASS_LOG+"getData after set date and time");
		 
 
			Integer count =  businessExpenses.size();
			log.debug(INSIDE_CLASS_LOG+"fetch count :{}",count);	
			String totalCount = count.toString();
			log.debug(INSIDE_CLASS_LOG+"fetch totalCount :{}",totalCount);	
			Integer totalRecordCount = Integer.parseInt(totalCount)+2;
			String totalRecordCountString = totalRecordCount.toString();
			String netWorth=getTotalAmount(businessExpenses);
			 
			
			data = new ArrayList<>();
			data.add(new String[] {ifh, ifile, csv, connectId, customerId, fileReference, currentDate, currentTime, p, one, totalRecordCountString});
			data.add(new String[] {bathdr, achcr, totalCount, "", "", "", paymentPurposeCodeS, paymentNarrationCompany, "", first, valueDate, debitAccountNumber, sar, netWorth, "", "", "", "", "", "",  molEstablisedId, employerId, "", "", "", batchReference});
			iterateBusinessExpense(secpty, n, sach, businessExpenses, data,paymentNarrationCompany,inv);
			
			log.info(INSIDE_CLASS_LOG+"fetchData Educational Benefit Successfully Iterate");
			 
			return data;

	  }
	  catch(Exception e)
	  {
    	  log.error("Error Inside @class BusinessExpenseServiceImpl @method getData :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
    	  throw new BusinessException();  
	  }
	}	
	
	
	private void iterateBusinessExpense(String secpty, String n, String sach, List<BusinessExpense> businessExpenses,
			List<String[]> data,String paymentNarrationCompany ,String inv) {
		String one = "1";
		log.info(INSIDE_CLASS_LOG+" iterateBusinessExpense");
		for (BusinessExpense businessExpense : businessExpenses) {
			log.debug(INSIDE_CLASS_LOG+"iterateBusinessExpense BusinessExpense :{}",businessExpense);
	        Integer employeeId = businessExpense.getEmployee().getId();
	        String employeeReferenceId = businessExpense.getEmployee().getEmployeeId();
	        String nationalIdentification = "";
	        log.debug("Inside @class BusinessExpenseServiceImpl iterateBusinessExpense  customerId is : {}", commonUtils.getCustomerId());
	        EmployeeNationalIdentification employeeNationalIdentification =  employeeNationalIdentificationRepository.findNationalIdentificationNumberByEmployeeId(employeeId, commonUtils.getCustomerId());
	        if(employeeNationalIdentification!=null){
	          nationalIdentification = employeeNationalIdentification.getIdentificationNumber();
	        }
	    	AccountDetailsService accountDetailsService = ApplicationContextProvider.getApplicationContext().getBean(AccountDetailsService.class);
	        List<AccountDetails> accountDetails = accountDetailsService.findAccountDetailsByEmployeeId(employeeId);

	        for (AccountDetails accountDetail : accountDetails) {
	        	log.debug(INSIDE_CLASS_LOG+"iterateBusinessExpense accountDetail :{}",accountDetail);
	            StringBuilder rowBuilder = new StringBuilder();
 
	            
	            rowBuilder.append(secpty).append(",");
	            rowBuilder.append(accountDetail.getIban()).append(",");
	            rowBuilder.append(accountDetail.getBeneficiaryName()).append(",");
	           rowBuilder.append(employeeId).append(",");
	            rowBuilder.append(accountDetail.getBankId()).append(",").append(",").append(",");

	           rowBuilder.append(businessExpense.getClaimAmount());
	           
	          rowBuilder.append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(n).append(",").append(n).append(",").append(",").append(",").append(",").append(",").append(",");
	           rowBuilder.append(sach).append(",").append(accountDetail.getBeneficiaryId()).append(","); 
	            rowBuilder.append(one).append(",").append(one).append(",").append(one).append(",").append(one).append(",");
	           rowBuilder.append(paymentNarrationCompany).append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",");
            rowBuilder.append(inv);			         
            data.add(rowBuilder.toString().split(","));
	            log.debug(INSIDE_CLASS_LOG+" iterateBusinessExpense data :{}",data);
	        }
	    }
	}
	
	OtherExpenseBankRequest savePath(String path,LocalDate date,Integer year,Integer weekNumber,String json)
	{
	  log.info("Inside @class BusinessExpenseServiceImpl @method savePath ");
	  try
	  {
          OtherExpenseBankRequestService otherExpenseBankRequestService = ApplicationContextProvider.getApplicationContext().getBean(OtherExpenseBankRequestService.class);
          OtherExpenseBankRequest otherExpenseBankRequest =   otherExpenseBankRequestService.savePath(PRConstant.BUSINESS_EXPENSE_SM, path, date, year, weekNumber, PRConstant.WPS_CREATED,json);
	   return otherExpenseBankRequest;
	  }
	  catch(Exception e)
	  {
		  log.error("Error inside @class BusinessExpenseServiceImpl @method savePath");
		  throw new BusinessException();
	  }
	}

 
	public ResponseEntity<byte[]> downloadCommonWpsFile(Integer weekNum)  {
		 
		log.info("Inside @class BusinessExpenseServiceImpl @method downloadCommonWpsFile");
		  LocalDate currDate = LocalDate.now();
		  Integer year = currDate.getYear();
		  OtherExpenseBankRequestService otherExpenseBankRequestService = ApplicationContextProvider.getApplicationContext().getBean(OtherExpenseBankRequestService.class);
		   String filePath =   otherExpenseBankRequestService.getPathForWps(PRConstant.BUSINESS_EXPENSE_SM,weekNum, year);
	    
	    		
	    log.info("Inside BusinessExpenseServiceImpl downloadWpsFile rootDirBucketName :{} ",rootDirBucketName);
		
	    String currentTime = getSystemTimeWithTimeStamp();
	  try {
	      log.debug("Inside BusinessExpenseServiceImpl @method downloadWpsFile :{} ",filePath );
	      InputStream inputStream = storageRest.read(rootDirBucketName, filePath);
	      log.debug("InputStream available bytes: {}", inputStream.available());
	      InputStreamResource resource = new InputStreamResource(inputStream);
	      byte[] fileData = resource.getContentAsByteArray();
	      HttpHeaders responseHeaders = new HttpHeaders();
	      responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	      responseHeaders.setContentDispositionFormData(ATTACHMENT, "Wps" + currentTime + ".txt");
	      return new ResponseEntity<>(fileData, responseHeaders, HttpStatus.OK);
	    }
	   catch (Exception e) {
		   log.error("Error Inside @class BusinessExpenseServiceImpl @method downloadWpsFile :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
	      throw new BusinessException("Something went wrong inside downloadWpsFile " );
	  }
	}


	
	private String getCurrentTime() {
	    LocalTime time = LocalTime.now();
	    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	    return time.format(timeFormatter);
	}

	private String getCurrentDate() {
	    LocalDate date = LocalDate.now();
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	    return date.format(dateFormatter);
	}

	private String getDateAfterDays(int days) {
	    LocalDate date = LocalDate.now();
	    LocalDate newDate = date.plusDays(days);
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	    return newDate.format(dateFormatter);
	}
	
	private String getDateAfterDaysC() {
		LocalDate date = LocalDate.now().plusDays(1);
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd"); 
	    return date.format(dateFormatter);
	    }
	

	private String getSystemTimeWithTimeStampNew() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
	    return now.format(formatter);
	}
   
	private String getTotalAmount(List<BusinessExpense> businessExpenses)
	   {
			log.debug("Inside Method getTotalAmount businessExpenses size :{} ",businessExpenses.size());
		   Double amount = 0.0 ;
		   for(BusinessExpense businessExpense : businessExpenses)
		   {
			   amount = amount+businessExpense.getClaimAmount();
		   }
		   log.debug("Inside Method getTotalAmount businessExpenses amount :{} ",amount);
		   return String.valueOf(amount);
		   
	   }
	private List<Integer> getEmployeeIdList(List<BusinessExpense> businessExpenses)
	{
		try
		{
			log.info("Inside @class HrBenefitServiceImpl @method getEmployeeIdList ");
			List<Integer> employeeIdList = new ArrayList<>();
              for(BusinessExpense businessExpense :businessExpenses)
              {
            	  employeeIdList.add(businessExpense.getEmployee().getId());
              }
              log.debug("The Size of List :{} ",employeeIdList.size());
			return employeeIdList; 
		}
		catch(Exception e)
		{
			   log.error("Error Inside @class HrBenefitServiceImpl @method getEmployeeIdList :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
			      throw new BusinessException();
		}

	}
	
	
	public void saveValueInBusinessExpense(OtherExpenseBankRequest otherExpenseBankRequest ,List<BusinessExpense> businessExpenses)
	{
		try
		{
			log.debug(" Inside @class HrBenefitServiceImpl @method saveValueInBenefits oTEBR id :{}  hrbenefit size :{}  ",otherExpenseBankRequest.getId(),businessExpenses.size());
		  for(BusinessExpense businessExpense : businessExpenses)
		  {
			  businessExpense.setOtherExpenseBankRequestId(otherExpenseBankRequest.getId());
			  businessExpenseRepository.save(businessExpense);
		}
		  log.info("Id of Other Expense Bank Request Saved in BusinessExpense ");
		}
		catch(Exception e)
		{
			log.error("Error Inside @class HrBenefitServiceImpl @method saveValueInBenefits :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}
	
	@Override
	public List<BusinessExpense> getListOfBusinessExpenseByOtherExpenseBankRequestId(OtherExpenseBankRequest otherExpenseBankRequest)
	{
		try
		{
			log.debug("Inside @class BusinessExpenseServiceImpl @method getListOfBusinessExpenseByOtherExpenseBankRequestId :{} ",otherExpenseBankRequest.getId());
			log.debug("Inside @class BusinessExpenseServiceImpl getListOfBusinessExpenseByOtherExpenseBankRequestId customerId is : {}", commonUtils.getCustomerId());
			List<BusinessExpense> listOfBusinessExpense =  businessExpenseRepository.getBusinessExpenseListByBankRequestId(otherExpenseBankRequest.getId(),commonUtils.getCustomerId());
			return listOfBusinessExpense;
		}
		catch(Exception e)
		{
			log.error("Error Inside @class BusinessExpenseServiceImpl @method getListOfBusinessExpenseByOtherExpenseBankRequestIdeInBenefits :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}
	
	@Override
	public List<Object[]> getBusinessExpenseforExpense(List<Integer> businessExpenseList)
	{
		try
		{
		  log.info("Inside @class BusinessExpenseServiceImpl @method getBusinessExpenseForExpense ");
		  log.debug("Inside @class BusinessExpenseServiceImpl getBusinessExpenseforExpense customerId is : {}", commonUtils.getCustomerId());
		 List<Object []> businessExpenses = businessExpenseRepository.getBusinessExpenseForExpense(businessExpenseList, commonUtils.getCustomerId());
		  return businessExpenses;
		}
		catch(Exception e)
		{
			log.error("Error Inside @class BusinessExpenseServiceImpl @method getBusinessExpenseforExpense :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();	
		}
	}

	@Override
	public  Object getBusinessExpenseforAccural(List<Integer> businessExpenseList)
	{
		try
		{
		  log.info("Inside @class BusinessExpenseServiceImpl @method getBusinessExpenseforAccural ");
		  log.debug("Inside @class BusinessExpenseServiceImpl getBusinessExpenseforAccural customerId is : {}", commonUtils.getCustomerId());
		 Object  businessExpenses = businessExpenseRepository.getBusinessExpenseForAccural(businessExpenseList, commonUtils.getCustomerId());
		  return businessExpenses;
		}
		catch(Exception e)
		{
			log.error("Error Inside @class BusinessExpenseServiceImpl @method getBusinessExpenseforAccural :{} :{} ", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();	
		}
	}

}
