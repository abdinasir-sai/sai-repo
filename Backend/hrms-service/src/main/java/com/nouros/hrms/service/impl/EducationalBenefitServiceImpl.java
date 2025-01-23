package com.nouros.hrms.service.impl;

import java.io.BufferedWriter ;
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
import com.nouros.hrms.repository.EducationalBenefitRepository;
import com.nouros.hrms.repository.EmployeeNationalIdentificationRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.AccountDetailsService;
import com.nouros.hrms.service.EducationalBenefitService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.EducationalBenefitDto;
import com.nouros.payrollmanagement.model.EmployeeMonthlySalary;
import com.nouros.payrollmanagement.model.PayrollRun;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;
import com.nouros.payrollmanagement.utils.PRConstant;
 
@Service
public class EducationalBenefitServiceImpl extends AbstractService<Integer,EducationalBenefit> implements EducationalBenefitService{

	private static final String TOTAL_AMOUNT_FOR_THE_YEAR_EXCEEDS_THE_LIMIT_OF_7000 = "Total amount for the year exceeds the limit of 7000";

	private static final String START_DATE_AND_END_DATE_MUST_BE_WITHIN_THE_CURRENT_YEAR = "Start Date and End Date must be within the current year";

	 private static final String INSIDE_CLASS_LOG = "Inside @Class EducationalBenefitServiceImpl @Method";
	 private static final String ATTACHMENT = "attachment";
	 private static final String EDUCATIONAL_BENEFIT = "EducationalBenefit";
	
	 @Value("${ROOT_DIR_HRMS_PAYROLL_FILE}")
		private String rootDirBucketName;
	
	public EducationalBenefitServiceImpl(GenericRepository<EducationalBenefit> repository) {
		super(repository, EducationalBenefit.class);
	}
	
	
	@Autowired
	private HrmsSystemConfigService hrmsSystemConfigService;
	
	@Autowired
	private StorageRest storageRest;
	
	  @Autowired
	  private EmployeeNationalIdentificationRepository employeeNationalIdentificationRepository;
	
    	
	@Autowired
	private EducationalBenefitRepository educationalBenefitRepository;
	
	@Autowired
	private CommonUtils commonUtils;
	
	
	private static final Logger log = LogManager.getLogger(EducationalBenefitServiceImpl.class);

	@Override
	public EducationalBenefit create(EducationalBenefit educationalBenefit) {
		log.info("inside @class EducationalBenefitServiceImpl @method create");

		try {
		Integer employeeId = educationalBenefit.getEmployee().getId();
        Double amountPaid = educationalBenefit.getAmount();
        
        log.debug("inside @class EducationalBenefitServiceImpl @method create employeeId :{} ,amountPaid:{}",employeeId,amountPaid);

        if (amountPaid > 7000) {
        	log.error("Amount for the year exceeds the limit of 7000");
            throw new BusinessException("Amount Paid Cannot Be Greater Than 4200");
        }
        
        java.time.LocalDate startDate = educationalBenefit.getStartDate().toLocalDate();
        java.time.LocalDate endDate = educationalBenefit.getEndDate().toLocalDate();
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();

        if (startDate.getYear() != currentYear || endDate.getYear() != currentYear) {
        	log.error(START_DATE_AND_END_DATE_MUST_BE_WITHIN_THE_CURRENT_YEAR);
        	throw new BusinessException(START_DATE_AND_END_DATE_MUST_BE_WITHIN_THE_CURRENT_YEAR);
        }
        log.debug("inside @class EducationalBenefitServiceImpl @method create customerId: {}", commonUtils.getCustomerId());
        Double totalAmountPaidForYear = educationalBenefitRepository.getTotalAmountPaidForYearByEmployeeInApproval(employeeId, commonUtils.getCustomerId());
        
        log.debug("inside @class EducationalBenefitServiceImpl @method create is totalAmountPaidForYear {}", totalAmountPaidForYear);
        
        if (totalAmountPaidForYear == null) {
            totalAmountPaidForYear = 0.0;
        }

        if ((totalAmountPaidForYear + amountPaid) > 7000) {
        	log.error(TOTAL_AMOUNT_FOR_THE_YEAR_EXCEEDS_THE_LIMIT_OF_7000);
            throw new BusinessException(TOTAL_AMOUNT_FOR_THE_YEAR_EXCEEDS_THE_LIMIT_OF_7000);
        }

        return educationalBenefitRepository.save(educationalBenefit);
    } catch (BusinessException ex) {
        throw new BusinessException("Error in creating EducationalBenefit", ex.getMessage());
    }
    
	}
	
	@Override
	public EducationalBenefit update(EducationalBenefit educationalBenefit) {
		log.info("inside @class EducationalBenefitServiceImpl @method update");

	    try {
	        Integer employeeId = educationalBenefit.getEmployee().getId();
	        Double amountPaid = educationalBenefit.getAmount();
	        
	        log.debug("inside @class EducationalBenefitServiceImpl @method update employeeId :{} ,amountPaid:{}",employeeId,amountPaid);
    
	        java.time.LocalDate startDate = educationalBenefit.getStartDate().toLocalDate();
	        java.time.LocalDate endDate = educationalBenefit.getEndDate().toLocalDate();
	        LocalDate now = LocalDate.now();
	        int currentYear = now.getYear();

	        if (startDate.getYear() != currentYear || endDate.getYear() != currentYear) {
	        	log.error(START_DATE_AND_END_DATE_MUST_BE_WITHIN_THE_CURRENT_YEAR);
	        	throw new BusinessException(START_DATE_AND_END_DATE_MUST_BE_WITHIN_THE_CURRENT_YEAR);
	        }
	        log.debug("inside @class EducationalBenefitServiceImpl update customerId is : {}", commonUtils.getCustomerId());
	        Double totalAmountPaidForYear = educationalBenefitRepository.getTotalAmountPaidForYearByEmployee(employeeId, commonUtils.getCustomerId());
	        
	        log.debug("inside @class EducationalBenefitServiceImpl @method create is totalAmountPaidForYear {}", totalAmountPaidForYear);
	        
	        if (totalAmountPaidForYear == null) {
	            totalAmountPaidForYear = 0.0;
	        }

	        if ((totalAmountPaidForYear + amountPaid) > 7000) {
	        	log.error(TOTAL_AMOUNT_FOR_THE_YEAR_EXCEEDS_THE_LIMIT_OF_7000);
	            throw new BusinessException(TOTAL_AMOUNT_FOR_THE_YEAR_EXCEEDS_THE_LIMIT_OF_7000);
	        }

	        return educationalBenefitRepository.save(educationalBenefit);
	    } catch (BusinessException ex) {
	        throw new BusinessException("Error in creating educationalBenefit", ex.getMessage());
	    }
	}
	
	@Override
	public void softDelete(int id) {

		EducationalBenefit educationalBenefit = super.findById(id);

		if (educationalBenefit != null) {

			EducationalBenefit educationalBenefit1 = educationalBenefit;
			educationalBenefitRepository.save(educationalBenefit1);

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
		log.info("Inside @class EducationBenefitService @method getIdAndAmountByEmployeeId ");
		List<EducationalBenefit> educationalBenefitList = educationalBenefitRepository.findByEmployeeIdAndWorkflowStage(employeeId,PRConstant.APPROVED_SM);
		HashMap<Integer, Double> educationalBenefitHashMap = new HashMap<>();
	   try{
		for(EducationalBenefit educationalBenefit : educationalBenefitList)
		{
			Integer id = educationalBenefit.getId();
			Double amount = educationalBenefit.getAmount();
			educationalBenefitHashMap.put(id, amount);
		}		
		return educationalBenefitHashMap;

	   }
	   catch(Exception e)
	   {
        log.error("Error Inside @class EducationBenefitService @method getIdAndAmountByEmployeeId :{} :{} ",e.getMessage(),Utils.getStackTrace(e)); 
	   throw new BusinessException();  
	}	
	}
	public Double getSumOfAmount(Map<Integer,Double> educationalBenefitMap)
	{
		log.info("Inside @class EducationBenefitService @method getSumOfAmount ");
		log.debug("Inside @class EducationBenefitService @method getSumOfAmount Map:{}",educationalBenefitMap);
		Double sum = 0.0;
		for (Map.Entry<Integer, Double> entry : educationalBenefitMap.entrySet()) {
		    try
		    {
		    	sum += entry.getValue();
		    }
		    catch(Exception e)
		    {
		    	log.error("Error inside @class EducationBenefitService @method getSumOfAmount :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
		    }
		}
		log.debug(" Inside @class EducationBenefitService @method getSumOfAmount Sum is :{}",sum);
        return sum;
	}
	
	public Boolean setWorkFlowStageForBulk(List<Integer> listOfKeys , String workflowStage)
	{
		log.info("Inside @class EducationBenefitService @method setWorkFlowStageForBulk");
		try
		{
			for(Integer id : listOfKeys)
			{
				EducationalBenefit educationalBenefitOptional = super.findById(id);
				 if(educationalBenefitOptional != null)
				{
					EducationalBenefit educationalBenefit = educationalBenefitOptional;
					educationalBenefit.setWorkflowStage(workflowStage);
					educationalBenefitRepository.save(educationalBenefit);
					log.debug("Inside @class EducationBenefitService @method setWorkFlowStageForBulk Status changed to :{}",workflowStage);		
				}			 
			}
			return true;
		}
		catch(Exception e)
		{
			log.error("Error Inside @class EducationBenefitService @method setWorkFlowStageForBulk :{} :{} ",e.getMessage(),Utils.getStackTrace(e)); 
			throw new BusinessException();  
		}
	}
	
	public ResponseEntity<byte[]> createWpsTxtFileForEducationalBenefit(String educationBenefitProcessInstanceId)
	{
		try {
			log.info("Inside @class EducationalBenefitServiceImpl @method createWpsTxtFile");
			if(educationBenefitProcessInstanceId!=null) {
				Optional <EducationalBenefit> optinalEducationalBenefit = educationalBenefitRepository.findByProcessInstanceId(educationBenefitProcessInstanceId);
		        if(optinalEducationalBenefit.isPresent())
		        {
		        	EducationalBenefit educationalBenefit =  optinalEducationalBenefit.get(); 
		        	 	Employee employee = educationalBenefit.getEmployee();
			        	List<String[]> data = fetchData(employee,educationalBenefit);
			        	ByteArrayOutputStream out = generateTxt(data);
			    		byte[] txtBytes = out.toByteArray();
			    		String fileReference = getSystemTimeWithTimeStamp();
			    		String fileName = String.valueOf(educationalBenefit.getId())+"Wps" + fileReference+ ".txt";
			    		String filePath = "hrmswps/"+fileName;
			    		
			    		
			    		log.debug("Value of FileName :{}  ,fileRefernce :{} file path:{}",fileName,fileReference,filePath);
			    		
			    		log.info("Inside Rundetails createWpsTxtFile rootDirBucketName :{}" , rootDirBucketName);
			    		InputStream inputStream = new ByteArrayInputStream(txtBytes);
			    		uploadFileInStorage(inputStream, fileName, filePath,rootDirBucketName);
			    		HttpHeaders headers = new HttpHeaders();
			            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			            headers.setContentDispositionFormData(ATTACHMENT, fileName);
			            //String fileDir = rootDirBucketName + filePath;
			            log.debug("The Path of File Dir to store is :{} ",filePath);
			            educationalBenefit.setOutputString(filePath);
			            educationalBenefitRepository.save(educationalBenefit);
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
		  
		  
	List<String[]> fetchData(Employee employee,EducationalBenefit educationalBenefit)
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
			  log.debug("Object of educationalBenefit :{} ",educationalBenefit);
			  Double amount = educationalBenefit.getAmount();
		         String amountString = String.valueOf(amount);
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
                    String alw = "ALW";
			      String employeeReferenceId = employee.getEmployeeId();
			      String bankId = accountDetail.getBankId();
			      
			      String nationalIdentification = "";
			      log.debug(" Inside @EBSI fetch  customerId is : {}", commonUtils.getCustomerId());
			      EmployeeNationalIdentification employeeNationalIdentification =  employeeNationalIdentificationRepository.findNationalIdentificationNumberByEmployeeId(employee.getId(), commonUtils.getCustomerId());
			        if(employeeNationalIdentification!=null){
			          nationalIdentification = employeeNationalIdentification.getIdentificationNumber();
			        }
			      
		        	 StringBuilder rowBuilder = new StringBuilder();
		        	 rowBuilder.append(secpty).append(",");
		            rowBuilder.append(accountDetail.getIban()).append(",");
 		            rowBuilder.append(accountDetail.getBeneficiaryName()).append(",");
 		           rowBuilder.append(employee.getId()).append(",");
 		            rowBuilder.append(accountDetail.getBankId()).append(",").append(",").append(",");

 		           rowBuilder.append(amountString);
 		           
 		          rowBuilder.append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(n).append(",").append(n).append(",").append(",").append(",").append(",").append(",").append(",");
 		           rowBuilder.append(sach).append(",").append(accountDetail.getBeneficiaryId()).append(","); 
 		            rowBuilder.append(one).append(",").append(one).append(",").append(one).append(",").append(one).append(",");
		           rowBuilder.append(paymentNarrationCompany).append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",");
   	            rowBuilder.append(alw);
		            log.info("After add the data from object");
		            data.add(new String[] {ifh, ifile, csv, connectId, customerId, fileReference, currentDate, currentTime, p, one,"3"});
		            data.add(new String[] {bathdr, achcr, one,"", "", "", paymentPurposeCodeO,paymentNarrationCompany, "", first,changedDate,debitAccountNumber ,sar,amountString ,"", "", "", "", "", "", pcmHsbcnetTest ,molEstablisedId, employerId, "", "", "",batchReference});
		            data.add(rowBuilder.toString().split(","));
		        }

			
			
			return data;

	  }
	  catch(Exception e)
	  {
    	  log.error("Error Inside @class EducationalBenefitServiceImpl @method fetchData :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
    	  throw new BusinessException();  
	  }
	}	
	public ResponseEntity<byte[]> downloadWpsFile(String educationBenefitProcessInstanceId)  {
 
		log.info("Inside @class EducationalBenefitServiceImpl @method downloadWpsFile");
		if(educationBenefitProcessInstanceId!=null) {
			Optional <EducationalBenefit> optinalEducationalBenefit = educationalBenefitRepository.findByProcessInstanceId(educationBenefitProcessInstanceId);
			
			if(optinalEducationalBenefit.isPresent())
	        {
	        	EducationalBenefit educationalBenefit =  optinalEducationalBenefit.get(); 

  String filePath = educationalBenefit.getOutputString();
	    
	    		
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
	
	
	private String getSystemTimeWithTimeStamp() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	    return now.format(formatter);
	}

	@Override
	public EducationalBenefit updateEducationalBenefitWorkflowStage(EducationalBenefitDto educationalBenefitDto) {
		log.debug("Inside method updateEducationalBenefitWorkflowStage Id : {}", educationalBenefitDto.getEducationalBenefitId());
		try {
			if (educationalBenefitDto.getEducationalBenefitId() != null) {
				EducationalBenefit optionalEducationalBenefit = super.findById(educationalBenefitDto.getEducationalBenefitId());
				if (optionalEducationalBenefit != null) {
					EducationalBenefit educationalBenefit = optionalEducationalBenefit;
					educationalBenefit.setWorkflowStage(educationalBenefitDto.getWorkflowStage());
					return educationalBenefitRepository.save(educationalBenefit);
				} else {
					throw new BusinessException("EducationalBenefit with ID " + educationalBenefitDto.getEducationalBenefitId() + " not found");
				}
			}
		} catch (Exception e) {
			throw new BusinessException("error while updating EducationalBenefit work flow stage", e.getMessage());
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
	public ResponseEntity<byte[]> createWpsTxtFileForAllEducationalBenefit()
	{
		log.info("inside @class EducationBenefitServiceImpl @method createWpsTxtFileForAllEducationalBenefit ");
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
		        log.debug("inside @class EducationalBenefitServiceImpl createWpsTxtFileForAllEducationalBenefit customerId is : {}", commonUtils.getCustomerId());
				List<EducationalBenefit> educationalBenefits = educationalBenefitRepository.getEducationBenefitByWorkflowStageAndDate(PRConstant.APPROVED_SM, sixDaysAgo, nextDate , commonUtils.getCustomerId());	
	               log.debug("size of educational Benefits :{} ",educationalBenefits.size());
				 List<String[]> data = getData(educationalBenefits);
					 ByteArrayOutputStream out = generateTxt(data);
		    		byte[] txtBytes = out.toByteArray();
		    		String fileReference = getSystemTimeWithTimeStamp();
		    		String fileName = "EB"+"Wps" + fileReference+ ".txt";
		    		String filePath = "hrmswps/"+fileName;
		    		
		    		
		    		log.debug("Value of FileName :{}  ,fileRefernce :{} file path:{}",fileName,fileReference,filePath);
		    		
		    		log.info("Inside createWpsTxtFileForAllEducationalBenefit rootDirBucketName :{}" , rootDirBucketName);
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
			log.error("Error inside @class EducationBenefitServiceImpl @method createWpsTxtFileForAllEducationalBenefit :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
	  throw new BusinessException("Something wrong in Creating wps for all Education Benefit ");
		}
 
	}

    
	List<String[]> getData(List<EducationalBenefit> educationalBenefits)
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
		  	String fileReference = "ILWPSOPOPVATEST"+getSystemTimeWithTimeStamp();
			String batchReference = "IWPSOPOP"+getSystemTimeWithTimeStampNew();
		    String currentDate = getCurrentDate();
			String currentTime = getCurrentTime();
			String valueDate = getDateAfterDays(1);
 
			
			 log.info(INSIDE_CLASS_LOG+"getData after set date and time");
		 
			if(educationalBenefits !=null && !educationalBenefits.isEmpty()) {
			Integer count =  educationalBenefits.size();
			log.debug(INSIDE_CLASS_LOG+"fetch count :{}",count);	
			String totalCount = count.toString();
			log.debug(INSIDE_CLASS_LOG+"fetch totalCount :{}",totalCount);	
			Integer totalRecordCount = Integer.parseInt(totalCount)+2;
			String totalRecordCountString = totalRecordCount.toString();
			String netWorth=getTotalAmount(educationalBenefits);
			 
			
			data = new ArrayList<>();
			data.add(new String[] {ifh, ifile, csv, connectId, customerId, fileReference, currentDate, currentTime, p, one, totalRecordCountString});
			data.add(new String[] {bathdr, achcr, totalCount, "", "", "", paymentPurposeCodeS, paymentNarrationCompany, "", first, valueDate, debitAccountNumber, sar, netWorth, "", "", "", "", "", "",  molEstablisedId, employerId, "", "", "", batchReference});
			iterateEducationalBenefit(secpty, n, sach, educationalBenefits, data,alw,paymentNarrationCompany);
			
			log.info(INSIDE_CLASS_LOG+"fetchData Educational Benefit Successfully Iterate");
			}
			return data;

	  }
	  catch(Exception e)
	  {
    	  log.error("Error Inside @class EducationalBenefitServiceImpl @method getData :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
    	  throw new BusinessException();  
	  }
	}	
	
	
	private void iterateEducationalBenefit(String secpty, String n, String sach, List<EducationalBenefit> educationalBenefits,
			List<String[]> data,String alw ,String paymentNarrationCompany) {
		String one = "1";
		log.info(INSIDE_CLASS_LOG+" iterateOnRunDetails");
		for (EducationalBenefit educationalBenefit : educationalBenefits) {
			log.debug(INSIDE_CLASS_LOG+"iterateOnRunDetails runDetail :{}",educationalBenefit);
	        Integer employeeId = educationalBenefit.getEmployee().getId();
	        String employeeReferenceId = educationalBenefit.getEmployee().getEmployeeId();
	        String nationalIdentification = "";
	        log.debug(" Inside @iterateEducationalBenefit  customerId is : {}", commonUtils.getCustomerId());
	        EmployeeNationalIdentification employeeNationalIdentification =  employeeNationalIdentificationRepository.findNationalIdentificationNumberByEmployeeId(employeeId, commonUtils.getCustomerId());
	        if(employeeNationalIdentification!=null){
	          nationalIdentification = employeeNationalIdentification.getIdentificationNumber();
	        }
	    	AccountDetailsService accountDetailsService = ApplicationContextProvider.getApplicationContext().getBean(AccountDetailsService.class);
	        List<AccountDetails> accountDetails = accountDetailsService.findAccountDetailsByEmployeeId(employeeId);

	        for (AccountDetails accountDetail : accountDetails) {
	        	log.debug(INSIDE_CLASS_LOG+"iterateEducationalBenefit accountDetail :{}",accountDetail);
	            StringBuilder rowBuilder = new StringBuilder();
 
	        	 rowBuilder.append(secpty).append(",");
		            rowBuilder.append(accountDetail.getIban()).append(",");
		            rowBuilder.append(accountDetail.getBeneficiaryName()).append(",");
		           rowBuilder.append(employeeId).append(",");
		            rowBuilder.append(accountDetail.getBankId()).append(",").append(",").append(",");

		           rowBuilder.append(educationalBenefit.getAmount());
		           
		          rowBuilder.append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(n).append(",").append(n).append(",").append(",").append(",").append(",").append(",").append(",");
		           rowBuilder.append(sach).append(",").append(accountDetail.getBeneficiaryId()).append(","); 
		            rowBuilder.append(one).append(",").append(one).append(",").append(one).append(",").append(one).append(",");
		           rowBuilder.append(paymentNarrationCompany).append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",").append(",");
	            rowBuilder.append(alw);
	            data.add(rowBuilder.toString().split(","));
		            log.debug(INSIDE_CLASS_LOG+" iterateEducationalBenefit data :{}",data);
	            
	            
	        }
	    }
	}
	
	void savePath(String path,LocalDate date,Integer year,Integer weekNumber)
	{
	  log.info("Inside @class EducationalBenefitServiceImpl @method savePath ");
	  try
	  {
		  educationalBenefitRepository.setDataInFileData(EDUCATIONAL_BENEFIT, date, path,year,weekNumber);
		  log.debug("Inside @class EducationalBenefitServiceImpl @method savePath date :{}  path:{} ",date,path);
	  }
	  catch(Exception e)
	  {
		  log.error("Error inside @class EducationalBenefitServiceImpl @method savePath");
		  throw new BusinessException();
	  }
	}

	@Override
	public ResponseEntity<byte[]> downloadCommonWpsFile(Integer weekNum)  {
		 
		log.info("Inside @class EducationalBenefitServiceImpl @method downloadCommonWpsFile");
		  LocalDate currDate = LocalDate.now();
		  Integer year = currDate.getYear();
		  log.debug("inside @class EducationalBenefitServiceImpl downloadCommonWpsFile customerId is : {}", commonUtils.getCustomerId());
		   String filePath =  educationalBenefitRepository.getFilePath(EDUCATIONAL_BENEFIT,weekNum,year, commonUtils.getCustomerId());
	    
	    		
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


	private String getSystemTimeWithTimeStampNew() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
	    return now.format(formatter);
	}
   
	    private String getTotalAmount(List<EducationalBenefit> educationalBenefits)
	   {
			log.debug("Inside Method getTotalAmount educationalBenefits size :{} ",educationalBenefits.size());
		   Double amount = 0.0 ;
		   for(EducationalBenefit educationalBenefit : educationalBenefits)
		   {
			   amount = amount+educationalBenefit.getAmount();
		   }
		   log.debug("Inside Method getTotalAmount educationalBenefits amount :{} ",amount);
		   return String.valueOf(amount);
		   
	   }
	    
	    @Override
	    public List<Object[]> getAmountForEducationalBenefit(LocalDate startDate , LocalDate endDate)
	    {
	    	log.info("Inside Method getAmountForEducationalBenefit ");
	    	try
	    	{
	    		log.debug("Inside Method getAmountForEducationalBenefit startDate :{}  endDate :{} ",startDate,endDate);
	    		log.debug("inside @class EducationalBenefitServiceImpl getAmountForEducationalBenefit customerId is : {}", commonUtils.getCustomerId());
	    		List<Object[]> listSum = educationalBenefitRepository.getEducationalBenefitByDates(startDate, endDate, commonUtils.getCustomerId());
	    		log.debug(" The list 0f Educational Benefit is :{}",listSum);
	    		return listSum;
	    	}
	    	catch(Exception e)
	    	{
	    		log.error("Error Inside @class EducationalBenefitServiceImpl @method getAmountForEducationalBenefit :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
	  	      throw new BusinessException("Something went wrong inside getAmountForEducationalBenefit " );	
	    	}
	    }
	 
	    @Override
	
	 public  List<Object[]> getAmountForAccuralEducationalBenefit(LocalDate startDate , LocalDate endDate)
	    {
	    	log.info("Inside Method getAmountForEducationalBenefit ");
	    	try
	    	{
	    		log.debug("Inside Method getAmountForEducationalBenefit startDate :{}  endDate :{} ",startDate,endDate);
	    		log.debug("inside @class EducationalBenefitServiceImpl getAmountForAccuralEducationalBenefit customerId is : {}", commonUtils.getCustomerId());
	    		List<Object[]> listSum = educationalBenefitRepository.getEducationalBenefitByDates(startDate, endDate, commonUtils.getCustomerId());
	    		log.debug(" The list 0f Educational Benefit is :{}",listSum);
	    		return listSum;
	    	}
	    	catch(Exception e)
	    	{
	    		log.error("Error Inside @class EducationalBenefitServiceImpl @method getAmountForEducationalBenefit :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
	  	      throw new BusinessException("Something went wrong inside getAmountForEducationalBenefit " );	
	    	}
	    }

}
