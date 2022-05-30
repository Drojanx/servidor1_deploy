package com.actividadaprendizaje.bookshelter.controller;

import com.actividadaprendizaje.bookshelter.domain.Book;
import com.actividadaprendizaje.bookshelter.domain.Review;
import com.actividadaprendizaje.bookshelter.domain.User;
import com.actividadaprendizaje.bookshelter.exception.BookNotFoundException;
import com.actividadaprendizaje.bookshelter.exception.BookPurchaseException;
import com.actividadaprendizaje.bookshelter.exception.ReviewSaveException;
import com.actividadaprendizaje.bookshelter.service.BookService;
import com.actividadaprendizaje.bookshelter.service.ReviewService;
import com.actividadaprendizaje.bookshelter.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller

public class ReviewController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping(value = "/rate-book/{id}")
    public String rateBook(Model model, HttpServletRequest request, @PathVariable long id) throws BookNotFoundException {
        Book book = bookService.findBook(id);
        String remoteUsername = request.getRemoteUser();
        User remoteUser = userService.findByUsername(remoteUsername);
        Review review = new Review();
        review.setComment("");
        review.setBook(book);
        review.setUser(remoteUser);
        if(reviewService.findByUserAndBook(remoteUser, book) != null)
            review = reviewService.findByUserAndBook(remoteUser, book);
        model.addAttribute("review", review);
        model.addAttribute("book", book);
        model.addAttribute("user", remoteUser);
        return "rate-book";
    }

    @PostMapping("/save-review")
    public String reviewBook(@ModelAttribute Review review, Model model) throws ReviewSaveException {
        Book book = review.getBook();
        User user = review.getUser();
        if(reviewService.findByUserAndBook(user, book)==null){
            boolean addReview = reviewService.addReview(review);
            if(!addReview){
                throw new ReviewSaveException("Error al guardar reseña");
            }
        } else {
            Review ogRview = reviewService.findByUserAndBook(user, book);
            boolean modifyReview = reviewService.modifyReview(ogRview, review);
            if(!modifyReview){
                throw new ReviewSaveException("Error al guardar reseña");
            }
        }
        model.addAttribute("review", review);
        model.addAttribute("book", book);
        model.addAttribute("user", user);
        return "redirect:/rate-book/"+book.getId();
    }
}
