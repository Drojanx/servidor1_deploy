package com.actividadaprendizaje.bookshelter.security;

import com.actividadaprendizaje.bookshelter.service.UserService;
import com.actividadaprendizaje.bookshelter.domain.Role;
import com.actividadaprendizaje.bookshelter.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service que permite validar las credenciales de un usuario
 * contra la base de datos e iniciar la sesión de usuario
 */
@Service
public class BookShelterUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(BookShelterUserDetailsService.class);

    @Autowired
    private UserService userService;

    /**
     * Valida las credenciales de usuario
     * @param username El nombre de usuario
     * @return Los detalles del usuario que inicia la sesión
     * @throws UsernameNotFoundException En caso de que el usuario no exista
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("Usuario/contraseña incorrectos");

        List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
        return buildUserForAuthentication(user, authorities);
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        userRoles.forEach(role -> roles.add(new SimpleGrantedAuthority(role.getName())));
        return new ArrayList<>(roles);
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.isActive(), true, true, true, authorities);
    }
}
