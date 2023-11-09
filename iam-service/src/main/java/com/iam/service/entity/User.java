package com.iam.service.entity;

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
@Table(name = "tm_users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", unique = true, nullable = false)
	private Long userId;

	@Column(name = "uuid", unique = true, nullable = false)
	private String uuid;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "role", nullable = false)
	private String role;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted;

	@Column(name = "client_id", nullable = false)
	private String clientId;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date", nullable = false)
	private String createdDate;
}
