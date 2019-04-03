package io.digitalstate.taxii.security;

import io.digitalstate.taxii.security.detailsservice.TaxiiUserDetailsService;
import io.digitalstate.taxii.security.entrypoint.TaxiiBasicAuthenticationEntryPoint;
import io.digitalstate.taxii.security.filter.HttpBasicSecurityFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class TaxiiSecurityProvider extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(TaxiiSecurityProvider.class);

    @Autowired
    private TaxiiUserDetailsService userDetailsService;

    @Value("${taxii.basic-auth.enabled:true}")
    private boolean securityIsEnabled;

    @Autowired
    private TaxiiBasicAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
//        authProvider.setHideUserNotFoundExceptions(true);
        return authProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (securityIsEnabled) {
            logger.info("TAXII BASIC-AUTH SECURITY IS ENABLED!!");
            http.csrf().disable()
                    .authorizeRequests().anyRequest().authenticated()
                    .and().httpBasic()
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .and().sessionManagement().disable();

            http.addFilterAfter(new HttpBasicSecurityFilter(), BasicAuthenticationFilter.class);
        } else {
            logger.warn("TAXII BASIC-AUTH SECURITY IS DISABLED!!");
            http.authorizeRequests().antMatchers("/**").permitAll();
        }
    }

    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService);
    }

}
