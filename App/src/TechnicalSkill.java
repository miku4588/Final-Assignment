// 従業員の技術スキルを検証するクラス
class TechnicalSkill extends EmployeeInfoValidator {
    private double technicalSkill;

    /**
     * コンストラクタ
     * @param technicalSkill 技術スキル評価
     * @throws IllegalArgumentException 評価が1〜5の範囲外の場合
     */
    public TechnicalSkill(double technicalSkill) {
        if (!validateInput(technicalSkill)) {
            throw new IllegalArgumentException("技術スキルは1から5の範囲でなければなりません。");
        }
        this.technicalSkill = technicalSkill;
    }

    /**
     * 技術スキルの評価を検証するメソッド
     * @param technicalSkill 技術スキル評価
     * @return 技術スキルが1〜5の範囲内かどうか
     */
    @Override
    protected boolean validateInput(double technicalSkill) {
        // 技術スキルは1から5の範囲であることを確認
        return technicalSkill >= 1 && technicalSkill <= 5;
    }

    /**
     * 技術スキル評価を取得するメソッド
     * @return 技術スキル評価
     */
    public double getTechnicalSkill() {
        return technicalSkill;
    }
}
