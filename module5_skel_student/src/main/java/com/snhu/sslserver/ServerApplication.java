package com.snhu.sslserver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}

@RestController
class ServerController{
    /**
     * Method to create SHA-256 checksum of input data
     * @param data
     * @return
     */
    private String createChecksum(String data) {
        try {
           // Create a MessageDigest object with SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Generate the hash value using the input data
            byte[] hashBytes = digest.digest(data.getBytes());

            // Convert to hex using byteToHex method
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
    public String myHash(){
    	String data = "Joshua Sevy";
        String checksum = createChecksum(data);
       
        return "<p>data: " + data + "</p><p>checksum: " + checksum + "</p>";
    }
}
