/**
 * 従業員の態度評価を管理するクラス。
 * 態度評価を保持。
 */
class Attitude extends EmployeeInfoValidator {
    private double attitude;
    private String input;

    /**
     * コンストラクタ
     * @param attitude 態度評価（文字列で受け取る）
     */
    public Attitude(String attitude) {
        this.input = attitude;
    }

    /**
     * validate: 値の検証と変換処理を行う（抽象メソッドの実装）
     */
    @Override
    protected void validate() {
        if (!validateInput(input)) {
            throw new IllegalArgumentException("態度評価は1〜5の範囲でなければなりません。");
        }
        this.attitude = Double.parseDouble(input); // バリデーション成功後に変換
    }

    /**
     * 入力文字列が1〜5の範囲内かチェック
     */
    @Override
    protected boolean validateInput(String attitude) {
        try {
            double value = Double.parseDouble(attitude);
            return value >= 1 && value <= 5;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 態度評価の数値を取得
     */
    public double getAttitude() {
        return attitude;
    }
}
