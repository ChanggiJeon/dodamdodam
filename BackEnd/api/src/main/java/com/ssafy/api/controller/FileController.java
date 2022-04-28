package com.ssafy.api.controller;

import com.ssafy.api.common.FileUtil;
import com.ssafy.api.service.FileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/file")
@Tag(name = "UploadController", description = "파일 업로드 테스트 컨트롤러")
public class FileController {
    private final FileService fileService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(
            @RequestParam("category") String category,
            @RequestPart(value = "file") MultipartFile multipartFile) {
        return fileService.uploadFileV1(category, multipartFile);
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadFile(
            @RequestParam("resourcePath") String resourcePath) {
        byte[] data = fileService.downloadFileV1(resourcePath);
        ByteArrayResource resource = new ByteArrayResource(data);
        HttpHeaders headers = buildHeaders(resourcePath, data);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(resource);
    }

    private HttpHeaders buildHeaders(String resourcePath, byte[] data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(data.length);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(FileUtil.createContentDisposition(resourcePath));
        return headers;
    }

//    @PostMapping("/upload/list")
//    public FileUploadResponse uploadFile(
//            @RequestParam("category") String category,
//            @RequestPart(value = "file") List<MultipartFile> multipartFiles,
//            Authentication authentication) {
//        long userId = Long.parseLong(authentication.getName());
//        return fileService.uploadFile(userId, category, multipartFiles);
//    }
}