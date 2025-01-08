public class EngineerStartYear {
    // フィールド
    private int engineerStartYear; // 従業員のエンジニアとしての開始年（YYYY形式）

    // コンストラクタ
    public EngineerStartYear(int engineerStartYear) {
        if (String.valueOf(engineerStartYear).length() != 4) {
            throw new IllegalArgumentException("エンジニアの開始年はYYYY形式でなければなりません。");
        }
        // this.engineerStartYearは、クラスのインスタンス変数（フィールド）を指す。引数として渡されたengineerStartYearの値が、クラスのフィールドに設定される。
        this.engineerStartYear = engineerStartYear;
    }

    // ゲッターメソッド
    public int getEngineerStartYear() {
        return engineerStartYear;
    }
}
