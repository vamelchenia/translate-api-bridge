package com.nerdtranslator.translateapibridge.controller;

import com.nerdtranslator.translateapibridge.data.RestResponse;
import com.nerdtranslator.translateapibridge.data.TranslationData;
import com.nerdtranslator.translateapibridge.service.LanguagesService;
import com.nerdtranslator.translateapibridge.service.NerdTranslatorService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequestMapping("/translation")
@RequiredArgsConstructor
@Tag(
        name = "Translation data",
        description = "All received data from APIs"
)
public class NerdTranslatorController {
    private final NerdTranslatorService nerdTranslatorService;
    private final LanguagesService languagesService;

    @PostMapping("/{originalLanguage}/{targetLanguage}")
    @Operation(summary = "Get translation")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(value = "Your text to translate")))
    public RestResponse getTranslation(
            @Parameter(description = "Original text language code from supported languages map", required = true)
                @PathVariable String originalLanguage,
            @Parameter(description = "Target text language code from supported languages map", required = true)
                @PathVariable String targetLanguage,
            @RequestBody String originalText) {
        TranslationData response = nerdTranslatorService.getTranslation(originalText, originalLanguage, targetLanguage);
        return new RestResponse(HttpStatus.OK, response);
    }

    @GetMapping("/languages")
    @Operation(summary = "Get map of all supported languages with their codes")
    public RestResponse getAllSupportedLanguages() {
        Map<String, String> supportedLanguages = languagesService.getSupportedLanguages();
        return new RestResponse(HttpStatus.OK, supportedLanguages);
    }
}
