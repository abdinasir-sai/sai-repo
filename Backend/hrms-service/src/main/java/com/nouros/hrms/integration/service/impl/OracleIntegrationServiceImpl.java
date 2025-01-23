
package com.nouros.hrms.integration.service.impl;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.apache.commons.collections.iterators.EntrySetMapIterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Date;
import com.enttribe.commons.configuration.ConfigUtils;
import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nouros.hrms.integration.service.OracleIntegrationService;
import com.nouros.hrms.model.BusinessExpense;
import com.nouros.hrms.model.BusinessTrip;
import com.nouros.hrms.model.ChildEducationBenefit;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.HrBenefits;
import com.nouros.hrms.model.IntegrationLog;
import com.nouros.hrms.model.OtherExpenseBankRequest;
import com.nouros.hrms.model.IntegrationLog.Status;
import com.nouros.hrms.service.BusinessExpenseService;
import com.nouros.hrms.service.BusinessTripService;
import com.nouros.hrms.service.CheckService;
import com.nouros.hrms.service.ChildEducationBenefitService;
import com.nouros.hrms.service.EducationalBenefitService;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.HealthClubBenefitService;
import com.nouros.hrms.service.HrBenefitsService;
import com.nouros.hrms.service.IntegrationLogService;
import com.nouros.hrms.service.NewHireBenefitService;
import com.nouros.hrms.service.OtherExpenseBankRequestService;
import com.nouros.hrms.service.impl.IntegrationLogServiceImpl;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.HttpResponseWrapper;
import com.nouros.payrollmanagement.model.EmployeeMonthlySalary;
import com.nouros.payrollmanagement.model.EmployeeSalaryStructure;
import com.nouros.payrollmanagement.model.PayrollRun;
import com.nouros.payrollmanagement.repository.MasterDataRepository;
import com.nouros.payrollmanagement.service.EmployeeMonthlySalaryService;
import com.nouros.payrollmanagement.service.EmployeeSalaryStructureService;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;
import com.nouros.payrollmanagement.service.PayrollRunService;
import com.nouros.payrollmanagement.service.impl.EmployeeMonthlySalaryServiceImpl;
import com.nouros.payrollmanagement.utils.HttpUtil;
import com.nouros.payrollmanagement.utils.PRConstant;
import com.nouros.payrollmanagement.wrapper.Data;
import com.nouros.payrollmanagement.wrapper.GLEntries;
import com.nouros.payrollmanagement.wrapper.GLMetaData;
import com.nouros.payrollmanagement.wrapper.ParentWrapper;
import com.nouros.payrollmanagement.wrapper.RequestObject;


@Service
public class OracleIntegrationServiceImpl implements OracleIntegrationService {

	@Autowired
	  private CommonUtils commonUtils;
	
	private static final String THE_GL_ENTRIES_ADDED = "The GLEntries added ";
	private static final String THE_VALUE_IS = "The value is :{}";
	private static final String ACCESS_TOKEN_URL = "ACCESS_TOKEN_URL";
	private static final String AD_LADGER_URL = "AD_LADGER_URL";
	private static final String ACCESS_TOKEN_SCOPE_VALUE = "ACCESS_TOKEN_SCOPE_VALUE";
	private static final String ACCESS_TOKEN_APP_KEY = "ACCESS_TOKEN_APP_KEY";
	private static final String ACCESS_TOKEN_AUTHORIZATION = "ACCESS_TOKEN_AUTHORIZATION";
	private static final String LEDGER_ID = "300000002406322";
	private static final String ZERO4 = "0000";
	private static final String ZERO3 = "000";
	private static final String SAR = "SAR";
	private static final String CLASS_NAME = "OracleIntegrationServiceImpl";

	@Autowired
	public HrmsSystemConfigService hrmsSystemConfigService;

	private static final Logger log = LogManager.getLogger(OracleIntegrationServiceImpl.class);


	public String OracleLedgerJson(Integer payrollId) {
		log.debug(" Inside class :{} @method OracleLedgerJson  customerId is : {}",CLASS_NAME ,commonUtils.getCustomerId());
		PayrollRunService payRollRunService = ApplicationContextProvider.getApplicationContext()
				.getBean(PayrollRunService.class);
		CheckService checkService = ApplicationContextProvider.getApplicationContext().getBean(CheckService.class);
		PayrollRun payRollRun = payRollRunService.getPayrollById(payrollId);
		try {
			Map<String, String> hrmsSystemConfigMap = hrmsSystemConfigService.getHrmsKeyValue();
			String listEmployee = hrmsSystemConfigMap.get(PRConstant.ON_BOARD_MEMBER_TYPE_LIST);
			String[] parts = listEmployee.split(",");
			log.debug("Inside @class OracleIntegrationServiceImpl @method getPayRollJson parts :{} ", parts);
			MasterDataRepository masterDataRepository = ApplicationContextProvider.getApplicationContext()
					.getBean(MasterDataRepository.class);
			GLMetaData glMetaData = getGlMetaDataObject();
			log.info("Inside @class OracleIntegrationServiceImpl @method getPayRollJson value of GLMetaData :{} ",
					glMetaData);
			List<GLEntries> gLEntriesSet = new ArrayList<>();
			String workFlowStage = hrmsSystemConfigMap.get(PRConstant.FIRST_WORKFLOW_STAGE_FOR_GL);
			log.debug("Value for Workflow Stage for first WorkflowStage :{} firstWorflowStage:{} ",workFlowStage,PRConstant.FIRST_WORKFLOW_STAGE_FOR_GL);
			if(workFlowStage==null)
			{
				workFlowStage=PRConstant.APPROVED_BY_FINANCE_CONTROLLER;
			}
			if (payRollRun.getWorkflowStage().equalsIgnoreCase(workFlowStage) && workFlowStage!=null) {
//				addLeaveDataInGl(payRollRun,parts,masterDataRepository,glMetaData,gLEntriesSet);
				addPayrollInGl(payrollId, parts, masterDataRepository, glMetaData, gLEntriesSet);
			}
//			HrBenefitsService hrBenefitsService = ApplicationContextProvider.getApplicationContext()
//					.getBean(HrBenefitsService.class);
//			OtherExpenseBankRequestService otherExpenseBankRequestService = ApplicationContextProvider
//					.getApplicationContext().getBean(OtherExpenseBankRequestService.class);
//			LocalDate localDate = LocalDate.now();
//			String benefitsName = hrmsSystemConfigMap.get(PRConstant.BENEFITS_NAME);
//			String[] benefits = benefitsName.split(",");
//			List<OtherExpenseBankRequest> listOfOtherExpenseBankRequest = otherExpenseBankRequestService.getListOfOtherExpenseBankRequestByStage(PRConstant.APPROVED_BY_MANAGER, localDate,benefits); 
//			  
//				 List<Integer> listOfHrBenefitsId = getIdListofHrBenefits(hrBenefitsService,listOfOtherExpenseBankRequest);
//			if (listOfHrBenefitsId != null) {
//				iterateForBenefitExpense(masterDataRepository, gLEntriesSet, hrBenefitsService, listOfHrBenefitsId);
//				iterateBenefitsAccurals(masterDataRepository, gLEntriesSet, hrBenefitsService, listOfHrBenefitsId);
//			  
//			 }
//			log.info("Hr Benefits added in GL ");
//			BusinessExpenseService businessExpenseService = ApplicationContextProvider.getApplicationContext().getBean(BusinessExpenseService.class);
//			List<OtherExpenseBankRequest> listOfBusinessExpenseWps = otherExpenseBankRequestService.getListOfOtherExpenseBankRequestByStageForEntity(PRConstant.APPROVED_BY_MANAGER, localDate,PRConstant.BUSINESS_EXPENSE_SM); 
//			if(listOfBusinessExpenseWps!=null)
//			{
//				List<Integer> listOfBusinessExpenseId  = getIdListofBusinessExpense( businessExpenseService ,listOfBusinessExpenseWps);
//			    iterateForBusinessExpense(masterDataRepository, gLEntriesSet,businessExpenseService,listOfBusinessExpenseId);
//			 	 iterateForBusinessExpenseAccural(masterDataRepository,gLEntriesSet,businessExpenseService,listOfBusinessExpenseId);
//			}
//			BusinessTripService businessTripService = ApplicationContextProvider.getApplicationContext().getBean(BusinessTripService.class);
//			List<OtherExpenseBankRequest> listOfBusinessTripWps = otherExpenseBankRequestService.getListOfOtherExpenseBankRequestByStageForEntity(PRConstant.APPROVED_BY_MANAGER, localDate, PRConstant.BUSINESS_TRIP_SM) ;
//			if(listOfBusinessTripWps!=null)
//			{
//				List<Integer> businessTripsId = getIdListOfBusinessTrip(businessTripService ,listOfBusinessTripWps);
//			  iterateExternalBusinessTripExpense(masterDataRepository, gLEntriesSet, businessTripService, businessTripsId, benefitsName);
//			iterateExternalBusinessTripAccural(masterDataRepository, gLEntriesSet, businessTripService, businessTripsId, benefitsName);
//			log.info("External Type added in GL Now Internal ");
//			iterateInternalBusinessTripExpense(masterDataRepository, gLEntriesSet, businessTripService, businessTripsId, benefitsName);
//			iterateInternalBusinessTripAccural(masterDataRepository, gLEntriesSet, businessTripService, businessTripsId, benefitsName);
//			log.info("Internal Type added in GL Now  ");
//			}
//			
//			if (bodCheck && payRollRun.getWorkflowStage().equalsIgnoreCase(PRConstant.BOARD_MEMBER_BANK_APPROVAL)) {
//				log.info("Going to push data for BOD ");
//				iterateForBodExpense(masterDataRepository, gLEntriesSet, payRollRun.getId());
//				iterateForBodAccural(masterDataRepository, gLEntriesSet, payRollRun.getId());
//			}
			if (gLEntriesSet.size() > 0) {
				glMetaData.setGl_entries(gLEntriesSet);
				String parentWrapperValue = getParentWrapper(glMetaData);
				log.debug("The value of RequestObject is  :{}", parentWrapperValue);
				//hrBenefitsService.updateGLStatus(PRConstant.PUSHED, PRConstant.NOT_PUSHED, PRConstant.APPROVED_SM);
				return parentWrapperValue;

				//return updateDataOnOracleLedger(parentWrapperValue, payrollId);
			}
			return null;
		} catch (Exception e) {
			log.error("Error inside @class OracleIntegrationServiceImpl @method getPayRollJson :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException("Error in creating json");
		}
	}

	
	private void addPayrollInGl(Integer payrollId, String[] parts, MasterDataRepository masterDataRepository,
			GLMetaData glMetaData, List<GLEntries> gLEntriesSet) {
		try {
			log.info("Inside @class OracleIntegrationServiceImpl @method getPayRollJson value of GLMetaData :{} ",
					glMetaData);
			EmployeeMonthlySalaryService employeeMonthlySalaryService = ApplicationContextProvider
					.getApplicationContext().getBean(EmployeeMonthlySalaryService.class);
			List<Object[]> listOfObject = employeeMonthlySalaryService.getSumOfValue(payrollId, parts);
			log.debug("The size of Object list is :{}", listOfObject.size());
			iterateForListOfObjectExpense(masterDataRepository, listOfObject, gLEntriesSet);
			List<Object[]> listOfObjectAccural = employeeMonthlySalaryService.getSumOfValueOfAccural(payrollId, parts);
			log.debug("The size of Object list is of Accural :{}", listOfObjectAccural.size());
			iterateForListOfObjectAccural(masterDataRepository, gLEntriesSet, listOfObjectAccural);
		} catch (Exception e) {
			log.error("Error inside @class OracleIntegrationServiceImpl @method addPayrollInGl :{}  :{}",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}

	}

	private Double iterateForListOfObjectAccural(MasterDataRepository masterDataRepository, List<GLEntries> gLEntriesSet,
			List<Object[]> listOfObjectAccural) {
		try {
			Double totalValue =0.0;
			for (Object[] object : listOfObjectAccural) {
				Integer code;
				List<String> list = getCompenentList();
				log.debug("the size of acc list :{} ", list.size());
				for (int i = 1; i < list.size(); i++) {
					if (object[i - 1] != null) {
						log.debug("the value of acc :{} ", list.get(i));
						log.debug("the value of object :{} ", object[i - 1]);
						GLEntries gl = getfixedGl();
						code = getGlEntryCodeOnParameters(masterDataRepository, list.get(i), PRConstant.COMPONENT,
								PRConstant.ACCURALS);
						if (object[i - 1] != null) {
							Double value = (Double) object[i - 1];
							log.debug(THE_VALUE_IS, value);
							if (value != 0.0) {
								gl = getGlEntryforAccruals(code, gl, value);
								gLEntriesSet.add(gl);
								totalValue = totalValue+value;
							}
						}
					}
				}
				log.info("Accural Value added ");
			}
			return totalValue;
		} catch (Exception e) {
			log.error("Error inside @class OracleIntegrationServiceImpl @method iterateForListOfObjectAccural :{}  :{}",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	private void iterateForBenefitExpense(MasterDataRepository masterDataRepository, List<GLEntries> gLEntriesSet,
			HrBenefitsService hrBenefitsService, List<Integer> listOfHrBenefitsId) {
		try {
			List<String> benefitList = getBenefitsList();
			for (int i = 0; i < benefitList.size(); i++) {
				List<Object[]> listOfBenefits = hrBenefitsService.getForHRBenefitsForExpense(PRConstant.NOT_PUSHED,
						benefitList.get(i), listOfHrBenefitsId);
				log.debug("The Benefit is :{} ", benefitList.get(i));
				for (Object[] object : listOfBenefits) {
					String deptCodeSs = (String) object[0];
					Integer deptCode = Integer.parseInt(deptCodeSs);
					Integer code;
					if (object[1] != null) {
						GLEntries gl = getfixedGl();
						code = getGlEntryCodeOnParameters(masterDataRepository, benefitList.get(i),
								PRConstant.COMPONENT, PRConstant.EXPENSES);
						Double value = (Double) object[1];
						log.debug(THE_VALUE_IS, value);
						if (value != 0.0) {
							gl = getGlEntryforExpense(code, gl, value, deptCode);
							gLEntriesSet.add(gl);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Error inside @class OracleIntegrationServiceImpl @method iterateForBenefitExpense :{}  :{}",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}

	}

	private Map<String , Double> iterateForListOfObjectExpense(MasterDataRepository masterDataRepository, List<Object[]> listOfObject,
			List<GLEntries> gLEntriesSet) {
		Map<String , Double> deptAndValueMap = new HashMap<>();
		try {
			for (Object[] object : listOfObject) {
				String deptCodeSs = (String) object[0];
				Integer deptCode = Integer.parseInt(deptCodeSs);
				Integer code;
				List<String> list = getCompenentList();
				for (int i = 1; i < list.size(); i++) {
					GLEntries gl = getfixedGl();
					log.debug("the value of object :{} ", object[i]);
					code = getGlEntryCodeOnParameters(masterDataRepository, list.get(i), PRConstant.COMPONENT,
							PRConstant.EXPENSES);
					if (object[i] != null) {
						Double value = (Double) object[i];
						log.debug(THE_VALUE_IS, value);
						gLEntriesSet.add(gl);
						if (value != 0.0) {
							gl = getGlEntryforExpense(code, gl, value, deptCode);
							deptAndValueMap.put(deptCodeSs,value);
						}
					}
				}

			}
			return deptAndValueMap;
		} catch (Exception e) {
			log.error("Error inside @class OracleIntegrationServiceImpl @method iterateForListOfObjectExpense :{}  :{}",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	public void iterateBenefitsAccurals(MasterDataRepository masterDataRepository, List<GLEntries> gLEntriesSet,
			HrBenefitsService hrBenefitsService, List<Integer> listOfHrBenefitsId) {
		try {
			List<String> benefitList = getBenefitsList();
			for (int i = 0; i < benefitList.size(); i++) {
				Object[] listOfBenefits = hrBenefitsService.getForHRBenefitsForAccural(PRConstant.NOT_PUSHED,
						benefitList.get(i), listOfHrBenefitsId);
				log.debug("The Benefit is :{} ", benefitList.get(i));
				for (Object object : listOfBenefits) {
					Integer code;
					if (object != null) {
						GLEntries gl = getfixedGl();
						code = getGlEntryCodeOnParameters(masterDataRepository, benefitList.get(i),
								PRConstant.COMPONENT, PRConstant.ACCURALS);
						Double value = (Double) object;
						log.debug(THE_VALUE_IS, value);
						if (value != 0.0) {
							gl = getGlEntryforAccruals(code, gl, value);
							gLEntriesSet.add(gl);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Error inside @class OracleIntegrationServiceImpl @method iterateBenefitsAccurals :{}  :{}",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	private GLMetaData getGlMetaDataObject() {
		log.info("Inside @class OracleIntegrationServiceImpl @method getGlMetaDataObject");
		try {
			Integer value = getBatchName();
			value = value + 1;
			String valueString = String.valueOf(value);
			GLMetaData glMetaData = new GLMetaData();
			glMetaData.setLedger_id(LEDGER_ID);
			glMetaData.setBatch_name(valueString);
			glMetaData.setBatch_description(PRConstant.NOUR_OS_LEDGER_DATA);
			String formattedDate = getCurrDate();
			glMetaData.setAccounting_date(formattedDate);
			glMetaData.setUser_source_name(PRConstant.NOUR_OS);
			glMetaData.setUser_category_name(PRConstant.NOUR_OS_TRANSFER);
			glMetaData.setError_to_suspense_indicator(PRConstant.FALSE);
			glMetaData.setSummary_indicator(PRConstant.FALSE);
			glMetaData.setImport_descriptive_flex("N");
			return glMetaData;
		}
		catch (Exception e) {
			log.error("Error inside @class OracleIntegrationServiceImpl @method getGlMetaDataObject :{}  :{}",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	private GLEntries getfixedGl() {
		try {
			log.info("Inside @class OracleIntegrationServiceImpl @method getfixedGl ");
			LocalDate currentDate = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yy");
			String formattedDate = currentDate.format(formatter);
			GLEntries glEntries = new GLEntries();
			glEntries.setLedger_id(LEDGER_ID);
			glEntries.setPeriod_name(formattedDate);
			String date = getCurrDate();
			glEntries.setJe_accounting_date(date);
			glEntries.setUser_je_source_name(PRConstant.NOUR_OS);
			glEntries.setUser_je_category_name(PRConstant.NOUR_OS_TRANSFER);
			glEntries.setBalance_type("A");
			glEntries.setCompany_code("001");
			String batchName = PRConstant.NOUR_OS_BATCH + " " + dateFormated();
			glEntries.setBatch_name(batchName);
			// "NourOS Journal"
			String jeName = PRConstant.NOUR_OS_JOURNAL + " " + dateFormated();
			glEntries.setJe_name(jeName);
			String dateString = PRConstant.NOUR_OS_JOURNAL_DESC + " " + dateFormated();
			glEntries.setJe_desc(dateString);
			glEntries.setVersion_number("1");
			glEntries.setStatus("New");
			glEntries.setCurrency_date(date);
			glEntries.setCurr_conver_rate("NA");
			glEntries.setUser_curr_conver_type("NA");
			return glEntries;
		} catch (Exception e) {
			log.error("Error inside @class OracleIntegrationServiceImpl @method getfixedGl ");
			throw new BusinessException();
		}
	}

	@Override
	public String getAccessTokenForOracleApi() {
		try {
			log.info("Inside @class OracleIntegrationServiceImpl @method getAccessTokenForOracleApi ");
			String url = ConfigUtils.getString(ACCESS_TOKEN_URL);
			log.debug("URL :{} ", url);
			Map<String, String> headers = new HashMap<>();
			headers.put("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
			headers.put("appKey", ConfigUtils.getString(ACCESS_TOKEN_APP_KEY));
			headers.put("Authorization",
					"Basic Nzk3MTdjYTNhMGI0NDM1OGI4MmI0M2I5YzEzOTkzYWQ6MTllM2M5N2UtNGQxMS00OGExLTgzZWQtYTI5MTZmMzQzNGQ1");
			StringBuilder bodyBuilder = new StringBuilder();
			bodyBuilder.append("grant_type=").append(URLEncoder.encode("client_credentials", StandardCharsets.UTF_8))
					.append("&scope=")
					.append(URLEncoder.encode(ConfigUtils.getString(ACCESS_TOKEN_SCOPE_VALUE), StandardCharsets.UTF_8));
			String bodyString = bodyBuilder.toString();
			HttpResponseWrapper httpResponseWrapper = HttpUtil.sendPostRequest(url, bodyString, headers);
			String response = httpResponseWrapper.getResponse();
			Integer statusCode = httpResponseWrapper.getStatusCode();
			log.debug("Response :{} ", response);
			log.debug("Status code :{}", statusCode);
			return response;
		} catch (Exception e) {
			log.error("Error inside @class OracleIntegrationServiceImpl @method getAccessTokenForOracleApi :{}  :{}",
					e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}

	}

	public String updateDataOnOracleLedger(String ladgerData, Integer payrollId) {
		String response = getAccessTokenForOracleApi();
		JSONObject jsonObject = new JSONObject(response);
		String accessKey = jsonObject.getString("access_token");
		log.debug("The access key is :{} ", accessKey);
		accessKey = "Bearer " + accessKey;
		String url = ConfigUtils.getString(AD_LADGER_URL);
		log.debug("URL :{} ", url);
		String timeStamp = getSystemTimeWithTimeStamp();
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.put("appKey", ConfigUtils.getString(ACCESS_TOKEN_APP_KEY));
		headers.put("channel_id", "adc_nour_os");
		headers.put("source_id", "nour_os");
		headers.put("timestamp", getTime());
		headers.put("data_classification", "confidential");
		headers.put("transaction_type", "dataTransfer");
		headers.put("sub_source_id", "ledgerdata");
		headers.put("request_id", timeStamp);
		headers.put("Authorization", accessKey);
		log.debug("The value of Ledger Data  :{} ", ladgerData);
		HttpResponseWrapper httpResponseWrapper = HttpUtil.sendPostRequest(url, ladgerData, headers);
		String finalResponse = httpResponseWrapper.getResponse();
		Integer statusCode = httpResponseWrapper.getStatusCode();
		log.debug("The value of Final Response is :{} ", finalResponse);
		log.debug("The value of Final StatusCode is :{} ", statusCode);
		JSONObject jsonObjectResponse = new JSONObject(finalResponse);
		JSONArray resultMetaData = jsonObjectResponse.getJSONArray("result_metadata");
		log.debug("The value of resultMetaData is :{} ", resultMetaData);
		JSONObject metadata = resultMetaData.getJSONObject(0);
		log.debug("The value of metadata is :{} ", metadata);
		String processResult = metadata.getString("process_result");
		log.debug("The value of processResult is :{} ", processResult);
		Double processId = Double.parseDouble(processResult);
		log.debug("The value of processId is :{} ", processId);
		saveIntegrationLog(finalResponse, statusCode, url, headers, ladgerData, processId, timeStamp, payrollId);
		log.info("The data in integration_log is saved ");
		OtherExpenseBankRequestService otherExpenseBankRequestService = ApplicationContextProvider
				.getApplicationContext().getBean(OtherExpenseBankRequestService.class);
		return finalResponse;
	}

	public String getTime() {
		log.info("Inside @class OracleIntegrationServiceImpl @method getTime ");
		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		String formattedDate = dateTime.format(formatter);
		log.debug("The get time value is :{} ", formattedDate);
		return formattedDate;
	}

	private String getTimeInMonthYear() {
		log.info("Inside @class OracleIntegrationServiceImpl @method getTimeInMonthYear ");
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMMM");
		String formattedDate = currentDate.format(formatter);
		formattedDate = "ledgerdata" + formattedDate;
		formattedDate = formattedDate.toLowerCase();
		log.debug("the Date is :{}", formattedDate);
		return formattedDate;
	}

	private String getCurrDate() {
		log.info("Inside @class OracleIntegrationServiceImpl @method getCurrDate ");
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedDate = currentDate.format(formatter);
		log.debug("Inside @class OracleIntegrationServiceImpl @method getCurrDate the Date is :{} ", formattedDate);
		return formattedDate;
	}

	private Integer getGlEntryCodeOnParameters(MasterDataRepository masterDataRepository, String name, String type,
			String subType) {
		try {
			log.info("Inside @class OracleIntegrationServiceImpl @method getGlEntryCodeOnParameters ");
			log.debug("Inside @class OracleIntegrationServiceImpl @method getGlEntryCodeOnParameters :{} :{}", name,
					type);
			Integer code;
			code = masterDataRepository.getCodeByNameAndTypeAndSubType(name, type, subType);
			log.debug(
					"inside @class OracleIntegrationServiceImpl @method getGlEntryCodeOnParameters name :{} and code :{} ",
					name, code);
			return code;
		} catch (Exception e) {
			log.error("Error inside @class OracleIntegrationServiceImpl @method getGlEntryCodeOnParameters");
			throw new BusinessException();
		}
	}

	private GLEntries getGlEntryforExpense(Integer code, GLEntries glEntries, Double amount, Integer deptCode) {

		try {
			Integer value = getBatchName();
			value = value + 1;
			String groupid = String.valueOf(value);
			log.info("Inside @class OracleIntegrationServiceImpl @method getGlEntryforExpense ");
			DecimalFormat df = new DecimalFormat("#.##");
			String exAmnt = (String) df.format(amount);
			log.debug("The  Value is in Expense :{} code :{} ", exAmnt, code);
			glEntries.setLocation_code("001001");
			glEntries.setLob_code("0001");
			String deptCodeS = String.valueOf(deptCode);
			glEntries.setGroup_id(groupid);
			glEntries.setCc_code(deptCodeS);
			glEntries.setProject_code(ZERO4);
			String codeC = String.valueOf(code);
			glEntries.setNatural_acc_code(codeC);
			glEntries.setIntercomp_code(ZERO3);
			glEntries.setCustom_code1(ZERO4);
			glEntries.setCustom_code2(ZERO4);
			glEntries.setCurrency_code(SAR);
			glEntries.setEntered_dr_amt(exAmnt);
			glEntries.setAccounted_dr(exAmnt);
			glEntries.setAccounted_cr("");
			glEntries.setEntered_cr_amt("");
			return glEntries;
		} catch (Exception e) {
			log.error("Error Inside OracleIntegrationServiceImpl @method getGlEntry :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}

	}

	private GLEntries getGlEntryforAccruals(Integer code, GLEntries glEntries, Double amount) {
		try {
			Integer value = getBatchName();
			value = value + 1;
			String groupid = String.valueOf(value);
			log.info("Inside @class OracleIntegrationServiceImpl @method getGlEntryforAccruals ");
			DecimalFormat df = new DecimalFormat("#.##");
			String exAmnt = (String) df.format(amount);
			log.debug("The  Value is in accural :{}  code :{} ", exAmnt, code);
			glEntries.setLocation_code("000000");
			glEntries.setLob_code(ZERO4);
			glEntries.setGroup_id(groupid);
			glEntries.setCc_code(ZERO4);
			glEntries.setProject_code(ZERO4);
			String codeC = String.valueOf(code);
			glEntries.setNatural_acc_code(codeC);
			glEntries.setIntercomp_code(ZERO3);
			glEntries.setCustom_code1(ZERO4);
			glEntries.setCustom_code2(ZERO4);
			glEntries.setCurrency_code(SAR);
			glEntries.setEntered_dr_amt("");
			glEntries.setAccounted_dr("");
			glEntries.setAccounted_cr(exAmnt);
			glEntries.setEntered_cr_amt(exAmnt);
			return glEntries;
		} catch (Exception e) {
			log.error("Error Inside OracleIntegrationServiceImpl @method getGl :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}

	}

	public void iterateForBodExpense(MasterDataRepository masterDataRepository, List<GLEntries> gLEntriesSet,
			Integer payrollId) {
		log.info("Inside @class OracleIntegrationServiceImpl @method iterateForBodExpense ");
		try {
			EmployeeMonthlySalaryService employeeMonthlySalaryService = ApplicationContextProvider
					.getApplicationContext().getBean(EmployeeMonthlySalaryService.class);
			List<Object[]> listOfBod = employeeMonthlySalaryService.getExpenseForBOD(payrollId,
					PRConstant.BOARD_OF_DIRECTORS);
			for (Object[] object : listOfBod) {
				String deptCodeSs = (String) object[0];
				Integer deptCode = Integer.parseInt(deptCodeSs);
				Integer code;
				if (object[1] != null) {
					GLEntries gl = getfixedGl();
					code = getGlEntryCodeOnParameters(masterDataRepository, PRConstant.CONSULTING_FEES,
							PRConstant.COMPONENT, PRConstant.EXPENSES);
					Double value = (Double) object[1];
					log.debug(THE_VALUE_IS, value);
					if (value != 0.0) {
						gl = getGlEntryforExpense(code, gl, value, deptCode);
						gLEntriesSet.add(gl);
					}
				}
				log.info(THE_GL_ENTRIES_ADDED);
			}
		} catch (Exception e) {
			log.error("Error Inside OracleIntegrationServiceImpl @method iterateForBodExpense :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	public void iterateForBodAccural(MasterDataRepository masterDataRepository, List<GLEntries> gLEntriesSet,
			Integer payrollId) {
		log.info("Inside @class OracleIntegrationServiceImpl @method iterateForBodAccural ");
		try {
			EmployeeMonthlySalaryService employeeMonthlySalaryService = ApplicationContextProvider
					.getApplicationContext().getBean(EmployeeMonthlySalaryService.class);
			Object[] listOfBod = employeeMonthlySalaryService.getAccuralsForBOD(payrollId,
					PRConstant.BOARD_OF_DIRECTORS);
			for (Object object : listOfBod) {
				Integer code;
				if (object != null) {
					GLEntries gl = getfixedGl();
					code = getGlEntryCodeOnParameters(masterDataRepository, PRConstant.OTHER_NON_TRADE_PAYABLES,
							PRConstant.COMPONENT, PRConstant.ACCURALS);
					Double value = (Double) object;
					log.debug(THE_VALUE_IS, value);
					if (value != 0.0) {
						gl = getGlEntryforAccruals(code, gl, value);
						gLEntriesSet.add(gl);
					}
				}
				log.info(THE_GL_ENTRIES_ADDED);
			}
		}
		catch (Exception e) {
			log.error("Error Inside OracleIntegrationServiceImpl @method iterateForBodAccural :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	private String getParentWrapper(GLMetaData glMetaData) {
		log.info("Inside @class OracleIntegrationServiceImpl @method getParentWrapper ");
		try {

			Data data = new Data();
			data.setGl_metadata(glMetaData);
			List<Data> dataList = new ArrayList<>();
			RequestObject requestObject = new RequestObject();

			List<RequestObject> listRequest = new ArrayList<>();
			dataList.add(data);
			requestObject.setData(dataList);
			listRequest.add(requestObject);

			ParentWrapper parentWrapper = new ParentWrapper();
			parentWrapper.setRequestObject(listRequest);

			HashMap<String, GLMetaData> glmap = new HashMap<>();
			glmap.put("gl_metadata", glMetaData);

			log.debug("The GLEntries added map:{}", glmap);

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValueAsString(glmap);
			String parentWrapperValue = objectMapper.writeValueAsString(parentWrapper);
			log.debug("The GLEntries added map:{}", glmap);

			return parentWrapperValue;

		} catch (Exception e) {
			throw new BusinessException();
		}
	}

	private List<String> getCompenentList() {
		List<String> list = new ArrayList<>();
		list.add("List");
		list.add(PRConstant.OVER_BASE);
		list.add(PRConstant.CRITICAL_TALENT);
		list.add(PRConstant.SHORT_TERM_INCENTIVE);
		list.add(PRConstant.MOBILE_ALLOWANCE);
		list.add(PRConstant.OVERTIME);
		list.add(PRConstant.EMPLOYER_GOSI);
		list.add(PRConstant.EMPLOYEE_GOSI);
		list.add(PRConstant.SIGNING_BONUS);
		list.add(PRConstant.RELOCATION_PACKAGE_NEW_HIRE);
		list.add(PRConstant.OTHER_EARNING);
		list.add(PRConstant.OTHER_DEDUCTION);
		return list;
	}

	private List<String> getBenefitsList() {
		List<String> list = new ArrayList<>();
		list.add(PRConstant.EDUCATION_BENEFIT_NEW);
		list.add(PRConstant.HEALTH_CLUB_BENEFIT_NEW);
		list.add(PRConstant.NEW_HIRE_BENEFIT_NEW);
		list.add(PRConstant.CHILD_EDUCATION_BENEFIT_NEW);
		return list;
	}

	void saveIntegrationLog(String finalResponse, Integer statusCode, String url, Map<String, String> headers,
			String payload, Double processId, String timeStamp, Integer payrollId) {
		try {

			log.info("Inside @class OracleIntegrationServiceImpl @method saveIntegrationLog ");
			IntegrationLogService integrationLogService = ApplicationContextProvider.getApplicationContext()
					.getBean(IntegrationLogService.class);
			IntegrationLog integrationLog = new IntegrationLog();
			integrationLog.setResponse(finalResponse);
			Integer batchName = getBatchName() + 1;

			log.debug("The batch for Setting in IntegrationLog is :{}  ", batchName);
			integrationLog.setUrl(url);
			integrationLog.setPayload(payload);
			integrationLog.setHttpType("GET");
			integrationLog.setBatchName(batchName);
			integrationLog.setIntegrationType("OutBound");
			String header = IntegrationLogServiceImpl.getJsonString(headers);
			integrationLog.setHeaders(header);
			integrationLog.setStatus(Status.COMPLETED);
			integrationLog.setRetryCount(0);
			integrationLog.setProcessId(processId);

			integrationLog.setRequestId(timeStamp);
			integrationLog.setPayrollId(payrollId);
			log.info("The value :{} ", integrationLog.getPayrollId());
			integrationLogService.createIntegrationRequest(integrationLog);
			log.info("Data Saved  in Integration log ");
		} catch (Exception e) {
			log.error("Error Inside  OracleIntegrationServiceImpl @method  saveIntegrationLog :{}  :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	private Integer getBatchName() {
		try {
			log.info("Inside @class OracleIntegrationServiceImpl @method getBatchName ");
			IntegrationLogService integrationLogService = ApplicationContextProvider.getApplicationContext()
					.getBean(IntegrationLogService.class);
			Integer batchName = integrationLogService.getBatchName();
			log.debug("THe batch name is :{} ", batchName);
			// batchName = batchName +1;
			// log.error("THe batch name after increasing is :{} ",batchName);
			return batchName;
		} catch (Exception e) {
			log.error("Error Inside OracleIntegrationServiceImpl @method getBatchName :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	private String dateFormated() {
		LocalDate date = LocalDate.now();
		int monthValue = date.getMonthValue();

		String[] monthAbbreviations = { "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov",
				"dec" };

		String month = monthAbbreviations[monthValue - 1];

		DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("d");
		String day = date.format(dayFormatter);

		String formattedDate = month + "_" + day;
		return formattedDate;
	}

	private String getSystemTimeWithTimeStamp() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		return now.format(formatter);
	}

	@Override
	public String updateFinalResponse(Double processId, String status, String requestId) {
		log.info("Inside @class OracleIntegrationServiceImpl @method updateFinalResponse ");
		log.debug(" Inside class :{} @method updateFinalResponse  customerId is : {}",CLASS_NAME ,commonUtils.getCustomerId());
		try {
			log.debug("the requestID :{}  status :{} ", requestId, status);
			IntegrationLogService integrationLogService = ApplicationContextProvider.getApplicationContext()
					.getBean(IntegrationLogService.class);
			Integer rows = integrationLogService.updateResponse(processId, status, requestId);
			log.debug("Total no. of rows updated :{} ", rows);
			Integer id = integrationLogService.getPayRollRun(processId, requestId);
			log.debug("Inside @class OracleIntegrationServiceImpl @method updateFinalResponse :{} ", id);
			PayrollRunService payRollRunService = ApplicationContextProvider.getApplicationContext()
					.getBean(PayrollRunService.class);
			PayrollRun payrollRun = payRollRunService.getPayrollById(id);
			log.debug("Inside @class OracleIntegrationServiceImpl @method updateFinalResponse :{} ",
					payrollRun.getWorkflowStage());
			payrollRun.setWorkflowStage(PRConstant.COMPLETED);
			payRollRunService.update(payrollRun);
			return APIConstants.SUCCESS_JSON;
		} catch (Exception e) {
			log.error("Error Inside OracleIntegrationServiceImpl @method updateFinalResponse :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}
	
	public List<Integer> getIdListofHrBenefits(HrBenefitsService hrBenefitsService , List<OtherExpenseBankRequest>  listOfOtherExpenseBankRequest)
	{
		try
		{
			log.info("Inside @class OracleIntegerServiceImpl @method getIdListOfHrBenefits ");
			List<Integer> benefitsId = new ArrayList<>();
			for(OtherExpenseBankRequest OtherExpenseBankRequest : listOfOtherExpenseBankRequest)
			{
		List<HrBenefits> hrBenefitsList = hrBenefitsService.getListOfHrBenefitsByOtherExpenseBankRequestId(OtherExpenseBankRequest);
			for(HrBenefits hrBenefit :hrBenefitsList)
			{
				benefitsId.add(hrBenefit.getId());
			}
			}
			return benefitsId;
		}
		catch(Exception e)
		{
			log.error("Error Inside OracleIntegrationServiceImpl @method getIdListofHrBenefits :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}
	
	public List<Integer> getIdListofBusinessExpense(BusinessExpenseService businessExpenseService , List<OtherExpenseBankRequest>  listOfOtherExpenseBankRequest)
	{
		try
		{
			log.debug("Inside @class OracleIntegerServiceImpl @method getIdListofBusinessExpense size is :{} ",listOfOtherExpenseBankRequest.size());
			List<Integer> expenseId = new ArrayList<>();
			for(OtherExpenseBankRequest OtherExpenseBankRequest : listOfOtherExpenseBankRequest)
			{
		List<BusinessExpense> businessExpenseList = businessExpenseService.getListOfBusinessExpenseByOtherExpenseBankRequestId(OtherExpenseBankRequest);
			for(BusinessExpense businessExpense :businessExpenseList)
			{
				log.debug("Id of Business Expense is :{} ",businessExpense.getId());
				expenseId.add(businessExpense.getId());
			}
			}
			return expenseId;
		}
		catch(Exception e)
		{
			log.error("Error Inside OracleIntegrationServiceImpl @method getIdListofHrBenefits :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}
	
 	void iterateForBusinessExpense(MasterDataRepository masterDataRepository,List<GLEntries> gLEntriesSet,BusinessExpenseService businessExpenseService,List<Integer> listOfBusinessExpenseId)
	{
	  try
	  {
		  log.info("Inside @class OracleIntegrationServiceImpl @method iterateForBusinessExpense ");
		 List<Object []> businessExpenses = businessExpenseService.getBusinessExpenseforExpense(listOfBusinessExpenseId);
	       for(Object[] businessExpense : businessExpenses)
	       {
	    	   log.debug("Inside iterateForBusinessExpense :{} ",businessExpense[0]);
	    	   String code = (String)businessExpense[0];
	    	   Integer text1 = Integer.parseInt(code);
	    	   GLEntries glEntry = getfixedGl();
	    	   Integer deptCode = getGlEntryCodeOnParameters(masterDataRepository ,PRConstant.BUSINESS_EXPENSE,PRConstant.COMPONENT,PRConstant.EXPENSES);
	         if(businessExpense[1]!=null)
	         {
	        	 Double value = (Double) businessExpense[1];
	        	 log.debug(THE_VALUE_IS,value);
	        	 if(value !=0.0)
	        	 {
	        		 glEntry = getGlEntryforExpense(deptCode, glEntry, value, text1);
	        		 gLEntriesSet.add(glEntry);
	        	 }
	         }
	       }
	  
	  }
	  catch(Exception e)
	  {
		  log.error("Error Inside OracleIntegrationServiceImpl @method iterateForBusinessExpense :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
	  }
	
	}

 	
 	void iterateForBusinessExpenseAccural(MasterDataRepository masterDataRepository,List<GLEntries> gLEntriesSet,BusinessExpenseService businessExpenseService,List<Integer> listOfBusinessExpenseId)
	{
	  try
	  {
		  log.info("Inside @class OracleIntegrationServiceImpl @method iterateForBusinessExpense ");
		 Object businessExpenses = businessExpenseService.getBusinessExpenseforAccural(listOfBusinessExpenseId);
	    	  GLEntries glEntry = getfixedGl();
	    	   Integer deptCode = getGlEntryCodeOnParameters(masterDataRepository ,PRConstant.BUSINESS_EXPENSE,PRConstant.COMPONENT,PRConstant.ACCURALS);
	         if(businessExpenses!=null)
	         {
	        	 Double value = (Double) businessExpenses;
	        	 log.debug(THE_VALUE_IS,value);
	        	 if(value !=0.0)
	        	 {
	        		 glEntry = getGlEntryforAccruals(deptCode, glEntry, value);
	        		 gLEntriesSet.add(glEntry);
	        	 }
	         }  
	  }
	  catch(Exception e)
	  {
		  log.error("Error Inside OracleIntegrationServiceImpl @method iterateForBusinessExpense :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
	  }
	
	}

	List<Integer> getIdListOfBusinessTrip(BusinessTripService businessTripService,List<OtherExpenseBankRequest> listOfBusinessTrip)
	{
		try
		{
			log.debug("Inside @class OracleIntegrationServiceImpl @method getIdListOfBusinessTrip :{} ",listOfBusinessTrip.size());
			List<Integer> businessTripIds =  new ArrayList<>();
			for(OtherExpenseBankRequest otherExpenseBankRequest : listOfBusinessTrip)
			{
				List<BusinessTrip> businessTrips = businessTripService.getBusinessTripByOtherExpenseBankRequestId(otherExpenseBankRequest);
				for(BusinessTrip businessTrip :businessTrips)
				{
					businessTripIds.add(businessTrip.getId());
				}
			}
			log.debug("Size of Business Trip ids :{} ",businessTripIds.size());
			return businessTripIds;
		}
		catch(Exception e)
		{
			log.error("Error Inside OracleIntegrationServiceImpl @method getIdListOfBusinessTrip :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	void iterateExternalBusinessTripExpense(MasterDataRepository masterDataRepository ,List<GLEntries> gLEntriesSet,BusinessTripService businessTripService,List<Integer> businessTripsId,String type)
	{
		try
		  {
			  log.info("Inside @class OracleIntegrationServiceImpl @method iterateExternalBusinessTripExpense ");
			 List<Object []> businessTrips = businessTripService.getBusinessTripForExpense(businessTripsId,type);
			 Integer deptCode;  
			 for(Object[] businessTrip : businessTrips)
		       {
		    	 
		    	   String code = (String)businessTrip[0];
		    	   log.debug("Inside iterateExternalBusinessTripExpense :{} ",code);
		    	   Integer text1 = Integer.parseInt(code);
		    	   GLEntries glEntry = getfixedGl();
		       deptCode = getGlEntryCodeOnParameters(masterDataRepository ,PRConstant.EXTERNAL_BUSINESS_TRIP,PRConstant.COMPONENT,PRConstant.EXPENSES);
		      
		    if(businessTrip[1]!=null)
		         {
		        	 Double value = (Double) businessTrip[1];
		        	 log.debug(THE_VALUE_IS,value);
		        	 if(value !=0.0)
		        	 {
		        		 glEntry = getGlEntryforExpense(deptCode, glEntry, value, text1);
		        		 gLEntriesSet.add(glEntry);
		        	 }
		         }
		       }
		  
		  }
		  catch(Exception e)
		  {
			  log.error("Error Inside OracleIntegrationServiceImpl @method iterateExternalBusinessTripExpense :{} :{}", e.getMessage(),
						Utils.getStackTrace(e));
				throw new BusinessException();
		  }
	}
	
	void iterateExternalBusinessTripAccural(MasterDataRepository masterDataRepository ,List<GLEntries> gLEntriesSet,BusinessTripService businessTripService,List<Integer> businessTripsId,String type)
	{
		try
		  {
			  log.info("Inside @class OracleIntegrationServiceImpl @method iterateExternalBusinessTripAccural ");
			  Object  businessTrips = businessTripService.getBusinessTripForAccural(businessTripsId,type);
			  Integer deptCode;
		      	   GLEntries glEntry = getfixedGl();
		      	  	 deptCode = getGlEntryCodeOnParameters(masterDataRepository ,PRConstant.EXTERNAL_BUSINESS_TRIP,PRConstant.COMPONENT,PRConstant.ACCURALS);
				      
		         if(businessTrips!=null)
		         {
		        	 Double value = (Double) businessTrips;
		        	 log.debug(THE_VALUE_IS,value);
		        	 if(value !=0.0)
		        	 {
		        		 glEntry = getGlEntryforAccruals(deptCode, glEntry, value);
		        		 gLEntriesSet.add(glEntry);
		        	 }
		         }
		    }
		
		  
		  catch(Exception e)
		  {
			  log.error("Error Inside OracleIntegrationServiceImpl @method iterateExternalBusinessTripAccural :{} :{}", e.getMessage(),
						Utils.getStackTrace(e));
				throw new BusinessException();
		  }
	}

	
	void iterateInternalBusinessTripExpense(MasterDataRepository masterDataRepository ,List<GLEntries> gLEntriesSet,BusinessTripService businessTripService,List<Integer> businessTripsId,String type)
	{
		try
		  {
			  log.info("Inside @class OracleIntegrationServiceImpl @method iterateInternalBusinessTripExpense ");
			 List<Object []> businessTrips = businessTripService.getBusinessTripForExpense(businessTripsId,type);
			 Integer deptCode;  
			 for(Object[] businessTrip : businessTrips)
		       {
		    	   log.debug("Inside iterateInternalBusinessTripExpense :{} ",businessTrip[0]);
		    	   String code = (String)businessTrip[0];
		    	   Integer text1 = Integer.parseInt(code);
		    	   GLEntries glEntry = getfixedGl();
		     	    deptCode = getGlEntryCodeOnParameters(masterDataRepository ,PRConstant.INTERNAL_BUSINESS_TRIP,PRConstant.COMPONENT,PRConstant.EXPENSES);
		     
		    if(businessTrip[1]!=null)
		         {
		        	 Double value = (Double) businessTrip[1];
		        	 log.debug(THE_VALUE_IS,value);
		        	 if(value !=0.0)
		        	 {
		        		 glEntry = getGlEntryforExpense(deptCode, glEntry, value, text1);
		        		 gLEntriesSet.add(glEntry);
		        	 }
		         }
		       }
		  
		  }
		  catch(Exception e)
		  {
			  log.error("Error Inside OracleIntegrationServiceImpl @method iterateInternalBusinessTripExpense :{} :{}", e.getMessage(),
						Utils.getStackTrace(e));
				throw new BusinessException();
		  }
	}
	
	void iterateInternalBusinessTripAccural(MasterDataRepository masterDataRepository ,List<GLEntries> gLEntriesSet,BusinessTripService businessTripService,List<Integer> businessTripsId,String type)
	{
		try
		  {
			  log.info("Inside @class OracleIntegrationServiceImpl @method iterateInternalBusinessTripAccural ");
			  Object  businessTrips = businessTripService.getBusinessTripForAccural(businessTripsId,type);
			  Integer deptCode;
		      	   GLEntries glEntry = getfixedGl();
		      	    deptCode = getGlEntryCodeOnParameters(masterDataRepository ,PRConstant.INTERNAL_BUSINESS_TRIP,PRConstant.COMPONENT,PRConstant.ACCURALS);	    
		         if(businessTrips!=null)
		         {
		        	 Double value = (Double) businessTrips;
		        	 log.debug(THE_VALUE_IS,value);
		        	 if(value !=0.0)
		        	 {
		        		 glEntry = getGlEntryforAccruals(deptCode, glEntry, value);
		        		 gLEntriesSet.add(glEntry);
		        	 }
		         }
		    }
		
		  
		  catch(Exception e)
		  {
			  log.error("Error Inside OracleIntegrationServiceImpl @method iterateInternalBusinessTripAccural :{} :{}", e.getMessage(),
						Utils.getStackTrace(e));
				throw new BusinessException();
		  }
	}
	
	@Override
	public String getFirstOCILedgerDataEntry(Integer payrollId)
	{
		try
		{
			log.debug(" Inside class :{} @method getFirstOCILedgerDataEntry  ",CLASS_NAME );
			PayrollRunService payRollRunService = ApplicationContextProvider.getApplicationContext()
					.getBean(PayrollRunService.class);	 
			PayrollRun payRollRun = payRollRunService.getPayrollById(payrollId);
			Map<String, String> hrmsSystemConfigMap = hrmsSystemConfigService.getHrmsKeyValue();
			String listEmployee = hrmsSystemConfigMap.get(PRConstant.ON_BOARD_MEMBER_TYPE_LIST);
			String[] parts = listEmployee.split(",");
			log.debug("Inside @class OracleIntegrationServiceImpl @method getFirstOCILedgerDataEntry parts :{} ", parts);
			MasterDataRepository masterDataRepository = ApplicationContextProvider.getApplicationContext()
					.getBean(MasterDataRepository.class);
			GLMetaData glMetaData = getGlMetaDataObject();
			log.info("Inside @class OracleIntegrationServiceImpl @method getFirstOCILedgerDataEntry value of GLMetaData :{} ",
					glMetaData);
			List<GLEntries> gLEntriesSet = new ArrayList<>();
			String workFlowStage = hrmsSystemConfigMap.get(PRConstant.FIRST_WORKFLOW_STAGE_FOR_GL);
			log.debug(" Workflow Stage of payroll ");
			log.debug("Value for Workflow Stage for first WorkflowStage :{} firstWorflowStage:{} ",workFlowStage,PRConstant.FIRST_WORKFLOW_STAGE_FOR_GL);
			if(workFlowStage==null)
			{
				workFlowStage=PRConstant.APPROVED_BY_FINANCE_CONTROLLER;
			}
			Map<String ,List<Employee>> employeeCodeMap = new HashMap<>();
			setEmployeeCodeMapping(employeeCodeMap,payRollRun,parts);
			log.debug("Size of EmployeeCodeMap :{} ",employeeCodeMap.size());
			log.debug(" Employee Code Map :{} ",employeeCodeMap);
			if (payRollRun.getWorkflowStage().equalsIgnoreCase(workFlowStage) && workFlowStage!=null) {
				log.info(" First Entry ");
				addDataForBasicSalaryInGlExpense(payRollRun,masterDataRepository,gLEntriesSet,employeeCodeMap,parts);
				addDataForBasicSalaryInGlAccural(payRollRun,masterDataRepository,gLEntriesSet,employeeCodeMap,parts);	
			}
			if(payRollRun.getWorkflowStage().equalsIgnoreCase("PAYROLL_BANK_APPROVAL"))
			{
				log.info(" Second Entry ");
			BigDecimal accuralSum= addDataForBasicSalaryInGlAccural(payRollRun,masterDataRepository,gLEntriesSet,employeeCodeMap,parts);	
				log.debug("Value of Accural Sum for Second Entry :{} ",accuralSum);
		      	   GLEntries glEntry = getfixedGl();
		      	   Double value = accuralSum.doubleValue();
				 glEntry = getGlEntryforAccruals(999, glEntry, value);
        		 gLEntriesSet.add(glEntry);
			}
			if (gLEntriesSet.size() > 0) {
				glMetaData.setGl_entries(gLEntriesSet);
				String parentWrapperValue = getParentWrapper(glMetaData);
				log.debug("The value of RequestObject is  :{}", parentWrapperValue);
				//hrBenefitsService.updateGLStatus(PRConstant.PUSHED, PRConstant.NOT_PUSHED, PRConstant.APPROVED_SM);
				//return parentWrapperValue;
				return updateDataOnOracleLedger(parentWrapperValue, payrollId);
			}
			return null;
		}
		catch(Exception e)
		{
			log.error("Error Inside OracleIntegrationServiceImpl @method getFirstOCILedgerDataEntry :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}	
	
	private Map<String , Double> addDataForBasicSalaryInGlExpense(PayrollRun payrollRun ,MasterDataRepository masterDataRepositry,List<GLEntries> glEntriesSet,Map<String ,List<Employee>> employeeCodeMap,String[] parts)
	{
		EmployeeMonthlySalaryService employeeMonthlySalaryService = ApplicationContextProvider
				.getApplicationContext().getBean(EmployeeMonthlySalaryService.class);
		   Map<String , Double> deptCodeAndValueMap = new HashMap<>();
		try {
 			log.info("Inside @class OracleIntegrationServiceImpl @method addDataForBasicSalaryInGlExpense ");
			for(Map.Entry<String,List<Employee>> entrySet : employeeCodeMap.entrySet())
			{
				Double sum = 0.0;
				String departmentCode = entrySet.getKey();
				List<Employee> employeeList = entrySet.getValue();	
				 Double[] valuesSum = new Double[5];
	             Double[] deductionSum = new Double[4];
	             Arrays.fill(valuesSum, 0.0); 
	             Arrays.fill(deductionSum, 0.0); 
			    log.debug("Department Code :{} ",departmentCode);	
			    if(!employeeList.isEmpty()) {
			    	sum = iterateEmployeeForGlExpense(employeeList,payrollRun,valuesSum,departmentCode,masterDataRepositry,glEntriesSet,deductionSum);
			    }
				 pushTheBasicSalaryDetailsInGlExpense(departmentCode,valuesSum,masterDataRepositry,glEntriesSet,PRConstant.EXPENSES);
		           pushTheBasicSalaryWithSameCode(departmentCode,deductionSum,masterDataRepositry,glEntriesSet,PRConstant.EXPENSES);	 
		           deptCodeAndValueMap.put(entrySet.getKey(),sum);	
	     	}
			List<Object[]> listOfObject = employeeMonthlySalaryService.getSumOfValue(payrollRun.getId(), parts);
			log.debug("The size of Object list is :{}", listOfObject.size());
		Map<String ,Double> deptAndCodeMap 	= iterateForListOfObjectExpense(masterDataRepositry, listOfObject, glEntriesSet);
		log.debug(" value of deptAndCodeMap :{} ",deptAndCodeMap);
		for (Map.Entry<String, Double> entry : deptAndCodeMap.entrySet()) { 
		    deptCodeAndValueMap.put(entry.getKey(), entry.getValue());
		}
         log.debug("Value of deptCodeAndValueMap :{} ",deptCodeAndValueMap);
			return deptCodeAndValueMap;
 		}
		catch(Exception e)
		{
			log.error("Error Inside addDataAccordingToLeaveInGl :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

   private Double iterateEmployeeForGlExpense(List<Employee> employeeList,PayrollRun payrollRun,Double[] valuesSum,String departmentCode,MasterDataRepository masterDataRepositry,List<GLEntries> glEntriesSet,Double[] deductionSum)
   {
		EmployeeSalaryStructureService employeeSalaryStructureService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeSalaryStructureService.class);
		EmployeeMonthlySalaryService employeeMonthlySalaryService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeMonthlySalaryService.class);
	try
	{ 
		Double sum = 0.0 ;
		for(Employee employee :employeeList)
		{
			EmployeeMonthlySalary employeeMonthlySalary = employeeMonthlySalaryService.getEmployeeMonthlySalaryByEmployeeIdAndPayrollRunId(employee.getId(), payrollRun.getId());
			EmployeeSalaryStructure employeeSalaryStructure = employeeSalaryStructureService.getEmployeeMappedSalaryStructure(employee.getId());
			if(employeeMonthlySalary!=null && employeeSalaryStructure!=null)
			{
			sum = calculateSalary(employeeSalaryStructure,employeeMonthlySalary,valuesSum,deductionSum) + sum;
		 log.debug("Values for EmployeeId :{} , Basic Salary :{} , HRA :{} , TA :{}",employee.getId(),valuesSum[0],valuesSum[1],valuesSum[2]);
			}
		}
		return sum;
	}
	catch(Exception e)
	{
		log.error("Error Inside iterateEmployeeForGl :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
		throw new BusinessException();
	}
   }
   
   private Double calculateSalary(EmployeeSalaryStructure employeeSalaryStructure,EmployeeMonthlySalary employeeMonthlySalary,Double[] expenseSum ,Double[] deductionSum)
   {
	   Double totalSum = 0.0 ;
	   try
	   {
		   Integer totalDays = employeeMonthlySalary.getPayrollRun().getDurationDayCount();
		   log.debug("Inside @class OracleIntegrationServiceImpl @method calculateSalary employeeSalarystructureID :{} EmployeeMonthlySalaryID :{} ",
				   employeeSalaryStructure.getId(),employeeMonthlySalary.getId());
		   Double basicSalary = Optional.ofNullable(employeeSalaryStructure.getBasicSalary()).orElse(0.0);
		   Double hra = Optional.ofNullable(employeeSalaryStructure.getHra()).orElse(0.0);
		   Double ta = Optional.ofNullable(employeeSalaryStructure.getTa()).orElse(0.0);
		   Integer employeeWorkingDay = Optional.ofNullable(employeeMonthlySalary.getWorkingDays()).orElse(0);
		   Double sickUnpaidCount = Optional.ofNullable(employeeMonthlySalary.getSickUnpaidCount()).orElse(0.0);
		   Double sickPaidCount = Optional.ofNullable(employeeMonthlySalary.getSickPaidCount()).orElse(0.0);
		   Double unpaidLeaveCount = (double) Optional.ofNullable(employeeMonthlySalary.getUnpaidLeaveCount()).orElse(0);
		   log.debug("EmployeeId :{} Basic Salary :{} HRA :{} TA :{} ",
				   employeeMonthlySalary.getEmployee().getId(),basicSalary,hra,ta);
		   Double perDayBasicSalary = basicSalary/totalDays;
		   Double perDayHRA = hra/totalDays;
		   Double perDayTA = ta/totalDays;
		   Double unpaidLeaveValue = (double)employeeMonthlySalary.getUnpaidLeaveCount()*perDayBasicSalary;
		   log.debug("EmployeeId :{} Per Day Basic Salary :{} PerDay HRA :{} Per Day TA :{} unpaidLeaveValue:{} ",
				   employeeMonthlySalary.getEmployee().getId(),perDayBasicSalary,perDayHRA,perDayTA,unpaidLeaveValue);
		  
		   Double totalEmployeeDays = (double)employeeWorkingDay + sickUnpaidCount; 
		   log.debug(" EmployeeWorking Days :{} sickUnpaidCount :{} totalEmployeeDays :{} sickPaidCount:{} ",
				   employeeWorkingDay,sickUnpaidCount,totalEmployeeDays,sickPaidCount);
		   
		   Double sickPaidValue = sickPaidCount*perDayBasicSalary;
		   Double sickUnpaidValue = sickUnpaidCount*perDayBasicSalary;
		   log.debug("Value of SickPaidValue :{} sickUnpaidValue :{} ",sickUnpaidValue,sickUnpaidValue);
		   
		   
		   Double correctBasicSalary = perDayBasicSalary*totalEmployeeDays;
		   Double correctHRA = perDayHRA*totalEmployeeDays;
		   Double correctTA = perDayTA*totalEmployeeDays;
		   log.debug(" Correct Value of Basic Salary :{} HRA :{} TA :{} for ID:{}  ",correctBasicSalary,correctHRA,correctTA,employeeMonthlySalary.getEmployee().getId());
		  
		   Double annualLeaveConstant = Double.parseDouble(hrmsSystemConfigService.getValue(PRConstant.ANNUAL_LEAVE_CONSTANT));
         	Double annualLeaveValue = annualLeaveConstant*perDayBasicSalary;
		  log.debug(" annualLeaveConstant:{}  annualLeaveValue:{} ",annualLeaveConstant,annualLeaveValue);
		  
		  
		  expenseSum[0] = correctBasicSalary + expenseSum[0]+perDayBasicSalary*unpaidLeaveCount;
		  expenseSum[1] = correctHRA +expenseSum[1]+perDayHRA*unpaidLeaveCount;
		  expenseSum[2] = correctTA +expenseSum[2]+perDayTA*unpaidLeaveCount; 
		  expenseSum[3] = sickUnpaidValue+sickPaidValue+expenseSum[3];
		  expenseSum[4] = annualLeaveValue+expenseSum[4];
		  deductionSum[0] = perDayBasicSalary*unpaidLeaveCount+deductionSum[0];
		  deductionSum[1] = perDayHRA*unpaidLeaveCount+deductionSum[1];
		  deductionSum[2] = perDayTA*unpaidLeaveCount+deductionSum[2];
		  deductionSum[3] = sickUnpaidValue+deductionSum[3];
         		  log.debug("Values of Basic Salary :{} Hra :{} TA :{} SickLeave :{} AnnualLeave :{} after Sum ",expenseSum[0],expenseSum[1],expenseSum[2],expenseSum[3],expenseSum[4]);
         		//log.debug("Value of Accural inside calculateSalary :{}",Arrays.toString(accuralSum));
         		log.debug("Value of deduction inside calculateSalary :{}",Arrays.toString(deductionSum));
         	 
         	totalSum = 	expenseSum[0]+expenseSum[1]+expenseSum[2]+expenseSum[3]+expenseSum[4]+deductionSum[0]
         			+deductionSum[1]+deductionSum[2]+deductionSum[3];
         	
         	return totalSum;
	   }
	   catch(Exception e)
	   {
		   log.error("Error inside @class OracleIntegrationServiceImpl @method calculateSalary :{} :{} ",
				   e.getMessage(),Utils.getStackTrace(e));
         return null;
	   }
 
   }
   
   private void pushTheBasicSalaryDetailsInGlExpense(String departmentCode ,Double[] valuesArray,MasterDataRepository masterDataRepository,List<GLEntries> gLEntriesSet,String type)
   {
	   try {
	           
		   Integer departmentCodeInt = Integer.parseInt(departmentCode);
		   log.debug("Inside @pushTheBasicSalaryDetailsInGl  departmentCode :{} type :{} ",departmentCode,type);
		   List<String> basicSalaryList = getBasicSalaryList();
		   for(int i=0;i<basicSalaryList.size();i++)
		   {
			   GLEntries gl = getfixedGl();
			 Integer code = getGlEntryCodeOnParameters(masterDataRepository,basicSalaryList.get(i),PRConstant.COMPONENT, type);
 			  gl = getGlEntryforExpense(code,gl, valuesArray[i], departmentCodeInt);
 			 gLEntriesSet.add(gl);
		   }   
	   }
	   catch(Exception e)
	   {
		   log.error("Error Inside @class OracleIntegerationServiceImpl @method pushTheBasicSalaryInGl :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
		   throw new BusinessException();
	   }
   } 
   
  private  List<String> getBasicSalaryList()
  {
	  List<String> salaryThings = new ArrayList<>();
	  salaryThings.add(PRConstant.BASIC_SALARY);
	  salaryThings.add(PRConstant.HRA);
	  salaryThings.add(PRConstant.TA);
	  salaryThings.add(PRConstant.SICK_LEAVE);
	  salaryThings.add(PRConstant.ANNUAL_LEAVE);
	  return salaryThings;
  }
  private void pushTheBasicSalaryDetailsInGlAccural(Double[] valuesArray,MasterDataRepository masterDataRepository,List<GLEntries> gLEntriesSet,String type)
  {
	   try {
	
		   log.debug("Inside @pushTheBasicSalaryDetailsInGl  type :{} ",type);
		   List<String> basicSalaryList = getBasicSalaryList();
		   for(int i=0;i<basicSalaryList.size();i++)
		   {
			   GLEntries gl = getfixedGl();
			 Integer code = getGlEntryCodeOnParameters(masterDataRepository,basicSalaryList.get(i),PRConstant.COMPONENT, type);
			  gl = getGlEntryforAccruals(code,gl, valuesArray[i]);
			 gLEntriesSet.add(gl);
		   }   
	   }
	   catch(Exception e)
	   {
		   log.error("Error Inside @class OracleIntegerationServiceImpl @method pushTheBasicSalaryInGl :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
		   throw new BusinessException();
	   }
  } 

 private void  pushTheBasicSalaryWithSameCode(String departmentCode,Double[] deductionSum,MasterDataRepository masterDataRepository,List<GLEntries> glEntriesSet,String type)
  {
	  try
	  {
		  log.debug(" Inside @pushTheBasicSalaryWithSameCode type :{} departmentCode:{} ",type,departmentCode);
		  List<String> sameCodePushList = getSameCodeList();
		  for(int i = 0;i<sameCodePushList.size();i++)
		  {
			  Integer deptCodeInt = Integer.parseInt(departmentCode);
			  GLEntries gl = getfixedGl();
			  Integer code = getGlEntryCodeOnParameters(masterDataRepository,sameCodePushList.get(i),PRConstant.COMPONENT, type);
			  gl = getGlEntryforExpenseSameCode(code,gl, deductionSum[i], deptCodeInt);
			  glEntriesSet.add(gl);
		  }
	  }
	  catch(Exception e)
	  {
		  log.error("Error Inside @class OracleIntegerationServiceImpl @method pushTheBasicSalaryWithSameCode :{} :{} ",
				  e.getMessage(),Utils.getStackTrace(e));
		   throw new BusinessException();
	  }
  }
 
	private GLEntries getGlEntryforExpenseSameCode(Integer code, GLEntries glEntries, Double amount, Integer deptCode) {

		try {
			Integer value = getBatchName();
			value = value + 1;
			String groupid = String.valueOf(value);
			log.info("Inside @class OracleIntegrationServiceImpl @method getGlEntryforExpenseSameCode ");
			DecimalFormat df = new DecimalFormat("#.##");
			String exAmnt = (String) df.format(amount);
			log.debug("The  Value is in Expense :{} code :{} ", exAmnt, code);
			glEntries.setLocation_code("001001");
			glEntries.setLob_code("0001");
			String deptCodeS = String.valueOf(deptCode);
			glEntries.setGroup_id(groupid);
			glEntries.setCc_code(deptCodeS);
			glEntries.setProject_code(ZERO4);
			String codeC = String.valueOf(code);
			glEntries.setNatural_acc_code(codeC);
			glEntries.setIntercomp_code(ZERO3);
			glEntries.setCustom_code1(ZERO4);
			glEntries.setCustom_code2(ZERO4);
			glEntries.setCurrency_code(SAR);
			glEntries.setEntered_dr_amt("");
			glEntries.setAccounted_dr("");
			glEntries.setAccounted_cr(exAmnt);
			glEntries.setEntered_cr_amt(exAmnt);
			return glEntries;
		} catch (Exception e) {
			log.error("Error Inside OracleIntegrationServiceImpl @method getGlEntry :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}

	}

	private List<String> getSameCodeList()
	{
		  List<String> salaryThings = new ArrayList<>();
		  salaryThings.add(PRConstant.BASIC_SALARY);
		  salaryThings.add(PRConstant.HRA);
		  salaryThings.add(PRConstant.TA);
		  salaryThings.add(PRConstant.SICK_LEAVE);
 		  return salaryThings;

	}
	
	private void setEmployeeCodeMapping(Map<String , List<Employee>> employeeCodeMap,PayrollRun payrollRun , String[] parts)
	{
		try
		{
			log.info("Inside @method setEmployeeCodeMapping ");
			EmployeeService employeeService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeService.class);
			List<String> departmentCodes = employeeService.getDepartmentCodeForEmployees();
			log.debug("DepartmentCode Recevied :{} ",departmentCodes);
			for(String departmentCode : departmentCodes)
			{
                log.debug("going to get Employee List for Department Code :{} ",departmentCode);
				List<Employee> employeeList = employeeService.getEmployeesByDepartmentCode(departmentCode,payrollRun.getId(),parts);
				log.debug("Size of Employee List :{} ",employeeList.size());
               if(employeeList!=null)
               {
            	   employeeCodeMap.put(departmentCode,employeeList);
               }
               else
               {
            	   log.debug("No Employee Found For Department Code :{}  ",departmentCode);
               }
			}
		}
		catch(Exception e)
		{
			log.error("Error Inside @class OracleIntegrationServiceImpl @method setEmployeeCodeMapping :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}
	private BigDecimal addDataForBasicSalaryInGlAccural(PayrollRun payrollRun,MasterDataRepository masterDataRepository,List<GLEntries> gLEntriesSet,Map<String,List<Employee>> employeeCodeMap,String[] parts)
	{
		log.info("Inside @class OracleIntegrationServiceImpl @method addDataForBasicSalaryInGlAccural ");
		Double[] accuralSum = new Double[5];
		 Arrays.fill(accuralSum, 0.0); 
		 BigDecimal totalValues = BigDecimal.ZERO;
		 EmployeeSalaryStructureService employeeSalaryStructureService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeSalaryStructureService.class);
		 EmployeeMonthlySalaryService employeeMonthlySalaryService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeMonthlySalaryService.class);
         Map<String ,BigDecimal> deptCodeValueMap = new HashMap<>();
		 try
		{
			 for(Map.Entry<String, List<Employee>> entrySet : employeeCodeMap.entrySet())
			 {
				 log.debug("Inside @addDataForBasicSalaryInGlAccural going to iterate for accural for Code :{} ",entrySet.getKey());
				BigDecimal totalValue = iterateEmployeeForGlAccural(entrySet.getValue(),payrollRun,gLEntriesSet,accuralSum,employeeSalaryStructureService,employeeMonthlySalaryService);
			log.debug("Value of deptCodevalueMap :{} :{} ",entrySet.getKey(),totalValue);
				deptCodeValueMap.put(entrySet.getKey(),totalValue);
				totalValues = totalValues.add(totalValue);
               log.debug("For Code :{} totalValue :{}",entrySet.getKey(),totalValues);
			 }
			 pushTheBasicSalaryDetailsInGlAccural(accuralSum, masterDataRepository, gLEntriesSet,PRConstant.ACCURALS);
			 List<Object[]> listOfObjectAccural = employeeMonthlySalaryService.getSumOfValueOfAccural(payrollRun.getId(), parts);
				log.debug("The size of Object list is of Accural :{}", listOfObjectAccural.size());
			Double value = iterateForListOfObjectAccural(masterDataRepository, gLEntriesSet, listOfObjectAccural);
		    deptCodeValueMap.put("Remaining", BigDecimal.valueOf(value));
		    BigDecimal finalValue = BigDecimal.valueOf(value).add(totalValues);
		    return finalValue;
		}
		catch(Exception e)
		{
			log.error("Error Inside @class OracleIntegrationServiceImpl @method addDataForBasicSalaryInGlAccural :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}
	
	private BigDecimal iterateEmployeeForGlAccural(List<Employee> employeeList,PayrollRun payrollRun,List<GLEntries> gLEntriesSet,Double[] accuralSum
			, EmployeeSalaryStructureService employeeSalaryStructureService,EmployeeMonthlySalaryService employeeMonthlySalaryService  )
	  { 
		 try
		 {
			 BigDecimal totalSum = BigDecimal.ZERO;
	         log.info("inside @class OracleIntegrationServiceImpl @method iterateEmployeeForGlAccural");
	         for(Employee employee : employeeList)
	         {
	     		EmployeeMonthlySalary employeeMonthlySalary = employeeMonthlySalaryService.getEmployeeMonthlySalaryByEmployeeIdAndPayrollRunId(employee.getId(), payrollRun.getId());
				EmployeeSalaryStructure employeeSalaryStructure = employeeSalaryStructureService.getEmployeeMappedSalaryStructure(employee.getId());
				 Integer totalDays = employeeMonthlySalary.getPayrollRun().getDurationDayCount();
				   log.debug("Inside @class OracleIntegrationServiceImpl @method calculateSalary employeeSalarystructureID :{} EmployeeMonthlySalaryID :{} ",
						   employeeSalaryStructure.getId(),employeeMonthlySalary.getId());
				   Double basicSalary = Optional.ofNullable(employeeSalaryStructure.getBasicSalary()).orElse(0.0);
				   Double hra = Optional.ofNullable(employeeSalaryStructure.getHra()).orElse(0.0);
				   Double ta = Optional.ofNullable(employeeSalaryStructure.getTa()).orElse(0.0);
				   Integer employeeWorkingDay = Optional.ofNullable(employeeMonthlySalary.getWorkingDays()).orElse(0);
				   Double sickUnpaidCount = Optional.ofNullable(employeeMonthlySalary.getSickUnpaidCount()).orElse(0.0);
				   Double sickPaidCount = Optional.ofNullable(employeeMonthlySalary.getSickPaidCount()).orElse(0.0);
				   Double unpaidLeaveCount = (double) Optional.ofNullable(employeeMonthlySalary.getUnpaidLeaveCount()).orElse(0);
				   log.debug("EmployeeId :{} Basic Salary :{} HRA :{} TA :{} ",
						   employeeMonthlySalary.getEmployee().getId(),basicSalary,hra,ta);
				   Double perDayBasicSalary = basicSalary/totalDays;
				   Double perDayHRA = hra/totalDays;
				   Double perDayTA = ta/totalDays;
				   Double unpaidLeaveValue = (double)employeeMonthlySalary.getUnpaidLeaveCount()*perDayBasicSalary;
				   log.debug("EmployeeId :{} Per Day Basic Salary :{} PerDay HRA :{} Per Day TA :{} unpaidLeaveValue:{} ",
						   employeeMonthlySalary.getEmployee().getId(),perDayBasicSalary,perDayHRA,perDayTA,unpaidLeaveValue);
				  
				   Double totalEmployeeDays = (double)employeeWorkingDay + sickUnpaidCount; 
				   log.debug(" EmployeeWorking Days :{} sickUnpaidCount :{} totalEmployeeDays :{} sickPaidCount:{} ",
						   employeeWorkingDay,sickUnpaidCount,totalEmployeeDays,sickPaidCount);
				   
				   Double sickPaidValue = sickPaidCount*perDayBasicSalary;
				   Double sickUnpaidValue = sickUnpaidCount*perDayBasicSalary;
				   log.debug("Value of SickPaidValue :{} sickUnpaidValue :{} ",sickUnpaidValue,sickUnpaidValue);
				   
				   
				   Double correctBasicSalary = perDayBasicSalary*totalEmployeeDays;
				   Double correctHRA = perDayHRA*totalEmployeeDays;
				   Double correctTA = perDayTA*totalEmployeeDays;
				   log.debug(" Correct Value of Basic Salary :{} HRA :{} TA :{} for ID:{}  ",correctBasicSalary,correctHRA,correctTA,employeeMonthlySalary.getEmployee().getId());
				   Double annualLeaveValue = 0.0;
				   if(payrollRun.getWorkflowStage().equalsIgnoreCase(PRConstant.APPROVED_BY_FINANCE_CONTROLLER)) {
				   Double annualLeaveConstant = Double.parseDouble(hrmsSystemConfigService.getValue(PRConstant.ANNUAL_LEAVE_CONSTANT));
		         	 annualLeaveValue = annualLeaveConstant*perDayBasicSalary;
					  log.debug(" annualLeaveConstant:{}  annualLeaveValue:{} ",annualLeaveConstant,annualLeaveValue);
				   }
				   else {
					   Integer annualLeaveCount = employeeMonthlySalary.getAnnualLeaveCount();
					   annualLeaveValue = (double)perDayBasicSalary*annualLeaveCount;
					   log.debug(" Value of annualLeaveCount :{} annualLeaveValue :{} ",annualLeaveCount,annualLeaveValue);
				   }
	

				  
				accuralSum[0] = correctBasicSalary+accuralSum[0];
	    				  accuralSum[1] = correctHRA+accuralSum[1];
	    				  accuralSum[2] = correctTA+accuralSum[2];
	    				  accuralSum[3] = sickPaidValue + accuralSum[3];
	    				  accuralSum[4] = annualLeaveValue+accuralSum[4];
	    				  
                    log.debug("Value of Accural is :{} ",Arrays.toString(accuralSum));
                    totalSum = totalSum.add(BigDecimal.valueOf(accuralSum[0]))
                            .add(BigDecimal.valueOf(accuralSum[1]))
                            .add(BigDecimal.valueOf(accuralSum[2]))
                            .add(BigDecimal.valueOf(accuralSum[3]))
                            .add(BigDecimal.valueOf(accuralSum[4]));

	         }
	         log.debug("Value of total Sum :{} ",totalSum);
	         return totalSum;
		 }
		 catch(Exception e)
		 {
			 log.error("Error inside @class OracleIntegrationServiceImpl @method iterateEmployeeForGlAccural :{} :{}",e.getMessage(),Utils.getStackTrace(e));
			 throw new BusinessException();
		 }
	 }
	
	    
}
