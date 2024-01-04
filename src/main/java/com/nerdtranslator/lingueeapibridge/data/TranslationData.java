package com.nerdtranslator.lingueeapibridge.data;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TranslationData {
    private String translation;
    private byte[] audioData;
    private String partOfSpeech;
}
