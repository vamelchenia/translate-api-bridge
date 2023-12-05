package com.nerdtranslator.lingueeapibridge.service.impl;

import com.nerdtranslator.lingueeapibridge.data.TranslationData;
import com.nerdtranslator.lingueeapibridge.service.NaturalLangApiService;
import com.nerdtranslator.lingueeapibridge.service.TextToSpeechApiService;
import com.nerdtranslator.lingueeapibridge.service.TranslationApiService;
import com.nerdtranslator.lingueeapibridge.service.NerdTranslatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NerdTranslatorServiceImpl implements NerdTranslatorService {
    private final TranslationApiService translationApiService;
    private final TextToSpeechApiService textToSpeechService;
    private final NaturalLangApiService naturalLanguageApiService;

    @Override
    public TranslationData getTranslation(String originalText, String originalLanguage, String targetLanguage) {
        TranslationData translationData = new TranslationData();
        List<String> originWords = getWordsList(originalText);
        int wordsCount = originWords.size();
        if (wordsCount == 0) {
            throw new RuntimeException("wrong input");
        } else {
            String singleTranslationFromApi
                = translationApiService.getSingleTranslationFromApi(originalText, originalLanguage, targetLanguage);
            translationData.setTranslation(singleTranslationFromApi);
            List<String> translatedWords = getWordsList(singleTranslationFromApi);
            int countTranslationWords = translatedWords.size();
            if (countTranslationWords < 6) {
                byte[] audio = textToSpeechService.transformTextToSound(singleTranslationFromApi, originalLanguage);
                translationData.setAudioData(audio);
                if (countTranslationWords <= 2) {
                    if (countTranslationWords == 1) {
                        String partOfSpeech = naturalLanguageApiService.analyzeText(singleTranslationFromApi);
                        translationData.setPartOfSpeech(partOfSpeech);
                    } else {
                        List<String> partsOfSpeech = new ArrayList<>();
                        for (String word : translatedWords) {
                            String partOfSpeech = naturalLanguageApiService.analyzeText(word);
                            if (!"PRETEXT".equals(partOfSpeech)) {
                                partsOfSpeech.add(partOfSpeech);
                            }
                        }
                        if (partsOfSpeech.size() == 1) {
                            translationData.setPartOfSpeech(partsOfSpeech.get(0));
                        }
                    }
                }
            }
        }
        return translationData;
    }

    public static List<String> getWordsList(String str) {
        if (str == null || str.isEmpty()) {
            throw new RuntimeException("Translation is empty.");
        }
        return Arrays.stream(str.split("\\s+")).toList();
    }
}

