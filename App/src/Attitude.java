/**
 * 従業員の態度評価を管理するクラス。
 * 態度評価を保持。
 */
class Attitude extends EmployeeInfoValidator {
    private double attitude;

    /**
     * コンストラクタ
     * @param attitude 態度評価（評価値）
     * @throws IllegalArgumentException 評価が1〜5の範囲外の場合
     */
    public Attitude(double attitude) {
        if (!validateInput(attitude)) {
            throw new IllegalArgumentException("態度評価は1〜5の範囲でなければなりません。");
        }
        this.attitude = attitude;
    }

    /**
     * 態度評価を検証するメソッド
     * @param attitude 態度評価（評価値）
     * @return 態度評価が1〜5の範囲内かどうか
     */
    @Override
    protected boolean validateInput(double attitude) {
        // 態度評価は1〜5の範囲であることを確認
        return attitude >= 1 && attitude <= 5;
    }

    /**
     * 態度評価を取得するメソッド
     * @return 態度評価
     */
    public double getAttitude() {
        return attitude;
    }
}