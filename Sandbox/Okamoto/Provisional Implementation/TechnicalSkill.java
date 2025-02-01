public class TechnicalSkill {
    // フィールド
    private int technicalSkill; // 技術スキルの評価（1-5の範囲）

    // コンストラクタ
    public TechnicalSkill(int technicalSkill) {
        if (technicalSkill < 1 || technicalSkill > 5) {
            throw new IllegalArgumentException("技術スキルは1から5の範囲でなければなりません。");
        }
        this.technicalSkill = technicalSkill;
    }

    // ゲッターメソッド
    public int getTechnicalSkill() {
        return technicalSkill;
    }
}
