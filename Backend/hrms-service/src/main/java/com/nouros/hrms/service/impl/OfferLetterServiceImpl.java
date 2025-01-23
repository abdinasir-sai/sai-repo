package com.nouros.hrms.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;

import com.enttribe.commons.configuration.ConfigUtils;
import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.platform.utility.notification.mail.model.NotificationAttachment;
import com.enttribe.platform.utility.notification.model.NotificationTemplate;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.enttribe.product.storagesystem.rest.StorageRest;
import com.enttribe.usermanagement.user.model.User;
import com.enttribe.usermanagement.user.rest.UserRest;
import com.enttribe.utils.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.pdf.BaseFont;
import com.lowagie.text.DocumentException;
import com.nouros.hrms.integration.service.NotificationIntegration;
import com.nouros.hrms.service.OfferLetterService;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.HttpResponseWrapper;
import com.nouros.hrms.wrapper.OfferLetterWrapper;

@Service
public class OfferLetterServiceImpl implements OfferLetterService {

	@Autowired
	private NotificationIntegration notificationIntegration;

	@Autowired
	private UserRest userRest;

	@Autowired
	CustomerInfo customerInfo;

	private static final String ATTACHMENT_FILE_PATH = "ATTACHMENT_FILE_PATH";
	private static final String IMG = "img";
	private static final String BR = "br";
	private static final String PDF = ".pdf";

	@Value("${ROOT_DIR_HRMS_PAYROLL_FILE}")
	private String rootDirBucketName;

	@Autowired
	private StorageRest storageRest;

	private static final Logger log = LogManager.getLogger(OfferLetterServiceImpl.class);

	private User getUserContext() {
		return userRest.byUserName(customerInfo.getUsername());
	}

	@Override
	public String sendHtmlContent(OfferLetterWrapper offerLetterWrapper) {

		log.info("Inside @class OfferLetterServiceImpl @method sendOfferLetter ");
		try {

			JSONObject jsonObject = getJsonOfWrapper(offerLetterWrapper);
			NotificationTemplate template = notificationIntegration.getTemplte("offer_letter_1");
			log.debug("The Template :{} ", template);
			String filePath = "/opt/visionwaves/hrms/offerLetter/OfferLetter.html";
			StringBuilder content = new StringBuilder();
	 
			try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

				String line;
				while ((line = reader.readLine()) != null) {
					content.append(line).append("\n");
				}
			} catch (Exception e) {
				log.error("Error inside html :{} :{}", e.getMessage(), Utils.getStackTrace(e));
				throw new BusinessException();
			}

			String htmlContent = content.toString();
			log.debug("Html Content :{}", htmlContent);
			String correctHtmlContent = correctHtmlTemplate(htmlContent);
			log.debug("Html Content after  correctHtmlTemplate :{}", correctHtmlContent);
			String replaceHtml = replaceVariable(correctHtmlContent, offerLetterWrapper);
			log.debug("Replaced Content :{}", replaceHtml);
 			return replaceHtml;

		} catch (Exception e) {
			log.error("Error inside @class OfferLetterServiceImpl @method sendOfferLetter :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}

	}

	public JSONObject getJsonOfWrapper(OfferLetterWrapper offerLetterWrapper) {
		log.info("Inside @class OfferLetterServiceImpl @method getJsonOfWrapper ");
		try {

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", offerLetterWrapper.getName());
			jsonObject.put("designation", offerLetterWrapper.getDesignation());
			jsonObject.put("Uemail", "x101@visionwaves.com");
			jsonObject.put("Uphone", "VisionWave");
			return jsonObject;
		} catch (Exception e) {
			log.error("Error inside @class OfferLetterServiceImpl @method getJsonOfWrapper :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

	public String extractTemplateData(NotificationTemplate template) {
		StringBuilder contentBuilder = new StringBuilder();
		try {
			for (Field field : template.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				contentBuilder.append(field.getName()).append(": ").append(field.get(template)).append("\n");
			}
		} catch (IllegalAccessException e) {
			log.error("Error inside @class OfferLetterServiceImpl @method extractTemplateData :{} :{}", e.getMessage(),
					Utils.getStackTrace(e));
		}
		return contentBuilder.toString();
	}

	public static String correctHtmlTemplate(String htmlTemplate) {
		Document doc = Jsoup.parse(htmlTemplate, "", Parser.xmlParser());
		for (Element img : doc.select(IMG)) {
			img.tagName(IMG);
			if (!img.hasAttr("/")) {
				img.attr("/", "");
			}
		}
		for (Element br : doc.select(BR)) {
			br.tagName(BR);
			if (!br.hasAttr("/")) {
				br.attr("/", "");
			}
		}
		String correctedHtml = doc.toString();
		try {
			Jsoup.parse(correctedHtml, "", Parser.xmlParser());
		} catch (Exception e) {
			System.err.println("HTML is not parseable after corrections: " + e.getMessage());
			return null;
		}
		return correctedHtml;
	}

	private String replaceVariable(String htmlTemplate, OfferLetterWrapper offerLetterWrapper) {
		String referenceId = offerLetterWrapper.getName();
		String finalReferenceNumber = "OfferLetter" + "/" + referenceId;
		VelocityContext context = new VelocityContext();

		context.put("gender", offerLetterWrapper.getGender());
		context.put("name", offerLetterWrapper.getName());
		context.put("gradeCode", offerLetterWrapper.getGradeCode());
		context.put("designation", offerLetterWrapper.getDesignation());
		context.put("basicSalary", offerLetterWrapper.getBasicSalary());
		context.put("housing", offerLetterWrapper.getHousing());
		context.put("transportation", offerLetterWrapper.getTransportation());
		context.put("mobile", offerLetterWrapper.getMobile());
		context.put("location", offerLetterWrapper.getLocation());
		context.put("organization", offerLetterWrapper.getOrganization());

		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.init();
		StringWriter writer = new StringWriter();
		velocityEngine.evaluate(context, writer, "OfferLetter", htmlTemplate);
		return writer.toString();
	}

	private byte[] renderHtmlToPdf(String htmlContent) {
		try {
			htmlContent = htmlContent.replace("\\", "").replace("\n", "");
			log.debug("The html in renderHtmlToPdf :{} ", htmlContent);
			ITextRenderer renderer = new ITextRenderer();
			log.info(" renderer");
			renderer.setDocumentFromString(htmlContent);
			log.info(" setDocumentFromString");
			renderer.layout();
			log.info(" layout");
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			renderer.createPDF(outputStream);
			return outputStream.toByteArray();
		} catch (Exception e) {
			log.error("Exception occurred in renderHtmlToPdf: {}", e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException("Error occurred while converting to pdf bytes", e);

		}
	}

	private MultipartFile createMultipartFile(byte[] pdfByte, String fileNames) {
		return new MockMultipartFile(fileNames, fileNames, MediaType.MULTIPART_FORM_DATA_VALUE, pdfByte);
	}

	private String getCurrentDate() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
		return now.format(formatter);

	}

	public HttpResponseWrapper sendPostRequestWithFile(String methodUrl, byte[] bytes, MultipartFile file) {
		log.info("Inside @method sendPostRequestWithFile");
		HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
		try {
			HttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(methodUrl);
			httpPost.setEntity(createMultipartEntity(bytes, file));
			parseResponseFromClient(httpResponseWrapper, httpClient, httpPost);
			log.debug("HTTP_RESPONSE_WRAPPER :{}", httpResponseWrapper);
		} catch (Exception e) {
			log.error("Exception occurred in sendPostRequestWithFile: {}", e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException("Error occurred while making POST call", e);
		}
		return httpResponseWrapper;
	}

	private void parseResponseFromClient(HttpResponseWrapper httpResponseWrapper, HttpClient httpClient,
			HttpPost httpPost) {
		try {
			HttpResponse response = httpClient.execute(httpPost);
			httpResponseWrapper.setResponse(EntityUtils.toString(response.getEntity()));
			httpResponseWrapper.setStatusCode(response.getStatusLine().getStatusCode());
			log.debug("Response Object :{}", response);
		} catch (IOException e) {
			log.error("IOException occurred in parseResponseFromClient: {}", e.getMessage(), e);
			throw new BusinessException("Error occurred while processing HTTP response", e);
		}
	}

	private org.apache.http.HttpEntity createMultipartEntity(byte[] bytes, MultipartFile file) {
		return MultipartEntityBuilder.create()
				.addBinaryBody("fileData", bytes, ContentType.APPLICATION_OCTET_STREAM, file.getOriginalFilename())
				.build();
	}

	public String sendOfferLetter( MultipartFile file, OfferLetterWrapper offerLetterWrapper) {
		try {
			JSONObject jsonObject = getJsonOfWrapper(offerLetterWrapper);
			NotificationTemplate template = notificationIntegration.getTemplte("offer_letter_1");
			byte[] pdfBytes = file.getBytes();
			String methodUrl = ConfigUtils.getPlainString("METHOD_URL") + file.getName();
			HttpResponseWrapper httpResponseWrapper = sendPostRequestWithFile(methodUrl, pdfBytes, file);
			String response = httpResponseWrapper.getResponse();
			log.debug("Response fetched is : {} ", response);
			ObjectMapper om = new ObjectMapper();
			JsonNode json = om.readTree(response);
			log.debug("json fetched is : {} ", json);
			Integer docId = json.get("id").asInt();
			log.debug("docId fetched is : {} ", docId);

			String attachmentFilePath = ConfigUtils.getPlainString(ATTACHMENT_FILE_PATH) + docId;

			List<NotificationAttachment> notificationAttachmentList = new ArrayList<>();
			NotificationAttachment notificationAttachment = new NotificationAttachment();
			notificationAttachment.setIsPublic(true);
			notificationAttachment.setPath(attachmentFilePath);
			notificationAttachment.setName(file.getName());
			notificationAttachmentList.add(notificationAttachment);
			notificationIntegration.sendEmail(template, jsonObject, offerLetterWrapper.getEmail(), null,
					notificationAttachmentList);

			return APIConstants.SUCCESS_JSON;

		} catch (Exception e) {
			log.error("Exception occurred in sendOfferLetter: {}", e.getMessage(), Utils.getStackTrace(e));
			throw new BusinessException();
		}
	}

}
