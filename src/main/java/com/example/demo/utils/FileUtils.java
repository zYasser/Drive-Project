package com.example.demo.utils;

import java.util.HashMap;
import java.util.Map;

public class FileUtils
{


	// Map of magic numbers to file extensions
	private static final Map<String, String> MAGIC_NUMBERS = new HashMap<>();

	static {
		MAGIC_NUMBERS.put("FFD8FF", "jpg"); // JPEG
		MAGIC_NUMBERS.put("89504E47", "png"); // PNG
		MAGIC_NUMBERS.put("47494638", "gif"); // GIF
		MAGIC_NUMBERS.put("25504446", "pdf"); // PDF
		MAGIC_NUMBERS.put("504B0304", "zip"); // ZIP
	}


	public static String getFileExtensionFromBase64Bytes(byte[] bytes) {

		try {
			String magicNumber = getMagicNumber(bytes);

			return MAGIC_NUMBERS.getOrDefault(magicNumber, null);
		} catch (IllegalArgumentException e) {
			System.err.println("Invalid Base64 string: " + e.getMessage());
			return null;
		}
	}


	private static String getMagicNumber(byte[] bytes) {
		StringBuilder magicNumber = new StringBuilder();

		// Read the first 4 bytes (or fewer if the array is smaller)
		for (int i = 0; i < Math.min(4, bytes.length); i++) {
			// Convert each byte to a hexadecimal string
			magicNumber.append(String.format("%02X", bytes[i]));
		}

		return magicNumber.toString();
	}


}
