import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Mainクラス
 */
public class MainApp {
    // ロガーを初期化
    private static final EmployeeInfoLogger LOGGER = EmployeeInfoLogger.getInstance();
    // データ保存先フォルダ
    private static final String DATA_FOLDER = "data";
    // データCSV
    public static final String DATA_FILE = DATA_FOLDER + "/EmployeeInfo.csv";
    // 社員データを管理するインスタンス
    public static EmployeeManager manager = new EmployeeManager(new ArrayList<>()); 
    // ロック用のオブジェクト
    private static final Object LOCK = new Object();

    /**
     * Mainメソッド
     * 
     * @param args
     */
    public static void main(String[] args) {
        LOGGER.logOutput("アプリを起動しました。");

        // サブスレッド内でデータ読み込み
        Thread threadLoadData = new Thread(() -> {
            loadData();
        }, "DataLoader");

        threadLoadData.start();

        // サブスレッドの終了を待機
        // サブスレッドに割り込みが入るとエラーを吐くのでキャッチ
        try {
            threadLoadData.join();
        } catch (InterruptedException e) {
            LOGGER.logException("データ読み込み処理中に割り込みが発生しました。", e);
            ErrorHandler.handleError("データ読み込み処理中に割り込みが発生しました。");
        }

        ListViewUI listView = new ListViewUI(manager); // ListViewUI初期化
    }

    private static void loadData() {
        // ロックを取得
        synchronized (LOCK) {
            try {
                Files.createDirectories(Paths.get(DATA_FOLDER)); // createDirectories…対象のフォルダが既存の場合、作成されない
                File file = new File(DATA_FILE);

                // createNewFile…ファイル作成が成功したらtrue、ファイルが既存ならfalseを返す
                if (file.createNewFile()) { 
                    LOGGER.logOutput("データファイルを新規作成しました。");
                } else {
                    CSVHandler csvHandler = new CSVHandler(DATA_FILE);
                    List<EmployeeInfo> employeeList = csvHandler.readCSV();
                    if(employeeList != null) {
                        manager.setEmployeeList(employeeList);
                    }
                }
            } catch (IOException e) {
                LOGGER.logException("データフォルダまたはデータファイルの作成に失敗しました。", e);
                ErrorHandler.handleError("データフォルダまたはデータファイルの作成に失敗しました。");
            }
        }
    }
}