package com.example.demo.StorageStrategy;

import com.example.demo.entity.Blob;
import com.example.demo.repository.BlobRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class SQLStorage implements StorageStrategy {

	private static final Logger logger = LogManager.getLogger(SQLStorage.class);
	@Autowired
	private BlobRepository blobRepository;



	@Override
	public boolean saveBlob(Blob blob) {
		logger.info("Attempting to save blob with ID: {}", blob.getId());

		try {
			blobRepository.save(blob);
			logger.info("Blob with ID: {} successfully saved to database.", blob.getId());
			return true;
		} catch (Exception e) {
			logger.error("Failed to save blob with ID: {}. Error: {}", blob.getId(), e.getMessage());
			return false;
		}
	}

	@Override
	public byte[] getBlob(String id) {
		logger.info("Attempting to retrieve blob with ID: {}", id);

		try {
			Optional<Blob> optionalBlob = blobRepository.findById(id);
			if (optionalBlob.isEmpty()) {
				logger.warn("Blob with ID: {} not found in the database.", id);
				return null;
			}

			byte[] data = optionalBlob.get().getData();
			logger.info("Blob with ID: {} successfully retrieved from database.", id);
			return data;
		} catch (Exception e) {
			logger.error("Failed to retrieve blob with ID: {}. Error: {}", id, e.getMessage());
			return null;
		}
	}
}
