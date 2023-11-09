package com.iam.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iam.service.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	@Query(value = "SELECT ur from User ur WHERE ur.username =:userName AND ur.isDeleted = false")
	Optional<User> fetchUserDetailByUserName(String userName);

	@Query(value = "SELECT ur from User ur WHERE ur.userId =:userId AND ur.isDeleted = false")
	Optional<User> fetchUserDetailById(Long userId);

}
