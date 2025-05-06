import java.awt.Dimension;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * エラー処理をするクラス
 */
public class ErrorHandler {

    /**
     * エラーメッセージを処理するメソッド
     */
    public static void handleError(String errorMessage){

        // 画面に関する処理はinvokeLaterで囲むのが安全
        SwingUtilities.invokeLater(() -> {

            // マウスでコピー可能なテキストを設定
            JTextArea textArea = new JTextArea(errorMessage);
            textArea.setEditable(false); // 編集を不可にする
            textArea.setLineWrap(true);  // 自動改行
            textArea.setWrapStyleWord(true); // 単語単位で改行
            textArea.setCaretPosition(0); // 初期位置を先頭に設定
    
            // スクロール可能なペインを設定
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 200));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    
            // ダイアログを表示
            JOptionPane.showMessageDialog(null, scrollPane, "エラー", JOptionPane.ERROR_MESSAGE);
        });
    }
}