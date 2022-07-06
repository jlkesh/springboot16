package uz.jl.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import uz.jl.library.domains.Uploads;

import java.util.Optional;

public interface FileStorageRepository extends JpaRepository<Uploads, Long> {

    Optional<Uploads> findByGeneratedName(String generatedName);

}
