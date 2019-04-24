package io.digitalstate.taxii.security;

import io.digitalstate.taxii.security.detailsservice.TaxiiUserDetailsService;
import io.digitalstate.taxii.security.entrypoint.TaxiiBasicAuthenticationEntryPoint;
import io.digitalstate.taxii.security.filter.PreAuthTenantFilter;
//import io.digitalstate.taxii.security.filter.PostAuthTenantContextFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true)
@EnableWebSecurity
public class TaxiiSecurityProvider extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(TaxiiSecurityProvider.class);

    @Autowired
    private TaxiiUserDetailsService userDetailsService;

    @Value("${taxii.basic-auth.enabled:true}")
    private boolean isBasicAuthEnabled;

    @Autowired
    private TaxiiBasicAuthenticationEntryPoint authenticationEntryPoint;

//    @Autowired
//    private PostAuthTenantContextFilter postAuthTenantContextFilter;

    @Autowired
    private PreAuthTenantFilter preAuthTenantFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if (isBasicAuthEnabled) {
            logger.info("TAXII BASIC-AUTH SECURITY IS ENABLED.");
            http.csrf().disable()
                    .authorizeRequests().anyRequest().authenticated()
                    .and().httpBasic().authenticationEntryPoint(authenticationEntryPoint)
                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            http.addFilterBefore(preAuthTenantFilter, BasicAuthenticationFilter.class);

        } else {
            logger.warn("TAXII BASIC-AUTH SECURITY IS DISABLED.");
            http.csrf().disable()
                    .sessionManagement().disable()
                    .authorizeRequests().antMatchers("/**").permitAll();
        }
    }

    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService);
    }
}
