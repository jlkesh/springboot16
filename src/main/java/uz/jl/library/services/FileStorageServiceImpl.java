package uz.jl.library.services;

import lombok.NonNull;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
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
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
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
    public Uploads upload(MultipartFile multipartFile) {
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
            return uploads;
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

    @Override
    @Transactional
    public Uploads uploadCover(MultipartFile fileForCover) {
        try {
            String contentType = fileForCover.getContentType();
            String originalFilename = fileForCover.getOriginalFilename();
            long size = fileForCover.getSize();
            String filename = StringUtils.getFilename(originalFilename);
            String filenameExtension = StringUtils.getFilenameExtension(originalFilename);
            String generatedName = System.currentTimeMillis() + ".png";
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

            PDDocument document = PDDocument.load(fileForCover.getInputStream());
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
            ImageIOUtil.writeImage(bufferedImage, uploadPath.toString(), 300);
            return uploads;
        } catch (IOException e) {
            throw new RuntimeException("Something wrong try again");
        }
    }
}
