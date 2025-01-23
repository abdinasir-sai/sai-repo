package com.nouros.hrms.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.commons.configuration.ConfigUtils;
import com.enttribe.connectx.dto.ResponseDto;
import com.enttribe.connectx.meeting.controller.MeetingController;
import com.enttribe.connectx.meeting.dto.AvailableMeetingSlotsDto;
import com.enttribe.connectx.meeting.dto.ScheduleEventRequestDto;
import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.execution.controller.IWorkorderController;
import com.enttribe.orchestrator.execution.model.Workorder;
import com.enttribe.orchestrator.utility.annotation.TriggerBpmnAspect;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.product.pii.filter.PropertyFilter;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.usermanagement.user.model.User;
import com.enttribe.usermanagement.user.rest.UserRest;
import com.enttribe.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.nouros.hrms.integration.service.VectorIntegrationService;
import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.Interview;
import com.nouros.hrms.model.InterviewFeedback;
import com.nouros.hrms.model.JobApplication;
import com.nouros.hrms.model.JobOpening;
import com.nouros.hrms.repository.ApplicantRepository;
import com.nouros.hrms.repository.EmployeeRepository;
import com.nouros.hrms.repository.InterviewFeedbackRepository;
import com.nouros.hrms.repository.InterviewRepository;
import com.nouros.hrms.repository.JobApplicationRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.InterviewFeedbackService;
import com.nouros.hrms.service.InterviewService;
import com.nouros.hrms.service.ReminderService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.AvailableSlotDto;
import com.nouros.hrms.wrapper.InterviewAndFeedbackWrapper;
import com.nouros.hrms.wrapper.InterviewDto;
import com.nouros.hrms.wrapper.InterviewMeetingWrapper;
import com.nouros.hrms.wrapper.InterviewWrapper;
import com.nouros.hrms.wrapper.ParticipantDetailWrapper;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;
import com.nouros.payrollmanagement.utils.PRConstant;

/**
 * This is a class named "InterviewServiceImpl" which is located in the package
 * " com.nouros.hrms.service.impl", It appears to be an implementation of the
 * "InterviewService" interface and it extends the "AbstractService" class,
 * which seems to be a generic class for handling CRUD operations for entities.
 * This class is annotated with @Service, indicating that it is a Spring Service
 * bean. This class is using Lombok's @Slf4j annotation which will automatically
 * generate an Slf4j based logger instance, so it is using the Slf4j API for
 * logging. The class has a constructor which takes a single parameter of
 * GenericRepository Interview and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of Interview
 * Interview) for exporting the Interview data into excel file by reading the
 * template and mapping the Interview details into it. It's using Apache POI
 * library for reading and writing excel files, and has methods for parsing the
 * json files for column names and identities , and it also used 'ExcelUtils'
 * for handling the excel operations. It also uses 'ApplicationContextProvider'
 * from 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class InterviewServiceImpl extends AbstractService<Integer, Interview> implements InterviewService {

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   Interview entities.
	 */

	private static final Logger log = LogManager.getLogger(InterviewServiceImpl.class);

	private static final String IS_INTERVIEW_LINK_NEEDED = "IS_INTERVIEW_LINK_NEEDED";
	public static final String INSIDE_METHOD = "Inside @method {}";
	public static final String INSIDE_METHOD_ONE_PARAMETER = "Inside @method {} {}";
	public static final String SOMETHING_WENT_WRONG = "Something Went Wrong : {} ";
	public static final String AVAILABLE_SLOTS = "availableSlots";
	public static final String ENABLE_TIMESLOT = "ENABLE_TIMESLOT";
	public static final String ACCEPT = "Accept";
	public static final String REJECT = "Reject";
	public static final String NO_RESPONSE = "No response";
	public static final String REJECTED = "Rejected";
	public static final String INTERVIEW_PROPOSED = "Interview Proposed";
	public static final String POSTING_TITLE = "PostingTitle";
	public static final String APPLICANT_FIRST_ROUND_NOT_CLEARED="Applicant's First Round Not Cleared";
	private static ObjectMapper objectMapper = null;
	public InterviewServiceImpl(GenericRepository<Interview> repository) {
		super(repository, Interview.class);
	}

	@Autowired
	private InterviewRepository interviewRepository;

	@Autowired
	private VectorIntegrationService vectorIntegrationService;

	@Autowired
	private MeetingController meetingController;

	@Autowired
	IWorkorderController workorderController;

	@Autowired
	WorkflowActionsController workflowActionsController;

	@Autowired
	public HrmsSystemConfigService hrmsSystemConfigService;

	@Autowired
	private CommonUtils commonUtils;

	@Autowired
	private JobApplicationRepository jobApplicationRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ApplicantRepository applicantRepository;

	@Autowired
	private UserRest userRest;

	@Autowired
	CustomerInfo customerInfo;

	private User getUserContext() {
		return userRest.byUserName(customerInfo.getUsername());
	}

	/**
	 * Creates a new vendor.
	 *
	 * @param interview The interview object to create.
	 * @return The created vendor object.
	 */
	@Override
	public Interview create(Interview interview) {
		log.info("Inside class@ interviewserviceimpl method@ create");

		try {

			CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
			Integer workspaceId = customerInfo.getActiveUserSpaceId();
			interview.setWorkspaceId(workspaceId); // done done
//		try {
//			String interviewLink = meetingCreationForInterview(interview);
//			log.debug("interview link  is : {}", interviewLink);
//			JSONObject jsonObject = new JSONObject(interviewLink);
//			String password = jsonObject.getString("password");
//			String meetingUrl = jsonObject.getString("meetingUrl");
//			String formattedInterviewLink = "Meeting URL : click <a href=\"" + meetingUrl + "\">here</a> , Password : "
//					+ password;
//			interview.setInterviewJoinLink(formattedInterviewLink);
//		} catch (BusinessException be) {
//			log.error("Caught error for setting interview link : {} ", be.getMessage());
//		} catch (Exception e) {
//			log.error(SOMETHING_WENT_WRONG, e);
//		}
			log.debug("Inside method@ create going to save interview", interview);
			Interview createdInterview = interviewRepository.save(interview);
//		try {
//			createInterviewFeedback(createdInterview);
//		} catch (BusinessException be) {
//			log.error("Caught error for setting Interview Feedback Questions : {} ", be.getMessage());
//		} catch (Exception e) {
//			log.error(SOMETHING_WENT_WRONG, e);
//		}
			return createdInterview;

		} catch (BusinessException ex) {
			log.error("Error while creating interview");
			throw new BusinessException("Error while creating interview", ex.getMessage());
		}
	}

	@Override
	public Interview update(Interview interview) {
		log.info("Inside class@ interviewserviceimpl method@ update");
		Interview updatedInterview = interviewRepository.save(interview);

		return updatedInterview;
	}

	private void createInterviewFeedback(Interview createdInterview) {
		log.info(INSIDE_METHOD, "createInterviewFeedback");
		JSONObject questionJson = fetchJsonOfQuestions();
		InterviewFeedbackService interviewFeedbackService = ApplicationContextProvider.getApplicationContext()
				.getBean(InterviewFeedbackService.class);
		for (String key : questionJson.keySet()) {
			InterviewFeedback interviewFeedback = new InterviewFeedback();
			interviewFeedback.setInterview(createdInterview);
			interviewFeedback.setQuestion(key + questionJson.getString(key));
			if (questionJson.getString(key) != null) {
				String response = questionJson.getString(key);
				log.debug("Question to be set is key,questionJson : {} {}", key, response);
			}
			interviewFeedbackService.create(interviewFeedback);
		}
	}

	private JSONObject fetchJsonOfQuestions() {
		log.info(INSIDE_METHOD, "fetchJsonOfQuestions");
		JSONObject questionData = new JSONObject();
		questionData.put("Educational Background",
				" - Does the candidate have the appropriate educational qualifications or training for this position?");
		questionData.put("Prior Work Experience",
				" - Has the candidate acquired similar skills or qualifications through past work experiences?");
		questionData.put("Technical Qualifications/Experience",
				" - Does the candidate have the technical skills necessary for this position?");
		questionData.put("Verbal Communication",
				" - How were the candidate’s communication skills during the interview?");
		questionData.put("Candidate Interest",
				" - How much interest did the candidate show in the position and the organization?");
		questionData.put("Knowledge of Organization",
				" - Did the candidate research the organization prior to the interview?");
		questionData.put("Teambuilding/Interpersonal Skills",
				" - Did the candidate demonstrate, through their answers, good teambuilding/interpersonal skills?");
		questionData.put("Initiative",
				" - Did the candidate demonstrate, through their answers, a high degree of initiative?");
		questionData.put("Time Management",
				" - Did the candidate demonstrate, through their answers, good time management skills?");
		questionData.put("Customer Service",
				" - Did the candidate demonstrate, through their answers, a high level of customer service skills/abilities?");
		log.debug("QuestionData Object is : {} ", questionData);
		return questionData;
	}

	@Override
	public String meetingCreationForInterview(Interview interview) {
		log.info(INSIDE_METHOD, "meetingCreationForInterview");
		String response = "";
		log.debug("Interview Object is : {} ", interview);
		InterviewMeetingWrapper meetingWrapper = new InterviewMeetingWrapper();
		meetingWrapper.setMeetingDescription(interview.getMeetingDescription());
		meetingWrapper.setMeetingName(interview.getName());
		if (interview.getEmployee().getUserId() != null) {
			meetingWrapper.setModerator(interview.getEmployee().getUserId().toString());
		}
		meetingWrapper.setStartTime(interview.getFromDate());
		String minuteDifference = calculateMinuteDifference(interview.getFromDate(), interview.getToDate());
		meetingWrapper.setDuration(minuteDifference);
		List<ParticipantDetailWrapper> participantDetailsList = setDataOfParticipants(interview);
		log.debug("participant List is {} : ", participantDetailsList.size());
		meetingWrapper.setParticipants(participantDetailsList);
		if (ConfigUtils.getString(IS_INTERVIEW_LINK_NEEDED) != null) {
			String res = ConfigUtils.getString(IS_INTERVIEW_LINK_NEEDED);
			log.debug("Is Interview Link needed values from Config Properties is :{} ", res);
		}
		if (ConfigUtils.getString(IS_INTERVIEW_LINK_NEEDED).equalsIgnoreCase("Enable")) {
			try {
				response = vectorIntegrationService.meetingLinkCreation(meetingWrapper);
				log.debug("Response from  meetingLinkCreation  is : {} ", response);
				return response;
			} catch (BusinessException be) {
				log.error("Caught error for setting interview link : {} ", be.getMessage());
			} catch (Exception e) {
				log.error(SOMETHING_WENT_WRONG, e);
			}
		}
		return response;
	}

	private static String calculateMinuteDifference(Date date1, Date date2) {
		log.info(INSIDE_METHOD, "calculateMinuteDifference");
		Long timeDifferenceInMillis = date2.getTime() - date1.getTime();
		Long minuteDifference = timeDifferenceInMillis / (60 * 1000);
		log.debug(" minuteDifference is : {}", minuteDifference);
		return minuteDifference.toString();
	}

	private List<ParticipantDetailWrapper> setDataOfParticipants(Interview interview) {
		log.info(INSIDE_METHOD, "setDataOfParticipants");
		List<ParticipantDetailWrapper> participantDetailList = new ArrayList<>();
		ParticipantDetailWrapper participantDetail = new ParticipantDetailWrapper();
		if (interview.getApplicant().getFirstName() != null || interview.getApplicant().getLastName() != null) {
			participantDetail
					.setName(interview.getApplicant().getFirstName() + " " + interview.getApplicant().getLastName());
		} else {
			participantDetail.setName("");
		}
		if (interview.getApplicant().getEmailId() != null) {
			participantDetail.setEmail(interview.getApplicant().getEmailId());
		} else {
			participantDetail.setEmail("");
		}
		participantDetailList.add(participantDetail);
		return participantDetailList;
	}

	@Override
	public Interview findByProcessInstanceId(String processInstanceId) {
		try {
			log.debug(" Inside @findByProcessInstanceId  customerId is : {}", commonUtils.getCustomerId());
			return interviewRepository.findByProcessInstanceId(processInstanceId);
		} catch (Exception e) {
			throw new BusinessException("No interview found for give processInstanceId " + processInstanceId);
		}
	}

	@Override
	public InterviewAndFeedbackWrapper findInterviewDetails(Integer applicantId, Integer jobOpeningId) {
		log.info(INSIDE_METHOD, "findInterviewDetails");
		Interview interview = new Interview();
		InterviewAndFeedbackWrapper interviewAndFeedbackWrapper = new InterviewAndFeedbackWrapper();
		try {
			log.debug(" Inside @findInterviewDetails  customerId is : {}", commonUtils.getCustomerId());
			interview = interviewRepository.findInterviewByApplicantAndJobOpening(applicantId, jobOpeningId);
			interviewAndFeedbackWrapper.setInterview(interview);
		} catch (Exception e) {
			throw new BusinessException(
					"No Interview Found for given applicantId and JobOpeningId : {} {} " + applicantId + jobOpeningId);
		}
		try {
			InterviewFeedbackRepository interviewFeedbackRepository = ApplicationContextProvider.getApplicationContext()
					.getBean(InterviewFeedbackRepository.class);
			interviewAndFeedbackWrapper
					.setInterviewFeedbacks(interviewFeedbackRepository.findAllByInterviewId(interview.getId()));
		} catch (Exception e) {
			throw new BusinessException("No InterviewFeedback Found for given InterviewId : {} " + interview.getId());
		}
		return interviewAndFeedbackWrapper;
	}

	@Override
	public InterviewAndFeedbackWrapper updateInterviewFeedback(
			InterviewAndFeedbackWrapper interviewAndFeedbackWrapper) {
		log.info(INSIDE_METHOD, "updateInterviewFeedback");
		InterviewAndFeedbackWrapper updatedInterviewAndFeedbackWrapper = new InterviewAndFeedbackWrapper();
		List<InterviewFeedback> updatedInterviewFeedbackList = new ArrayList<>();
		List<InterviewFeedback> interviewFeedbackList = interviewAndFeedbackWrapper.getInterviewFeedbacks();
		try {
			InterviewFeedbackRepository interviewFeedbackRepository = ApplicationContextProvider.getApplicationContext()
					.getBean(InterviewFeedbackRepository.class);
			for (InterviewFeedback InterviewFeedbacks : interviewFeedbackList) {
				InterviewFeedback updatedInterviewFeedback = interviewFeedbackRepository.save(InterviewFeedbacks);
				updatedInterviewFeedbackList.add(updatedInterviewFeedback);
				updateReminderTask(InterviewFeedbacks);
				updateJobApplicationStatus(interviewAndFeedbackWrapper.getInterview().getJobApplication());
			}
			log.debug("updatedInterviewFeedbackList is : {} ", updatedInterviewFeedbackList);
			updatedInterviewAndFeedbackWrapper.setInterview(interviewAndFeedbackWrapper.getInterview());
			updatedInterviewAndFeedbackWrapper.setInterviewFeedbacks(updatedInterviewFeedbackList);
			return updatedInterviewAndFeedbackWrapper;
		} catch (Exception e) {
			throw new BusinessException("No InterviewFeedback Found for given Interview : {}  "
					+ interviewAndFeedbackWrapper.getInterview().getId());
		}

	}

	private void updateReminderTask(InterviewFeedback interviewFeedbacks) {
		ReminderService reminderService = ApplicationContextProvider.getApplicationContext()
				.getBean(ReminderService.class);
		Applicant applicant = interviewFeedbacks.getInterview().getApplicant();
		JobOpening jobOpening = interviewFeedbacks.getInterview().getJobOpening();
		String name = applicant.getFirstName() + " " + applicant.getLastName();
		String salutation = "-";
		if (null != applicant.getGender()) {
			if (applicant.getGender().equalsIgnoreCase("Male")) {
				salutation = "him";
			} else if (applicant.getGender().equalsIgnoreCase("Female")) {
				salutation = "her";
			}
		}
		reminderService.reviewCandidateDecideYouWantToHire(applicant.getId(), jobOpening.getId(), salutation, name);
	}

	private void updateJobApplicationStatus(JobApplication jobApplication) {
		log.info(INSIDE_METHOD, "updateJobApplicationStatus");
		jobApplication.setApplicationStatus("Offer planned");
		JobApplicationRepository jobApplicationRepository = ApplicationContextProvider.getApplicationContext()
				.getBean(JobApplicationRepository.class);
		jobApplicationRepository.save(jobApplication);

	}

	@Override
	public ResponseDto getAvailableSlotsAndConflict(AvailableMeetingSlotsDto availableMeetingSlotsDto) {
		log.info(INSIDE_METHOD, "getAvailableSlotsAndConflict");

		try {
			return meetingController.getAvailableSlotsAndConflict(availableMeetingSlotsDto);

		} catch (BusinessException ex) {
			log.error("error inside @class interviewServiceImplmethod @getAvailableSlotsAndConflict");
			throw new BusinessException("error inside @class interviewServiceImplmethod @getAvailableSlotsAndConflict");
		}
	}

	@Override
	public Interview updateInterviewWorkflowStage(InterviewDto interviewDto) {

		log.debug("Inside method updateInterviewWorkflowStage interviewId : {}", interviewDto.getInterviewId());
		try {
			if (interviewDto.getInterviewId() != null) {
				Interview optionalInterview = super.findById(interviewDto.getInterviewId());
				if (optionalInterview != null) {
					Interview interview = optionalInterview;
					if (interviewDto.getWorkflowStage().equalsIgnoreCase("CANCELLED") && interview != null
							&& interview.getProcessInstanceId() != null) {
						log.debug("Inside method updateInterviewWorkflowStage ProcessInstanceId found is  : {}",
								interview.getProcessInstanceId());
						Workorder wordorder = workorderController
								.findByProcessInstanceId(interview.getProcessInstanceId());
						log.debug("Inside method updateInterviewWorkflowStage wordorder found is  : {}", wordorder);
						if (wordorder != null && wordorder.getReferenceId() != null) {
							try {
								String response = workorderController.deleteWorkorder(wordorder.getReferenceId());
								log.debug(
										"Inside method updateInterviewWorkflowStage response from  deleteWorkorder api is : {}",
										response);
								workflowActionsController.notifyActions(interview.getProcessInstanceId());
							} catch (Exception e) {
								log.error("Facing error While deleting Workorder");
							}
						}
					}
					if (interview != null) {
						interview.setWorkflowStage(interviewDto.getWorkflowStage());
					}
					return interviewRepository.save(interview);
				} else {
					throw new BusinessException("Interview with ID " + interviewDto.getInterviewId() + " not found");
				}
			}
		} catch (Exception e) {
			throw new BusinessException("error while updating Interview WorkflowStage", e.getMessage());
		}
		return null;
	}

	@Override
	public String updateInterviewTimeSlot(String processInstanceId, InterviewWrapper interviewWrapper) {
		log.debug("Inside method @updateInterviewTimeSlot InterviewWrapper is : {} ", interviewWrapper);

		try {

			Interview updatedInterview = null;
			Employee employee = null;
			Employee interviewerRecruiter = null;
			Employee interviewerManager = null;
			Interview interview = null;
			String res = null;
			if (ConfigUtils.getString(ENABLE_TIMESLOT) != null) {
				res = ConfigUtils.getString(ENABLE_TIMESLOT);
				log.debug("Is Time Slot to be enable , from Config Properties is :{} ", res);
			}

			if (processInstanceId != null) {
				log.debug("ProcessInstanceId is : {}", processInstanceId);
				log.debug(" Inside @updateInterviewTimeSlot  customerId is : {}", commonUtils.getCustomerId());
				interview = interviewRepository.findByProcessInstanceId(processInstanceId);
				log.debug("Interview By ProcessInstanceId is : {}", interview);
				if (interview == null) {
					log.debug("interviewWrapper is : {}", interviewWrapper.getInterviewId());
					interview = interviewRepository.findById(interviewWrapper.getInterviewId()).orElse(null);
					log.debug("Interview By Id is  : {}", interview);

				}
			}

			log.debug("Interview Object Found is  : {} ", interview);

			if (interview != null && interview.getApplicantScheduledCount() != null
					&& interview.getApplicantScheduledCount() > 1
					&& !hrmsSystemConfigService.getValue(PRConstant.ORGANISATION_NAME).equalsIgnoreCase("SAI")) {
				log.error("Applicant Already Rescheduled the Interview");
				throw new BusinessException("Applicant Already Rescheduled Interview Once, Cannot Reschedule Again");
			} else if (interview != null && interview.getApplicantScheduledCount() != null
					&& interview.getApplicantScheduledCount() > 3
					&& hrmsSystemConfigService.getValue(PRConstant.ORGANISATION_NAME).equalsIgnoreCase("SAI")) {
				log.error("Applicant Already Rescheduled the Interview for SAI");
				throw new BusinessException("Applicant Already Rescheduled Interview Once, Cannot Reschedule Again");
			} else if (interview != null && interview.getInterviewerScheduledCount() != null
					&& interview.getInterviewerScheduledCount() > 3) {
				log.error("Interviewer Already Rescheduled the Interview Thrice");
				throw new BusinessException(
						"Interviewer Already Rescheduled Interview Thrice, Cannot Reschedule Again");
			} else if (interview != null && interview.getInterviewerRecruiterCount() != null
					&& interview.getInterviewerRecruiterCount() > 3) {
				log.error("Recruiter Already Rescheduled the Interview Thrice");
				throw new BusinessException("Recruiter Already Rescheduled Interview Thrice, Cannot Reschedule Again");
			} else if (interview != null && interview.getInterviewerManagerCount() != null
					&& interview.getInterviewerManagerCount() > 3) {
				log.error("Manager Already Rescheduled the Interview thrice");
				throw new BusinessException("Manager Already Rescheduled Interview Thrice, Cannot Reschedule Again");
			}
			if (interview != null && interview.getWorkflowStage() != null
					&& interview.getWorkflowStage().equalsIgnoreCase(REJECTED)) {
				log.debug("timeslot to be set in case of  Rejected  : {} ", interview);

				fetchAndSetTimeSlots(employee, interviewerRecruiter, interviewerManager, interview, res);
				interview.setInterviewerAccept(NO_RESPONSE);
				interview.setInterviewerManagerAccept(NO_RESPONSE);
				interview.setInterviewerRecruiterAccept(NO_RESPONSE);

				updatedInterview = interviewRepository.save(interview);
				log.debug("Interview updated object in case of Rejected is  : {} ", updatedInterview);
				return APIConstants.SUCCESS_JSON;

			} else if (interview != null) {
				log.debug("timeslot to be set in case of  else : {} ", interview);
				fetchAndSetTimeSlots(employee, interviewerRecruiter, interviewerManager, interview, res);

				updatedInterview = interviewRepository.save(interview);

				log.debug("Interview updated object in case of else is  : {} ", updatedInterview);
				return APIConstants.SUCCESS_JSON;

			}

			return APIConstants.FAILURE_JSON;

		} catch (BusinessException ex) {

			throw new BusinessException("Error inside method updateInterviewTimeSlot ");
		}

	}

	private void fetchAndSetTimeSlots(Employee employee, Employee interviewerRecruiter, Employee interviewerManager,
			Interview interview, String res) {
		Map<String, String> hrmsSystemConfigMap = hrmsSystemConfigService.getHrmsKeyValue();
		String mappedInterviewStartTime = hrmsSystemConfigMap.get(PRConstant.INTERVIEW_START_TIME);
		String mappedInterviewEndTime = hrmsSystemConfigMap.get(PRConstant.INTERVIEW_END_TIME);
		log.debug("Inside fetchAndSetTimeSlots mappedInterviewStartTime:{}, mappedInterviewEndTime:{} ",
				mappedInterviewStartTime, mappedInterviewEndTime);
		Integer mappedInterviewStartTime1 = Integer.parseInt(mappedInterviewStartTime);
		Integer mappedInterviewEndTime1 = Integer.parseInt(mappedInterviewEndTime);
		log.debug("Inside fetchAndSetTimeSlots mappedInterviewStartTime1:{}, mappedInterviewEndTime1:{} ",
				mappedInterviewStartTime1, mappedInterviewEndTime1);

		Date interviewStartTime = interview.getInterviewStartTime();
		Date interviewEndTime = interview.getInterviewEndTime();
		String employeeEmail = "";
		String interviewerRecruiterEmail = "";
		String interviewerManagerEmail = "";
		Date newInterviewStartTime = null;
		Date newInterviewEndTime = null;

		log.debug("Interview  interviewStartTime and  interviewEndTime is : {},{} ", interviewStartTime,
				interviewEndTime);

		if (interview.getEmployee() != null && interview.getEmployee().getWorkEmailAddress() != null) {
			employeeEmail = interview.getEmployee().getWorkEmailAddress();

		}
		if (interview.getInterviewerRecruiter() != null
				&& interview.getInterviewerRecruiter().getWorkEmailAddress() != null) {
			interviewerRecruiterEmail = interview.getInterviewerRecruiter().getWorkEmailAddress();
		}
		if (interview.getInterviewerManager() != null
				&& interview.getInterviewerManager().getWorkEmailAddress() != null) {
			interviewerManagerEmail = interview.getInterviewerManager().getWorkEmailAddress();
		}
		log.debug("Employee objects are employee : {}  ,interviewerRecruiter : {} ,and interviewerManager : {}  ",
				employee, interviewerRecruiter, interviewerManager);
		// Add one day to interviewStartTime

		if (interview.getWorkflowStage().equalsIgnoreCase(REJECTED)) {

			log.debug("Interview  interviewStartTime and  interviewEndTime is : {},{} ", interviewStartTime,
					interviewEndTime);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(interviewStartTime);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			newInterviewStartTime = calendar.getTime();

			// Add one day to interviewEndTime
			calendar.setTime(interviewEndTime);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			newInterviewEndTime = calendar.getTime();

			if (res.equalsIgnoreCase("false")) {

				calendar.setTime(newInterviewStartTime);
				calendar.set(Calendar.HOUR_OF_DAY, mappedInterviewStartTime1);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				newInterviewStartTime = calendar.getTime();

				// Set end time to 11:00
				calendar.setTime(newInterviewEndTime);
				calendar.set(Calendar.HOUR_OF_DAY, mappedInterviewEndTime1);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				newInterviewEndTime = calendar.getTime();

				log.debug("Interview in case of Rejection newInterviewStartTime and  newInterviewEndTime is : {},{} ",
						newInterviewStartTime, newInterviewEndTime);

				interview.setInterviewStartTime(newInterviewStartTime);
				interview.setInterviewEndTime(newInterviewEndTime);

			}

		} else {

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_MONTH, 2);
			newInterviewStartTime = calendar.getTime();

			// Add one day to interviewEndTime
			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_MONTH, 2);
			newInterviewEndTime = calendar.getTime();

			if (res.equalsIgnoreCase("false")) {

				calendar.setTime(newInterviewStartTime);
				calendar.set(Calendar.HOUR_OF_DAY, mappedInterviewStartTime1);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				newInterviewStartTime = calendar.getTime();

				// Set end time to 11:00
				calendar.setTime(newInterviewEndTime);
				calendar.set(Calendar.HOUR_OF_DAY, mappedInterviewEndTime1);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				newInterviewEndTime = calendar.getTime();

				log.debug(
						"Interview in case of else condition newInterviewStartTime and  newInterviewEndTime is : {},{} ",
						newInterviewStartTime, newInterviewEndTime);

				interview.setInterviewStartTime(newInterviewStartTime);
				interview.setInterviewEndTime(newInterviewEndTime);

			}

		}

		log.debug("Interview  newInterviewStartTime : {}  , newInterviewEndTime is : {} ", newInterviewStartTime,
				newInterviewEndTime);

		if (res.equalsIgnoreCase("true")) {

			AvailableMeetingSlotsDto availableMeetingSlotsDto = new AvailableMeetingSlotsDto();

			if (newInterviewStartTime != null) {
				availableMeetingSlotsDto.setStartTime(newInterviewStartTime.toString());
			}
			if (newInterviewEndTime != null) {
				availableMeetingSlotsDto.setEndTime(newInterviewEndTime.toString());
			}
			availableMeetingSlotsDto.setUserGroup(Collections.emptyList());

			List<String> requiredAttendees = new ArrayList<>();
			requiredAttendees.add(employeeEmail);
			requiredAttendees.add(interviewerRecruiterEmail);
			requiredAttendees.add(interviewerManagerEmail);

			availableMeetingSlotsDto.setRequiredAttendees(requiredAttendees);
			log.debug("availableMeetingSlotsDto fromed is : {} ", availableMeetingSlotsDto);
			ResponseDto responseDto = getAvailableSlotsAndConflict(availableMeetingSlotsDto);

			log.debug("Response formed  is : {} ", responseDto);

			AvailableSlotDto availableSlotDto = getFirstAvailableSlot(responseDto.getResponse());

			if (availableSlotDto.getStartTime() != null) {
				interview.setInterviewStartTime(availableSlotDto.getStartTime());
			}

			if (availableSlotDto.getEndTime() != null) {
				interview.setInterviewEndTime(availableSlotDto.getEndTime());
			}

		}
	}

	@Override
	public Interview updateInterviewScheduleStatus(InterviewWrapper interviewWrapper) {
		log.debug("Inside @updateInterviewScheduleStatus interviewId : {} , emailId : {} , task : {} ",
				interviewWrapper.getInterviewId(), interviewWrapper.getEmailId(), interviewWrapper.getTask());
		if (interviewWrapper.getInterviewId() != null && interviewWrapper.getEmailId() != null
				&& interviewWrapper.getTask() != null) {

			Interview oldInterviewObject = interviewRepository.findById(interviewWrapper.getInterviewId()).orElse(null);

			if (oldInterviewObject == null) {
				log.error("Inside @updateInterviewScheduleStatus , Interview Object not found  ");
				throw new BusinessException("Interview Not found by ID");
			}
			String emailIdFromInterviewWrapper = interviewWrapper.getEmailId();

			Interview newInterviewObject = null;

			if (interviewWrapper.getTask().equalsIgnoreCase("accept")) {
				log.info("Inside @updateInterviewScheduleStatus In case of accept");
				newInterviewObject = acceptSlot(emailIdFromInterviewWrapper, oldInterviewObject);
			} else if (interviewWrapper.getTask().equalsIgnoreCase("reject")) {
				log.info("Inside @updateInterviewScheduleStatus In case of reject");
				newInterviewObject = rejectSlot(emailIdFromInterviewWrapper, oldInterviewObject);
			} else {
				log.error("Inside @updateInterviewScheduleStatus  task passed is wrong : {}",
						interviewWrapper.getTask());
				throw new BusinessException("Invalid Task");
			}

			log.debug("Inside @updateInterviewScheduleStatus newInterviewObject is : {}", newInterviewObject);

			return newInterviewObject;

		} else {

			log.error("Inside @updateInterviewScheduleStatus  interviewWrapper passed wrong ");
			throw new BusinessException("interviewWrapper passed wrong , please Verify Data");
		}
	}

	public Interview acceptSlot(String emailId, Interview oldInterviewObject) {
		log.info("Inside @acceptSlot");

		// Validate input parameters
		if (emailId == null || oldInterviewObject == null) {
			log.error("EmailId or Interview object is null");
			throw new BusinessException("Invalid input parameters - emailId or interview object is null");
		}

		// Check interviewer manager
		if (oldInterviewObject.getInterviewerManager() != null
				&& oldInterviewObject.getInterviewerManager().getWorkEmailAddress() != null
				&& emailId.equalsIgnoreCase(oldInterviewObject.getInterviewerManager().getWorkEmailAddress())) {
			oldInterviewObject.setInterviewerManagerAccept(ACCEPT);
		}
		// Check interviewer recruiter
		else if (oldInterviewObject.getInterviewerRecruiter() != null
				&& oldInterviewObject.getInterviewerRecruiter().getWorkEmailAddress() != null
				&& emailId.equalsIgnoreCase(oldInterviewObject.getInterviewerRecruiter().getWorkEmailAddress())) {
			oldInterviewObject.setInterviewerRecruiterAccept(ACCEPT);
		}
		// Check employee
		else if (oldInterviewObject.getEmployee() != null
				&& oldInterviewObject.getEmployee().getWorkEmailAddress() != null
				&& emailId.equalsIgnoreCase(oldInterviewObject.getEmployee().getWorkEmailAddress())) {
			oldInterviewObject.setInterviewerAccept(ACCEPT);
		}
		return interviewRepository.save(oldInterviewObject);
	}

	public Interview rejectSlot(String emailId, Interview oldInterviewObject) {
		log.info("Inside @rejectSlot");

		// Validate input parameters
		if (emailId == null || oldInterviewObject == null) {
			log.error("EmailId or Interview object is null");
			throw new BusinessException("Invalid input parameters - emailId or interview object is null");
		}

		// Check interviewer manager
		if (oldInterviewObject.getInterviewerManager() != null
				&& oldInterviewObject.getInterviewerManager().getWorkEmailAddress() != null
				&& emailId.equalsIgnoreCase(oldInterviewObject.getInterviewerManager().getWorkEmailAddress())) {
			oldInterviewObject.setInterviewerManagerAccept(REJECT);
			Integer managerCount = oldInterviewObject.getInterviewerManagerCount() + 1;
			log.debug("Inside @rejectSlot managerCount is : {}", managerCount);
			oldInterviewObject.setInterviewerManagerCount(managerCount);
		}
		// Check interviewer recruiter
		else if (oldInterviewObject.getInterviewerRecruiter() != null
				&& oldInterviewObject.getInterviewerRecruiter().getWorkEmailAddress() != null
				&& emailId.equalsIgnoreCase(oldInterviewObject.getInterviewerRecruiter().getWorkEmailAddress())) {
			oldInterviewObject.setInterviewerRecruiterAccept(REJECT);
			Integer recruiterCount = oldInterviewObject.getInterviewerRecruiterCount() + 1;
			log.debug("Inside @rejectSlot recruiterCount is : {}", recruiterCount);
			oldInterviewObject.setInterviewerRecruiterCount(recruiterCount);
		}
		// Check employee
		else if (oldInterviewObject.getEmployee() != null
				&& oldInterviewObject.getEmployee().getWorkEmailAddress() != null
				&& emailId.equalsIgnoreCase(oldInterviewObject.getEmployee().getWorkEmailAddress())) {
			oldInterviewObject.setInterviewerAccept(REJECT);
			Integer interviewerCount = oldInterviewObject.getInterviewerScheduledCount() + 1;
			log.debug("Inside @rejectSlot interviewerCount is : {}", interviewerCount);
			oldInterviewObject.setInterviewerScheduledCount(interviewerCount); // Fixed: was setting wrong counter
		}
		// Check applicant
		else if (oldInterviewObject.getApplicant() != null && oldInterviewObject.getApplicant().getEmailId() != null
				&& emailId.equalsIgnoreCase(oldInterviewObject.getApplicant().getEmailId())) {
			Integer applicantScheduleCount = oldInterviewObject.getApplicantScheduledCount() + 1;
			log.debug("Inside @rejectSlot ApplicantScheduledCount is : {}", applicantScheduleCount);
			oldInterviewObject.setApplicantScheduledCount(applicantScheduleCount);
		}
		oldInterviewObject.setWorkflowStage(REJECTED);
		return interviewRepository.save(oldInterviewObject);
	}

	public AvailableSlotDto getFirstAvailableSlot(String responseJson) {
		log.debug("Inside getFirstAvailableSlot responseJson:{}", responseJson);
		if (responseJson == null || responseJson.isEmpty()) {
			log.error("Response JSON is null or empty.");
			return null;
		}
		AvailableSlotDto availableSlotDto = new AvailableSlotDto();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(responseJson);
			log.debug("Inside getFirstAvailableSlot read tree rootNode:{} ", rootNode);
			if (rootNode.has(AVAILABLE_SLOTS) && rootNode.get(AVAILABLE_SLOTS).size() > 0) {
				JsonNode firstSlot = rootNode.get(AVAILABLE_SLOTS).get(0);
				log.debug("Inside getFirstAvailableSlot firstSlot:{}", firstSlot);
				String startTimeString = firstSlot.get("startTime").asText();
				String endTimeString = firstSlot.get("endTime").asText();
				log.debug("Inside getFirstAvailableSlot startTimeString:{} ,endTimeString:{} ", startTimeString,
						endTimeString);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				Date startTime = sdf.parse(startTimeString);
				Date endTime = sdf.parse(endTimeString);
				log.debug("Inside getFirstAvailableSlot startTime:{} ,endTime:{} ", startTime, endTime);
				availableSlotDto.setStartTime(startTime);
				availableSlotDto.setEndTime(endTime);
				log.debug("Inside getFirstAvailableSlot set starttime:{}, endtime:{}", availableSlotDto.getStartTime(),
						availableSlotDto.getEndTime());
				return availableSlotDto;
			}
		} catch (Exception e) {
			log.error("Error parsing response JSON or extracting available slots: ", e);
		}
		return availableSlotDto;
	}

	@Override
	public Interview interviewCreateForFirstRound(Integer jobAppId, String jobApplicationId) {
		log.debug("Inside interviewCreateForFirstRound jobAppId :{} , jobApplicationId : {} ", jobAppId,
				jobApplicationId);

		try {

			Interview interview = new Interview();
			JobApplication jobApplication = null;

			if (jobAppId != null) {
				jobApplication = jobApplicationRepository.findById(jobAppId).orElse(null);
				log.debug("Inside interviewCreateForFirstRound jobApplication is  :{}", jobApplication);
			} else if (jobApplication == null) {
				jobApplication = jobApplicationRepository.findJobApplicationsByJobApplicationId(jobApplicationId);
				log.debug("Inside interviewCreateForFirstRound jobApplication by jobApplicationId is :{}",
						jobApplication);
			}

			if (jobApplication != null && jobApplication.getApplicant() != null) {
				jobApplication.setApplicationStatus("Interview scheduled");

				Applicant applicant = jobApplication.getApplicant();

				applicant.setApplicantStatus("Interview scheduled");

				applicantRepository.save(applicant);
				jobApplicationRepository.save(jobApplication);

				log.info("Going to create interview");
				interview.setJobApplication(jobApplication);

				interview.setWorkflowStage(INTERVIEW_PROPOSED);
				jobApplicationRepository.save(jobApplication);
				String designationName = hrmsSystemConfigService.getValue(PRConstant.DESIGNATION_NAME);

				if (designationName != null) {
					List<Employee> employeeList = employeeRepository.findByDesignationName(designationName);
					log.debug("Employee list  is : {}", employeeList);

					if (employeeList != null && !employeeList.isEmpty()) {
						log.debug("Interview Recruiter set is  : {}", employeeList.get(0));
						interview.setInterviewerRecruiter(employeeList.get(0));

					}

				}

				interviewCreateFirstRoundIfJobOpeningNotNull(interview, jobApplication);

				interview.setApplicantScheduledCount(0);
				interview.setInterviewerScheduledCount(0);
				interview.setInterviewerRecruiterCount(0);
				interview.setInterviewerManagerCount(0);
				interview.setInterviewerAccept(NO_RESPONSE);
				interview.setInterviewerManagerAccept(NO_RESPONSE);
				interview.setInterviewerRecruiterAccept(NO_RESPONSE);

			} else {
				log.error("Either JobApplication or Applicant Not Found By Given jobApplicationId");
				throw new BusinessException("Either JobApplication or Applicant Not Found By Given jobApplicationId");
			}

			log.debug("interview save is : {}", interview);
			return interviewRepository.save(interview);

		} catch (BusinessException ex) {
			log.error("Error while creating FirstRound Interview");
			throw new BusinessException("Error while creating FirstRound Interview", ex.getMessage());
		}

	}

	private void interviewCreateFirstRoundIfJobOpeningNotNull(Interview interview, JobApplication jobApplication) {
		log.debug("Inside interviewCreateFirstRoundIfJobOpeningNotNull  ");
		if (jobApplication.getJobOpening() != null) {

			if (jobApplication.getJobOpening().getDepartment() != null) {
				log.debug("Department set is : {}", jobApplication.getJobOpening().getDepartment());
				interview.setDepartment(jobApplication.getJobOpening().getDepartment());
				log.debug("DepartmentName set is : {}",
						jobApplication.getJobOpening().getDepartment().getName());
				interview.setDepartmentName(jobApplication.getJobOpening().getDepartment().getName());
				interview.setName(jobApplication.getFirstName());

			}

			log.debug("Applicant set is : {}", jobApplication.getApplicant());
			interview.setApplicant(jobApplication.getApplicant());

			log.debug("JobOpening set is : {}", jobApplication.getJobOpening());
			interview.setJobOpening(jobApplication.getJobOpening());

			String applicantType = jobApplication.getApplicant().getApplicantType();
			log.debug("ApplicantType is : {}", applicantType);

			if (applicantType.equalsIgnoreCase("Referred")) {
				log.info("Set InterviewManger for Referred case");
				if (jobApplication.getApplicant().getReferedByEmailId() != null
						&& jobApplication.getJobOpening().getHiringManager() != null
						&& jobApplication.getApplicant().getReferedByEmailId().equalsIgnoreCase(
								jobApplication.getJobOpening().getHiringManager().getWorkEmailAddress())) {
					log.info("If Emaild Id is same for Referred case");

					List<Employee> alternateEmployees = employeeRepository
							.findAlternateEmployeesForInterviewWithoutJobLevel(
									jobApplication.getJobOpening().getHiringManager().getDepartment().getName(),
									jobApplication.getJobOpening().getHiringManager().getWorkEmailAddress());

					log.debug("Employee list is : {}", alternateEmployees);
					if (alternateEmployees != null && !alternateEmployees.isEmpty()) {
						log.debug("Employee list is : {}", alternateEmployees);
						interview.setInterviewerManager(alternateEmployees.get(0));
					}

				} else {
					interview.setInterviewerManager(jobApplication.getJobOpening().getHiringManager());
				}
			} else {
				log.debug("InterviewManager set Incase of Not Referred is : {}",
						jobApplication.getJobOpening().getHiringManager());
				interview.setInterviewerManager(jobApplication.getJobOpening().getHiringManager());
			}

		}
	}

	public static String getPreviousJobLevel(String jobLevel) {
		log.debug("Inside getPreviousJobLevel jobLevel is : {} ", jobLevel);
		List<String> jobLevels = Arrays.asList("L1", "L2", "L3", "L4", "L6", "L7");

		int index = jobLevels.indexOf(jobLevel);

		if (index <= 0) {
			return null;
		}

		log.debug("Result is : {} ", jobLevels.get(index - 1));
		return jobLevels.get(index - 1);
	}

	@Override
	public ResponseDto sendMeetingLink(Integer interviewId) {
		log.debug("Inside sendMeetingLink interviewid : {}", interviewId);
		Interview interview = null;

		if (interviewId != null) {

			interview = interviewRepository.findById(interviewId).orElse(null);
			log.debug("Inside sendMeetingLink interview is : {}", interview);
		}

		if (interview == null) {
			log.error("Interview Not Found For Applicant");
			throw new BusinessException("Interview Not Found For Applicant");
		}

		ScheduleEventRequestDto scheduleEventRequestDto = new ScheduleEventRequestDto();

		User contextUser = getUserContext();
		String meetingStartTime = null;
		String meetingEndTime = null;
		List<String> optionalAttendees = new ArrayList<>();

		String subject = hrmsSystemConfigService.getValue(PRConstant.SUBJECT);
		log.debug("subject is : {} ", subject);

		subject = updateSubjectForMeetingLink(interview, subject);
		log.debug("updatedSubject is : {} ", subject);

		String body = hrmsSystemConfigService.getValue(PRConstant.BODY);
		log.debug("body is : {} ", body);

		body = updateBodyForMeetingLink(interview, body);

		log.debug("updatedbody is : {} ", body);

		String timeZone = "Asia/Kolkata";
		String senderEmail = contextUser.getEmail();
		log.debug("senderEmail is : {} ", senderEmail);
		String meetingType = "Teams";

		if (interview.getInterviewStartTime() != null) {
			log.debug("meetingStartTime is : {} ", interview.getInterviewStartTime());
			meetingStartTime = interview.getInterviewStartTime().toString();
		}
		if (interview.getInterviewEndTime() != null) {
			log.debug("meetingEndTime is : {} ", interview.getInterviewEndTime());
			meetingEndTime = interview.getInterviewEndTime().toString();
		}
		if (interview.getApplicant() != null && interview.getApplicant().getEmailId() != null) {
			log.debug("Applicant email is : {} ", interview.getApplicant().getEmailId());
			optionalAttendees.add(interview.getApplicant().getEmailId());
		}

		if (interview.getInterviewerRecruiter() != null
				&& interview.getInterviewerRecruiter().getWorkEmailAddress() != null) {
			log.debug("Employee email is : {} ", interview.getInterviewerRecruiter().getWorkEmailAddress());
			optionalAttendees.add(interview.getInterviewerRecruiter().getWorkEmailAddress());
		}

		if (interview.getInterviewerManager() != null
				&& interview.getInterviewerManager().getWorkEmailAddress() != null) {
			log.debug("Interview Manager is : {} ", interview.getInterviewerManager().getWorkEmailAddress());
			optionalAttendees.add(interview.getInterviewerManager().getWorkEmailAddress());
		}

		scheduleEventRequestDto.setBody(body);
		scheduleEventRequestDto.setSubject(subject);
		scheduleEventRequestDto.setTimeZone(timeZone);
		scheduleEventRequestDto.setMeetingStartTime(meetingStartTime);
		scheduleEventRequestDto.setMeetingEndTime(meetingEndTime);
		scheduleEventRequestDto.setSenderEmail(senderEmail);
		scheduleEventRequestDto.setMeetingType(meetingType);
		scheduleEventRequestDto.setOptionalAttendees(optionalAttendees);

		ResponseDto responseDto = null;
		try {
			responseDto = scheduleEvent(scheduleEventRequestDto);
		} catch (BusinessException ex) {
			log.error("Error Inside Method scheduleEvent");
		}
		log.debug("Result is : {} ", responseDto);

		return responseDto;
	}

	private String updateBodyForMeetingLink(Interview interview, String body) {
		if (body != null && body.contains(POSTING_TITLE) && body.contains("ApplicantName")
				&& interview.getJobOpening() != null && interview.getJobOpening().getPostingTitle() != null) {
			String postingTitle = interview.getJobOpening().getPostingTitle().getName();
			body = body.replace(POSTING_TITLE, postingTitle);

			if (interview.getApplicant() != null && interview.getApplicant().getFullName() != null) {
				String applicantNameUpdated = interview.getApplicant().getFullName();
				body = body.replace("ApplicantName", applicantNameUpdated);
			}

			log.debug("Updated body with PostingTitle and ApplicantName: {}", body);
		}
		return body;
	}

	private String updateSubjectForMeetingLink(Interview interview, String subject) {
		if (subject != null && subject.contains(POSTING_TITLE) && interview.getJobOpening() != null
				&& interview.getJobOpening().getPostingTitle() != null) {
			String postingTitle = interview.getJobOpening().getPostingTitle().getName();
			subject = subject.replace(POSTING_TITLE, postingTitle);
			log.debug("Updated subject with PostingTitle: {}", subject);
		}
		return subject;
	}

	public ResponseDto scheduleEvent(ScheduleEventRequestDto scheduleEventRequestDto) {
		log.info(INSIDE_METHOD, "scheduleEvent");

		try {
			return meetingController.scheduleEvent(scheduleEventRequestDto);

		} catch (BusinessException ex) {
			log.error("error inside @class interviewServiceImplmethod @scheduleEvent");
			throw new BusinessException("error inside @class interviewServiceImplmethod @scheduleEvent");
		}
	}

	/**
	 * Creates a new Interview object based on existing interview with next level
	 * manager
	 * 
	 * @param interviewId ID of the existing interview
	 * @return New Interview object with updated manager
	 */
	@Override
	public Interview interviewCreateForSecondRound(Integer interviewId, String processInstanceId) {
		log.debug("Creating interviewCreateForSecondRound interview for interview ID is: {}", interviewId);

		try {
			// Get existing interview from repository
			Interview existingInterview = null;

			if (interviewId != null) {
				existingInterview = interviewRepository.findById(interviewId).orElse(null);
				log.debug("existingInterview is by InterviewId {}", existingInterview);
			} else if (processInstanceId != null) {

				existingInterview = interviewRepository.findByProcessInstanceId(processInstanceId);
				log.debug("existingInterview is by processInstanceId {}", existingInterview);
			}

			if (existingInterview == null) {
				log.error("No interview found with ID: {}", interviewId);
				throw new BusinessException("No interview found with ID: " + interviewId);
			}

			if (existingInterview.getJobApplication() != null
					&& existingInterview.getJobApplication().getApplicationStatus() != null && !existingInterview
							.getJobApplication().getApplicationStatus().equalsIgnoreCase("1st Interview Cleared")) {
				log.error(APPLICANT_FIRST_ROUND_NOT_CLEARED);

			} else {

				Interview newInterview = new Interview();

				newInterview.setJobApplication(existingInterview.getJobApplication());
				newInterview.setWorkspaceId(existingInterview.getWorkspaceId());
				newInterview.setWorkflowStage(existingInterview.getWorkflowStage());
				newInterview.setApplicant(existingInterview.getApplicant());
				newInterview.setJobOpening(existingInterview.getJobOpening());
				newInterview.setDepartment(existingInterview.getDepartment());
				newInterview.setDepartmentName(existingInterview.getDepartmentName());
				newInterview.setName(existingInterview.getName());

				if (existingInterview.getJobApplication() != null
						&& existingInterview.getJobApplication().getJobOpening() != null
						&& existingInterview.getJobApplication().getJobOpening().getHiringManager() != null
						&& existingInterview.getJobApplication().getJobOpening().getHiringManager()
								.getReportingManager() != null) {

					Employee reportingManager = existingInterview.getJobApplication().getJobOpening().getHiringManager()
							.getReportingManager();

					newInterview.setInterviewerManager(reportingManager);
					log.debug("Set new interview manager: {}", reportingManager.getId());
				} else {
					log.warn("Could not set interview manager - one or more required objects in chain is null");
					throw new BusinessException("Could not set interview manager - required data missing");
				}

				// Reset counters and status flags
				newInterview.setApplicantScheduledCount(0);
				newInterview.setInterviewerScheduledCount(0);
				newInterview.setInterviewerRecruiterCount(0);
				newInterview.setInterviewerManagerCount(0);
				newInterview.setInterviewerAccept(NO_RESPONSE);
				newInterview.setInterviewerManagerAccept(NO_RESPONSE);
				newInterview.setInterviewerRecruiterAccept(NO_RESPONSE);

				log.info("Successfully created next level interview object for interview ID: {}", interviewId);
				return interviewRepository.save(newInterview);

			}

		} catch (BusinessException be) {
			log.error("Business error creating next level interview: {}", be.getMessage());
			throw be;
		} catch (Exception e) {
			log.error("Error creating next level interview for interview ID: {}", interviewId, e);
			throw new BusinessException("Error creating next level interview", e.getMessage());
		}

		return null;
	}

	@Override
	public String updateJobApplicationStatusForInterview(Integer interviewId, String interviewStatus) {
		log.debug("Inside updateJobApplicationStatus interviewId is : {} ", interviewId);

		try {
			Interview interview = null;

			if (interviewId != null) {
				interview = interviewRepository.findById(interviewId).orElse(null);
				log.debug("Inside updateJobApplicationStatus interview found for interviewId is : {}", interview);
			}

			if (interview == null) {
				log.error("No Interviw Found By Given Interview ID");
				throw new BusinessException("No Interviw Found By Given Interview ID");
			}

			if (interview.getApplicant() != null && interview.getJobApplication() != null && interviewStatus != null) {

				String formattedInterviewStatus = interviewStatus.replace("_", " ");
				log.debug("Formatted interview status: {}", formattedInterviewStatus);

				Applicant applicant = interview.getApplicant();
				applicant.setApplicantStatus(formattedInterviewStatus);
				applicantRepository.save(applicant);

				JobApplication jobApplication = interview.getJobApplication();
				jobApplication.setApplicationStatus(formattedInterviewStatus);
				jobApplicationRepository.save(jobApplication);

				log.debug("Applicant and JobApplication status updated successfully");

				return APIConstants.SUCCESS_JSON;

			}

		} catch (BusinessException ex) {
			log.error("Error inside method updateJobApplicationStatus");
			throw new BusinessException("Error inside method updateJobApplicationStatus");
		}

		return APIConstants.FAILURE_JSON;

	}

	@Override
	public InterviewDto secondRoundInterviewCreate(Integer interviewId, String processInstanceId,String interviewStage) {
		log.debug("Creating secondRoundInterviewCreate interview is: {}", interviewId);

		try {
			// Get existing interview from repository
			Interview existingInterview = null;

			if (interviewId != null) {
				existingInterview = interviewRepository.findById(interviewId).orElse(null);
				log.debug("existingInterview is by InterviewId {}", existingInterview);
			} else if (processInstanceId != null) {

				existingInterview = interviewRepository.findByProcessInstanceId(processInstanceId);
				log.debug("existingInterview is by processInstanceId {}", existingInterview);
			}

			if (existingInterview == null) {
				log.error("No interview found with ID: {}", interviewId);
				throw new BusinessException("No interview found with ID: " + interviewId);
			}

			if (existingInterview.getJobApplication() != null
					&& existingInterview.getJobApplication().getApplicationStatus() != null && !existingInterview
							.getJobApplication().getApplicationStatus().equalsIgnoreCase("1st Interview Cleared")) {
				log.error(APPLICANT_FIRST_ROUND_NOT_CLEARED);
                 throw new BusinessException(APPLICANT_FIRST_ROUND_NOT_CLEARED);
			} else {

				Interview newInterview = new Interview();

				// Copy basic fields from existing interview
				newInterview.setJobApplication(existingInterview.getJobApplication());
				newInterview.setWorkspaceId(existingInterview.getWorkspaceId());
				if(interviewStage != null) {
					log.debug("interview stage: {}", interviewStage);
					String formattedInterviewStage = interviewStage.replace("_", " ");
					log.debug("Formatted interview stage: {}", formattedInterviewStage);
					newInterview.setWorkflowStage(formattedInterviewStage);
				}
				newInterview.setApplicant(existingInterview.getApplicant());
				newInterview.setJobOpening(existingInterview.getJobOpening());
				newInterview.setDepartment(existingInterview.getDepartment());
				newInterview.setDepartmentName(existingInterview.getDepartmentName());
				newInterview.setName(existingInterview.getName());

				// Set the new interview manager as reporting manager of current hiring manager
				if (existingInterview.getJobApplication() != null
						&& existingInterview.getJobApplication().getJobOpening() != null
						&& existingInterview.getJobApplication().getJobOpening().getHiringManager() != null
						&& existingInterview.getJobApplication().getJobOpening().getHiringManager()
								.getReportingManager() != null) {

					Employee reportingManager = existingInterview.getJobApplication().getJobOpening().getHiringManager()
							.getReportingManager();

					newInterview.setInterviewerManager(reportingManager);
					log.debug("Set new interview manager: {}", reportingManager.getId());
				} else {
					log.warn("Could not set interview manager - one or more required objects in chain is null");
					throw new BusinessException("Could not set interview manager - required data missing");
				}

				// Reset counters and status flags
				newInterview.setApplicantScheduledCount(0);
				newInterview.setInterviewerScheduledCount(0);
				newInterview.setInterviewerRecruiterCount(0);
				newInterview.setInterviewerManagerCount(0);
				newInterview.setInterviewerAccept(NO_RESPONSE);
				newInterview.setInterviewerManagerAccept(NO_RESPONSE);
				newInterview.setInterviewerRecruiterAccept(NO_RESPONSE);

				log.info("Successfully created next level interview object for interview ID: {}", interviewId);
				Interview interviewCreated = interviewRepository.save(newInterview);
				log.debug("Creating secondRoundInterviewCreate interviewCreated is: {}", interviewCreated.getId());
				
				try {
					TriggerBpmnAspect triggerBpmnAspect = ApplicationContextProvider.getApplicationContext()
							.getBean(TriggerBpmnAspect.class);
					String objInterview = null;
					log.info("Going to Trigger BPMN for interviewCreated :{} ", interviewCreated.getId());
					try {
						objInterview = getObjectMapper().writeValueAsString(interviewCreated);
						log.debug(" Obj Interview :{} ", objInterview);
					} catch (JsonProcessingException e) {
						log.error("Error while getting object value as string :{}", Utils.getStackTrace(e));
					}
					try {
						triggerBpmnAspect.triggerBpmnViaAPI(objInterview, "HRMS_APP_NAME", "Interview");
						log.info(" triggerBpmnAspect completed Interview :{}  ", interviewCreated.getWorkflowStage());
						activateAction(interviewCreated);
					} catch (Exception e) {
						log.error("Error in Triggering Workflow :{} :{}", e.getMessage(), Utils.getStackTrace(e));

					}
				} catch (Exception e) {
					log.error("While triggering workflow action exception occured :{}", e);
				}
				
				
				InterviewDto interviewDto = new InterviewDto();
				log.info("new dto created");
				interviewDto.setInterviewId(interviewCreated.getId());
				interviewDto.setWorkflowStage(interviewCreated.getWorkflowStage());
				log.debug("Response of interviewDto is : {}",interviewDto);
				return interviewDto;
			}

		} catch (BusinessException be) {
			log.error("Business error creating next level interview: {}", be.getMessage());
			throw be;
		} catch (Exception e) {
			log.error("Error creating next level interview for interview ID: {}", interviewId, e);
			throw new BusinessException("Error creating next level interview", e.getMessage());
		}

	}

	@Override
	public String getInterviewerManagerWorkEmailAddress(Integer interviewId) {
		log.debug("Inside method @getInterviewerManagerWorkEmailAddress interviewId is : {} ", interviewId);

		try {
		Interview interview = null;
		if (interviewId != null) {
			interview = interviewRepository.findById(interviewId).orElse(null);
			log.debug("Inside method @getInterviewerManagerWorkEmailAddress interview Obj is : {} ", interview);
		}

		if (interview == null) {
			log.error("Interview Not Found By Given Id");
			throw new BusinessException("Interview Not Found By Given Id");
		}

		String workEmailAddress = null;

		if (interview.getInterviewerManager() != null
				&& interview.getInterviewerManager().getWorkEmailAddress() != null) {

			workEmailAddress = interview.getInterviewerManager().getWorkEmailAddress();
			log.debug("Inside method @getInterviewerManagerWorkEmailAddress workEmailAddressj is : {} ",
					workEmailAddress);
		}

		return workEmailAddress;
		
		}catch (BusinessException ex) {
			log.error("Error Inside method getInterviewerManagerWorkEmailAddress");
			throw new BusinessException("Error Inside method getInterviewerManagerWorkEmailAddress",ex.getMessage());
		}
	}
	
	
	public static ObjectMapper getObjectMapper() {
		log.info("Inside @Class InterviewServiceImpl @Method getObjectMapper");
		if (!(objectMapper instanceof ObjectMapper)) {
			objectMapper = new ObjectMapper();
			SimpleFilterProvider filterProvider = new SimpleFilterProvider();
			filterProvider.setFailOnUnknownId(false);
			FilterProvider filters = filterProvider.addFilter("propertyFilter", new PropertyFilter());
			objectMapper.setFilterProvider(filters);
		}
		log.info("objectMapper value is : {}", objectMapper);
		return objectMapper;
	}

	public void activateAction(Interview interview) {

		try {
			log.debug("Inside class Interview method activateAction  :{} ", interview.getId());
			String processInstanceId = interview.getProcessInstanceId();
			log.debug("Starting workflowActionController  processInstanceId :{} ", processInstanceId);
			WorkflowActionsController workflowActionController = ApplicationContextProvider.getApplicationContext()
					.getBean(WorkflowActionsController.class);
			workflowActionController.notifyActions(processInstanceId);
			log.info("Starting workflowActionController completed ");
		} catch (Exception e) {
			log.error("Error in Triggering Action :{} :{} ", e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}

	}

}
