package com.nerdtranslator.lingueeapibridge.controller;

import com.nerdtranslator.lingueeapibridge.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/translations")
@RequiredArgsConstructor
public class TranslationController {
    private final TranslationService translationService;

}
