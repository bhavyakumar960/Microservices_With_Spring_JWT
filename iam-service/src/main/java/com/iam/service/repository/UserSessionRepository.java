package com.iam.service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iam.service.entity.UserSession;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    @Query(value = "SELECT us from UserSession us WHERE us.user_id =:userId AND us.tokenExpired = false")
    Optional<UserSession> findActiveSessionById(Long userId);

    @Query(value = "SELECT us from UserSession us WHERE us.user_id =:userId AND us.token =:token")
    Optional<UserSession> findSessionToken(String token, Long userId);

    @Query(value = "SELECT us from UserSession us WHERE us.tokenExpired = false")
    List<UserSession> findAllSessions();

}
