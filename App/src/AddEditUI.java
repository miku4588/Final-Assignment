import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.JTextComponent;

import java.awt.event.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.function.*;
import java.util.stream.*;
import java.awt.*;
import java.text.SimpleDateFormat;

/**
 * 従業員情報追加/編集UIのメインクラス。
 * フォーム入力、バリデーション、保存機能を提供する。
 */
public class AddEditUI {
    private JFrame frame;
    private EmployeeFormPanel formPanel;
    private JLabel creationDateLabel;
    private JLabel lastUpdatedDateLabel;
    private JTextField employeeIdField;
    // ロガーを取得
    private static final EmployeeInfoLogger LOGGER = EmployeeInfoLogger.getInstance();

    /**
     * 新規データ追加用のコンストラクタ
     */
    public AddEditUI() {
        LOGGER.logOutput("新規追加画面を表示。");
        initialize(false);
    }

    /**
     * 既存データ編集用のコンストラクタ
     * 
     * @param emp 編集したい社員の情報
     */
    public AddEditUI(EmployeeInfo emp) {
        LOGGER.logOutput("氏名：" + emp.getName() + "の編集画面を表示。");
        initialize(true, emp.getEmployeeId());
        if (emp != null) {
            frame.setTitle("エンジニア編集"); // タイトルも変える
            setEmployeeInfo(emp); // フォームにデータをセット
            employeeIdField.setEditable(false); // 社員IDを編集不可にする
            creationDateLabel
                    .setText("データ作成日: " + emp.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))); // データ作成日を設定
        }

    }

    // initializeオーバーロード（新規追加用から呼ばれる）
    private void initialize(boolean isEditMode) {
        initialize(isEditMode, (String) null);
    }

    /** UIコンポーネントの設定とレイアウト */
    private void initialize(boolean isEditMode, String employeeId) {
        frame = new JFrame("エンジニア新規追加");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 3));
        JLabel employeeIdLabel = new JLabel("社員ID: ");
        this.employeeIdField = new JTextField(15);

        formPanel = new EmployeeFormPanel(employeeIdField);

        idPanel.add(employeeIdLabel);
        idPanel.add(this.employeeIdField);

        ButtonPanel buttonPanel = new ButtonPanel(
                frame,
                formPanel,
                this.employeeIdField, // クラスフィールドを渡す
                isEditMode,
                employeeId);

        // 日付表示設定
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        String currentDate = new SimpleDateFormat("yyyy/MM/d").format(new Date());
        creationDateLabel = new JLabel("データ作成日: " + currentDate);
        datePanel.add(creationDateLabel);

        // 編集モードの場合だけ最終更新日を表示
        if (isEditMode) {
            lastUpdatedDateLabel = new JLabel("最終更新日: " + currentDate);
            datePanel.add(lastUpdatedDateLabel);
        }

        frame.add(datePanel, BorderLayout.SOUTH);

        // メインレイアウト設定
        JPanel container = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 5, 0);

        gbc.gridx = 0;
        gbc.gridy = 0; // ここを0にして左上に配置
        gbc.anchor = GridBagConstraints.NORTHWEST; // 左上に固定
        gbc.insets = new Insets(5, 5, 5, 5);
        container.add(idPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        container.add(buttonPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        container.add(new JScrollPane(formPanel), gbc);

        frame.add(container, BorderLayout.CENTER);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                formPanel.setExiting(true);
                showConfirmExitDialog(); // ここで全処理を行う
            }
        });
        frame.setVisible(true);
    }

    private void showConfirmExitDialog() {
        int choice = JOptionPane.showConfirmDialog(
                frame,
                "入力された情報を破棄し、アプリを終了します。よろしいですか？",
                "確認",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        LOGGER.logOutput("アプリ終了確認: " + choice);

        if (choice == JOptionPane.YES_OPTION) {
            formPanel.clearForm();
            frame.dispose();

            LOGGER.logOutput(JOptionPane.YES_OPTION + "が選択されたためアプリを終了します。");
            System.exit(0);
        } else {
            LOGGER.logOutput("アプリの終了がキャンセルされました。");
            formPanel.setExiting(false);
        }

    }

    public void setEmployeeInfo(EmployeeInfo emp) {
        formPanel.setEmployeeInfo(emp);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddEditUI::new);
    }
}

/**
 * 保存/キャンセルボタンの処理を担当するパネル。
 * バリデーションとデータ保存機能を実装。
 */
class ButtonPanel extends JPanel {
    private JFrame frame;
    private EmployeeFormPanel formPanel;
    private JTextField employeeIdField;
    private boolean isEditMode;
    private String employeeId;
    // ロガーを取得
    private static final EmployeeInfoLogger LOGGER = EmployeeInfoLogger.getInstance();

    /**
     * ボタンとアクションリスナーを初期化
     * 
     * @param frame           親フレーム
     * @param formPanel       フォームパネル
     * @param employeeIdField 社員ID入力フィールド
     * @param isEditMode
     */
    public ButtonPanel(JFrame frame, EmployeeFormPanel formPanel, JTextField employeeIdField, boolean isEditMode,
            String employeeId) {
        this.frame = frame;
        this.formPanel = formPanel;
        this.employeeIdField = employeeIdField;
        this.isEditMode = isEditMode;
        this.employeeId = employeeId;

        setLayout(new FlowLayout(FlowLayout.RIGHT)); // ボタンを右寄せ
        JButton saveButton = new JButton("保存"); // ボタンを作成＝表示はされない
        JButton cancelButton = new JButton("キャンセル"); // ボタンを作成

        saveButton.addActionListener(e -> saveEmployee());
        cancelButton.addActionListener(e -> showDiscardDialog());

        add(saveButton); // ボタンをパネルに追加=表示される
        add(cancelButton);// ボタンをパネルに追加

    }

    /** 従業員情報保存処理 */
    private void saveEmployee() {

        LOGGER.logOutput("入力された情報を保存します。");

        List<String> errors = new ArrayList<>();
        Map<String, Object> fieldValues = new HashMap<>();

        LOGGER.logOutput("バリデーション開始。");

        // フィールドバリデーション一括実行
        validateField("employeeId", employeeIdField, text -> new EmployeeId(text), errors, fieldValues);
        validateField("phonetic", formPanel.getPhoneticField(), text -> new Phonetic(text), errors, fieldValues);
        validateField("name", formPanel.getNameField(), text -> new Name(text), errors, fieldValues);

        // 生年月日
        validateField(
                "birthDate",
                formPanel.getBirthYearCombo(),
                s -> new BirthDate(formPanel.getBirthDate()),
                errors,
                fieldValues);

        // 入社年月
        validateField(
                "joinYearMonth",
                formPanel.getJoinYearCombo(),
                s -> new JoinYearMonth(formPanel.getJoinYearMonth()),
                errors,
                fieldValues);

        validateField("engineerStartYear", formPanel.getEngineerStartYearBox(), text -> new EngineerStartYear(text),
                errors, fieldValues);

        // 評価フィールド（コンボボックス使用）
        validateField("technicalSkill", formPanel.getTechnicalSkillCombo(), text -> new TechnicalSkill(text), errors,
                fieldValues);
        validateField("attitude", formPanel.getAttitudeCombo(), text -> new Attitude(text), errors, fieldValues);
        validateField("communicationSkill", formPanel.getCommunicationSkillCombo(),
                text -> new CommunicationSkill(text), errors, fieldValues);
        validateField("leadership", formPanel.getLeadershipCombo(), text -> new Leadership(text), errors, fieldValues);

        validateField("career", formPanel.getCareerArea(),
                text -> new Career(escapeForCSV(text)), errors, fieldValues);

        validateField("trainingHistory", formPanel.getTrainingHistoryArea(),
                text -> new TrainingHistory(escapeForCSV(text)), errors, fieldValues);

        validateField("remarks", formPanel.getRemarksArea(),
                text -> new Remarks(escapeForCSV(text)), errors, fieldValues);

        // 言語選択バリデーション
        validateLanguages(errors, fieldValues);

        if (!errors.isEmpty()) {
            LOGGER.logOutput("バリデーションチェックNG。エラー一覧を出力します。");
            showValidationErrors(errors);
            return;
        }

        LOGGER.logOutput("従業員オブジェクトを生成。");

        // 従業員情報オブジェクト作成
        EmployeeInfo newEmployee = new EmployeeInfo(
                (EmployeeId) fieldValues.get("employeeId"),
                (Name) fieldValues.get("name"),
                (Phonetic) fieldValues.get("phonetic"),
                (BirthDate) fieldValues.get("birthDate"),
                (JoinYearMonth) fieldValues.get("joinYearMonth"),
                (EngineerStartYear) fieldValues.get("engineerStartYear"),
                (TechnicalSkill) fieldValues.get("technicalSkill"),
                (Attitude) fieldValues.get("attitude"),
                (CommunicationSkill) fieldValues.get("communicationSkill"),
                (Leadership) fieldValues.get("leadership"),
                (Career) fieldValues.get("career"),
                (TrainingHistory) fieldValues.get("trainingHistory"),
                (Remarks) fieldValues.get("remarks"),
                (Languages) fieldValues.get("languages"), // UIから選ばれた言語

                LocalDate.now(),
                LocalDate.now());

        // 処理中ダイアログを作成
        JDialog progressDialog = new JDialog(frame, isEditMode ? "更新中" : "処理中", true);
        progressDialog.setLayout(new BorderLayout());

        JLabel label = new JLabel(isEditMode ? "更新中です..." : "保存中です...", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        progressDialog.add(label, BorderLayout.CENTER);

        progressDialog.setSize(250, 120);
        progressDialog.setLocationRelativeTo(frame);
        progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        // SwingWorker で非同期に保存 or 編集
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                try {
                    Thread.sleep(500); // ダイアログの見栄え調整
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (isEditMode) {
                    // 編集処理（非同期）
                    return EmployeeEditor.editEmployee(
                            ((EmployeeId) fieldValues.get("employeeId")).getEmployeeId(),
                            ((Name) fieldValues.get("name")).getName(),
                            ((Phonetic) fieldValues.get("phonetic")).getPhonetic(),
                            ((BirthDate) fieldValues.get("birthDate")).getBirthDate().toString(),
                            ((JoinYearMonth) fieldValues.get("joinYearMonth")).toString(),
                            ((EngineerStartYear) fieldValues.get("engineerStartYear")).getEngineerStartYear()
                                    .toString(),
                            String.valueOf(((TechnicalSkill) fieldValues.get("technicalSkill")).getTechnicalSkill()),
                            String.valueOf(((Attitude) fieldValues.get("attitude")).getAttitude()),
                            String.valueOf(((CommunicationSkill) fieldValues.get("communicationSkill"))
                                    .getCommunicationSkill()),
                            String.valueOf(((Leadership) fieldValues.get("leadership")).getLeadership()),
                            ((TrainingHistory) fieldValues.get("trainingHistory")).getTrainingHistory(),
                            ((Career) fieldValues.get("career")).getCareer(),
                            ((Remarks) fieldValues.get("remarks")).getRemarks(),
                            ((Languages) fieldValues.get("languages")).toString(),
                            (employeeInfo, isNew) -> CSVHandler.writeCSV(employeeInfo, isNew));
                } else {
                    // 新規登録処理（非同期）
                    return EmployeeAdder.addEmployee(newEmployee);
                }
            }

            @Override
            protected void done() {
                employeeId = ((EmployeeId) fieldValues.get("employeeId")).getEmployeeId();

                progressDialog.dispose();
                try {
                    boolean result = get();
                    if (result) {
                        LOGGER.logOutput("従業員情報の保存が完了しました。");
                        JOptionPane.showMessageDialog(frame,
                                isEditMode ? "更新しました。" : "保存しました。",
                                "完了",
                                JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();

                        if (isEditMode) {
                            // 編集成功なら詳細画面へ戻る
                            new DetailViewUI(employeeId);
                        } else {
                            // 新規作成成功なら一覧画面へ戻る
                            new ListViewUI(EmployeeManager.getInstance());
                        }
                    } else {
                        // 失敗時はエラーメッセージだけ表示
                        LOGGER.logOutput("従業員情報の保存に失敗しましたしました。");
                        JOptionPane.showMessageDialog(frame,
                                isEditMode ? "更新に失敗しました。" : "保存に失敗しました。",
                                "エラー",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    LOGGER.logException("従業員情報の保存中にエラーが発生しました。", ex);
                    JOptionPane.showMessageDialog(frame, "エラーが発生しました。", "例外", JOptionPane.ERROR_MESSAGE);
                }
            }

        };

        worker.execute();
        progressDialog.setVisible(true);
    }

    /**
     * 改行を含む項目のダブルクォーテーションを調整
     * 
     * @param text
     * @return
     */
    private String escapeForCSV(String text) {
        // 値が空なら空文字「""」を返す
        if (text == null || text.isEmpty())
            return "\"\"";

        // 3文字以上かつ先頭と末尾がダブルクォーテーションの場合は、いったん先頭と末尾の文字を消す
        if (text.length() > 2 && text.startsWith("\"") && text.endsWith("\""))
            return text.substring(1, text.length() - 1);

        // 文中のダブルクォーテーションを2個連続に置換
        String sanitized = text.replace("\"", "\"\"");

        // 全体を新たにダブルクォーテーションで囲む
        if (sanitized.contains("\n")) {
            return "\"" + sanitized + "\"";
        } else {
            return sanitized;
        }
    }

    private void validateLanguages(List<String> errors, Map<String, Object> fieldValues) {
        try {
            Languages languages = new Languages();
            String input = formPanel.getProgrammingLanguageField().getText(); // ← ここで自由入力を取得
            if (input != null && !input.trim().isEmpty()) {
                String[] langArray = input.split(",\\s*"); // カンマ区切り＋空白トリム
                for (String lang : langArray) {
                    if (!languages.addLanguage(lang.trim())) {
                        throw new IllegalArgumentException("無効な言語です: " + lang);
                    }
                }
            }
            fieldValues.put("languages", languages);

        } catch (Exception e) {
            errors.add("言語入力: " + e.getMessage());
            formPanel.updateFieldValidation(formPanel.getProgrammingLanguageField(), false);
        }
    }

    public void updateFieldValidation(Component field, boolean isValid) {
        ((JComponent) field).setBorder(new LineBorder(isValid ? Color.GRAY : Color.RED, 1));

    }

    /**
     * 汎用バリデーションメソッド
     * 
     * @param fieldName フィールド名
     * @param component UIコンポーネント
     * @param validator バリデーション関数
     * @param errors    エラーリスト
     * @param values    値マップ
     */
    private void validateField(String fieldName, JComponent component,
            Function<String, EmployeeInfoValidator> validator,
            List<String> errors, Map<String, Object> values) {
        try {
            String text = "";
            if (component instanceof JTextField) {
                text = ((JTextField) component).getText().trim();
            } else if (component instanceof JTextArea) {
                text = ((JTextArea) component).getText().trim();
            } else if (component instanceof JComboBox) {
                text = (String) ((JComboBox<?>) component).getSelectedItem();
            }

            values.put(fieldName, validator.apply(text));
            formPanel.updateFieldValidation(component, true);
        } catch (IllegalArgumentException e) {
            errors.add(e.getMessage());
            formPanel.updateFieldValidation(component, false);
        }
    }

    /** バリデーションエラー表示 */
    private void showValidationErrors(List<String> errors) {
        String errorHtml = errors.stream()
                .map(e -> "<li>" + e + "</li>")
                .collect(Collectors.joining("", "<html><b>入力エラー:</b><ul>", "</ul></html>"));
        JOptionPane.showMessageDialog(frame, errorHtml, "入力エラー", JOptionPane.ERROR_MESSAGE);
    }

    /** 破棄確認ダイアログ表示 */
    private void showDiscardDialog() {

        int result = JOptionPane.showConfirmDialog(
                frame, "変更を破棄しますか？", "確認", JOptionPane.YES_NO_OPTION);
        LOGGER.logOutput("キャンセル確認: " + result);

        if (result == JOptionPane.YES_OPTION) {
            frame.setVisible(false);
            LOGGER.logOutput(JOptionPane.YES_OPTION + "が選択されたため、保存せず前の画面へ戻ります。");
            
            if (isEditMode) {
                // 編集時は詳細画面へ遷移
                new DetailViewUI(employeeId);
            } else {
                // 新規追加時は一覧画面へ遷移
                new ListViewUI(EmployeeManager.getInstance());
            }
        }
        // NOの場合は何もしない（画面そのまま）
    }
}

/**
 * 従業員情報入力フォームを管理するパネル。
 * 全入力フィールドとバリデーション機能を含む。
 */
class EmployeeFormPanel extends JPanel {
    // 基本情報フィールド
    private JTextField employeeIdField;
    private JTextField nameField, phoneticField;
    private JComboBox<String> birthYearCombo, birthMonthCombo, birthDayCombo;
    private JComboBox<String> joinYearCombo, joinMonthCombo;
    private JTextField programmingLanguageField;

    private boolean isExiting = false;

    public void setExiting(boolean exiting) {
        this.isExiting = exiting;
    }

    public void setEmployeeInfo(EmployeeInfo emp) {
        employeeIdField.setText(emp.getEmployeeId());
        phoneticField.setText(emp.getPhonetic());
        nameField.setText(emp.getName());

        if (emp.getBirthDate() != null) {
            LocalDate birthDate = emp.getBirthDate();
            birthYearCombo.setSelectedItem(String.valueOf(birthDate.getYear()));
            birthMonthCombo.setSelectedItem(String.format("%02d", birthDate.getMonthValue()));
            birthDayCombo.setSelectedItem(String.format("%02d", birthDate.getDayOfMonth()));
        } else {
            // 初期値クリア
            birthYearCombo.setSelectedIndex(0);
            birthMonthCombo.setSelectedIndex(0);
            birthDayCombo.setSelectedIndex(0);
        }

        YearMonth joinYearMonth = emp.getJoinYearMonth(); // YearMonth型と仮定

        joinYearCombo.setSelectedItem(String.valueOf(joinYearMonth.getYear()));
        joinMonthCombo.setSelectedItem(String.format("%02d", joinYearMonth.getMonthValue()));

        engineerStartYearBox.setSelectedItem(emp.getEngineerStartYear());

        programmingLanguageField.setText(String.join(", ", emp.getLanguages()));

        technicalSkillCombo.setSelectedItem(String.valueOf(emp.getTechnicalSkill()));
        attitudeCombo.setSelectedItem(String.valueOf(emp.getAttitude()));
        communicationSkillCombo.setSelectedItem(String.valueOf(emp.getCommunicationSkill()));
        leadershipCombo.setSelectedItem(String.valueOf(emp.getLeadership()));

        careerArea.setText(escapeForTextArea(emp.getCareer()));
        trainingHistoryArea.setText(escapeForTextArea(emp.getTrainingHistory()));
        remarksArea.setText(escapeForTextArea(emp.getRemarks()));

        // 言語選択のチェックボックスは一旦全部オフにしてから
        Set<String> selectedLangs = emp.getLanguages();
        for (JCheckBox cb : languageCheckboxes) {
            cb.setSelected(selectedLangs.contains(cb.getText()));
        }
    }

    private String escapeForTextArea(String text) {
        // 値が空なら空文字「""」を返す
        if (text.equals("\"\"") || text == null || text.isEmpty())
            return null;

        // 3文字以上かつ先頭と末尾がダブルクォーテーションの場合は、先頭と末尾の文字を消す
        if (text.length() > 2 && text.startsWith("\"") && text.endsWith("\""))
            return text.substring(1, text.length() - 1);

        return text;
    }

    // 職業情報フィールド
    private JComboBox<String> engineerStartYearBox;

    // 評価フィールド（コンボボックス）
    private JComboBox<String> technicalSkillCombo, attitudeCombo,
            communicationSkillCombo, leadershipCombo;

    // 言語選択
    private JPanel languagePanel;
    private List<JCheckBox> languageCheckboxes = new ArrayList<>();

    // 経歴・備考
    private JTextArea careerArea, trainingHistoryArea, remarksArea;

    // フォームのクリア処理
    public void clearForm() {
        employeeIdField.setText("");
        nameField.setText("");
        phoneticField.setText("");
        programmingLanguageField.setText("");

        // 生年月日関連
        birthYearCombo.setSelectedIndex(-1);
        birthMonthCombo.setSelectedIndex(-1);
        birthDayCombo.setSelectedIndex(-1);

        // 入社年月
        joinYearCombo.setSelectedIndex(-1);
        joinMonthCombo.setSelectedIndex(-1);
    }

    public JTextField getProgrammingLanguageField() {
        return programmingLanguageField;
    }

    /** フォームレイアウト初期化 */
    public EmployeeFormPanel(JTextField employeeIdField) {
        this.employeeIdField = employeeIdField;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // 年月日データ生成
        String[] years = new String[100];
        int currentYear = java.time.Year.now().getValue();
        for (int i = 0; i < 100; i++) {
            years[i] = String.valueOf(currentYear - i);
        }

        // 月は「01」〜「12」
        String[] months = new String[12];
        for (int i = 0; i < 12; i++) {
            months[i] = String.format("%02d", i + 1);
        }

        // 日は「01」〜「31」
        String[] days = new String[31];
        for (int i = 0; i < 31; i++) {
            days[i] = String.format("%02d", i + 1);
        }

        // 左側パネル作成（元の配置順序を厳守）
        JPanel leftPanel = new JPanel(new GridBagLayout());
        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.anchor = GridBagConstraints.WEST;
        leftGbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;

        // フリガナフィールド
        leftGbc.gridx = 0;
        leftGbc.gridy = row;
        leftPanel.add(new JLabel("フリガナ:"), leftGbc);
        leftGbc.gridx = 1;
        phoneticField = new JTextField(25);
        leftPanel.add(phoneticField, leftGbc);

        // 名前フィールド
        row++;
        leftGbc.gridx = 0;
        leftGbc.gridy = row;
        leftPanel.add(new JLabel("名前:"), leftGbc);
        leftGbc.gridx = 1;
        nameField = new JTextField(25);
        leftPanel.add(nameField, leftGbc);

        // 生年月日（年、月、日のコンボボックス + ラベル）
        row++;
        leftGbc.gridx = 0;
        leftGbc.gridy = row;
        leftPanel.add(new JLabel("生年月日"), leftGbc);
        leftGbc.gridx = 1;
        JPanel birthDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        birthYearCombo = new JComboBox<>(years);
        birthDatePanel.add(birthYearCombo);
        birthDatePanel.add(new JLabel("年"));
        birthMonthCombo = new JComboBox<>(months);
        birthDatePanel.add(birthMonthCombo);
        birthDatePanel.add(new JLabel("月"));
        birthDayCombo = new JComboBox<>(days);
        birthDatePanel.add(birthDayCombo);
        birthDatePanel.add(new JLabel("日"));
        leftPanel.add(birthDatePanel, leftGbc);

        // 入社年月
        row++;
        leftGbc.gridx = 0;
        leftGbc.gridy = row;
        leftPanel.add(new JLabel("入社年月:"), leftGbc);

        leftGbc.gridx = 1;
        // 年と月をまとめたパネルを作る
        JPanel joinYearMonthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        joinYearCombo = new JComboBox<>(years);
        joinMonthCombo = new JComboBox<>(months);

        joinYearMonthPanel.add(joinYearCombo);
        joinYearMonthPanel.add(new JLabel("年"));
        joinYearMonthPanel.add(joinMonthCombo);
        joinYearMonthPanel.add(new JLabel("月"));

        leftPanel.add(joinYearMonthPanel, leftGbc);
        // エンジニア歴
        row++;
        leftGbc.gridx = 0;
        leftGbc.gridy = row;
        leftPanel.add(new JLabel("エンジニア開始年:"), leftGbc);

        leftGbc.gridx = 1;
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        engineerStartYearBox = new JComboBox<>(years);
        panel.add(engineerStartYearBox);
        panel.add(new JLabel(" 年"));
        leftPanel.add(panel, leftGbc);

        // 扱える言語
        row++;
        leftGbc.gridx = 0;
        leftGbc.gridy = row;
        leftGbc.anchor = GridBagConstraints.WEST;
        leftPanel.add(new JLabel("扱える言語:"), leftGbc);

        leftGbc.gridx = 1;
        programmingLanguageField = new JTextField(25);
        leftGbc.fill = GridBagConstraints.HORIZONTAL;
        leftPanel.add(programmingLanguageField, leftGbc);

        row++;

        // 「技術力」ラベル
        leftGbc.gridx = 0;
        leftGbc.gridy = row;
        leftGbc.weightx = 0;
        leftGbc.fill = GridBagConstraints.NONE;
        leftPanel.add(new JLabel("技術力"), leftGbc);
        // コンボボックス
        leftGbc.gridx = 1;
        technicalSkillCombo = createRatingCombo();
        leftGbc.weightx = 0;
        leftGbc.fill = GridBagConstraints.NONE;
        leftPanel.add(technicalSkillCombo, leftGbc);
        // 「/5」ラベル
        leftGbc.gridx = 2;
        leftPanel.add(new JLabel("/5"), leftGbc);

        // 受講態度
        row++;
        leftGbc.gridx = 0;
        leftGbc.gridy = row;
        leftPanel.add(new JLabel("受講態度:"), leftGbc);
        leftGbc.gridx = 1;
        attitudeCombo = createRatingCombo();
        leftPanel.add(attitudeCombo, leftGbc);
        leftGbc.gridx = 2;
        leftPanel.add(new JLabel(" /5"), leftGbc);

        // コミュニケーション能力
        row++;
        leftGbc.gridx = 0;
        leftGbc.gridy = row;
        leftPanel.add(new JLabel("コミュニケーション能力:"), leftGbc);
        leftGbc.gridx = 1;
        communicationSkillCombo = createRatingCombo();
        leftPanel.add(communicationSkillCombo, leftGbc);
        leftGbc.gridx = 2;
        leftPanel.add(new JLabel(" /5"), leftGbc);

        // リーダーシップ力
        row++;
        leftGbc.gridx = 0;
        leftGbc.gridy = row;
        leftPanel.add(new JLabel("リーダーシップ力:"), leftGbc);
        leftGbc.gridx = 1;
        leadershipCombo = createRatingCombo();
        leftPanel.add(leadershipCombo, leftGbc);
        leftGbc.gridx = 2;
        leftPanel.add(new JLabel(" /5"), leftGbc);

        // 右側テキストエリア（元の配置）
        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.insets = new Insets(5, 10, 5, 5);
        rightGbc.fill = GridBagConstraints.BOTH;
        rightGbc.weightx = 1;
        rightGbc.weighty = 1;

        careerArea = new JTextArea(8, 30);
        trainingHistoryArea = new JTextArea(8, 30);
        remarksArea = new JTextArea(8, 30);

        JTextArea[] areas = { careerArea, trainingHistoryArea, remarksArea };
        String[] labels = { "経歴", "研修の受講歴", "備考" };

        for (int i = 0; i < labels.length; i++) {
            rightGbc.gridx = 0;
            rightGbc.gridy = i * 2;
            rightPanel.add(new JLabel(labels[i]), rightGbc);

            rightGbc.gridy = i * 2 + 1;
            JScrollPane scrollPane = new JScrollPane(areas[i]);
            rightPanel.add(scrollPane, rightGbc);
        }

        // メインパネルに追加（元の並び順）
        add(leftPanel);
        add(rightPanel);
        initRealTimeValidation();
    }

    /** 評価用コンボボックス生成 */
    private JComboBox<String> createRatingCombo() {
        JComboBox<String> combo = new JComboBox<>(new String[] { "1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5" });
        combo.setSelectedIndex(8); // デフォルト5を選択
        return combo;
    }

    // === フィールドアクセサメソッド群 ===

    public JTextField getPhoneticField() {
        return phoneticField;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JComboBox<String> getBirthYearCombo() {
        return birthYearCombo;
    }

    public JComboBox<String> getBirthMonthCombo() {
        return birthMonthCombo;
    }

    public JComboBox<String> getBirthDayCombo() {
        return birthDayCombo;
    }

    public JComboBox<String> getJoinYearCombo() {
        return joinYearCombo;
    }

    public JComboBox<String> getJoinMonthCombo() {
        return joinMonthCombo;
    }

    // 生年月日を文字列で返す（YYYY/MM/DD）
    public String getBirthDate() {
        return String.format("%s/%s/%s",
                birthYearCombo.getSelectedItem(),
                birthMonthCombo.getSelectedItem(),
                birthDayCombo.getSelectedItem());
    }

    // 入社年月を文字列で返す（YYYY/MM）
    public String getJoinYearMonth() {
        return String.format("%s/%s",
                joinYearCombo.getSelectedItem(),
                joinMonthCombo.getSelectedItem());
    }

    public JComboBox<String> getEngineerStartYearBox() {
        return engineerStartYearBox;
    }

    public JComboBox<String> getTechnicalSkillCombo() {
        return technicalSkillCombo;
    }

    public JComboBox<String> getAttitudeCombo() {
        return attitudeCombo;
    }

    public JComboBox<String> getCommunicationSkillCombo() {
        return communicationSkillCombo;
    }

    public JComboBox<String> getLeadershipCombo() {
        return leadershipCombo;
    }

    public JTextArea getCareerArea() {
        return careerArea;
    }

    public JTextArea getTrainingHistoryArea() {
        return trainingHistoryArea;
    }

    public JTextArea getRemarksArea() {
        return remarksArea;
    }

    public JPanel getLanguagePanel() {
        return languagePanel;
    }

    /** 選択された言語リストを取得 */
    public List<String> getSelectedLanguages() {
        return languageCheckboxes.stream()
                .filter(JCheckBox::isSelected)
                .map(JCheckBox::getText)
                .collect(Collectors.toList());
    }

    /** バリデーション状態をUIに反映 */
    public void updateFieldValidation(Component field, boolean isValid) {
        ((JComponent) field).setBorder(new LineBorder(isValid ? Color.GRAY : Color.RED, 1));
    }

    /** リアルタイムバリデーション初期化 */
    private void initRealTimeValidation() {
        // テキストフィールド用バリデーション
        addTextValidation(nameField, text -> new Name(text));
        addTextValidation(phoneticField, text -> new Phonetic(text));
        addTextValidation(careerArea, text -> new Career(text));

        // コンボボックス用バリデーション
        addComboValidation(birthYearCombo, () -> new BirthDate(getBirthDate()));
        addComboValidation(birthMonthCombo, () -> new BirthDate(getBirthDate()));
        addComboValidation(birthDayCombo, () -> new BirthDate(getBirthDate()));
        addComboValidation(technicalSkillCombo, () -> new TechnicalSkill(
                (String) technicalSkillCombo.getSelectedItem()));
    }

    /** テキストコンポーネント用バリデーション追加 */
    private void addTextValidation(JTextComponent component, Function<String, EmployeeInfoValidator> validator) {
        component.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (isExiting)
                    return; // アプリ終了中ならスキップ
                validateComponent(component, validator.apply(component.getText().trim()));
            }
        });
    }

    /** コンボボックス用バリデーション追加 */
    private void addComboValidation(JComboBox<?> combo, Supplier<EmployeeInfoValidator> validatorSupplier) {
        combo.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (isExiting)
                    return; // アプリ終了中ならスキップ
                validateComponent(combo, validatorSupplier.get());
            }
        });
    }

    private void validateComponent(Component component, EmployeeInfoValidator validator) {
        try {
            String value = "";

            if (component instanceof JTextField) {
                value = ((JTextField) component).getText();
            } else if (component instanceof JComboBox) {
                Object selected = ((JComboBox<?>) component).getSelectedItem();
                value = (selected != null) ? selected.toString() : "";
            }

            if (!validator.validateInput(value)) {
                throw new IllegalArgumentException("入力が不正です");
            }

            updateFieldValidation(component, true); // OKなら更新
        } catch (IllegalArgumentException ex) {
            updateFieldValidation(component, false); // NGなら更新
        }
    }
}
