package com.actividadaprendizaje.bookshelter.service;

import com.actividadaprendizaje.bookshelter.domain.Book;
import com.actividadaprendizaje.bookshelter.domain.User;

public interface UserService {

    boolean add(User user);
    User findUser(long id);
    User findByUsername(String username);
    boolean modifyUser(User user, User formUser);
}
