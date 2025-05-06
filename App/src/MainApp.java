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
    private static final String DATA_FILE = DATA_FOLDER + "/EmployeeInfo.csv";
    // ロック用のオブジェクト
    private static final Object LOCK = new Object();

    /**
     * Mainメソッド
     * 
     * @param args
     */
    public static void main(String[] args) {
        LOGGER.logOutput("アプリを起動しました。");
        
        // 💡今はここで初期化してますがメンバとして定数で持ってた方がよさそうなら修正します。
        EmployeeManager manager = new EmployeeManager(new ArrayList<>()); 

        // サブスレッド内でデータ読み込み
        Thread threadLoadData = new Thread(() -> {
            loadData(manager);
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

        /////////デバッグ用/////////
        System.out.println();
        System.out.println("メインクラス　読み込んだデータ");
        System.out.println(manager.getEmployeeList());
        System.out.println("読み込んだデータここまで");
        System.out.println();
        System.out.println("読み込んだデータの0個目の要素の氏名は…");
        System.out.println(manager.getEmployeeList().get(0).getName());
        /////////デバッグ用おわり/////////

        ListViewUI listView = new ListViewUI(manager); // ListViewUI初期化
        CSVUI csvUI = new CSVUI();
    }

    private static void loadData(EmployeeManager manager) {
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
                    if(employeeList.isEmpty()) {
// 💡💡💡💡💡社員がひとりもいない場合の処理考え中
                    } else {
                        manager.setEmployeeList(employeeList);
                    }
                }
            } catch (IOException e) {
                LOGGER.logException("データフォルダまたはデータファイルの作成に失敗しました。", e);
                // 💡エラーハンドラーも呼ぶ
            }
        }
    }
}