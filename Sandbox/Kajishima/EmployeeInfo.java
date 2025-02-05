public class EmployeeInfo {

    /**社員ID */
    private EmployeeId employeeId;

    /**氏名 */
    private Name name;

    /**コンストラクタ */
    public EmployeeInfo(String employeeId, String name) {
        this.employeeId = new EmployeeId(employeeId);
        this.name = new Name(name);
    }

    /**
     * toStringメソッド
     * @param なし
     * @return CSV出力するためカンマ区切りの文字列に変換
     * @version 1.0.0
     */
    @Override
    public String toString() {
        return employeeId + "," + name;
    }
}