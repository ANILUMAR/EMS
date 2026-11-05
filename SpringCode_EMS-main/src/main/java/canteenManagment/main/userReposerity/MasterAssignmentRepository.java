package canteenManagment.main.userReposerity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import canteenManagment.main.UserEntity.MasterAssignmentTbl;
@Repository
public interface MasterAssignmentRepository extends JpaRepository<MasterAssignmentTbl, Integer> {
	
	@Query("SELECT DISTINCT m.moduleDescription FROM MasterAssignmentTbl m")
	List<String> findAllDistinctModuleDescriptions();   
	
	@Query("SELECT DISTINCT m.processDescription FROM MasterAssignmentTbl m")
	List<String> findAllDistinctprocessDescription();  
	
	@Query("SELECT DISTINCT m.createDesc FROM MasterAssignmentTbl m")
	List<String> findAllDistinctcreateDesc();  
}
