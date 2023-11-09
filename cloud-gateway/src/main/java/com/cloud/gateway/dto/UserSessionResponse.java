package com.cloud.gateway.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserSessionResponse {
    private String access_token;
    private String token_type;
    private String token_expiry;
    private boolean valid;
}
