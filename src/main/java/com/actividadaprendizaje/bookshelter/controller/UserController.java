package com.actividadaprendizaje.bookshelter.controller;

import com.actividadaprendizaje.bookshelter.domain.Book;
import com.actividadaprendizaje.bookshelter.domain.Purchase;
import com.actividadaprendizaje.bookshelter.domain.Review;
import com.actividadaprendizaje.bookshelter.domain.User;
import com.actividadaprendizaje.bookshelter.exception.UserModificationException;
import com.actividadaprendizaje.bookshelter.exception.UserRegistrationException;
import com.actividadaprendizaje.bookshelter.service.BookService;
import com.actividadaprendizaje.bookshelter.service.PurchaseService;
import com.actividadaprendizaje.bookshelter.service.UserService;
import com.actividadaprendizaje.bookshelter.service.FileService;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private FileService fileService;

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/register-user")
    public String registerUser(Model model) {
        model.addAttribute("user", new User());
        return "register-user";
    }

    @PostMapping("/add-user")
    public String addUser(@ModelAttribute User user, Model model) throws UserRegistrationException {
        boolean userAdded = userService.add(user);
        if (!userAdded){
            throw new UserRegistrationException("Error al registrar el usuario");
        }
        model.addAttribute("user", user);
        return "add-user";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpServletRequest request) {
        String remoteUsername = request.getRemoteUser();
        User remoteUser = userService.findByUsername(remoteUsername);
        Review review = new Review();
        List<Purchase> purchaseList = purchaseService.findPurchases(remoteUser);
        List<Book> myBooks = new ArrayList<>();
        for(Purchase purchase : purchaseList){
            myBooks.add(purchase.getBook());
        }
        model.addAttribute("user", remoteUser);
        model.addAttribute("review", review);
        model.addAttribute("myBooks", myBooks);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String changeUserData(@ModelAttribute("userModify") User formUser, HttpServletRequest request, Model model) throws UserModificationException {
        String remoteUsername = request.getRemoteUser();
        User remoteUser = userService.findByUsername(remoteUsername);
        boolean userModified = userService.modifyUser(remoteUser, formUser);
        if (!userModified){
            throw new UserModificationException("Error al modificar el usuario");
        }
        model.addAttribute("user", remoteUser);
        return "redirect:/profile";
    }

    @GetMapping("/profile/orders")
    public String getUserOrders(Model model, @PathVariable long userId) {
        return "user-orders";
    }

    @ExceptionHandler(UserRegistrationException.class)
    public ModelAndView handleUserRegistrationException(HttpServletRequest request, UserRegistrationException exception) {
        logger.error("Error: " + exception.getMessage(), exception);

        ModelAndView mav = new ModelAndView();
        mav.addObject("message", "No se ha podido registrar el usuario. Por favor contacte con soporte técnico");
        mav.addObject("exception", exception);
        mav.addObject("url", request.getRequestURL());
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler({ TransactionSystemException.class })
    public ModelAndView handleConstraintViolationException(HttpServletRequest request, TransactionSystemException exception) {
        logger.error("Error: " + exception.getMessage(), exception);

        Throwable cause = ((TransactionSystemException) exception).getRootCause();
        ModelAndView mav = new ModelAndView();
        if (cause instanceof ConstraintViolationException){
            mav.addObject("message", "No se ha podido registrar el usuario. Por favor contacte con soporte técnico");
            mav.addObject("exception", exception);
            mav.addObject("url", request.getRequestURL());
            mav.setViewName("error");
            return mav;
        }
        return mav;
    }

    @ExceptionHandler
    public ModelAndView handleException(HttpServletRequest request, Exception exception) {
        logger.error("Error: " + exception.getMessage(), exception);

        ModelAndView mav = new ModelAndView();
        mav.addObject("message", exception.getMessage());
        mav.addObject("exception", exception);
        mav.addObject("url", request.getRequestURL());
        mav.setViewName("error");
        return mav;
    }
}

