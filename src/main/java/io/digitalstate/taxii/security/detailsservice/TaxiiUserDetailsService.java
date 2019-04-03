package io.digitalstate.taxii.security.detailsservice;

import io.digitalstate.taxii.mongo.model.document.UserDocument;
import io.digitalstate.taxii.mongo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class TaxiiUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(TaxiiUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //@TODO Review usage of how Tenant information passed into this method as currently the interface only support "username"

        UserDocument user = userRepository.findUserByUsername(username, null)
                .orElseThrow(() -> new UsernameNotFoundException("unable to find username in tenant"));

        return User.builder()
                .username(user.username())
                .password(user.passwordInfo().password())
                //@TODO Review authorities
                //@TODO Review Roles implementation
                .authorities(new SimpleGrantedAuthority("user"))
                .build();
    }
}
