/**
 * 従業員のコミュニケーションスキル評価を管理するクラス。
 * コミュニケーションスキル評価を保持。
 */
class CommunicationSkill extends EmployeeInfoValidator {
    private double communicationSkill;

    /**
     * コンストラクタ
     * @param communicationSkill コミュニケーションスキル評価（評価値）
     * @throws IllegalArgumentException 評価が1〜5の範囲外の場合
     */
    public CommunicationSkill(double communicationSkill) {
        if (!validateInput(communicationSkill)) {
            throw new IllegalArgumentException("コミュニケーションスキル評価は1〜5の範囲でなければなりません。");
        }
        this.communicationSkill = communicationSkill;
    }

    /**
     * コミュニケーションスキル評価を検証するメソッド
     * @param communicationSkill コミュニケーションスキル評価（評価値）
     * @return コミュニケーションスキル評価が1〜5の範囲内かどうか
     */
    @Override
    protected boolean validateInput(double communicationSkill) {
        // コミュニケーションスキル評価は1〜5の範囲であることを確認
        return communicationSkill >= 1 && communicationSkill <= 5;
    }

    /**
     * コミュニケーションスキル評価を取得するメソッド
     * @return コミュニケーションスキル評価
     */
    public double getCommunicationSkill() {
        return communicationSkill;
    }
}