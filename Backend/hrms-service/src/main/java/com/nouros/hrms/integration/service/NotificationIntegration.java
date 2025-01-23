package com.nouros.hrms.integration.service;

import java.util.List;

import org.json.JSONObject;

import com.enttribe.platform.utility.notification.mail.model.NotificationAttachment;
import com.enttribe.platform.utility.notification.model.NotificationTemplate;

public interface NotificationIntegration {

	void sendEmail(NotificationTemplate notificationTemplate, JSONObject payload, String toEmail, String ccUser,List<NotificationAttachment> notificationAttachment);

	public NotificationTemplate getTemplte(String name);

	public void sendNotification(NotificationTemplate notificationTemplate, JSONObject payload, String toUser);
}
