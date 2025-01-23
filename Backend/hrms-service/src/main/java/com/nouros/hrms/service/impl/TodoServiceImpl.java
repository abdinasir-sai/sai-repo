package com.nouros.hrms.service.impl;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.hibernate.Session;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.enttribe.commons.configuration.ConfigUtils;
import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.platform.utility.notification.model.NotificationTemplate;
import com.enttribe.product.namemanagement.model.CustomNumberValues.Status;
import com.enttribe.product.namemanagement.rest.ICustomNumberValuesRest;
import com.enttribe.product.namemanagement.utils.wrapper.NameGenerationWrapperV2;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.usermanagement.user.model.User;
import com.enttribe.usermanagement.user.rest.UserRest;
import com.enttribe.utils.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nouros.hrms.integration.service.NotificationIntegration;
import com.nouros.hrms.integration.service.VectorIntegrationService;
import com.nouros.hrms.model.Todo;
import com.nouros.hrms.model.TodoAssignee;
import com.nouros.hrms.model.TodoHistory;
import com.nouros.hrms.model.TodoAssignee.NotificationFrequency;
import com.nouros.hrms.repository.TodoAssigneeRepository;
import com.nouros.hrms.repository.TodoHistoryRepository;
import com.nouros.hrms.repository.TodoRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.TodoAssigneeService;
import com.nouros.hrms.service.TodoHistoryService;
import com.nouros.hrms.service.TodoService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.TodoAssigneeData;
import com.nouros.hrms.wrapper.TodoAssigneeDto;
import com.nouros.hrms.wrapper.TodoData;
import com.nouros.hrms.wrapper.TodoDto;
import com.nouros.hrms.wrapper.TodoHistoryData;
import com.nouros.hrms.wrapper.TodoUpdateRequest;
import com.nouros.payrollmanagement.service.HrmsSystemConfigService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


@Service
public class TodoServiceImpl extends AbstractService<Integer, Todo>
        implements TodoService {

    private static final String UNKNOWN_ASSIGNOR = "Unknown Assignor";

	private static final String UNKNOWN_TASK = "Unknown Task";

	private static final String HYPERLINK = "hyperlink";

	private static final String A_HREF = "<a href=\"";

	private static final String TASK_TITLE = "taskTitle";

	private static final String PENDING_UPDATE = "PENDING_UPDATE";

	private static final String NO_SUMMARY_AVAILABLE_FOR_THIS_TASK = "No summary available for this task.";

	private static final Logger log = LogManager.getLogger(TodoServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    public TodoServiceImpl(GenericRepository<Todo> repository) {
        super(repository, Todo.class);
    }
    
    private static Map<String, Map<String, String>> configMap;

    @Autowired
    CustomerInfo customerInfo;

    @Autowired
    private TodoHistoryRepository todoHistoryRepository;

    @Autowired
	ICustomNumberValuesRest customNumberValuesRest;
    
    @Autowired
    private NotificationIntegration notificationIntegration;

    @Autowired
	private UserRest userRest;

    @Autowired
    private TodoRepository repository;
    
    @Autowired
    private VectorIntegrationService integrationService;
    
    @Autowired
    private TodoAssigneeRepository todoAssigneeRepository;

    @Autowired
    private TodoAssigneeService todoAssigneeService;

    @Autowired
	private HrmsSystemConfigService hrmsSystemConfigService;

    @Autowired
    private TodoHistoryService todoHistoryService;


    
    private User getUserContext() {
		return userRest.byUserName(customerInfo.getUsername());
	}

    
    @Override
    public String softDelete(int id) {
        log.info("Inside method softDelete");
        try {
            Todo todo = super.findById(id);

            if (todo != null) {

                Todo todo1 = todo;
                todo1.setDeleted(true);
                repository.save(todo1);
                log.info("Todo soft deleted successfully");
                return APIConstants.SUCCESS_JSON;
            }
        } catch (Exception e) {
            log.debug("SomeThing Went Wrong While Deleting Todos");
        }
        return APIConstants.FAILURE_JSON;
    }

    @Override
    public void softBulkDelete(List<Integer> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                softDelete(list.get(i));
            }}
    }

    // @Transactional
    @Override
    public Todo create(Todo todo) {
        log.info("Inside @class TodoServiceImpl @method create - Creating a new Todo task");
        createReferenceId(todo);
        log.debug("Inside @class TodoServiceImpl @method create todo : {}",todo);
        Todo savedTodo = repository.save(todo);
        log.info("Todo task created successfully with ID: {}", savedTodo.getId());
        return savedTodo;
    }


    private void createReferenceId(Todo todo) {
        if (todo.getId()==null){
        Map<String, String> map = new HashMap<>();
        String part2 = setPartTwoData(todo);
            map.put("part2", part2);
            log.debug("Inside @class TodoServiceImpl @method create map : {} ",map);
			String generatedName = null;
			NameGenerationWrapperV2 nameGenerationWrapperV2 = null;
			try {
				nameGenerationWrapperV2 = customNumberValuesRest.generateNameAndFriendlyName("TodoRule", map,
						Status.ALLOCATED);
				log.info("nameGenerationWrapperV2: {}", nameGenerationWrapperV2);
				generatedName = nameGenerationWrapperV2.getGeneratedName();
				todo.setReferenceId(generatedName);
			} catch (Exception e) {
				logger.error("Failed to create/generate Naming Id For Todo");
			}
        }
    }


    private String setPartTwoData(Todo todo) {
        String part2 = "";
        try {
            if(todo.getAssignner()!= null){
                StringBuilder stringBuilder = new StringBuilder();
                if (todo.getAssignner().getFirstName() != null && !todo.getAssignner().getFirstName().isEmpty()) {
                    stringBuilder.append(todo.getAssignner().getFirstName().charAt(0));
                }
                if (todo.getAssignner().getLastName() != null && !todo.getAssignner().getLastName().isEmpty()) {
                    stringBuilder.append(todo.getAssignner().getLastName().charAt(0));
                }
                part2 = stringBuilder.toString().toUpperCase();
                }
            return part2;
        } catch (Exception e) {
            logger.error("Inside @class TodoServiceImpl @method setPartTwoData Exception : {}",Utils.getStackTrace(e));
            return part2;
        }        
    }
    

    @Override
    public Todo update(Todo todo) {
        log.info("Inside @class TodoServiceImpl @method update");

        return repository.save(todo);
    }

    // @Scheduled(cron = "0 0 10 * * *")
    // public void sendScheduledNotificationToAssignorDaily() {
    //     sendScheduledNotificationToAssignortoUpdate();
    //     sendScheduledNotificationToAssigneeForUpdate();	
    // }



    @Scheduled(cron = "0 0 10 * * ?")
    @Override
    public void sendScheduledNotificationToAssignorDaily() {
        sendScheduledNotificationToAssignortoUpdate();
        sendScheduledNotificationToAssigneeForUpdate();	
    }

    public void sendScheduledNotificationToAssignortoUpdate() {
        List<Todo> todoList = repository.findTodosForAssignorNotificationToday();

        if (todoList != null && !CollectionUtils.isEmpty(todoList)) {
            log.info("Found {} todos for assignor notification today.", todoList.size());
            for (Todo todo : todoList) {
                try {
                    if (todo.getAssignner() != null && todo.getAssignner().getUserName() != null) {
                        String oneLinerSummary = fetchOneLinerSummary(todo);
                        User assignor = userRest.findUserById(todo.getAssignner().getUserid());
                        String Email = assignor.getEmail();
                        if (oneLinerSummary == null || oneLinerSummary.isEmpty()) {
                            oneLinerSummary = NO_SUMMARY_AVAILABLE_FOR_THIS_TASK;
                        }

                        log.info("Preparing to send notification for Todo: {}, Assignor: {}",
                                todo.getTaskTitle(), todo.getAssignner().getUserName());

                        sendNotification(oneLinerSummary, Email);
                        sendEmailToAssignor(todo);
                        todo.setAssignerLastNotification(new Date());
                        repository.save(todo);

                        log.info("Notification and email sent successfully for Todo ID: {}", todo.getId());
                    } else {
                        log.warn("Assignor details are missing for Todo ID: {}", todo.getId());
                    }
                } catch (Exception e) {
                    log.error("Error processing notification for Todo ID: {}", todo.getId(), e);
                }
            }
        } else {
            log.info("No todos found for assignor notification today.");
        }
    }

 @Override
    public Object createTodoAndAssignee(List<TodoDto> todoDtos) {
        log.info("Inside @class TodoServiceImpl @method createTodoAndAssignee");
        log.debug("Inside @class TodoServiceImpl @method createTodoAndAssignee todoDtos :{}", todoDtos);
        try {
            if (todoDtos != null && !todoDtos.isEmpty()) {
                for (TodoDto todoDto : todoDtos) {
                    if (!todoDto.getIsSplit()) {
                        Todo todo = createTodo(todoDto);
                        createTodoHistory(todo,null);
                        List<TodoAssigneeDto> todoAssigneeDtos = todoDto.getTodoAssignees();
                        createToDoIfAssigneeNotEmpty(todoDto, todo, todoAssigneeDtos);
                    } else if (todoDto.getIsSplit()) {
                        List<TodoAssigneeDto> todoAssigneeDtos = todoDto.getTodoAssignees();
                        if (!todoAssigneeDtos.isEmpty()) {
                            for (TodoAssigneeDto todoAssigneeDto : todoAssigneeDtos) {
                                Todo todo = createTodo(todoDto);
                                createTodoHistory(todo,null);
                                TodoAssignee todoAssignee = createTodoAssignee(todoDto, todo, todoAssigneeDto);
                                String updateFrequencyNotification = getUpdateFrequencyNotification(todoAssignee.getAssignee().getUserid(),todo.getAssignner().getUserid(),todo.getPriority());
                                sendEmailNotificationsToAllAssignee(todoAssigneeDto.getAssignee(),getUserContext(),todo.getTaskTitle(),todo.getId());
                                sendPushNotificationToAllAssignee(todoAssigneeDto.getAssignee(), getUserContext(),todo);
                                NotificationFrequency frequency = NotificationFrequency.valueOf(updateFrequencyNotification);
                                todoAssignee.setAssigneeNotificationFrequency(frequency);
                                todoAssigneeService.create(todoAssignee);
                            }
                        }
                    }
                }
                return APIConstants.SUCCESS_JSON;  
            }         
            throw new BusinessException("todoDtos is empty or null");
        } catch (Exception e) {
            log.error("Exception occurs @Method createTodoAndAssignee :{} :{}",e.getMessage(),Utils.getStackTrace(e));
            return APIConstants.FAILURE_JSON;
        }
    }


private void createToDoIfAssigneeNotEmpty(TodoDto todoDto, Todo todo, List<TodoAssigneeDto> todoAssigneeDtos) {
	if (!todoAssigneeDtos.isEmpty()) {
	    for (TodoAssigneeDto todoAssigneeDto : todoAssigneeDtos) {
	        TodoAssignee todoAssignee =  createTodoAssignee(todoDto, todo, todoAssigneeDto);
	        String updateFrequencyNotification = getUpdateFrequencyNotification(todoAssignee.getAssignee().getUserid(),todo.getAssignner().getUserid(),todo.getPriority());
	        sendEmailNotificationsToAllAssignee(todoAssigneeDto.getAssignee(),getUserContext(),todo.getTaskTitle(),todo.getId());
	        sendPushNotificationToAllAssignee(todoAssigneeDto.getAssignee(), getUserContext(),todo);
	        NotificationFrequency frequency = NotificationFrequency.valueOf(updateFrequencyNotification);
	        todoAssignee.setAssigneeNotificationFrequency(frequency);
	        todoAssigneeService.create(todoAssignee);    
	    }
	}
}
   
    private Todo createTodo(TodoDto todoDto) {
        Todo todo = new Todo();
        User user = getUserContext();
        todo.setTaskTitle(todoDto.getTaskTitle());
        if (todoDto.getDescription() != null)
            todo.setDescription(todoDto.getDescription());
        todo.setPriority(todoDto.getPriority());
        todo.setType(todoDto.getType());
        todo.setStartDate(new Date());
        todo.setDueDate(todoDto.getDueDate());
        todo.setAssignerLastNotification(new Date());
        todo.setEscalationNotification(false);
        if (!todoDto.getStatus().equals(PENDING_UPDATE))
            todo.setStatus(todoDto.getStatus());
        else
            todo.setStatus(PENDING_UPDATE);
        todo.setAssignner(new com.nouros.hrms.model.User(user));
        todo.setCreator(new com.nouros.hrms.model.User(user));
        todo.setCreatedTime(new Date());
        todo.setModifiedTime(new Date());
        todo.setLastModifier(new com.nouros.hrms.model.User(user));
        todo.setAssignnerUpdateFrequency("DAILY");
        if (todoDto.getType().equals("CHILD") && todoDto.getParentTaskId()!=null){
            Optional<Todo> optTodo = repository.findById(todoDto.getParentTaskId());
            if (optTodo.isPresent()) {
                Todo parentTodo = optTodo.get();
                todo.setParentTask(parentTodo);
            }
        }
        log.debug("Inside @class TodoServiceImpl @method createTodo todo : {}", todo);
        // todo = repository.save(todo);
        todo = create(todo);
        log.debug("created todo :{}", todo);
        return todo;
    }

    private TodoHistory createTodoHistory(Todo todo, String remark) {
        log.info("Inside @Class TodoServiceImpl @method createTodoHistory");
    
        TodoHistory todoHistory = new TodoHistory();
        User user = getUserContext();
        todoHistory.setUser(new com.nouros.hrms.model.User(user));
        String historyRemark = (remark != null && !remark.isEmpty()) ? remark : "New Task " + todo.getTaskTitle() + " created by " + user.getFullName();
        todoHistory.setRemark(historyRemark);
        todoHistory.setTodo(todo);
        todoHistory = todoHistoryService.create(todoHistory);
        log.info("TodoHistory successfully created for task: {}", todo.getTaskTitle());
        return todoHistory;
    }
    

    private String getHyperLinkForEmail(Integer todoId) {
		log.info("Inside @class TodoServiceImpl @method getHyperLinkForEmail");
		JSONObject json = new JSONObject();
		String question = ConfigUtils.getPlainString("MAIL_ASSIST_QUESTION");
		String finalQuestion = question + " " + todoId;
		log.debug("getHyperLinkForEmail finalQuestion",finalQuestion);
		json.put("mailAssistQuestion", finalQuestion);
		json.put("topic", finalQuestion);
		json.put("selectedQuestionFromSuggestion", "");
		json.put("typeChange", "modified");
		log.debug("@method getHyperLinkForEmail json",json);
		return json.toString();
	}

    private String getEncodedDataForExternal(String json) {
		log.info("Inside @class TodoServiceImpl @method getEncodedDataForExternal");
		byte[] urlBytes = json.getBytes(StandardCharsets.UTF_8);
		return Base64.getEncoder().encodeToString(urlBytes);
	}

    private void sendEmailNotificationsToAllAssignee(User assigneeUser, User assignorUser,String taskTitle,Integer id) {
        log.info("Inside @method sendEmailNotificationsToAllAssignee");
        log.debug("assigneeUser :{} assignorUser :{}", assigneeUser, assignorUser);
        String templateNameForNourosTemplate = ConfigUtils.getPlainString("ASSIGNEE_TEMPLATE_EMAIL");

        NotificationTemplate notificationTemplateForNourosTemplate = notificationIntegration
                .getTemplte(templateNameForNourosTemplate);
        log.debug("notificationTemplateForNourosTemplate :{}", notificationTemplateForNourosTemplate);
        String assigneeFullName = "";
        String assignorFullName = "";
        JSONObject object = new JSONObject();
        if (assigneeUser.getFirstName() != null && assigneeUser.getLastName() != null) {
            assigneeFullName = assigneeUser.getFirstName() + " " + assigneeUser.getLastName();
        }
        if (assignorUser.getFirstName() != null && assignorUser.getLastName() != null) {
            assignorFullName = assignorUser.getFirstName() + " " + assignorUser.getLastName();
        }
        log.debug("assigneeFullName :{} assignorFullName :{}", assigneeFullName, assignorFullName);
        object.put("recipient", assigneeFullName);
        object.put("name", assignorFullName);
        object.put(TASK_TITLE,taskTitle);
        String hyperLinkVariable = getHyperLinkVariable(id);
        String linkHtml = A_HREF + hyperLinkVariable
        + "\" style=\"padding:8px 8px;background-color:#000037;color:white;text-align:center;text-decoration:none;display:inline-block;border-radius:4px;\">Update</a>";
        object.put("link", linkHtml);
        log.debug("object :{}", object);
        User assigneeUserForGetEmail = userRest.findUserById(assigneeUser.getUserid());
        String assigneeEmail = assigneeUserForGetEmail.getEmail();
        notificationIntegration.sendEmail(notificationTemplateForNourosTemplate, object, assigneeEmail, null,
        Collections.emptyList());
        log.info("after the email goes successfully.");
    }

    
    private TodoAssignee createTodoAssignee(TodoDto todoDto, Todo todo, TodoAssigneeDto todoAssigneeDto) {
        TodoAssignee todoAssignee = new TodoAssignee();
        
        if (todoAssigneeDto.getId() != null) {
            todoAssignee.setId(todoAssigneeDto.getId());
    }
    todoAssignee.setTodo(todo);
    todoAssignee.setAssignee(new com.nouros.hrms.model.User(todoAssigneeDto.getAssignee()));

    if (todoAssigneeDto.getAssigneeNotificationFrequency() != null) {
        try {
            NotificationFrequency frequency = NotificationFrequency.valueOf(todoAssigneeDto.getAssigneeNotificationFrequency());
            todoAssignee.setAssigneeNotificationFrequency(frequency);
        } catch (IllegalArgumentException e) {
            log.error("Invalid notification frequency: {}", todoAssigneeDto.getAssigneeNotificationFrequency(), e);
        }
    }

    todoAssignee.setAssigneeLastNotification(new Date());

    if (!todoDto.getStatus().equals(PENDING_UPDATE)) {
        todo.setStatus(todoDto.getStatus());
    } else {
        todo.setStatus(PENDING_UPDATE);
    }
    todoAssignee = todoAssigneeService.create(todoAssignee);
    log.debug("Created todoAssignee: {}", todoAssignee);
    return todoAssignee;
}

    public String fetchOneLinerSummary(Todo todo) {
    if (todo == null) {
        log.warn("Todo is null. Cannot fetch one-liner summary.");
        return NO_SUMMARY_AVAILABLE_FOR_THIS_TASK;
    }

    String taskTitle = (todo.getTaskTitle() != null) 
        ? todo.getTaskTitle().replaceAll("\\s+", " ").replaceAll("[(){}\\[\\]]", "") 
        : "Untitled Task";

    JSONObject payloadJson = new JSONObject();
    payloadJson.put(TASK_TITLE, taskTitle);
    payloadJson.put("status", todo.getStatus() != null ? todo.getStatus() : "N/A");
    payloadJson.put("dueDate", todo.getDueDate() != null ? todo.getDueDate().toString() : "N/A");
    payloadJson.put("priority", todo.getPriority() != null ? todo.getPriority() : "N/A");

    List<String> assigneeNames = new ArrayList<>();

    List<TodoAssignee> mainTaskAssignees = todoAssigneeRepository.findAssigneesByTodoId(todo.getId());
    for (TodoAssignee todoAssignee : mainTaskAssignees) {
        if (todoAssignee.getAssignee() != null) {
            assigneeNames.add(todoAssignee.getAssignee().getFullName());
        }
    }

    if (todo.getSubTasks() != null) {
        for (Todo subTask : todo.getSubTasks()) {
            List<TodoAssignee> subTaskAssignees = todoAssigneeRepository.findAssigneesByTodoId(subTask.getId());
            for (TodoAssignee todoAssignee : subTaskAssignees) {
                if (todoAssignee.getAssignee() != null) {
                    assigneeNames.add(todoAssignee.getAssignee().getFullName());
                }
            }
        }
    }

    String assigneeFullNames = assigneeNames.isEmpty() ? "No assignees" : String.join(", ", assigneeNames);
    payloadJson.put("assigneeNames", assigneeFullNames);

    Map<String, String> payloadMap = payloadJson.keySet().stream()
        .collect(Collectors.toMap(key -> key, key -> payloadJson.optString(key, "N/A")));


    String promptName =  ConfigUtils.getPlainString("TODO_STATUS");

    try {
        String response = integrationService.executePromptTemplate(promptName, payloadMap);

        if (response == null || response.trim().isEmpty()) {
            log.warn("Empty summary received for Todo ID: {}", todo.getId());
            return NO_SUMMARY_AVAILABLE_FOR_THIS_TASK;
        }

        log.info("Summary fetched successfully for Todo ID: {} response: {}", todo.getId(), response);
        return response.trim();
    } catch (IllegalArgumentException e) {
        log.error("Invalid template or payload for Todo ID: {}", todo.getId(), e);
        return "Template error: Invalid template string.";
    } catch (Exception e) {
        log.error("Error fetching summary for Todo ID: {}", todo.getId(), e);
        return NO_SUMMARY_AVAILABLE_FOR_THIS_TASK;
    }
}

 private void sendPushNotificationToAllAssignee(User assigneeUser, User assignorUser,Todo todo) {
        log.info("Inside @method sendPushNotificationToAllAssignee");
        log.debug("assigneeUser :{} assignorUser :{}", assigneeUser, assignorUser);
        String templateNameForNourosTemplate = ConfigUtils.getPlainString("ASSIGNEE_TEMPLATE_NOTIFICATION");
        NotificationTemplate notificationTemplateForNourosTemplate = notificationIntegration
                .getTemplte(templateNameForNourosTemplate);
        // String assigneeFullName = "";
        String assignorFullName = "";
        JSONObject object = new JSONObject();
        // if (assigneeUser.getFirstName() != null && assigneeUser.getLastName() != null) {
        //    assigneeFullName = assigneeUser.getFirstName() + " " + assigneeUser.getLastName();
        // }
        if (assignorUser.getFirstName() != null && assignorUser.getLastName() != null) {
            assignorFullName = assignorUser.getFirstName() + " " + assignorUser.getLastName();
        }
        // object.put("recipient", assigneeFullName);
        object.put("name", assignorFullName);
        object.put("message",todo.getTaskTitle());
        object.put("taskId",todo.getId());
        object.put("dueDate",getFormatProvidedDate(todo.getDueDate()));
        object.put("priority",todo.getPriority());
        String hyperLinkVariable = getHyperLinkVariable(todo.getId());
        String linkHtml = A_HREF + hyperLinkVariable
                + "\" style=\"padding:8px 8px;color:white;text-align:center;text-decoration:none;display:inline-block;border-radius:4px;position:absoulute !important;width:100%;height:100%;\"></a>";
        object.put("link", linkHtml);
        log.debug("object :{}", object);
        User assigneeUserForGetEmail = userRest.findUserById(assigneeUser.getUserid());
        String assigneeEmail = assigneeUserForGetEmail.getEmail();
        notificationIntegration.sendNotification(notificationTemplateForNourosTemplate,object,assigneeEmail);
        log.info("after the notification goes successfully.");
    }

    private String getFormatProvidedDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, yyyy");
        return sdf.format(date);
    }


    private String fetchLevelByUserId(Integer userId){
        EmployeeService employeeService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeService.class);
        return employeeService.getDesignationLevelByUserId(userId);
    }

    private void sendNotification(String message, String userName) {
        String template = ConfigUtils.getPlainString("ASSIGNOR_NOTIFICATION_TEMPLATE");
        NotificationTemplate notificationTemplate = notificationIntegration.getTemplte(template);
        if (notificationTemplate == null) {
            log.error("sendNotification Notification template not found for name: {}", template);
            return;
        }

        JSONObject payload = new JSONObject();
        payload.put("message", message);

        try {
            notificationIntegration.sendNotification(notificationTemplate, payload, userName);
            log.info("Notification sent successfully to user: {} with message: {}", userName, message);
        } catch (Exception e) {
            log.error("Error sending notification to user: {}", userName, e);
        }
    }




private void sendNotification(String taskTitle, String assignorName, String hyperlink, String userName) {
    String template = ConfigUtils.getPlainString("NOTIFICATION_TEMPLATE_NAME");
    NotificationTemplate notificationTemplate = notificationIntegration.getTemplte(template);
    if (notificationTemplate == null) {
        log.error("Notification template not found for name: {}", template);
        return;
    }

    JSONObject payload = new JSONObject();
    payload.put(TASK_TITLE, taskTitle);
    payload.put("name", assignorName);
    payload.put(HYPERLINK, hyperlink);

    notificationIntegration.sendNotification(notificationTemplate, payload, userName);
}

public void sendScheduledNotificationToAssigneeForUpdate() {
    List<TodoAssignee> todoAssigneeList = todoAssigneeRepository.findAssigneesForNotificationToday();

    if (todoAssigneeList != null && !CollectionUtils.isEmpty(todoAssigneeList)) {
        for (TodoAssignee todoAssignee : todoAssigneeList) {
            if (todoAssignee.getAssignee() != null && todoAssignee.getAssignee().getUserName() != null) {
                String userName = todoAssignee.getAssignee().getUserName();
                User assignor = userRest.findUserById(todoAssignee.getAssignee().getUserid());
                String email = assignor.getEmail();
                String taskTitle = todoAssignee.getTodo() != null ? todoAssignee.getTodo().getTaskTitle() : UNKNOWN_TASK;
                String assignorName = todoAssignee.getTodo() != null && todoAssignee.getTodo().getAssignner() != null
                        ? todoAssignee.getTodo().getAssignner().getFirstName() + " " + todoAssignee.getTodo().getAssignner().getLastName()
                        : UNKNOWN_ASSIGNOR;

                String hyperLinkVariable = getHyperLinkVariable();
                String linkHtml = A_HREF + hyperLinkVariable
                        + "\" style=\"padding:8px 8px;color:white;text-align:center;"
                        + "text-decoration:none;display:inline-block;border-radius:4px;position:absoulute !important;width:100%;height:100%;\"></a>";

                sendNotification(taskTitle, assignorName, linkHtml, email);

                sendEmailNotificationsToAllAssignee(todoAssignee);

                todoAssignee.setAssigneeLastNotification(new Date());
                todoAssigneeRepository.save(todoAssignee);

                log.debug("Notification sent for Todo: {} to Assignee: {}", todoAssignee.getId(), userName);
            } else {
                log.warn("Assignee is not found for Todo with ID: {}", todoAssignee.getId());
            }
        }
    } else {
        log.info("No Todos found for notification today.");
    }
}


private void sendEmailNotificationsToAllAssignee(TodoAssignee todoAssignee) {
    com.nouros.hrms.model.User assigneeUser = todoAssignee.getAssignee();
    Integer assigneeId = todoAssignee.getAssignee().getUserid();
    User assignorUser = userRest.findUserById(assigneeId);
    String assigneeEmail = assignorUser.getEmail();
    if (assigneeEmail == null || assigneeEmail.isEmpty()) {
        log.error("Assignee email is missing for assignee: {}", assigneeUser.getFirstName() + " " + assigneeUser.getLastName());
        return;}
    String templateNameForNourosTemplate = ConfigUtils.getPlainString("TODO_STATUS");
    NotificationTemplate notificationTemplateForNourosTemplate = notificationIntegration
            .getTemplte(templateNameForNourosTemplate);

    String hyperLinkVariable = getHyperLinkVariable();

    JSONObject object = new JSONObject();

    String assigneeFullName = assigneeUser != null ? assigneeUser.getFirstName() + " " + assigneeUser.getLastName() : "Unknown Assignee";
    String assignorFullName = assignorUser != null ? assignorUser.getFirstName() + " " + assignorUser.getLastName() : UNKNOWN_ASSIGNOR;
    String taskTitle = todoAssignee.getTodo() != null ? todoAssignee.getTodo().getTaskTitle() : UNKNOWN_TASK;

    String linkHtml = A_HREF + hyperLinkVariable
            + "\" style=\"padding:8px 8px;background-color:#000037;color:white;text-align:center;"
            + "text-decoration:none;display:inline-block;border-radius:4px;\">Click to Update</a>";

    object.put("recipient", assigneeFullName);
    object.put("name", assignorFullName);
    object.put(TASK_TITLE, taskTitle);
    object.put(HYPERLINK, linkHtml);

    String toEmail = assigneeUser != null ? assigneeEmail : "";
    String ccUser = "";

    notificationIntegration.sendEmail(notificationTemplateForNourosTemplate, object, toEmail, ccUser, null);

    log.debug("Email notification sent to Assignee: {} for Task: {}", assigneeFullName, taskTitle);
}


private void sendEmailToAssignor(Todo todo) {
    String template = ConfigUtils.getPlainString("MAIL_TO_ASSIGNOR");
    NotificationTemplate notificationTemplate = notificationIntegration.getTemplte(template);
    if (notificationTemplate == null) {
        log.error("sendEmailToAssignor Notification template not found for name: {}", template);
        return;
    }

    com.nouros.hrms.model.User assignorUser = todo.getAssignner();
    String assignorFullName = assignorUser != null ? assignorUser.getFirstName() + " " + assignorUser.getLastName() : UNKNOWN_ASSIGNOR;
    String taskTitle = todo != null ? todo.getTaskTitle() : UNKNOWN_TASK;

    String hyperlinkVariable = getHyperLinkVariable();

    String linkHtml = A_HREF + hyperlinkVariable
            + "\" style=\"padding:8px 8px;background-color:#000037;color:white;text-align:center;"
            + "text-decoration:none;display:inline-block;border-radius:4px;\">Click to View</a>";

    JSONObject payload = new JSONObject();
    payload.put("name", assignorFullName);
    payload.put(TASK_TITLE, taskTitle);
    payload.put(HYPERLINK, linkHtml);

    try {
        Integer assignorId = assignorUser != null ? assignorUser.getUserid() : null;
        User assignorDetails = assignorId != null ? userRest.findUserById(assignorId) : null;
        String assignorEmail = assignorDetails != null ? assignorDetails.getEmail() : null;

        if (assignorEmail != null && !assignorEmail.isEmpty()) {
            notificationIntegration.sendEmail(notificationTemplate, payload, assignorEmail, null, null);
            log.info("Email sent successfully to Assignor: {} for Todo ID: {}", assignorEmail, todo.getId());
        } else {
            log.warn("Assignor email not found for Todo ID: {}", todo.getId());
        }
    } catch (Exception e) {
        log.error("Error sending email to Assignor for Todo ID: {}", todo.getId(), e);
    }
}


   private String getHyperLinkVariable(Integer id) {
    String json = getHyperLinkForEmail(id);
    String encodedData = getEncodedDataForExternal(json);
    String hyperLinkVariable = null;
    hyperLinkVariable = ConfigUtils.getPlainString("BASE_URL")
            + ConfigUtils.getPlainString("ROUTE_URL") + encodedData;
    return hyperLinkVariable;
}

private String getHyperLinkVariable() {
    String hyperLinkVariable = null;
    hyperLinkVariable = ConfigUtils.getPlainString("BASE_URL")
            + ConfigUtils.getPlainString("ROUTE_URL");
    return hyperLinkVariable;
}


    private String getUpdateFrequencyNotification(Integer assigneeUserId,Integer assignorUserId,String priority){
        getStringToMap();
        String assigneeLevel = fetchLevelByUserId(assigneeUserId);
        String assignorLevel = fetchLevelByUserId(assignorUserId);
        String updateFrequencyNotification = getPriorityValue(assigneeLevel,assignorLevel,priority);
        log.debug("updateFrequencyNotification :{}",updateFrequencyNotification);
        return updateFrequencyNotification;
    }
 
    private void getStringToMap() {
        log.info("Inside @method getUpdateNotificationFrequency");
        try {
            String json = hrmsSystemConfigService.getValue("TODO_CONDITION");
            ObjectMapper objectMapper = new ObjectMapper();
//            configMap = objectMapper.readValue(json, new TypeReference<>() {});
//            log.debug("configMap :{}",configMap);
            
        } catch (Exception e) {
            log.error("Exception occurs Inside @method getUpdateNotificationFrequency :{} :{}",e.getMessage(),Utils.getStackTrace(e));
        }
    }
    
    private String getPriorityValue(String assigneeLevel, String assignorLevel, String priorityType) {
        int assigneeNumber = extractLevelNumber(assigneeLevel);
        int assignorNumber = extractLevelNumber(assignorLevel);
        if (assigneeNumber == -1 || assignorNumber == -1) {
            throw new BusinessException("Invalid assignee or assignor level.");
        }
        String levelRelationship;
        if (assigneeNumber == assignorNumber) {
            levelRelationship = "same";
        } else if (assignorNumber < assigneeNumber) {
            levelRelationship = "high";
        } else {
            levelRelationship = "low";
        }
        Map<String, String> priorityMapping = configMap.get(levelRelationship);
        if (priorityMapping == null) {
            throw new RuntimeException("Configuration for level relationship '" + levelRelationship + "' not found.");
        }
        return priorityMapping.get(priorityType);
    }
 
    private int extractLevelNumber(String level) {
        try {
            return Integer.parseInt(level.substring(1));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return -1;
        }
    }

    @Override
    public String updateTodoStatus(Integer id, String status) {
        log.info("Inside @Class TodoServiceImpl @method updateTodoStatus");
        log.debug("Inside @Class TodoServiceImpl @method updateTodoStatus id :{}", id);
        try {
            Optional<Todo> optTodo = repository.findById(id);
            if (optTodo.isPresent()) {
                Todo todo = optTodo.get();
                todo.setStatus(status);
                repository.save(todo);
                return com.enttribe.connectx.util.APIConstants.SUCCESS_JSON;
            }
            throw new BusinessException("There is no todo found for the given id.");
        } catch (Exception e) {
            log.error("Exception occurs @method updateTodoStatus :{} :{}", e.getMessage(), Utils.getStackTrace(e));
            throw new BusinessException("Exception occurs @method updateTodoStatus");
        }
    }


    @Override
    public String updateAssignee(Integer id, Integer oldAssigneeId, Integer newAssigneeId) {
        log.info("Inside @Class TodoServiceImpl @method updateAssignee");
        log.debug("Inside @Class TodoServiceImpl @method updateAssignee id :{} oldAssigneeId:{} newAssigneeId:{}", id,
                oldAssigneeId, newAssigneeId);
        try {
            TodoAssignee todoAssignee = todoAssigneeService.findByTodoAssigneeByAssigneeId(id, oldAssigneeId);
            User newAssignee = userRest.findUserById(newAssigneeId);
            if (newAssignee != null && todoAssignee != null) {
                String remark = "Assignee updated from " + todoAssignee.getAssignee().getFullName() 
                            + " to " + newAssignee.getFullName()+" for task "+ todoAssignee.getTodo().getTaskTitle();
                Todo todo = todoAssignee.getTodo();
                createTodoHistory(todo, remark);
                todo.setTodoUpdate(remark);
                repository.save(todo); 
                todoAssignee.setAssignee(new com.nouros.hrms.model.User(newAssignee));
                todoAssigneeService.create(todoAssignee);
                return APIConstants.SUCCESS_JSON;
            }
            throw new BusinessException("old todoAssignee or the new todoAssignee is null");
        } catch (Exception e) {
            log.error("Exception occurs @method updateTodoStatus :{} :{}", e.getMessage(), Utils.getStackTrace(e));
            throw new BusinessException("Exception occurs @method updateAssignee");
        }
    }

    @Override
    public String updateDeadline(Integer id, Date newDueDate) {
        log.info("Inside @Class TodoServiceImpl @method updateDeadline");
        log.debug("Inside @Class TodoServiceImpl @method updateDeadline id :{} newDueDate:{}", id, newDueDate);
        try {
            Optional<Todo> todoOptional = repository.findById(id);
            if (todoOptional.isPresent() && newDueDate != null) {
                Todo todo = todoOptional.get();
                String remark = "Deadline of task "+todo.getTaskTitle() +" updated from " + todo.getDueDate() + " to " + newDueDate;
                createTodoHistory(todo, remark);
                todo.setTodoUpdate(remark);
                todo.setDueDate(newDueDate);
                repository.save(todo); 
                return APIConstants.SUCCESS_JSON;
            }
            throw new BusinessException("Todo not found or new due date is null");
        } catch (Exception e) {
            log.error("Exception occurs @method updateDeadline :{} :{}", e.getMessage(), Utils.getStackTrace(e));
            throw new BusinessException("Exception occurs @method updateDeadline");
        }
    }
    

    @Override
    public String updateTodo(TodoUpdateRequest todoUpdateRequest) {
        log.info("Inside @Class TodoServiceImpl @method updateTodo - Request received to update todo with ID: {}", todoUpdateRequest.getId());
        log.debug("Request details - id: {}, oldAssigneeIds: {}, newAssigneeIds: {}, newDueDate: {}", 
            todoUpdateRequest.getId(), todoUpdateRequest.getOldAssigneeIds(), todoUpdateRequest.getNewAssigneeIds(), todoUpdateRequest.getNewDueDate());
    
        try {
            Optional<Todo> todoOptional = repository.findById(todoUpdateRequest.getId());
            if (!todoOptional.isPresent()) {
                log.warn("Todo not found with ID: {}", todoUpdateRequest.getId());
                throw new BusinessException("Todo not found");
            }
    
            Todo todo = todoOptional.get();
            log.info("Todo found - ID: {}, Task Title: {}", todo.getId(), todo.getTaskTitle());
    
            StringBuilder remarksBuilder = new StringBuilder();
    
            List<Integer> oldAssigneeIds = todoUpdateRequest.getOldAssigneeIds();
            List<Integer> newAssigneeIds = todoUpdateRequest.getNewAssigneeIds();
            Date newDueDate = todoUpdateRequest.getNewDueDate();
    
            if (oldAssigneeIds != null && newAssigneeIds != null && !oldAssigneeIds.isEmpty() && !newAssigneeIds.isEmpty()) {
                log.info("Assignee update detected for Todo ID: {}", todoUpdateRequest.getId());
                for (int i = 0; i < newAssigneeIds.size(); i++) {
                    Integer newAssigneeId = newAssigneeIds.get(i);
                    Integer oldAssigneeId = i < oldAssigneeIds.size() ? oldAssigneeIds.get(i) : null;
    
                    if (oldAssigneeId != null && !oldAssigneeId.equals(newAssigneeId)) {
                        log.debug("Updating assignee from ID: {} to ID: {}", oldAssigneeId, newAssigneeId);
    
                        TodoAssignee oldTodoAssignee = todoAssigneeService.findByTodoAssigneeByAssigneeId(oldAssigneeId, todoUpdateRequest.getId());
                        User newAssignee = userRest.findUserById(newAssigneeId);
    
                        if (newAssignee != null && oldTodoAssignee != null) {
                            String remark = "Assignee updated from " + oldTodoAssignee.getAssignee().getFullName() 
                                            + " to " + newAssignee.getFullName() + " for task " + todo.getTaskTitle();
                            remarksBuilder.append(remark).append("; ");
                            createTodoHistory(todo, remark);
    
                            oldTodoAssignee.setAssignee(new com.nouros.hrms.model.User(newAssignee));
                            todoAssigneeService.create(oldTodoAssignee);
    
                            log.info("Assignee updated successfully for Todo ID: {} - New assignee: {}", todoUpdateRequest.getId(), newAssignee.getFullName());
                        } else {
                            log.error("Assignee update failed - New or old assignee not found for Todo ID: {}", todoUpdateRequest.getId());
                            throw new BusinessException("Invalid assignee update: New or old assignee not found");
                        }
                    }
                }
            }
    
            if (newDueDate != null && (todo.getDueDate() == null || !todo.getDueDate().equals(newDueDate))) {
                log.info("Deadline update detected for Todo ID: {}", todoUpdateRequest.getId());
                String remark = "Deadline of task " + todo.getTaskTitle() + " updated from " 
                                + todo.getDueDate() + " to " + newDueDate;
                remarksBuilder.append(remark).append(" and ");
                createTodoHistory(todo, remark);
                todo.setDueDate(newDueDate);
    
                log.info("Deadline updated successfully for Todo ID: {} - New due date: {}", todoUpdateRequest.getId(), newDueDate);
            }
    
            if (remarksBuilder.length() == 0) {
                log.warn("No updates detected for Todo ID: {} - No assignee or deadline changes", todoUpdateRequest.getId());
                throw new BusinessException("No updates detected for assignee or deadline");
            }
    
            todo.setTodoUpdate(remarksBuilder.toString().trim());
            repository.save(todo);
    
            log.info("Todo ID: {} updated successfully with remarks: {}", todoUpdateRequest.getId(), remarksBuilder.toString());
    
            return APIConstants.SUCCESS_JSON;
        } catch (Exception e) {
            log.error("Exception occurred @method updateTodo - Error updating Todo ID: {} - Message: {} - StackTrace: {}", 
                      todoUpdateRequest.getId(), e.getMessage(), Utils.getStackTrace(e));
            throw new BusinessException("Exception occurs @method updateTodo");
        }
    }
    

    @Override
    public Object findAllData(String referenceId, Integer assigneeId) {
        log.info("Inside @Class TodoServiceImpl @method updateAssignee");
        log.debug("Inside @Class TodoServiceImpl @method updateAssignee referenceId :{} assigneeId:{}", referenceId,assigneeId);

        try {
            if (referenceId != null) {
                Todo todo = repository.findByReferenceId(referenceId);
                    log.debug("Inside @class TodoServiceImpl @method findAllData todo : {}", todo);
                    TodoData todoData = setTodoData(todo);
                    List<TodoAssigneeData> assigneeDataList = new ArrayList<>();
                    List<TodoHistoryData> todoHistoryDataList = new ArrayList<>();

                    if (assigneeId == null) {
                        List<TodoAssignee> assignees = todoAssigneeRepository.findAssigneesByTodoId(todo.getId());
                        log.debug("Inside @class TodoServiceImpl @method findAllData assignees : {}", assignees);
                        for (TodoAssignee assignee : assignees) {
                            TodoAssigneeData assigneeData = new TodoAssigneeData();
                            if (assignee.getAssignee()!=null){
                                // User assignor = userRest.findUserById(assignee.getAssignee().getUserid());
                                // assigneeData.setAssignee(assignor);
                                assigneeData.setAssignee(assignee.getAssignee());
                                assigneeDataList.add(assigneeData);
                                log.debug("Inside @class TodoServiceImpl @method findAllData assigneeDataList :- {}", assigneeDataList);
                                getAndSetTodoHistoryData(todo.getId(), assignee.getAssignee().getUserid(), todoHistoryDataList);
                                log.debug("Inside @class TodoServiceImpl @method findAllData todoHistoryDataList :- {}", todoHistoryDataList);
                            }

                        }
                        log.debug("Inside @class TodoServiceImpl @method findAllData - assigneeDataList ,todoHistoryDataList : {}   {}",assigneeDataList, todoHistoryDataList);
                    }else {
                        TodoAssignee assignee = todoAssigneeRepository.findTodoAssigneeByUserId( assigneeId,todo.getId());
                        log.debug("Inside @class TodoServiceImpl @method findAllData assignee : {}", assignee);
                        TodoAssigneeData assigneeData = new TodoAssigneeData();
                        if (assignee.getAssignee()!=null){
                            // User assignor = userRest.findUserById(assignee.getAssignee().getUserid());
                            // assigneeData.setAssignee(assignor);
                            assigneeData.setAssignee(assignee.getAssignee());
                            assigneeDataList.add(assigneeData);
                            log.debug("Inside @class TodoServiceImpl @method findAllData else assigneeDataList : {}", assigneeDataList);
                            getAndSetTodoHistoryData(todo.getId(), assignee.getAssignee().getUserid(), todoHistoryDataList);
                            log.debug("Inside @class TodoServiceImpl @method findAllData else todoHistoryDataList : {}", todoHistoryDataList);
                        }
                    
                    }
                
                todoData.setTodoAssigneeData(assigneeDataList);
                todoData.setTodoHistoryData(todoHistoryDataList);
                log.debug("Inside @class TodoServiceImpl @method findAllData todoData - : {}", todoData);
                return todoData;
            }
    } catch (Exception e) {
        log.error("Exception occurs @method findAllData :{} :{}", e.getMessage(), Utils.getStackTrace(e));
    }
    return null;
}


    private TodoData setTodoData(Todo todo) {
        log.debug("Inside @class TodoServiceImpl @method setTodoData todo : {}", todo);
        TodoData todoData = new TodoData();
        todoData.setId(todo.getId());
        todoData.setReferenceId(todo.getReferenceId());
        todoData.setTaskTitle(todo.getTaskTitle()!= null ? todo.getTaskTitle() : "");
        if (todo.getAssignner() != null) {
            // User assignor = userRest.findUserById(todo.getAssignner().getUserid());
            // todoData.setAssignner(assignor);
            todoData.setAssignner(todo.getAssignner());
        }
        todoData.setDescription(todo.getDescription()!= null ? todo.getDescription() : "");
        todoData.setPriority(todo.getPriority()!= null ? todo.getPriority() : "");
        // todoData.setDueDate(todo.getDueDate()!= null ? todo.getDueDate() : null);
        // todoData.setStartDate(todo.getStartDate()!= null ? todo.getStartDate() : null);
        if (todo.getDueDate() != null) {
            todoData.setDueDate(formatDate(todo.getDueDate()));
        }
        if (todo.getStartDate() != null) {
            todoData.setStartDate(formatDate(todo.getStartDate()));
        }
        todoData.setAssignnerUpdateFrequency(todo.getAssignnerUpdateFrequency()!= null ? todo.getAssignnerUpdateFrequency() : "");
        todoData.setStatus(todo.getStatus()!= null ? todo.getStatus() : "");
        todoData.setType(todo.getType()!= null ? todo.getType() : "");
        log.debug("Inside @class TodoServiceImpl @method setTodoData todoData : {}", todoData);
        return todoData;
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }

    private void getAndSetTodoHistoryData(Integer id, Integer assigneeId, List<TodoHistoryData> todoHistoryDataList) {
        TodoHistoryService todoHistoryService = ApplicationContextProvider.getApplicationContext().getBean(TodoHistoryService.class);
        List<TodoHistory> todoHistories = todoHistoryService.findTodoHistoryByTodoIdAndTodoAssignee(id, assigneeId);
        log.debug("Inside @class TodoServiceImpl @method getAndSetTodoHistoryData todoHistories : {}", todoHistories);
        for(TodoHistory history : todoHistories){
            TodoHistoryData historyData = new TodoHistoryData();
            historyData.setId(history.getId());
            historyData.setRemark(history.getRemark()!= null ? history.getRemark() : "");
            historyData.setTodoId(history.getTodo()!= null ? history.getTodo().getId() : null);
            historyData.setUserId(history.getUser().getUserid());
            historyData.setLastRemarkDate(history.getLastRemarkDate());
            historyData.setType(history.getType());
            todoHistoryDataList.add(historyData);
        }
        log.debug("Inside @class TodoServiceImpl @method findAllData todoHistoryDataList - : {}", todoHistoryDataList);
    }

    @Override
    public List<Todo> search(String filter, Integer offset, Integer size, String orderBy, String orderType, Integer assignee) {
        try {
            log.debug("Inside @class TodoServiceImpl @method search filter, offset, size, orderBy, orderType , assignee : {}, {}, {}, {}, {} , {}", 
                      filter, offset, size, orderBy, orderType, assignee);
    if(assignee!=null){            
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("enableFilterForTodo")
                   .setParameter("assignee", assignee);
                }
            return this.search(filter, offset, size, orderBy, orderType);
        } catch (Exception e) {
            log.error("Error inside search: {}", Utils.getStackTrace(e), e);
            throw new BusinessException(e.getMessage());
        }
    }

}
