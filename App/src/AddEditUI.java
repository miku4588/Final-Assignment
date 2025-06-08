import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Set;
import java.util.Date;
import java.util.HashSet;

import javax.swing.*;

// エンジニア情報を追加・編集するためのUIクラス
public class AddEditUI {
    private JFrame frame;
    private EmployeeFormPanel formPanel; // 入力フォーム部分
    private ButtonPanel buttonPanel; // 保存・戻るボタン
    private JTextField employeeIdField; // 社員IDの入力欄
    private JLabel creationDateLabel; // データ作成日表示
    private JLabel lastUpdatedDateLabel; // 最終更新日表示

    public AddEditUI() {
        initialize(); // 初期化処理
    }

    private void initialize() {
        frame = new JFrame("エンジニア新規追加");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        formPanel = new EmployeeFormPanel(); // フォーム先に作成

        // 社員ID入力エリアの準備
        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel employeeIdLabel = new JLabel("社員ID: ");
        employeeIdField = new JTextField(15);
        idPanel.add(employeeIdLabel);
        idPanel.add(employeeIdField);

        // ボタンパネルに社員IDフィールドも渡す
        buttonPanel = new ButtonPanel(frame, formPanel, employeeIdField);

        // 日付の表示（作成日・更新日）
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate = sdf.format(new Date());
        creationDateLabel = new JLabel("データ作成日: " + currentDate);
        lastUpdatedDateLabel = new JLabel("最終更新日: " + currentDate);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        datePanel.add(creationDateLabel);
        datePanel.add(lastUpdatedDateLabel);
        frame.add(datePanel, BorderLayout.SOUTH);

        // 全体のパネル構成（GridBagLayout使用）
        JPanel container = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 10, 0);

        // ボタンパネルの配置
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(5, 5, 5, 5);
        container.add(buttonPanel, gbc);

        // 社員IDパネルの配置
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        container.add(idPanel, gbc);

        // 入力フォームパネルの配置（スクロール付き）
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        container.add(new JScrollPane(formPanel), gbc);

        frame.add(container, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddEditUI::new); // メインスレッドでUIを実行
    }
}

// 保存ボタンと戻るボタンのパネル
class ButtonPanel extends JPanel {
    private JButton saveButton, cancelButton;
    private JFrame frame;
    private EmployeeFormPanel formPanel;
    private JTextField employeeIdField;

    public ButtonPanel(JFrame frame, EmployeeFormPanel formPanel, JTextField employeeIdField) {
        this.frame = frame;
        this.formPanel = formPanel;
        this.employeeIdField = employeeIdField;

        setLayout(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("保存");
        cancelButton = new JButton("戻る");

        // 保存処理
        saveButton.addActionListener(e -> saveEmployee());

        // キャンセル処理
        cancelButton.addActionListener(e -> showDiscardDialog());

        add(saveButton);
        add(cancelButton);
    }

    private void saveEmployee() {
        // 必須入力チェック
        String idText = employeeIdField.getText().trim();
        String name = formPanel.getNameField().getText().trim();
        String phonetic = formPanel.getPhoneticField().getText().trim();
        if (idText.isEmpty() || name.isEmpty() || phonetic.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "社員ID、名前、フリガナは必ず入力してください。", "入力エラー", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // UIを操作不可に（例：保存ボタン無効化）
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);

        // モーダルダイアログで処理中をブロック表示
        JDialog loading = new JDialog(frame, "処理中…", true);
        loading.getContentPane().add(new JLabel("保存中です。しばらくお待ちください…"));
        loading.pack();
        loading.setLocationRelativeTo(frame);
        loading.setVisible(true);

        // 入力情報から EmployeeInfo を作成
        EmployeeInfo newEmployee = formPanel.getEmployeeData();

        // サブスレッドで保存処理
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                return EmployeeAdder.addEmployee(newEmployee);
            }

            @Override
            protected void done() {
                try {
                    boolean result = get();
                    if (result) {
                        JOptionPane.showMessageDialog(frame, "保存しました。", "完了", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose(); // 画面を閉じる
                    } else {
                        JOptionPane.showMessageDialog(frame, "保存に失敗しました。", "エラー", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "予期しないエラーが発生しました。", "エラー", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } finally {
                    saveButton.setEnabled(true);
                    cancelButton.setEnabled(true);
                    loading.dispose(); // ローディングダイアログを閉じる
                }
            }
        };

        worker.execute(); // サブスレッド起動
    }

    private void showDiscardDialog() {
        int result = JOptionPane.showConfirmDialog(frame, "変更を破棄しますか？", "確認", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            frame.dispose();
        }
    }
}

// 入力フォーム全体のパネル
class EmployeeFormPanel extends JPanel {
    private JTextField nameField, phoneticField, languagesSpokenField;
    private JComboBox<String> engineerStartYearBox;
    private JComboBox<String> technicalSkillBox, attitudeBox, communicationSkillBox, leadershipBox;
    private JTextArea careerArea, trainingHistoryArea, remarksArea;

    public EmployeeFormPanel() {
        setLayout(new GridBagLayout());

        // 年月日の初期データ作成
        String[] years = new String[100];
        for (int i = 0; i < 100; i++) {
            years[i] = String.valueOf(2025 - i);
        }
        String[] months = new String[12];
        for (int i = 0; i < 12; i++) {
            months[i] = String.valueOf(i + 1);
        }
        String[] days = new String[31];
        for (int i = 0; i < 31; i++) {
            days[i] = String.valueOf(i + 1);
        }

        JPanel leftPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // 左側に表示するラベルとコンポーネントの設定
        String[] labels = { "名前", "フリガナ", "生年月日", "入社年月", "エンジニア開始年", "扱える言語", "技術力", "受講態度", "コミュニケーション能力", "リーダーシップ力" };
        Component[] fields = {
            nameField = new JTextField(25),
            phoneticField = new JTextField(25),
            createDatePanel("生年月日", years, months, days),
            createDatePanel("入社年月", years, months, null),
            engineerStartYearBox = new JComboBox<>(years),
            languagesSpokenField = new JTextField(25),
            createRatingPanel(technicalSkillBox = new JComboBox<>(), ""),
            createRatingPanel(attitudeBox = new JComboBox<>(), ""),
            createRatingPanel(communicationSkillBox = new JComboBox<>(), ""),
            createRatingPanel(leadershipBox = new JComboBox<>(), "")
        };

        // プレースホルダの設定
        setPlaceholder(nameField, "例: 山田太郎");
        setPlaceholder(phoneticField, "例: ヤマダタロウ");
        setPlaceholder(languagesSpokenField, "例: 日本語, 英語");

        // 左パネルに要素を追加
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(15, 10, 15, 10);
            leftPanel.add(new JLabel(labels[i] + ":"), gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            leftPanel.add(fields[i], gbc);
        }

        // 右側のテキストエリア（経歴、研修、備考）
        JPanel rightPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        String[] rightLabels = { "経歴", "研修の受講歴", "備考" };
        JTextArea[] areas = {
            careerArea = new JTextArea(8, 30),
            trainingHistoryArea = new JTextArea(8, 30),
            remarksArea = new JTextArea(8, 30)
        };

        for (int i = 0; i < rightLabels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i * 2;
            gbc.weighty = 0.1;
            rightPanel.add(new JLabel(rightLabels[i]), gbc);
            gbc.gridy = i * 2 + 1;
            gbc.weighty = 0.9;
            rightPanel.add(new JScrollPane(areas[i]), gbc);
        }

        // 左右のパネルをメインパネルに追加
        add(leftPanel);
        add(rightPanel);
    }

    public JTextField getNameField() {
        return nameField; // 名前フィールドの取得
    }

    public JTextField getPhoneticField() {
        return phoneticField; // フリガナフィールドの取得
    }

    public EmployeeInfo getEmployeeData() {
        Name name = new Name(nameField.getText().trim());
        Phonetic phonetic = new Phonetic(phoneticField.getText().trim());
        Languages languages = new Languages();
        String languagesSpoken = languagesSpokenField.getText().trim();
        String[] languagesList = languagesSpoken.split(",");
        Set<String> languagesHashSet = new HashSet<>(Arrays.asList(languagesList));
        languages.addLanguages(languagesHashSet);
        
        // 生年月日や入社年月の取得ロジックを追加
        int birthYear = Integer.parseInt(((JComboBox<String>) ((JPanel) getComponent(0)).getComponent(2)).getSelectedItem().toString());
        int birthMonth = Integer.parseInt(((JComboBox<String>) ((JPanel) getComponent(0)).getComponent(2)).getSelectedItem().toString());
        int birthDay = Integer.parseInt(((JComboBox<String>) ((JPanel) getComponent(0)).getComponent(2)).getSelectedItem().toString());
        BirthDate birthDate = new BirthDate(birthYear + "/" + birthMonth + "/" + birthDay);

        
        // EmployeeInfoオブジェクトを生成して返す処理
        return new EmployeeInfo(null, name, phonetic, birthDate, null, null, null, null, null, null, null, null, null, languages, null, null);
    }

    // 評価用のドロップダウンとラベルを含んだパネルを生成
    private JPanel createRatingPanel(JComboBox<String> comboBox, String labelText) {
        String[] ratings = { "1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5" };
        comboBox.setModel(new DefaultComboBoxModel<>(ratings));
        comboBox.setSelectedIndex(8); // デフォルト値を5に設定
        JLabel label = new JLabel(labelText + "/5");
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(comboBox);
        panel.add(label);
        return panel;
    }

    // 年月日コンボボックスを含んだパネルを生成
    private JPanel createDatePanel(String title, String[] years, String[] months, String[] days) {
        JPanel panel = new JPanel();
        JComboBox<String> yearBox = new JComboBox<>(years);
        JComboBox<String> monthBox = new JComboBox<>(months);
        JComboBox<String> dayBox = (days != null) ? new JComboBox<>(days) : null;

        panel.add(new JLabel(title + " 年:"));
        panel.add(yearBox);
        panel.add(new JLabel("月:"));
        panel.add(monthBox);
        if (dayBox != null) {
            panel.add(new JLabel("日:"));
            panel.add(dayBox);
        }

        return panel;
    }

    // プレースホルダの実装
    private void setPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });
    }
}
