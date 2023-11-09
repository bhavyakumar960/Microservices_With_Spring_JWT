package com.iam.service.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@Table(name = "isfa_user_session")
@AllArgsConstructor
@NoArgsConstructor
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "uuid", unique = true, nullable = false)
    private String uuid;

    @Column(name = "token", unique = true, nullable = false, length = 1200)
    private String token;

    @Column(name = "token_type", nullable = false)
    private String tokenType;

    @Column(name = "token_expiry", nullable = false)
    private LocalDateTime tokenExpiry;

    @Column(name = "tokenExpired", nullable = false)
    private Boolean tokenExpired;

    @Column(name = "user_id", nullable = false)
    private Long user_id;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private String createdDate;
}
