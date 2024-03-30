package br.com.eterniaserver.tuff.domain.user.services;

import br.com.eterniaserver.tuff.Constants;
import br.com.eterniaserver.tuff.domain.user.dtos.TokenDto;
import br.com.eterniaserver.tuff.domain.user.entities.UserAccount;

import br.com.eterniaserver.tuff.domain.user.models.AuthenticateRequest;
import br.com.eterniaserver.tuff.domain.user.models.AuthenticateResponse;
import br.com.eterniaserver.tuff.domain.user.models.VerifyTokenRequest;
import br.com.eterniaserver.tuff.domain.user.models.VerifyTokenResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class JWTService {

    @Value("${spring.security.jwt.expiration}")
    private String expiration;

    @Value("${spring.security.jwt.signature-key}")
    private String signatureKey;

    private Optional<SecretKey> secretKey = Optional.empty();

    private final PasswordEncoder passwordEncoder;

    public JWTService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public VerifyTokenResponse verify(VerifyTokenRequest request) {
        if (!request.token().contains("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constants.INVALID_TOKEN);
        }

        String token = request.token().split(" ")[0];
        if (!isValidToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constants.INVALID_TOKEN);
        }

        return new VerifyTokenResponse();
    }

    public AuthenticateResponse authenticate(AuthenticateRequest request) {
        UserAccount userAccount = request.getUserAccount();
        String password = request.getPassword();

        if (!passwordEncoder.matches(password, userAccount.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constants.INVALID_CREDENTIALS);
        }

        long exp = Long.parseLong(expiration);
        LocalDateTime expDate = LocalDateTime.now().plusMinutes(exp);
        Date date = Date.from(expDate.atZone(ZoneId.systemDefault()).toInstant());

        String login = userAccount.getLogin();
        String token = Jwts.builder().subject(login).expiration(date).signWith(getSecretKey()).compact();
        String[] roles = userAccount.getRoles().toArray(String[]::new);

        return new AuthenticateResponse(new TokenDto(login, token, roles));
    }

    public String getUserLogin(String token) {
        return decodeToken(token).getSubject();
    }

    public boolean isValidToken(String token) {
        Claims claims = decodeToken(token);
        Date expDate = claims.getExpiration();
        LocalDateTime date = expDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return !LocalDateTime.now().isAfter(date);
    }

    private Claims decodeToken(String token) {
        return Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSecretKey() {
        if (secretKey.isEmpty())
            secretKey = Optional.of(Keys.hmacShaKeyFor(signatureKey.getBytes(StandardCharsets.UTF_8)));

        return secretKey.get();
    }

}
