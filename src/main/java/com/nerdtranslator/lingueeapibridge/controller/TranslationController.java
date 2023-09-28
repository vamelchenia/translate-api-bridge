package com.nerdtranslator.lingueeapibridge.controller;

import com.nerdtranslator.lingueeapibridge.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/translation")
@RequiredArgsConstructor
public class TranslationController {
    private final TranslationService translationService;

    @GetMapping("/{from}/{to}")
    public RestResponse getTranslation(@PathVariable String from,
                                       @PathVariable String to,
                                       @RequestBody String originalText) {
        String translation = translationService.getTranslation(originalText, from, to);
        return new RestResponse(translation);
    }
}
