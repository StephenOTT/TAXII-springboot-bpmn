package io.digitalstate.taxii.security.filter;

import io.digitalstate.taxii.endpoints.context.TenantWebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Checks for the TenantSlug in the URL pattern,
 * and if found it sends it adds it into the Request Context for future use.
 */
@Component
public class PreAuthTenantFilter extends OncePerRequestFilter {

    private static PathPatternParser pathPatternParser = new PathPatternParser();

    @Value("${taxii.tenant.tenant_path_pattern:/taxii/tenant/{tenantSlug}/**}")
    private String tenantPathPattern;

    private String taxiPath = "/taxii/**";

    @Autowired
    private TenantWebContext tenantWebContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        PathPattern taxiiPathPattern = pathPatternParser.parse(taxiPath);
        boolean isTaxiiPath = taxiiPathPattern.matches(PathContainer.parsePath(request.getRequestURI()));

        if (!isTaxiiPath) {
        // If its not a Taxii related path then just keep going in the chain:
            filterChain.doFilter(request, response);

        // If it is a Taxii related path then parse for tenant info:
        } else {

            PathPattern tenantPathParser = pathPatternParser.parse(tenantPathPattern);
            PathContainer taxiiTenantContainer = PathContainer.parsePath(request.getRequestURI());

            // If its a Tenant Path then get the tenant info:
            if (tenantPathParser.matches(taxiiTenantContainer)) {
                Map<String, String> uriVariables = Optional.ofNullable(tenantPathParser.matchAndExtract(taxiiTenantContainer))
                        .orElseThrow(() -> new IllegalArgumentException("Tenant Path Pattern match failed"))
                        .getUriVariables();

                String slug = uriVariables.get("tenantSlug");

                if (!slug.isEmpty()) {
                    tenantWebContext.setTenantSlug(slug);
                } else {
                    //@TODO Review if a Optional.ofNullable can be used instead.
                    // Question is if the URL of something like tenant//collections, where the double slash will create a "blank" String rather than null
                    throw new IllegalArgumentException("Tenant Slug was blank or not found");
                }
            }
            // If its not a Tenant path (typically when getting a Taxii Discovery endpoint
            // or after the Tenant info has been collected
            filterChain.doFilter(request, response);
        }
    }
}
