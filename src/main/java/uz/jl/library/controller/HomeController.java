package uz.jl.library.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class HomeController {

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String homePage(@PathVariable String id, Model model) {
        model.addAttribute("id",id);
        return "index";
    }
}

