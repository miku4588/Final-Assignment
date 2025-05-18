/**
 * 従業員のリーダーシップ評価を管理するクラス。
 * リーダーシップ評価を保持。
 */
class Leadership extends EmployeeInfoValidator {
    private double leadership;

    /**
     * コンストラクタ
     * @param leadership リーダーシップ評価（評価値）
     * @throws IllegalArgumentException 評価が1〜5の範囲外の場合
     */
    public Leadership(String leadership) {
        // Stringからdoubleに変換し、バリデーションを行う
        if (!validateLeadership(leadership)) {
            throw new IllegalArgumentException("リーダーシップ評価は1〜5の範囲でなければなりません。");
        }
        this.leadership = Double.parseDouble(leadership); // Stringをdoubleに変換
    }

    /**
     * リーダーシップ評価を検証するメソッド
     * @param leadership リーダーシップ評価（評価値）
     * @return リーダーシップ評価が1〜5の範囲内かどうか
     */
    protected boolean validateLeadership(String leadership) {
        try {
            double value = Double.parseDouble(leadership); // Stringをdoubleに変換
            return value >= 1 && value <= 5; // 範囲をチェック
        } catch (NumberFormatException e) {
            return false; // 数値変換に失敗した場合は無効とする
        }
    }

    /**
     * リーダーシップ評価を取得するメソッド
     * @return リーダーシップ評価
     */
    public double getLeadership() {
        return leadership;
    }
}
