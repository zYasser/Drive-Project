package com.example.demo.storage;

import com.example.demo.entity.Blob;
import com.example.demo.utils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class S3Storage implements StorageStrategy
{

	private static final Logger logger = LogManager.getLogger(SQLStorage.class);
	@Value("pre.signed.url")
	String preSignedUrl;

	@Override
	public boolean saveBlob(Blob blob) {

		try {
			// Create a URL object
			URL url = new URL(preSignedUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			String fileExtension = FileUtils.getFileExtensionFromBase64Bytes(blob.getData());
			String contentType = "";
			if (fileExtension != null && fileExtension.equals("png") || fileExtension.equals(
					"gif") || fileExtension.equals("jpg")) {
				contentType = "image/png";
			} else if (fileExtension != null) {
				contentType = "application/" + fileExtension;
			} else {
				contentType = "text/plain";
			}
			// Configure the connection
			connection.setDoOutput(true);
			connection.setRequestMethod(String.valueOf(HttpMethod.PUT));

			connection.setRequestProperty("Content-Type", contentType);
			connection.setRequestProperty("Content-Length", String.valueOf(blob.getData().length));
			return true;
		} catch (Exception e) {
			logger.error("Failed to upload BLob with id:{} to S3 Bucket: {}", blob.getData(), e.getMessage());
			return false;

		}
	}

	@Override
	public byte[] getBlob(String id) {
		try {
			// Create a URL object using the pre-signed URL and append the id if needed
			URL url = new URL(preSignedUrl + "?id=" + id); // Adjust if the id is part of the URL or parameters
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// Set the request method to GET
			connection.setRequestMethod(HttpMethod.GET.name());

			// Check the response code
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				// Read the input stream (response body)
				try (InputStream inputStream = connection.getInputStream()) {
					return inputStream.readAllBytes();  // Read the entire stream into a byte array
				}
			} else {
				logger.error("Failed to retrieve blob. HTTP error code: {}", responseCode);
				return null;
			}
		} catch (Exception e) {
			logger.error("Failed to download Blob with id:{} from S3 Bucket: {}", id, e.getMessage());
			return null;
		}
	}
}
