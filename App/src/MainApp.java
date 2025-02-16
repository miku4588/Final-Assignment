/**
 * Mainクラス
 */
public class MainApp {

    /**
     * Mainメソッド
     * @param args
     */
    public static void main(String[] args) {
        
        // EmployeeInfoLoggerクラスのメソッドを呼び出す
        // createLogFolderメソッド…ログフォルダの存在確認　なければ作成、あれば何もしない
        // logOutputメソッド…ログファイル存在チェック　なければ作成、あれば何もしない
        // 💡ログフォルダのパスは変数ではなく定数に変更（LOG_FOLDER）

        EmployeeManager manager = new EmployeeManager(); // EmployeeManager初期化
        // 💡データ読み込みもメソッド化した方がいいかも…EmployeeManagerクラスにメソッド追加しましょうか！！
        // 💡EmployeeManagerクラスの中でデータCSV存在チェック　なければ作成、あればデータを読み込む
        // 💡EmployeeManagerクラスにデータ保存先のパス（DATA_FOLDER）、データCSVのファイル名（CSV_FILE）の定数を用意

        ListViewUI listView = new ListViewUI(); // ListViewUI初期化
        // 💡ListViewUIの中にはメインメソッド置かずにここで初期化
        // 💡ListViewUIのコンストラクタの中でdisplayEmployeesメソッドを呼び、画面を表示させる
    }
}
