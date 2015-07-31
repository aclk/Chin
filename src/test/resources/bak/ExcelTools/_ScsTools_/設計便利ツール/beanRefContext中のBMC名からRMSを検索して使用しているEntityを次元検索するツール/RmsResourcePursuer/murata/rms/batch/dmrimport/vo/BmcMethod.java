package murata.rms.batch.dmrimport.vo;

public class BmcMethod extends Resource {

	// Entityのspecification_type_id郡
	public final static int BMCMETHOD_NAME_EN		= 1;
	public final static int BMCMETHOD_ENT_DESK		= 2;

	// 別名を振る値群
	public String getBmcMethodNameEn() {
		return getResourceNameEn();
	}
	public void setBmcMethodNameEn(String resourceNameEn) {
		this.setResourceNameEn(resourceNameEn);
	}
	public String getBmcMethodNameJp() {
		return getResourceNameJp();
	}
	public void setBmcMethodNameJp(String resourceNameJp) {
		this.setResourceNameJp(resourceNameJp);
	}
}
