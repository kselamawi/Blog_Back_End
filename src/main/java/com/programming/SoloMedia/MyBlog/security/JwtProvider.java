package com.programming.SoloMedia.MyBlog.security;


import com.programming.SoloMedia.MyBlog.exception.SpringBlogException;
import com.programming.SoloMedia.MyBlog.model.Bloger;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

// this class will be responsible for creating JSON web tokens
@Service
public class JwtProvider {
   /* Here we're going to create a method called generateToken which takes Authenticate as a parameter and returns an object of type
     authenticate back.But before that,we need to go to the 'AuthService' class and Autowired the 'JwtProvider class' and
     generate the token using the instance of this class.then we will retrieve the principal from the authenticated object and
     cast it to the building spring security user class.after that we are going to invoke the builder method of the "Jwts" class and
      passing in the subject which would part of JWT,then we'll provide the key which our JSON web token will be signed( in our case
      all the JWT will be digitally signed at the time of creation)
      */
    private KeyStore keyStore;
    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceAsStream, "secret".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new SpringBlogException("Exception occured while loading keystore");
        }

    }


        public String generateToken(Authentication authentication) {
            Bloger principal = (Bloger) authentication.getPrincipal();
            return Jwts.builder()
                    .setSubject(principal.getUserName())


                /*what this method do is that it will create a key where we can sign our JSON web tokens and for the key
 we have to instruct to use a special algorithm called 'signatureAlgorithm.HS512',this is basically
 hashing algorithm w/h will be used to sign our JSON web token,what this will do is it will create a secret w/h
 uses HS512 signature algorithm ,and we will use this algorithm to create the key,once the key is created,it will be
 used to digitally sign our existing web token.
 One thing we need to note is that we are creating a key, every time we are generating a token.But this is not correct.B/c
 what should ideal do is that create a key only once and reuse this key to sign the JWT every time we're creating a new
 one,so to do that we will move the creation of this key to a 'postConstruct' block so in this way we create the key at the time
  of server startup ,and we reuse the same key every time we generate a JSON web token. so instead of saying ".signwith(Keys.secretKeyFor(SignatureAlgorithm.HS512)
  we defined "key =Keys.secretKeyFor(SignatureAlgorithm.HS512) and passed the key instance to the method .signWith()".
  now we have to return this token to the authentication service(w/h is AuthService) and from there return it back to the controller(w/h is AuthController)
  w/h will then send it back to the client.
  Note: one important thing we need to know is  that when use the 'signatureAlgorithm.HS512' and passed the key to the method '.signwith(key)'
   every time we start application, our key will be changed,then the validation will fail and to avoid this,we must use
   'Asymmetric Encryption using Java Keystore' mechanism to sign our JSON web token, w/h means we can use java key store
   w/h contains a public key and a private key.so when we are generating the token we will use the private key to sign the token and the public key to validate the token.
   So basically the advantage of using keystore is,once we create our key store,we can store it in our resources' folder on the disk and we can read that
   public key or private key whenever we need so in this way we can avoid all these inconsistencies with the key.

   To create a keystore : on Angular Terminal inside our frontend folder which is 'blog-frontend' in our case type the following commands:
    "keytool -genkey -alias springblog -keyalg RSA -keystore springblog.jks -keysize 2048" and press enter
    then enter keystore password and re-enter password,then provide your first and last name,then organization unit (it can be any thing.
    the name is 'springblog and the password = secret')
   */
              .signWith(getPrivateKey())
                .compact();

    }

        private PrivateKey getPrivateKey() {
            try {
                return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
            } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
                throw new SpringBlogException("Exception occurred while retrieving public key from keystore");
            }
        }

            /*Here,we'll have validateToken method w/h takes string parameter*/
            public boolean validateToken(String jwt) {
//                Jwts.parserBuilder().setSigningKey(getPublickey()).parseClaimsJws(jwt);
                Jwts.parserBuilder()
                        // set signing keys, and other props
                        .build();
                return true;
            }

            private PublicKey getPublickey() {
                try {
                    return keyStore.getCertificate("springblog").getPublicKey();
                } catch (KeyStoreException e) {
                    throw new SpringBlogException("Exception occured while retrieving public key from keystore");
                }
            }

            public String getUsernameFromJWT(String token) {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(getPublickey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                return claims.getSubject();
            }
    }

