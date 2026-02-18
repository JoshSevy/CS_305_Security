package com.snhu.sslserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

	/**
	 * Method to create SHA-256 checksum of input data
	 * @param data
	 * @return
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
	 * @param bytes
	 * @return
	 */
	private String bytesToHex(byte[] bytes) {
		StringBuilder hex = new StringBuilder();
		for (byte b : bytes) {
			hex.append(String.format("%02x", b));
		}
		return hex.toString();
	}

	@RequestMapping("/hash")
	@GetMapping("/hash")
	public String hash(
			@RequestParam String data,
			@RequestParam(required = false) String expected) {

		String checksum = createChecksum(data);

		StringBuilder response = new StringBuilder();

		response.append("data: ").append(data).append("\n\n");
		response.append("Message Digest SHA-256 : Checksum Value: ")
				.append(checksum)
				.append("\n\n");

		// Verification logic
		if (expected != null && !expected.isEmpty()) {
			if (checksum.equalsIgnoreCase(expected)) {
				response.append("Verification Result: PASS ✅");
			} else {
				response.append("Verification Result: FAIL ❌");
			}
		} else {
			response.append("Verification Result: No comparison checksum provided.");
		}

		return "<pre>" + response + "</pre>";
	}
}
