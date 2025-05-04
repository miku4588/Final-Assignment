import java.util.List;

// 社員情報を追加する処理を行うクラス
public class EmployeeAdder {
    // CSVファイルの読み書きを行うためのヘルパークラス（外部から渡される）
    private CSVHandler csvHandler;

    // コンストラクタ：CSVHandlerを受け取って内部で使えるようにする
    public EmployeeAdder(CSVHandler csvHandler) {
        this.csvHandler = csvHandler;
    }

    /**
     * 社員情報を追加するメインの処理
     * @param newEmployee 追加したい社員情報（EmployeeInfo型）
     * @return true：正常に追加された/false：エラーがあった
     */
    public boolean addEmployee(EmployeeInfo newEmployee) {
        // CSVから現在の社員リストを読み込む
        List<EmployeeInfo> employeeList = csvHandler.readCSV();
        if (employeeList == null) {
            // 読み込みに失敗した場合はエラーメッセージを出して終了
            showError("社員情報の読み込みに失敗しました。");
            return false;
        }

        //  社員IDがすでに使われていないかチェック（ユニークチェック）
        if (!isEmployeeIdUnique(newEmployee.getEmployeeId(), employeeList)) {
            showError("この社員IDはすでに使われています。");
            return false;
        }

        // 必須項目のブランク（空文字）チェック
        if (isBlank(newEmployee.getEmployeeId()) ||  // 社員IDが空か？
            newEmployee.getName() == null ||         // 名前が未設定か？
            newEmployee.getPhonetic() == null) {     // フリガナが未設定か？
            showError("必須項目が入力されていません。");
            return false;
        }

        // ③ 問題がなければリストに追加してCSVに書き込み
        employeeList.add(newEmployee);
        csvHandler.writeCSV(employeeList);

        // 処理成功メッセージ（UIでは詳細画面に遷移する想定）
        System.out.println("保存完了！");
        return true;
    }

    // 社員IDが他の社員とかぶっていないかを確認するメソッド
    private boolean isEmployeeIdUnique(EmployeeId id, List<EmployeeInfo> employeeList) {
        for (EmployeeInfo e : employeeList) {
            if (e.getEmployeeId().equals(id)) {
                return false; // 同じIDが見つかったらユニークでない
            }
        }
        return true; // 同じIDがなければユニーク
    }

    // 文字列がnullまたは空白かをチェックするメソッド
    private boolean isBlank(EmployeeId input) {
        return input == null || EmployeeInfo.trim().isEmpty();
    }

    // エラーメッセージを出力する（UIではダイアログに変更可能）
    private void showError(String message) {
        System.out.println("エラー: " + message);
        // UIなら JOptionPane.showMessageDialog(null, message); にしてもOK
    }
}
