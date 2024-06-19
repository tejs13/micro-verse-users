package com.microverse.users.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken;

import com.microverse.users.security.jwtservices.JwtService;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	@Autowired
	private HandlerExceptionResolver handlerExceptionResolver;
	@Autowired
    private JwtService jwtService;
	@Autowired
    private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response, @NonNull  FilterChain filterChain)
			throws ServletException, IOException {
		
		final String authHeader = request.getHeader("Authorization");
		
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			System.out.println("HERE$$$$$$$$$$$$$$$$");
            filterChain.doFilter(request, response);  // continue other filters
            return;
        }
		
		
		try {
			
			System.out.println("HERE$$$$$$$$$$$$$$$$  in ELSE");
			
			final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwt);
            
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (userEmail != null && authentication == null) {  // initially authentication should be null
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
            
			
		}
		catch(ExpiredJwtException e) {
			System.out.println("HERE$$$$$$$$$$$$$$$$  in JWT EXCEPTION");

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Invalid JWT");
		}
		catch(Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(e.getLocalizedMessage());
//			 handlerExceptionResolver.resolveException(request, response, null, e);
			
		}
		
		
	}
	

}
