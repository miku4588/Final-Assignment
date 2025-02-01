public class Attitude {
    // フィールド
    private int attitude; // 従業員の態度評価（1-5の範囲）
    // コンストラクタ

    public Attitude(int attitude) {
        if (attitude < 1 || attitude > 5) {
            throw new IllegalArgumentException("態度評価は1から5の範囲でなければなりません。");
        }
        this.attitude = attitude;
    }

    // ゲッターメソッド
    public int getAttitude() {
        return attitude;
    }
}
