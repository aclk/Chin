package murata.rms.batch.dmrimport.vo;

import java.util.ArrayList;
import java.util.Date;

public class Parenthood {

	private Resource parentResource = null;	// 親リソースのアイテム
	private Resource childResource = null;	// 子リソースのアイテム

	private String description = "";
	private Date updateTime = null;

	private ArrayList<String> descriptions = new ArrayList<String>();


	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = (description == null) ? "" : description;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Resource getChildResource() {
		return childResource;
	}
	public void setChildResource(Resource childResource) {
		this.childResource = childResource;
	}
	public Resource getParentResource() {
		return parentResource;
	}
	public void setParentResource(Resource parentResource) {
		this.parentResource = parentResource;
	}
	public ArrayList<String> getDescriptions() {
		return descriptions;
	}

}
