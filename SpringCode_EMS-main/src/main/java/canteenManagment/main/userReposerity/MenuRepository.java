package canteenManagment.main.userReposerity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import canteenManagment.main.UserEntity.MenuItem;

@Repository
public interface MenuRepository extends JpaRepository<MenuItem, Long> {
	
	
	
}