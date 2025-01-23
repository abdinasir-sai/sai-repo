package com.nouros.payrollmanagement.utils;

import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.enttribe.commons.http.HttpException;
import com.enttribe.commons.http.HttpPostRequest;
import com.enttribe.commons.http.HttpRequest;
import com.enttribe.commons.http.IllegalHttpStatusException;
import com.enttribe.commons.unit.Duration;
import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.utils.Utils;
import com.nouros.hrms.wrapper.HttpResponseWrapper;

public class HttpUtil {

	private static final Logger logger = LogManager.getLogger(HttpUtil.class);
	
	public static final String INSIDE_METHOD = "Inside @method {} ";
    public static final String SEND_POST_REQUEST = "sendPostRequest";
    public static final String INSIDE_METHOD_TWO_PARAMETER = "Inside @method {} {}";
    public static final String EXCEPTION_OCCURED = "Error occured @method {} ";
    public static final String INSIDE_METHOD_ONE_PARAMETER = "Inside @method {}";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public static final String HTTP_RESPONSE_WRAPPER = "httpResponseWrapper";
    public static final String EXCEPTION_LABEL_TAG = "exceptionLabelTag";
	
	public static HttpResponseWrapper sendPostRequest(String url, String json, Map<String, String> headers) {
		HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
		try {
			logger.info(INSIDE_METHOD, SEND_POST_REQUEST);
			logger.debug(INSIDE_METHOD_TWO_PARAMETER,SEND_POST_REQUEST,
"postUrl:{}", url, "json:{}", json);
			if (Objects.isNull(json)) {
				json = "";
			}
			HttpRequest httpPost = new HttpPostRequest(url, json)
					.setConnectionTimeout(Duration.seconds(120))
					.ignoreCertificateErrors();
			
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				if(Objects.isNull(entry.getKey()) || Objects.isNull(entry.getValue())) {
					continue;
				}
				httpPost.addHeader(entry.getKey(), entry.getValue());
			}
			httpResponseWrapper.setResponse(httpPost.getString());
			httpResponseWrapper.setStatusCode(httpPost.getStatusCode());
			logger.info(INSIDE_METHOD, SEND_POST_REQUEST);
			logger.debug(INSIDE_METHOD_ONE_PARAMETER,SEND_POST_REQUEST,
HTTP_RESPONSE_WRAPPER+"{}", httpResponseWrapper);
		}catch(IllegalHttpStatusException httpEx) {
			httpResponseWrapper.setStatusCode(httpEx.getStatusCode());
			httpResponseWrapper.setResponse(httpEx.getResponseMessage());
			logger.error(EXCEPTION_OCCURED, SEND_POST_REQUEST, HTTP_RESPONSE_WRAPPER+"{}", httpResponseWrapper);
			return httpResponseWrapper;
		}
		catch(HttpException  httpEx) {
			httpResponseWrapper.setResponse(httpEx.getMessage());
			logger.error(EXCEPTION_OCCURED, SEND_POST_REQUEST, HTTP_RESPONSE_WRAPPER+"{}", httpResponseWrapper);
			return httpResponseWrapper;
		} catch (Exception ex) {
			logger.error(EXCEPTION_OCCURED, SEND_POST_REQUEST, EXCEPTION_LABEL_TAG+"{}", Utils.getStackTrace(ex), ex);
			throw new BusinessException(SOMETHING_WENT_WRONG);
		}
		return httpResponseWrapper;
	}

}
