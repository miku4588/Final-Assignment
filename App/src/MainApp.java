import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Mainクラス
 */
public class MainApp {
    // データ保存先フォルダ
    private static final String DATA_FOLDER = "App/data";
    // データCSV
    private static final String DATA_FILE = DATA_FOLDER + "/EmployeeInfo.csv";
    // ロック用のオブジェクト
    private static final Object LOCK = new Object();

    /**
     * Mainメソッド
     * 
     * @param args
     */
    public static void main(String[] args) {

        // ロガーを初期化
        EmployeeInfoLogger logger = EmployeeInfoLogger.getInstance();
        logger.logOutput("アプリを起動しました。");

        // EmployeeManagerを初期化
        EmployeeManager manager = new EmployeeManager(new ArrayList<>()); // EmployeeManagerを空のデータで初期化

        // サブスレッド内でデータ読み込み
        Thread threadLoadData = new Thread(() -> {
            // ロックを取得
            synchronized (LOCK) {
                try {
                    Files.createDirectories(Paths.get(DATA_FOLDER)); // createDirectories…対象のフォルダが既存の場合、作成されない
                    File file = new File(DATA_FILE);

                    // createNewFile…ファイル作成が成功したらtrue、ファイルが既存ならfalseを返す
                    if (file.createNewFile()) { 
                        logger.logOutput("データファイルを新規作成しました。");
                    } else {
                        List<EmployeeInfo> employeeList = CSVHandler.readCSV(DATA_FILE);
                        manager.setEmployeeList(employeeList);
                    }
                } catch (IOException e) {
                    logger.logException(e);
                    // 💡エラーハンドラーも呼ぶ
                }
            }
        }, "DataLoader");

        threadLoadData.start();

        // サブスレッドの終了を待機
        // サブスレッドに割り込みが入るとエラーを吐くのでキャッチ
        try {
            threadLoadData.join(10000);
        } catch (Exception e) {
            logger.logException(e);
            // 💡エラーハンドラーも呼ぶ
        }

        /////////デバッグ用/////////
        System.out.println();
        System.out.println("メインクラス　読み込んだデータ");
        System.out.println(manager.getEmployeeList());
        System.out.println("読み込んだデータここまで");
        System.out.println();
        /////////デバッグ用おわり/////////

        ListViewUI listView = new ListViewUI(); // ListViewUI初期化
    }
}