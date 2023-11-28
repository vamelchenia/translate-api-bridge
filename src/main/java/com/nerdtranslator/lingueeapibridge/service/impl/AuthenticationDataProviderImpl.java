package com.nerdtranslator.lingueeapibridge.service.impl;

import com.nerdtranslator.lingueeapibridge.service.AuthenticationDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:sensitive.properties")
public class AuthenticationDataProviderImpl implements AuthenticationDataProvider {

    private final Environment env;
    private static final String JSON_PAIR_PATTERN = "\"%s\": \"%s\",";
    private static final String LAST_PAIR_PATTERN = "\"%s\": \"%s\"";

    @Autowired
    public AuthenticationDataProviderImpl(Environment env) {
        this.env = env;
    }

    @Override
    public byte[] getAuthenticationData() {
        StringBuilder credentialsBuilder = new StringBuilder();
        credentialsBuilder.append("{");
        String currPair = String.format(JSON_PAIR_PATTERN, "type", env.getProperty("SPEECH_TYPE"));
        credentialsBuilder.append(currPair);
        currPair = String.format(JSON_PAIR_PATTERN, "project_id", env.getProperty("SPEECH_PROJECT_ID"));
        credentialsBuilder.append(currPair);
        currPair = String.format(JSON_PAIR_PATTERN, "private_key_id", env.getProperty("SPEECH_PRIVATE_KEY_ID"));
        credentialsBuilder.append(currPair);
        currPair = String.format(JSON_PAIR_PATTERN, "private_key", env.getProperty("SPEECH_PRIVATE_KEY"));
        credentialsBuilder.append(currPair);
        currPair = String.format(JSON_PAIR_PATTERN, "client_email", env.getProperty("SPEECH_CLIENT_EMAIL"));
        credentialsBuilder.append(currPair);
        currPair = String.format(JSON_PAIR_PATTERN, "client_id", env.getProperty("SPEECH_CLIENT_ID"));
        credentialsBuilder.append(currPair);
        currPair = String.format(JSON_PAIR_PATTERN, "auth_uri", env.getProperty("SPEECH_AUTH_URI"));
        credentialsBuilder.append(currPair);
        currPair = String.format(JSON_PAIR_PATTERN, "token_uri", env.getProperty("SPEECH_TOKEN_URI"));
        credentialsBuilder.append(currPair);
        currPair = String.format(JSON_PAIR_PATTERN, "auth_provider_x509_cert_url", env.getProperty("SPEECH_AUTH_PROVIDER_X509_CERT_URL"));
        credentialsBuilder.append(currPair);
        currPair = String.format(JSON_PAIR_PATTERN, "client_x509_cert_url", env.getProperty("SPEECH_CLIENT_X509_CERT_URL"));
        credentialsBuilder.append(currPair);
        currPair = String.format(LAST_PAIR_PATTERN, "universe_domain", env.getProperty("SPEECH_UNIVERSE_DOMAIN"));
        credentialsBuilder.append(currPair);
        credentialsBuilder.append("}");
        return credentialsBuilder.toString().getBytes();
    }
}
