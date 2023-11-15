package com.nerdtranslator.lingueeapibridge.controller;

import com.nerdtranslator.lingueeapibridge.service.NerdTranslatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/translation")
@RequiredArgsConstructor
public class NerdTranslatorController {
    private final NerdTranslatorService nerdTranslatorService;

    @GetMapping("/{originalLanguage}/{targetLanguage}")
    public RestResponse getTranslation(@PathVariable String originalLanguage,
                                       @PathVariable String targetLanguage,
                                       @RequestBody String originalText) {
        String response = nerdTranslatorService.getTranslation(originalText, originalLanguage, targetLanguage);
        return new RestResponse(response);
    }
}
