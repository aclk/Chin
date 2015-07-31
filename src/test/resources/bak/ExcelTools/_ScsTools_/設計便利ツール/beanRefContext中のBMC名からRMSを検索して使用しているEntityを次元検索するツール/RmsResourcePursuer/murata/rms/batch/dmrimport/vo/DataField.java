package murata.rms.batch.dmrimport.vo;

/**
 * データ項目のVO。
 * @author K.Miura
 *
 */
public class DataField extends Resource {
	
	// データ項目のspecification_type_id郡
	public final static int FLD_NAME_EN			= 1;
	public final static int FLD_DESK			= 2;
	public final static int FLD_DATA_FIELD_ID 	= 405;		// データ項目ID
	public final static int FLD_TYPE 			= 402;		// 型
	public final static int FLD_PRECISION 		= 407;		// 精度
	public final static int FLD_DECIMAL_POINT	= 406;		// 小数点
	

	// 共通項目

	// 明細項目
	private int dataFieldNameEnSpecId;			// データ項目和名
	
	private String dataFieldId = "";			// データ項目ID
	private int dataFieldIdSpecId;				// データ項目ID明細ID
	
	private String type = "";					// 型
	private int typeSpecId;						// 型明細ID
	
	private String precision = "";				// 精度
	private int precisionSpecId;				// 精度明細ID
	
	private String decimalPoint = "";			// 小数点
	private int decimalPointSpecId;				// 小数点明細ID

	
	// 別名を振る値群
	public String getDataFieldNameEn() {
		return getResourceNameEn();
	}
	public void setDataFieldNameEn(String resourceNameEn) {
		this.setResourceNameEn(resourceNameEn);
	}
	public String getDataFieldNameJp() {
		return getResourceNameJp();
	}
	public void setDataFieldNameJp(String resourceNameJp) {
		this.setResourceNameJp(resourceNameJp)	;
	}
	
	// Getter,Setter群
	public String getDataFieldId() {
		return dataFieldId;
	}
	public void setDataFieldId(String dataFieldId) {
		this.dataFieldId = (dataFieldId == null) ? "" : dataFieldId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = (type == null) ? "" : type;
	}
	public String getDecimalPoint() {
		return decimalPoint;
	}
	public void setDecimalPoint(String decimalPoint) {
		this.decimalPoint = (decimalPoint == null) ? "" : decimalPoint;
	}
	public String getPrecision() {
		return precision;
	}
	public void setPrecision(String precision) {
		this.precision = (precision == null) ? "" : precision;
	}
	public int getDataFieldIdSpecId() {
		return dataFieldIdSpecId;
	}
	public void setDataFieldIdSpecId(int dataFieldIdSpecId) {
		this.dataFieldIdSpecId = dataFieldIdSpecId;
	}
	public int getDataFieldNameEnSpecId() {
		return dataFieldNameEnSpecId;
	}
	public void setDataFieldNameEnSpecId(int dataFieldNameEnSpecId) {
		this.dataFieldNameEnSpecId = dataFieldNameEnSpecId;
	}
	public int getDecimalPointSpecId() {
		return decimalPointSpecId;
	}
	public void setDecimalPointSpecId(int decimalPointSpecId) {
		this.decimalPointSpecId = decimalPointSpecId;
	}
	public int getPrecisionSpecId() {
		return precisionSpecId;
	}
	public void setPrecisionSpecId(int precisionSpecId) {
		this.precisionSpecId = precisionSpecId;
	}
	public int getTypeSpecId() {
		return typeSpecId;
	}
	public void setTypeSpecId(int typeSpecId) {
		this.typeSpecId = typeSpecId;
	}

}
