package yongs.temp.service;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import yongs.temp.vo.User;

@Service
public class JwtTokenProviderService {
	private final String secretKey = "ThisIsMySecretKey";	
	private long accessTokenValidTime  = 1000L * 60 * 60 * 2; // 2시간 토큰 유효
	private long refreshTokenValidTime = 1000L * 60 * 60 * 24; // 24시간 토큰 유효

	// private long accessTokenValidTime  = 1000L * 60; // 60초 토큰 유효
	// private long refreshTokenValidTime = 1000L * 60; // 1분 토큰 유효
	
    // Jwt refresh 토큰 생성
    public String createRefreshToken(User user) {
    	String userId = user.getEmail();
    	Claims claims = Jwts.claims().setSubject(userId);
    	Date now = new Date();
    	
    	return Jwts.builder()
    			.setHeaderParam("typ", "JWT")
    			.setClaims(claims)
    			.setIssuedAt(now)
    			.setExpiration(new Date(now.getTime() + refreshTokenValidTime))
    			.signWith(SignatureAlgorithm.HS256, generateKey())
    			.compact();
    }
    
    // Jwt access 토큰 생성
    public String createAccessToken(User user) {
    	String userId = user.getEmail();
    	String username = user.getName();
    	List<String> roles = user.getRoles();

    	// Payload : userId + name + roles
    	Claims claims = Jwts.claims().setSubject(userId);
    	claims.put("name", username);
    	claims.put("roles", roles);
    	Date now = new Date();
    	
    	return Jwts.builder()
    			.setHeaderParam("typ", "JWT")
    			.setClaims(claims)
    			.setIssuedAt(now)
    			.setExpiration(new Date(now.getTime() + accessTokenValidTime))
    			.signWith(SignatureAlgorithm.HS256, generateKey())
    			.compact();
    }
    
    public boolean validateToken(String jwt) {
    	if(jwt != null) {
    		try{
    			Jwts.parser().setSigningKey(this.generateKey()).parseClaimsJws(jwt);
    			return true;            
            } catch (Exception e) {
            	return false;
            }   		
    	} else {
    		return false;
    	} 
    }
    
    private String generateKey(){
    	return Base64.getEncoder().encodeToString(this.secretKey.getBytes());
    }
}
