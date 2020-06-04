package sdg.demo.candidate;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
public class SDGCandidateController {
	private static final String RESPONSE_STRING_FORMAT = "candidate => %s\n";

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final RestTemplate restTemplate;
	private final static String[] headers_to_proagate = { "x-request-id", "x-b3-traceid", "x-b3-spanid", "x-b3-sampled",
			"x-b3-flags", "x-ot-span-context", "x-datadog-trace-id", "x-datadog-parent-id", "x-datadog-sampled",
			"end-user", "user-agent" };

	/*
	 * @Autowired private Tracer tracer;
	 */
	private String remoteURL = "http://titles:9080";

	public SDGCandidateController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@RequestMapping("/")
	public ResponseEntity<?> getTitles() {
		try {
			ResponseEntity<String> responseEntity = restTemplate.getForEntity(remoteURL, String.class);
			String response = "Hitting candidate service" + "=>Connected to titles service too:"
					+ responseEntity.getBody();
			return ResponseEntity.ok(String.format(RESPONSE_STRING_FORMAT, response.trim()));
		} catch (HttpStatusCodeException ex) {
			logger.warn("Exception trying to get the response from recommendation service.", ex);
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(String.format(RESPONSE_STRING_FORMAT,
					String.format("%d %s", ex.getRawStatusCode(), createHttpErrorResponseString(ex))));
		} catch (RestClientException ex) {
			logger.warn("Exception trying to get the response from recommendation service.", ex);
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
					.body(String.format(RESPONSE_STRING_FORMAT, ex.getMessage()));
		}
	}

	@RequestMapping(value = "/v1/api/sdg/demo/candidate/check/{title}", method = RequestMethod.GET)
	public ResponseEntity<String> getCandidate(@RequestHeader("User-Agent") String userAgent,
			@RequestHeader HttpHeaders requestHeaders,
			@RequestHeader(value = "user-preference", required = false) String userPreference,
			@PathVariable("title") String title) {
		try {
			/**
			 * Set baggage
			 */
			/*
			 * tracer.activeSpan().setBaggageItem("user-agent", userAgent); if
			 * (userPreference != null && !userPreference.isEmpty()) {
			 * tracer.activeSpan().setBaggageItem("user-preference", userPreference); }
			 */
			HttpHeaders headers = this.createRequestHeader(requestHeaders);
			HttpEntity<?> entity = new HttpEntity<>(null, headers);
			ResponseEntity<String> responseEntity = restTemplate
					.exchange(remoteURL + "/v1/api/sdg/demo/person/checktitle?name=" + title,HttpMethod.GET, entity,  String.class);
			String response = responseEntity.getBody();
			return ResponseEntity.ok(String.format(RESPONSE_STRING_FORMAT, response.trim()));
		} catch (HttpStatusCodeException ex) {
			logger.warn("Exception trying to get the response from preference service.", ex);
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(String.format(RESPONSE_STRING_FORMAT,
					String.format("%d %s", ex.getRawStatusCode(), createHttpErrorResponseString(ex))));
		} catch (RestClientException ex) {
			logger.warn("Exception trying to get the response from preference service.", ex);
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
					.body(String.format(RESPONSE_STRING_FORMAT, ex.getMessage()));
		}
	}

	@RequestMapping(value = "/v1/api/sdg/demo/candidate/titles", method = RequestMethod.GET)
	public ResponseEntity<String> getTitles(@RequestHeader("User-Agent") String userAgent,
			@RequestHeader HttpHeaders requestHeaders,
			@RequestHeader(value = "user-preference", required = false) String userPreference) {
		try {
			/**
			 * Set baggage
			 */
			/*
			 * tracer.activeSpan().setBaggageItem("user-agent", userAgent); if
			 * (userPreference != null && !userPreference.isEmpty()) {
			 * tracer.activeSpan().setBaggageItem("user-preference", userPreference); }
			 */
			HttpHeaders headers = this.createRequestHeader(requestHeaders);
			HttpEntity<?> entity = new HttpEntity<>(null, headers);
			ResponseEntity<String> responseEntity = restTemplate
					.exchange(remoteURL + "/v1/api/sdg/demo/person/alltitles", HttpMethod.GET, entity, String.class);
			String response = responseEntity.getBody();
			return ResponseEntity.ok(String.format(RESPONSE_STRING_FORMAT, response.trim()));
		} catch (HttpStatusCodeException ex) {
			logger.warn("Exception trying to get the response from preference service.", ex);
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(String.format(RESPONSE_STRING_FORMAT,
					String.format("%d %s", ex.getRawStatusCode(), createHttpErrorResponseString(ex))));
		} catch (RestClientException ex) {
			logger.warn("Exception trying to get the response from preference service.", ex);
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
					.body(String.format(RESPONSE_STRING_FORMAT, ex.getMessage()));
		}
	}

	private HttpHeaders createRequestHeader(HttpHeaders requestHeaders) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-TP-DeviceID", "your value");
		for (String header : headers_to_proagate) {
			String value = requestHeaders.getFirst(header);
			if (value != null) {
				headers.set(header, value);
			}
		}
		return headers;
	}

	private String createHttpErrorResponseString(HttpStatusCodeException ex) {
		String responseBody = ex.getResponseBodyAsString().trim();
		if (responseBody.startsWith("null")) {
			return ex.getStatusCode().getReasonPhrase();
		}
		return responseBody;
	}
}
