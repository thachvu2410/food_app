package com.cybersoft.food_project.service;

import com.cybersoft.food_project.model.FileStorageProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploadServiceImp implements FileUploadService{

    private final Path rootPath;

    public FileUploadServiceImp(FileStorageProperties fileStorageProperties) throws IOException {
        // Định nghĩa đường dẫn root
//        this.rootPath = Paths.get(D:\home\text).toAbsolutePath().normalize();
        this.rootPath = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        if (Files.notExists(this.rootPath)){
            // tạo folder để lưu file nếu đường dẫn chưa tồn tại
            Files.createDirectories(rootPath);
        }
    }


    @Override
    public boolean storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Files.copy(file.getInputStream(), rootPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            return true;
        }catch (Exception e){
            System.out.println("loi tai storeFile: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Resource loadFileByName(String fileName) {
        try {
            Path path = this.rootPath.resolve(fileName).normalize();
            // byte[]
            // base64
            // resource
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists()){
                return resource;
            }else {
                return null;
            }
        }catch (Exception e){
            System.out.println("loi loadFile: " + e.getMessage());
            return null;
        }

    }
}
