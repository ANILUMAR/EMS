package canteenManagment.main.userReposerity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import canteenManagment.main.UserEntity.MasterProjectTbl;

@Repository
public interface MasterProjectRepository extends JpaRepository<MasterProjectTbl, Integer> {
	
	@Query("Select count(p.id) from MasterProjectTbl p")
	Integer getAllProjectsCount();

}
