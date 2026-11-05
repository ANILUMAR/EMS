package canteenManagment.main.UserEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "master_screens_tbl")

public class MasterScreensTbl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screen_id")
    private Long screenId;

    @Column(name = "screen_description", nullable = false, length = 255)
    private String screenDescription;

    // Many screens -> one module
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private MasterModuleDescriptionTbl module;

    // Many screens -> one submodule
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submodule_id", nullable = false)
    private MasterSubModuleDescriptionTbl subModule;

	public Long getScreenId() {
		return screenId;
	}

	public void setScreenId(Long screenId) {
		this.screenId = screenId;
	}

	public String getScreenDescription() {
		return screenDescription;
	}

	public void setScreenDescription(String screenDescription) {
		this.screenDescription = screenDescription;
	}

	public MasterModuleDescriptionTbl getModule() {
		return module;
	}

	public void setModule(MasterModuleDescriptionTbl module) {
		this.module = module;
	}

	public MasterSubModuleDescriptionTbl getSubModule() {
		return subModule;
	}

	public void setSubModule(MasterSubModuleDescriptionTbl subModule) {
		this.subModule = subModule;
	}
    
    
}
