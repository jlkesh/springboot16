package uz.jl.library.services;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import uz.jl.library.domains.Uploads;
import uz.jl.library.repository.FileStorageRepository;
import uz.jl.library.dto.UploadsDTO;
import uz.jl.library.exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path rootPath;
    private final FileStorageRepository repository;

    public FileStorageServiceImpl(@Value("${file.upload.path}") String uploadPath, FileStorageRepository dao) {
        this.rootPath = Paths.get(uploadPath);
        this.repository = dao;
    }

    @PostConstruct
    public void init() throws IOException {
        if (!Files.exists(rootPath)) {
            Files.createDirectories(rootPath);
        }
    }


    @Override
    @Transactional
    public void upload(MultipartFile multipartFile) {
        try {
            String contentType = multipartFile.getContentType();
            String originalFilename = multipartFile.getOriginalFilename();
            long size = multipartFile.getSize();
            String filename = StringUtils.getFilename(originalFilename);
            String filenameExtension = StringUtils.getFilenameExtension(originalFilename);
            String generatedName = System.currentTimeMillis() + "." + filenameExtension;
            String path = "/uploads/" + generatedName;
            Uploads uploads = Uploads
                    .builder()
                    .contentType(contentType)
                    .originalName(filename)
                    .size(size)
                    .generatedName(generatedName)
                    .path(path)
                    .build();
            Path uploadPath = rootPath.resolve(generatedName);
            repository.save(uploads);
            Files.copy(multipartFile.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Something wrong try again");
        }
    }

    @Override
    public ResponseEntity<Resource> download(@NonNull String filename) {
        Path path = rootPath.resolve(filename);
        Resource resource = new FileSystemResource(path);
        Uploads uploads = repository.findByGeneratedName(filename).orElseThrow(() -> {
            throw new NotFoundException("File not found");
        });


        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(uploads.getContentType()))
                .contentLength(uploads.getSize())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + uploads.getOriginalName() + "\""
                )
                .body(resource);
    }

    @Override
    public UploadsDTO get(@NonNull String fileName) {
        return null;
    }

    @Override
    public List<Uploads> getAll() {
        return repository.findAll();
    }
}
