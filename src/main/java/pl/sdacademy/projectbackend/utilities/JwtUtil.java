package pl.sdacademy.projectbackend.utilities;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.sdacademy.projectbackend.exceptions.BadRequestException;
import pl.sdacademy.projectbackend.oauth.facebook.model.UserPrincipal;

import java.util.Date;
import java.util.function.Function;

@Service
public class JwtUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("${jwt.expiration.time}")
    private Long EXPIRATION_TIME;

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public String extractLogin(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    public String generateToken(UserDetails user) {
        return createToken(user);
    }

    private String createToken(UserDetails user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
    
    public Boolean validateToken(String token, UserDetails user) {
        final String login = extractLogin(token);
        return (login.equals(user.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    private String extractRole(UserPrincipal userPrincipal) {
        return userPrincipal.getAuthorities()
                .toString()
                .replace('[', ' ')
                .replace(']', ' ')
                .trim();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }


    public String generateToken(Authentication authentication) {
        return createToken(authentication);
    }

    private String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .claim("email", userPrincipal.getUsername())
                .claim("role", extractRole(userPrincipal))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            throw new BadRequestException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new BadRequestException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new BadRequestException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new BadRequestException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("JWT claims string is empty.");
        }
    }

}
