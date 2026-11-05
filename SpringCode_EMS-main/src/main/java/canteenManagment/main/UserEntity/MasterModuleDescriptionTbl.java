package canteenManagment.main.UserEntity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "master_module_description_tbl")
public class MasterModuleDescriptionTbl {
	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "module_id")
    private Long moduleId;

    @Column(name = "module_description", nullable = false, length = 255)
    private String moduleDescription;

    // One module -> many submodules
    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MasterSubModuleDescriptionTbl> subModules;

    // One module -> many screens
    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MasterScreensTbl> screens;

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleDescription() {
		return moduleDescription;
	}

	public void setModuleDescription(String moduleDescription) {
		this.moduleDescription = moduleDescription;
	}

	public List<MasterSubModuleDescriptionTbl> getSubModules() {
		return subModules;
	}

	public void setSubModules(List<MasterSubModuleDescriptionTbl> subModules) {
		this.subModules = subModules;
	}

	public List<MasterScreensTbl> getScreens() {
		return screens;
	}

	public void setScreens(List<MasterScreensTbl> screens) {
		this.screens = screens;
	}
    
    

}
