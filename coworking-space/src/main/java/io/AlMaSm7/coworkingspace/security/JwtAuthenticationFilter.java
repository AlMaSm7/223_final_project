package io.AlMaSm7.coworkingspace.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.AlMaSm7.coworkingspace.repositories.UserRepo;
import lombok.val;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtServiceHMAC jwtService;
    private final UserRepo memberRepository;

    public JwtAuthenticationFilter(JwtServiceHMAC jwtService, UserRepo memberRepository) {
        this.jwtService = jwtService;
        this.memberRepository = memberRepository;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        val authToken = jwtService.resolveKey(request);
        long userId = 0;
        var requestedAuthorities = new ArrayList<String>();

        if (authToken != null) {
            DecodedJWT decoded;
            try {
                decoded = jwtService.verifyJwt(authToken, true);
                if(decoded.getClaim("user_id") == null){
                    userId = 0;
                } else {
                    System.out.println(decoded.getClaim("user_id").asString());
                    userId = Long.parseLong(decoded.getClaim("user_id").asString());
                }
                requestedAuthorities = jwtService.getRequestedAuthorities(decoded);
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }
        }

        if (userId != 0 && SecurityContextHolder.getContext().getAuthentication() == null) {
            var optionalUser = memberRepository.findById(userId);

            if (optionalUser.isEmpty()) {
                throw new JWTVerificationException("Unauthorized");
            }

            val user = optionalUser.get();

            val userDetails = jwtService.getUserDetails(user, requestedAuthorities);
            val authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            logger.debug("authenticated user $userId, setting security context");
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

}
