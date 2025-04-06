import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.logging.FileHandler;

/**
 * Mainクラス
 */
public class MainApp {
    // データ保存先フォルダ
    private static final String DATA_FOLDER = "App/data";
    // データCSV
    private static final String DATA_FILE = DATA_FOLDER + "/EmployeeInfo.csv";

    /**
     * Mainメソッド
     * @param args
     */
    public static void main(String[] args) {
        
        // ロガーを初期化
        EmployeeInfoLogger logger = EmployeeInfoLogger.getInstance();
        logger.logOutput("アプリを起動しました。");

        // データリスト初期化
        EmployeeManager manager = new EmployeeManager();

        // サブスレッド内でデータ読み込み
        Thread threadLoadData = new Thread(() -> {
            // try-catchの前にロックを取得
            try {
                // データCSV保存フォルダとCSVファイル存在確認
                // createDirectories…対象のフォルダが既存の場合、作成されない
                // FileHandler…対象のファイルが既存の場合、作成されない？？？
                Files.createDirectories(Paths.get(DATA_FOLDER));
                FileHandler fileHandler = new FileHandler(DATA_FILE);
                CSVHandler.readCSV(DATA_FILE);
            } catch (IOException e) {
                logger.logException(e);
            }
            // ロックを開放　成功か失敗か返す
        }, "DataLoader");

        threadLoadData.start();

        // サブスレッドの終了を待機
        try {
            threadLoadData.join(10000);
        } catch (Exception e) {
            logger.logException(e); // サブスレッドに割り込みが入るとエラーを吐くのでキャッチ
        }

        ListViewUI listView = new ListViewUI(); // ListViewUI初期化
    }
}












// サブスレッドの終了を完全に待機できるよう修正する！！！
// エラー時、logExceptionしか動かないようになってるからエラーハンドラーも書く！！！！！！！
// データ読み込みのメソッドも書く！！！！！！！！！！