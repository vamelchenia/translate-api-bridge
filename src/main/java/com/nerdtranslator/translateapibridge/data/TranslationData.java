package com.nerdtranslator.translateapibridge.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TranslationData {
    private String translation;
    private byte[] audioData;
    private String partOfSpeech;
}