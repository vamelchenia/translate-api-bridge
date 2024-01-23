package com.nerdtranslator.translateapibridge.controller;

import com.nerdtranslator.translateapibridge.data.RequestData;
import com.nerdtranslator.translateapibridge.data.RestResponse;
import com.nerdtranslator.translateapibridge.data.TranslationData;
import com.nerdtranslator.translateapibridge.util.PartOfSpeech;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class NerdTranslatorControllerTest {
    private final NerdTranslatorController controller;

    @Test
    void one_english_word_should_equal_to_another_russian_word_case_IT() {
        makeAssertion("witchcraft", "en", "ru", "колдовство", PartOfSpeech.NOUN);
    }

    @Test
    void one_english_word_should_equal_to_hieroglyphic_traditional_case_IT() {
        makeAssertion("witchcraft", "en",
                "zh-TW", "巫術", PartOfSpeech.NOUN);
    }

    @Test
    void one_english_word_should_equal_to_hieroglyphic_simplified_case_IT() {
        makeAssertion("witchcraft", "en",
                "zh-CN", "巫术", PartOfSpeech.NOUN);
    }

    @Test
    void several_english_equal_to_russian_words_case_IT() {
        makeAssertion("playing cat", "en",
                "ru", "играющий кот");
    }

    @Test
    void sentence_test_case_IT() {
        String originalText = "Большинство медведей обитает " +
                "в равнинных или горных лесах умеренных " +
                "и тропических широт, реже — на безлесных высокогорьях.";
        String expectedResult = "Most bears live in lowland or mountain forests " +
                "of temperate and tropical latitudes, less often in treeless highlands.";
        makeAssertion(originalText, "ru", "en", expectedResult);
    }

    @Test
    void main_meaning_of_multiple_meaning_word_case_IT() {
        //bug
        makeAssertion("book", "en",
                "fr", "livre", PartOfSpeech.NOUN);
    }

    @Test
    void long_text_case_IT() {
        var originalText = "Cats, with their playful charm and rapid growth, " +
                "capture our hearts. Their curious nature, developmental milestones, " +
                "and joyful antics bring a sense of wonder to our lives. " +
                "These tiny feline companions teach us about responsibility and empathy while " +
                "forming a unique bond that makes them treasured members of families worldwide. " +
                "Whether it's their fluffy coats, mischievous eyes, or the way they pounce on their " +
                "toys, kittens exude an irresistible charm.";

        var expectedResult = "Кошки своим игривым очарованием и быстрым ростом покоряют наши сердца. " +
                "Их любопытная натура, этапы развития и радостные выходки привносят в нашу жизнь ощущение чуда. " +
                "Эти крошечные кошачьи компаньоны учат нас ответственности и сочувствию, образуя уникальную связь, " +
                "которая делает их ценными членами семей по всему миру. Котята излучают непреодолимое очарование, " +
                "будь то их пушистая шерсть, озорные глаза или то, как они набрасываются на свои игрушки.";
        makeAssertion(originalText, "en", "ru", expectedResult);
    }

    @Test
    void article_plus_word_1_case_IT() {
        makeAssertion("a letter", "en",
                "ru", "письмо");
    }

    @Test
    void article_plus_word_2_case_IT() {
        makeAssertion("le livre", "fr",
                "en", "the book", PartOfSpeech.NOUN);
    }

    @Test
    void preposition_plus_word_case_IT() {
        makeAssertion("at school", "en",
                "ru", "в школе");
    }

    @Test
    void compound_noun_non_hyphenated_case_1_IT() {
        makeAssertionWithPossibleValues("водопровод", "ru", "en",
                List.of("water pipes", "plumbing"));
    }

    @Test
    void compound_noun_non_hyphenated_case_2_IT() {
        makeAssertion("lighthouse", "en",
                "ru", "маяк", PartOfSpeech.NOUN);
    }

    @Test
    void compound_noun_hyphenated_case_1_IT() {
        makeAssertion("chou-fleur", "fr",
                "en", "cauliflower", PartOfSpeech.NOUN);
    }

    @Test
    void compound_noun_hyphenated_case_2_IT() {
        RequestData requestData = new RequestData("editor-in-chief");
        RestResponse restResponse = controller.getTranslation("en", "ru", requestData);
        if (restResponse.data() instanceof TranslationData actualTranslationData) {
            Assertions.assertThat(actualTranslationData.getTranslation())
                    .isEqualToIgnoringCase("главный редактор");
        }
    }

    @Test
    void compound_noun_hyphenated_case_3_IT() {
        makeAssertion("кресло-качалка", "ru", "en", "rocking chair");
    }

    @Test
    void fixed_expression_case_IT() {
        makeAssertion("на днях", "ru",
                "en", "the other day");
    }

    @Test
    void acronym_case_IT() {
        makeAssertion("ADN", "fr", "en", "DNA");
    }

    @Test
    void new_mixed_words_case_IT() {
        makeAssertion("staycation", "en",
                "ru", "отдых", PartOfSpeech.NOUN);
    }

    @Test
    void proverbs_case_IT() {
        makeAssertion("Strike while the iron is hot", "en",
                "ru", "Куй железо пока горячо");
    }

    @Test
    void one_word_in_target_lang_equals_to_multiple_words_case_IT() {
        makeAssertion("sibling", "en", "ru", "брат или сестра");
    }

    @Test
    void multiple_meanings_case_1_IT() {
        makeAssertionWithPossibleValues("book", "en", "ru",
                List.of("книга", "резервировать", "заказывать"));
    }

    @Test
    void multiple_meanings_case_2_IT() {
        makeAssertionWithPossibleValues("bark", "en", "ru",
                List.of("лаять", "кора дерева"));
    }

    @Test
    void multiple_meanings_case_3_IT() {
        makeAssertionWithPossibleValues("bon", "fr", "ru",
                List.of("хороший", "талон"));
    }

    @Test
    void multiple_meanings_case_4_IT() {
        RequestData requestData = new RequestData("красивый");
        RestResponse restResponse = controller.getTranslation("ru", "en", requestData);
        if (restResponse.data() instanceof TranslationData actualTranslationData) {
            List<String> possibleValues = List.of("beautiful", "handsome");
            Assertions.assertThat(actualTranslationData.getTranslation().toLowerCase()).isIn(possibleValues);
            assertEquals(PartOfSpeech.ADJ.name(), actualTranslationData.getPartOfSpeech());
        }
    }

    private void makeAssertion(String originalText, String originalLanguage, String targetLanguage,
                               String expectedResult) {
        RequestData requestData = new RequestData(originalText);
        RestResponse restResponse = controller.getTranslation(originalLanguage, targetLanguage, requestData);
        if (restResponse.data() instanceof TranslationData actualTranslationData) {
            assertEquals(expectedResult, actualTranslationData.getTranslation());
        }
    }

    private void makeAssertion(String originalText, String originalLanguage, String targetLanguage,
                               String expectedResult, PartOfSpeech partOfSpeech) {
        RequestData requestData = new RequestData(originalText);
        RestResponse restResponse = controller.getTranslation(originalLanguage, targetLanguage, requestData);
        if (restResponse.data() instanceof TranslationData actualTranslationData) {
            assertEquals(expectedResult, actualTranslationData.getTranslation());
            assertEquals(partOfSpeech.name(), actualTranslationData.getPartOfSpeech());
        }
    }

    private void makeAssertionWithPossibleValues(String originalText, String originalLanguage, String targetLanguage,
                                                 List<String> possibleValues) {
        RequestData requestData = new RequestData(originalText);
        RestResponse restResponse = controller.getTranslation(originalLanguage, targetLanguage, requestData);
        if (restResponse.data() instanceof TranslationData actualTranslationData) {
            Assertions.assertThat(actualTranslationData.getTranslation().toLowerCase()).isIn(possibleValues);
        }
    }
}