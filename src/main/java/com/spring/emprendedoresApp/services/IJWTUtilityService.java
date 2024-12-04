package com.spring.emprendedoresApp.services;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.List;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.spring.emprendedoresApp.persistence.entities.UserEntity;

public interface IJWTUtilityService {

	
    /**
     * Valida y analiza un JWT recibido. Si es válido, devuelve las reclamaciones del token.
     * 
     * @param jwt El token JWT a validar.
     * @return Las reclamaciones del JWT (JWTClaimsSet) si el token es válido.
     * @throws NoSuchAlgorithmException Si el algoritmo RSA no está disponible.
     * @throws InvalidKeySpecException Si el formato de la clave no es válido.
     * @throws IOException Si ocurre un problema al leer los archivos de clave.
     * @throws ParseException Si ocurre un error al analizar el JWT.
     * @throws JOSEException Si el JWT tiene una firma no válida o ha expirado.
     */
    public JWTClaimsSet parseJWT(String jwt)
            throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, ParseException, JOSEException;

	/**
	 * Genera un JSON Web Token (JWT) firmado con la clave privada.
	 * Este JWT contiene el ID del usuario como su sujeto y tiene una validez de 1 hora.
	 * 
	 * @param userId ID del usuario para incluirlo en las reclamaciones del token.
	 * @return Un JWT firmado en formato String.
	 * @throws NoSuchAlgorithmException Si el algoritmo RSA no está disponible.
	 * @throws InvalidKeySpecException Si el formato de la clave no es válido.
	 * @throws IOException Si ocurre un problema al leer los archivos de clave.
	 * @throws JOSEException Si ocurre un problema al generar el JWT.
	 */
	String generateJWT(UserEntity user, List<String> roles)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, JOSEException;
}
