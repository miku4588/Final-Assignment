import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// JFrameを継承する
public class CSVUI extends JDialog {
    // ロガーを取得
    private static final EmployeeInfoLogger LOGGER = EmployeeInfoLogger.getInstance();
    
    // CSV読み込み画面
    private JButton templateButton = new JButton("テンプレートをダウンロード");
    private JTextField filePathField = new JTextField(20); // ファイルパス表示フィールド
    private JButton selectButton = new JButton("ファイル選択");
    private JButton importButton = new JButton("読み込む");
    
    // 読み込み中・保存中メッセージ
    private JDialog processingDialog = new JDialog(this, Dialog.ModalityType.APPLICATION_MODAL); // APPLICATION_MODAL…自分以外の全てのウィンドウの操作をブロック

    // 確認ダイアログ
    private JDialog confirmDialog = new JDialog(this, "確認", true); // true…親ウィンドウの操作をブロック
    private JButton confirmButton = new JButton("確定");
    private JButton backButton = new JButton("戻る");

    // ロック用のオブジェクト
    private static final Object LOCK = new Object();


    /**
     * コンストラクタ(CSV読み込み画面を表示する)
     */
    public CSVUI(JFrame parent) {
        super(parent, "CSV読み込み", true); // true…親ウィンドウの操作をブロック

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // ×が押されたら…このウィンドウだけ終了
        setLayout(new BorderLayout(10, 10));

        // 上部パネル…テンプレートダウンロードボタン
        JPanel topPanel = new JPanel();
        topPanel.add(templateButton);
        add(topPanel, BorderLayout.NORTH);

        // 中央パネル…ファイルパス表示フィールド + ファイル選択ボタン
        filePathField.setEditable(false); // ファイルパス表示フィールドを編集不可に
        JPanel centerPanel = new JPanel();
        centerPanel.add(filePathField);
        centerPanel.add(selectButton);
        add(centerPanel, BorderLayout.CENTER);

        // 下部パネル…読み込むボタン
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(importButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // ファイル選択ボタンにイベントリスナーを追加
        selectButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(); // ファイル選択ダイアログ
            int chooserCode = chooser.showOpenDialog(this); // chooserを開き、ユーザーの操作を整数で返す
            if (chooserCode == JFileChooser.APPROVE_OPTION) { // APPROVE_OPTION…ユーザーが「開く」を選択した場合
                File file = chooser.getSelectedFile(); // ユーザーが選択したファイル
                filePathField.setText(file.getAbsolutePath()); // 絶対パスをfilePathFieldに渡す
            }
        });

        // 読み込むボタンにイベントリスナーを追加
        importButton.addActionListener(e -> {
            if (filePathField.getText().isEmpty()) { // ファイル選択されてない場合
                ErrorHandler.showErrorDialog("ファイルを選択してください。");
                return; // イベントリスナーから抜ける
            }
            loadCSV(filePathField.getText()); // CSV読み込み処理
        });

        // テンプレートダウンロードボタンにイベントリスナーを追加
        templateButton.addActionListener(e -> {
            if (CSVHandler.tryExportTemplateCSV(null)) {
                JOptionPane.showMessageDialog(this, "CSVテンプレートを出力しました。", "テンプレート出力完了", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // 表示させる
        pack(); // ウィンドウサイズ自動調整
        setLocationRelativeTo(parent); // 親ウィンドウを基準に表示
        setVisible(true); // 可視化
        LOGGER.logOutput("CSV読み込み画面を表示。");
    }


    /**
     * 処理中メッセージを初期化する
     */
    private void initializeProcessingDialog(String message) {
        processingDialog.setUndecorated(true); // タイトルバーを消す（×ボタンも消える）
        JPanel processingPanel = new JPanel();
        processingPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // 黒い枠線
        processingPanel.add(new JLabel(message));
        processingDialog.getContentPane().add(processingPanel);
        processingDialog.pack(); // ウィンドウサイズ自動調整
        processingDialog.setLocationRelativeTo(this); // 表示位置は親ウィンドウが基準
    }


    /**
     * ユーザーが選択したCSVファイルを読み込んで、保存確認のダイアログを表示する
     * @param filePath
     */
    private void loadCSV(String filePath) {

        // 処理中メッセージ表示
        initializeProcessingDialog("CSVファイルを読み込み中です。");
        SwingUtilities.invokeLater(() -> processingDialog.setVisible(true));

        // サブスレッド生成
        Thread threadLoadData = new Thread(() -> {
            // ロックを取得
            synchronized (LOCK) {
                CSVHandler csvHandler = new CSVHandler(filePath); // CSVハンドラー
                List<EmployeeInfo> importEmployeeList = csvHandler.readCSV(false); // 読み込む社員データのリスト
                if (importEmployeeList == null) {
                    LOGGER.logOutput("CSV読み込み失敗。再度CSVファイルを指定してください。");
                    // エラーメッセージはreadCSV()の中ですでに表示済み
                } else if (importEmployeeList.isEmpty()) {
                    LOGGER.logOutput("データが1件もありません。再度CSVファイルを指定してください。");
                    ErrorHandler.showErrorDialog("データが1件もありません。\n再度CSVファイルを指定してください。");
                } else {
                    // 保存確認のダイアログを表示
                    SwingUtilities.invokeLater(() -> {
                        int addCount = csvHandler.addedEmployeeCount;
                        int updateCount = csvHandler.updatedEmployeeCount;
                        showConfirmDialog(addCount, updateCount, importEmployeeList);
                    });
                }
            }
            // 処理中メッセージを閉じる
            SwingUtilities.invokeLater(() -> processingDialog.dispose());
        }, "CSVimporter");

        // サブスレッド開始
        threadLoadData.start();
    }


    /**
     * 確認ダイアログを表示する
     * @param addCount 追加人数
     * @param updateCount 更新人数
     */
    private void showConfirmDialog(int addCount, int updateCount, List<EmployeeInfo> employeeList) {
        confirmDialog.setLayout(new BorderLayout(10,10));

        // 中央パネル…確認メッセージ
        JPanel messagePanel = new JPanel();
        messagePanel.add(new JLabel(
            "<html>まだ保存は完了していません。<br>下記の内容で保存してよろしいですか？<br>" +
            "追加" + addCount + "名、更新" + updateCount + "名</html>"
        ));
        confirmDialog.add(messagePanel, BorderLayout.CENTER);

        // 下部パネル…確定ボタン + 戻るボタン
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);
        buttonPanel.add(backButton);
        confirmDialog.add(buttonPanel, BorderLayout.SOUTH);

        // 確定ボタンにイベントリスナーを追加
        confirmButton.addActionListener(e -> {
            confirmDialog.dispose();// 確認ダイアログ終了
            saveCSV(employeeList); // 保存処理
        });

        // 戻るボタンにイベントリスナーを追加
        backButton.addActionListener(e -> confirmDialog.dispose());

        // 表示させる
        confirmDialog.pack(); // ウィンドウサイズ自動調整
        confirmDialog.setLocationRelativeTo(this); // 表示位置は親ウィンドウが基準
        confirmDialog.setVisible(true); // 可視化
    }


    /**
     * 保存完了メッセージを表示する
     */
    private void showSavedDialog() {

        // JOptionPaneで十分そうなのでこれにしています。
        // 今後の変更によりJOptionPaneでは表現できなくなる場合は、JDialogに変更します。
        JOptionPane.showMessageDialog(this, "保存が完了しました。", "CSV読み込み完了", JOptionPane.INFORMATION_MESSAGE);
    }


    /**
     * 読み込んだCSVファイルの保存処理
     * @param employeeList
     */
    private void saveCSV(List<EmployeeInfo> employeeList) {
        
        // 処理中メッセージ表示
        initializeProcessingDialog("保存中です。");
        SwingUtilities.invokeLater(() -> processingDialog.setVisible(true));

        // サブスレッド生成
        Thread threadSaveData = new Thread(() -> {

            // 最終的にCSVに書き込みたいStringリストを定義
            List<EmployeeInfo> finalEmployeeList = new ArrayList<>(EmployeeManager.getEmployeeList());

            // 各要素をfinalEmployeeListに追加（更新の場合は既存データと差し替え）
            for (EmployeeInfo employee : employeeList) {
                if (employee.getLastUpdatedDate() == null) { // 最終更新日がnullなら新規追加
                    finalEmployeeList.add(employee);
                } else {
                    finalEmployeeList.removeIf(
                            removeEmployee -> removeEmployee.getEmployeeId().equals(employee.getEmployeeId()));
                    finalEmployeeList.add(employee);
                }
            }

            // ロックを取得
            synchronized (LOCK) {
                CSVHandler.tryWriteCSV(finalEmployeeList);
            }

            // 処理中メッセージを閉じて、保存完了メッセージを表示
            SwingUtilities.invokeLater(() -> {
                processingDialog.dispose();
                showSavedDialog();
            });
        }, "CSVsaver");

        // サブスレッド開始
        threadSaveData.start();
    }    
}