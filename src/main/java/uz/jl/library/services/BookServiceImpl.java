package uz.jl.library.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.jl.library.domains.Book;
import uz.jl.library.domains.Uploads;
import uz.jl.library.dto.BookCreateDTO;
import uz.jl.library.dto.BookUpdateDTO;
import uz.jl.library.exception.NotFoundException;
import uz.jl.library.mappers.BookMapper;
import uz.jl.library.repository.BookRepository;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final FileStorageService fileStorageService;

    private final BookMapper mapper;

    @Override
    public void create(BookCreateDTO dto, MultipartFile fileForCover, MultipartFile fileForBook) {
        CompletableFuture.runAsync(() -> {
            Uploads cover = fileStorageService.uploadCover(fileForBook);
            Uploads file = fileStorageService.upload(fileForBook);
            Book book = mapper.fromCreateDTO(dto);
            book.setCover(cover);
            book.setFile(file);
            bookRepository.save(book);
        });

    }

    @Override
    public Book get(long id) {
        return bookRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Book not found with given id '%s'".formatted(id));
        });
    }

    @Override
    public void delete(long id) {
        Book book = this.get(id);
        bookRepository.delete(book);
    }

    @Override
    public void update(BookUpdateDTO dto) {
        Book book = mapper.fromUpdateDTO(dto);
        bookRepository.save(book);
    }

    @Override
    public Page<Book> findAll(@NonNull String searchQuery, Optional<Integer> pageOptional, Optional<Integer> limitOptional) {
        int page = pageOptional.orElse(0);
        int size = limitOptional.orElse(10);
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return bookRepository.findAll(searchQuery.toLowerCase(), pageable);
    }
}
