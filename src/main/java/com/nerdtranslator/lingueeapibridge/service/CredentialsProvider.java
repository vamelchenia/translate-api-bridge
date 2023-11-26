package com.nerdtranslator.lingueeapibridge.service;

import org.springframework.stereotype.Component;

public interface CredentialsProvider {
    byte[] getAuthorizationData();
}
