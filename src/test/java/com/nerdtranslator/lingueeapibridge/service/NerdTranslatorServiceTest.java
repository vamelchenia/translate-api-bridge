package com.nerdtranslator.lingueeapibridge.service;

import com.nerdtranslator.lingueeapibridge.data.TranslationData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
class NerdTranslatorServiceTest {

    @Autowired
    private NerdTranslatorService nerdTranslatorService;
    private static final String wordToTranslate = "witchcraft";
    private static final String translationToExpect = "колдовство";

    @Test
    void oneEnglishWordShouldEqualToAnotherRussianWord() {
        log.info("start method");
        TranslationData actualTranslationData = nerdTranslatorService.getTranslation(wordToTranslate, "en", "ru");
        System.out.println("actual translation data is " + actualTranslationData.getTranslation());
        assertEquals(translationToExpect, actualTranslationData.getTranslation());
        log.info("end method");
    }

    @Test
    void test() {
    }
}