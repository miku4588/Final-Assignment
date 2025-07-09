import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 従業員情報を検証するための抽象クラス。
 * サブクラスはこのクラスを継承して、具体的なバリデーションのロジックを実装する。
 */
public abstract class EmployeeInfoValidator {

    /**
     * 文字列の長さを検証するメソッド。
     * 
     * @param value 検証対象の文字列
     * @param minLength 最小長さ
     * @param maxLength 最大長さ
     * @return 文字列が指定された長さの範囲内であれば true、それ以外は false
     */
    public boolean validateLength(String value, int minLength, int maxLength) {
        // 文字列がnullでなく、指定された長さの範囲内に収まっているかをチェック
        return value != null && value.length() >= minLength && value.length() <= maxLength;
    }

    /**
     * 文字列の文字種を検証するメソッド。
     * 
     * @param value 検証対象の文字列
     * @param type 検証する文字種のタイプ（"ALPHANUMERIC" または "KANA"）
     * @return 文字列が指定された文字種であれば true、それ以外は false
     */
    protected boolean validateCharacterType(String value, String type) {
        // 文字列がnullの場合、falseを返す
        if (value == null) return false;

        // 指定された文字種に基づき、正規表現を使って文字列を検証
        switch (type) {
            case "ALPHANUMERIC":
                // 英数字のみを許可
                return value.matches("^[a-zA-Z0-9]+$");
            case "KANA":
                // 全角カナのみを許可
                return value.matches("^[ァ-ヶー]+$");
            default:
                return false; // 不正なタイプの場合はfalseを返す
        }
    }

    /**
     * 文字列の形式を検証するメソッド。
     * 
     * @param value 検証対象の文字列
     * @param format 文字列の形式（"YYYY/MM/DD"など）
     * @return 文字列が指定された形式に合致する場合は true、それ以外は false
     */
    protected  boolean validateFormat(String value, String format) {
        // 形式が "YYYY/MM/DD" の場合
        if ("YYYY/MM/DD".equals(format)) {
            try {
                // 文字列を日付としてパースして形式が正しいかチェック
                LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                return true;
            } catch (Exception e) {
                // パースに失敗した場合はfalseを返す
                return false;
            }
        }
        return false; // 他の形式については未対応
    }

    /**
     * デフォルトの文字列検証メソッド。
     * 空文字またはnullの文字列は無効として扱う。
     * 
     * @param value 検証対象の文字列
     * @return 文字列がnullでなく、空でなければ true、それ以外は false
     */
    protected boolean validateInput(String value) {
        // 文字列がnullまたは空白のみの場合は無効とする
        return value != null && !value.trim().isEmpty();
    }

    

    /**
     * double 型の技術スキルを検証するメソッド。
     * 技術スキルは1から5の範囲であることを確認する。
     * 
     * @param value 検証対象の技術スキル評価（double型）
     * @return 技術スキルが1〜5の範囲内であれば true、それ以外は false
     */
    protected  boolean validateInput(double value) {
        // 技術スキルが1以上5以下の範囲であることをチェック
        return value >= 1 && value <= 5;
    }
}
