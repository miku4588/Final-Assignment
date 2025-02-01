public class ID {

    /**社員ID */
    private String id;

    /**コンストラクタ */
    public ID(String id) {
        this.id = id;
    }

    /**
     * 社員IDのgetter
     * @param なし
     * @return 社員ID
     * @version 1.0.0
     */
    public String getID() {
        return id;
    }

    /**
     * 社員IDのsetter
     * @param id 社員ID
     * @return なし
     * @version 1.0.0
     */
    public void setID(String id) {
        String regex_AlphaNum = "^[A-Za-z0-9]+$" ; // 半角英数字の正規表現

        
        this.id = id;
    }

    /**
     * 社員IDを文字列として返す
     * @return id
     * @version 1.0.0
     */
    @Override
    public String toString() {
        return id;
    }
}