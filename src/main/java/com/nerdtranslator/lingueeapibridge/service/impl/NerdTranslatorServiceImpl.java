package com.nerdtranslator.lingueeapibridge.service.impl;

import com.nerdtranslator.lingueeapibridge.data.TranslationData;
import com.nerdtranslator.lingueeapibridge.service.TextToSpeechService;
import com.nerdtranslator.lingueeapibridge.service.TranslationApiService;
import com.nerdtranslator.lingueeapibridge.service.NerdTranslatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NerdTranslatorServiceImpl implements NerdTranslatorService {
    private final TranslationApiService translationApiService;
    private final TextToSpeechService textToSpeechService;
    //private final NaturalLanguageApiService naturalLanguageApiService;

    @Override
    public TranslationData getTranslation(String originalText, String originalLanguage, String targetLanguage) {
        TranslationData translationData = new TranslationData();
        int wordsCount = countWords(originalText);
        if (wordsCount == 0) {
            throw new RuntimeException("wrong input");
        } else {
            String singleTranslationFromApi
                = translationApiService.getSingleTranslationFromApi(originalText, originalLanguage, targetLanguage);
            translationData.setTranslation(singleTranslationFromApi);
            int countTranslationWords = countWords(singleTranslationFromApi);
            if (countTranslationWords < 6) {
                byte[] audio = textToSpeechService.transformTextToSound(singleTranslationFromApi, originalLanguage);
                translationData.setAudioData(audio);
                /*if (countTranslationWords == 1) {
                    String partOfSpeech = naturalLanguageApiService.analyzeText(singleTranslationFromApi);
                    translationData.setPartOfSpeech(partOfSpeech);
                }*/
            }
        }
        return translationData;
    }

    public static int countWords(String str) {
        if (str == null || str.isEmpty()) {
            return 0;
        }
        String[] words = str.split("\\s+");
        return words.length;
    }
}

