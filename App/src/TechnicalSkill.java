// 従業員の技術スキルを検証するクラス
class TechnicalSkill extends EmployeeInfoValidator {
    private double technicalSkill;
    private String input;

    /**
     * コンストラクタ
     * @param technicalSkill 技術スキル評価（文字列で受け取る）
     */
    public TechnicalSkill(String technicalSkill) {
        this.input = technicalSkill;
    }

    /**
     * 技術スキルの評価を検証するメソッド（バリデーションの実行）
     */
    @Override
    protected void validate() {
        if (!validateInput(input)) {
            throw new IllegalArgumentException("技術スキルは1から5の範囲でなければなりません。");
        }
        this.technicalSkill = Double.parseDouble(input); // 検証通過後にパース
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
