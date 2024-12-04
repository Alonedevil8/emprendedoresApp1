package com.spring.emprendedoresApp.security;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nimbusds.jwt.JWTClaimsSet;
import com.spring.emprendedoresApp.services.IJWTUtilityService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private IJWTUtilityService jwtUtilityService;

    public JWTAuthorizationFilter(IJWTUtilityService jwtUtilityService) {
		this.jwtUtilityService = jwtUtilityService;
	}

	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // Verificar si el encabezado es nulo o no tiene el prefijo "Bearer"
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraer el token del encabezado
        String token = header.substring(7);

        try {
            // Parsear el token para obtener los reclamos (claims)
            JWTClaimsSet claims = jwtUtilityService.parseJWT(token);
            
            // Extraer roles de las reclamaciones y asignarlos a Authorities
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.getClaim("authorities");

            // Convertir roles a GrantedAuthorities
            List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))  // Asegúrate de prefijar los roles con "ROLE_"
                .collect(Collectors.toList());

            // Crear un objeto de autenticación con el sujeto del token y los roles
            UsernamePasswordAuthenticationToken authenticationToken = 
                    new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);


            // Establecer el contexto de seguridad con la autenticación
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (Exception e) {
            // Manejar excepciones relacionadas con el parsing del token
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token.");
            return;
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
	
}

