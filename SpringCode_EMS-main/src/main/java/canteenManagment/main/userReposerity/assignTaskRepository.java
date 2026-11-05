package canteenManagment.main.userReposerity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import canteenManagment.main.UserEntity.AssignTask;
import canteenManagment.main.UserEntity.UserEntity;
@Repository
public interface assignTaskRepository extends JpaRepository<AssignTask, Long> {
	List<AssignTask> findByAssignedToEmail(Optional<UserEntity> userEntity);   
	List<AssignTask> findByAssignedTo_Id(Long user );

}
