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
    public CommunicationSkill(String communicationSkill) {
        if (!validateInput(communicationSkill)) {
            throw new IllegalArgumentException("コミュニケーションスキル評価は1〜5の範囲でなければなりません。");
        }
        this.communicationSkill = Double.parseDouble(communicationSkill); // Stringをdoubleに変換
    }

    /**
     * コミュニケーションスキル評価を検証するメソッド
     * @param communicationSkill コミュニケーションスキル評価（評価値）
     * @return コミュニケーションスキル評価が1〜5の範囲内かどうか
     */
    @Override
    protected boolean validateInput(String communicationSkill) {
        try {
            double value = Double.parseDouble(communicationSkill); // Stringをdoubleに変換
            return value >= 1 && value <= 5; // 範囲をチェック
        } catch (NumberFormatException e) {
            return false; // 数値変換に失敗した場合は無効とする
        }
    }

    /**
     * コミュニケーションスキル評価を取得するメソッド
     * @return コミュニケーションスキル評価
     */
    public double getCommunicationSkill() {
        return communicationSkill;
    }
}

