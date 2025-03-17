import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class AddEditUI {
    private JFrame frame;
    private EmployeeFormPanel formPanel;
    private ButtonPanel buttonPanel; // ボタンパネルの宣言
    private JTextField employeeIdField; // 社員ID用のテキストフィールド
    private JLabel creationDateLabel; // データ作成日ラベル
    private JLabel lastUpdatedDateLabel; // 最終更新日ラベル

    public AddEditUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("エンジニア新規追加");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        buttonPanel = new ButtonPanel(frame); // frameを渡してボタンパネルのインスタンスを作成
        formPanel = new EmployeeFormPanel();

        JPanel idPanel = new JPanel();
        idPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel employeeIdLabel = new JLabel("社員ID: ");
        employeeIdField = new JTextField(generateEmployeeId(), 15);
        idPanel.add(employeeIdLabel);
        idPanel.add(employeeIdField);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate = sdf.format(new Date());
        creationDateLabel = new JLabel("データ作成日: " + currentDate);
        lastUpdatedDateLabel = new JLabel("最終更新日: " + currentDate);

        JPanel datePanel = new JPanel();
        datePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        datePanel.add(creationDateLabel);
        datePanel.add(lastUpdatedDateLabel);
        frame.add(datePanel, BorderLayout.SOUTH);

        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 10, 0);

        // ボタンパネルを上に配置
        GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
        buttonPanelConstraints.gridx = 0; // 0列目に配置
        buttonPanelConstraints.gridy = 0; // 0行目に配置
        buttonPanelConstraints.anchor = GridBagConstraints.NORTHEAST; // 右上に配置
        buttonPanelConstraints.insets = new Insets(5, 5, 5, 5); // 余白設定
        container.add(buttonPanel, buttonPanelConstraints);

        GridBagConstraints idPanelConstraints = new GridBagConstraints();
        idPanelConstraints.gridx = 0;
        idPanelConstraints.gridy = 1; // 1行目に配置
        idPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        idPanelConstraints.weightx = 1.0;
        container.add(idPanel, idPanelConstraints);

        GridBagConstraints formPanelConstraints = new GridBagConstraints();
        formPanelConstraints.gridx = 0;
        formPanelConstraints.gridy = 2; // 2行目に配置
        formPanelConstraints.fill = GridBagConstraints.BOTH;
        formPanelConstraints.weightx = 1.0;
        formPanelConstraints.weighty = 1.0;
        container.add(new JScrollPane(formPanel), formPanelConstraints);

        frame.add(container, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private String generateEmployeeId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder employeeId = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            employeeId.append(chars.charAt(random.nextInt(chars.length())));
        }
        System.out.println("Generated Employee ID: " + employeeId.toString());
        return employeeId.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddEditUI::new);
    }
}

// エンジニア情報入力フォームのパネル
class EmployeeFormPanel extends JPanel {
    private JTextField nameField, phoneticField, languagesSpokenField; // 各入力フィールド
    private JComboBox<String> birthYearBox, birthMonthBox, birthDayBox; // 生年月日用のコンボボックス
    private JComboBox<String> joinYearBox, joinMonthBox; // 入社年月用のコンボボックス
    private JComboBox<String> engineerStartYearBox; // エンジニア開始年用のコンボボックス
    private JComboBox<String> technicalSkillBox, attitudeBox, communicationSkillBox, leadershipBox; // コンボボックス
    private JTextArea careerArea, trainingHistoryArea, remarksArea; // テキストエリア

    public EmployeeFormPanel() {
        setLayout(new GridBagLayout()); // GridBagLayoutでレイアウトを管理

        // 年、月、日のリストを作成
        String[] years = new String[100];
        for (int i = 0; i < 100; i++) {
            years[i] = String.valueOf(2025 - i); // 2025年から過去100年分
        }

        String[] months = new String[12];
        for (int i = 0; i < 12; i++) {
            months[i] = String.valueOf(i + 1); // 1月から12月
        }

        String[] days = new String[31];
        for (int i = 0; i < 31; i++) {
            days[i] = String.valueOf(i + 1); // 1日から31日
        }

        // 左側の入力フィールドパネル
        JPanel leftPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 10, 10); // 余白設定
        gbc.fill = GridBagConstraints.HORIZONTAL; // 横幅いっぱいに広がる
        gbc.weightx = 1; // 横幅調整

        // ラベルと入力フィールドを設定
        String[] labels = { "名前", "フリガナ", "生年月日", "入社年月", "エンジニア開始年", "扱える言語", "技術力", "受講態度", "コミュニケーション能力",
                "リーダーシップ力" }; // ラベルの配列
        Component[] fields = {
                nameField = new JTextField(25), // 横幅を25に設定
                phoneticField = new JTextField(25), // 横幅を25に設定
                createDatePanel("生年月日", years, months, days),
                createDatePanel("入社年月", years, months, null), // 日を含まない
                engineerStartYearBox = new JComboBox<>(years), // エンジニア開始年をプルダウンに変更
                languagesSpokenField = new JTextField(25), // 扱える言語用のテキストフィールド
                technicalSkillBox = new JComboBox<>(new String[] { "1", "2", "3", "4", "5" }),
                attitudeBox = new JComboBox<>(new String[] { "1", "2", "3", "4", "5" }),
                communicationSkillBox = new JComboBox<>(new String[] { "1", "2", "3", "4", "5" }),
                leadershipBox = new JComboBox<>(new String[] { "1", "2", "3", "4", "5" }),
        };

        // プレースホルダーを設定するためのヘルパーメソッド
        setPlaceholder(nameField, "例: 山田太郎");
        setPlaceholder(phoneticField, "例: ヤマダタロウ");
        setPlaceholder(languagesSpokenField, "例: 日本語, 英語");

        // 左パネルにラベルと入力フィールドを追加
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; // x座標
            gbc.gridy = i; // y座標
            gbc.weightx = 0; // ラベルの幅を最小限に
            gbc.anchor = GridBagConstraints.WEST; // ラベルを左寄せ
            gbc.insets = new Insets(15, 10, 15, 10); // 余白設定
            leftPanel.add(new JLabel(labels[i] + ":"), gbc); // ラベルを追加

            gbc.gridx = 1; // 次の列
            gbc.weightx = 1; // テキストボックスの幅を調整
            leftPanel.add(fields[i], gbc); // 入力フィールドを追加
        }

        // 右側のテキストエリアパネル
        JPanel rightPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 余白設定
        gbc.fill = GridBagConstraints.BOTH; // 横幅・高さに広がる
        gbc.weightx = 1; // 横幅調整
        gbc.weighty = 1; // 高さ調整

        // 経歴・研修・備考のテキストエリア
        String[] rightLabels = { "経歴", "研修の受講歴", "備考" }; // 右側のラベル
        JTextArea[] areas = {
                careerArea = new JTextArea(8, 30), // 経歴用テキストエリア
                trainingHistoryArea = new JTextArea(8, 30), // 研修用テキストエリア
                remarksArea = new JTextArea(8, 30) // 備考用テキストエリア
        };

        // 右パネルにラベルとスクロール可能なテキストエリアを追加
        for (int i = 0; i < rightLabels.length; i++) {
            gbc.gridx = 0; // x座標
            gbc.gridy = i * 2; // y座標
            gbc.weighty = 0.1; // ラベルの高さを調整
            rightPanel.add(new JLabel(rightLabels[i]), gbc); // ラベルを追加
            gbc.gridy = i * 2 + 1; // 次の行
            gbc.weighty = 0.9; // テキストエリアの高さを調整
            rightPanel.add(new JScrollPane(areas[i]), gbc); // スクロール可能なテキストエリアを追加
        }

        // 左右のパネルを全体パネルに追加
        add(leftPanel); // 左パネルを追加
        add(rightPanel); // 右パネルを追加
    }

    // 生年月日や入社年月のためのコンボボックスを作成
    private JPanel createDatePanel(String title, String[] years, String[] months, String[] days) {
        JPanel panel = new JPanel();
        JComboBox<String> yearBox = new JComboBox<>(years);
        JComboBox<String> monthBox = new JComboBox<>(months);
        JComboBox<String> dayBox = (days != null) ? new JComboBox<>(days) : null;

        panel.add(new JLabel(title + " 年:"));
        panel.add(yearBox);
        panel.add(new JLabel("月:"));
        panel.add(monthBox);
        if (dayBox != null) { // 日が必要な場合
            panel.add(new JLabel("日:"));
            panel.add(dayBox);
        }

        return panel;
    }

    // プレースホルダーを設定するメソッド
    private void setPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(java.awt.Color.GRAY);
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(java.awt.Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(java.awt.Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });
    }
}

// ボタンパネル（保存・戻る）
class ButtonPanel extends JPanel {
    private JButton saveButton, cancelButton; // ボタン
    private JFrame frame; // JFrameを保持する変数

    public ButtonPanel(JFrame frame) { // コンストラクタでframeを受け取る
        this.frame = frame; // frameをインスタンス変数に保存
        setLayout(new FlowLayout(FlowLayout.RIGHT)); // 右寄せのフローレイアウト

        saveButton = new JButton("保存"); // 保存ボタン
        cancelButton = new JButton("戻る"); // 戻るボタン

        // 保存ボタンのアクションリスナー
        saveButton.addActionListener(e -> saveEmployee());

        // 戻るボタンのアクションリスナー
        cancelButton.addActionListener(e -> showDiscardDialog());

        add(saveButton); // 保存ボタンを追加
        add(cancelButton); // 戻るボタンを追加
    }

    // 保存処理
    private void saveEmployee() {
        JOptionPane.showMessageDialog(frame, "保存中です。", "処理中", JOptionPane.INFORMATION_MESSAGE);
        // ここに保存処理を追加
        JOptionPane.showMessageDialog(frame, "保存しました。", "完了", JOptionPane.INFORMATION_MESSAGE);
        frame.dispose(); // フレームを閉じる
    }

    // 破棄確認ダイアログを表示
    private void showDiscardDialog() {
        int choice = JOptionPane.showConfirmDialog(frame, "入力した情報が破棄されます。よろしいですか？", "警告", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            frame.dispose(); // フレームを閉じる
        }
    }
}
