package com.nouros.hrms.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.dao.impl.CustomRsqlVisitor;
import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.platform.utility.notification.model.NotificationTemplate;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.nouros.hrms.integration.service.NotificationIntegration;
import com.nouros.hrms.model.TodoAssignment;
import com.nouros.hrms.model.User;
import com.nouros.hrms.repository.TodoAssignmentRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeService;
import com.nouros.hrms.service.TodoAssignmentService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.TodoAssignmentDto;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class TodoAssignmentServiceImpl extends AbstractService<Integer, TodoAssignment>
		implements TodoAssignmentService {

	private static final String TEMPLATE_NAME = "todoAssignedTemplate";
	private static final String ASSIGNED_MESSAGE = "assignedMessage";

	@PersistenceContext
	private EntityManager entityManager;

	public TodoAssignmentServiceImpl(GenericRepository<TodoAssignment> repository) {
		super(repository, TodoAssignment.class);
	}

	@Autowired
	CustomerInfo customerInfo;

	@Autowired
	private NotificationIntegration notificationIntegration;

	@Autowired
	private TodoAssignmentRepository todoAssignmentRepository;
	
	@Autowired
	  private CommonUtils commonUtils;

	private static final Logger log = LogManager.getLogger(TodoAssignmentServiceImpl.class);

	@Override
	public TodoAssignment create(TodoAssignment todoAssignment) {
		log.info("inside @class TodoAssignmentServiceImpl @method create");
		List<User> users = todoAssignment.getUsers();

		if (users != null && !users.isEmpty() && todoAssignment.getProgress().equalsIgnoreCase("NOT_STARTED")) {
			try {
				for (User user : users) {
					log.debug("inside @method create user coming  is : {} ", user.toString());
					String assignedMessage = "X task has been assigned to you by Z ";
					String modifiedAssignedMessage = assignedMessage.replace("X", todoAssignment.getTaskName());
					log.debug("inside @method create modifiedAssignedMessage after task replaced is : {} ",
							modifiedAssignedMessage);
					modifiedAssignedMessage = modifiedAssignedMessage.replace("Z", customerInfo.getUserFullname());
					log.debug("inside @method create modifiedAssignedMessage after creator replaced is : {} ",
							modifiedAssignedMessage);
					sendNotification(modifiedAssignedMessage, user.getUserName());
				}
			} catch (Exception e) {
				log.error("SomeThing Went Wrong While Sending TodoAssignment Notification In Case of Assigning");
			}
		}
		return todoAssignmentRepository.save(todoAssignment);
	}

	private void sendNotification(String message, String userName) {
		NotificationTemplate notificationTemplate = notificationIntegration.getTemplte(TEMPLATE_NAME);
		log.debug("notificationTemplate found is : {} ", notificationTemplate);
		JSONObject payload = new JSONObject();
		payload.put(ASSIGNED_MESSAGE, message);
		notificationIntegration.sendNotification(notificationTemplate, payload, userName);
	}

	@Override
	public TodoAssignment update(TodoAssignment todoAssignment) {
		log.info("inside @class TodoAssignmentServiceImpl @method update");
		List<User> users = todoAssignment.getUsers();
		if (users != null && !users.isEmpty() && todoAssignment.getProgress().equalsIgnoreCase("IN_PROGRESS")) {
			try {
				for (User user : users) {
					log.debug("inside @method update user coming  is : {} ", user.toString());
					String assignedMessage = "X task has been modified by Z ";
					String modifiedAssignedMessage = assignedMessage.replace("X", todoAssignment.getTaskName());
					log.debug("inside @method modifiedAssignedMessage after task replaced is : {} ",
							modifiedAssignedMessage);
					modifiedAssignedMessage = modifiedAssignedMessage.replace("Z", customerInfo.getUserFullname());
					log.debug("inside @method update modifiedAssignedMessage after creator replaced is : {} ",
							modifiedAssignedMessage);
					sendNotification(modifiedAssignedMessage, user.getUserName());
				}
			} catch (Exception e) {
				log.error("SomeThing Went Wrong While Sending TodoAssignment Notification In Case of Modification ");
			}
		} else if (users != null && !users.isEmpty() && todoAssignment.getProgress().equalsIgnoreCase("COMPLETED")) {
			try {
				for (User user : users) {
					log.debug("inside @method update else if user coming  is : {} ", user.toString());
					String assignedMessage = "X task has been completed by Z ";
					String modifiedAssignedMessage = assignedMessage.replace("X", todoAssignment.getTaskName());
					log.debug("modifiedAssignedMessage after task replaced is : {} ", modifiedAssignedMessage);
					modifiedAssignedMessage = modifiedAssignedMessage.replace("Z", customerInfo.getUserFullname());
					log.debug("inside @method update else if modifiedAssignedMessage after creator replaced is : {} ",
							modifiedAssignedMessage);
					sendNotification(modifiedAssignedMessage, user.getUserName());
				}
			} catch (Exception e) {
				log.error("SomeThing Went Wrong While Sending TodoAssignment Notification In Case of Completion");
			}
		}
		return todoAssignmentRepository.save(todoAssignment);
	}

	@Override
	public String softDelete(int id) {
		log.info("Inside method softDelete");
		try {
			TodoAssignment todoAssignment = super.findById(id);

			if (todoAssignment != null) {

				TodoAssignment todoAssignment1 = todoAssignment;
				todoAssignment1.setIsDeleted(true);
				todoAssignmentRepository.save(todoAssignment1);
				log.info("ToDoAssignment soft deleted successfully");
				return APIConstants.SUCCESS_JSON;
			}
		} catch (Exception e) {
			log.debug("SomeThing Went Wrong While Deleting TodoAssignments");
		}
		return APIConstants.FAILURE_JSON;
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
	public List<TodoAssignment> getByTodoBucketId(Integer id) {
		log.info("inside @class TodoAssignmentServiceImpl @method getByTodoBucketId");
		log.debug(" Inside @findAllBuckets  customerId is : {}", commonUtils.getCustomerId());
		return todoAssignmentRepository.getByTodoBucketId(id,commonUtils.getCustomerId());
	}

	@Override
	public List<TodoAssignment> search(String query, Integer offset, Integer size, String orderby, String orderType) {
		logger.info("Inside Search Method");
		if (query == null) {
			return Collections.emptyList();
		}
		return searchByFilterWithLimit(TodoAssignment.class, query, orderby, orderType, offset, size);
	}

	@SuppressWarnings("unchecked")
	public List<TodoAssignment> searchByFilterWithLimit(Class<TodoAssignment> type, String query, String orderBy,
			String orderType, Integer offset, Integer size) {
		logger.debug("Inside searchByFilter Method for TodoAssignment, with query: {}", query);
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<TodoAssignment> criteria = builder.createQuery(type);
		Root<TodoAssignment> root = criteria.from(type);

		logger.debug("Root information For Search: {}", root);
		logger.debug("Root information class For Search: {}", root.getClass());
		logger.debug("Root information java type For Search: {}", root.getJavaType());

		try {
			Node rootNode = new RSQLParser().parse(query);
			CustomRsqlVisitor<TodoAssignment> visitor = new CustomRsqlVisitor<>();
			Specification<TodoAssignment> specification = rootNode.accept(visitor);
			Predicate predicate = specification.toPredicate(root, criteria, builder);

			criteria.where(predicate);
			if (orderBy != null && orderType != null) {
				if ("desc".equalsIgnoreCase(orderType)) {
					criteria.orderBy(builder.desc(root.get(orderBy)));
				} else {
					criteria.orderBy(builder.asc(root.get(orderBy)));
				}
			}
			TypedQuery<TodoAssignment> typedQuery = entityManager.createQuery(criteria);
			if (offset >= 0 && size >= 0) {
				typedQuery.setMaxResults(size);
				typedQuery.setFirstResult(offset);
			}
			Session session = entityManager.unwrap(Session.class);
			log.debug("UserId fetched for login user is : {}", customerInfo.getUserId());
			session.enableFilter("todoAssignmentUserIdInFilter").setParameter("userId", customerInfo.getUserId());

			return typedQuery.getResultList();
		} catch (Exception e) {
			logger.error("Error in searchByFilter: {}", e.getMessage(), e);
		}
		return Collections.emptyList();
	}

	@Override
	public Long count(String query) {
		logger.info("Inside count Method");
		if (query == null) {
			return null;
		}
		return countByFilterWithLimit(TodoAssignment.class, query);
	}

	@SuppressWarnings("unchecked")
	public Long countByFilterWithLimit(Class<TodoAssignment> type, String query) {
		logger.debug("Inside countByFilter Method for TodoAssignment, with query: {}", query);
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<TodoAssignment> root = criteria.from(type);

		logger.debug("Root information For Count: {}", root);
		logger.debug("Root information class For Count: {}", root.getClass());
		logger.debug("Root information java type For Count: {}", root.getJavaType());

		try {
			Node rootNode = new RSQLParser().parse(query);
			CustomRsqlVisitor<TodoAssignment> visitor = new CustomRsqlVisitor<>();
			Specification<TodoAssignment> specification = rootNode.accept(visitor);
			Predicate predicate = specification.toPredicate(root, criteria, builder);

			criteria.where(predicate);
			criteria.select(builder.countDistinct(root));

			Session session = entityManager.unwrap(Session.class);
			log.debug("UserId fetched for login user is : {}", customerInfo.getUserId());
			session.enableFilter("todoAssignmentUserIdInFilter").setParameter("userId", customerInfo.getUserId());
			return entityManager.createQuery(criteria).getSingleResult();
		} catch (Exception e) {
			logger.error("Error in countByFilter: {}", e.getMessage(), e);
		}
		return 0L;
	}

	@Override
	public TodoAssignment createTodo(TodoAssignmentDto todoAssignmentDto) {
		log.debug("inside  @method createTodo todoAssignmentDto is : {}", todoAssignmentDto);

		TodoAssignment todoAssignment = new TodoAssignment();

		List<String> userNames = new ArrayList<>();
		List<User> users = new ArrayList<>();
		EmployeeService employeeService = ApplicationContextProvider.getApplicationContext()
				.getBean(EmployeeService.class);

		if (todoAssignmentDto != null && todoAssignmentDto.getUserNames() != null) {
			log.debug("UserNames are : {}", todoAssignmentDto.getUserNames());

			userNames = todoAssignmentDto.getUserNames();
			log.debug("UserNames fetched from dto are  : {}", todoAssignmentDto.getUserNames());
			users = employeeService.getUserFromName(userNames);
		}

		setDataInTodoAssignmentObj(todoAssignmentDto, todoAssignment, users);

		log.debug("Users are : {}", users);

		if (users != null && !users.isEmpty() && todoAssignment.getProgress().equalsIgnoreCase("NOT_STARTED")) {
			try {
				for (User user : users) {
					log.debug("inside @method create user coming  is : {} ", user.toString());
					String assignedMessage = "X task has been assigned to you by Z ";
					String modifiedAssignedMessage = assignedMessage.replace("X", todoAssignment.getTaskName());
					log.debug("inside @method create modifiedAssignedMessage after task replaced is : {} ",
							modifiedAssignedMessage);
					modifiedAssignedMessage = modifiedAssignedMessage.replace("Z", customerInfo.getUserFullname());
					log.debug("inside @method create modifiedAssignedMessage after creator replaced is : {} ",
							modifiedAssignedMessage);
					sendNotification(modifiedAssignedMessage, user.getUserName());
				}
			} catch (Exception e) {
				log.error("SomeThing Went Wrong While Sending TodoAssignment Notification In Case of Assigning");
			}
		}

		return todoAssignmentRepository.save(todoAssignment);
	}

	private void setDataInTodoAssignmentObj(TodoAssignmentDto todoAssignmentDto, TodoAssignment todoAssignment,
			List<User> users) {

		log.debug("inside  @method setDataInTodoAssignmentObj todoAssignmentDto is : {}", todoAssignmentDto);

		if (todoAssignmentDto.getProgress() != null) {
			log.debug("Progress set is : {}", todoAssignmentDto.getProgress());
			todoAssignment.setProgress(todoAssignmentDto.getProgress());

		}
		if (todoAssignmentDto.getPriority() != null) {
			log.debug("Priority set is : {}", todoAssignmentDto.getPriority());
			todoAssignment.setPriority(todoAssignmentDto.getPriority());
		}
		if (todoAssignmentDto.getRepeat() != null) {
			log.debug("Repeat set is : {}", todoAssignmentDto.getRepeat());
			todoAssignment.setRepeat(todoAssignmentDto.getRepeat());
		}

		if (todoAssignmentDto.getNotes() != null) {
			todoAssignment.setNotes(todoAssignmentDto.getNotes());
		}

		if (todoAssignmentDto.getAttachments() != null) {
			todoAssignment.setAttachments(todoAssignmentDto.getAttachments());
		}

		if (todoAssignmentDto.getCustomConfig() != null) {
			todoAssignment.setCustomConfig(todoAssignmentDto.getCustomConfig());
		}

		if (todoAssignmentDto.getTodoBucket() != null) {
			log.debug("Bucket set is : {}", todoAssignmentDto.getTodoBucket());
			todoAssignment.setTodoBucket(todoAssignmentDto.getTodoBucket());
		}

		if (users != null) {
			log.debug("User set is : {}", users);
			todoAssignment.setUsers(users);
		}

		if (todoAssignmentDto.getStartDate() != null) {
			todoAssignment.setStartDate(todoAssignmentDto.getStartDate());
		}

		if (todoAssignmentDto.getIsDeleted() != null) {
			todoAssignment.setIsDeleted(todoAssignmentDto.getIsDeleted());
		}

		if (todoAssignmentDto.getDueDate() != null) {
			todoAssignment.setDueDate(todoAssignmentDto.getDueDate());
		}

		if (todoAssignmentDto.getTaskName() != null) {
			log.debug("TaskName set is : {}", todoAssignmentDto.getTaskName());
			todoAssignment.setTaskName(todoAssignmentDto.getTaskName());
		}
	}

	@Override
	public TodoAssignment updateTodo(TodoAssignmentDto todoAssignmentDto) {
		log.debug("inside  @method updateTodo todoAssignmentDto is : {}", todoAssignmentDto);

		TodoAssignment todoAssignment;

		if (todoAssignmentDto != null && todoAssignmentDto.getId() != null) {

			todoAssignment = super.findById(todoAssignmentDto.getId());

		} else {
			log.error("Please provide TodoAssignment Id");
			throw new BusinessException("Please provide TodoAssignment Id ");
		}

		// TodoAssignment todoAssignment =
		// todoAssignmentRepository.findById(todoAssignmentDto.getId()).orElse(null);

		log.debug("inside  @method updateTodo Old  todoAssignmentobj  is : {}", todoAssignment);

		List<String> userNames = new ArrayList<>();
		List<User> users = new ArrayList<>();
		EmployeeService employeeService = ApplicationContextProvider.getApplicationContext()
				.getBean(EmployeeService.class);

		if (todoAssignmentDto != null && todoAssignmentDto.getUserNames() != null) {
			log.debug("UserNames are : {}", todoAssignmentDto.getUserNames());

			userNames = todoAssignmentDto.getUserNames();
			log.debug("UserNames fetched from dto are  : {}", todoAssignmentDto.getUserNames());
			users = employeeService.getUserFromName(userNames);
		}

		setDataInTodoAssignmentObj(todoAssignmentDto, todoAssignment, users);

		log.debug("Users are : {}", users);

		if (users != null && !users.isEmpty() && todoAssignmentDto.getProgress().equalsIgnoreCase("IN_PROGRESS")) {
			try {
				for (User user : users) {
					log.debug("user coming when progress is in progress: {} ", user.toString());
					String assignedMessage = "X task has been modified by Z ";
					String modifiedAssignedMessage = assignedMessage.replace("X", todoAssignment.getTaskName());
					log.debug("inside  @method updateTodo modifiedAssignedMessage after task replaced is : {} ",
							modifiedAssignedMessage);
					modifiedAssignedMessage = modifiedAssignedMessage.replace("Z", customerInfo.getUserFullname());
					log.debug(
							"inside  @method updateTodo modifiedAssignedMessage modifiedAssignedMessage after creator replaced is : {} ",
							modifiedAssignedMessage);
					sendNotification(modifiedAssignedMessage, user.getUserName());
				}
			} catch (Exception e) {
				log.error("SomeThing Went Wrong While Sending TodoAssignment Notification In Case of Modification ");
			}
		} else if (users != null && !users.isEmpty() && todoAssignmentDto.getProgress().equalsIgnoreCase("COMPLETED")) {
			try {
				for (User user : users) {
					log.debug("@method updateTodo user coming  is : {} ", user.toString());
					String assignedMessage = "X task has been completed by Z ";
					String modifiedAssignedMessage = assignedMessage.replace("X", todoAssignment.getTaskName());
					log.debug("inside  @method updateTodo else if modifiedAssignedMessage after task replaced is : {} ",
							modifiedAssignedMessage);
					modifiedAssignedMessage = modifiedAssignedMessage.replace("Z", customerInfo.getUserFullname());
					log.debug("inside  @method updateTodo modifiedAssignedMessage after creator replaced is : {} ",
							modifiedAssignedMessage);
					sendNotification(modifiedAssignedMessage, user.getUserName());
				}
			} catch (Exception e) {
				log.error("SomeThing Went Wrong While Sending TodoAssignment Notification In Case of Completion");
			}
		}
		return todoAssignmentRepository.save(todoAssignment);

	}

}
