package com.PDF.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

/**
 * Created by martanase on 4/10/2017.
 */

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

   @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("Configuration");
       //http
               //.authorizeRequests().anyRequest().authenticated().and()
        http
                .httpBasic()
                .and()
            .authorizeRequests()
                .antMatchers("/user/authenticate").anonymous()
                .antMatchers("/**",
                        "/index.html",
                        "/login.html",
                        "/static/**",
                        "/template/**",
                        "/settings/**",
                        "/css/**",
                        "/js/**",
                        "/library/**")
                //.antMatchers("/")
                .permitAll()
                .anyRequest().authenticated()
            .and()
                .csrf()
                .disable()
                .headers()
                .frameOptions().disable()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .and()
                .logout()
                .permitAll();
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

/*    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
    }*/

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }
}

