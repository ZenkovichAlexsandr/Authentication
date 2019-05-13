package com.assesment.authentification.implementation;

import com.assesment.authentification.implementation.security.handler.RestAccessDeniedHandler;
import com.assesment.authentification.implementation.security.handler.RestAuthenticationEntryPoint;
import com.assesment.authentification.implementation.security.service.JwtAuthenticationFilter;
import com.assesment.authentification.implementation.security.service.JwtAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile("!test")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final String[] allowedUrlsForPost = new String[]{"/auth/login", "/account"};
    private static final String[] allowedUrlsForGet = new String[]{"/health", "/v2/api-docs", "/swagger-ui.html",
        "/swagger-resources/**", "/webjars/**"
    };

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Configure authentication.
     *
     * @param authenticationManagerBuilder - authentication manager builder
     */
    @Autowired
    public void configureAuthentication(final AuthenticationManagerBuilder authenticationManagerBuilder)
            throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/**")
                .permitAll()
                .and()
                .csrf().disable()
                .addFilterAfter(new JwtAuthenticationFilter(authenticationManagerBean()),
                        BasicAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .accessDeniedHandler(new RestAccessDeniedHandler());
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.GET, allowedUrlsForGet)
                .antMatchers(HttpMethod.POST, allowedUrlsForPost)
                .antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .authenticationProvider(this.jwtAuthenticationProvider)
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(this.passwordEncoder());
    }
}
