package ru.sgmu.seem.webapp.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.sgmu.seem.webapp.repositories.UserDAO;
import ru.sgmu.seem.webapp.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableJpaRepositories(basePackageClasses = UserDAO.class)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private CustomUserDetailsService userDetailsService;
    private EncoderConfig encoderConfig;

    @Autowired
    public WebSecurityConfig(CustomUserDetailsService userDetailsService,
                             EncoderConfig encoderConfig) {
        this.userDetailsService = userDetailsService;
        this.encoderConfig = encoderConfig;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoderConfig.passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/news/**",
                        "/about/**",
                        "/staff/**",
                        "/contacts",
                        "/js/**",
                        "/css/**",
                        "/images/**",
                        "/favicons/**").permitAll()
                .antMatchers("/editor").hasAnyRole("MODER","ADMIN")
                .antMatchers("/editor/main/**").hasRole("ADMIN")
                .antMatchers("/editor/news/**").hasAnyRole("MODER", "ADMIN")
                .antMatchers("/editor/about/**").hasRole( "ADMIN")
                .antMatchers("/editor/about/**").hasRole( "ADMIN")
                .antMatchers("/editor/staff/**").hasRole( "ADMIN")
                .antMatchers("/editor/contacts/**").hasRole( "ADMIN")
                .antMatchers("/editor/forum/**").hasAnyRole( "MODER","ADMIN")
                .antMatchers("/editor/users/**").hasRole("ADMIN")
//                .antMatchers(HttpMethod.POST,"/forum/**").hasAnyRole( "TEACHER","MODER","ADMIN")
//                .antMatchers(HttpMethod.POST,"/editor/news/**").hasAnyRole( "ADMIN","MODER")
//                .antMatchers(HttpMethod.POST,"/editor/forum/**").hasAnyRole( "MODER","ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login-error").permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/wrongpage")
                .and()
                .csrf().ignoringAntMatchers("/forum/files/**");
    }
}
