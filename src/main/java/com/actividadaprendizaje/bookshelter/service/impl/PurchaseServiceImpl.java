package com.actividadaprendizaje.bookshelter.service.impl;

import com.actividadaprendizaje.bookshelter.domain.Book;
import com.actividadaprendizaje.bookshelter.domain.Purchase;
import com.actividadaprendizaje.bookshelter.domain.User;
import com.actividadaprendizaje.bookshelter.repository.PurchaseRepository;
import com.actividadaprendizaje.bookshelter.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public boolean addPurchase(Book book, User user) {
        try{
            Purchase purchase = new Purchase();
            purchase.setCreationDate(LocalDate.now());
            purchase.setBook(book);
            purchase.setUser(user);
            user.addPurchaseToUser(purchase);
            purchaseRepository.save(purchase);
        } catch (DataIntegrityViolationException ex){
            return false;
        }
        return true;
    }

    @Override
    public List<Purchase> findPurchases(User user) {
        return purchaseRepository.findByUser(user);
    }

    @Override
    public boolean findPurchaseByUserAndBook(User user, Book book) {
        return purchaseRepository.findByUserAndBook(user, book)!=null;
    }


}
