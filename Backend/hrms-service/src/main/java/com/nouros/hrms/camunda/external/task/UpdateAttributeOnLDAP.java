package com.nouros.hrms.camunda.external.task;

import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

import com.enttribe.commons.configuration.ConfigUtils;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.utils.Utils;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.service.EmployeeService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Component
@ExternalTaskSubscription("updateUserOnLDAP")
public class UpdateAttributeOnLDAP implements ExternalTaskHandler {

	private static final Logger log = LogManager.getLogger(UpdateAttributeOnLDAP.class);

	private static final long ONE_MINUTE = 1000L * 60;
	private static final int MAX_RETRIES = 4;
	private static final String LDAPURL = "LDAPURL";
	private static final String USERNAME = "USERNAME";
	private static final String PASSWORD = "PASSWORD";
	private static final String JKS_FILE_PATH = "JKS_FILE_PATH";
	private static final String JKS_PASS = "JKS_PASS";

	public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

		try {
 
			log.info("inside @class CreateUser @method execute");
			String processInstanceId = externalTask.getProcessInstanceId();
			EmployeeService employeeService = ApplicationContextProvider.getApplicationContext()
					.getBean(EmployeeService.class);
			Employee employee = employeeService.findByProcessInstanceId(processInstanceId);
			String manager = employee.getReportingManager().getWorkEmailAddress();
			String employeeType = employee.getEmploymentType();
			String departMent = employee.getDepartment().getName();
			String email = employee.getWorkEmailAddress();

			log.debug("inside @class CreateUser @method execute manager {},employeeType {},departMent {},email {}",manager,employeeType,departMent,email);
			
			String ldapUrl = ConfigUtils.getString(LDAPURL);
			String username = ConfigUtils.getString(USERNAME);
			String password = ConfigUtils.getString(PASSWORD);
			String trustStorePassword = ConfigUtils.getString(JKS_PASS);

			Map<String, String> env = new HashMap<>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, ldapUrl);
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, username);
			env.put(Context.SECURITY_CREDENTIALS, password);

			File file = new File(ConfigUtils.getString(JKS_FILE_PATH));
			String trustStoreFilePath = file.getAbsolutePath();
			System.setProperty("javax.net.ssl.trustStore", trustStoreFilePath);
			System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);

			DirContext ctx = new InitialDirContext(new Hashtable<>(env));
			ModificationItem[] modificationItems = new ModificationItem[4];
			modificationItems[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("manager", manager));
			modificationItems[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("workEmail", email));
			modificationItems[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("employeeType", employeeType));
			modificationItems[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("department", departMent));

			String newUserDN = "CN=" + employee.getFirstName() + ",";
			newUserDN += "OU=Testusers-nourOS,DC=ad,DC=aramcodigital,DC=com";

			ctx.modifyAttributes(newUserDN, modificationItems);

			log.info("inside @class updateAttribute @method execute after modification");
			
			ctx.close();
			
			externalTaskService.complete(externalTask);

		} catch (Exception e) {
			Integer retries = this.getRetries(externalTask);
			Long timeout = this.getNextTimeout(retries);
			log.error("inside @class CreateUser @method execute exception : {}", Utils.getStackTrace(e), e);
			externalTaskService.handleFailure(externalTask, e.getMessage(), e.getMessage(), retries, timeout);
		}

	}

	private Integer getRetries(ExternalTask task) {
		Integer retries = task.getRetries();
		if (retries == null) {
			retries = MAX_RETRIES;
		} else {
			retries = retries - 1;
		}
		return retries;
	}

	private Long getNextTimeout(Integer retries) {
		return ONE_MINUTE * (MAX_RETRIES - retries);
	}

}
