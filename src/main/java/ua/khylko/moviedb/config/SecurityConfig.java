package ua.khylko.moviedb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ua.khylko.moviedb.security.JwtFilterManager;
import ua.khylko.moviedb.service.user.impl.UserServiceImpl;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserServiceImpl userService;
    private final JwtFilterManager jwtFilterManager;

    @Autowired
    public SecurityConfig(UserServiceImpl userService, JwtFilterManager jwtFilterManager) {
        this.userService = userService;
        this.jwtFilterManager = jwtFilterManager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                //
                .authorizeRequests()
                .antMatchers("/moviedb/auth/signup",
                                        "/moviedb/auth/signin",
                                        "/error", "/test",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                //
                .and()
                .formLogin()
                .loginPage("/moviedb/auth/signin")
                .loginProcessingUrl("/process_signin")
                .defaultSuccessUrl("/moviedb/user/info", true)
                .failureUrl("/moviedb/auth/signin?error")
                //
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/moviedb/auth/signin")
                //
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtFilterManager, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
