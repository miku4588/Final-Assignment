import java.nio.file.*;
import java.time.LocalDate;
import java.util.logging.FileHandler;

/**
 * ログを取るクラス
 */
public class EmployeeInfoLogger {

    /**
     * ログ保存先のフォルダ
     */
    private static final String LOG_FOLDER = "../EmployeeInfoApp/Log";

    /**
     * createLogFolderメソッド
     */
    public static void createLogFolder() {
        try {
            // ログフォルダを作成
            Files.createDirectories(Paths.get(LOG_FOLDER));
            String logFile = LOG_FOLDER + "/EmployeeInfoApp-" + LocalDate.now() + ".log";
            FileHandler logHandler = new FileHandler(logFile, true);

            // ログのフォーマットを設定
            // ログの利用方法をよく調べてチームの仕様に合わせて変更・対応してください
            System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
            logHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(logHandler);
        } catch (IOException e) {
            // スタックトレースを文字列で取得してログに出力する
            printLogStackTrace(e, "ログ設定で例外が発生しました");
        }
    }

    /**
     * logOutputメソッド
     */
    public static void logOutput(String message) {
        // ログファイル存在チェック　なければ作成、あればログを残す
    }
}
