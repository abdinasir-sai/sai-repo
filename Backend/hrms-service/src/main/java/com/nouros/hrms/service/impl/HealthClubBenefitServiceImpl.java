package com.nouros.hrms.service.impl;

import java.io.BufferedWriter; 
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.enttribe.product.storagesystem.rest.StorageRest;
import com.enttribe.utils.Utils;
import com.nouros.hrms.model.AccountDetails;
import com.nouros.hrms.model.ChildEducationBenefit;
import com.nouros.hrms.model.EducationalBenefit;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeNationalIdentification;
import com.nouros.hrms.model.HealthClubBenefit;
import com.nouros.hrms.repository.EmployeeNationalIdentificationRepository;
import com.nouros.hrms.repository.HealthClubBenefitRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.AccountDetailsService;
import com.nouros.hrms.service.HealthClubBenefitService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.HealthClubBenefitDto;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;
import com.nouros.payrollmanagement.utils.PRConstant;

@Service
public class HealthClubBenefitServiceImpl extends AbstractService<Integer,HealthClubBenefit> implements HealthClubBenefitService {

	public HealthClubBenefitServiceImpl(GenericRepository<HealthClubBenefit> repository) {
		super(repository, HealthClubBenefit.class);
	}
	
	
	@Autowired
	private StorageRest storageRest;
	
	 private static final String INSIDE_CLASS_LOG = "Inside @Class HealthClubBenefitServiceImpl @Method";
	 private static final String ATTACHMENT = "attachment";
	 private static final String HEALTH_CLUB_BENEFIT = "HealthClubBenefit";

	
	@Value("${ROOT_DIR_HRMS_PAYROLL_FILE}")
	private String rootDirBucketName;
	
	@Autowired
	private HealthClubBenefitRepository healthClubBenefitRepository;
	
	@Autowired
	  private EmployeeNationalIdentificationRepository employeeNationalIdentificationRepository;
	
	@Autowired
	private CommonUtils commonUtils;
	  
		@Autowired
		private HrmsSystemConfigService hrmsSystemConfigService;

	private static final Logger log = LogManager.getLogger(HealthClubBenefitServiceImpl.class);

	@Override
	public HealthClubBenefit create(HealthClubBenefit healthClubBenefit) {
	    log.info("inside @class HealthClubBenefitServiceImpl @method create");

	    try {
	        Integer employeeId = healthClubBenefit.getEmployee().getId();
	        Double amountPaid = healthClubBenefit.getAmountPaid();
 
	        log.debug("inside @class HealthClubBenefitServiceImpl @method create employeeId : {}, amountPaid : {}",employeeId,amountPaid);
	        if (amountPaid > 4200) {
	        	log.error("Amount Paid for the year exceeds the limit of 4200");
	            throw new BusinessException("Amount Paid Cannot Be Greater Than 4200");
	        }
	        
	        java.util.Date startDateDate = healthClubBenefit.getStartDate();
	        java.util.Date endDateDate = healthClubBenefit.getEndDate();
	        
	        java.time.LocalDate startDate = convertToLocalDate(startDateDate);
	        java.time.LocalDate endDate = convertToLocalDate(endDateDate);
	        LocalDate now = LocalDate.now();
	        int currentYear = now.getYear();

	        if (startDate.getYear() != currentYear || endDate.getYear() != currentYear) {
	        	log.error("Inside method @create Start Date and End Date must be within the current year");
	            throw new BusinessException("Start Date and End Date must be within the current year");
	        }
	        log.debug("inside @class HealthClubBenefitServiceImpl create customerId is : {}", commonUtils.getCustomerId());
	        Double totalAmountPaidForYear = healthClubBenefitRepository.getTotalAmountPaidForYearByEmployeeInApproved(employeeId, commonUtils.getCustomerId());
	        log.debug("inside @class HealthClubBenefitServiceImpl @method create totalAmountPaidForYear : {}",totalAmountPaidForYear);
	        
	        if (totalAmountPaidForYear == null) {
	            totalAmountPaidForYear = 0.0;
	        }

	        if ((totalAmountPaidForYear + amountPaid) > 4200) {
	        	log.error("Inside method @create Total amount for the year exceeds the limit of 4200");
	            throw new BusinessException("Total amount for the year exceeds the limit of 4200");
	        }

	        return healthClubBenefitRepository.save(healthClubBenefit);
	    } catch (BusinessException ex) {
	        throw new BusinessException("Error in creating HealthClubBenefit", ex.getMessage());
	    }
	}
	
	@Override
	public HealthClubBenefit update(HealthClubBenefit healthClubBenefit) {
		log.info("inside @class HealthClubBenefitServiceImpl @method update");

	    try {
	        Integer employeeId = healthClubBenefit.getEmployee().getId();
	        Double amountPaid = healthClubBenefit.getAmountPaid();
    
	        log.debug("inside @class HealthClubBenefitServiceImpl @method update employeeId : {}, amountPaid : {}",employeeId,amountPaid);
	        java.util.Date startDateDate = healthClubBenefit.getStartDate();
	        java.util.Date endDateDate = healthClubBenefit.getEndDate();
	        
	        java.time.LocalDate startDate = convertToLocalDate(startDateDate);
	        java.time.LocalDate endDate = convertToLocalDate(endDateDate);
	        LocalDate now = LocalDate.now();
	        int currentYear = now.getYear();

	        if (startDate.getYear() != currentYear || endDate.getYear() != currentYear) {
	        	log.error("Inside method @update Start Date and End Date must be within the current year");
	            throw new BusinessException("Start Date and End Date must be within the current year");
	        }
	        log.debug("inside @class HealthClubBenefitServiceImpl update customerId is : {}", commonUtils.getCustomerId());
	        Double totalAmountPaidForYear = healthClubBenefitRepository.getTotalAmountPaidForYearByEmployee(employeeId, commonUtils.getCustomerId());
	        log.debug("inside @class HealthClubBenefitServiceImpl @method update totalAmountPaidForYear : {}",totalAmountPaidForYear);
	        if (totalAmountPaidForYear == null) {
	            totalAmountPaidForYear = 0.0;
	        }

	        if ((totalAmountPaidForYear + amountPaid) > 4200) {
	        	log.error("Inside method @update Total amount for the year exceeds the limit of 4200");
	            throw new BusinessException("Total amount for the year exceeds the limit of 4200");
	        }

	        return healthClubBenefitRepository.save(healthClubBenefit);
	    } catch (BusinessException ex) {
	        throw new BusinessException("Error in creating HealthClubBenefit", ex.getMessage());
	    }
	}

	private LocalDate convertToLocalDate(Date date) {
		log.info("inside @class HealthClubBenefitServiceImpl @method convertToLocalDate");
	    Instant instant = date.toInstant();
	   
	   ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
	    
	    return zdt.toLocalDate();
	}
	
	@Override
	public void softDelete(int id) {

		HealthClubBenefit healthClubBenefit = super.findById(id);

		if (healthClubBenefit != null) {

			HealthClubBenefit healthClubBenefit1 = healthClubBenefit;
			healthClubBenefitRepository.save(healthClubBenefit1);

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
	
	public HashMap<Integer,Double> getIdAndAmountByEmployeeId(Integer employeeId , Date payRollRunDate)
	{
		log.info("Inside @HealthClubBenefitServiceImpl @method getIdAndAmountByEmployeeId ");
		log.debug("inside @class HealthClubBenefitServiceImpl getIdAndAmountByEmployeeId customerId is : {}", commonUtils.getCustomerId());
		List<HealthClubBenefit> healthClubBenefitsList = healthClubBenefitRepository.findByEmployeeIdAndWorkflowStage(employeeId,PRConstant.APPROVED_SM);
       log.debug("Inside @HealthClubBenefitServiceImpl @method getIdAndAmountByEmployeeId Size of List:{}",healthClubBenefitsList.size());
		HashMap<Integer,Double> healthClubBenefitHashMap = new HashMap<>();	 
     try{
		for(HealthClubBenefit healthClubBenefit : healthClubBenefitsList)
		{
			Date healthBenefitEndDate = healthClubBenefit.getEndDate();
			LocalDate healthBenefitLocalDate = healthBenefitEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate payRollRunDateLocalDate =  payRollRunDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			
			if(healthBenefitLocalDate.isAfter(payRollRunDateLocalDate))
		  {
			   Integer id = healthClubBenefit.getId();
			   Double amountPaid = healthClubBenefit.getAmountPaid();
		   healthClubBenefitHashMap.put(id, amountPaid);
           }
		} 
		return healthClubBenefitHashMap;
	 }
	 catch(Exception e)
	 {
		log.error("Error inside @HealthClubBenefitServiceImpl @method getIdAndAmountByEmployeeId :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
       throw new BusinessException();
	 }
	}
	
	public Double getSumOfAmount(HashMap<Integer,Double> healthClubBenefitHashMap)
	{
		log.info("Inside @class HealthClubBenefitServiceImpl @method getSumOfAmount ");
		log.debug("Inside @class HealthClubBenefitServiceImpl @method getSumOfAmount Map:{}",healthClubBenefitHashMap);
		Double sum = 0.0;
		for (Map.Entry<Integer, Double> entry : healthClubBenefitHashMap.entrySet()) {
		    try
		    {
		    	sum += entry.getValue();
		    }
		    catch(Exception e)
		    {
		    	log.debug("Exception Inside @class HealthClubBenefitServiceImpl @method getSumOfAmount :{} :{}",e.getMessage(),Utils.getStackTrace(e));
		    }
		}
		log.debug(" Inside @class HealthClubBenefitServiceImpl @method getSumOfAmount Sum is :{}",sum);
        return sum;
	}
	
	public Boolean setWorkFlowStageForBulk(List<Integer> listOfKeys , String workflowStage)
	{
		log.info("Inside @class HealthClubBenefitServiceImpl @method setWorkFlowStageForBulk");
		try
		{
			for(Integer id : listOfKeys)
			{
				HealthClubBenefit healthClubBenefitOptional = super.findById(id);
				if(healthClubBenefitOptional != null)
				{
					HealthClubBenefit healthClubBenefit = healthClubBenefitOptional;
					healthClubBenefit.setWorkflowStage(workflowStage);
					healthClubBenefitRepository.save(healthClubBenefit);
					log.debug("Inside @class HealthClubBenefitServiceImpl @method setWorkFlowStageForBulk Status changed to :{}",workflowStage);		
				}			
			}
			return true;
		}
		catch(Exception e)
		{
			log.error("Error inside @HealthClubBenefitServiceImpl @method setWorkFlowStageForBulk :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}
	
	public ResponseEntity<byte[]> createWpsTxtFileForEducationalBenefit(String HealthClubBenefitProcessInstanceId)
	{
		try {
			log.info("Inside @class HealthClubBenefitServiceImpl @method createWpsTxtFile");
			if(HealthClubBenefitProcessInstanceId!=null) {
				log.debug("getIdAndAmountByEmployeeId createWpsTxtFileForEducationalBenefit customerId is : {}", commonUtils.getCustomerId());
				Optional <HealthClubBenefit> optinalHealthClubBenefit = healthClubBenefitRepository.findByProcessInstanceId(HealthClubBenefitProcessInstanceId);
		        if(optinalHealthClubBenefit.isPresent())
		        {
		        	HealthClubBenefit healthClubBenefit =  optinalHealthClubBenefit.get(); 
                   
   		        	Employee employee = healthClubBenefit.getEmployee();
   		        	List<String[]> data = fetchData(employee,healthClubBenefit);
   		        	ByteArrayOutputStream out = generateTxt(data);
   		    		byte[] txtBytes = out.toByteArray();
   		    		String fileReference = getSystemTimeWithTimeStamp();
   		    		String fileName = String.valueOf(healthClubBenefit.getId())+"Wps" + fileReference+ ".txt";
   		    		String filePath = "hrmswps/" + fileName;
   		    		log.info("Inside Rundetails createWpsTxtFile rootDirBucketName :{}" , rootDirBucketName);
   		    		InputStream inputStream = new ByteArrayInputStream(txtBytes);
   		    		uploadFileInStorage(inputStream, fileName, filePath,rootDirBucketName);
   		    		HttpHeaders headers = new HttpHeaders();
   		            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
   		            headers.setContentDispositionFormData(ATTACHMENT, fileName);
   		           // String fileDir = rootDirBucketName + filePath ;
   		            log.debug("The Path of File Dir to store is :{} ",filePath);
   		            healthClubBenefit.setOutputString(filePath);
   		            healthClubBenefitRepository.save(healthClubBenefit);
   		            return new ResponseEntity<>(txtBytes, headers, HttpStatus.OK);

                   }

		        }
		 	
		 
		} catch (Exception e) {
			log.error(INSIDE_CLASS_LOG+"createWpsTxtFile exception occurs :{}",e.getMessage());
			log.error(INSIDE_CLASS_LOG+"  createWpsTxtFile method ex : {}", Utils.getStackTrace(e), e);
			throw new BusinessException("Exception occurs inside fetchData");
			
		}
		return null;	

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
		  
		  
	List<String[]> fetchData(Employee employee,HealthClubBenefit healthClubBenefit)
	{
	      try {
	    	  List<String[]> data = new ArrayList<>();
	    		AccountDetailsService accountDetailsService = ApplicationContextProvider.getApplicationContext().getBean(AccountDetailsService.class);
	    		List<AccountDetails> accountDetails = accountDetailsService.findAccountDetailsByEmployeeId(employee.getId());
	    		log.info("Inside @class HealthClubBenefitServiceImpl @method fetchData");
	    		 for (AccountDetails accountDetail : accountDetails) {
	    	        	log.debug( "Inside @class HealthClubBenefitServiceImpl @method fetchData accountDetail Id :{}",accountDetail.getId());
	    	        	Double amount = healthClubBenefit.getAmountPaid();
	    		         String amountString = String.valueOf(amount);
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
								String alw = "ALW";
						      String employeeReferenceId = employee.getEmployeeId();
						      String bankId = accountDetail.getBankId();
						      
						      String nationalIdentification = "";
						      log.debug("getIdAndAmountByEmployeeId fetchData customerId is : {}", commonUtils.getCustomerId());
						      EmployeeNationalIdentification employeeNationalIdentification =  employeeNationalIdentificationRepository.findNationalIdentificationNumberByEmployeeId(employee.getId(), commonUtils.getCustomerId());
						        if(employeeNationalIdentification!=null){
						          nationalIdentification = employeeNationalIdentification.getIdentificationNumber();
						        }
						      
					        	 StringBuilder rowBuilder = new StringBuilder();
					        	 rowBuilder.append(secpty).append(",");
					            rowBuilder.append(accountDetail.getIban()).append(",");
			 		            rowBuilder.append(accountDetail.getBeneficiaryName()).append(",");
					            rowBuilder.append(accountDetail.getBankId()).append(",");
					            rowBuilder.append(employee.getId()).append(",").append(",");
			 		           rowBuilder.append(amountString);
			 		           
			 		          rowBuilder.append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(n).append(",").append(n).append(",").append(",").append(",").append(",").append(",").append(",");
			 		           rowBuilder.append(sach).append(",").append(accountDetail.getBeneficiaryId()); 
			 		            rowBuilder.append(one).append(one).append(one).append(one);
					           rowBuilder.append(paymentNarrationCompany).append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",");
			   	            rowBuilder.append(alw);		            
			   	            
			   	            
			   	         log.info("After add the data from object");
				            data.add(new String[] {ifh, ifile, csv, connectId, customerId, fileReference, currentDate, currentTime, p, one});
				            data.add(new String[] {bathdr, achcr, "", "", "", paymentPurposeCodeO, paymentNarrationCompany, "", first,  sar,amountString ,"", "", "", "", "", "", pcmHsbcnetTest ,molEstablisedId, employerId, "", "", "", batchReference});
	    	            data.add(rowBuilder.toString().split(","));
	    	            log.debug(" the data is :{}",data);
	    	        }

	    		
	    		
	    		return data;

	      }
	      catch(Exception e)
	      {
	    	  log.error("Error Inside @class HealthClubBenefitServiceImpl @method fetchData :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
	    	  throw new BusinessException();
	      }
	
	}
	
	
 	public ResponseEntity<byte[]> downloadWpsFile(String HealthClubBenefitProcessInstanceId)  {
		 
		
		if(HealthClubBenefitProcessInstanceId!=null) {
			log.debug("Inside @class HealthClubBenefitServiceImpl downloadWpsFile customerId is : {}", commonUtils.getCustomerId());
			Optional <HealthClubBenefit> optinalHealthClubBenefit = healthClubBenefitRepository.findByProcessInstanceId(HealthClubBenefitProcessInstanceId);
	        if(optinalHealthClubBenefit.isPresent())
	        {
	        	HealthClubBenefit healthClubBenefit =  optinalHealthClubBenefit.get(); 
	        	String filePath = healthClubBenefit.getOutputString();
	    
	    		
	    log.info("Inside EducationalBenefitServiceImpl downloadWpsFile rootDirBucketName :{} ",rootDirBucketName);
		
	    String currentTime = getSystemTimeWithTimeStamp();
	  try {
	      log.debug("Inside EducationalBenefitServiceImpl @method downloadWpsFile :{} ",filePath );
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
		   log.error("Error Inside @class EducationalBenefitServiceImpl @method downloadWpsFile :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
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
	
	private String getSystemTimeWithTimeStampNew() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
	    return now.format(formatter);
	}

	
	
	
	
	private String getSystemTimeWithTimeStamp() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	    return now.format(formatter);
	}

	@Override
	public HealthClubBenefit updateHealthClubBenefitWorkflowStage(HealthClubBenefitDto healthClubBenefitDto) {
		log.debug("Inside method updateHealthClubBenefitWorkflowStage healthClubBenefitId : {}", healthClubBenefitDto.getHealthClubBenefitId());
		try {
			if (healthClubBenefitDto.getHealthClubBenefitId() != null) {
				HealthClubBenefit optionalHealthClubBenefit = super.findById(healthClubBenefitDto.getHealthClubBenefitId());
				if (optionalHealthClubBenefit != null) {
					HealthClubBenefit healthClubBenefit = optionalHealthClubBenefit;
					healthClubBenefit.setWorkflowStage(healthClubBenefitDto.getWorkflowStage());
					return healthClubBenefitRepository.save(healthClubBenefit);
				} else {
					throw new BusinessException("HealthClubBenefit with ID " + healthClubBenefitDto.getHealthClubBenefitId() + " not found");
				}
			}
		} catch (Exception e) {
			throw new BusinessException("error while updating HealthClubBenefit work flow stage", e.getMessage());
		}
		return null;
	}
	
	private String getDateAfterDaysC() {
		LocalDate date = LocalDate.now().plusDays(1);
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd"); 
	    return date.format(dateFormatter);
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

	
	@Override
	public ResponseEntity<byte[]> createWpsTxtFileForAllHealthClubBenefit()
	{
		log.info("inside @class HealthClubBenefitServiceImpl @method createWpsTxtFileForAllChildEducationBenefit ");
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
		        log.debug("Inside @class HealthClubBenefitServiceImpl createWpsTxtFileForAllHealthClubBenefit customerId is : {}", commonUtils.getCustomerId());
				List<HealthClubBenefit> healthClubBenefits = healthClubBenefitRepository.getHealthClubBenefitByWorkflowStageAndDate(PRConstant.APPROVED_SM, sixDaysAgo, nextDate, commonUtils.getCustomerId());	
	            
				 List<String[]> data = getData(healthClubBenefits);
					 ByteArrayOutputStream out = generateTxt(data);
		    		byte[] txtBytes = out.toByteArray();
		    		String fileReference = getSystemTimeWithTimeStamp();
		    		String fileName = "HCB"+"Wps" + fileReference+ ".txt";
		    		String filePath = "hrmswps/"+fileName;
		    		
		    		
		    		log.debug("Value of FileName :{}  ,fileRefernce :{} file path:{}",fileName,fileReference,filePath);
		    		
		    		log.info("Inside HealthClubBenefitServiceImpl createWpsTxtFileForAllHealthClubBenefit rootDirBucketName :{}" , rootDirBucketName);
		    		InputStream inputStream = new ByteArrayInputStream(txtBytes);
		    		uploadFileInStorage(inputStream, fileName, filePath,rootDirBucketName);
		    		HttpHeaders headers = new HttpHeaders();
		            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		            headers.setContentDispositionFormData(ATTACHMENT, fileName);
		            //String fileDir = rootDirBucketName + filePath;
		            log.debug("The Path of File Dir to store is :{} ",filePath);
		             
		            savePath(filePath,currDate,year,weekNumber);
		            
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
			log.error("Error inside @class HealthClubBenefitServiceImpl @method createWpsTxtFileForAllHealthClubBenefit :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
	  throw new BusinessException("Something wrong in Creating wps for all Education Benefit ");
		}

	}
	
	List<String[]> getData(List<HealthClubBenefit> healthClubBenefits)
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
			 String alw = mapHrmsSystemConfigMap.get("ALW");	
		      log.debug(INSIDE_CLASS_LOG+"getData after all the data fetch from Hrms_System_Config mapHrmsSystemConfigMap :{} ",mapHrmsSystemConfigMap);
		
		    String currentDate = getCurrentDate();
			String currentTime = getCurrentTime();
			String valueDate = getDateAfterDays(1);
			String fileReference = "ILWPSOPOPVATEST"+getSystemTimeWithTimeStamp();
			String batchReference = "IWPSOPOP"+getSystemTimeWithTimeStampNew();
			
			 log.info(INSIDE_CLASS_LOG+"getData after set date and time");
		 
			if(healthClubBenefits !=null && !healthClubBenefits.isEmpty()) {
			Integer count =  healthClubBenefits.size();
			log.debug(INSIDE_CLASS_LOG+"fetch count :{}",count);	
			String totalCount = count.toString();
			log.debug(INSIDE_CLASS_LOG+"fetch totalCount :{}",totalCount);	
			Integer totalRecordCount = Integer.parseInt(totalCount)+2;
			String totalRecordCountString = totalRecordCount.toString();
			String netWorth=getTotalAmount(healthClubBenefits);
			 
			
			data = new ArrayList<>();
			data.add(new String[] {ifh, ifile, csv, connectId, customerId, fileReference, currentDate, currentTime, p, one, totalRecordCountString});
			data.add(new String[] {bathdr, achcr, totalCount, "", "", "", paymentPurposeCodeS, paymentNarrationCompany, "", first, valueDate, debitAccountNumber, sar, netWorth, "", "", "", "", "", "",  molEstablisedId, employerId, "", "", "", batchReference});
			iterateHealthClubBenefit(secpty, n, sach, healthClubBenefits, data,alw,paymentNarrationCompany);
			
			log.info(INSIDE_CLASS_LOG+"fetchData Health Club Benefit Successfully Iterate");
			}
			return data;

	  }
	  catch(Exception e)
	  {
    	  log.error("Error Inside @class HealthClubBenefitServiceImpl @method getData :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
    	  throw new BusinessException();  
	  }
	}	
	
	private void iterateHealthClubBenefit(String secpty, String n, String sach, List<HealthClubBenefit> healthClubBenefits,
			List<String[]> data,String alw ,String paymentNarrationCompany) {
		String one = "1";
		log.info(INSIDE_CLASS_LOG+" iterateHealthClubBenefit");
		for (HealthClubBenefit healthClubBenefit : healthClubBenefits) {
			log.debug(INSIDE_CLASS_LOG+"iterateHealthClubBenefit healthClubBenefit :{}",healthClubBenefit);
	        Integer employeeId = healthClubBenefit.getEmployee().getId();
	        String employeeReferenceId = healthClubBenefit.getEmployee().getEmployeeId();
	        String nationalIdentification = "";
	        log.debug("Inside @class HealthClubBenefitServiceImpl iterateHealthClubBenefit customerId is : {}", commonUtils.getCustomerId());
	        EmployeeNationalIdentification employeeNationalIdentification =  employeeNationalIdentificationRepository.findNationalIdentificationNumberByEmployeeId(employeeId, commonUtils.getCustomerId());
	        if(employeeNationalIdentification!=null){
	          nationalIdentification = employeeNationalIdentification.getIdentificationNumber();
	        }
	    	AccountDetailsService accountDetailsService = ApplicationContextProvider.getApplicationContext().getBean(AccountDetailsService.class);
	        List<AccountDetails> accountDetails = accountDetailsService.findAccountDetailsByEmployeeId(employeeId);

	        for (AccountDetails accountDetail : accountDetails) {
	        	log.debug(INSIDE_CLASS_LOG+"iterateHealthClubBenefit accountDetail :{}",accountDetail);
	            StringBuilder rowBuilder = new StringBuilder();
 
	            rowBuilder.append(secpty).append(",");
	            rowBuilder.append(accountDetail.getIban()).append(",");
	            rowBuilder.append(accountDetail.getBeneficiaryName()).append(",");
	           rowBuilder.append(employeeId).append(",");
	            rowBuilder.append(accountDetail.getBankId()).append(",").append(",").append(",");

	           rowBuilder.append(healthClubBenefit.getAmountPaid());
	           
	          rowBuilder.append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(n).append(",").append(n).append(",").append(",").append(",").append(",").append(",").append(",");
	           rowBuilder.append(sach).append(",").append(accountDetail.getBeneficiaryId()).append(","); 
	            rowBuilder.append(one).append(",").append(one).append(",").append(one).append(",").append(one).append(",");
	           rowBuilder.append(paymentNarrationCompany).append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",");
            rowBuilder.append(alw);			         
            data.add(rowBuilder.toString().split(","));
	            log.debug(INSIDE_CLASS_LOG+" iterateHealthClubBenefit data :{}",data);
	        }
	    }
	}

	void savePath(String path,LocalDate date,Integer year,Integer weekNumber)
	{
	  log.info("Inside @class HealthClubBenefitServiceImpl @method savePath ");
	  try
	  {
		  log.debug("Inside @class HealthClubBenefitServiceImpl customerId is : {}", commonUtils.getCustomerId());
		  healthClubBenefitRepository.setDataInFileData(HEALTH_CLUB_BENEFIT, date, path,year,weekNumber, commonUtils.getCustomerId());
		  log.debug("Inside @class HealthClubBenefitServiceImpl @method savePath date :{}  path:{} ",date,path);
	  }
	  catch(Exception e)
	  {
		  log.error("Error inside @class HealthClubBenefitServiceImpl @method savePath");
		  throw new BusinessException();
	  }
	}

	@Override
	public ResponseEntity<byte[]> downloadCommonWpsFile(Integer weekNum)  {
		 
		log.info("Inside @class HealthClubBenefitServiceImpl @method downloadCommonWpsFile");
		  LocalDate currDate = LocalDate.now();
		  Integer year = currDate.getYear();
		  log.debug("Inside @class HealthClubBenefitServiceImpl downloadCommonWpsFile customerId is : {}", commonUtils.getCustomerId());
		   String filePath =  healthClubBenefitRepository.getFilePath(HEALTH_CLUB_BENEFIT,weekNum,year, commonUtils.getCustomerId());
	    
	    		
	    log.info("Inside HealthClubBenefitServiceImpl downloadCommonWpsFile rootDirBucketName :{} ",rootDirBucketName);
		
	    String currentTime = getSystemTimeWithTimeStamp();
	  try {
	      log.debug("Inside HealthClubBenefitServiceImpl @method downloadCommonWpsFile :{} ",filePath );
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
		   log.error("Error Inside @class HealthClubBenefitServiceImpl @method downloadCommonWpsFile :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
	      throw new BusinessException("Something went wrong inside downloadCommonWpsFile " );
	  }
	}
  
	private String getTotalAmount(List<HealthClubBenefit> healthClubBenefits)
	   {
			log.debug("Inside Method getTotalAmount healthClubBenefits size :{} ",healthClubBenefits.size());
		   Double amount = 0.0 ;
		   for(HealthClubBenefit healthClubBenefit : healthClubBenefits)
		   {
			   amount = amount+healthClubBenefit.getAmountPaid();
		   }
		   log.debug("Inside Method getTotalAmount healthClubBenefits amount :{} ",amount);
		   return String.valueOf(amount);
		   
	   }
 
	 @Override
	    public List<Object[]> getAmountForHealthClubBenefit(LocalDate startDate , LocalDate endDate)
	    {
	    	log.info("Inside Method getAmountForHealthClubBenefit ");
	    	try
	    	{
	    		log.debug("Inside Method getAmountForHealthClubBenefit startDate :{}  endDate :{} ",startDate,endDate);
	    		log.debug("Inside @class HealthClubBenefitServiceImpl getAmountForHealthClubBenefit customerId is : {}", commonUtils.getCustomerId());
	    		List<Object[]> listSum = healthClubBenefitRepository.getHealthClubBenefitByDates(startDate, endDate, commonUtils.getCustomerId());
	    		log.debug(" The list 0f getAmountForHealthClubBenefit is :{}",listSum);
	    		return listSum;
	    	}
	    	catch(Exception e)
	    	{
	    		log.error("Error Inside @class HealthClubBenefitServiceImpl @method getAmountForHealthClubBenefit :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
	  	      throw new BusinessException("Something went wrong inside getAmountForHealthClubBenefit " );	
	    	}
	    }
	 
	    @Override
	 public  List<Object[]> getAmountForAccuralHealthClubBenefit(LocalDate startDate , LocalDate endDate)
	    {
	    	log.info("Inside Method getAmountForAccuralChildEducationBenefit ");
	    	try
	    	{
	    		log.debug("Inside Method getAmountForAccuralChildEducationBenefit startDate :{}  endDate :{} ",startDate,endDate);
	    		log.debug("Inside Method getAmountForAccuralChildEducationBenefit customerId is : {}", commonUtils.getCustomerId());
	    		List<Object[]> listSum = healthClubBenefitRepository.getHealthClubBenefitByDatesAcc(startDate, endDate, commonUtils.getCustomerId());
	    		log.debug(" The list 0f Child Education Benefit is :{}",listSum);
	    		return listSum;
	    	}
	    	catch(Exception e)
	    	{
	    		log.error("Error Inside @class ChildEducationBenefitServiceImpl @method getAmountForAccuralChildEducationBenefit :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
	  	      throw new BusinessException("Something went wrong inside getAmountForAccuralChildEducationBenefit " );	
	    	}
	    }

	

}
