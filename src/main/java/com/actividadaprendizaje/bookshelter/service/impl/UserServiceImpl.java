package com.actividadaprendizaje.bookshelter.service.impl;

import com.actividadaprendizaje.bookshelter.domain.Role;
import com.actividadaprendizaje.bookshelter.domain.User;
import com.actividadaprendizaje.bookshelter.repository.RoleRepository;
import com.actividadaprendizaje.bookshelter.repository.UserRepository;
import com.actividadaprendizaje.bookshelter.security.Constants;
import com.actividadaprendizaje.bookshelter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public boolean add(User user) {
        try{
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setCreationDate(LocalDate.now());
            user.setActive(true);
            Role userRole = roleRepository.findByName(Constants.USER_ROLE);
            user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex){
            return false;
        }
        return true;
    }

    @Override
    public User findUser(long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User findByUsername(String username) { return userRepository.findByUsername(username); }

    @Override
    public boolean modifyUser(User user, User formUser) {
        try{
            user.setName(formUser.getName());
            user.setSurname(formUser.getSurname());
            user.setBirthDate(formUser.getBirthDate());
            user.setEmail(formUser.getEmail());
            user.setUsername(formUser.getUsername());
            userRepository.save(user);

        } catch (DataIntegrityViolationException ex){
            return false;
        }
        return true;
    }
}
