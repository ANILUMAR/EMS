package canteenManagment.main.UserEntity;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "master_submodule_description_tbl")

public class MasterSubModuleDescriptionTbl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submodule_id")
    private Long subModuleId;

    @Column(name = "submodule_description", nullable = false, length = 255)
    private String subModuleDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private MasterModuleDescriptionTbl module;

    @OneToMany(mappedBy = "subModule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MasterScreensTbl> screens;

	public Long getSubModuleId() {
		return subModuleId;
	}

	public void setSubModuleId(Long subModuleId) {
		this.subModuleId = subModuleId;
	}

	public String getSubModuleDescription() {
		return subModuleDescription;
	}

	public void setSubModuleDescription(String subModuleDescription) {
		this.subModuleDescription = subModuleDescription;
	}

	public MasterModuleDescriptionTbl getModule() {
		return module;
	}

	public void setModule(MasterModuleDescriptionTbl module) {
		this.module = module;
	}

	public List<MasterScreensTbl> getScreens() {
		return screens;
	}

	public void setScreens(List<MasterScreensTbl> screens) {
		this.screens = screens;
	}
    
    
}
