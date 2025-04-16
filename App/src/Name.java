/**
 * 名前を管理するクラス。
 * 名前のバリデーションを行い、正しい場合に名前を保持。
 */
public class Name extends EmployeeInfoValidator {

    // 従業員の名前を格納するフィールド
    private String name;

    /**
     * 名前を設定するコンストラクタ。
     * 名前のバリデーションを行い、無効な場合は例外をスロー。
     *
     * @param name 名前（従業員の名前）
     * @throws IllegalArgumentException 名前が無効な場合（1〜20文字の範囲内のみ許可）
     */
    public Name(String name) {
        // 名前が有効かどうかをチェック
        if (!validateInput(name)) {
            // 無効な場合は例外を投げる
            throw new IllegalArgumentException("名前は1〜20文字の範囲でのみ使用できます。");
        }
        // 名前が有効ならフィールドにセット
        this.name = name;
    }

    /**
     * 名前のバリデーションを行います。
     * 名前は1〜20文字以内のみ許可。
     *
     * @param name 名前（従業員の名前）
     * @return 名前が有効かどうか（1〜20文字の範囲の場合は true）
     */
    @Override
    protected boolean validateInput(String name) {
        // 長さチェック（1〜20文字）
        if (!validateLength(name, 1, 20)) {
            return false;
        } else {
            return true;
        }
    }
    /**
     * 名前を取得するメソッド。
     *
     * @return 従業員の名前
     */
    public String getName() {
        return name;
    }
}
