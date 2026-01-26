package com.eb.eventsbridge.modules.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eb.eventsbridge.modules.auth.entity.RefreshToken;
import com.eb.eventsbridge.modules.auth.entity.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long>{
	public Optional<RefreshToken> findByToken(String token);
	
	public List<RefreshToken> findByUserAndIsRevokedFalse(User user);

    public void deleteByToken(String refreshToken);

}
