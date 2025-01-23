package com.nouros.hrms.camunda.external.task;

import java.io.File;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

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
import com.nouros.hrms.wrapper.CreateAdUserDto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Component
@ExternalTaskSubscription("CreateADUser")
public class CreateADUser implements ExternalTaskHandler {

	private static final Logger log = LogManager.getLogger(CreateADUser.class);
	
	private static final long ONE_MINUTE = 1000L * 60;
	private static final int MAX_RETRIES = 4;
	private static final String LDAPURL = "LDAPURL";
	private static final String USERNAME = "USERNAME";
	private static final String PASSWORD = "PASSWORD";
	private static final String DOMAIN = "DOMAIN";
	private static final String JKS_FILE_PATH = "JKS_FILE_PATH";
	private static final String JKS_PASS = "JKS_PASS";
	private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&+?";

	@Override
	public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
		try {
			log.info("inside @class CreateADUser @method execute");
			String ldapUrl = ConfigUtils.getString(LDAPURL); 
			String username = ConfigUtils.getString(USERNAME);
			String password = ConfigUtils.getString(PASSWORD);
			String domainName = ConfigUtils.getString(DOMAIN);
			String trustStorePassword = ConfigUtils.getString(JKS_PASS);
			String processInstanceId = externalTask.getProcessInstanceId();
			EmployeeService employeeService = ApplicationContextProvider.getApplicationContext()
					.getBean(EmployeeService.class);
			Employee employee = employeeService.findByProcessInstanceId(processInstanceId);
			CreateAdUserDto userDto = new CreateAdUserDto();
			if (Objects.nonNull(employee)) {
				userDto.setFirstName(employee.getFirstName());
				userDto.setLastName(employee.getLastName());
				String email = employee.getFirstName() + "." + employee.getLastName() + "@" + domainName;
				String trimEmail = email.replace(" ", "").toLowerCase();
				userDto.setEmail(trimEmail);
				userDto.setGivenName(employee.getFirstName());
				String samAccount = userDto.getFirstName() + "." + userDto.getLastName();
				String trimSamAccount = samAccount.replace(" ", "");
				userDto.setSamAccountName(trimSamAccount);
				userDto.setUserPrincipalName(trimEmail);
			}
			createLDAPUser(ldapUrl, username, password, trustStorePassword, userDto);
			log.info("inside @class CreateADUser @method execute sucessFully createAdUser {} ", userDto.getSamAccountName());
			if(employee !=null) {
			employee.setWorkEmailAddress(userDto.getEmail());
			}else {
				log.error("Employee is null, cannot set Work Email Address");
			}
			
			employeeService.update(employee);
			externalTaskService.complete(externalTask);
		} catch (Exception e) {
			Integer retries = this.getRetries(externalTask);
			Long timeout = this.getNextTimeout(retries);
			log.error("inside @class CreateADUser @method execute exception :{}", Utils.getStackTrace(e), e);
			externalTaskService.handleFailure(externalTask, e.getMessage(), e.getMessage(), retries, timeout);
		}

	}

	private void createLDAPUser(String ldapUrl, String username, String password, String trustStorePassword,
			CreateAdUserDto userDto) throws NamingException {
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
		log.info("inside @class CreateADUser @method execute Connected to LDAP");
		Attributes userAttributes = new BasicAttributes(true);


		Attribute objClasses = new BasicAttribute("objectClass");
		objClasses.add("top");
		objClasses.add("person");
		objClasses.add("organizationalPerson");
		objClasses.add("user");


		userAttributes.put(objClasses);
		userAttributes.put("samAccountName", userDto.getSamAccountName());
		userAttributes.put("userAccountControl",512);
		userAttributes.put("name", userDto.getFirstName() + " " + userDto.getLastName());
		userAttributes.put("userPrincipalName", userDto.getEmail());
		userAttributes.put("givenName", userDto.getGivenName());
		userAttributes.put("sn", userDto.getLastName());
		userAttributes.put("displayName", userDto.getFirstName() + " " + userDto.getLastName());
		userAttributes.put("unicodePwd:\\UNI", generatePassword());


		String newUserDN = "CN=" + userDto.getFirstName() + ",";
		newUserDN += "OU=Testusers-nourOS,DC=ad,DC=aramcodigital,DC=com";
		log.info("inside @class CreateADUser @method execute going to createSubcontext");
		ctx.createSubcontext(newUserDN, userAttributes);
		ctx.close();
	}

	public static String generatePassword() {
		SecureRandom random = new SecureRandom();
		StringBuilder password = new StringBuilder(8);
		for (int i = 0; i < 8; i++) {
			password.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
		}
		log.info("inside @method generatePassword is : {}", password);
		return password.toString();
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
