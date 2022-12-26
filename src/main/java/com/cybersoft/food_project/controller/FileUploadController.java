package com.cybersoft.food_project.controller;

import com.cybersoft.food_project.service.FileUploadServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    FileUploadServiceImp fileUploadService;
//    FileUploadService fileUploadService; không khởi tạo theo interface vì dùng constructor trong class FileUploadServiceImp
    // => nếu khởi tạo theo interface sẽ thiếu hàm constructor => bị lỗi
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file){
        System.out.println("kiem tra" + file.getOriginalFilename());
        fileUploadService.storeFile(file);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName,
                                                HttpServletRequest request) throws IOException {
        Resource resource = fileUploadService.loadFileByName(fileName);
        String contentType = "";
        if (resource != null){
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }

        if (contentType == null || contentType.equals("")){
            contentType= "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
