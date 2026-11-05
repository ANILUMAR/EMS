package canteenManagment.main.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import canteenManagment.main.UserEntity.TimesheetEntry;
import canteenManagment.main.security.AuthContext;
import canteenManagment.main.service.TimesheetService;

@RestController
@RequestMapping("/api/timesheets")
public class TimesheetController {

    @Autowired
    private TimesheetService service;

    /** Admins see every entry; other roles only see the hours they logged. */
    @GetMapping
    public List<TimesheetEntry> getAll(Authentication authentication) {
        List<TimesheetEntry> entries = service.getAll();

        if (!AuthContext.isAdmin(authentication)) {
            String email = AuthContext.email(authentication);
            entries = entries.stream()
                    .filter(entry -> entry.getEmployeeName() != null
                            && entry.getEmployeeName().equalsIgnoreCase(email))
                    .collect(Collectors.toList());
        }

        return entries;
    }

    /**
     * A non-admin always books hours against their own account, so a tampered
     * payload cannot log time for somebody else.
     */
    @PostMapping
    public TimesheetEntry save(@RequestBody TimesheetEntry entry, Authentication authentication) {
        if (!AuthContext.isAdmin(authentication)) {
            entry.setEmployeeName(AuthContext.email(authentication));
        }
        entry.setId(null);
        return service.save(entry);
    }
}
