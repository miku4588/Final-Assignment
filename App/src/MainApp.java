import java.util.logging.Logger;

/**
 * Mainクラス
 */
public class MainApp {

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
            // ロックを取得
            // データCSVフォルダ存在確認　フォルダがなければ作成
            // データCSV存在確認　CSVがなければ作成
            // 1行ずつデータを読み込む（読み込みの際にバリデーションチェックなど実施）
            // ロックを開放　成功か失敗か返す
        }, "DataLoader");

        threadLoadData.start();

        // サブスレッドの終了を待機
        try {
            threadLoadData.join();
        } catch (InterruptedException e) {
            logger.logException(e); // サブスレッドに割り込みが入るとInterruptedExceptionエラーを吐くのでキャッチ
        }

        ListViewUI listView = new ListViewUI(); // ListViewUI初期化
    }
}