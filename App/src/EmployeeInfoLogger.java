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
    private static final String LOG_FOLDER = "App/log";

    /**
     * コンストラクタ
     */
    private EmployeeInfoLogger() {
        createLogFolder();
    }

    /**
     * インスタンスを返すメソッド
     * 
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
            // ログフォルダとログファイルを作成
            // createDirectories…対象のフォルダが既存の場合、作成されない
            // FileHandler…対象のファイルが既存の場合、作成されない
            Files.createDirectories(Paths.get(LOG_FOLDER));
            String logFile = LOG_FOLDER + "/EmployeeInfoApp-" + LocalDate.now() + ".log";
            FileHandler logHandler = new FileHandler(logFile, true);

            // ログのフォーマットを設定
            System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %5$s %n");
            logHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(logHandler);

        } catch (IOException e) {
            logException(e); 
        }
    }

    /**
     * ログを出力するメソッド
     */
    public void logOutput(String message) {
        // スタックトレースを取得
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // スタックトレースからクラス名とメソッド名を取得（要素の3番目以降にある）
        String className = stackTrace[2].getClassName();
        String methodName = stackTrace[2].getMethodName();

        // String.formatメソッドでStringのフォーマットを変えて渡す
        // %sが3つ…うしろの3つの引数が並んだかたちになる
        LOGGER.info(String.format("%s [%s.%s]", message, className, methodName));
    }

    /**
     * 例外を記録するメソッド
     */
    public void logException(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        LOGGER.severe(String.format("%s%n%s", "例外が発生しました。", sw.toString()));
    }
}