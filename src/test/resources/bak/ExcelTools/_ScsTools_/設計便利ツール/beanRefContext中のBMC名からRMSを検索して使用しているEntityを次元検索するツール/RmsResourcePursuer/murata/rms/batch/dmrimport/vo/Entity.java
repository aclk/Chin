package murata.rms.batch.dmrimport.vo;

public class Entity extends Resource {

	// Entityのspecification_type_id郡
	public final static int ENT_NAME_EN		= 1;
	public final static int ENT_DESK		= 2;
	public final static int ENT_ENTITY_ID 	= 401;
	public final static int ENT_COVER_KEY 	= 403;
	public final static int ENT_SUBJECT		= 404;


	// 明細項目
	private int entityNameEnSpecId;				// エンティティ英名用明細ID

	private String entityId = "";				// エンティティID
	private int entityIdSpecId;					// エンティティ英名用明細ID

	private String coverKey = "";				// 代理キー
	private int coverKeySpecId;					// 代理キー用明細ID

	private String subject = "";				// MS,PSなどの業務サブシステム
	private int subjectSpecId;					// サブジェクト用明細ID

	// 別名を振る値群
	public String getEntityNameEn() {
		return getResourceNameEn();
	}
	public void setEntityNameEn(String resourceNameEn) {
		this.setResourceNameEn(resourceNameEn);
	}
	public String getEntityNameJp() {
		return getResourceNameJp();
	}
	public void setEntityNameJp(String resourceNameJp) {
		this.setResourceNameJp(resourceNameJp);
	}
	public String getCoverKey() {
		return coverKey;
	}
	public void setCoverKey(String coverKey) {
		this.coverKey = (coverKey == null) ? "" : coverKey;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = (subject == null) ? "" : subject;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = (entityId == null) ? "" : entityId;
	}
	public int getCoverKeySpecId() {
		return coverKeySpecId;
	}
	public void setCoverKeySpecId(int coverKeySpecId) {
		this.coverKeySpecId = coverKeySpecId;
	}
	public int getEntityIdSpecId() {
		return entityIdSpecId;
	}
	public void setEntityIdSpecId(int entityIdSpecId) {
		this.entityIdSpecId = entityIdSpecId;
	}
	public int getEntityNameEnSpecId() {
		return entityNameEnSpecId;
	}
	public void setEntityNameEnSpecId(int entityNameEnSpecId) {
		this.entityNameEnSpecId = entityNameEnSpecId;
	}
	public int getSubjectSpecId() {
		return subjectSpecId;
	}
	public void setSubjectSpecId(int subjectSpecId) {
		this.subjectSpecId = subjectSpecId;
	}
}
