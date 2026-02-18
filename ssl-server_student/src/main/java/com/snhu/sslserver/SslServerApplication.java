package com.snhu.sslserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class SslServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SslServerApplication.class, args);
	}
}

@RestController
class ServerController{
	private static final String HASH_ALGORITHM = "SHA-256";
	// Security constants for input validation
	private static final int MAX_INPUT_LENGTH = 10000; // Max 10KB
	private static final int MAX_HASH_LENGTH = 64; // SHA-256 hex string length

	/**
	 * Redirects root path to /hash endpoint
	 * @return redirect to /hash
	 */
	@GetMapping("/")
	public RedirectView redirectRoot() {
		return new RedirectView("/hash", true, false);
	}

	/**
	 * Catch-all route that redirects any undefined paths to /hash endpoint
	 * @return redirect to /hash
	 */
	@GetMapping("/**")
	public RedirectView redirectAllOtherRoutes() {
		return new RedirectView("/hash", true, false);
	}

	/**
	 * Validates and sanitizes input data to prevent malicious inputs
	 * @param data the input data to validate
	 * @param paramName the parameter name for error messages
	 * @throws ResponseStatusException if validation fails
	 */
	private void validateInput(String data, String paramName) {
		// Check for null
		if (data == null) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				paramName + " cannot be null"
			);
		}

		// Check for excessive length (DoS prevention)
		if (data.length() > MAX_INPUT_LENGTH) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				paramName + " exceeds maximum length of " + MAX_INPUT_LENGTH + " characters"
			);
		}
	}

	/**
	 * Validates expected hash format
	 * @param hash the hash string to validate
	 * @throws ResponseStatusException if validation fails
	 */
	private void validateHashFormat(String hash) {
		// Check length
		if (hash.length() != MAX_HASH_LENGTH) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				"Invalid hash format: expected " + MAX_HASH_LENGTH + " hex characters"
			);
		}

		// Check for valid hexadecimal characters only
		if (!hash.matches("^[a-fA-F0-9]+$")) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				"Invalid hash format: must contain only hexadecimal characters (0-9, a-f, A-F)"
			);
		}
	}

	/**
	 * HTML-encodes a string to prevent XSS attacks
	 * @param input the string to encode
	 * @return HTML-encoded string
	 */
	private String htmlEncode(String input) {
		if (input == null) {
			return "";
		}
		return input
			.replace("&", "&amp;")
			.replace("<", "&lt;")
			.replace(">", "&gt;")
			.replace("\"", "&quot;")
			.replace("'", "&#x27;")
			.replace("/", "&#x2F;");
	}

	/**
	 * Method to create SHA-256 checksum of input data
	 * @param data the input string to hash
	 * @return hexadecimal string representation of the SHA-256 hash
	 */
	private String createChecksum(String data) {
		try {
			MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);

			byte[] hashBytes =
					md.digest(data.getBytes(StandardCharsets.UTF_8));

			return bytesToHex(hashBytes);

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 algorithm not found", e);
		}
	}

	/**
	 * Helper method to convert byte array to hexadecimal string
	 * @param bytes the byte array to convert
	 * @return hexadecimal string representation
	 */
	private String bytesToHex(byte[] bytes) {
		StringBuilder hex = new StringBuilder();
		for (byte b : bytes) {
			hex.append(String.format("%02x", b));
		}
		return hex.toString();
	}

	@GetMapping("/hash")
	public String hash(
			@RequestParam(required = false) String data,
			@RequestParam(required = false) String expected) {

		// If no data parameter provided, show usage information
		if (data == null || data.trim().isEmpty()) {
			return displayUsageInformation();
		}

		// Validate input data
		validateInput(data, "data");

		// Validate expected hash if provided
		if (expected != null && !expected.trim().isEmpty()) {
			validateInput(expected, "expected");
			validateHashFormat(expected.trim());
		}

		String checksum = createChecksum(data);

		StringBuilder response = new StringBuilder();

		// Use HTML encoding to prevent XSS attacks
		response.append("data: ").append(htmlEncode(data)).append("\n\n");
		response.append("Message Digest SHA-256 : Checksum Value: ")
				.append(checksum) // Hash output is already safe (hex only)
				.append("\n\n");

		// Verification logic
		if (expected != null && !expected.trim().isEmpty()) {
			if (checksum.equalsIgnoreCase(expected.trim())) {
				response.append("Verification Result: PASS ✅");
			} else {
				response.append("Verification Result: FAIL ❌\n");
				response.append("Expected: ").append(expected.trim());
			}
		} else {
			response.append("Verification Result: No comparison checksum provided.");
		}

		return "<pre>" + response + "</pre>";
	}

	/**
	 * Displays simple usage information for the /hash endpoint
	 * @return HTML formatted usage guide
	 */
	private String displayUsageInformation() {
		return "<html><head><style>" +
			"body { font-family: system-ui; margin: 0; padding: 40px 20px; background: #f5f5f5; }" +
			".card { max-width: 500px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }" +
			"h1 { margin-top: 0; color: #333; }" +
			"code { background: #eee; padding: 8px 12px; border-radius: 4px; display: block; margin: 10px 0; font-family: monospace; font-size: 13px; overflow-x: auto; }" +
			"p { color: #666; line-height: 1.6; margin: 10px 0; }" +
			"</style></head><body>" +
			"<div class='card'>" +
			"<h1>SHA-256 Hash Generator</h1>" +
			"<p><strong>Usage:</strong></p>" +
			"<code>/hash?data=HelloWorld</code>" +
			"<code>/hash?data=HelloWorld&expected=872e4e50ce9990d8b041330c47c9ddd11bec6b503ae9386a99da8584e9bb12c4</code>" +
			"<p><strong>Limits:</strong> Max 10,000 characters input</p>" +
			"</div>" +
			"</body></html>";
	}
}
