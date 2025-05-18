// 従業員の技術スキルを検証するクラス
class TechnicalSkill extends EmployeeInfoValidator {
    private double technicalSkill;

    /**
     * コンストラクタ
     * @param technicalSkill 技術スキル評価
     * @throws IllegalArgumentException 評価が1〜5の範囲外の場合
     */
    public TechnicalSkill(String technicalSkill) {
        if (!validateInput(technicalSkill)) {
            throw new IllegalArgumentException("技術スキルは1から5の範囲でなければなりません。");
        }
        this.technicalSkill = Double.parseDouble(technicalSkill); // Stringをdoubleに変換
    }

    /**
     * 技術スキルの評価を検証するメソッド
     * @param technicalSkill 技術スキル評価
     * @return 技術スキルが1〜5の範囲内かどうか
     */
    @Override
    protected boolean validateInput(String technicalSkill) {
        try {
            double value = Double.parseDouble(technicalSkill); // Stringをdoubleに変換
            return value >= 1 && value <= 5; // 範囲をチェック
        } catch (NumberFormatException e) {
            return false; // 数値変換に失敗した場合は無効とする
        }
    }

    /**
     * 技術スキル評価を取得するメソッド
     * @return 技術スキル評価
     */
    public double getTechnicalSkill() {
        return technicalSkill;
    }
}
