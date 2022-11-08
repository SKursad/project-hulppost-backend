package nl.novi.hulppost.config;

import nl.novi.hulppost.security.CustomUserDetailsService;
import nl.novi.hulppost.security.JwtAuthenticationEntryPoint;
import nl.novi.hulppost.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/images/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/requests").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/requests/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/replies").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/replies/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/users").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/users/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/accounts").authenticated()
                .antMatchers(HttpMethod.GET, "/api/v1/accounts/**").authenticated()
                .antMatchers(HttpMethod.POST, "/api/v1/requests/**/image").authenticated()
                .antMatchers(HttpMethod.POST, "/api/v1/requests").hasAnyRole("HELP-SEEKER","ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/replies").authenticated()
                .antMatchers(HttpMethod.POST, "/api/v1/requests/**").authenticated()
                .antMatchers(HttpMethod.POST, "/api/v1/replies/**").authenticated()
                .antMatchers(HttpMethod.POST, "/api/v1/users").authenticated()
                .antMatchers(HttpMethod.POST, "/api/v1/accounts").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/v1/requests/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/v1/replies/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/v1/users/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/v1/users/**/image").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/v1/accounts/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/v1/accounts/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/v1/users/").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/replies/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/v1/requests/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/v1/users/**/deleteImage").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/v1/requests/**/deleteImage").authenticated()
                .antMatchers("/api/v1/auth/**").permitAll()
                .anyRequest()
                .authenticated();

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

}
