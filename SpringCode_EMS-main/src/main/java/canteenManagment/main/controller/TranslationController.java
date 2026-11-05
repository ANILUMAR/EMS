package canteenManagment.main.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import canteenManagment.main.service.TranslationService;

@RestController
@RequestMapping("/api/translation")
//@CrossOrigin
public class TranslationController {

    @Autowired
    private TranslationService translationService;
	  
	  @GetMapping
	  public ResponseEntity<List<TranslationDTO>> getTranslationMap(@RequestParam String lang) {
	      List<TranslationDTO> translations = translationService.getTranslationsByLang(lang);
	      return ResponseEntity.ok(translations);
	  }
	
}
