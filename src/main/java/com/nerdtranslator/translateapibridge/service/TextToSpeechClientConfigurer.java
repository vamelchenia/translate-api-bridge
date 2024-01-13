package com.nerdtranslator.translateapibridge.service;

import com.google.cloud.texttospeech.v1.TextToSpeechClient;

public interface TextToSpeechClientConfigurer {
    TextToSpeechClient configure();
}
