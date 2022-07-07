package uz.jl.library.services;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import uz.jl.library.domains.Book;
import uz.jl.library.dto.BookCreateDTO;
import uz.jl.library.dto.BookUpdateDTO;

import java.util.Optional;

public interface BookService {
    void create(BookCreateDTO dto, MultipartFile cover, MultipartFile file);

    Book get(long id);

    void delete(long l);

    void update(BookUpdateDTO dto);

    Page<Book> findAll(String searchQuery, Optional<Integer> page, Optional<Integer> limit);
}
