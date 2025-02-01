public class ErrorHandler {
    private Logger logger; // Logger インスタンス


    public ErrorHandler(Logger logger) {
        this.logger = logger;
    }

    public void handleError(String errorMessage) {
        System.out.println("エラー: " + errorMessage); // エラーメッセージを表示
        logger.logOutput(errorMessage); // エラーメッセージをログに記録
    }
      public void logException(Exception exception) {
        // 例外をログに記録するロジックを実装
        logger.logOutput(exception.getMessage());
    }
}



/*役割: エラーメッセージを処理し、例外をログに記録。
 * アプリケーション内で発生するエラーや例外を一元管理し、適切な対応を行うための機能を提供
 * 
*/