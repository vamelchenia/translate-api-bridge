package com.nerdtranslator.lingueeapibridge.service;

public interface TextToSpeechApiService {
    byte[] transformTextToSound(String textToTransfer, String langCode);
}
