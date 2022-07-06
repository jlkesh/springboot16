package uz.jl.library.controller;


import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import uz.jl.library.services.FileStorageService;

@Controller
@RequestMapping("/uploads")
public class FileStorageController {

    private final FileStorageService fileStorageService;

    public FileStorageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }


    @GetMapping
    public String fileUploadPage() {
        return "file/upload";
    }

    @GetMapping("{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> viewFile(@PathVariable String filename) {
        return fileStorageService.download(filename);
    }

    @PostMapping
    public String fileUpload(@RequestParam(name = "file") MultipartFile multipartFile) {
        fileStorageService.upload(multipartFile);
        return "redirect:/uploads/files";
    }

    @GetMapping("/files")
    public ModelAndView uploadedFilesPage() {
        ModelAndView modelAndView = new ModelAndView("file/file_list");
        modelAndView.addObject("files", fileStorageService.getAll());
        return modelAndView;
    }
}
