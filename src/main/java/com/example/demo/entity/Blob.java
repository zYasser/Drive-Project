package com.example.demo.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Lob;

import java.time.LocalDateTime;
import java.util.Base64;

public class Blob
{
	@Id
	private String id;

	@Lob
	private byte[] data;

	private long size;

	private LocalDateTime createdAt;

	private String storageType;

	public byte[] decodeBase64Data(String base64Data) {
		return Base64.getDecoder().decode(base64Data);
	}



}
