package com.movies.movieslist.security;

import com.movies.movieslist.security.exceptions.UnauthorizedException;
import com.movies.movieslist.token.Token;
import com.movies.movieslist.token.TokenRepository;
import com.movies.movieslist.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader= request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        jwt=authHeader.substring(7);
        userEmail=jwtService.extractUsername(jwt);
        
        Token jwtUser= tokenRepository.findByToken(jwt).get();
        if(jwtUser.isExpired()){
            throw new UnauthorizedException("User not logged");
        }

        if(userEmail !=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails= this.userDetailsService.loadUserByUsername(userEmail);

            if(jwtService.isTokenValid(jwt,userDetails)){
                if(jwtService.isTokenExpired(jwt) && !request.getServletPath().equals("/api/v1/auth/token/refresh'")){
                    throw new UnauthorizedException("Token expired");
                }

                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);

    }
}
