public class Name {

    /**氏名 */
    private String name;

    /**コンストラクタ */
    public Name(String name) {
        this.name = name;
    }

    /**
     * 氏名のgetter
     * @param なし
     * @return 氏名
     * @version 1.0.0
     */
    public String getName() {
        return name;
    }

    /**
     * 氏名のsetter
     * @param name 氏名
     * @return なし
     * @version 1.0.0
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 氏名を文字列として返す
     * @return id
     * @version 1.0.0
     */
    @Override
    public String toString() {
        return name;
    }
}