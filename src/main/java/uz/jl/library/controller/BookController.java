package uz.jl.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.jl.library.domains.Book;
import uz.jl.library.dto.BookCreateDTO;
import uz.jl.library.dto.BookUpdateDTO;
import uz.jl.library.enums.Language;
import uz.jl.library.services.BookService;

import java.util.Optional;
import java.util.OptionalInt;

@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getAll(
            @RequestParam Optional<String> search,
            @RequestParam(name = "page") Optional<Integer> page,
            @RequestParam(name = "limit") Optional<Integer> limit,
            Model model) {
        model.addAttribute("books", bookService.findAll(page, limit));
        model.addAttribute("genres", Book.Genre.values());
        model.addAttribute("languages", Language.values());
        return "book/book_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@ModelAttribute BookCreateDTO dto) {
        bookService.create(dto);
        return "redirect:/book";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deletePage(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.get(id));
        return "book/book_delete";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable Long id) {
        bookService.delete(id);
        return "redirect:/book";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String updatePage(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.get(id));
        return "book/book_update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@ModelAttribute BookUpdateDTO dto) {
        bookService.update(dto);
        return "redirect:/book";
    }

}

