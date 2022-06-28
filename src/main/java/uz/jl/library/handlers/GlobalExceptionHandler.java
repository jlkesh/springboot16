package uz.jl.library.handlers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import uz.jl.library.exception.NotFoundException;

@ControllerAdvice("uz.jl.library")
public class GlobalExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    public String notFoundHandler(NotFoundException e,
                                  Model model,
                                  WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI().toString();
        model.addAttribute("message", e.getMessage());
        model.addAttribute("path", path);
        return "errors/404.html";
    }

    @ExceptionHandler({HttpServerErrorException.InternalServerError.class})
    public String _500Handler() {
        return "errors/500.html";
    }
}