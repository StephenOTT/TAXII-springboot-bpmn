package io.digitalstate.taxii.security.entrypoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class TaxiiBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(TaxiiBasicAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authEx) throws IOException, ServletException {

        logger.error(authEx.getMessage(), authEx);

        response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        //@TODO Rebuild with JSON writer
        PrintWriter writer = response.getWriter();
        writer.println("HTTP Status 401 - " + authEx.getMessage());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //@TODO Review to convert this to being the tenant info
        setRealmName("Taxii");
        super.afterPropertiesSet();
    }
}
