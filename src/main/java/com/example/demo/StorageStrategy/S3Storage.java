package com.example.demo.StorageStrategy;

import com.example.demo.entity.Blob;

public class S3Storage implements StorageBase
{



	private final String BUCKET_NAME = dotenv.get("BUCKET_NAME");
	private final String REGION = dotenv.get("REGION");
	private final String ACCESS_KEY = dotenv.get("ACCESS_KEY");
	private final String SECRET_KEY = dotenv.get("SECRET_KEY");


	@Override
	public boolean saveBlob(Blob blob) {
		return false;
	}

	@Override
	public byte[] getBlob(String id) {
		return new byte[0];
	}
}
