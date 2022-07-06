package uz.jl.library.mappers;


import org.mapstruct.Mapper;
import uz.jl.library.domains.Book;
import uz.jl.library.dto.BookCreateDTO;
import uz.jl.library.dto.BookUpdateDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book fromCreateDTO(BookCreateDTO dto);

    Book fromUpdateDTO(BookUpdateDTO dto);

    BookCreateDTO toDto(Book book);
    List<BookCreateDTO> toDto(List<Book> book);
}
