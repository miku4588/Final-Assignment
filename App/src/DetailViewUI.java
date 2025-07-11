import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

// JFrameを継承する
public class DetailViewUI extends JFrame {
    // ロガーを取得
    private static final EmployeeInfoLogger LOGGER = EmployeeInfoLogger.getInstance();

    // 対象の社員
    EmployeeInfo targetEmployee;

    // ボタン
    JButton backButton = new JButton("一覧画面に戻る");
    JButton editButton = new JButton("編集");
    JButton deleteButton = new JButton("削除");

    /**
     * コンストラクタ
     * @param EmployeeIdString
     */
    public DetailViewUI(String EmployeeIdString) {
        targetEmployee = getEmployeeInfo(EmployeeIdString);
        displayDetailViewUI();
    }

    /**
     * 社員IDで検索してEmployeeInfoを取ってくる
     * @param EmployeeIdString
     * @return EmployeeInfo型の社員データ
     */
    private EmployeeInfo getEmployeeInfo(String EmployeeIdString) {
        List<EmployeeInfo> employees = EmployeeManager.getEmployeeList();
        for(EmployeeInfo employee : employees) {
            if (employee.getEmployeeId().equals(EmployeeIdString)) {
                LOGGER.logOutput(employee.getName() + "の詳細情報を表示します。");
                return employee;
            }
        }
        LOGGER.logOutput("指定された社員IDと一致する社員が見つかりません。");
        ErrorHandler.showErrorDialog("指定された社員IDと一致する社員が見つかりません。");
        return null;
    }

    /**
     * 詳細画面を表示させる
     * @param targetEmployee
     */
    private void displayDetailViewUI() {
        // 画面に関する処理はinvokeLaterで囲むのが安全
        SwingUtilities.invokeLater(() -> {
            setTitle("エンジニア詳細");
            setSize(1000, 700);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // ×が押されたら…このウィンドウだけ終了
            setLayout(new BorderLayout());

            // 社員ID表示用パネルは選択可能（編集は不可）にする
            JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel idLabel = new JLabel("社員ID：");
            JTextField idField = new JTextField(targetEmployee.getEmployeeId());
            idField.setEditable(false); // 編集不可
            idField.setPreferredSize(new Dimension(100, idField.getPreferredSize().height)); // 幅だけ固定
            idPanel.add(idLabel);
            idPanel.add(idField);

            // 上部左パネル…社員ID、一覧に戻るボタン
            JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topLeftPanel.add(idPanel);
            topLeftPanel.add(backButton);

            // 上部右パネル…編集ボタン、削除ボタン
            JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topRightPanel.add(editButton);
            topRightPanel.add(deleteButton);

            // 上部パネルに入れる
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.add(topLeftPanel, BorderLayout.WEST);
            topPanel.add(topRightPanel, BorderLayout.EAST);
            add(topPanel, BorderLayout.NORTH);

            // 日時系、数値系の項目の値を表示用に整える
            String birthDateString = null;
            if (targetEmployee.getBirthDate() != null) {
                birthDateString = targetEmployee.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
            }

            String joinYearMonthString = null;
            if (targetEmployee.getJoinYearMonth() != null) {
                joinYearMonthString = targetEmployee.getJoinYearMonth().format(DateTimeFormatter.ofPattern("yyyy年MM月"));
            }

            String engineerStartYearString = null;
            if (targetEmployee.getEngineerStartYear() != null) {
                engineerStartYearString = targetEmployee.getEngineerStartYear().format(DateTimeFormatter.ofPattern("yyyy年"));
            }

            double technicalSkillDouble = targetEmployee.getTechnicalSkill();
            double attitudeDouble = targetEmployee.getAttitude();
            double communicationSkillDouble = targetEmployee.getCommunicationSkill();
            double leadershipDouble = targetEmployee.getLeadership();
            
            Number technicalSkill = technicalSkillDouble;
            Number attitude = attitudeDouble;
            Number communicationSkill = communicationSkillDouble;
            Number leadership = leadershipDouble;

            if (technicalSkillDouble == Math.floor(technicalSkillDouble)) {technicalSkill = (int)technicalSkillDouble;}
            if (attitudeDouble == Math.floor(attitudeDouble)) {attitude = (int)attitudeDouble;}
            if (communicationSkillDouble == Math.floor(communicationSkillDouble)) {communicationSkill = (int)communicationSkillDouble;}
            if (leadershipDouble == Math.floor(leadershipDouble)) {leadership = (int)leadershipDouble;}            

            // 中央左パネル…各項目（経歴、研修の受講歴、備考以外）
            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS)); // 縦に並べる
            leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 余白
            addSelectableField(leftPanel, "氏名カナ：", targetEmployee.getPhonetic());
            addSelectableField(leftPanel, "氏名：", targetEmployee.getName());
            addSelectableField(leftPanel, "生年月日：", birthDateString);
            addSelectableField(leftPanel, "入社年月：", joinYearMonthString);                    
            addSelectableField(leftPanel, "エンジニア開始年：", engineerStartYearString);
            addSelectableField(leftPanel, "扱える言語：", String.join(",", targetEmployee.getLanguages()));
            addSelectableField(leftPanel, "技術力：", technicalSkill + " / 5");
            addSelectableField(leftPanel, "受講態度：", attitude + " / 5");
            addSelectableField(leftPanel, "コミュニケーション能力：", communicationSkill + " / 5");
            addSelectableField(leftPanel, "リーダーシップ力：", leadership + " / 5");
            leftPanel.add(Box.createVerticalGlue()); // 最後にglueを詰め込む（最後じゃないとレイアウト崩れるので注意）

            // 中央右パネル…経歴、研修の受講歴、備考
            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS)); // 縦に並べる
            rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 余白
            addScrollPane(rightPanel, "経歴", targetEmployee.getCareer());
            addScrollPane(rightPanel, "研修の受講歴", targetEmployee.getTrainingHistory());
            addScrollPane(rightPanel, "備考", targetEmployee.getRemarks());

            // 中央パネルに入れる
            JPanel centerPanel = new JPanel(new GridLayout(1, 2));
            centerPanel.add(leftPanel);
            centerPanel.add(rightPanel);
            add(centerPanel, BorderLayout.CENTER);

            // 下部パネル
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            String creationDateString = "";
            if(targetEmployee.getCreationDate() != null) {
                creationDateString = targetEmployee.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
            }
            bottomPanel.add(new JLabel("データ作成日：" + creationDateString));

            bottomPanel.add(Box.createHorizontalStrut(10)); // 余白

            String lastUpdatedDateString = "";
            if(targetEmployee.getLastUpdatedDate() != null) {
                lastUpdatedDateString = "最終更新日：" + targetEmployee.getLastUpdatedDate().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
            }
            bottomPanel.add(new JLabel(lastUpdatedDateString));
            
            add(bottomPanel, BorderLayout.SOUTH);

            // 一覧に戻るボタンにイベントリスナーを追加
            backButton.addActionListener(e -> {
                setVisible(false);
                new ListViewUI(EmployeeManager.getInstance());
            });

            // 編集ボタンにイベントリスナーを追加
            editButton.addActionListener(e -> {
                setVisible(false);
                new AddEditUI(targetEmployee);
            });

            // 削除ボタンにイベントリスナーを追加
            deleteButton.addActionListener(e -> {
                showConfirmDialog(targetEmployee.getEmployeeId());
            });

            // 表示させる
            setVisible(true); // 可視化
            LOGGER.logOutput("詳細情報画面を表示。");
        });
    }


    /**
     * 値が選択可能（編集は不可）な項目行をパネルに追加する
     * @param panel 項目行を追加したいパネル
     * @param label
     * @param value
     */
    private void addSelectableField(JPanel panel, String label, String value) {
        // 項目名
        JLabel nameLabel = new JLabel(label);
        nameLabel.setPreferredSize(new Dimension(150, nameLabel.getPreferredSize().height)); // 幅は100、高さは文字サイズに合わせる

        // 値
        JTextField valueField = new JTextField(value);
        valueField.setPreferredSize(new Dimension(300, valueField.getPreferredSize().height)); // 幅は300、高さは文字サイズに合わせる
        valueField.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // グレーの枠線
        valueField.setEditable(false); // 編集不可

        // パネルに入れる
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // 左詰めで横並び
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowPanel.getPreferredSize().height)); // 最大の高さを固定
        rowPanel.add(nameLabel);
        rowPanel.add(valueField);
        panel.add(rowPanel);
    }

    /**
     * スクロール可能なテキストエリアをパネルに追加する
     * @param panel テキストエリアを追加したいパネル
     * @param label
     * @param value
     */
    private void addScrollPane(JPanel panel, String label, String value) {
        // 値がダブルクォーテーションで囲まれている場合は外しておく
        String displayValue = value;
        if (displayValue != null && displayValue.length() >= 2 &&
                displayValue.startsWith("\"") && displayValue.endsWith("\"")) {
            displayValue = displayValue.substring(1, displayValue.length() - 1);
        }

        // ラベル
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, labelPanel.getPreferredSize().height)); // 最大の高さを固定
        JLabel nameLabel = new JLabel(label);
        labelPanel.add(nameLabel);

        // 値
        JTextArea valuArea = new JTextArea(displayValue);
        valuArea.setEditable(false);
        valuArea.setOpaque(false); // 背景を透明に
        JScrollPane valuScrollPane = new JScrollPane(valuArea);

        // パネルに入れる
        panel.add(labelPanel);
        panel.add(valuScrollPane);
    }


    /**
     * 削除確認ダイアログ
     * @param EmployeeIdString
     */
    private void showConfirmDialog(String EmployeeIdString) {
        // 画面に関する処理はinvokeLaterで囲むのが安全
        SwingUtilities.invokeLater(() -> {
            // ダイアログ
            JDialog deleteDialog = new JDialog(this, "削除確認", true); // true…親ウィンドウの操作をブロック
            deleteDialog.setLayout(new BorderLayout(10,10));

            // ボタン
            JButton deleteConfirmButton = new JButton("削除");
            JButton cancelButton = new JButton("キャンセル");

            // 中央パネル…確認メッセージ
            JPanel messagePanel = new JPanel();
            messagePanel.add(new JLabel(
                "<html>削除したデータは復元できません。<br>" +
                "削除しますか？</html>"
            ));
            deleteDialog.add(messagePanel, BorderLayout.CENTER);

            // 下部パネル…確定ボタン + 戻るボタン
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(deleteConfirmButton);
            buttonPanel.add(cancelButton);
            deleteDialog.add(buttonPanel, BorderLayout.SOUTH);

            // 確定ボタンにイベントリスナーを追加
            deleteConfirmButton.addActionListener(e -> {
                deleteDialog.dispose();// 確認ダイアログ終了
                showDeletingingDialog(EmployeeIdString);
            });

            // キャンセルボタンにイベントリスナーを追加
            cancelButton.addActionListener(e -> deleteDialog.dispose());

            // 表示させる
            deleteDialog.pack(); // ウィンドウサイズ自動調整
            deleteDialog.setLocationRelativeTo(this); // 表示位置は親ウィンドウが基準
            deleteDialog.setVisible(true); // 可視化
        });
    }

    /**
     * 処理中メッセージを初期化する
     */
    private void showDeletingingDialog(String EmployeeIdString) {

        JDialog deletingDialog = new JDialog(this, true); // true…親ウィンドウの操作をブロック
        deletingDialog.setUndecorated(true); // タイトルバーを消す（×ボタンも消える）

        JPanel deletingPanel = new JPanel();
        deletingPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // 黒い枠線
        deletingPanel.add(new JLabel("削除中です。"));

        deletingDialog.getContentPane().add(deletingPanel);
        deletingDialog.pack(); // ウィンドウサイズ自動調整
        deletingDialog.setLocationRelativeTo(this); // 表示位置は親ウィンドウが基準

        SwingUtilities.invokeLater(() -> deletingDialog.setVisible(true)); // 可視化

        // 削除終わったら削除中メッセージを消す
        if (EmployeeDeleter.deleteEmployee(EmployeeIdString)) {
            SwingUtilities.invokeLater(() -> {
                deletingDialog.dispose();
                this.dispose();
                showDeletedDialog();
            });
        }
    }


    /**
     * 保存完了メッセージを表示する
     */
    private void showDeletedDialog() {

        // JOptionPaneで十分そうなのでこれにしています。
        // 今後の変更によりJOptionPaneでは表現できなくなる場合は、JDialogに変更します。
        JOptionPane.showMessageDialog(this, "削除が完了しました。", "削除完了", JOptionPane.INFORMATION_MESSAGE);
    }

}