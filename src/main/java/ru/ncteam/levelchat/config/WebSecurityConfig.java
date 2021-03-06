package ru.ncteam.levelchat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import ru.ncteam.levelchat.authentication.AuthenticationSuccessHandlerImpl;
import ru.ncteam.levelchat.dao.UserLogDAO;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private UserLogDAO userLogDAOImpl;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
                .authorizeRequests()
                .antMatchers("/","/index*","/userpage*").hasAnyRole("USER","ADMIN")
                .antMatchers("/adminpage*").hasRole("ADMIN")
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                .loginProcessingUrl("/j_spring_security_check")
                .failureUrl("/login?error=true")
                .successHandler(getAuthenticationSuccessHandlerImpl())
                .and()
                .logout()
                .permitAll()
                .and()
                .rememberMe()
                .and()
                .csrf().disable();
    }
    
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.authenticationProvider((AuthenticationProvider) userLogDAOImpl);
        auth.userDetailsService((UserDetailsService)userLogDAOImpl);
    }*/

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService((UserDetailsService)userLogDAOImpl);
    }
    
    @Bean
    public AuthenticationSuccessHandlerImpl getAuthenticationSuccessHandlerImpl() {
    	AuthenticationSuccessHandlerImpl successHandler = new AuthenticationSuccessHandlerImpl(); 
        return successHandler;
    }
    
}
