package uz.jl.library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.jl.library.domains.Book;
import uz.jl.library.dto.BookCreateDTO;
import uz.jl.library.dto.BookUpdateDTO;
import uz.jl.library.exception.NotFoundException;
import uz.jl.library.mappers.BookMapper;
import uz.jl.library.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper mapper;

    @Override
    public void create(BookCreateDTO dto) {
        Book book = mapper.fromCreateDTO(dto);
        System.out.println(book);
//        bookRepository.save(book);
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
    public List<Book> findAll(Optional<Integer> pageOptional, Optional<Integer> limitOptional) {
        int page = pageOptional.orElse(0);
        int size = limitOptional.orElse(10);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Book> booksPerPage = bookRepository.findAll(pageable);
        return booksPerPage.get().toList();
    }
}
