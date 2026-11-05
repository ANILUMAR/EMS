package canteenManagment.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import canteenManagment.main.UserEntity.TimesheetEntry;
import canteenManagment.main.userReposerity.TimesheetRepository;
import java.util.List;


@Service
public class TimesheetService {
    @Autowired
    private TimesheetRepository repository;

    public List<TimesheetEntry> getAll() {
        return repository.findAll();
    }

    public TimesheetEntry save(TimesheetEntry entry) {
        return repository.save(entry);
    }
}
