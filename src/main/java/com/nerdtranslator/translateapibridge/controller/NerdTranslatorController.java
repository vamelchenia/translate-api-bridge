package com.nerdtranslator.translateapibridge.controller;

import com.nerdtranslator.translateapibridge.data.RequestData;
import com.nerdtranslator.translateapibridge.data.RestResponse;
import com.nerdtranslator.translateapibridge.data.TranslationData;
import com.nerdtranslator.translateapibridge.service.SupportedLanguagesService;
import com.nerdtranslator.translateapibridge.service.NerdTranslatorService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
    private final SupportedLanguagesService supportedLanguagesService;
    private static final Logger log = LoggerFactory.getLogger(NerdTranslatorController.class);

    @PostMapping("/{originalLanguage}/{targetLanguage}")
    @Operation(summary = "Get translation")
    public RestResponse getTranslation(
            @Parameter(description = "Original text language code from supported languages map", required = true)
                @PathVariable String originalLanguage,
            @Parameter(description = "Target text language code from supported languages map", required = true)
                @PathVariable String targetLanguage,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request body in JSON format",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RequestData.class),
                            examples = @ExampleObject(value = "{\"originalText\":\"Your text to translate\"}")))
            @RequestBody RequestData requestData) {
        log.info("Received request to /translation/" + originalLanguage + "/" + targetLanguage +
                 " with request body: " + requestData);
        TranslationData response = nerdTranslatorService.getTranslation(requestData, originalLanguage, targetLanguage);
        return new RestResponse(HttpStatus.OK, response);
    }

    @GetMapping("/languages")
    @Operation(summary = "Get map of all supported languages with their codes")
    public RestResponse getAllSupportedLanguages() {
        log.info("Received request to /translation/languages");
        Map<String, String> supportedLanguages = supportedLanguagesService.getSupportedLanguagesForTranslationApi();
        return new RestResponse(HttpStatus.OK, supportedLanguages);
    }
}