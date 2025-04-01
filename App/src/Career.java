/**
 * 従業員の経歴情報を管理するクラス。
 * 経歴情報のバリデーションを行い、正しい場合に経歴情報を保持。
 */
class Career extends EmployeeInfoValidator {
    private String career;

    /**
     * 経歴情報を設定するコンストラクタ。
     * 経歴情報のバリデーションを行い、無効な場合は例外をスロー。
     *
     * @param career 経歴情報（500文字以内）
     * @throws IllegalArgumentException 経歴情報が無効な場合（500文字を超える場合）
     */
    public Career(String career) {
        if (!validateInput(career)) {
            throw new IllegalArgumentException("経歴情報は500文字以内で入力してください。");
        }
        this.career = career;
    }

    /**
     * 経歴情報のバリデーションを行う。
     * 経歴情報は1〜500文字以内。
     *
     * @param career 経歴情報
     * @return 経歴情報が有効かどうか（1〜500文字以内ならtrue）
     */
    @Override
    protected boolean validateInput(String career) {
        return validateLength(career, 1, 500);
    }

    /**
     * 経歴情報を取得するメソッド。
     *
     * @return 経歴情報
     */
    public String getCareer() {
        return career;
    }
}
