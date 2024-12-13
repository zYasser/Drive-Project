package com.example.demo.StorageStrategy;

import com.example.demo.entity.Blob;
import com.example.demo.utils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemStorage implements StorageStrategy
{

	private static final Logger logger = LogManager.getLogger(FileSystemStorage.class);

	@Value("${filesystem.path}")
	private String path;

	@Override
	public boolean saveBlob(Blob blob) {
		logger.info("Attempting to save blob with ID: {}", blob.getId());

		try {
			String fileExtension = FileUtils.getFileExtensionFromBase64Bytes(blob.getData());
			if (fileExtension == null) {
				fileExtension = ".txt";
			}

			Path p = Paths.get(path + "/" + blob.getId()  + "."+ fileExtension);

			File f = Files.write(p, blob.getData()).toFile();

			logger.info("Blob with ID: {} successfully saved to file {}", blob.getId(), f.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Failed to save blob with ID: {}. Error: {}", blob.getId(), e.getMessage());
			return false;
		}

		return true;
	}

	@Override
	public byte[] getBlob(String id) {
		logger.info("Attempting to retrieve blob with ID: {}", id);

		try {
			File f = new File(path);
			String file = "";
			String[] files = f.list();
			for (String s : files) {
				if (s.startsWith(id)) file = s;
			}


			if (file.equals("")) {
				logger.warn("Blob with ID: {} not found at path: {}", id, f.getAbsolutePath());
				return null;
			}
			f = new File(path + "/" + file);
			byte[] data = Files.readAllBytes(Path.of(f.getPath()));
			logger.info("Blob with ID: {} successfully retrieved from file {}", id, f.getAbsolutePath());
			return data;
		} catch (IOException e) {
			logger.error("Failed to retrieve blob with ID: {}. Error: {}", id, e.getMessage());
			return null;
		}
	}
}
