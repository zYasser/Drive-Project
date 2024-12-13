package com.example.demo.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class StorageBean
{


	@Value("${storage.type}")
	private String storageType;
	@Bean
	public StorageStrategy storageManager(){
		System.out.println("storageType = " + storageType);
		switch (storageType.toLowerCase()){
			case "database":
				return new SQLStorage();
			case "filesystem":
				return new FileSystemStorage();
			case "s3"
				return new S3Storage();
			default:
				String message="";
				if(storageType==null){
					message=String.format("Isn't %s valid option for storage, Only S3, FileSystem, Database acceptable as storage" , storageType);
				}else {
					message="Please choose whether you want filesystem,database,s3";

				}
				throw new IllegalStateException(message);

		}

}
}
