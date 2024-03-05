Sample project using Oauth2 Resource Server with separate Authorization Server.
It uses private-public key pair with RS256 algorithm.
Access Tokens are provided by application itself by custom filter.

You can use `_requests/requests.http` to test the application

To generate private-public key pair use commands:
```bash
#generate private-public key pair
ssh-keygen -t rsa -b 4096 -m PKCS8 -f rs256.key
#Java requires x509 format, so convert the public key
ssh-keygen -m PKCS8 -f rs256.key.pub -e > rs256.key.pub.pem
#or
openssl rsa -in rs256.key -pubout -outform PEM -out rs256.key.pub
```

```
-e     This option will read a private or public OpenSSH key
       file and print to stdout a public key in one of the
       formats specified by the -m option.
-m key_format
       Specify a key format for key generation, the -i (import),
       -e (export) conversion options, and the -p change
       passphrase operation.  The latter may be used to convert
       between OpenSSH private key and PEM private key formats.
       The supported key formats are: “RFC4716” (RFC 4716/SSH2
       public or private key), “PKCS8” (PKCS8 public or private
       key) or “PEM” (PEM public key).  By default OpenSSH will
       write newly-generated private keys in its own format, but
       when converting public keys for export the default format
       is “RFC4716”.  Setting a format of “PEM” when generating
       or updating a supported private key type will cause the
       key to be stored in the legacy PEM private key format.
-t dsa | ecdsa | ecdsa-sk | ed25519 | ed25519-sk | rsa
       Specifies the type of key to create.  The possible values
       are “dsa”, “ecdsa”, “ecdsa-sk”, “ed25519”, “ed25519-sk”,
       or “rsa”.
```

Because we use single key pair, we can define public key through `public-key-location` property in application.yml file.


_OAuth2ResourceServerJwtConfiguration.java_
```java
@Bean
@Conditional({KeyValueCondition.class})
JwtDecoder jwtDecoderByPublicKeyValue() throws Exception {
    RSAPublicKey publicKey = (RSAPublicKey)KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(this.getKeySpec(this.properties.readPublicKey())));
    NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(publicKey).signatureAlgorithm(SignatureAlgorithm.from(this.exactlyOneAlgorithm())).build();
    jwtDecoder.setJwtValidator(this.getValidators(JwtValidators.createDefault()));
    return jwtDecoder;
}
```

Resource Server Docs:
https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html

Authorization Server Docs:
https://spring.io/projects/spring-authorization-server