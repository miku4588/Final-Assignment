import java.util.List;


// 社員情報を追加する処理を行うクラス
public class EmployeeAdder {
    // ロガー取得
    private static final EmployeeInfoLogger LOGGER = EmployeeInfoLogger.getInstance();

    /**
     * 社員情報を追加するメインの処理
     * 
     * @param newEmployee 追加したい社員情報（EmployeeInfo型）
     * @return true：正常に追加された/false：エラーがあった
     */

    public static boolean addEmployee(EmployeeInfo newEmployee) {
        LOGGER.logOutput("社員ID：" + newEmployee.getEmployeeId() + "　" + newEmployee.getName() + "の社員情報を追加します。");
        // 現在の社員リストを取得
        List<EmployeeInfo> employeeList = EmployeeManager.getEmployeeList();
        if (employeeList == null) {
            // 読み込みに失敗した場合はエラーメッセージを出して終了
            LOGGER.logOutput("社員情報の読み込みに失敗しました。");
            ErrorHandler.showErrorDialog("社員情報の読み込みに失敗しました。");
            return false;
        }

        // 社員IDがすでに使われていないかチェック（ユニークチェック）
        if (!isEmployeeIdUnique(newEmployee.getEmployeeId(), employeeList)) {
            LOGGER.logOutput("この社員IDはすでに使われています。");
            ErrorHandler.showErrorDialog("この社員IDはすでに使われています。");
            return false;
        }

        // 必須項目のブランク（空文字）チェック
        if (isBlank(newEmployee.getEmployeeId()) || // 社員IDが空か？
                newEmployee.getName() == null || // 名前が未設定か？ // 💡名前は未入力可です
                newEmployee.getPhonetic() == null) { // フリガナが未設定か？ // 💡フリガナも未入力可です
            ErrorHandler.showErrorDialog("必須項目が入力されていません。");
            return false;
        }

        // 問題がなければリストに追加してCSVに書き込み
        CSVHandler.writeCSV(newEmployee, true);

        // 処理成功メッセージ（UIでは詳細画面に遷移する想定）
        LOGGER.logOutput("社員ID：" + newEmployee.getEmployeeId() + "　" + newEmployee.getName() + "の情報を追加しました。");
        return true;
    }

    // 社員IDが他の社員とかぶっていないかを確認するメソッド
    private static boolean isEmployeeIdUnique(String id, List<EmployeeInfo> employeeList) {
        for (EmployeeInfo e : employeeList) {
            if (e.getEmployeeId().equals(id)) {
                return false; // 同じIDが見つかったらユニークでない
            }
        }return true;
    }

    // 文字列がnullまたは空白かをチェックするメソッド
    private static boolean isBlank(String input) {
        return input == null || input.isEmpty();
}
}
