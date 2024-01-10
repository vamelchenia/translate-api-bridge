package com.nerdtranslator.translateapibridge.service;

public interface TextToSpeechApiService {
    byte[] transformTextToSound(String textToTransfer, String langCode);
}
