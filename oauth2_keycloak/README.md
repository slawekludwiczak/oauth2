Sample project using Oauth2 Stack:
- KeyCloak as Authorization Server
- OAuth2 Client (frontend)
- OAuth2 Resource Server (api)

Add to your hosts file:
```
127.0.0.1	keycloak
```
When client and keycloak are running on same host, there is a problem with cookie overriding.

It is important to use localhost or 127.0.0.1 across keycloak configuration and Spring config files for client.

To export realm configured in keycloak:
```bash
docker exec keycloak sh /opt/keycloak/bin/kc.sh export --realm spring-realm --file /opt/keycloak/data/spring-realm.json
not needed when mounted
docker cp keycloak:/opt/keycloak/data/spring-realm.json ./spring-realm.json
```

To run:
```
mvn package jib:dockerBuild
```

Create KeyCloak realm and users:
https://www.keycloak.org/getting-started/getting-started-docker

KeyCloak Account Console:
http://auth-server:8080/realms/your-realm-name/account

Resource Server Docs:
https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html

OAuth2 Client Docs:
https://docs.spring.io/spring-security/reference/reactive/oauth2/client/index.html