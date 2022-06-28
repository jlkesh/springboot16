package uz.jl.library.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


@Controller
public class HomeController {
    //@RequestParam(required = false, defaultValue = "me") String name

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String homePage(@RequestParam Optional<String> name, Model model) {
        model.addAttribute("name", name.orElse("John"));
        return "index";
    }
}

