package canteenManagment.main.userReposerity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import canteenManagment.main.UserEntity.WorkLog;
@Repository

public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {
}
