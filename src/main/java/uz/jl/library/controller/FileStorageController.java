package uz.jl.library.controller;


import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import uz.jl.library.dto.UploadsDTO;
import uz.jl.library.exception.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/uploads")
public class FileStorageController {
    static List<UploadsDTO> uploadsDTOList = new ArrayList<>();


    @GetMapping
    public String fileUploadPage() {
        return "file/upload";
    }

    @GetMapping("{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> viewFile(@PathVariable String filename) {
        Path uploadPath = Paths.get("/apps/library/uploads");
        Path path = uploadPath.resolve(filename);
        Resource resource = new FileSystemResource(path);
        UploadsDTO uploadsDTO = uploadsDTOList.stream().filter(dto -> dto.getGeneratedName().equals(filename)).findFirst().orElseThrow(() -> {
            throw new NotFoundException("Requesting file not found");
        });

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, uploadsDTO.getContentType(),
                        "attachment; filename=\"" + uploadsDTO.getOriginalName() + "\""
                )
                .body(resource);
    }

    @PostMapping
    public String fileUpload(@RequestParam(name = "file") MultipartFile multipartFile) throws IOException {

        String contentType = multipartFile.getContentType();
        String originalFilename = multipartFile.getOriginalFilename();
        byte[] content = multipartFile.getBytes();
        long size = multipartFile.getSize();
        String filename = StringUtils.getFilename(originalFilename);
        String filenameExtension = StringUtils.getFilenameExtension(originalFilename);
        String generatedName = System.currentTimeMillis() + "." + filenameExtension;
        String path = "/uploads/" + generatedName;
        UploadsDTO dto = UploadsDTO
                .builder()
                .id(1L)
                .contentType(contentType)
                .originalName(filename)
                .size(size)
                .generatedName(generatedName)
                .path(path)
                .build();
        Path uploadPath = Paths.get("/apps/library/uploads").resolve(generatedName);
        Files.copy(multipartFile.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
        uploadsDTOList.add(dto);
        return "redirect:/uploads/files";
    }

    @GetMapping("/files")
    public ModelAndView uploadedFilesPage() {
        ModelAndView modelAndView = new ModelAndView("file/file_list");
        modelAndView.addObject("files", uploadsDTOList);
        return modelAndView;
    }
}
