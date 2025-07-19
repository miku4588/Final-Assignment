/**
 * 名前を管理するクラス。
 * 名前のバリデーションを行い、正しい場合に名前を保持。
 */
public class Name extends EmployeeInfoValidator {

    private String name;

    public Name(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("氏名は必須です");
        }
        if (!validateInput(name)) {
            throw new IllegalArgumentException("氏名は0〜20文字の範囲でのみ使用できます。");
        }
        this.name = name;
    }

    /**
     * 入力された名前が1〜20文字かチェック。
     */
    @Override
    protected boolean validateInput(String name) {
        return validateLength(name, 0, 20);
    }

    public String getName() {
        return name;
    }
}
