package one.june.snippetbox.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

class JwtUtilTest {
    @Test
    void shouldGenerateJwtTokenForUser() {
        JwtUtil jwtUtil = new JwtUtil("4cEml04vr8U9ydS3ZHoSXLTrZ/Ab2wi7DluPMOdL++qsiDmQv4E+vD7QkC0iTBkGUULMsvC5Ma50Zzm1kyzo0g==", 60L * 60 * 1000);

        String token = jwtUtil.generateToken("username");

        Assertions.assertNotNull(token);
        Assertions.assertTrue(Pattern.matches("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$", token));
    }

    @Test
    void shouldExtractClaimsFromValidToken() {
        JwtUtil jwtUtil = new JwtUtil("4cEml04vr8U9ydS3ZHoSXLTrZ/Ab2wi7DluPMOdL++qsiDmQv4E+vD7QkC0iTBkGUULMsvC5Ma50Zzm1kyzo0g==", 60L * 60 * 1000);
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTczNjMyNDg3MCwiZXhwIjoyMDUxNjg0ODcwfQ.v-rCFxIzIFKI2LygcMzJT_h2kewRd2CSuxeo5y7LtNk";

        Claims claims = jwtUtil.extractClaims(token);

        Assertions.assertEquals("username", claims.getSubject());
    }

    @Test
    void shouldThrowExceptionForInvalidToken() {
        JwtUtil jwtUtil = new JwtUtil("4cEml04vr8U9ydS3ZHoSXLTrZ/Ab2wi7DluPMOdL++qsiDmQv4E+vD7QkC0iTBkGUULMsvC5Ma50Zzm1kyzo0g==", 60L * 60 * 1000);
        String token = "invalid token";

        Assertions.assertThrows(MalformedJwtException.class, () -> jwtUtil.extractClaims(token));
    }

    @Test
    void shouldThrowExceptionForCorruptToken() {
        JwtUtil jwtUtil = new JwtUtil("4cEml04vr8U9ydS3ZHoSXLTrZ/Ab2wi7DluPMOdL++qsiDmQv4E+vD7QkC0iTBkGUULMsvC5Ma50Zzm1kyzo0g==", 60L * 60 * 1000);
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtIsImlhdCI6MTczNjMyNDg3MCwiZXhwIjoyMDUxNjg0ODcwfQ.v-rCFxIzIFKI2LygcMzJT_h2kewRd2CSuxeo5y7LtNk";

        Assertions.assertThrows(SignatureException.class, () -> jwtUtil.extractClaims(token));
    }
}