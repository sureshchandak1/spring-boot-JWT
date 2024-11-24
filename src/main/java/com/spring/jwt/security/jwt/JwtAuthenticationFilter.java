package com.spring.jwt.security.jwt;

import com.spring.jwt.utils.JsonKeys;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger mLogger = LoggerFactory.getLogger(OncePerRequestFilter.class);

    @Autowired
    private JwtHelper mJwtHelper;

    @Autowired
    private UserDetailsService mUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestHeader = request.getHeader(JsonKeys.KEY_AUTHORIZATION);
        mLogger.info(JsonKeys.KEY_AUTHORIZATION, requestHeader);

        String userName = null;
        String token = null;

        if (requestHeader != null && requestHeader.startsWith(JsonKeys.KEY_BEARER)) {

            token = requestHeader.substring(7);

            try {

                userName = this.mJwtHelper.getUsernameFromToken(token);

            } catch (IllegalArgumentException e) {
                logger.info("Illegal Argument while fetching the username !!");
            } catch (ExpiredJwtException e) {
                logger.info("Given jwt token is expired !!");
            } catch (MalformedJwtException e) {
                logger.info("Some changed has done in token !! Invalid Token");
            } catch (Exception e) {
                throw new RuntimeException("Something went wrong! Please try again...");
            }
        } else {
            mLogger.info("Invalid Header Value!");
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // fetch user detail from username
            UserDetails userDetails = this.mUserDetailsService.loadUserByUsername(userName);
            Boolean isValidToken = this.mJwtHelper.validateToken(token, userDetails);
            if (isValidToken) {

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // set authentication
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else {
                mLogger.info("Validation fails!");
            }
        }

        filterChain.doFilter(request, response);

    }
}
