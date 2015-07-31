package murata.rms.batch.dmrimport.vo;

import java.util.Date;

/**
 * Entity、DataFieldなど、リソース管理のItemとなるものの基底クラス。
 * @author A1B5MEMB
 */
public class Resource {

	// 共通項目
	private int resourceId = 0;

	private String resourceNameJp = "";
	private String resourceNameEn = "";

	private String updateLog = "";				// 更新履歴
	private Date createTime = null;				// リソースの作成日
	private Date updateTime = null;				// リソースの更新日

	private String desc = "";					// 説明
	private int descSpecId;						// 説明用明細ID

	/**
	 * DMR側のID。無論DMR側からのデータの時にしか使用しない。
	 * 継承側により、意味が変わる(Entity時にはDMRのEntityの一意ID、など)
	 */
	public long dmrId = 0;

	public String getResourceNameEn() {
		return resourceNameEn;
	}
	public void setResourceNameEn(String resourceNameEn) {
		this.resourceNameEn = (resourceNameEn == null) ? "" :  resourceNameEn;
	}
	public String getResourceNameJp() {
		return resourceNameJp;
	}
	public void setResourceNameJp(String resourceNameJp) {
		this.resourceNameJp = (resourceNameJp == null) ? "" : resourceNameJp;
	}

	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = (desc == null) ? "" : desc;
	}

	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getDescSpecId() {
		return descSpecId;
	}

	public void setDescSpecId(int descSpecId) {
		this.descSpecId = descSpecId;
	}

	public long getDmrId() {
		return dmrId;
	}

	public void setDmrId(long dmrId) {
		this.dmrId = dmrId;
	}

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public String getUpdateLog() {
		return updateLog;
	}

	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}