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
    public Attitude(String attitude) {
        if (!validateInput(attitude)) {
            throw new IllegalArgumentException("態度評価は1〜5の範囲でなければなりません。");
        }
        this.attitude = Double.parseDouble(attitude); // Stringをdoubleに変換
    }

    /**
     * 態度評価を検証するメソッド
     * @param attitude 態度評価（評価値）
     * @return 態度評価が1〜5の範囲内かどうか
     */
    @Override
    protected boolean validateInput(String attitude) {
        try {
            double value = Double.parseDouble(attitude); // Stringをdoubleに変換
            return value >= 1 && value <= 5; // 範囲をチェック
        } catch (NumberFormatException e) {
            return false; // 数値変換に失敗した場合は無効とする
        }
    }

    /**
     * 態度評価を取得するメソッド
     * @return 態度評価
     */
    public double getAttitude() {
        return attitude;
    }
}
