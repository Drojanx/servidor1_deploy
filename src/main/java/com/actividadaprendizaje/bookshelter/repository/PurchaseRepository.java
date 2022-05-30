package com.actividadaprendizaje.bookshelter.repository;

import com.actividadaprendizaje.bookshelter.domain.Book;
import com.actividadaprendizaje.bookshelter.domain.Purchase;
import com.actividadaprendizaje.bookshelter.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PurchaseRepository extends CrudRepository<Purchase, Long> {

    List<Purchase> findAll();
    List<Purchase> findByUser(User user);
    Purchase findByUserAndBook(User user, Book book);
}
