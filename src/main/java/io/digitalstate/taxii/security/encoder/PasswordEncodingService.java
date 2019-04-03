package io.digitalstate.taxii.security.encoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncodingService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String encodeRawPassword(String rawPassword){
        return passwordEncoder.encode(rawPassword);
    }

}
