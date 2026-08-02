package com.mukha.authservice.service;

import com.mukha.authservice.dto.request.SignUpRequest;
import com.mukha.authservice.exception.RegistrationException;
import com.mukha.authservice.exception.UserAlreadyExistsException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakUserService {
    private final Keycloak keycloakAdminClient;
    @Value("${keycloak.realm}")
    private String realm;

    public String createUser(SignUpRequest request) {
        UserRepresentation kcUser = toKeycloakUser(request);
        try (Response response = keycloakAdminClient.realm(realm).users().create(kcUser)) {
            if (response.getStatus() == HttpStatus.CONFLICT.value()) {
                throw new UserAlreadyExistsException(request.login());
            }
            if (response.getStatus() != HttpStatus.CREATED.value()) {
                throw new RegistrationException();
            }
            String kcUserId = CreatedResponseUtil.getCreatedId(response);
            assignDefaultRole(kcUserId);
            return kcUserId;
        }
    }


    public void deleteUser(String kcUserId) {
        try {
            keycloakAdminClient.realm(realm).users().get(kcUserId).remove();
        } catch (Exception e) {
            log.error("Failed to delete Keycloak user {}", kcUserId, e);
        }
    }

    private void assignDefaultRole(String kcUserId) {
        RoleRepresentation role = keycloakAdminClient.realm(realm).roles()
                .get("user").toRepresentation();
        keycloakAdminClient.realm(realm).users().get(kcUserId)
                .roles().realmLevel().add(List.of(role));
    }

    private UserRepresentation toKeycloakUser(SignUpRequest request) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.login());
        user.setEnabled(true);
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(request.password());
        cred.setTemporary(false);
        user.setCredentials(List.of(cred));
        return user;
    }
}