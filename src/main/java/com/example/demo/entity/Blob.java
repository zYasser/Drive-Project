package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

import java.time.LocalDateTime;
import java.util.Base64;
@Entity
public class Blob
{
	@Id
	private String id;
	@Lob
	private byte[] data;


	public Blob(String id, byte[] data) {
		this.id = id;
		this.data = data;
	}

	public Blob() {
	}

	public byte[] decodeBase64Data(String base64Data) {
		return Base64.getDecoder().decode(base64Data);
	}


	public byte[] getData() {
		return data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
