package uz.jl.library.services;

import lombok.NonNull;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import uz.jl.library.domains.Uploads;
import uz.jl.library.dto.UploadsDTO;

import java.util.List;


public interface FileStorageService {

    Uploads upload(MultipartFile multipartFile);

    ResponseEntity<Resource> download(@NonNull String fileName);

    UploadsDTO get(@NonNull String fileName);

    List<Uploads> getAll();

}
