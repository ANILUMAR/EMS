package canteenManagment.main.userReposerity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import canteenManagment.main.UserEntity.AssignTask;
import canteenManagment.main.UserEntity.UserEntity;
import canteenManagment.main.UserEntity.UserIdName;

import java.util.List;



@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByName(String name); 
    Optional<UserEntity> findByEmail(String email);
    
    void deleteByEmail(String email);
    
	@Query("SELECT DISTINCT m.name FROM UserEntity m")
	List<String> findAllDistinctname();
	
	
	@Query("SELECT DISTINCT u.id AS id, u.name AS name FROM UserEntity u")
	List<UserIdName> findAllDistinctIdAndName();
	
	@Query("Select count(u.id) from UserEntity u")
	Integer getAllEmployeesCount();
}
