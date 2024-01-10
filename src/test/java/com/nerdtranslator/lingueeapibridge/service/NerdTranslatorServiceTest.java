package com.nerdtranslator.lingueeapibridge.service;

import com.nerdtranslator.lingueeapibridge.data.TranslationData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class NerdTranslatorServiceTest {


    private final NerdTranslatorService nerdTranslatorService;
    private static final String wordToTranslate = "witchcraft";
    private static final String translationToExpect = "колдовство";
    private static final String NOUN_STR = "NOUN";

    @Autowired
    NerdTranslatorServiceTest(NerdTranslatorService nerdTranslatorService) {
        this.nerdTranslatorService = nerdTranslatorService;
    }

    @Test
    void oneEnglishWordShouldEqualToAnotherRussianWord() {
        TranslationData actualTranslationData = nerdTranslatorService.getTranslation(wordToTranslate, "en", "ru");
        assertEquals(translationToExpect, actualTranslationData.getTranslation());
        assertEquals(NOUN_STR, actualTranslationData.getPartOfSpeech());
    }

    @Test
    void oneEnglishWordShouldEqualToHieroglyphicTraditional() {
        TranslationData actualTranslationData =
                nerdTranslatorService.getTranslation(wordToTranslate, "en", "zh-TW");
        assertEquals("巫術", actualTranslationData.getTranslation());
        assertEquals(NOUN_STR, actualTranslationData.getPartOfSpeech());
    }

    @Test
    void oneEnglishWordShouldEqualToHieroglyphicSimplified() {
        TranslationData actualTranslationData =
                nerdTranslatorService.getTranslation(wordToTranslate, "en", "zh-CN");
        assertEquals("巫术", actualTranslationData.getTranslation());
        assertEquals(NOUN_STR, actualTranslationData.getPartOfSpeech());
    }

    @Test
    void severalEnglishEqualToRussianWordsCase() {
        TranslationData actualTranslationData =
                nerdTranslatorService.getTranslation("playing cat", "en", "ru");
        assertEquals("играющий кот", actualTranslationData.getTranslation());
    }

    @Test
    void sentenceTestCase() {
        String textToTest = "Большинство медведей обитает " +
                "в равнинных или горных лесах умеренных " +
                "и тропических широт, реже — на безлесных высокогорьях.";
        String textToExpect = "Most bears live in lowland or mountain forests " +
                "of temperate and tropical latitudes, less often in treeless highlands.";
        TranslationData actualTranslationData =
                nerdTranslatorService.getTranslation(textToTest, "ru", "en");
        assertEquals(textToExpect, actualTranslationData.getTranslation());
    }

    @Test
    void mainMeaningOfMultipleMeaningWord() {
        TranslationData actualTranslationData =
                nerdTranslatorService.getTranslation("book", "en", "fr");
        assertEquals("livre", actualTranslationData.getTranslation());
    }

    @Test
    void longTextCase() {
        var textToTranslate = "Cats, with their playful charm and rapid growth, " +
                "capture our hearts. Their curious nature, developmental milestones, " +
                "and joyful antics bring a sense of wonder to our lives. " +
                "These tiny feline companions teach us about responsibility and empathy while " +
                "forming a unique bond that makes them treasured members of families worldwide. " +
                "Whether it's their fluffy coats, mischievous eyes, or the way they pounce on their " +
                "toys, kittens exude an irresistible charm.";

        var textToExpect = "Кошки своим игривым очарованием и быстрым ростом покоряют наши сердца. " +
                "Их любопытная натура, этапы развития и радостные выходки привносят в нашу жизнь ощущение чуда. " +
                "Эти крошечные кошачьи компаньоны учат нас ответственности и сочувствию, образуя уникальную связь, " +
                "которая делает их ценными членами семей по всему миру. Котята излучают непреодолимое очарование, " +
                "будь то их пушистая шерсть, озорные глаза или то, как они набрасываются на свои игрушки.";

        TranslationData actualTranslationData =
                nerdTranslatorService.getTranslation(textToTranslate, "en", "ru");
        assertEquals(textToExpect, actualTranslationData.getTranslation());
    }

    @Test
    void articlePlusWord1Case() {
        TranslationData actualTranslationData =
                nerdTranslatorService.getTranslation("a letter", "en", "ru");
        assertEquals("письмо", actualTranslationData.getTranslation());
    }
    @Test
    void articlePlusWord2Case() {
        TranslationData actualTranslationData =
                nerdTranslatorService.getTranslation("le livre", "fr", "en");
        assertEquals("the book", actualTranslationData.getTranslation());
        assertEquals(NOUN_STR, actualTranslationData.getPartOfSpeech());
    }


}