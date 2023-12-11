package com.nerdtranslator.lingueeapibridge.controller;

import com.nerdtranslator.lingueeapibridge.data.RestResponse;
import com.nerdtranslator.lingueeapibridge.data.TranslationData;
import com.nerdtranslator.lingueeapibridge.service.LanguagesService;
import com.nerdtranslator.lingueeapibridge.service.NerdTranslatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/translation")
@RequiredArgsConstructor
public class NerdTranslatorController {
    private final NerdTranslatorService nerdTranslatorService;
    private final LanguagesService languagesService;

    @GetMapping("/{originalLanguage}/{targetLanguage}")
    public RestResponse getTranslation(@PathVariable String originalLanguage,
                                       @PathVariable String targetLanguage,
                                       @RequestBody String originalText) {
        TranslationData response = nerdTranslatorService.getTranslation(originalText, originalLanguage, targetLanguage);
        return new RestResponse(response);
    }

    @GetMapping("/languages")
    public RestResponse getAllSupportedLanguages() {
        Map<String, String> supportedLanguages = languagesService.getSupportedLanguages();
        return new RestResponse(supportedLanguages);
    }
}
