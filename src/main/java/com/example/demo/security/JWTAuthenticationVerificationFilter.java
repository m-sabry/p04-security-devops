package com.example.demo.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

/**
 * This class is responsible for the authorization process.
 * This class extends the BasicAuthenticationFilter class.
 * It overrides on method, and defines another custom method.
 */
@Component
@Slf4j
public class JWTAuthenticationVerificationFilter extends BasicAuthenticationFilter {
	
	public JWTAuthenticationVerificationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    /**
     * This method is used when we have multiple roles, and a policy for RBAC.
     * @param req
     * @param res
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
	@Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) 
    		throws IOException, ServletException {
        String header = req.getHeader(SecurityConstants.HEADER_STRING);

        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            if(header != null && !header.startsWith(SecurityConstants.TOKEN_PREFIX))
                log.info("  ===== Missing security token...");
            else
                log.info("  ===== Missing header... ");
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    /**
     * It validates the token read from the Authorization header.
     * @param req
     * @return
     */
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
	    log.info("  ===== Request: " + req.toString());
		String token = req.getHeader(SecurityConstants.HEADER_STRING);
        log.info("  ===== token: " + token);
        if (token != null) {
            String user = JWT.require(HMAC512(SecurityConstants.SECRET.getBytes())).build()
                    .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                    .getSubject();
            if (user != null) {
                log.info("  ===== Going to authenticate user: " + user);
                UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());

                if(upat != null) {
                    log.info("  ===== User authenticated!");
                    return upat;
                }
                else {
                    log.info("  ===== User not authenticated!");
                    return null;
                }
            }
            log.info("  ===== User not authenticated!");
            return null;
        }
        log.info("  ===== Missing token!");
        return null;
	}

}
