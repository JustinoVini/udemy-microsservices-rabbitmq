package br.com.udemy.productapi.modules.jwt.dto;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    private Integer id;
    private String name;
    private String email;

    public static JwtResponse getUser(Claims jwtClaims) {
        try {
            return new ObjectMapper().convertValue(jwtClaims.get("authUser"), JwtResponse.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
