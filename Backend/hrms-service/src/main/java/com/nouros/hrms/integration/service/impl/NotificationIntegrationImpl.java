package com.nouros.hrms.integration.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.platform.utility.notification.mail.model.NotificationAttachment;
import com.enttribe.platform.utility.notification.mail.rest.INotificationMailRest;
import com.enttribe.platform.utility.notification.mail.wrapper.NotificationMailWrapper;
import com.enttribe.platform.utility.notification.model.NotificationTemplate;
import com.enttribe.platform.utility.notification.model.NotificationTemplateDetail;
import com.enttribe.platform.utility.notification.model.NotificationTemplateDetail.TemplateType;
import com.enttribe.platform.utility.notification.rest.INotificationRest;
import com.enttribe.platform.utility.notification.rest.INotificationTemplateRest;
import com.enttribe.platform.utility.notification.utils.NotificationUtils;
import com.enttribe.platform.utility.notification.wrapper.BaseNotificationContentHolder;
import com.enttribe.platform.utility.notification.wrapper.BaseNotificationHolder;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.product.security.wrapper.UserWrapper;
import com.enttribe.utils.Utils;
import com.google.gson.Gson;
import com.nouros.hrms.integration.service.NotificationIntegration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class NotificationIntegrationImpl implements NotificationIntegration {

	private static final Logger log = LogManager.getLogger(NotificationIntegrationImpl.class);

	@Autowired
	private INotificationMailRest emailNotificationRest;

	@Autowired
	private INotificationTemplateRest notificationTemplateRest;

	@Autowired
	private CustomerInfo customerInfo;

	@Autowired
	private INotificationRest notificationRest;

	@Override
	public void sendEmail(NotificationTemplate notificationTemplate, JSONObject payload, String toEmail, String ccUser,
			List<NotificationAttachment> notificationAttachmentList) {
		try {
			log.debug("inside send Email with notificationTemplate and notificationDetail");

			NotificationTemplateDetail emailTemplate = getEmailTemplateContent(notificationTemplate);
			log.debug("notificationTemplateDetail emailTemplate  is : {} ", emailTemplate);
			if (emailTemplate != null) {
				log.debug("inside if condition when  emailTemplate  is found : {} ", emailTemplate);
				NotificationMailWrapper inputJson = setInputJson(notificationTemplate, payload,
						notificationAttachmentList, emailTemplate);

				Set<String> toEmailSet = new HashSet<>();
				Set<String> ccUserSet = new HashSet<>();
				if (null != emailTemplate.getToEmail() && !emailTemplate.getToEmail().trim().isEmpty()) {
					toEmailSet.add(HtmlUtils.htmlUnescape(emailTemplate.getToEmail()));
				}
				if (null != emailTemplate.getCcEmail() && !emailTemplate.getCcEmail().trim().isEmpty()) {
					ccUserSet.add(HtmlUtils.htmlUnescape(emailTemplate.getCcEmail()));
				}
				log.debug("Inside @method sendEmail toEmailSet,ccUserSet:{} {}", toEmailSet, ccUserSet);
				if (null != toEmail && !toEmail.trim().isEmpty()) {
					toEmailSet.add(getIndividualEmails(toEmail));
				}
				if (null != ccUser && !ccUser.trim().isEmpty()) {
					ccUserSet.add(getIndividualEmails(ccUser));
				}
				log.debug("Inside @method sendEmail toEmail,ccUser:{} {}", toEmail, ccUser);
				log.debug("Inside @method sendEmail toEmailSet,ccUserSet:{} {}", toEmailSet, ccUserSet);
				inputJson.setToEmailIds(toEmailSet);
				inputJson.setCcEmailIds(ccUserSet);
				log.debug("Inside @method sendEmail inputJson:{}", inputJson);
				log.info("Inside @method sendEmail before sending");
				String responce = emailNotificationRest.sendEmail(inputJson, true);
				log.debug("Inside @method sendEmail responce:{}", responce);
			}
		} catch (Exception e) {
			log.error("Error occured inside  @method sendEmail {},{}", Utils.getStackTrace(e), e);
		}
	}

	private NotificationMailWrapper setInputJson(NotificationTemplate notificationTemplate, JSONObject payload,
			List<NotificationAttachment> notificationAttachmentList, NotificationTemplateDetail emailTemplate) {
		String templateContent = HtmlUtils.htmlUnescape(emailTemplate.getContent());
		String text = NotificationUtils.mapTemplateByComponent(payload, templateContent, TemplateType.EMAIL);
		String emailSubject = HtmlUtils.htmlUnescape(emailTemplate.getSubject());
		String subject = NotificationUtils.mapTemplateByComponent(payload, emailSubject, TemplateType.EMAIL);
//		Set<String> emptySet=new HashSet<>();
//		emptySet.add("pintu.sahu@bootnext.biz");
		NotificationMailWrapper inputJson = new NotificationMailWrapper();
		inputJson.setSubject(subject);
//		inputJson.setBccEmailIds(emptySet);
		inputJson.setTemplateName(notificationTemplate.getName());
		inputJson.setEmailContent(text);
		inputJson.setFromEmail("info@enttribe.com");
		if (!notificationAttachmentList.isEmpty()) {
			inputJson.setAttachment(notificationAttachmentList);
		}
		return inputJson;
	}

	private static NotificationTemplateDetail getEmailTemplateContent(NotificationTemplate notificationTemplate) {
		Set<NotificationTemplateDetail> notificationTemplateTypes = notificationTemplate.getTemplateType();
		log.debug("Inside @method getEmailTemplateContent notificationTemplateTypes : {}", notificationTemplateTypes);
		return notificationTemplateTypes.stream()
				.filter(ntt -> NotificationTemplateDetail.TemplateType.EMAIL.equals(ntt.getTemplatetype())).findFirst()
				.orElse(null);
	}

	private String getIndividualEmails(String emails) {
		emails = emails.replaceAll("[\\[\\]]", "");
		log.debug("Inside @method getIndividualEmails emails:{}", emails);
		String[] emailArray = emails.split(",");
		for (int i = 0; i < emailArray.length; i++) {
			emailArray[i] = emailArray[i].trim();
		}
		return String.join(",", emailArray);
	}

	private static NotificationTemplateDetail getNotificationTemplate(NotificationTemplate notificationTemplate) {
		Set<NotificationTemplateDetail> notificationTemplateTypes = notificationTemplate.getTemplateType();
		log.debug("notificationTemplateTypes fechted  is : {} ", notificationTemplateTypes);
		return notificationTemplateTypes.stream()
				.filter(ntt -> NotificationTemplateDetail.TemplateType.NOTIFICATION.equals(ntt.getTemplatetype()))
				.findFirst().orElse(null);
	}

	@Override
	public NotificationTemplate getTemplte(String name) {
		return notificationTemplateRest.templateByName(name);

	}

	private UserWrapper getCurrentUserDetails() {
		try {
			return customerInfo.getCustomerInfo();
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public void sendNotification(NotificationTemplate notificationTemplate, JSONObject payload, String toUser) {
		try {
			log.debug("send Notification with 3 parameter payload : {} ,toUser : {} ", payload, toUser);
			List<String> toUserNameList = new ArrayList<>();
			NotificationTemplateDetail notificationTemplateDetail = getNotificationTemplate(notificationTemplate);
			String text = "";
			if (null != notificationTemplateDetail) {
				log.debug("notificationTemplateDetail fecthed  is : {} ", notificationTemplateDetail.toString());
				Map<String, Object> notificationPropertiesMap = new HashMap<>();
				BaseNotificationHolder notificationWrapper = new BaseNotificationHolder();
				Map<String, BaseNotificationContentHolder> map = new HashMap<>();
				notificationPropertiesMap.put("date", LocalDate.now().toString());
				BaseNotificationContentHolder genericNotificationContentWrapper = new BaseNotificationContentHolder();
				genericNotificationContentWrapper.setTitle(notificationTemplateDetail.getTitle());
				genericNotificationContentWrapper.setTemplateName(notificationTemplate.getName());
				try {
					 text = NotificationUtils.mapTemplateByComponent(payload,
							notificationTemplateDetail.getContent(), TemplateType.NOTIFICATION);
					log.debug("text response is : {} ", text);
					String actionJson = notificationTemplate.getActionJson();
					if(actionJson!=null && !actionJson.isEmpty()){
						actionJson = NotificationUtils.mapTemplateByComponent(payload,
						actionJson, TemplateType.NOTIFICATION);
					}
					genericNotificationContentWrapper.setActionJson(actionJson);

				} catch (Exception e) {
					log.error(
							"Inside @method sendNotification Getting Error mapTemplateByComponent , stacktrace : {} , exception : {} ",
							Utils.getStackTrace(e), e);
				}

				 
				Map<String, String> mobileNotificationPropertiesMap = setMobileNotificationMap(
						notificationTemplateDetail.getTitle(), text);
				log.debug("mobileNotificationPropertiesMap formed  is : {} ", mobileNotificationPropertiesMap);
				genericNotificationContentWrapper.setNotificationText(text);
				map.put("en", genericNotificationContentWrapper);
				notificationWrapper.setWebContent(map);
				notificationWrapper.setSender(this.getCurrentUserDetails().getUsername());
				notificationWrapper.setPushNotificationRequired(true);
				log.debug("notificationWrapper formed is : {} ", notificationWrapper);
				if (!mobileNotificationPropertiesMap.isEmpty()) {
					Map<String, BaseNotificationContentHolder> notificationMobileData = new HashMap<>();
					notificationMobileData.put("en", genericNotificationContentWrapper);
					notificationWrapper.setMobileContent(notificationMobileData);
				}
				if (null != toUser && !toUser.trim().isEmpty()) {
					toUserNameList.add(getIndividualEmails(toUser));
				}
				notificationWrapper.setRecipientList(toUserNameList);
				log.debug("Inside @method sendNotification notificationWrapper:{}", notificationWrapper);
				String responce = notificationRest.createNotificationWithoutUserChoices(notificationWrapper);
				log.debug("Inside @method sendNotification responce:{}", responce);
			}

		} catch (Exception e) {
			log.error(
					"Inside @method sendNotification Getting Error While Sending notification, stacktrace : {} , exception : {}  ",
					Utils.getStackTrace(e), e);
			// throw new BusinessException(e.getMessage());
		}
	}

	public Map<String, String> setMobileNotificationMap(String title, String text) {
		log.debug("inside setMobileNotificationMap  title : {} ,text : {} ", title, text);
		Map<String, String> mobileNotificationPropertiesMap = new HashMap<>();
		mobileNotificationPropertiesMap.put("title", title);
		mobileNotificationPropertiesMap.put("content", text);
		log.debug("mobileNotificationPropertiesMap formed is : {} ", mobileNotificationPropertiesMap);
		return mobileNotificationPropertiesMap;
	}

}
