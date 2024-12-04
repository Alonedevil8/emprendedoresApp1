package com.spring.emprendedoresApp.services.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.spring.emprendedoresApp.persistence.entities.UserEntity;
import com.spring.emprendedoresApp.services.IJWTUtilityService;

@Service
public class JWTUtilityServiceImpl implements IJWTUtilityService {

    // Inyección de las rutas de las claves públicas y privadas en formato PEM
    @Value("classpath:jwtKeys/private_key.pem")
    private Resource privateKeyResource;  // Ruta para la clave privada

    @Value("classpath:jwtKeys/public_key.pem")
    private Resource publicKeyResource;   // Ruta para la clave pública
    
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
    @Override
    public String generateJWT(UserEntity user, List<String> roles) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, JOSEException {
        // Cargar la clave privada desde el archivo PEM.
        PrivateKey privateKey = loadPrivateKey(privateKeyResource);
        
        // Crear un firmante utilizando la clave privada cargada.
        JWSSigner signer = new RSASSASigner(privateKey);

        // Definir la fecha y hora actuales como tiempo de emisión del token.
        Date now = new Date();
        
        String username = user.getUsername(); // Obtener el username
        
        // Construir el conjunto de reclamaciones (claims) del JWT.
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)  // El ID del usuario se incluye como "subject".
                .claim("authorities", roles)       // Añadir los roles al JWT.
                .issueTime(now)              // El tiempo de emisión es la fecha actual.
                .expirationTime(new Date(now.getTime() + 3600000))  // El token expira en 1 hora.
                .build();
        
        // Crear un objeto SignedJWT con las cabeceras y las reclamaciones.
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);
        
        // Firmar el JWT con el firmante configurado.
        signedJWT.sign(signer);
        
        // Devolver el JWT firmado en formato serializado (compacto).
        return signedJWT.serialize();
    }
    
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
    @Override
    public JWTClaimsSet parseJWT(String jwt) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, ParseException, JOSEException {
        // Cargar la clave pública desde el archivo PEM.
        PublicKey publicKey = loadPublicKey(publicKeyResource);
        
        // Analizar el JWT recibido para convertirlo en un objeto SignedJWT.
        SignedJWT signedJWT = SignedJWT.parse(jwt);
        
        // Crear un verificador de firma utilizando la clave pública.
        JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) publicKey);
        
        // Verificar si la firma del JWT es válida.
        if (!signedJWT.verify(verifier)) {
            throw new JOSEException("Invalid JWT signature");
        }
        
        // Obtener las reclamaciones del JWT.
        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
        
        // Verificar si el token ha expirado.
        if (claimsSet.getExpirationTime().before(new Date())) {
            throw new JOSEException("Expired token");
        }
        
        // Si el JWT es válido y no ha expirado, devolver las reclamaciones.
        return claimsSet;
    }

    /**
     * Carga una clave privada desde un archivo PEM.
     * 
     * @param resource El archivo que contiene la clave privada en formato PEM.
     * @return La clave privada (PrivateKey) cargada desde el archivo.
     * @throws IOException Si ocurre un problema al leer el archivo.
     * @throws NoSuchAlgorithmException Si el algoritmo RSA no está disponible.
     * @throws InvalidKeySpecException Si el formato de la clave no es válido.
     */
    private PrivateKey loadPrivateKey(Resource resource) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Leer los bytes del archivo de clave privada.
        byte[] keyBytes = Files.readAllBytes(Paths.get(resource.getURI()));

        // Limpiar la clave privada de las cabeceras y pies innecesarios.
        String privateKeyPEM = new String(keyBytes, StandardCharsets.UTF_8)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        // Decodificar la clave privada en Base64.
        byte[] decodedKey = Base64.getDecoder().decode(privateKeyPEM);

        // Crear una instancia de KeyFactory para el algoritmo RSA.
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        // Generar y devolver la clave privada desde el formato PKCS8.
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
    }

    /**
     * Carga una clave pública desde un archivo PEM.
     * 
     * @param resource El archivo que contiene la clave pública en formato PEM.
     * @return La clave pública (PublicKey) cargada desde el archivo.
     * @throws IOException Si ocurre un problema al leer el archivo.
     * @throws NoSuchAlgorithmException Si el algoritmo RSA no está disponible.
     * @throws InvalidKeySpecException Si el formato de la clave no es válido.
     */
    private PublicKey loadPublicKey(Resource resource) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Leer los bytes del archivo de clave pública.
        byte[] keyBytes = Files.readAllBytes(Paths.get(resource.getURI()));

        // Limpiar la clave pública de las cabeceras y pies innecesarios.
        String publicKeyPEM = new String(keyBytes, StandardCharsets.UTF_8)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        // Decodificar la clave pública en Base64.
        byte[] decodedKey = Base64.getDecoder().decode(publicKeyPEM);

        // Crear una instancia de KeyFactory para el algoritmo RSA.
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        // Generar y devolver la clave pública desde el formato X.509.
        return keyFactory.generatePublic(new java.security.spec.X509EncodedKeySpec(decodedKey));
    }
}

