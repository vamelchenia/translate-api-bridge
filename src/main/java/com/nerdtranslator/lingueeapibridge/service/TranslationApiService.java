package com.nerdtranslator.lingueeapibridge.service;

import java.util.List;

public interface TranslationApiService {
    List<String> fetchDataFromApi(String projectId, String originalText, String originalLanguage, String targetLanguage);
}
