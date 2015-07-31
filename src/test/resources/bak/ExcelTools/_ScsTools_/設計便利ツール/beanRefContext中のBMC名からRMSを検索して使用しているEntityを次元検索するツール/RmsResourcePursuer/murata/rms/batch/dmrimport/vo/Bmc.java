package murata.rms.batch.dmrimport.vo;

public class Bmc extends Resource {

	// Entityのspecification_type_id郡
	public final static int BMC_NAME_EN		= 1;
	public final static int BMC_DESK		= 2;


	// 別名を振る値群
	public String getBmcNameEn() {
		return getResourceNameEn();
	}
	public void setBmcNameEn(String resourceNameEn) {
		this.setResourceNameEn(resourceNameEn);
	}
	public String getBmcNameJp() {
		return getResourceNameJp();
	}
	public void setBmcNameJp(String resourceNameJp) {
		this.setResourceNameJp(resourceNameJp);
	}
}
