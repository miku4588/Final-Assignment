public class Validation {

    /**
     * バリデーションチェック
     * @param employeeId 社員ID
     * @param name 氏名
     * @return trueならバリデーションチェックOK
     * @version 1.0.0
     */
    public static boolean validateData(String employeeId, String name) {
        return validateEmployeeId(employeeId) && validateName(name);
    }

    /**
     * 社員IDバリデーションチェック
     * @param employeeId 社員ID
     * @return trueならバリデーションチェックOK
     * @version 1.0.0
     */
    public static boolean validateEmployeeId(String employeeId) {
        String regex_AlphaNum = "^[A-Za-z0-9]+$" ; // 半角英数字の正規表現
        return true;
    }
}
