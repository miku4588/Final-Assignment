import java.util.concurrent.TimeUnit;

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
        EmployeeInfoLogger.createLogFolder(); // ログフォルダの存在確認
        EmployeeInfoLogger.logOutput("ここにログの内容が入ります（考え中）"); // ログファイル存在チェック
        // 💡ログフォルダのパスは変数ではなく定数に変更（LOG_FOLDER）

        EmployeeManager manager = new EmployeeManager(); // EmployeeManager初期化
        // 💡データ読み込みもメソッド化した方がいいかも…EmployeeManagerクラスにメソッド追加しましょうか！！
        // 💡EmployeeManagerクラスの中でデータCSV存在チェック　なければ作成、あればデータを読み込む
        // 💡EmployeeManagerクラスにデータ保存先のパス（DATA_FOLDER）、データCSVのファイル名（CSV_FILE）の定数を用意

        // スレッドの内容　ListViewUI初期化
        Thread threadListViewUI = new Thread(() -> {
            ListViewUI listView = new ListViewUI(); // ListViewUI初期化
        });

        threadListViewUI.start();
        
    }
}