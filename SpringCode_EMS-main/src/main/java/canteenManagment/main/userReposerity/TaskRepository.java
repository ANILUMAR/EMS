package canteenManagment.main.userReposerity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import canteenManagment.main.UserEntity.Task;
@Repository

public interface TaskRepository extends JpaRepository<Task, Long> {
}