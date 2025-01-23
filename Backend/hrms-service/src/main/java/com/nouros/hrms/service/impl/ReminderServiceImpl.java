package com.nouros.hrms.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.platform.utility.notification.model.NotificationTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nouros.hrms.integration.service.NotificationIntegration;
import com.nouros.hrms.integration.service.VectorIntegrationService;
import com.nouros.hrms.model.Applicant;
import com.nouros.hrms.model.JobOpening;
import com.nouros.hrms.model.Projects;
import com.nouros.hrms.model.Reminder;
import com.nouros.hrms.model.Reminder.Status;
import com.nouros.hrms.repository.ApplicantRepository;
import com.nouros.hrms.repository.BusinessPlanRepository;
import com.nouros.hrms.repository.JobApplicationRepository;
import com.nouros.hrms.repository.ReminderRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.ApplicantService;
import com.nouros.hrms.service.ReminderService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Service
public class ReminderServiceImpl extends AbstractService<Integer, Reminder> implements ReminderService {

	private static final Logger log = LogManager.getLogger(ReminderServiceImpl.class);

	@Autowired
	private ReminderRepository reminderRepository;

	@Autowired
	private VectorIntegrationService vectorIntegrationService;

	@Autowired
	private BusinessPlanRepository businessPlanRepository;

	@Autowired
	private ApplicantRepository applicantRepository;

	@Autowired
	private JobApplicationRepository jobApplicationRepository;

	@Autowired
	private ApplicantService applicantService;

	@Autowired
	private NotificationIntegration notificationIntegration;
	
	@Autowired
	  private CommonUtils commonUtils;

	private static final String RECRUITMENT_REQUEST = "Recruitment Request";
	private static final String JOB_OFFERING = "Job Offering";
	private static final String CANDIDATE_APPLICATION = "Candidate Application";
	private static final String CANDIDATE_EVALUATION = "Candidate Evaluation";
	private static final String REMINDER_SERVICE_IMPL = "inside @Class ReminderServiceImpl @Method";
	private static final String REMINDER_NOT_FOUND_FOR_GIVEN_ID = "Reminder not found for given id";
	private static final String SPAN = " </span> ";
	private static final String USER = "Hr.Coordinator";
	private static final String TEMPLATE_NAME = "testForReminderNotification";
	private static final String NOTIFICATION = "reviewMessage";
	
	public ReminderServiceImpl(GenericRepository<Reminder> repository) {
		super(repository, Reminder.class);
	}

	@Override
	public String updateStatus(Integer id, String status) {
		try {
			Reminder optionalReminder = super.findById(id);
			if (optionalReminder == null) {
				throw new BusinessException(REMINDER_NOT_FOUND_FOR_GIVEN_ID + id);
			}
			Reminder reminder = optionalReminder;
			reminder.setStatus(Status.valueOf(status));
			saveReminder(reminder);
			return APIConstants.SUCCESS_JSON;
		} catch (Exception e) {
			log.error(REMINDER_SERVICE_IMPL + " updateStatus :{}", e.getMessage());
			throw new BusinessException(e.getMessage());
		}

	}

	@Override
	public String updateReminderDate(Integer id, Date reminderDate) {
		try {
			Reminder optionalReminder = super.findById(id);
			if (optionalReminder == null) {
				throw new BusinessException(REMINDER_NOT_FOUND_FOR_GIVEN_ID + id);
			}
			Reminder reminder = optionalReminder;
			reminder.setReminderDate(reminderDate);
			saveReminder(reminder);
			return APIConstants.SUCCESS_JSON;
		} catch (Exception e) {
			log.error(REMINDER_SERVICE_IMPL + " updateReminderDate :{}", e.getMessage());
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public List<Reminder> getAllReminder() {
		String status = "YET_TO_START";
		log.debug("Inside getAllReminder customerId is : {}", commonUtils.getCustomerId());
		List<Reminder> getReminders = reminderRepository.getAllReminder(status, commonUtils.getCustomerId());
		if (getReminders.isEmpty()) {
			throw new BusinessException("Data is not present for the specified request.");
		}
		return getReminders;
	}

	@Override
	public void syncReminder() {
		log.info("inside the @Class ReminderServiceImpl @Method syncReminder");
		reviewAndFillTheFeedbackFormForCandidate();
		reviewTheSalaryForJobOfferPositionAndCandidate();
		reviewJobOfferForPositionAndCandidate();
		recuirtmentRequestReminders();
		log.info("after all the @Method syncReminder is successfully completed");
	}

	@Override
	public void scheduleSyncReminder() {
		getListOfApplicant();
	}

	@Override
	public boolean checkOrCreate(Date reminderDate, String taskDetails, String category) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		try {
			reminderDate = formatter.parse(reminderDate.toString());
		} catch (java.text.ParseException e) {
			log.error(REMINDER_SERVICE_IMPL + " checkOrCreate :{}", e.getMessage());
		}
		if (log.isInfoEnabled()) {
			String formattedDate = formatDate(reminderDate);
			log.info("Reminder Date: {}", formattedDate);
		}
		log.debug("reminderDate :{} taskDetails :{} category: {} ", reminderDate, taskDetails, category);
		log.debug("Inside checkOrCreate customerId is : {}", commonUtils.getCustomerId());
		List<Reminder> list = reminderRepository.reminderByDateAndTaskDetails(formatDate(reminderDate), taskDetails, commonUtils.getCustomerId());
		if (list.isEmpty()) {
			Reminder reminder = initializeAndSave();
			reminder.setCategory(category);
			reminder.setTaskDetails(taskDetails);
			reminder.setReminderDate(reminderDate);
			saveReminder(reminder);
			return true;
		}
		return false;
	}

	public static String formatDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}

	private void getListOfApplicant() {
		try {
			log.info(REMINDER_SERVICE_IMPL + " getListOfApplicant");
			log.debug("Inside getListOfApplicant customerId is : {}", commonUtils.getCustomerId());
			List<Applicant> applicantLists = applicantRepository.findAllByCreatedDate(commonUtils.getCustomerId());
			Set<String> jobTitles = iterateApplicantListAndJobOpening(applicantLists);
			for (String jobTitle : jobTitles) {
				String question = "Find the suitable candidate for the posistion X";
				String task = "Review the CVs of new candidate(s) for position <span class=\"highlighted-reminders-title\"> X </span>";
				String modifiedTask = task.replace("X", jobTitle);
				String modifiedQuestion = question.replace("X", jobTitle);
				Reminder reminder = initializeAndSave();
				reminder.setCategory(CANDIDATE_APPLICATION);
				reminder.setTaskDetails(modifiedTask);
				reminder.setQuestion(modifiedQuestion);
				saveReminder(reminder);
				sendNotification(modifiedTask);
			}
		} catch (Exception e) {
			log.error(REMINDER_SERVICE_IMPL + " getListOfApplicant :{}", e.getMessage());
		}
	}

	private Set<String> iterateApplicantListAndJobOpening(List<Applicant> applicantLists) {
		log.info(REMINDER_SERVICE_IMPL + " iterateApplicantListAndJobOpening");
		List<JobOpening> jobOpenings = null;
		Set<String> jobTitles = new HashSet<>();
		if (applicantLists != null && !applicantLists.isEmpty()) {
			for (Applicant applicantList : applicantLists) {
				jobOpenings = applicantService.findSuitableJobOpeningByApplicantId(applicantList.getId());
			}
			if (jobOpenings != null && !jobOpenings.isEmpty()) {
				for (JobOpening jobOpening : jobOpenings) {
					jobTitles.add(jobOpening.getPostingTitle().getName());
				}
			}
		}
		return jobTitles;
	}

	private void reviewTheSalaryForJobOfferPositionAndCandidate() {
		try {
			log.debug(" Inside @reviewTheSalaryForJobOfferPositionAndCandidate customerId is : {}", commonUtils.getCustomerId());
			log.info(REMINDER_SERVICE_IMPL + " reviewTheSalaryForJobOfferPositionAndCandidate");
			List<Object[]> objectList = applicantRepository.reviewTheSalaryForJobOfferPositionAndCandidate(commonUtils.getCustomerId());
			List<Object> result = new ArrayList<>();
			List<String> applicantIds = new ArrayList<>();
			List<String> jobOpeningIds = new ArrayList<>();
			String question = "Update offer for applicantId ${%s} and jobOpeningId ${%s}";
			String modifiedQuestion = "";
			if (!objectList.isEmpty()) {
				for (Object[] positionData : objectList) {
					String title = (String) positionData[0];
					String applicantId = (String) positionData[1];
					String jobOpeningId = (String) positionData[2];
					result.add(title);
					applicantIds.add(applicantId);
					jobOpeningIds.add(jobOpeningId);
				}
			}
			for (int i = 0; i < result.size(); i++) {
				Object resultTask = result.get(i);
				Object applicantId = applicantIds.get(i);
				Object jobOpeningId = jobOpeningIds.get(i);
				modifiedQuestion = String.format(question, applicantId, jobOpeningId);
				Reminder reminder = initializeAndSave();
				reminder.setCategory(JOB_OFFERING);
				reminder.setDueDate(getDueDate(0));
				reminder.setTaskDetails(resultTask.toString());
				reminder.setQuestion(modifiedQuestion);
				saveReminder(reminder);
				sendNotification(resultTask);
			}
		} catch (Exception e) {
			log.error(REMINDER_SERVICE_IMPL + " reviewTheSalaryForJobOfferPositionAndCandidate :{}", e.getMessage());
		}
	}

	public void reviewJobOfferForPositionAndCandidate() {
		try {
			log.info(REMINDER_SERVICE_IMPL + " reviewJobOfferForPositionAndCandidate");
			log.debug("Inside reviewJobOfferForPositionAndCandidate customerId is : {}", commonUtils.getCustomerId());
			List<Object[]> objectList = applicantRepository.reviewJobOfferForPositionAndCandidate(commonUtils.getCustomerId());
			List<Object> result = new ArrayList<>();
			List<String> applicantIds = new ArrayList<>();
			List<String> jobOpeningIds = new ArrayList<>();
			String question = "Create offer for applicantId ${%s} and jobOpeningId ${%s}";
			String modifiedQuestion = "";
			if (!objectList.isEmpty()) {
				for (Object[] positionData : objectList) {
					String title = (String) positionData[0];
					String applicantId = (String) positionData[1];
					String jobOpeningId = (String) positionData[2];
					result.add(title);
					applicantIds.add(applicantId);
					jobOpeningIds.add(jobOpeningId);
				}
			}
			for (int i = 0; i < result.size(); i++) {
				Object resultTask = result.get(i);
				Object applicantId = applicantIds.get(i);
				Object jobOpeningId = jobOpeningIds.get(i);
				modifiedQuestion = String.format(question, applicantId, jobOpeningId);
				Reminder reminder = initializeAndSave();
				reminder.setCategory(JOB_OFFERING);
				reminder.setDueDate(getDueDate(2));
				reminder.setTaskDetails(resultTask.toString());
				reminder.setQuestion(modifiedQuestion);
				saveReminder(reminder);
				sendNotification(resultTask);
			}
		} catch (Exception e) {
			log.error(REMINDER_SERVICE_IMPL + " reviewJobOfferForPositionAndCandidate :{}", e.getMessage());
		}
	}

	@Override
	public void reviewJobOffer(Integer applicantId, Integer jobOpeningId, String jobOpeningPosition,
			String candiDateName) {
		try {
			log.info(REMINDER_SERVICE_IMPL + "reviewJobOffer");
			String question = "Create offer for applicantId ${" + applicantId + "} and jobOpeningId ${" + jobOpeningId
					+ "}";
			String task = "Review the job offer for position <span class=\"highlighted-reminders-title\"> "
					+ jobOpeningPosition + SPAN + " and candidate <span class=\"highlighted-reminders-title\"> "
					+ candiDateName + SPAN;
			Reminder rem = new Reminder();
			rem.setTaskDetails(task);
			rem.setQuestion(question);
			rem.setCategory(CANDIDATE_APPLICATION);
			rem.setStatus(Status.YET_TO_START);
			rem.setReminderDate(rem.getCreatedTime());
			rem.setDueDate(getDueDate(2));
			saveReminder(rem);
		} catch (Exception e) {
			log.error(REMINDER_SERVICE_IMPL + " reviewJobOffer :{}", e.getMessage());
		}
	}

	@Override
	public void reviewCandidateDecideYouWantToHire(Integer applicantId, Integer jobOpeningId, String gender,
			String candiDateName) {
		try {
			log.info(REMINDER_SERVICE_IMPL + "reviewCandidateDecideYouWantToHire");
			String question = "Create offer for applicantId ${" + applicantId + "} and jobOpeningId ${" + jobOpeningId
					+ "}";
			String task = "Review candidate <span class=\"highlighted-reminders-title\"> " + candiDateName + SPAN
					+ " decide whether you want to hire <span class=\"highlighted-reminders-title\"> " + gender + SPAN
					+ ", and proceed within the process.";
			Reminder rem = new Reminder();
			rem.setTaskDetails(task);
			rem.setQuestion(question);
			rem.setCategory(CANDIDATE_EVALUATION);
			rem.setStatus(Status.YET_TO_START);
			rem.setReminderDate(rem.getCreatedTime());
			rem.setDueDate(getDueDate(5));
			saveReminder(rem);
			sendNotification(task);
		} catch (Exception e) {
			log.error(REMINDER_SERVICE_IMPL + " reviewCandidateDecideYouWantToHire :{}", e.getMessage());
		}
	}

	@Override
	public Reminder updatePinned(Integer id, Boolean pinned) {
		Reminder reminderOptional = super.findById(id);
		if (reminderOptional != null) {
			Reminder reminder = reminderOptional;
			reminder.setIsPinned(pinned);
			return saveAndReturnReminder(reminder);
		}
		throw new BusinessException("Data is not present for the specified id");
	}

	private void recuirtmentRequestReminders() {
		try {
			log.info(REMINDER_SERVICE_IMPL + " recuirtmentRequestReminders");
			log.debug("Inside recuirtmentRequestReminders customerId is : {}", commonUtils.getCustomerId());
			List<Object[]> objectList = businessPlanRepository.findDataForNewRecruitment(commonUtils.getCustomerId());
			List<Object> result = new ArrayList<>();
			List<Object> titleList = new ArrayList<>();
			String question = "Create a Job Opening for X";
			String modifiedQuestion = "";
			if (!objectList.isEmpty()) {
				for (Object[] object : objectList) {
					String title = (String) object[0];
					String position = (String) object[1];
					result.add(title);
					titleList.add(position);
				}
			}
			for (int i = 0; i < result.size(); i++) {
				Object resultTask = result.get(i);
				Object position = titleList.get(i);
				modifiedQuestion = question.replace("X", (CharSequence) position);
				Reminder reminder = initializeAndSave();
				reminder.setCategory(RECRUITMENT_REQUEST);
				reminder.setDueDate(getDueDate(0));
				reminder.setTaskDetails(resultTask.toString());
				reminder.setQuestion(modifiedQuestion);
				saveReminder(reminder);
				sendNotification(resultTask);
			}
		} catch (Exception e) {
			log.error(REMINDER_SERVICE_IMPL + " recuirtmentRequestReminders :{}", e.getMessage());
		}
	}

	/**
	 * @param reminder
	 */
	private void saveReminder(Reminder reminder) {
		log.info(REMINDER_SERVICE_IMPL + "saveReminder");
		try {
			vectorIntegrationService.createReminder(reminder);
		} catch (Exception e) {
			log.error(REMINDER_SERVICE_IMPL + " saveReminder :{}", e.getMessage());
		}
	}

	/**
	 * @param reminder
	 */
	private Reminder saveAndReturnReminder(Reminder reminder) {
		log.info(REMINDER_SERVICE_IMPL + "saveReminder");
		String response;
		Reminder reminderCreated = new Reminder();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			response = vectorIntegrationService.createReminder(reminder);
			reminderCreated = objectMapper.readValue(response, Reminder.class);
			log.debug("response coming is : {}", response);
			return reminderCreated;
		} catch (Exception e) {
			log.error(REMINDER_SERVICE_IMPL + " saveReminder :{}", e.getMessage());
		}
		return reminderCreated;
	}

	private void reviewAndFillTheFeedbackFormForCandidate() {
		try {
			log.info(REMINDER_SERVICE_IMPL + "reviewAndFillTheFeedbackFormForCandidate");
			log.debug("Inside reviewAndFillTheFeedbackFormForCandidate customerId is : {}", commonUtils.getCustomerId());
			List<Object[]> objectList = jobApplicationRepository.reviewAndFillTheFeedbackFormForCandidate( commonUtils.getCustomerId());
			String question = "update interview feedback for applicantId ${%s} and jobOpeningId ${%s} ";
			List<Object> result = new ArrayList<>();
			List<String> applicantIds = new ArrayList<>();
			List<String> jobOpeningIds = new ArrayList<>();
			String modifiedQuestion = "";
			if (!objectList.isEmpty()) {
				for (Object[] positionData : objectList) {
					String title = (String) positionData[0];
					String applicantId = (String) positionData[1];
					String jobOpeningId = (String) positionData[2];
					result.add(title);
					applicantIds.add(applicantId);
					jobOpeningIds.add(jobOpeningId);
				}
				for (int i = 0; i < result.size(); i++) {
					Object resultTask = result.get(i);
					Object applicantId = applicantIds.get(i);
					Object jobOpeningId = jobOpeningIds.get(i);
					modifiedQuestion = String.format(question, applicantId, jobOpeningId);
					Reminder reminder = initializeAndSave();
					reminder.setCategory(CANDIDATE_APPLICATION);
					reminder.setDueDate(getDueDate(2));
					reminder.setTaskDetails(resultTask.toString());
					reminder.setQuestion(modifiedQuestion);
					saveReminder(reminder);
					sendNotification(resultTask);
				}
			}
		} catch (Exception e) {
			log.error(REMINDER_SERVICE_IMPL + " reviewAndFillTheFeedbackFormForCandidate :{}", e.getMessage());
		}
	}

	private void sendNotification(Object message) {
		NotificationTemplate notificationTemplate = notificationIntegration.getTemplte(TEMPLATE_NAME);
		JSONObject payload = new JSONObject();
		payload.put(NOTIFICATION, message.toString());
		notificationIntegration.sendNotification(notificationTemplate, payload, USER);
	}

	private void sendNotification(String message) {
		NotificationTemplate notificationTemplate = notificationIntegration.getTemplte(TEMPLATE_NAME);
		JSONObject payload = new JSONObject();
		payload.put(NOTIFICATION, message);
		notificationIntegration.sendNotification(notificationTemplate, payload, USER);
	}

	private Reminder initializeAndSave() {
		Reminder rem = new Reminder();
		rem.setStatus(Status.YET_TO_START);
		rem.setReminderDate(rem.getCreatedTime());
		rem.setIsPinned(false);
		return rem;
	}

	private Date getDueDate(int setDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, setDate);
		return calendar.getTime();
	}
}
