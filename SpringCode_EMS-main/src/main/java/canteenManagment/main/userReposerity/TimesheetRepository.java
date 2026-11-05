package canteenManagment.main.userReposerity;

import org.springframework.data.jpa.repository.JpaRepository;

import canteenManagment.main.UserEntity.TimesheetEntry;

public interface TimesheetRepository extends JpaRepository<TimesheetEntry, Long> {}

