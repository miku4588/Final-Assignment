import java.io.File;
import java.io.FileOutputStream;
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
    private static final String DATA_FOLDER = System.getProperty("user.dir") + File.separator + "data";
    // データCSV
    public static final String DATA_FILE = DATA_FOLDER + File.separator + "EmployeeInfo.csv";

    /**
     * Mainメソッド
     * 
     * @param args
     */
    public static void main(String[] args) {
        LOGGER.logOutput("アプリを起動しました。");

        // サブスレッド内でデータ読み込み
        Thread threadLoadData = new Thread(() -> {
            loadEmployeeInfoCSV();
        }, "DataLoader");

        threadLoadData.start();

        // サブスレッドの終了を待機
        // サブスレッドに割り込みが入るとエラーを吐くのでキャッチ
        try {
            threadLoadData.join();
        } catch (InterruptedException e) {
            LOGGER.logException("データ読み込み処理中に割り込みが発生しました。", e);
            ErrorHandler.showErrorDialog("データ読み込み処理中に割り込みが発生しました。");
        }

        // ListViewUIを初期化
        // EmployeeManagerがnullの場合はListViewUIを出さない
        try {
            new ListViewUI(EmployeeManager.getInstance());
        } catch (IllegalStateException e) {
            LOGGER.logOutput("データが不正のため一覧画面の表示を中止します。");
        }
    }

    private static void loadEmployeeInfoCSV() {

        // createDirectoriesとcreateNewFileはIOExceptionが出る場合がある
        try {

            // データ保存先フォルダの作成
            // createDirectories…対象のフォルダが既存の場合、作成されない
            Files.createDirectories(Paths.get(DATA_FOLDER));
            
            // データCSVファイルの作成
            // createNewFile…ファイル作成が成功したらtrue、ファイルが既存ならfalseを返す
            File file = new File(DATA_FILE);
            if (file.createNewFile()) {
                LOGGER.logOutput("データファイルを新規作成しました。");

                // BOM付きにする
                FileOutputStream bomWrinter = new FileOutputStream(file);
                bomWrinter.write(new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF});
                bomWrinter.close();

                // データがまだないため空のリストで初期化
                List<EmployeeInfo> employeeList = new ArrayList<>();
                EmployeeManager.initializeEmployeeManager(employeeList); 
            } else {
                CSVHandler csvHandler = new CSVHandler(DATA_FILE);
                List<EmployeeInfo> employeeList = csvHandler.readCSV(true); // エラーの場合、nullを返す

                // リストがnullならエラーなので、データを読み込まない
                if(employeeList == null) {
                    LOGGER.logOutput("データが不正のため読み込み処理を中止します。");
                } else {
                    EmployeeManager.initializeEmployeeManager(employeeList);
                }
            }
        } catch (IOException e) {
            LOGGER.logException("データフォルダまたはデータファイルの作成に失敗しました。", e);
            ErrorHandler.showErrorDialog("データフォルダまたはデータファイルの作成に失敗しました。");
        }
    }
}