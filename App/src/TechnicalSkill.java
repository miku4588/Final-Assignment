// 従業員の技術スキルを検証するクラス
class TechnicalSkill extends EmployeeInfoValidator {
    private double technicalSkill;

    /**
     * コンストラクタ
     * 
     * @param technicalSkill 技術スキル評価（文字列で受け取る）
     */
    public TechnicalSkill(String technicalSkill) {
        if (!validateInput(technicalSkill)) {
            throw new IllegalArgumentException("備考は500文字以内で入力してください。");
        }
        this.technicalSkill = Double.parseDouble(technicalSkill);
    }

    /**
     * 技術スキルの評価が正しいかどうかを判定する補助メソッド
     */
    @Override
    protected boolean validateInput(String technicalSkill) {
        try {
            double value = Double.parseDouble(technicalSkill);
            return value >= 1.0 && value <= 5.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 技術スキル評価を取得
     */
    public double getTechnicalSkill() {
        return technicalSkill;
    }
}
