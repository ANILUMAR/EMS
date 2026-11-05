package canteenManagment.main.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import canteenManagment.main.UserEntity.Translation;
import canteenManagment.main.controller.TranslationDTO;
import canteenManagment.main.userReposerity.TranslationRepository;

@Service
public class TranslationService {

    @Autowired
    private TranslationRepository translationRepository;

	/*
	 * public Map<String, String> getTranslationsByLang(String langCode) {
	 * List<Translation> list = translationRepository.findByLanguage(langCode);
	 * Map<String, String> map = new HashMap<>(); for (Translation t : list) {
	 * map.put(t.getKey(), t.getValue()); } return map; }
	 */
    

    public List<TranslationDTO> getTranslationsByLang(String langCode) {
        List<Translation> list = translationRepository.findByLanguage(langCode);
        List<TranslationDTO> translationList = new ArrayList<>();

        for (Translation t : list) {
            TranslationDTO dto = new TranslationDTO();
            dto.setKey(t.getKey());
            dto.setValue(t.getValue());
            translationList.add(dto);
        }

        return translationList;
    }
    
    public Map<String, String> getTranslationsByLangAndScreen(String lang, String screen) {
        List<Translation> translations = translationRepository.findByScreenAndLang(screen, lang);
        return translations.stream().collect(Collectors.toMap(Translation::getKey, Translation::getValue));
    }
}

