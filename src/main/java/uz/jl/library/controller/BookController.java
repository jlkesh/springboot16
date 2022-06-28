package uz.jl.library.controller;

import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.jl.library.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/book")
public class BookController {

    private final List<Book> books = new ArrayList<>();

    private final AtomicLong counter = new AtomicLong(1);

    {
        books.add(new Book(counter.getAndIncrement(), "Daftar xoshiyasidagi bitiklar", 300, "O'tkir Hoshimov"));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getAll(Model model) {
        model.addAttribute("books", books);
        return "book/book_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addPage() {
        return "book/book_add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@ModelAttribute Book book) {
        book.setId(counter.getAndIncrement());
        books.add(book);
        return "redirect:/book";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deletePage(@PathVariable Long id, Model model) {
        Book searchingBook = books.stream()
                .filter(book -> book.getId().equals(id)
                ).findFirst().orElseThrow(() -> {
                    throw new NotFoundException("Book not found with given id '%s'".formatted(id));
                });
        model.addAttribute("book", searchingBook);
        return "book/book_delete";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable Long id) {
        books.removeIf(book -> book.getId().equals(id));
        return "redirect:/book";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String updatePage(@PathVariable Long id, Model model) {
        Book searchingBook = books.stream()
                .filter(book ->
                        book.getId().equals(id)
                ).findFirst().orElseThrow(() -> {
                    throw new NotFoundException("Book not found with given id '%s'".formatted(id));
                });
        model.addAttribute("book", searchingBook);
        return "book/book_update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@ModelAttribute Book dto) {
        books.stream()
                .filter(book -> book.getId().equals(dto.getId()))
                .forEach(book -> {
                    book.setName(dto.getName());
                    book.setAuthor(dto.getAuthor());
                    book.setPageCount(dto.getPageCount());
                });
        return "redirect:/book";
    }

}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Book {
    private Long id;
    private String name;
    private Integer pageCount;
    private String author;
}
