public class EmployeeId {
    //employeeIdという名前のフィールドを定義
    private String employeeId; // 従業員のID（半角英数字）

    // コンストラクタ(引数)
    public EmployeeId(String employeeId) {
        // 正規表現^[a-zA-Z0-9]+$は、文字列が英字（大文字・小文字）または数字のみからなることを意味する
        if (!employeeId.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("従業員IDは半角英数字でなければなりません。");
        }
        this.employeeId = employeeId;
    }

    //ゲッターメソッド
    public String getEmployeeId() {
        return employeeId;
    }
}
