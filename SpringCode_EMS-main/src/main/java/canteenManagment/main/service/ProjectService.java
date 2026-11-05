package canteenManagment.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import canteenManagment.main.UserEntity.Project;
import canteenManagment.main.userReposerity.ProjectRepository;

@Service
public class ProjectService {
    @Autowired private ProjectRepository projectRepo;

    public List<Project> findAll() {
        return projectRepo.findAll();
    }

    public Project create(Project project) {
        return projectRepo.save(project);
    }
}