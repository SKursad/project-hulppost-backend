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
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return  new JwtAuthenticationFilter();
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
                .antMatchers(HttpMethod.GET, "/hulppost/requests/**").permitAll()
                .antMatchers(HttpMethod.GET, "/hulppost/requests").permitAll()
                .antMatchers(HttpMethod.GET, "/hulppost/replies/**").permitAll()
                .antMatchers(HttpMethod.GET, "/hulppost/users/**").permitAll()
                .antMatchers(HttpMethod.GET, "/hulppost/accounts").authenticated()
                .antMatchers(HttpMethod.POST, "/hulppost/requests/**/image").authenticated()
                .antMatchers(HttpMethod.POST, "/hulppost/requests/**").authenticated()
                .antMatchers(HttpMethod.POST, "/hulppost/replies/**").authenticated()
                .antMatchers(HttpMethod.POST, "/hulppost/replies").authenticated()
                .antMatchers(HttpMethod.POST, "/hulppost/requests").authenticated()
                .antMatchers(HttpMethod.POST, "/hulppost/users").authenticated()
                .antMatchers(HttpMethod.POST, "/hulppost/accounts").authenticated()
                .antMatchers(HttpMethod.PUT, "/hulppost/requests/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/hulppost/replies/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/hulppost/users/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/hulppost/accounts/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/hulppost/accounts/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/hulppost/users/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/hulppost/replies/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/hulppost/requests/**").authenticated()
                .antMatchers("/auth/**").permitAll()
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
