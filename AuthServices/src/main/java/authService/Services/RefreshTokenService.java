package authService.Services;

import authService.Entities.RefreshToken;
import authService.Entities.UserInfo;
import authService.Repository.RefreshTokenRepository;
import authService.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    public RefreshToken CreateRefreshToken(String username) {
        UserInfo userInfoFromRepo = userRepository.findByUsername(username);
        RefreshToken refreshToken = RefreshToken.builder().userInfo(userInfoFromRepo).token(UUID.randomUUID().toString()).expiryDate(Instant.now().plusMillis(6000000)).build();
        return refreshTokenRepository.save((refreshToken));
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken isRefreshTokenValid(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + "Refresh Token is expired.Please make a new login");
        }
        return token;
    }
}
