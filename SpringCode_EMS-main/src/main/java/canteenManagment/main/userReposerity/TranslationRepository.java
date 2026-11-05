package canteenManagment.main.userReposerity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import canteenManagment.main.UserEntity.Translation;
import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {
    List<Translation> findByLanguage(String langCode);
    
    @Query("SELECT t FROM Translation t WHERE t.screenName = :screen AND t.language = :lang")
    List<Translation> findByScreenAndLang(@Param("screen") String screen, @Param("lang") String lang);
}

