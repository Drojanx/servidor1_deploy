package com.actividadaprendizaje.bookshelter.controller;


import com.actividadaprendizaje.bookshelter.domain.Book;
import com.actividadaprendizaje.bookshelter.domain.Purchase;
import com.actividadaprendizaje.bookshelter.domain.Review;
import com.actividadaprendizaje.bookshelter.domain.User;
import com.actividadaprendizaje.bookshelter.exception.BookNotFoundException;
import com.actividadaprendizaje.bookshelter.service.BookService;
import com.actividadaprendizaje.bookshelter.service.PurchaseService;
import com.actividadaprendizaje.bookshelter.service.ReviewService;
import com.actividadaprendizaje.bookshelter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class WebController {

    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping(value = "/")
    public String index(Model model) {
        List<Book> allBooks = bookService.findAllBooks();
        model.addAttribute("books", allBooks);
        model.addAttribute("categoryList", false);
        return "index";
    }

    @GetMapping(value = "/catalog")
    public String catalog(Model model) {
        List<Book> allBooks = bookService.findAllBooks();
        model.addAttribute("books", allBooks);
        model.addAttribute("categoryList", false);
        return "catalog";
    }

    @GetMapping(value = "/catalog/{categoryName}")
    public String productsByCategory(Model model, @PathVariable String categoryName) {
        List<Book> categoryBooks = bookService.findByCategory(categoryName);
        model.addAttribute("books", categoryBooks);
        model.addAttribute("categoryList", true);
        model.addAttribute("category", categoryName);
        return "catalog";
    }

    @GetMapping(value = "/book/{id}")
    public String product(Model model, HttpServletRequest request, @PathVariable long id, @RequestParam(value="justbought", required = false, defaultValue = "false") String justbought) throws BookNotFoundException {
        String remoteUsername = request.getRemoteUser();
        User remoteUser = userService.findByUsername(remoteUsername);
        Book book = bookService.findBook(id);
        boolean purchased = purchaseService.findPurchaseByUserAndBook(remoteUser, book);
        List<Review> reviews = reviewService.findByBook(book);
        model.addAttribute("book", book);
        model.addAttribute("reviews", reviews);
        model.addAttribute("purchased", purchased);
        return "book";
    }

    @GetMapping(value = "/book/{id}/checkout")
    public String productCheckout(Model model, @PathVariable long id) throws BookNotFoundException {
        Book book = bookService.findBook(id);
        model.addAttribute("book", book);
        return "checkout";
    }

}
