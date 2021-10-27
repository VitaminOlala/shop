package com.shopme.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;


//Luu anh vào file project
public class FileUploadUtil {
	
	public static void saveFile(String uploadDir, String fileName,
				MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(uploadDir);    //lay ra 1 URI (uploadDir)
		
		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		try(InputStream inputStream = multipartFile.getInputStream()){
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		}catch (IOException e) {
			throw new IOException("Could not save file: " +fileName, e);
		}
	}
}
