import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.logging.*;

/**
 * ログを取るクラス
 */
public class EmployeeInfoLogger {

    // ロガー
    private static final Logger LOGGER = Logger.getLogger("EmployeeInfoLogger");
    // ロガーのインスタンス
    private static EmployeeInfoLogger instance;
    // ログ保存先フォルダ
    private static final String LOG_FOLDER = "../EmployeeInfoApp/Log";

    /**
     * コンストラクタ
     */
    private EmployeeInfoLogger() {
        createLogFolder();
    }

    /**
     * インスタンスを返すメソッド
     * @return instance
     */
    public static EmployeeInfoLogger getInstance() {
        if (instance == null) {
            instance = new EmployeeInfoLogger();
        }
        return instance;
    }

    /**
     * ログ保存先フォルダを作るメソッド
     */
    public void createLogFolder() {
        try {
            // ログフォルダを作成
            Files.createDirectories(Paths.get(LOG_FOLDER));
            String logFile = LOG_FOLDER + "/EmployeeInfoApp-" + LocalDate.now() + ".log";
            FileHandler logHandler = new FileHandler(logFile, true);

            // ログのフォーマットを設定
            System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
            logHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(logHandler);
            
        } catch (IOException e) {
            // スタックトレースを文字列で取得してログに出力する
            logException(e);  // クラス図では引数が例外のみ。メッセージも渡す？
        }
    }

    /**
     * ログを出力するメソッド
     */
    public void logOutput(String message) {
        // ログファイル存在チェック　なければ作成、あればログを残す
    }

    /**
     * 例外を記録するメソッド
     */
    public void logException(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        // LOGGER.severe(String.format("%s¥n%s", errorString, sw.toString()));  💡ここ確認中！！
    }
}