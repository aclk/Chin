package murata.rms.batch.dmrimport.vo;

public class Criteria extends Resource {

	public final static int CRI_CRITERIA_ID = 201;				// クライテリアID
	public final static int CRI_USE_VO_TYPE = 202;				// 使用するVOの型
	public final static int CRI_CRITERIA_TYPE = 203;			// タイプ
	public final static int CRI_QL_TYPE = 204;					// QLタイプ
	public final static int CRI_ENTITY_OBJECT_TYPE = 205;		// ENTITYオブジェクトの型
	public final static int CRI_ENTITY_OBJECT_CREATE = 206;		// ENTITYオブジェクトの作成
	public final static int CRI_CONTROL_BREAK = 207;			// コントロールブレイクの有無
	public final static int CRI_TABLE_LOCK = 208;				// テーブルロックの有
	public final static int CRI_DETAIL_DESC = 209;				// 詳細説明の有無
	public final static int CRI_QL_TEMPLATE = 210;				// QLテンプレートの有無
	public final static int CRI_ADD_DESC = 211;					// 補足説明の有無
    public final static int CRI_PERFORMANCE = 212;                 // 実行時間(ms)


	// Criteriaのspecification_type_id郡

	// 明細項目
	private String criteriaId = "";				// クライテリアID
	private int criteriaIdSpecId;				// クライテリアIDの明細ID

	private String useVoType = "";				// 使用するVOの型
	private int useVoTypeSpecId;				// 使用するVOの型の明細ID

	private String criteriaType = "";			// タイプ
	private int criteriaTypeSpecId;				// タイプの明細ID

	private String qlType = "";					// QLタイプ
	private int qlTypeSpecId;					// QLタイプの明細ID

	private String entityObjectType = "";		// Entityオブジェクトの型
	private int entityObjectTypeSpecId;			// Entityオブジェクトの型

	private String entityObjectCreate = "";		// Entityオブジェクトの作成
	private int entityObjectCreateSpecId;		// Entityオブジェクトの作成

	private String controlBreak = "";			// コントロールブレイクの有無
	private int controlBreakSpecId;				// コントロールブレイクの有無の明細ID

	private String tableLock = "";				// テーブルロックの有無
	private int tableLockSpecId;				// テーブルロックの有無の明細ID

	private String detailDesc = "";				// 詳細説明の有無
	private int detailDescSpecId;				// 詳細説明の有無の明細ID

	private String qlTemplate = "";				// QLテンプレートの有無
	private int qlTemplateSpecId;				// QLテンプレートの有無の明細ID

	private String addDesc = "";				// 補足説明の有無
	private int addDescSpecId;					// 補足説明の有無の明細ID

    private String performance = "";                // 実行時間(ms)
    private int performanceSpecId;                  // 実行時間(ms)の明細ID

	// ゲッタセッタ
    public String getPerformance() {
        return performance;
    }
    public void setPerformance(String performance) {
        this.performance = performance == null ? "" : performance;
    }
    public int getPerformanceSpecId() {
        return performanceSpecId;
    }
    public void setPerformanceSpecId(int performanceSpecId) {
        this.performanceSpecId = performanceSpecId ;
    }
	public String getAddDesc() {
		return addDesc;
	}
	public void setAddDesc(String addDesc) {
		this.addDesc = addDesc == null ? "" : addDesc;
	}
	public int getAddDescSpecId() {
		return addDescSpecId;
	}
	public void setAddDescSpecId(int addDescSpecId) {
		this.addDescSpecId = addDescSpecId ;
	}
	public String getControlBreak() {
		return controlBreak;
	}
	public void setControlBreak(String controlBreak) {
		this.controlBreak = controlBreak == null ? "" : controlBreak;
	}
	public int getControlBreakSpecId() {
		return controlBreakSpecId;
	}
	public void setControlBreakSpecId(int controlBreakSpecId) {
		this.controlBreakSpecId = controlBreakSpecId;
	}
	public String getCriteriaId() {
		return criteriaId;
	}
	public void setCriteriaId(String criteriaId) {
		this.criteriaId = criteriaId == null ? "" : criteriaId;
	}
	public int getCriteriaIdSpecId() {
		return criteriaIdSpecId;
	}
	public void setCriteriaIdSpecId(int criteriaIdSpecId) {
		this.criteriaIdSpecId = criteriaIdSpecId;
	}
	public String getCriteriaType() {
		return criteriaType;
	}
	public void setCriteriaType(String criteriaType) {
		this.criteriaType = criteriaType == null ? "" : criteriaType;
	}
	public int getCriteriaTypeSpecId() {
		return criteriaTypeSpecId;
	}
	public void setCriteriaTypeSpecId(int criteriaTypeSpecId) {
		this.criteriaTypeSpecId = criteriaTypeSpecId;
	}
	public String getDetailDesc() {
		return detailDesc;
	}
	public void setDetailDesc(String detailDesc) {
		this.detailDesc = detailDesc == null ? "" : detailDesc;
	}
	public int getDetailDescSpecId() {
		return detailDescSpecId;
	}
	public void setDetailDescSpecId(int detailDescSpecId) {
		this.detailDescSpecId = detailDescSpecId;
	}
	public String getEntityObjectCreate() {
		return entityObjectCreate;
	}
	public void setEntityObjectCreate(String entityObjectCreate) {
		this.entityObjectCreate = entityObjectCreate == null ? "" : entityObjectCreate;
	}
	public int getEntityObjectCreateSpecId() {
		return entityObjectCreateSpecId;
	}
	public void setEntityObjectCreateSpecId(int entityObjectCreateSpecId) {
		this.entityObjectCreateSpecId = entityObjectCreateSpecId;
	}
	public String getEntityObjectType() {
		return entityObjectType;
	}
	public void setEntityObjectType(String entityObjectType) {
		this.entityObjectType = entityObjectType == null ? "" : entityObjectType;
	}
	public int getEntityObjectTypeSpecId() {
		return entityObjectTypeSpecId;
	}
	public void setEntityObjectTypeSpecId(int entityObjectTypeSpecId) {
		this.entityObjectTypeSpecId = entityObjectTypeSpecId;
	}
	public String getQlTemplate() {
		return qlTemplate;
	}
	public void setQlTemplate(String qlTemplate) {
		this.qlTemplate = qlTemplate == null ? "" : qlTemplate;
	}
	public int getQlTemplateSpecId() {
		return qlTemplateSpecId;
	}
	public void setQlTemplateSpecId(int qlTemplateSpecId) {
		this.qlTemplateSpecId = qlTemplateSpecId;
	}
	public String getUseVoType() {
		return useVoType;
	}
	public void setUseVoType(String useVoType) {
		this.useVoType = useVoType  == null ? "" : useVoType;
	}
	public int getUseVoTypeSpecId() {
		return useVoTypeSpecId;
	}
	public void setUseVoTypeSpecId(int useVoTypeSpecId) {
		this.useVoTypeSpecId = useVoTypeSpecId;
	}
	public String getQlType() {
		return qlType;
	}
	public void setQlType(String qlType) {
		this.qlType = qlType  == null ? "" : qlType;
	}
	public int getQlTypeSpecId() {
		return qlTypeSpecId;
	}
	public void setQlTypeSpecId(int qlTypeSpecId) {
		this.qlTypeSpecId = qlTypeSpecId;
	}
	public String getTableLock() {
		return tableLock;
	}
	public void setTableLock(String tableLock) {
		this.tableLock = tableLock == null ? "" : tableLock;
	}
	public int getTableLockSpecId() {
		return tableLockSpecId;
	}
	public void setTableLockSpecId(int tableLockSpecId) {
		this.tableLockSpecId = tableLockSpecId;
	}




}
