import java.awt.*;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.event.*;


public class ListViewUI {
    // ロガーを取得
    private static final EmployeeInfoLogger LOGGER = EmployeeInfoLogger.getInstance();
    // 従業員情報
    private EmployeeManager manager;
    // 検索結果の件数表示
    private JLabel countLabel;
    // フレーム
    private JFrame frame;
    // パネル
    private JPanel panel;
    // 検索ボックス
    private JTextField searchBox;
    // ラベル
    private JLabel text;
    // 各種ボタン
    private JButton searchButton, filteredSearchButton, addButton, csvExportButton, deleteButton, selectAllButton, cancelAllButton;
    // 従業員表示テーブル
    private JTable employeeTable;
    // テーブルモデル（テーブル内のデータ）
    private DefaultTableModel model;
    // ソーター（ソート機能）
    private TableRowSorter<DefaultTableModel> sorter;

    /**
     * コンストラクタ
     * 
     * @param manager
     */
    public ListViewUI(EmployeeManager manager) {
        this.manager = manager;
        setupTable(); // 社員情報のテーブルを設定
        displayEmployees(); // UIを表示
    }

    /**
     * 一覧画面表示
     */
    private void displayEmployees() {

        // フレーム
        frame = new JFrame("人材管理アプリ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);

        // 全体パネル
        panel = new JPanel();
        panel.setLayout(new BorderLayout()); // パネルの配置の決め方東西南北センター

        // 上部ボタン用パネル
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 3)); // 水平にボタンを配置

        // 左上のテキスト
        text = new JLabel("エンジニア人材管理");
        controlPanel.add(text);

        // 検索ボックスと検索ボタン
        searchBox = new JTextField(15);
        controlPanel.add(searchBox);
        searchButton = new JButton("検索");
        controlPanel.add(searchButton);
        // 検索処理
        searchButton.addActionListener(e -> {
            String keyword = searchBox.getText();
            keywordSearch(keyword);
        });

        // 絞り込みボタン 🔴機能落ちさせます。。。
        filteredSearchButton = new JButton("絞り込み検索");
        filteredSearchButton.setEnabled(false); // 機能が使えないため非活性にしておく
        controlPanel.add(filteredSearchButton);

        // 新規追加ボタン
        addButton = new JButton("新規追加");
        controlPanel.add(addButton);
        // ポップアップメニューの作成
        JPopupMenu pupMenu = new JPopupMenu();
        JMenuItem button1 = new JMenuItem("CSV読込");
        JMenuItem button2 = new JMenuItem("1名追加");
        pupMenu.add(button1);
        pupMenu.add(button2);
        // ポップアップ1　CSV読込
        button1.addActionListener(e -> {
            CSVUI csvui = new CSVUI(frame);
            // csvuiが閉じられたら
            csvui.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    keywordSearch(null); // 画面を更新するため、キーワードなしで検索
                }
            });
        });
        // ポップアップ2　1名追加
        button2.addActionListener(e -> {
            frame.setVisible(false);// フレームを非表示
            new AddEditUI();
        });
        // プルダウンメニューの表示
        addButton.addActionListener(e -> {
            pupMenu.show(addButton, 0, addButton.getHeight());
        });

        // CSVエクスポートボタン
        csvExportButton = new JButton("CSV出力");
        csvExportButton.addActionListener(e -> {
            if(CSVHandler.tryExportTemplateCSV(getSelectedEmployeeInfos())) {
                JOptionPane.showMessageDialog(frame, "CSVテンプレートを出力しました。", "テンプレート出力完了", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        csvExportButton.setEnabled(false);// チェックボックスが押されるまで非アクティブ
        controlPanel.add(csvExportButton);

        // 削除ボタン
        deleteButton = new JButton("削除");
        deleteButton.addActionListener(e -> {
            showDeleteDialog();
        });
        deleteButton.setEnabled(false);// チェックボックスが押されるまで非アクティブ
        controlPanel.add(deleteButton);

        // 選択全件選択
        selectAllButton = new JButton("全件選択");
        selectAllButton.addActionListener(e -> {
            for (int row = 0; row < employeeTable.getRowCount(); row++) {
                employeeTable.setValueAt(true, row, 0);
            }
        });
        controlPanel.add(selectAllButton);

        // 選択解除
        cancelAllButton = new JButton("選択解除");
        cancelAllButton.addActionListener(e -> {
            for (int row = 0; row < employeeTable.getRowCount(); row++) {
                employeeTable.setValueAt(false, row, 0);
            }
        });
        cancelAllButton.setEnabled(false);// チェックボックスが押されるまで非アクティブ
        controlPanel.add(cancelAllButton);

        // 件数表示
        countLabel = new JLabel();
        controlPanel.add(countLabel);
        countLabel.setText("表示件数: " + employeeTable.getRowCount() + " 件"); // 社員の人数を更新


        // チェックボックスの変更を監視
        employeeTable.getModel().addTableModelListener(e -> {
            updateCheckboxDependentButtons();
        });
        employeeTable.setRowHeight(60);
        employeeTable.getTableHeader().setReorderingAllowed(false); // ヘッダーの列の入れ替えを無効にする

        // テーブルをスクロール可能にする
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        int maxVisibleRows = 10;
        int tableHeight = 60 * maxVisibleRows;
        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, tableHeight));
        panel.add(scrollPane);

        // フレームにパネルを追加
        frame.add(panel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.NORTH);

        // フレームを表示
        frame.setVisible(true);
    }


    /**
     * テーブルの設定
     */
    private void setupTable() {
        // テーブルモデルを設定
        String[] columnNames = { "選択", "社員ID", "氏名", "年齢", "エンジニア歴", "扱える言語", "データ作成日", "最終更新日" };
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // 1列目はチェックボックス、他は文字
                if (columnIndex == 0) {
                    return Boolean.class;
                } else {
                    return String.class;
                }
            }

            // チェックボックス（選択列）のみ編集可能
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // チェックボックス（選択列）のみ編集可能
            }
        };

        // 社員情報をテーブルモデルにセット
        for (EmployeeInfo emp : manager.getEmployeeList()) {
            Object[] rowData = new Object[] {
                    false, // チェックボックス
                    emp.getEmployeeId(), // 社員ID
                    emp.getName(), // 氏名
                    calculateAge(emp), // 年齢
                    calculateEngineerYears(emp), // エンジニア暦
                    emp.getLanguages(), // 言語
                    emp.getCreationDate(), // 作成日
                    emp.getLastUpdatedDate(),// 更新日
            };
            model.addRow(rowData);
        }
        employeeTable = new JTable(model);

        // 社員ID列の見た目をリンク文字っぽく
        employeeTable.getColumnModel().getColumn(1)
                .setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
                    JLabel label = new JLabel("<html><u><font color='#0088ff'>" + value + "</font></u></html>");
                    if (isSelected) {
                        label.setBackground(table.getSelectionBackground());
                        label.setOpaque(true);
                    }
                    return label;
                });

        // マウスオーバーでカーソルの形を変更
        employeeTable.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int col = employeeTable.columnAtPoint(e.getPoint()); // マウスポインタの位置（何列目か）
                if (col <= 1) {
                    employeeTable.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // 手の形
                } else {
                    employeeTable.setCursor(Cursor.getDefaultCursor()); // デフォルトの形
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // 使わないけど書かないとエラーになる
            }
        });

        // 社員IDがクリックされたら詳細情報画面を表示
        employeeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = employeeTable.rowAtPoint(e.getPoint()); // マウスポインタの位置（何行目か）
                int col = employeeTable.columnAtPoint(e.getPoint()); // マウスポインタの位置（何列目か）
                if (col == 1 && row >= 0) {
                    int modelRow = employeeTable.convertRowIndexToModel(row);
                    String employeeId = model.getValueAt(modelRow, 1).toString();
                    frame.setVisible(false);// フレームを非表示
                    new DetailViewUI(employeeId);
                }
            }
        });

        // ソーターを設定
        sorter = new TableRowSorter<>(model);
        employeeTable.setRowSorter(sorter);
        sorter.setSortable(0, false); // チェックボックス列はソートボタンなし
    }


    /**
     * 削除確認ダイアログ
     */
    private void showDeleteDialog() {
        List<String> selectedIds = getSelectedEmployeeIds();
        int result = JOptionPane.showConfirmDialog(frame, "削除したデータは復元できません。削除しますか。", "確認", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            // 処理中メッセージ作成
            JDialog processingDialog = new JDialog(this.frame, true); // true…親ウィンドウの操作をブロック
            processingDialog.setUndecorated(true); // タイトルバーを消す（×ボタンも消える）
            JPanel processingPanel = new JPanel();
            processingPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // 黒い枠線
            processingPanel.add(new JLabel("削除中です。"));
            processingDialog.getContentPane().add(processingPanel);
            processingDialog.pack(); // ウィンドウサイズ自動調整
            processingDialog.setLocationRelativeTo(this.frame); // 表示位置は親ウィンドウが基準

            // SwingWorker定義（削除処理と「削除完了」メッセージ表示）
            SwingWorker<Boolean, Void> deleter = new SwingWorker<Boolean, Void>() {

                // バックグラウンドでやる処理
                @Override
                protected Boolean doInBackground() throws Exception {
                    for (String employeeId : selectedIds) {
                        boolean isSuccess = EmployeeDeleter.deleteEmployee(employeeId);
                        if (!isSuccess) {
                            JOptionPane.showMessageDialog(frame, "従業員ID " + employeeId + " の削除に失敗しました。\nこれ以降の削除処理は行わず中断します。", "エラー",JOptionPane.ERROR_MESSAGE);
                            return false; // 失敗したらループから強制離脱
                        }
                    }
                    return true;
                };

                // バックグラウンド処理が終わったら実行
                @Override
                protected void done() {
                    processingDialog.dispose();

                    try {
                        Boolean result = get(); // バックグラウンド処理の結果を取得
                        if (result) {
                            JOptionPane.showMessageDialog(frame, "削除が完了しました。", "削除完了",JOptionPane.INFORMATION_MESSAGE);
                            LOGGER.logOutput("削除が完了しました。");
                        }
                    } catch (Exception e) {
                        LOGGER.logException("予期せぬエラーが発生しました。", e);
                        ErrorHandler.showErrorDialog("予期せぬエラーが発生しました。");
                    }

                };
            };

            deleter.execute(); // SwingWorker実行
            processingDialog.setVisible(true); // 可視化
        }
    }


    /**
     * 生年月日から年齢を算出
     * @param emp 社員情報
     * @return 年齢
     */
    private String calculateAge(EmployeeInfo emp) {
        LocalDate birthDate = emp.getBirthDate();

        // 生年月日が未入力だった場合の対応
        if (birthDate == null) 
            return "";

        LocalDate now = LocalDate.now();
        int age = now.getYear() - birthDate.getYear();

        // まだ誕生日来てなければ1引く
        if (now.getDayOfYear() < birthDate.getDayOfYear())
            age--;

        return String.valueOf(age);
    }


    /**
     * エンジニア開始年からエンジニア歴を算出
     * @param emp 社員情報
     * @return エンジニア歴
     */
    private String calculateEngineerYears(EmployeeInfo emp) {
        Year startYear = emp.getEngineerStartYear();

        // エンジニア開始年が未入力だった場合の対応
        if (startYear == null) 
            return "";

        int years = LocalDate.now().getYear() - startYear.getValue();
        return String.valueOf(years);
    }


    /**
     * チェックボックスの選択状態から各ボタンの活性・非活性を切り替え
     */
    private void updateCheckboxDependentButtons() {
        // もしテーブル内にデータが1行もなければ4つとも非活性
        if (employeeTable.getRowCount() == 0) {
            cancelAllButton.setEnabled(false);
            selectAllButton.setEnabled(false);
            deleteButton.setEnabled(false);
            csvExportButton.setEnabled(false);
            return;
        }
        
        // 1件以上データがあるならチェックボックスの状態を確認
        boolean isAnyChecked = false; // 1件以上選択されている状態
        boolean isAllChecked = true; // 全件が選択済みの状態
        for (int row = 0; row < employeeTable.getRowCount(); row++) {
            boolean isSelected = (Boolean) employeeTable.getValueAt(row, 0);
            if (isSelected) {
                isAnyChecked = true;
            } else {
                isAllChecked = false;
            }
        }
        cancelAllButton.setEnabled(isAnyChecked);
        selectAllButton.setEnabled(!isAllChecked);
        deleteButton.setEnabled(isAnyChecked);
        csvExportButton.setEnabled(isAnyChecked);
    }


    /**
     * チェックボックスで選択された社員の社員IDをリスト化
     * @return 選択された社員の社員IDのリスト
     */
    private List<String> getSelectedEmployeeIds() {
        List<String> selectedIds = new ArrayList<>();

        // テーブルの全行を走査
        for (int viewRow = 0; viewRow < employeeTable.getRowCount(); viewRow++) {
            int modelRow = employeeTable.convertRowIndexToModel(viewRow); // ソート対応
            Boolean isChecked = (Boolean) model.getValueAt(modelRow, 0);
            // trueの時だけ処理
            if (isChecked != null && isChecked) {
                String employeeId = model.getValueAt(modelRow, 1).toString(); // 2列目（社員ID）を取得
                selectedIds.add(employeeId);
            }
        }

        return selectedIds;
    }


    /**
     * チェックボックスで選択された社員の情報をリスト化
     * @return 選択された社員のリスト
     */
    private List<EmployeeInfo> getSelectedEmployeeInfos() {
        List<EmployeeInfo> employees = EmployeeManager.getEmployeeList(); // データリスト
        List<String> selectedIds = getSelectedEmployeeIds(); // 選択された社員IDのリスト
        List<EmployeeInfo> selectedEmployees = new ArrayList<>(); // 選択された社員情報のリスト（空）

        // 選択された社員IDに対して繰り返し処理
        for(String employeeId : selectedIds) {
            // データリストの中から社員IDが一致する社員情報を探す
            for(EmployeeInfo employee : employees) {
                if (employee.getEmployeeId().equals(employeeId)) {
                    selectedEmployees.add(employee);
                    break;
                }
            }
        }

        return selectedEmployees;
    }


    /**
     * 検索機能
     * @param keyword 検索ワード
     */
    void keywordSearch(String keyword) {
        // 新しいリストに検索結果を追加
        SearchCriteria filter = new SearchCriteria(keyword);
        List<EmployeeInfo> filteredList = new ArrayList<>();
        for (EmployeeInfo emp : manager.getEmployeeList()) {
            if (filter.matches(emp)) {
                filteredList.add(emp);
            }
        }

        // EDTの中でmodelの差し替え
        SwingUtilities.invokeLater(() -> {
            // ソーターを外さないとmodelが空にできないので外す
            employeeTable.setRowSorter(null);
            model.setRowCount(0);

            // 検索結果をmodelに追加
            for (EmployeeInfo emp : filteredList) {
                Object[] rowData = new Object[] {
                        false,
                        emp.getEmployeeId(),
                        emp.getName(),
                        calculateAge(emp),
                        calculateEngineerYears(emp),
                        emp.getLanguages(),
                        emp.getCreationDate(),
                        emp.getLastUpdatedDate(),
                };
                model.addRow(rowData);
            }
            countLabel.setText("表示件数: " + employeeTable.getRowCount() + " 件"); // 件数表示

            // 新しいソーターを設定
            sorter = new TableRowSorter<>(model);
            employeeTable.setRowSorter(sorter);
            sorter.setSortable(0, false); // チェックボックス列はソートボタンなし
        });
    }
}