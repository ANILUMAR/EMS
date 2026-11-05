package canteenManagment.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import canteenManagment.main.UserEntity.WorkLog;
import canteenManagment.main.userReposerity.WorkLogRepository;

@Service
public class WorkLogService {
    @Autowired private WorkLogRepository workLogRepository;

    public List<WorkLog> getAll() {
        return workLogRepository.findAll();
    }

    public WorkLog logWork(WorkLog workLog) {
        return workLogRepository.save(workLog);
    }
}