package canteenManagment.main.userReposerity;

import org.springframework.data.jpa.repository.JpaRepository;

import canteenManagment.main.UserEntity.DeletedTask;

public interface DeletedTaskRepository  extends JpaRepository<DeletedTask, Long>{

}
