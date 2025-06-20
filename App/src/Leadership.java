/**
 * 従業員のリーダーシップ評価を管理するクラス。
 * リーダーシップ評価を保持。
 */
class Leadership extends EmployeeInfoValidator {
    private double leadership;

    public Leadership(String leadership) {
        if (leadership == null) {
            throw new IllegalArgumentException("リーダーシップ評価は必須です。");
        }
        if (!validateInput(leadership)) {
            throw new IllegalArgumentException("リーダーシップ評価は1〜5の範囲でなければなりません。");
        }
        this.leadership = Double.parseDouble(leadership);
        validate();  // 追加：全体バリデーション（必要に応じて拡張）
    }

    @Override
    protected boolean validateInput(String leadership) {
        try {
            double value = Double.parseDouble(leadership);
            return value >= 1 && value <= 5;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    protected void validate() {
        // 今は特に追加バリデーションなし。
        // 例えば将来的に範囲外エラーの再チェックなどを入れる場合はこちらに。
    }

    public double getLeadership() {
        return leadership;
    }

    @Override
    protected boolean validateLength(String value, int min, int max) {
        // 評価値は数値なので文字数チェックは不要かもですが、一応オーバーライド
        return value != null && value.length() >= min && value.length() <= max;
    }
}
