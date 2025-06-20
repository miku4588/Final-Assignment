/**
 * 名前を管理するクラス。
 * 名前のバリデーションを行い、正しい場合に名前を保持。
 */
public class Name extends EmployeeInfoValidator {

    private String name;
    private String input;

    public Name(String input) {
        this.input = input;
    }

    /**
     * validate() を実装し、バリデーション処理を実行。
     */
    @Override
    protected void validate() {
        if (!validateInput(input)) {
            throw new IllegalArgumentException("名前は1〜20文字の範囲で入力してください。");
        }
        this.name = input;
    }

    /**
     * 入力された名前が1〜20文字かチェック。
     */
    @Override
    protected boolean validateInput(String name) {
        return validateLength(name, 1, 20);
    }

    @Override
    protected boolean validateLength(String value, int min, int max) {
        return value != null && value.length() >= min && value.length() <= max;
    }

    public String getName() {
        return name;
    }
}
