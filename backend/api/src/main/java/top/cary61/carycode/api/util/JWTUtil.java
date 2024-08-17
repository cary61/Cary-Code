package top.cary61.carycode.api.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.cary61.carycode.api.entity.TokenObject;
import top.cary61.carycode.api.mapper.InvalidTokenMapper;
import top.cary61.carycode.api.mapper.UserStateMapper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
public class JWTUtil {

    @Resource
    private UserStateMapper userStateMapper;

    @Resource
    private InvalidTokenMapper invalidTokenMapper;

    private final Algorithm algorithm;

    private final long validityPeriodDays;

    private final JWTVerifier jwtVerifier;


    private final String authPrefix = "Bearer ";

    private final int startIndex = authPrefix.length();



    public JWTUtil(@Value("${jwt.hash-key}") String hashKey,
                   @Value("${jwt.validity-period-days}") long validityPeriodDays) {
        this.algorithm = Algorithm.HMAC256(hashKey);
        this.validityPeriodDays = validityPeriodDays;
        this.jwtVerifier = JWT.require(algorithm)
                .withClaimPresence("userId")
                .build();
    }

    public String encode(long userId) {
        LocalDateTime now = LocalDateTime.now();
        return JWT.create()
                .withClaim("userId", userId)
                .withJWTId(UUID.randomUUID().toString())
                .withIssuedAt(TimeUtil.toDate(now))
                .withExpiresAt(TimeUtil.toDate(now.plusDays(validityPeriodDays)))
                .sign(algorithm);
    }

    private TokenObject decode(String token) {
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            long userId = decodedJWT.getClaim("userId").asLong();
            return new TokenObject(
                    userId,
                    decodedJWT.getId(),
                    TimeUtil.toLocalDateTime(decodedJWT.getIssuedAt()),
                    TimeUtil.toLocalDateTime(decodedJWT.getExpiresAt()),
                    userStateMapper.selectById(userId)
            );
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获得当前请求的token的对象表示
     *
     * @param authorization HTTP 认证头
     * @return null if token is not valid
     */
    public TokenObject parseAndCheck(String authorization) {
        // 认证头不对
        if (authorization == null || !authorization.startsWith(authPrefix)) {
            return null;
        }
        String token = authorization.substring(startIndex);
        TokenObject tokenObject = decode(token);
        // JWT Token 验证未通过
        if (tokenObject == null) {
            return null;
        }
        long userId = tokenObject.getUserId();
        // JWT Token 已被声明为无效 或 密码已过期
        if (invalidTokenMapper.selectByUuid(tokenObject.getUuid()) != null
                || tokenObject.getIssuedTime().isBefore(tokenObject.getUserState().getPasswordUpdateTime())) {
            return null;
        }
        return tokenObject;
    }

}
