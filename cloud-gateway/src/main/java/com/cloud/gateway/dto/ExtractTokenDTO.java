package com.cloud.gateway.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtractTokenDTO {
	String sub;
	String role;
	String clientId;
	String uuid;
	String iss;
	Long exp;
	Long iat;
	String email;
	String username;
}
