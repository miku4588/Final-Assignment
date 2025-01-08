import java.util.Date;

public class Validation {
    // Loggerのインスタンスを作成
    private static final Logger logger = new Logger("path/to/log/folder"); // 適切なパスを指定
    private static final ErrorHandler errorHandler = new ErrorHandler(logger);

    // EmployeeInfoのバリデーションを行うメソッド
    public static boolean validateEmployeeInfo(EmployeeInfo emp) {
        if (emp == null) {
            errorHandler.handleError("従業員情報が未入力です。"); // エラーメッセージを処理
            return false; // empがnullの場合は無効
        }

        // 各属性のバリデーションを行う
        if (!validateName(emp.getName().getName())) {
            errorHandler.handleError("名前が無効です。"); // エラーメッセージを処理
            return false;
        }
        if (!validateEmployeeId(emp.getEmployeeId().getEmployeeId())) {
            errorHandler.handleError("従業員IDが無効です。"); // エラーメッセージを処理
            return false;
        }
        if (!validateBirthDate(emp.getBirthDate().getBirthDate())) {
            errorHandler.handleError("生年月日が無効です。"); // エラーメッセージを処理
            return false;
        }
        if (!validateJoinDate(emp.getJoinDate().getJoinDate())) {
            errorHandler.handleError("入社日が無効です。"); // エラーメッセージを処理
            return false;
        }
        if (!validateCareer(emp.getCareer().getCareer())) {
            errorHandler.handleError("経歴情報が無効です。"); // エラーメッセージを処理
            return false;
        }
        if (!validateTrainingHistory(emp.getTrainingHistory().getTrainingHistory())) {
            errorHandler.handleError("研修履歴が無効です。"); // エラーメッセージを処理
            return false;
        }

        return true; // 全てのバリデーションが通過
    }

    public static boolean validateName(String name) {
        return name != null && name.length() <= 20; // 名前が20文字以内であることを確認
    }

    public static boolean validateEmployeeId(String employeeId) {
        return employeeId != null && employeeId.matches("[a-zA-Z0-9]+"); // 半角英数字であることを確認
    }

    public static boolean validateBirthDate(Date birthDate) {
        if (birthDate == null) {
            errorHandler.handleError("生年月日が未入力です。"); // エラーメッセージを処理
            return false; // nullチェック
        }
        return !birthDate.after(new Date()); // 生年月日が未来の日付でないことを確認
    }

    public static boolean validateJoinDate(Date joinDate) {
        if (joinDate == null) {
            errorHandler.handleError("入社日が未入力です。"); // エラーメッセージを処理
            return false; // nullチェック
        }
        return !joinDate.after(new Date()); // 入社日が未来の日付でないことを確認
    }

    public static boolean validateCareer(String career) {
        return career != null && career.length() <= 500; // 500文字以内であることを確認
    }

    public static boolean validateTrainingHistory(String trainingHistory) {
        return trainingHistory != null && trainingHistory.length() <= 500; // 500文字以内であることを確認
    }
}
