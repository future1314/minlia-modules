package com.minlia.modules.security.model.token;

import com.minlia.modules.security.exception.JwtExpiredTokenException;
import com.minlia.modules.security.exception.JwtInvalidTokenException;
import io.jsonwebtoken.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;

@ApiModel
@Slf4j
public class RawAccessJwtToken implements JwtToken {

    @ApiModelProperty(value = "令牌",example = "x001")
    private String token;
    
    public RawAccessJwtToken(String token) {
        this.token = token;
    }

    /**
     * Parses and validates JWT Token signature.
     */
    public Jws<Claims> parseClaims(String signingKey) {
        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            log.error("Invalid JWT Token", ex);
            throw new JwtInvalidTokenException("Invalid JWT token");
        } catch (ExpiredJwtException expiredEx) {
            log.info("JWT Token is expired", expiredEx);
            throw new JwtExpiredTokenException(this, "JWT Token expired", expiredEx);
        }
    }

    @Override
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
