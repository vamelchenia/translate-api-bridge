package com.nerdtranslator.translateapibridge.service;

import com.nerdtranslator.translateapibridge.data.RequestData;
import com.nerdtranslator.translateapibridge.data.TranslationData;

public interface NerdTranslatorService {
    TranslationData getTranslation(RequestData requestData, String originalLanguage, String targetLanguage);
}
