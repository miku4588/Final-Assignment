/**
 * 従業員IDを管理するクラス。
 * 従業員IDのバリデーションを行い、正しい場合にIDを保持。
 */
class EmployeeId extends EmployeeInfoValidator {
    private String employeeId;

    /**
     * 従業員IDを設定するコンストラクタ。
     * 従業員IDのバリデーションを行い、無効な場合は例外をスロー。
     *
     * @param employeeId 従業員ID（半角英数字のみ使用可能）
     * @throws IllegalArgumentException 従業員IDが無効な場合（半角英数字のみ使用可能）
     */
    public EmployeeId(String employeeId) {
        if (!validateInput(employeeId)) {
            throw new IllegalArgumentException("従業員IDは半角英数字のみ使用できます。");
        }
        this.employeeId = employeeId;
    }

    /**
     * 従業員IDのバリデーションを行う。
     * 従業員IDは半角英数字のみ使用可能。
     *
     * @param employeeId 従業員ID
     * @return 従業員IDが有効かどうか（半角英数字のみならtrue）
     */
    @Override
    protected boolean validateInput(String employeeId) {
        return validateCharacterType(employeeId, "ALPHANUMERIC");
    }

    /**
     * 従業員IDを取得するメソッド。
     *
     * @return 従業員ID
     */
    public String getEmployeeId() {
        return employeeId;
    }

    @Override
protected void validate() {
    if (!validateInput(employeeId)) {
        throw new IllegalArgumentException("従業員IDは半角英数字のみ使用できます。");
    }
}

}
