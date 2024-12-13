package com.example.demo.dto;


import java.time.LocalDateTime;

public class BlobData {

	private String id;
	private long size;
	private LocalDateTime createdAt;
	private byte[] data;

	// Private constructor to enforce the use of the builder
	private BlobData(Builder builder) {
		this.id = builder.id;
		this.size = builder.size;
		this.createdAt = builder.createdAt;
		this.data = builder.data;
	}

	// Getters
	public String getId() {
		return id;
	}

	public long getSize() {
		return size;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public byte[] getData() {
		return data;
	}

	// Builder class
	public static class Builder {
		private String id;
		private long size;
		private LocalDateTime createdAt;
		private byte[] data;

		public Builder() {
			// Default values (optional)
			this.createdAt = LocalDateTime.now();
		}

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder size(long size) {
			this.size = size;
			return this;
		}

		public Builder createdAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public Builder data(byte[] data) {
			this.data = data;
			return this;
		}

		public BlobData build() {
			return new BlobData(this);
		}
	}
}
