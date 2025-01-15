import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private String logFolder;

    public Logger(String logFolder) {
        this.logFolder = logFolder;
        createLogFolder();
    }

    public void createLogFolder() {
        File folder = new File(logFolder);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    public void logOutput(String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String logMessage = timestamp + " - " + message;
        writeLog(logMessage);
    }

    public void logException(Exception exception) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String logMessage = timestamp + " - ERROR: " + exception.getMessage();
        writeLog(logMessage);
    }

    private void writeLog(String logMessage) {
        try (FileWriter writer = new FileWriter(
                logFolder + "/EmployeeManagementApp-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".log",
                true)) {
            writer.write(logMessage + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/* 役割: ログ管理を行い、操作の結果や例外を記録 */

/*
 * logOutput メソッドでは、引数として渡されたメッセージをログに記録。
 * 現在の日時を yyyy-MM-dd HH:mm:ss 形式で取得し、ログメッセージにタイムスタンプを付加。
 * BufferedWriter（バッファードライター） を使用して、指定されたファイルに追記モードでメッセージを書き込む。
 */