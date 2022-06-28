package uz.jl.library.controller;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.jl.library.domains.Book;
import uz.jl.library.exception.NotFoundException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private List<Book> books = new ArrayList<>();
    private final Faker faker;

    @PostConstruct
    public void init() {
        com.github.javafaker.Book book = faker.book();
        for (int i = 0; i < 55; i++) {
            books.add(Book.builder()
                    .title(book.title())
                    .author(book.author())
                    .genre(book.genre())
                    .publisher(book.publisher())
                    .build());
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getAll(@RequestParam String search , Model model) {
        model.addAttribute("books", books);
        return "book/book_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addPage() {
        return "book/book_add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@ModelAttribute Book book) {
        books.add(book);
        return "redirect:/book";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deletePage(@PathVariable UUID id, Model model) {
        Book searchingBook = books.stream()
                .filter(book -> book.getId().equals(id)
                ).findFirst().orElseThrow(() -> {
                    throw new NotFoundException("Book not found with given id '%s'".formatted(id));
                });
        model.addAttribute("book", searchingBook);
        return "book/book_delete";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable UUID id) {
        books.removeIf(book -> book.getId().equals(id));
        return "redirect:/book";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String updatePage(@PathVariable UUID id, Model model) {
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
                    book.setTitle(dto.getTitle());
                    book.setGenre(dto.getGenre());
                    book.setAuthor(dto.getAuthor());
                    book.setPublisher(dto.getPublisher());
                });
        return "redirect:/book";
    }

}

