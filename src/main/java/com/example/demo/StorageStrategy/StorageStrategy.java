package com.example.demo.StorageStrategy;

import com.example.demo.entity.Blob;

public interface StorageStrategy
{
	boolean saveBlob(Blob blob);
	byte[] getBlob(String id);

}
