package canteenManagment.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import canteenManagment.main.UserEntity.WorkLog;
import canteenManagment.main.service.WorkLogService;

@RestController
@RequestMapping("/api/worklogs")
public class WorkLogController {
    @Autowired
    private WorkLogService workLogService;

    @GetMapping
    public ResponseEntity<List<WorkLog>> getAllWorkLogs() {
        return ResponseEntity.ok(workLogService.getAll());
    }

    @PostMapping
    public ResponseEntity<WorkLog> logWork(@RequestBody WorkLog workLog) {
        return ResponseEntity.ok(workLogService.logWork(workLog));
    }
}