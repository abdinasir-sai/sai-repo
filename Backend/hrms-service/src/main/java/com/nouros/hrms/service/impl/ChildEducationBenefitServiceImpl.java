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
import com.nouros.hrms.model.ChildEducationBenefit;
import com.nouros.hrms.model.EducationalBenefit;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeChildren;
import com.nouros.hrms.model.EmployeeNationalIdentification;
import com.nouros.hrms.model.NewHireBenefit;
import com.nouros.hrms.repository.ChildEducationBenefitRepository;
import com.nouros.hrms.repository.EmployeeChildrenRepository;
import com.nouros.hrms.repository.EmployeeNationalIdentificationRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.AccountDetailsService;
import com.nouros.hrms.service.ChildEducationBenefitService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.ChildEducationBenefitDto;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;
import com.nouros.payrollmanagement.utils.PRConstant;

@Service
public class ChildEducationBenefitServiceImpl extends AbstractService<Integer,ChildEducationBenefit> implements ChildEducationBenefitService {

	private static final Logger log = LogManager.getLogger(ChildEducationBenefitServiceImpl.class);

	public ChildEducationBenefitServiceImpl(GenericRepository<ChildEducationBenefit> repository) {
		super(repository, ChildEducationBenefit.class);
	}

	@Autowired
	private StorageRest storageRest;
	
	@Autowired
	private EmployeeChildrenRepository employeeChildrenRepository;
	
	 private static final String INSIDE_CLASS_LOG = "Inside @Class ChildEducationBenefitServiceImpl @Method";
	 private static final String ATTACHMENT = "attachment";
	 private static final String CHILD_EDUCATION_BENEFIT = "ChildEducationBenefit";

	
	@Value("${ROOT_DIR_HRMS_PAYROLL_FILE}")
	private String rootDirBucketName;

	
	@Autowired
	private ChildEducationBenefitRepository childEducationBenefitRepository;
	
	  @Autowired
	  private EmployeeNationalIdentificationRepository employeeNationalIdentificationRepository;
	  
		@Autowired
		private HrmsSystemConfigService hrmsSystemConfigService;
		
		@Autowired
		private CommonUtils commonUtils;


//	@Override
//	public ChildEducationBenefit create(ChildEducationBenefit childEducationBenefit) {
//		log.info("inside @class ChildEducationBenefitServiceImpl @method create");
//
//		try {
//        Integer employeeId = childEducationBenefit.getEmployee().getId(); 
//
//        
//        int countForEmployeeThisYear = childEducationBenefitRepository.countEmployeeByEmployeeIdAndDateOfJoining(employeeId);
//
//        if (countForEmployeeThisYear > 0) {
//            throw new BusinessException("Cannot create ChildEducationBenefit it has already been created for this Employee within one year of their date of joining.");
//        }
//
//        return childEducationBenefitRepository.save(childEducationBenefit);
//		}catch (Exception ex) {
//			throw new BusinessException("error in creating ChildEducationBenefit",ex.getMessage());
//		}
//	}
	
	@Override
	public ChildEducationBenefit create(ChildEducationBenefit childEducationBenefit) {
	    log.info("Inside ChildEducationBenefitServiceImpl @method create");

	    try {
	        Integer employeeId = childEducationBenefit.getEmployee().getId();
	        
	        LocalDate startOfYearCycle = LocalDate.of(LocalDate.now().getYear(), 8, 1);
	        LocalDate endOfYearCycle = LocalDate.of(LocalDate.now().getYear() + 1, 7, 31);
	        
	        Double amountPaid = childEducationBenefit.getAmountRequired();
	        
	        if (amountPaid > 105000) {
	        	log.error("Amount Paid for the year exceeds the limit of 105000");
	            throw new BusinessException("Amount Paid Cannot Be Greater Than 105000");
	        }

	        List<EmployeeChildren> children = employeeChildrenRepository.findByEmployeeId(employeeId);

	        log.debug("Inside ChildEducationBenefitServiceImpl @method create list of children : {}", children);

	        for (EmployeeChildren child : children) {
	            double amountToBeCreated = child.getAmount(); 
	            log.debug("Inside ChildEducationBenefitServiceImpl create customerId is : {}", commonUtils.getCustomerId());
	            Double totalAmountForChild = employeeChildrenRepository.getTotalAmountForChildInYearCycle(
	                employeeId, child.getName(), startOfYearCycle, endOfYearCycle,commonUtils.getCustomerId());

	            if (totalAmountForChild == null) {
	                totalAmountForChild = 0.0;
	            }

	            if (totalAmountForChild + amountToBeCreated > 35000) {
	                throw new BusinessException("Cannot create ChildEducationBenefit, amount exceeds 35,000 for child: " + child.getName());
	            }
	        }

	        return childEducationBenefitRepository.save(childEducationBenefit);
	    } catch (Exception ex) {
	        throw new BusinessException("Error in creating ChildEducationBenefit", ex.getMessage());
	    }
	}



	
	public HashMap<Integer,Double> getIdAndAmountByEmployeeId(Integer employeeId)
	{
		log.info("Inside @class ChildEducationBenefitServiceImpl @method getIdAndSumByEmployeeId");
		log.debug("Inside @class ChildEducationBenefitServiceImpl @method getIdAndSumByEmployeeId customerId is : {}", commonUtils.getCustomerId());
		List<ChildEducationBenefit> childEducationBenefitList = childEducationBenefitRepository.findByEmployeeIdAndWorkflowStage(employeeId,"Approved", commonUtils.getCustomerId());
		HashMap<Integer, Double> childEducationBenefitHashMap = new HashMap<>();
	   try
	   {
		for(ChildEducationBenefit childEducationBenefit : childEducationBenefitList)
		{
			Integer id =  childEducationBenefit.getId();
			Double amountRequried = childEducationBenefit.getAmountRequired();  
			childEducationBenefitHashMap.put(id, amountRequried);
		}
		 return childEducationBenefitHashMap;
	   }
	   catch(Exception e)
	   {
        log.error("Error inside @ChildEducationBenefitServiceImpl @method getIdAndAmountByEmployeeId :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
		throw new BusinessException();
	   }
	}
	
	public Double getSumOfAmount(HashMap<Integer,Double> childEducationBenefitMap)
	{
		log.info("Inside @class ChildEducationBenefitServiceImpl @method getSumOfAmount ");
		log.debug("Inside @class ChildEducationBenefitServiceImpl @method getSumOfAmount Map:{}",childEducationBenefitMap);
		Double sum = 0.0;
		for (Map.Entry<Integer, Double> entry : childEducationBenefitMap.entrySet()) {
		    try
		    {
		    	sum += entry.getValue();
		    }
		    catch(Exception e)
		    {
		    	log.error("Error inside @class ChildEducationBenefitServiceImpl @method getSumOfAmount :{} :{}",e.getMessage(),Utils.getStackTrace(e));
		    }
		}
		log.debug(" Inside @class ChildEducationBenefitServiceImpl @method getSumOfAmount Sum is :{}",sum);
        return sum;
	}
	public Boolean setWorkFlowStageForBulk(List<Integer> listOfKeys , String workflowStage)
	{
		log.info("Inside @class ChildEducationBenefitServiceImpl @method setWorkFlowStageForBulk ");
        try{
			for(Integer id : listOfKeys)
			{
				ChildEducationBenefit childEducationBenefitOptional = super.findById( id);
			  if(childEducationBenefitOptional != null)
			  {
				ChildEducationBenefit childEducationBenefit = childEducationBenefitOptional;
				childEducationBenefit.setWorkflowStage(workflowStage);
				childEducationBenefitRepository.save(childEducationBenefit);
				log.debug("Inside @class EducationBenefitService @method setWorkFlowStageForBulk Status changed to :{}",workflowStage);	
			  }
			}
			return true;
		}
		catch(Exception e)
		{
			log.error("Error inside @ChildEducationBenefitServiceImpl @method setWorkFlowStageForBulk :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}
	
	
	
	public ResponseEntity<byte[]> createWpsTxtFileForChildEducationalBenefit(String childEducationBenefitProcessInstanceId)
	{
		try {
			log.info("Inside @class ChildEducationBenefitServiceImpl @method createWpsTxtFile");
			if(childEducationBenefitProcessInstanceId!=null) {
				log.debug("Inside @class ChildEducationBenefitServiceImpl @method createWpsTxtFileForChildEducationalBenefit customerId is : {}", commonUtils.getCustomerId());
				Optional <ChildEducationBenefit> optinalChildEducationBenefit = childEducationBenefitRepository.findByProcessInstanceId(childEducationBenefitProcessInstanceId, commonUtils.getCustomerId());
		        if(optinalChildEducationBenefit.isPresent())
		        {
		        	ChildEducationBenefit childEducationBenefit =  optinalChildEducationBenefit.get(); 
		        	Employee employee = childEducationBenefit.getEmployee();
		        	List<String[]> data = fetchData(employee,childEducationBenefit);
		        	ByteArrayOutputStream out = generateTxt(data);
		    		byte[] txtBytes = out.toByteArray();
		    		String fileReference = getSystemTimeWithTimeStamp();
		    		String fileName = String.valueOf(childEducationBenefit.getId())+"Wps" + fileReference+ ".txt";
		    		String filePath = "hrmswps/"+fileName;
		    		log.info("Inside EmployeeMonthlySalary createWpsTxtFile rootDirBucketName :{}" , rootDirBucketName);
		    		InputStream inputStream = new ByteArrayInputStream(txtBytes);
		    		uploadFileInStorage(inputStream, fileName, filePath,rootDirBucketName);
		    		HttpHeaders headers = new HttpHeaders();
		            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		            headers.setContentDispositionFormData(ATTACHMENT, fileName);
		            //String fileDir = filePath  ;
//		            String fileDir = rootDirBucketName + filePath  ;
		            log.debug("The Path of File Dir to store is :{} ",filePath);
		            childEducationBenefit.setOutputString(filePath);
		            childEducationBenefitRepository.save(childEducationBenefit);
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
	
	public ResponseEntity<byte[]> downloadWpsFile(String childEducationBenefitProcessInstanceId)  {
		 
		log.info("Inside @class EducationalBenefitServiceImpl @method createWpsTxtFile");
		if(childEducationBenefitProcessInstanceId!=null) {
			log.debug("Inside @class EducationalBenefitServiceImpl downloadWpsFile customerId is : {}", commonUtils.getCustomerId());
			Optional <ChildEducationBenefit> optinalChildEducationBenefit = childEducationBenefitRepository.findByProcessInstanceId(childEducationBenefitProcessInstanceId, commonUtils.getCustomerId());
			
			if(optinalChildEducationBenefit.isPresent())
	        {
				ChildEducationBenefit childEducationBenefitt =  optinalChildEducationBenefit.get(); 

  String filePath = childEducationBenefitt.getOutputString();
	    
	    		
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
		  
		  
	List<String[]> fetchData(Employee employee,ChildEducationBenefit childEducationBenefit)
	{
	  try {
		  List<String[]> data = new ArrayList<>();
			AccountDetailsService accountDetailsService = ApplicationContextProvider.getApplicationContext().getBean(AccountDetailsService.class);
			List<AccountDetails> accountDetails = accountDetailsService.findAccountDetailsByEmployeeId(employee.getId());
			log.info("Inside @class ChildEducationBenefitServiceImpl @method fetchData");
			Double amount = childEducationBenefit.getAmountRequired();
	         String amountString = String.valueOf(amount);
			for (AccountDetails accountDetail : accountDetails) {
		        	log.debug( "Inside @class ChildEducationBenefitServiceImpl @method fetchData accountDetail Id :{}",accountDetail.getId());
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
					      log.debug(" Inside @ CEBSI fetchData customerId is : {}", commonUtils.getCustomerId());
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

			 }
			return data;

	  }
	  catch(Exception e)
	  {
    	  log.error("Error Inside @class ChildEducationBenefitServiceImpl @method fetchData :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
    	  throw new BusinessException();  
	  }
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
	public ChildEducationBenefit updateChildEducationBenefitWorkflowStage(
			ChildEducationBenefitDto childEducationBenefitDto) {
		log.debug("Inside method updateChildEducationBenefitWorkflowStage Id : {}", childEducationBenefitDto.getChildEducationBenefitId());
		try {
			if (childEducationBenefitDto.getChildEducationBenefitId() != null) {
				ChildEducationBenefit optionalChildEducationBenefit = super.findById(childEducationBenefitDto.getChildEducationBenefitId());
				if (optionalChildEducationBenefit != null) {
					ChildEducationBenefit childEducationBenefit = optionalChildEducationBenefit;
					childEducationBenefit.setWorkflowStage(childEducationBenefitDto.getWorkflowStage());
					return childEducationBenefitRepository.save(childEducationBenefit);
				} else {
					throw new BusinessException("ChildEducationBenefit with ID " + childEducationBenefitDto.getChildEducationBenefitId() + " not found");
				}
			}
		} catch (Exception e) {
			throw new BusinessException("error while updating ChildEducationBenefit work flow stage", e.getMessage());
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
	public ResponseEntity<byte[]> createWpsTxtFileForAllChildEducationBenefit()
	{
		log.info("inside @class ChildEducationBenefitServiceImpl @method createWpsTxtFileForAllChildEducationBenefit ");
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
		        log.debug(" Inside @createWpsTxtFileForAllChildEducationBenefit  customerId is : {}", commonUtils.getCustomerId());
				List<ChildEducationBenefit> childEducationBenefits = childEducationBenefitRepository.getChildEducationBenefitByWorkflowStageAndDate(PRConstant.APPROVED_SM, sixDaysAgo, nextDate, commonUtils.getCustomerId());	
	            
				 List<String[]> data = getData(childEducationBenefits);
					 ByteArrayOutputStream out = generateTxt(data);
		    		byte[] txtBytes = out.toByteArray();
		    		String fileReference = getSystemTimeWithTimeStamp();
		    		String fileName = "CEB"+"Wps" + fileReference+ ".txt";
		    		String filePath = "hrmswps/"+fileName;
		    		
		    		
		    		log.debug("Value of FileName :{}  ,fileRefernce :{} file path:{}",fileName,fileReference,filePath);
		    		
		    		log.info("Inside ChildEducationBenefitServiceImpl createWpsTxtFileForAllChildEducationBenefit rootDirBucketName :{}" , rootDirBucketName);
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
			log.error("Error inside @class ChildEducationBenefitServiceImpl @method createWpsTxtFileForAllChildEducationBenefit :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
	  throw new BusinessException("Something wrong in Creating wps for all Education Benefit ");
		}

	}
	
	List<String[]> getData(List<ChildEducationBenefit> childEducationBenefits)
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
		 
			if(childEducationBenefits !=null && !childEducationBenefits.isEmpty()) {
			Integer count =  childEducationBenefits.size();
			log.debug(INSIDE_CLASS_LOG+"fetch count :{}",count);	
			String totalCount = count.toString();
			log.debug(INSIDE_CLASS_LOG+"fetch totalCount :{}",totalCount);	
			Integer totalRecordCount = Integer.parseInt(totalCount)+2;
			String totalRecordCountString = totalRecordCount.toString();
			String netWorth=getTotalAmount(childEducationBenefits);
			 
			
			data = new ArrayList<>();
			data.add(new String[] {ifh, ifile, csv, connectId, customerId, fileReference, currentDate, currentTime, p, one, totalRecordCountString});
			data.add(new String[] {bathdr, achcr, totalCount, "", "", "", paymentPurposeCodeS, paymentNarrationCompany, "", first, valueDate, debitAccountNumber, sar, netWorth, "", "", "", "", "", "",  molEstablisedId, employerId, "", "", "", batchReference});
			iterateChildEducationBenefit(secpty, n, sach, childEducationBenefits, data,alw,paymentNarrationCompany);
			
			log.info(INSIDE_CLASS_LOG+"fetchData New Hire Benefit Successfully Iterate");
			}
			return data;

	  }
	  catch(Exception e)
	  {
    	  log.error("Error Inside @class ChildEducationBenefitServiceImpl @method getData :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
    	  throw new BusinessException();  
	  }
	}	
	
	private void iterateChildEducationBenefit(String secpty, String n, String sach, List<ChildEducationBenefit> childEducationBenefits,
			List<String[]> data,String alw,String paymentNarrationCompany) {
		String one = "1";
		log.info(INSIDE_CLASS_LOG+" iterateChildEducationBenefit");
		for (ChildEducationBenefit childEducationBenefit : childEducationBenefits) {
			log.debug(INSIDE_CLASS_LOG+"iterateChildEducationBenefit newHireBenefit :{}",childEducationBenefit);
	        Integer employeeId = childEducationBenefit.getEmployee().getId();
	        String employeeReferenceId = childEducationBenefit.getEmployee().getEmployeeId();
	        String nationalIdentification = "";
	        log.debug(" Inside @iterateChildEducationBenefit  customerId is : {}", commonUtils.getCustomerId());
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

		           rowBuilder.append(childEducationBenefit.getAmountRequired());
		           
		          rowBuilder.append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(n).append(",").append(n).append(",").append(",").append(",").append(",").append(",").append(",");
		           rowBuilder.append(sach).append(",").append(accountDetail.getBeneficiaryId()).append(","); 
		            rowBuilder.append(one).append(",").append(one).append(",").append(one).append(",").append(one).append(",");
		           rowBuilder.append(paymentNarrationCompany).append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",");
	            rowBuilder.append(alw);	
	            data.add(rowBuilder.toString().split(","));
		            log.debug(INSIDE_CLASS_LOG+" iterateChildEducationBenefit data :{}",data);
	        }
	    }
	}

	void savePath(String path,LocalDate date,Integer year,Integer weekNumber)
	{
	  log.info("Inside @class ChildEducationBenefitServiceImpl @method savePath ");
	  try
	  {
		  childEducationBenefitRepository.setDataInFileData(CHILD_EDUCATION_BENEFIT, date, path,year,weekNumber);
		  log.debug("Inside @class ChildEducationBenefitServiceImpl @method savePath date :{}  path:{} ",date,path);
	  }
	  catch(Exception e)
	  {
		  log.error("Error inside @class ChildEducationBenefitServiceImpl @method savePath");
		  throw new BusinessException();
	  }
	}

	@Override
	public ResponseEntity<byte[]> downloadCommonWpsFile(Integer weekNum)  {
		 
		log.info("Inside @class ChildEducationBenefitServiceImpl @method downloadCommonWpsFile");
		  LocalDate currDate = LocalDate.now();
		  Integer year = currDate.getYear();
		  log.debug("Inside ChildEducationBenefitServiceImpl downloadCommonWpsFile customerId: {}", commonUtils.getCustomerId());
		  String filePath =  childEducationBenefitRepository.getFilePath(CHILD_EDUCATION_BENEFIT,weekNum,year, commonUtils.getCustomerId());
	    
	    		
	    log.info("Inside ChildEducationBenefitServiceImpl downloadCommonWpsFile rootDirBucketName :{} ",rootDirBucketName);
		
	    String currentTime = getSystemTimeWithTimeStamp();
	  try {
	      log.debug("Inside ChildEducationBenefitServiceImpl @method downloadCommonWpsFile :{} ",filePath );
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
		   log.error("Error Inside @class ChildEducationBenefitServiceImpl @method downloadCommonWpsFile :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
	      throw new BusinessException("Something went wrong inside downloadCommonWpsFile " );
	  }
	}

	private String getSystemTimeWithTimeStampNew() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
	    return now.format(formatter);
	}
   
	private String getTotalAmount(List<ChildEducationBenefit> childEducationBenefits)
	   {
		log.debug("Inside Method getTotalAmount size :{} ",childEducationBenefits.size());
		   Double amount = 0.0 ;
		   for(ChildEducationBenefit childEducationBenefit : childEducationBenefits)
		   {
			   amount = amount+childEducationBenefit.getAmountRequired();
		   }
		   log.debug("Inside Method getTotalAmount childEducationBenefit amount :{} ",amount);
		   return String.valueOf(amount);
		   
	   }

	
    @Override
    public List<Object[]> getAmountForChildEducationBenefit(LocalDate startDate , LocalDate endDate)
    {
    	log.info("Inside Method getAmountForChildEducationBenefit ");
    	try
    	{
    		log.debug("Inside Method getAmountForChildEducationBenefit startDate :{}  endDate :{} ",startDate,endDate);
    		log.debug("Inside Method getAmountForChildEducationBenefit customerId is : {}", commonUtils.getCustomerId());
    		List<Object[]> listSum = childEducationBenefitRepository.getChildEducationBenefitByDates(startDate, endDate, commonUtils.getCustomerId());
    		log.debug(" The list 0f getAmountForChildEducationBenefit is :{}",listSum);
    		return listSum;
    	}
    	catch(Exception e)
    	{
    		log.error("Error Inside @class ChildEducationBenefitServiceImpl @method getAmountForChildEducationBenefit :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
  	      throw new BusinessException("Something went wrong inside getAmountForChildEducationBenefit " );	
    	}
    }
 
    @Override
 public  List<Object[]> getAmountForAccuralChildEducationBenefit(LocalDate startDate , LocalDate endDate)
    {
    	log.info("Inside Method getAmountForAccuralChildEducationBenefit ");
    	try
    	{
    		log.debug("Inside Method getAmountForAccuralChildEducationBenefit startDate :{}  endDate :{} ",startDate,endDate);
    		log.debug("Inside Method getAmountForAccuralChildEducationBenefit customerId is : {}", commonUtils.getCustomerId());
    		List<Object[]> listSum = childEducationBenefitRepository.getChildEducationBenefitByDatesAcc(startDate, endDate , commonUtils.getCustomerId());
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
