package com.hireach.congestiontracing.config;

import com.giffing.bucket4j.spring.boot.starter.servlet.ServletRequestFilter;
import com.hireach.congestiontracing.entity.Company;
import com.hireach.congestiontracing.filter.APIKeyAuthFilter;
import com.hireach.congestiontracing.repository.CompanyRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import java.util.Objects;
import java.util.Optional;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA3_256;

@Configuration
@EnableWebSecurity
public class APISecurityConfig extends WebSecurityConfigurerAdapter {

    private final ApplicationContext context;
    private final CompanyRepository companyRepository;

    public APISecurityConfig(ApplicationContext context, CompanyRepository companyRepository) {
        this.context = Objects.requireNonNull(context, "applicationContext should not be null");
        this.companyRepository = Objects.requireNonNull(companyRepository, "companyRepository should not be null");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        APIKeyAuthFilter filter = new APIKeyAuthFilter();

        filter.setAuthenticationManager(authentication -> {
            String urlApiKey = (String) authentication.getPrincipal();
            String hashedUrlApiKey = new DigestUtils(SHA3_256).digestAsHex(urlApiKey);

            Optional<Company> company = companyRepository.findByAccessKey(hashedUrlApiKey);

            if (company.isEmpty()) {
                throw new BadCredentialsException("The API key was not found or not the expected value.");
            }
            authentication.setAuthenticated(true);

            return authentication;
        });

        httpSecurity
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/documentation.html")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(filter)
                .addFilterAfter(context.getBean(ServletRequestFilter.class), APIKeyAuthFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

}
