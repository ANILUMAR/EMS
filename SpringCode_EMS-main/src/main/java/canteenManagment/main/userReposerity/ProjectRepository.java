package canteenManagment.main.userReposerity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import canteenManagment.main.UserEntity.Project;
@Repository

public interface ProjectRepository extends JpaRepository<Project, Long> {
}

