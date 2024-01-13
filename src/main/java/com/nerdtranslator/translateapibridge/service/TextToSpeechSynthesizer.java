package com.nerdtranslator.translateapibridge.service;

import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;

public interface TextToSpeechSynthesizer {
    SynthesizeSpeechResponse synthesize(String text, AudioEncoding encoding, String langCode);
    void setTextToSpeechClient(TextToSpeechClient textToSpeechClient);
    void setGender(SsmlVoiceGender gender);
}
