package com.actividadaprendizaje.bookshelter.service;

import com.actividadaprendizaje.bookshelter.domain.Book;
import com.actividadaprendizaje.bookshelter.domain.Purchase;
import com.actividadaprendizaje.bookshelter.domain.User;

import java.util.List;

public interface PurchaseService {

    boolean addPurchase(Book book, User user);
    List<Purchase> findPurchases(User user);
    boolean findPurchaseByUserAndBook(User user, Book book);
}
