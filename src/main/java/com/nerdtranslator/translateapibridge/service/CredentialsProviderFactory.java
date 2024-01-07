package com.nerdtranslator.translateapibridge.service;

import com.google.api.gax.core.CredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Component
@PropertySource("classpath:sensitive.properties")
@RequiredArgsConstructor
public class CredentialsProviderFactory {

    private final Environment env;
    private static final String JSON_PAIR_PATTERN = "\"%s\": \"%s\",";
    private static final String LAST_PAIR_PATTERN = "\"%s\": \"%s\"";

    public CredentialsProvider getCredentialsProvider() {
        return () -> {
            try (ByteArrayInputStream keyStream =
                         new ByteArrayInputStream(getAuthenticationData())) {
                return ServiceAccountCredentials.fromStream(keyStream);
            }
        };
    }

    private byte[] getAuthenticationData() {
        StringBuilder credentialsBuilder = new StringBuilder();
        credentialsBuilder.append("{");
        String currPair = String.format(JSON_PAIR_PATTERN, "type", env.getProperty("TYPE"));
        credentialsBuilder.append(currPair);
        currPair = String.format(JSON_PAIR_PATTERN, "project_id", env.getProperty("PROJECT_ID"));
        credentialsBuilder.append(currPair);
        currPair = String.format(JSON_PAIR_PATTERN, "private_key_id", env.getProperty("PRIVATE_KEY_ID"));
        credentialsBuilder.append(currPair);
        currPair = String.format(JSON_PAIR_PATTERN, "private_key", env.getProperty("PRIVATE_KEY"));
        credentialsBuilder.append(currPair);
        currPair = String.format(JSON_PAIR_PATTERN, "client_email", env.getProperty("CLIENT_EMAIL"));
        credentialsBuilder.append(currPair);
        currPair = String.format(JSON_PAIR_PATTERN, "client_id", env.getProperty("CLIENT_ID"));
        credentialsBuilder.append(currPair);
        currPair = String.format(JSON_PAIR_PATTERN, "auth_uri", env.getProperty("AUTH_URI"));
        credentialsBuilder.append(currPair);
        currPair = String.format(JSON_PAIR_PATTERN, "token_uri", env.getProperty("TOKEN_URI"));
        credentialsBuilder.append(currPair);
        currPair = String.format(JSON_PAIR_PATTERN, "auth_provider_x509_cert_url", env.getProperty("AUTH_PROVIDER_X509_CERT_URL"));
        credentialsBuilder.append(currPair);
        currPair = String.format(JSON_PAIR_PATTERN, "client_x509_cert_url", env.getProperty("CLIENT_X509_CERT_URL"));
        credentialsBuilder.append(currPair);
        currPair = String.format(LAST_PAIR_PATTERN, "universe_domain", env.getProperty("UNIVERSE_DOMAIN"));
        credentialsBuilder.append(currPair);
        credentialsBuilder.append("}");
        return credentialsBuilder.toString().getBytes();
    }
}
