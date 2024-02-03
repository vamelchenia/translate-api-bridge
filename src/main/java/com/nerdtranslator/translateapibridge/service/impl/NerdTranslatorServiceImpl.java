package com.nerdtranslator.translateapibridge.service.impl;

import com.nerdtranslator.translateapibridge.data.RequestData;
import com.nerdtranslator.translateapibridge.data.TranslationData;
import com.nerdtranslator.translateapibridge.exception.BadRequestException;
import com.nerdtranslator.translateapibridge.service.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final SupportedLanguagesService supportedLanguagesService;
    private static final Logger log = LoggerFactory.getLogger(NerdTranslatorServiceImpl.class);
    private static final int MAX_NUMBER_TO_GET_AUDIO = 6;
    private static final int MAX_NUMBER_TO_GET_PART_OF_SPEECH = 2;
    private static final int MINIMUM_WORDS_REQUIRED = 1;
    private static final int MAX_PARTS_OF_SPEECH_TO_RETURN = 1;

    @Override
    public TranslationData getTranslation(RequestData requestData, String originalLanguage, String targetLanguage) {
        log.info("NerdTranslatorServiceImpl getTranslation start");
        TranslationData translationData = new TranslationData();
        String originalText = requestData.getOriginalText();
        List<String> originWords = getWordsList(originalText);
        int wordsCount = originWords.size();
        if (wordsCount < MINIMUM_WORDS_REQUIRED) {
            log.error("An error occurred in NerdTranslatorServiceImpl: Bad request, minimum one word required");
            throw new BadRequestException("minimum one word required");
        } else {
            String singleTranslationFromApi
                = translationApiService.getSingleTranslationFromApi(originalText, originalLanguage, targetLanguage);
            translationData.setTranslation(singleTranslationFromApi);
            log.info("Translation for \"" + originalText + "\" from " + originalLanguage + " to "
                    + targetLanguage + " received: " + singleTranslationFromApi);

            List<String> translatedWords = getWordsList(singleTranslationFromApi);
            if (translatedWords.size() < MAX_NUMBER_TO_GET_AUDIO) {
                log.info("Receiving audio data");
                byte[] audio = textToSpeechService.transformTextToSound(singleTranslationFromApi, originalLanguage);
                translationData.setAudioData(audio);
                log.info("Audio data received");
            }

            String partOfSpeech = getPartOfSpeech(translatedWords, targetLanguage);
            if (partOfSpeech == null
                    || partOfSpeech.isEmpty()
                    || "X".equals(partOfSpeech)
                    || "UNKNOWN".equals(partOfSpeech)
                    || "UNRECOGNIZED".equals(partOfSpeech)) {
                translationData.setPartOfSpeech("X");
                log.info("Part of speech wasn't recognized or doesn't needed");
            } else {
                translationData.setPartOfSpeech(partOfSpeech);
                log.info("Part of speech received: " + partOfSpeech);
            }
        }
        log.info("NerdTranslatorServiceImpl getTranslation end");
        return translationData;
    }

    private List<String> getWordsList(String str) {
        return Arrays.stream(str.split("\\s+")).toList();
    }

    private String getPartOfSpeech(List<String> translatedWords, String textLanguage) {
        log.info("NerdTranslatorServiceImpl getPartOfSpeech start");
        textLanguage = filterTextLanguageCode(textLanguage);
        String partOfSpeech = null;
        List<String> supportedLanguages =
                supportedLanguagesService.getSupportedLanguagesForNaturalLanguageApi().values().stream().toList();
        int translatedWordsSize = translatedWords.size();
        if (translatedWordsSize <= MAX_NUMBER_TO_GET_PART_OF_SPEECH && supportedLanguages.contains(textLanguage)) {
            if (translatedWordsSize == MINIMUM_WORDS_REQUIRED) {
                partOfSpeech = getPartOfSpeechForOneWord(translatedWords.get(0), textLanguage);
            } else {
                partOfSpeech = getPartOfSpeechForTwoWords(translatedWords, textLanguage);
            }
        }
        log.info("NerdTranslatorServiceImpl getPartOfSpeech end");
        return partOfSpeech;
    }

    private String filterTextLanguageCode(String textLanguage) {
        if (textLanguage.contains("-")) {
            if (textLanguage.equals("zh-CN")) {
                textLanguage = "zh";
            }
            if (textLanguage.equals("zh-TW")) {
                textLanguage = "zh-Hant";
            }
        }
        return textLanguage;
    }

    private String getPartOfSpeechForOneWord(String word, String targetLanguage) {
        if (isOnlyOneTypeOfSymbols(word)) {
            return naturalLanguageApiService.analyzeText(word, targetLanguage);
        } else {
            String mainWord = word.substring(0, word.length() - 1);
            String lastSymbol = word.substring(word.length() - 1);
            if (isOnlyOneTypeOfSymbols(mainWord) && lastSymbol.matches("\\p{Punct}")) {
                return naturalLanguageApiService.analyzeText(mainWord, targetLanguage);
            }
            return null;
        }
    }

    private String getPartOfSpeechForTwoWords(List<String> translatedWords, String targetLanguage) {
        List<String> partsOfSpeech = new ArrayList<>();
        for (String word : translatedWords) {
            String response = null;
            if(isOnlyOneTypeOfSymbols(word)) {
                response = naturalLanguageApiService.analyzeText(word, targetLanguage);
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