package com.actividadaprendizaje.bookshelter.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.actividadaprendizaje.bookshelter.security.Constants.*;


/**
 * Configuración general de seguridad para Spring Security
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    private BookShelterUserDetailsService userDetailsService;

    /**
     * Configuración de la autenticación de usuario
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    /**
     * Configuración de seguridad
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/add-user").permitAll()
                .antMatchers("/register-user").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/new-user").permitAll()
                .antMatchers("/admin/**").hasAuthority(ADMIN_ROLE).anyRequest()
                .authenticated().and().csrf().disable()
                .formLogin()
                    .loginPage(LOGIN_URL)
                    .defaultSuccessUrl(LOGIN_SUCCESS_URL, true)
                    .failureUrl(LOGIN_FAILURE_URL)
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_URL))
                    .logoutSuccessUrl(LOGOUT_SUCCESS_URL);
    }

    /**
     * Permite el acceso al contenido estático
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/resources/**",
                "/static/**",
                "/templates/**",
                "/css/**",
                "/js/**",
                "/images/**",
                "/fonts/**",
                "/webjars/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

