package com.nerdtranslator.translateapibridge.service.impl;

import com.nerdtranslator.translateapibridge.data.TranslationData;
import com.nerdtranslator.translateapibridge.service.NaturalLangApiService;
import com.nerdtranslator.translateapibridge.service.TextToSpeechApiService;
import com.nerdtranslator.translateapibridge.service.TranslationApiService;
import com.nerdtranslator.translateapibridge.service.NerdTranslatorService;
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
    private final static int MAX_NUMBER_TO_GET_AUDIO = 6;
    private final static int MAX_NUMBER_TO_GET_PART_OF_SPEECH = 2;
    private static final int MINIMUM_WORDS_REQUIRED = 1;
    private static final int MAX_PARTS_OF_SPEECH_TO_RETURN = 1;

    @Override
    public TranslationData getTranslation(String originalText, String originalLanguage, String targetLanguage) {
        TranslationData translationData = new TranslationData();
        List<String> originWords = getWordsList(originalText);
        int wordsCount = originWords.size();
        if (wordsCount < MINIMUM_WORDS_REQUIRED) {
            throw new RuntimeException("Wrong input");
        } else {
            String singleTranslationFromApi
                = translationApiService.getSingleTranslationFromApi(originalText, originalLanguage, targetLanguage);
            translationData.setTranslation(singleTranslationFromApi);

            List<String> translatedWords = getWordsList(singleTranslationFromApi);
            if (translatedWords.size() < MAX_NUMBER_TO_GET_AUDIO) {
                byte[] audio = textToSpeechService.transformTextToSound(singleTranslationFromApi, originalLanguage);
                translationData.setAudioData(audio);
            }

            String partOfSpeech = getPartOfSpeech(translatedWords);
            if (partOfSpeech == null
                    || "X".equals(partOfSpeech)
                    || "UNKNOWN".equals(partOfSpeech)
                    || "UNRECOGNIZED".equals(partOfSpeech)) {
                translationData.setPartOfSpeech("X");
            } else {
                translationData.setPartOfSpeech(partOfSpeech);
            }
        }
        return translationData;
    }

    private List<String> getWordsList(String str) {
        if (str == null || str.isEmpty()) {
            throw new RuntimeException("Translation is empty.");
        }
        return Arrays.stream(str.split("\\s+")).toList();
    }

    private String getPartOfSpeech(List<String> translatedWords) {
        String partOfSpeech = null;
        int translatedWordsSize = translatedWords.size();
        if (translatedWordsSize <= MAX_NUMBER_TO_GET_PART_OF_SPEECH) {
            if (translatedWordsSize == MINIMUM_WORDS_REQUIRED) {
                partOfSpeech = getForOneWord(translatedWords.get(0));
            } else {
                partOfSpeech = getForTwoWords(translatedWords);
            }
        }
        return partOfSpeech;
    }

    private String getForOneWord(String word) {
        if (isOnlyOneTypeOfSymbols(word)) {
            return naturalLanguageApiService.analyzeText(word);
        } else {
            String mainWord = word.substring(0, word.length() - 1);
            String lastSymbol = word.substring(word.length() - 1);
            if (isOnlyOneTypeOfSymbols(mainWord) && lastSymbol.matches("\\p{Punct}")) {
                return naturalLanguageApiService.analyzeText(mainWord);
            }
            return null;
        }
    }

    private String getForTwoWords(List<String> translatedWords) {
        List<String> partsOfSpeech = new ArrayList<>();
        for (String word : translatedWords) {
            String response = null;
            if(isOnlyOneTypeOfSymbols(word)) {
                response = naturalLanguageApiService.analyzeText(word);
            }
            if (!"DET".equals(response)) {
                partsOfSpeech.add(response);
            }
        }
        if (partsOfSpeech.size() == MAX_PARTS_OF_SPEECH_TO_RETURN) {
            return partsOfSpeech.get(0);
        }
        return null;
    }

    private boolean isOnlyOneTypeOfSymbols(String word) {
        return word.matches("\\p{L}+")
                || word.matches("\\p{N}+")
                || word.matches("[^\\p{L}\\p{N}]+");
    }
}