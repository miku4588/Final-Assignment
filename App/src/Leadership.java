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
    public Leadership(double leadership) {
        if (!validateInput(leadership)) {
            throw new IllegalArgumentException("リーダーシップ評価は1〜5の範囲でなければなりません。");
        }
        this.leadership = leadership;
    }

    /**
     * リーダーシップ評価を検証するメソッド
     * @param leadership リーダーシップ評価（評価値）
     * @return リーダーシップ評価が1〜5の範囲内かどうか
     */
    @Override
    protected boolean validateInput(double leadership) {
        // リーダーシップ評価は1〜5の範囲であることを確認
        return leadership >= 1 && leadership <= 5;
    }

    /**
     * リーダーシップ評価を取得するメソッド
     * @return リーダーシップ評価
     */
    public double getLeadership() {
        return leadership;
    }
}
