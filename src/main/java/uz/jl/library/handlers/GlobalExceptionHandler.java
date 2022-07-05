package uz.jl.library.handlers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import uz.jl.library.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@EnableWebMvc
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
    public String handle500() {
        return "errors/500.html";
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public String handle400(MethodArgumentTypeMismatchException e,
                            Model model,
                            WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI().toString();
        model.addAttribute("message", e.getMessage());
        model.addAttribute("path", path);
        return "errors/400";
    }


    @ExceptionHandler({NoHandlerFoundException.class})
    public ModelAndView handle404(HttpServletRequest request,
                                  Exception e) {
        ModelAndView modelAndView = new ModelAndView("errors/404");
        modelAndView.addObject("message", e.getLocalizedMessage());
        modelAndView.addObject("path", request.getRequestURI());
        return modelAndView;
    }



}
