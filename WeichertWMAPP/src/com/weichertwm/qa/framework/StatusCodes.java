package com.weichertwm.qa.framework;

/**
 * Class defines the Status Codes that could be used for API request's code
 * validation
 * 
 * Reference: https://en.wikipedia.org/wiki/List_of_HTTP_status_codes
 * 
 * @author arujadha
 *
 */
public enum StatusCodes {
	// 1XX (Informational)-The request was received, continuing process
	CONTINUE(100), SWITCHING_PROTOCOLS(101), PROCESSING(102), EARLY_HINTS(103),

	// 2XX (Successful)-The request was successfully received, understood & accepted
	OK(200), CREATED(201), ACCEPTED(202), NON_AUTNO_HORITATIVE_INFO(203), NO_CONTENT(204), RESET_CONTENT(205),

	// 3XX (Redirection)-Further action needed in order to complete the request
	MULTIPLE_CHOICES(300), MOVED_PERMANENTLY(301), FOUND(302),

	// 4XX (Client Error)-The request contains bad syntax or cannot be fulfilled
	BAD_REQUEST(400), UNAUTORIZED(401), PAYMENT_REQUIRED(402), FORBIDDEN(403), NOT_FOUND(404),

	// 5XX (Server Error)-The server failed to fulfill an apparently valid request
	INTERNAL_SERVER_ERROR(500), NOT_IMPLEMENTED(501), BAD_GATEWAY(502), SERVICE_UNAVAILABLE(503), GATEWWAY_TIMEOUT(504);

	private int statusCode;

	/**
	 * Parameterized Constructor for member initialization
	 * 
	 * @param statusCode
	 *            Status code to be set
	 */
	private StatusCodes(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * Used to retrieve the Status code
	 * 
	 * @return Returns the Status code number
	 */
	public int getStatusCode() {
		return statusCode;
	}
}
