package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class BlobMetadata
{

	@Id
	private String id;

	private long size;

	private LocalDateTime createdAt;

	private String storageType;

	private String fileExtension;

	private boolean isActive = true;

	// Private constructor to enforce usage of Builder
	private BlobMetadata(Builder builder) {
		this.id = builder.id;
		this.size = builder.size;
		this.createdAt = builder.createdAt;
		this.storageType = builder.storageType;
		this.fileExtension = builder.fileExtension;
		this.isActive = builder.isActive;
	}

	public BlobMetadata() {
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

	public String getStorageType() {
		return storageType;
	}

	public boolean isActive() {
		return isActive;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	@Override
	public String toString() {
		return "BlobMetadata{" + "id='" + id + '\'' + ", size=" + size + ", createdAt=" + createdAt + ", storageType='" + storageType + '\'' + ", fileExtension='" + fileExtension + '\'' + ", isActive=" + isActive + '}';
	}

	// Builder class
	public static class Builder
	{
		private String id;
		private long size;
		private LocalDateTime createdAt = LocalDateTime.now();
		private String storageType;
		private String fileExtension;
		private boolean isActive = true; // Default value

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

		public Builder storageType(String storageType) {
			this.storageType = storageType;
			return this;
		}

		public Builder fileExtension(String fileExtension) {
			this.fileExtension = fileExtension;
			return this;
		}

		public Builder isActive(boolean isActive) {
			this.isActive = isActive;
			return this;
		}

		public BlobMetadata build() {
			return new BlobMetadata(this);
		}
	}
}



