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
        EmployeeInfoLogger.logOutput("アプリを起動しました。"); // ログファイル存在確認
        // 💡ログフォルダのパスは変数ではなく定数に変更（LOG_FOLDER）

        EmployeeManager manager = new EmployeeManager(); // EmployeeManager初期化

        // サブスレッド内でデータ読み込み
        Thread threadListViewUI = new Thread(() -> {

            // 💡EmployeeManagerクラスの中でデータCSV存在チェック なければ作成、あればデータを読み込む
            // 💡EmployeeManagerクラスにデータ保存先のパス（DATA_FOLDER）、データCSVのファイル名（CSV_FILE）の定数を用意

        });

        threadListViewUI.start();

        try {
            threadListViewUI.join(); // サブスレッドの終了を待機
        } catch (InterruptedException e) {
            e.printStackTrace();  // サブスレッドに割り込みが入るとInterruptedExceptionエラーを吐くのでキャッチ
        }

        ListViewUI listView = new ListViewUI(); // ListViewUI初期化
    }
}