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
import com.nouros.hrms.model.EducationalBenefit;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeNationalIdentification;
import com.nouros.hrms.model.NewHireBenefit;
import com.nouros.hrms.repository.EmployeeNationalIdentificationRepository;
import com.nouros.hrms.repository.NewHireBenefitRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.AccountDetailsService;
import com.nouros.hrms.service.NewHireBenefitService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.NewHireBenefitDto;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;
import com.nouros.payrollmanagement.utils.PRConstant;

@Service
public class NewHireBenefitServiceImpl extends AbstractService<Integer,NewHireBenefit> implements NewHireBenefitService{

	 private static final String INSIDE_CLASS_LOG = "Inside @Class NewHireBenefitServiceImpl @Method";
	 private static final String ATTACHMENT = "attachment";
	 private static final String NEW_HIRE_BENEFIT = "NewHireBenefit";

	
	@Value("${ROOT_DIR_HRMS_PAYROLL_FILE}")
	private String rootDirBucketName;

	
	public NewHireBenefitServiceImpl(GenericRepository<NewHireBenefit> repository) {
		super(repository, NewHireBenefit.class);
	}
	
	@Autowired
	private StorageRest storageRest;
	
	
	@Autowired
	private NewHireBenefitRepository newHireBenefitRepository;
	
	@Autowired
	private HrmsSystemConfigService hrmsSystemConfigService;
	
	  @Autowired
	  private EmployeeNationalIdentificationRepository employeeNationalIdentificationRepository;
	  
	  @Autowired
	  private CommonUtils commonUtils;


	private static final Logger log = LogManager.getLogger(NewHireBenefitServiceImpl.class);

	@Override
	public NewHireBenefit create(NewHireBenefit newHireBenefit) {
		log.info("inside @class NewHireBenefitServiceImpl @method create");

		try {
        Integer employeeId = newHireBenefit.getEmployee().getId(); 

        log.debug("inside @class NewHireBenefitServiceImpl @method create customerId is : {}", commonUtils.getCustomerId());
        int countForEmployee = newHireBenefitRepository.countByEmployee(employeeId,commonUtils.getCustomerId());

        if (countForEmployee > 0) {
            throw new BusinessException("Cannot create NewHireBenefit It Has Already Been Created By Employee.");
        }

        return newHireBenefitRepository.save(newHireBenefit);
		}catch (BusinessException ex) {
			throw new BusinessException("error in creating NewHireBenefit",ex.getMessage());
		}
	}
	
	@Override
	public void softDelete(int id) {

		NewHireBenefit newHireBenefit = super.findById(id);

		if (newHireBenefit != null) {

			NewHireBenefit newHireBenefit1 = newHireBenefit;
			newHireBenefitRepository.save(newHireBenefit1);

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
	public  HashMap<Integer, Double> getIdAndAmountByEmployeeId(Integer employeeId)
	{
		log.info("Inside @class NewHireBenefitServiceImpl @method getIdAndSumByEmployeeId ");
		log.debug(" Inside @getIdAndAmountByEmployeeId  customerId is : {}", commonUtils.getCustomerId());
		List<NewHireBenefit> newHireBenefitList = newHireBenefitRepository.findByEmployeeIdAndWorkflowStage(employeeId,PRConstant.APPROVED_SM);
		HashMap<Integer, Double> newHireBenefitHashMap  = new HashMap<>();
	
		try{
			for(NewHireBenefit newHireBenefit :newHireBenefitList )
			{
				Integer id =  newHireBenefit.getId();
				Double amount = newHireBenefit.getAmount();
				newHireBenefitHashMap.put(id,amount);
			}
			return newHireBenefitHashMap;
		}
		catch(Exception e)
		{
			log.error("Error inside @NewHireBenefitServiceImpl @method getIdAndAmountByEmployeeId :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	public Double getSumOfAmount( HashMap<Integer,Double> newHireBenefitMap)
	{
		log.info("Inside @class NewHireBenefitServiceImpl @method getSumOfAmount ");
		log.debug("Inside @class NewHireBenefitServiceImpl @method getSumOfAmount Map:{}",newHireBenefitMap);
		Double sum = 0.0;
		for (Map.Entry<Integer, Double> entry : newHireBenefitMap.entrySet()) {
		    try
		    {
		    	sum += entry.getValue();
		    }
		    catch(Exception e)
		    {
		       log.error("Error inside @class NewHireBenefitServiceImpl @method getSumOfAmount :{} :{}",e.getMessage(),Utils.getStackTrace(e));	
		    }
		}
		log.debug(" Inside @class NewHireBenefitServiceImpl @method getSumOfAmount Sum is :{}",sum);
        return sum;
	}

	public Boolean setWorkFlowStageForBulk(List<Integer> listOfKeys , String workflowStage)
	{
		log.info("Inside @class NewHireBenefitServiceImpl @method setWorkFlowStageForBulk ");
		try{
			for(Integer id : listOfKeys)
			{
				NewHireBenefit newHireBenefitOptional = super.findById(id);
			  if(newHireBenefitOptional != null)
			  {
				NewHireBenefit newHireBenefit = newHireBenefitOptional;
				newHireBenefit.setWorkflowStage(workflowStage);
				newHireBenefitRepository.save(newHireBenefit);
				log.debug("Inside @class NewHireBenefitServiceImpl @method setWorkFlowStageForBulk Status changed to :{}",newHireBenefit);	
			  }
			}
			return true;
		}
		catch(Exception e)
		{
			log.error("Error inside @NewHireBenefitServiceImpl @method setWorkFlowStageForBulk :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
			throw new BusinessException(e.getMessage());
		}
	}
	
	@Override
	public ResponseEntity<byte[]> createWpsTxtFileForNewHireBenefit(String newHireBenefitProcessInstanceId)
	{
		try {
			log.info("Inside @class NewHireBenefitServiceImpl @method createWpsTxtFile");
			if(newHireBenefitProcessInstanceId!=null) {
				log.debug(" Inside @createWpsTxtFileForNewHireBenefit  customerId is : {}", commonUtils.getCustomerId());
				Optional <NewHireBenefit> optinalNewHireBenefit = newHireBenefitRepository.findByProcessInstanceId(newHireBenefitProcessInstanceId);
		        if(optinalNewHireBenefit.isPresent())
		        {
		        	NewHireBenefit newHireBenefit =  optinalNewHireBenefit.get(); 
		        	Employee employee = newHireBenefit.getEmployee();
		        	List<String[]> data = fetchData(employee,newHireBenefit);
		        	ByteArrayOutputStream out = generateTxt(data);
		    		byte[] txtBytes = out.toByteArray();
		    		String fileReference = getSystemTimeWithTimeStamp();
		    		String fileName = String.valueOf(newHireBenefit.getId())+"Wps" + fileReference+ ".txt";
		    		String filePath = "hrmswps/"+fileName ;
		    		log.info("Inside NewHireBenefitServiceImpl createWpsTxtFile rootDirBucketName :{}" , rootDirBucketName);
		    		InputStream inputStream = new ByteArrayInputStream(txtBytes);
		    		uploadFileInStorage(inputStream, fileName, filePath,rootDirBucketName);
		    		HttpHeaders headers = new HttpHeaders();
		            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		            headers.setContentDispositionFormData(ATTACHMENT, fileName);
		          //  String fileDir = filePath;
		            //String fileDir = rootDirBucketName + filePath + fileName ;
            		            log.debug("The Path of File Dir to store is :{} ",filePath);
		            newHireBenefit.setOutputString(filePath);
		            newHireBenefitRepository.save(newHireBenefit);
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
		  
		  
	List<String[]> fetchData(Employee employee,NewHireBenefit newHireBenefit)
	{
	  try {
		  List<String[]> data = new ArrayList<>();
			AccountDetailsService accountDetailsService = ApplicationContextProvider.getApplicationContext().getBean(AccountDetailsService.class);
			List<AccountDetails> accountDetails = accountDetailsService.findAccountDetailsByEmployeeId(employee.getId());
			log.info("Inside @class NewHireBenefitServiceImpl @method fetchData");
			  Double amount = newHireBenefit.getAmount();
		         String amountString = String.valueOf(amount);
			log.info("Inside @class NewHireBenefitServiceImpl @method fetchData");
			 for (AccountDetails accountDetail : accountDetails) {
		        	log.debug( "Inside @class NewHireBenefitServiceImpl @method fetchData accountDetail Id :{}",accountDetail.getId());
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
							 String alw = "ALW";
					      String employeeReferenceId = employee.getEmployeeId();
					      String bankId = accountDetail.getBankId();
					      
					      String nationalIdentification = "";
					      log.debug("Inside @class NewHireBenefitServiceImpl @method fetchData customerId is : {}", commonUtils.getCustomerId());
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
			            data.add(new String[] {ifh, ifile, csv, connectId, customerId, fileReference, currentDate, currentTime, p, one,"3"});
			            data.add(new String[] {bathdr, achcr, one,"", "", "", paymentPurposeCodeO,"" ,paymentNarrationCompany, "", first,changedDate,debitAccountNumber ,sar,amountString ,"", "", "", "", "", "", pcmHsbcnetTest ,molEstablisedId, employerId, "", "", "",batchReference});
			            data.add(rowBuilder.toString().split(","));

			 }
			 
				return data;
	  }
	  catch(Exception e)
	  {
    	  log.error("Error Inside @class NewHireBenefitServiceImpl @method fetchData :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
    	  throw new BusinessException();  
	  }
 	}
	
	
	
	private String getSystemTimeWithTimeStampNew() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
	    return now.format(formatter);
	}
   
	
	@Override
public ResponseEntity<byte[]> downloadWpsFile(String newHireBenefitProcessInstanceId) {
		 
		
		if(newHireBenefitProcessInstanceId!=null) {
			log.debug("Inside @class NewHireBenefitServiceImpl @method downloadWpsFile customerId is : {}", commonUtils.getCustomerId());
			Optional <NewHireBenefit> optinalNewHireBenefit = newHireBenefitRepository.findByProcessInstanceId(newHireBenefitProcessInstanceId);
	        if(optinalNewHireBenefit.isPresent())
	        {
	        	NewHireBenefit newHireBenefit =  optinalNewHireBenefit.get(); 
	    	    String filePath = newHireBenefit.getOutputString();
	    	    
	    		
	    	    log.info("Inside NewHireBenefitServiceImpl downloadWpsFile rootDirBucketName :{} ",rootDirBucketName);
	    		
	    	    String currentTime = getSystemTimeWithTimeStamp();
	    	  try {
	    	      log.debug("Inside NewHireBenefitServiceImpl @method downloadWpsFile :{} ",filePath );
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
	    	      throw new BusinessException("Something went wrong inside downloadWpsFile", e);
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
	public NewHireBenefit updateNewHireBenefitWorkflowStage(NewHireBenefitDto newHireBenefitDto) {
		log.debug("Inside method updateNewHireBenefitWorkflowStage Id : {}", newHireBenefitDto.getNewHireBenefitId());
		try {
			if (newHireBenefitDto.getNewHireBenefitId() != null) {
				NewHireBenefit optionalNewHireBenefit = super.findById(newHireBenefitDto.getNewHireBenefitId());
				if (optionalNewHireBenefit != null) {
					NewHireBenefit newHireBenefit = optionalNewHireBenefit;
					newHireBenefit.setWorkflowStage(newHireBenefitDto.getWorkflowStage());
					return newHireBenefitRepository.save(newHireBenefit);
				} else {
					throw new BusinessException("NewHireBenefit with ID " + newHireBenefitDto.getNewHireBenefitId() + " not found");
				}
			}
		} catch (Exception e) {
			throw new BusinessException("error while updating NewHireBenefit work flow stage", e.getMessage());
		}
		return null;
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
	
	@Override
	public ResponseEntity<byte[]> createWpsTxtFileForAllNewHireBenefit()
	{
		log.info("inside @class NewHireBenefitServiceImpl @method createWpsTxtFileForAllNewHireBenefit ");
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
		        log.debug(" Inside @createWpsTxtFileForAllNewHireBenefit  customerId is : {}", commonUtils.getCustomerId());
				List<NewHireBenefit> newHireBenefits = newHireBenefitRepository.getNewHireBenefitByWorkflowStageAndDate(PRConstant.APPROVED_SM, sixDaysAgo, nextDate,commonUtils.getCustomerId());	
	            
				 List<String[]> data = getData(newHireBenefits);
					 ByteArrayOutputStream out = generateTxt(data);
		    		byte[] txtBytes = out.toByteArray();
		    		String fileReference = getSystemTimeWithTimeStamp();
		    		String fileName = "NHB"+"Wps" + fileReference+ ".txt";
		    		String filePath = "hrmswps/"+fileName;
		    		
		    		
		    		log.debug("Value of FileName :{}  ,fileRefernce :{} file path:{}",fileName,fileReference,filePath);
		    		
		    		log.info("Inside NewHireBenefitServiceImpl createWpsTxtFileForAllNewHireBenefit rootDirBucketName :{}" , rootDirBucketName);
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
// 			else
// 			{
// 				log.info("It's not saturday ");
// 				return null;
//			}
		}
		catch(Exception e)
		{
			log.error("Error inside @class NewHireBenefitServiceImpl @method createWpsTxtFileForAllNewHireBenefit :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
	  throw new BusinessException("Something wrong in Creating wps for all Education Benefit ");
		}

	}
	
	List<String[]> getData(List<NewHireBenefit> newHireBenefits)
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
		 
			if(newHireBenefits !=null && !newHireBenefits.isEmpty()) {
			Integer count =  newHireBenefits.size();
			log.debug(INSIDE_CLASS_LOG+"fetch count :{}",count);	
			String totalCount = count.toString();
			log.debug(INSIDE_CLASS_LOG+"fetch totalCount :{}",totalCount);	
			Integer totalRecordCount = Integer.parseInt(totalCount)+2;
			String totalRecordCountString = totalRecordCount.toString();
			String netWorth=getTotalAmount(newHireBenefits);
			 
			
			data = new ArrayList<>();
			data.add(new String[] {ifh, ifile, csv, connectId, customerId, fileReference, currentDate, currentTime, p, one, totalRecordCountString});
			data.add(new String[] {bathdr, achcr, totalCount, "", "", "", paymentPurposeCodeS, paymentNarrationCompany, "", first, valueDate, debitAccountNumber, sar, netWorth, "", "", "", "", "", "",  molEstablisedId, employerId, "", "", "", batchReference});
			iterateNewHireBenefit(secpty, n, sach, newHireBenefits, data,alw,paymentNarrationCompany);
			
			log.info(INSIDE_CLASS_LOG+"fetchData New Hire Benefit Successfully Iterate");
			}
			return data;

	  }
	  catch(Exception e)
	  {
    	  log.error("Error Inside @class NewHireBenefitServiceImpl @method getData :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
    	  throw new BusinessException();  
	  }
	}	
	
	private void iterateNewHireBenefit(String secpty, String n, String sach, List<NewHireBenefit> newHireBenefits,
			List<String[]> data,String alw ,String paymentNarrationCompany) {
		String one = "1";
		log.info(INSIDE_CLASS_LOG+" iterateNewHireBenefit");
		for (NewHireBenefit newHireBenefit : newHireBenefits) {
			log.debug(INSIDE_CLASS_LOG+"iterateNewHireBenefit newHireBenefit :{}",newHireBenefit);
	        Integer employeeId = newHireBenefit.getEmployee().getId();
	        String employeeReferenceId = newHireBenefit.getEmployee().getEmployeeId();
	        String nationalIdentification = "";
	        log.debug("Inside iterateNewHireBenefit customerId is : {}", commonUtils.getCustomerId());
	        EmployeeNationalIdentification employeeNationalIdentification =  employeeNationalIdentificationRepository.findNationalIdentificationNumberByEmployeeId(employeeId, commonUtils.getCustomerId());
	        if(employeeNationalIdentification!=null){
	          nationalIdentification = employeeNationalIdentification.getIdentificationNumber();
	        }
	    	AccountDetailsService accountDetailsService = ApplicationContextProvider.getApplicationContext().getBean(AccountDetailsService.class);
	        List<AccountDetails> accountDetails = accountDetailsService.findAccountDetailsByEmployeeId(employeeId);

	        for (AccountDetails accountDetail : accountDetails) {
	        	log.debug(INSIDE_CLASS_LOG+"iterateNewHireBenefit accountDetail :{}",accountDetail);
	            StringBuilder rowBuilder = new StringBuilder();
 
	        	 rowBuilder.append(secpty).append(",");
		            rowBuilder.append(accountDetail.getIban()).append(",");
		            rowBuilder.append(accountDetail.getBeneficiaryName()).append(",");
		           rowBuilder.append(employeeId).append(",");
		            rowBuilder.append(accountDetail.getBankId()).append(",").append(",").append(",");

		           rowBuilder.append(newHireBenefit.getAmount());
		           
		          rowBuilder.append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(n).append(",").append(n).append(",").append(",").append(",").append(",").append(",").append(",");
		           rowBuilder.append(sach).append(",").append(accountDetail.getBeneficiaryId()).append(","); 
		            rowBuilder.append(one).append(",").append(one).append(",").append(one).append(",").append(one).append(",");
		           rowBuilder.append(paymentNarrationCompany).append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",");
	            rowBuilder.append(alw);
	            data.add(rowBuilder.toString().split(","));
	            log.debug(INSIDE_CLASS_LOG+" iterateNewHireBenefit data :{}",data);
	        }
	    }
	}

	void savePath(String path,LocalDate date,Integer year,Integer weekNumber)
	{
	  log.info("Inside @class NewHireBenefitServiceImpl @method savePath ");
	  try
	  {
		  log.debug(" Inside @savePath  customerId is : {}", commonUtils.getCustomerId());
		  newHireBenefitRepository.setDataInFileData(NEW_HIRE_BENEFIT, date, path,year,weekNumber,commonUtils.getCustomerId());
		  log.debug("Inside @class NewHireBenefitServiceImpl @method savePath date :{}  path:{} ",date,path);
	  }
	  catch(Exception e)
	  {
		  log.error("Error inside @class NewHireBenefitServiceImpl @method savePath");
		  throw new BusinessException();
	  }
	}

	@Override
	public ResponseEntity<byte[]> downloadCommonWpsFile(Integer weekNum)  {
		 
		log.info("Inside @class NewHireBenefitServiceImpl @method downloadCommonWpsFile");
		  LocalDate currDate = LocalDate.now();
		  Integer year = currDate.getYear();
		  log.debug(" Inside @downloadCommonWpsFile  customerId is : {}", commonUtils.getCustomerId());
		   String filePath =  newHireBenefitRepository.getFilePath(NEW_HIRE_BENEFIT,weekNum,year,commonUtils.getCustomerId());
	    
	    		
	    log.info("Inside NewHireBenefitServiceImpl downloadCommonWpsFile rootDirBucketName :{} ",rootDirBucketName);
		
	    String currentTime = getSystemTimeWithTimeStamp();
	  try {
	      log.debug("Inside NewHireBenefitServiceImpl @method downloadCommonWpsFile :{} ",filePath );
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
		   log.error("Error Inside @class NewHireBenefitServiceImpl @method downloadCommonWpsFile :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
	      throw new BusinessException("Something went wrong inside downloadCommonWpsFile " );
	  }
	}

  
    private String getTotalAmount(List<NewHireBenefit> newHireBenefits)
	   {
		   Double amount = 0.0 ;
		   log.debug("Inside method getTotalAmount newHireBenefits size :{} ",newHireBenefits.size());
		   for(NewHireBenefit newHireBenefit : newHireBenefits)
		   {
			   amount = amount+newHireBenefit.getAmount();
		   }
		   log.debug("Inside method getTotalAmount newHireBenefits amount :{} ",amount);
		   return String.valueOf(amount);
		   
	   }
	 
   
    @Override
    public List<Object[]> getAmountForNewHireBenefit(LocalDate startDate , LocalDate endDate)
    {
    	log.info("Inside Method getAmountForNewHireBenefit ");
    	try
    	{
    		log.debug("Inside Method getAmountForNewHireBenefit startDate :{}  endDate :{} ",startDate,endDate);
    		log.debug(" Inside @getAmountForNewHireBenefit  customerId is : {}", commonUtils.getCustomerId());
    		List<Object[]> listSum = newHireBenefitRepository.getNewHireBenefitByDates(startDate, endDate,commonUtils.getCustomerId());
    		log.debug(" The list 0f Educational Benefit is :{}",listSum);
    		return listSum;
    	}
    	catch(Exception e)
    	{
    		log.error("Error Inside @class EducationalBenefitServiceImpl @method getAmountForNewHireBenefit :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
  	      throw new BusinessException("Something went wrong inside getAmountForNewHireBenefit " );	
    	}
    }
 
    @Override
 public  List<Object[]> getAmountForAccuralNewHireBenefit(LocalDate startDate , LocalDate endDate)
    {
    	log.info("Inside Method getAmountForEducationalBenefit ");
    	try
    	{
    		log.debug("Inside Method getAmountForAccuralNewHireBenefit startDate :{}  endDate :{} ",startDate,endDate);
    		log.debug(" Inside @getAmountForAccuralNewHireBenefit  customerId is : {}", commonUtils.getCustomerId());
    		List<Object[]> listSum = newHireBenefitRepository.getNewHireBenefitByDatesAcc(startDate, endDate,commonUtils.getCustomerId());
    		log.debug(" The list 0f Educational Benefit is :{}",listSum);
    		return listSum;
    	}
    	catch(Exception e)
    	{
    		log.error("Error Inside @class EducationalBenefitServiceImpl @method getAmountForAccuralNewHireBenefit :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
  	      throw new BusinessException("Something went wrong inside getAmountForAccuralNewHireBenefit " );	
    	}
    }


	
	
}
