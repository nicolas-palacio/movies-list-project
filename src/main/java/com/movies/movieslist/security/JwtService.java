package com.movies.movieslist.security;


import com.movies.movieslist.security.exceptions.UnauthorizedException;
import com.movies.movieslist.token.Token;
import com.movies.movieslist.token.TokenRepository;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class JwtService {
    //private Dotenv dotenv;

    private final TokenRepository tokenRepository;

    public String extractUsername(String token){

        return exctractClaim(token,Claims::getSubject);
    }


    public <T> T exctractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims=extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

    public boolean isTokenValid(String token,UserDetails userDetails){
        final String username=extractUsername(token);


        return (username.equals(userDetails.getUsername()));
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new java.util.Date());

    }


    private java.util.Date extractExpiration(String token) {
        return exctractClaim(token,Claims::getExpiration);
    }


    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails){


        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Claims extractAllClaims(String token){
        Claims claims=null;
        try{
            claims=Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
        }catch(io.jsonwebtoken.ExpiredJwtException e){
            tokenRepository.findByToken(token).get().setExpired(true);
            throw new UnauthorizedException("Token expired.");
        }
        return claims;

    }

    private Key getSignKey() {
        Dotenv dotenv = Dotenv
                .configure()
                .filename("env.properties")
                .load();
        byte[] keyBytes= Decoders.BASE64.decode(dotenv.get("SECRET_KEY").toString());

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
