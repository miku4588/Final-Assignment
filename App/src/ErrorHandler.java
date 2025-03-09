import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * エラー処理をするクラス
 */
public class ErrorHandler {

    /**
     * エラーメッセージを処理するメソッド
     */
    public void handleError(String errorMessage){

    }
    
    /**
     * 例外を記録するメソッド
     */
    public static void logException(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        // logger.severe(String.format("%s¥n%s", errorString, sw.toString()));  💡ここ確認中！！
    }
}