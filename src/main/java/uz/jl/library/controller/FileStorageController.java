package uz.jl.library.controller;


import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import uz.jl.library.dto.UploadsDTO;

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
        Path upload = Paths.get("upload");
//        Files.copy(multipartFile.getInputStream(), , StandardCopyOption.REPLACE_EXISTING);
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
