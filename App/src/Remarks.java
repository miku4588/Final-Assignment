
/**
 * 従業員に関する備考情報を管理するクラス。
 * 備考情報のバリデーションを行い、正しい場合に備考情報を保持。
 */
public class Remarks extends EmployeeInfoValidator {

    //フィールド
    private String remarks;

    /**
     * 備考情報を設定するコンストラクタ。
     * 備考情報のバリデーションを行い、無効な場合は例外をスロー。
     *
     * @param remarks 備考情報（500文字以内）
     * @throws IllegalArgumentException 備考情報が無効な場合（500文字を超える場合）
     */
    public Remarks(String remarks) {
        if (!validateInput(remarks)) {
            throw new IllegalArgumentException("備考は500文字以内で入力してください。");
        }
        this.remarks = remarks;
    }

    /**
     * 備考情報のバリデーションを行う。
     * 備考情報は1〜500文字以内。
     *
     * @param remarks 備考情報
     * @return 備考情報が有効かどうか（1〜500文字以内ならtrue）
     */
    @Override
    protected boolean validateInput(String remarks) {
        return validateLength(remarks, 0, 500);
    }

    /**
     * 備考情報を取得するメソッド。
     *
     * @return 備考情報
     */
    public String getRemarks() {
        return remarks;
    }
}
