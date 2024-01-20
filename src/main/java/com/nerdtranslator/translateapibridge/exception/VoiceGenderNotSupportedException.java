package com.nerdtranslator.translateapibridge.exception;

import lombok.Getter;

@Getter
public class VoiceGenderNotSupportedException extends RuntimeException {

    public VoiceGenderNotSupportedException(String message) {
        super(message);
    }
}
