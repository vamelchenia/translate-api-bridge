package com.nerdtranslator.lingueeapibridge.service;

public interface TextToSpeechService {
    byte[] transformTextToSoundUsingApi(String textToTransfer, String langCode);
}
