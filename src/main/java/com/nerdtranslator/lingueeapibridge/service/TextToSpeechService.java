package com.nerdtranslator.lingueeapibridge.service;

public interface TextToSpeechService {
    byte[] transformTextToSound(String textToTransfer, String langCode);
}
