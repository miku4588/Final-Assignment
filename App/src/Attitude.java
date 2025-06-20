/**
 * 従業員の態度評価を管理するクラス。
 * 態度評価を保持。
 */
class Attitude extends EmployeeInfoValidator {
    private double attitude;
    private String input;

    /**
     * コンストラクタ
     * @param input 態度評価（文字列で受け取る）
     */
    public Attitude(String input) {
        this.input = input;
    }

    /**
     * 入力文字列が1〜5の範囲内かチェック
     */
    @Override
    protected boolean validateInput(String hoge) {
        try {
            attitude = Double.parseDouble(input);
            return attitude >= 1 && attitude <= 5;
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
