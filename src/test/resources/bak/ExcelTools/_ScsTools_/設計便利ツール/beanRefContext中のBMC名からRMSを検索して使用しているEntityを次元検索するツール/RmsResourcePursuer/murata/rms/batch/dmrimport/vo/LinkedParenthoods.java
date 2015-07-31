package murata.rms.batch.dmrimport.vo;

import java.util.HashMap;

public class LinkedParenthoods {

    private int resourceIdChild = 0;

    private int resourceIdParent = 0;

    private LinkedParenthoods parent = null;

    private HashMap<Integer, LinkedParenthoods> children =
        new HashMap<Integer, LinkedParenthoods>();

    /**
     * childrenを戻す。
     * <br>
     * @return  children
     */
    public HashMap<Integer, LinkedParenthoods> getChildren() {
        return children;
    }

    /**
     * childrenを設定する。
     * <br>
     * @param children HashMap<Integer,LinkedParenthoods>
     */
    public void setChildren(HashMap<Integer, LinkedParenthoods> children) {
        this.children = children;
    }

    /**
     * parentを戻す。
     * <br>
     * @return  parent
     */
    public LinkedParenthoods getParent() {
        return parent;
    }

    /**
     * parentを設定する。
     * <br>
     * @param parent LinkedParenthoods
     */
    public void setParent(LinkedParenthoods parent) {
        this.parent = parent;
    }

    /**
     * resourceIdChildを戻す。
     * <br>
     * @return  resourceIdChild
     */
    public int getResourceIdChild() {
        return resourceIdChild;
    }

    /**
     * resourceIdChildを設定する。
     * <br>
     * @param resourceIdChild int
     */
    public void setResourceIdChild(int resourceIdChild) {
        this.resourceIdChild = resourceIdChild;
    }

    /**
     * resourceIdParentを戻す。
     * <br>
     * @return  resourceIdParent
     */
    public int getResourceIdParent() {
        return resourceIdParent;
    }

    /**
     * resourceIdParentを設定する。
     * <br>
     * @param resourceIdParent int
     */
    public void setResourceIdParent(int resourceIdParent) {
        this.resourceIdParent = resourceIdParent;
    }

    /**
     * 自身の所持している間連中、末尾の者を返す。
     * @return
     */
    public HashMap<Integer, LinkedParenthoods> getEndParenthoods() {
        HashMap<Integer, LinkedParenthoods> result =
            new HashMap<Integer, LinkedParenthoods>();

        searchEndParenthood(this , result);

        return result;
    }

    /**
     * 末尾のモノを探す回帰処理。
     * @param parenthoods
     * @param result
     */
    private void searchEndParenthood(LinkedParenthoods parenthoods,
        HashMap<Integer, LinkedParenthoods> result) {
        for (LinkedParenthoods item : parenthoods.children.values()) {
            if (item.getChildren().size() == 0) {
                result.put(item.getResourceIdChild() , item);
            } else {
                searchEndParenthood(item, result);
            }
        }
    }

    /**
     * 自身の子関連の中からリソースIDで関連を検索し返す。
     * @param resourceId
     * @return
     */
    public LinkedParenthoods getChildParenthoods(int resourceId) {
        for (LinkedParenthoods item : children.values()) {
            if (item.getResourceIdChild() == resourceId) {
                return item;
            }
        }
        return null;
    }

    public void addChild(LinkedParenthoods child) {
        // 自身の子に追加して。
        children.put(child.getResourceIdChild(), child);
        // 親は自分を刻む
        child.setParent(this);
    }

}
