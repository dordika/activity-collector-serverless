package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.dal.Activity;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class StoreActivityHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		logger.info("received: { " + input.toString() + " }");

		try {
			JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
			Activity activity = new Activity();
			activity.setAppVersion(body.get("appVersion").asText());
			activity.setInstance(body.get("instance").asText());
			activity.setActivity(body.get("activity").asText());
			activity.setSessionIdentifier(body.get("sessionIdentifier").asText());
			activity.setDateCreated(new Date());

			activity.setActivityValue(body.get("activityValue").textValue());

			activity.save();

			// Successful response
			return ApiGatewayResponse.builder()
					.setStatusCode(200)
					.setObjectBody(activity)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
					.build();

		} catch (Exception ex) {
			logger.error("Error in saving activity ", ex);

			// send the error response back
			Response responseBody = new Response("Error { " + ex.getMessage() +  " }in saving person: ", input);
			return ApiGatewayResponse.builder()
					.setStatusCode(500)
					.setObjectBody(responseBody)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
					.build();
		}
	}
}
