//    Congestion API - a REST API built to track congestion spots and
//    crowded areas using real-time location data from mobile devices.
//
//    Copyright (C) 2020, University Politehnica of Bucharest, member
//    of the HiReach Project consortium <https://hireach-project.eu/>
//    <andrei[dot]gheorghiu[at]upb[dot]ro. This project has received
//    funding from the European Union’s Horizon 2020 research and
//    innovation programme under grant agreement no. 769819.
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <https://www.gnu.org/licenses/>.
package com.hireach.congestiontracingstandalone.config;

import com.giffing.bucket4j.spring.boot.starter.servlet.ServletRequestFilter;
import com.hireach.congestiontracingstandalone.component.CompanyWrapper;
import com.hireach.congestiontracingstandalone.entity.Company;
import com.hireach.congestiontracingstandalone.filter.APIKeyAuthFilter;
import com.hireach.congestiontracingstandalone.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import java.util.Optional;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA3_256;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class APISecurityConfig extends WebSecurityConfigurerAdapter {

    private final ApplicationContext context;
    private final CompanyRepository companyRepository;
    private final CompanyWrapper companyWrapper;

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
            companyWrapper.setCompany(company.get());
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
