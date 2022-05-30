package com.actividadaprendizaje.bookshelter.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.actividadaprendizaje.bookshelter.security.Constants.ADMIN_ROLE;

/**
 * Métodos de utilidad relacionados con la seguridad
 */
public class SecurityUtils {

    private SecurityUtils() {}

    /**
     * Comprueba si el usuario está autenticado
     * @return true si el usuario está autenticando y false en caso contrario
     */
    public static boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }

    /**
     * Comprueba si el usuario autenticado tiene rol de administrador
     * @return true si el usuario tiene el rol de administrador y false en caso contrario
     */
    public static boolean isAdminUser() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(Object::toString)
                .anyMatch(item -> item.startsWith("ROLE_" + ADMIN_ROLE));
    }
}
