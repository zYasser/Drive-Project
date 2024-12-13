package com.example.demo.service;

import com.example.demo.storage.StorageStrategy;
import com.example.demo.dto.BlobData;
import com.example.demo.dto.BlobRequest;
import com.example.demo.dto.BlobResponse;
import com.example.demo.entity.Blob;
import com.example.demo.entity.BlobMetadata;
import com.example.demo.repository.BlobMetadataRepository;
import com.example.demo.utils.FileUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

/**
 * Service class for handling Blob operations, including saving and retrieving blob data.
 */
@Service
public class BlobService {

	private static final Logger logger = LoggerFactory.getLogger(BlobService.class);

	private final BlobMetadataRepository blobMetadataRepository;
	private final StorageStrategy storageManager;

	/**
	 * Storage type (e.g., file system, cloud storage) specified in the application properties.
	 */
	@Value("${storage.type}")
	private String storageType;

	/**
	 * Constructs a new {@code BlobService} with the required dependencies.
	 *
	 * @param blobMetadataRepository the repository for managing blob metadata
	 * @param storageStrategy the strategy for saving and retrieving blobs
	 */
	public BlobService(BlobMetadataRepository blobMetadataRepository, StorageStrategy storageStrategy) {
		this.blobMetadataRepository = blobMetadataRepository;
		this.storageManager = storageStrategy;
	}

	/**
	 * Saves a blob and its metadata to the database and storage.
	 *
	 * @param request the blob request containing ID and Base64-encoded data
	 * @return a {@code BlobResponse} indicating the outcome of the save operation
	 */
	@Transactional(rollbackOn = Exception.class)
	public BlobResponse saveBlob(BlobRequest request) {
		logger.info("Received request to save blob with id: {}", request.id());

		// Validate the request
		if (request.id() == null || request.id().isEmpty()) {
			logger.warn("Blob Id cannot be null or empty");
			return new BlobResponse("Blob Id cannot be null", null, 400);
		}
		if (request.data() == null) {
			logger.warn("Blob data is null for blob id: {}", request.id());
			return new BlobResponse("Please enter data", null, 400);
		}

		try {
			// Decode Base64 data and extract metadata
			byte[] decodedData = Base64.getDecoder().decode(request.data());
			int size = decodedData.length;
			String fileExtension = FileUtils.getFileExtensionFromBase64Bytes(decodedData);

			Blob blob = new Blob(request.id(), decodedData);
			BlobMetadata blobMetadata = new BlobMetadata.Builder()
					.id(request.id())
					.size(size)
					.storageType(storageType)
					.fileExtension(fileExtension)
					.build();

			// Check if the blob already exists
			if (blobMetadataRepository.findById(blob.getId()).isPresent()) {
				logger.warn("Blob with id: {} already exists", blob.getId());
				return new BlobResponse("There's already a blob associated with this id: " + blob.getId(), null, 400);
			}

			// Save metadata and blob data
			blobMetadataRepository.save(blobMetadata);
			boolean result = storageManager.saveBlob(blob);
			if (!result) {
				throw new RuntimeException("Failed to save blob to storage");
			}

			logger.info("Blob with id: {} successfully created", request.id());
			return new BlobResponse("Successfully created a new blob", null, 201);

		} catch (IllegalArgumentException e) {
			logger.error("Invalid Base64 data for blob id: {}", request.id(), e);
			return new BlobResponse("Data is not valid Base64 format", null, 400);
		} catch (RuntimeException e) {
			logger.error("Failed to save blob with id: {} to database. Error: {}", request.id(), e.getMessage());
			return new BlobResponse("Something went wrong. Please try again later", null, 500);
		} catch (Exception e) {
			logger.error("Error occurred while saving blob with id: {}", request.id(), e);
			return new BlobResponse("Something went wrong. Please try again later", null, 500);
		}
	}

	/**
	 * Retrieves a blob by its ID.
	 *
	 * @param id the ID of the blob to retrieve
	 * @return a {@code BlobResponse} containing the blob data or an error message
	 */
	public BlobResponse getBlobById(String id) {
		logger.info("Received request to retrieve blob with id: {}", id);

		Optional<BlobMetadata> blobMetadata = blobMetadataRepository.findById(id);
		if (blobMetadata.isEmpty() || !blobMetadata.get().isActive()) {
			logger.warn("Blob with id: {} not found or is inactive", id);
			return new BlobResponse("There's no blob with this data", null, 400);
		}

		BlobMetadata metadata = blobMetadata.get();

		// Verify storage type
		if (!metadata.getStorageType().equals(storageType)) {
			logger.error("Storage type mismatch for blob id: {}. Expected: {}, Found: {}", id, storageType, metadata.getStorageType());
			return new BlobResponse("Something went wrong. Please try again later", null, 500);
		}

		byte[] bytes = storageManager.getBlob(id);
		if (bytes == null || bytes.length == 0) {
			logger.warn("Blob with id: {} has no data", id);
			return new BlobResponse("There's no blob with this data", null, 400);
		}

		BlobData blobData = new BlobData.Builder()
				.data(bytes)
				.createdAt(metadata.getCreatedAt())
				.id(id)
				.size(metadata.getSize())
				.build();

		logger.info("Successfully retrieved blob with id: {}", id);
		return new BlobResponse("", blobData, 200);
	}
}
