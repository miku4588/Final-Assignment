import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class ListViewUI {
    // 従業員情報
    private EmployeeManager manager;
    // 件数表示
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
    private JButton searchButton, sortButton, addButton, csvExportButton, deleButton, selectAllButton, cancelAllButton;
    // 従業員表示テーブル
    private JTable employeeTable;

    CSVHandler csvHandler = new CSVHandler(MainApp.DATA_FILE);

    // 削除処理
    private EmployeeDeleter employeeDeleter = new EmployeeDeleter(csvHandler);//CSVHandlerのインスタンス

    /*
     * 一覧画面表示
     */
    public ListViewUI(EmployeeManager manager) {
        this.manager = manager;

        displayEmployees();
    }

    /*
     * 従業員表示メソッド
     */
    void displayEmployees() {

        // フレームの作成
        frame = new JFrame("人材管理アプリ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        // frame.setLocationRelativeTo(null);
        // パネルの作成
        panel = new JPanel();
        panel.setLayout(new BorderLayout()); // パネルの配置の決め方東西南北センター

        // ボタンの配置
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 3)); // 水平にボタンを配置

        // テキストの作成
        text = new JLabel("エンジニア人材管理");
        controlPanel.add(text);

        // 検索ボックス/検索ボタン 🔴編集途中
        searchBox = new JTextField(15);
        controlPanel.add(searchBox);
        searchButton = new JButton("検索");
        controlPanel.add(searchButton);
        // 検索処理
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchBox.getText();
                keywordSearch(keyword);
            }
        });

        // 絞り込みボタン 🔴編集途中
        sortButton = new JButton("絞り込み検索");
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 処理
            }
        });
        controlPanel.add(sortButton);

        // 新規追加ボタン 🔴編集途中
        addButton = new JButton("新規追加");
        controlPanel.add(addButton);
        // ポップアップメニューの作成
        JPopupMenu pupMenu = new JPopupMenu();
        // ⭐️⭐️⭐️ポップアップ１
        JMenuItem button1 = new JMenuItem("CSV読込");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CSVUI();
                // 一覧画面の更新
                reloadEmployeeTable();
            }
        });
        pupMenu.add(button1);
        // ポップアップ２
        JMenuItem button2 = new JMenuItem("1人追加");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);// フレームを非表示
                new AddEditUI();

                // 一覧画面の更新
                reloadEmployeeTable();
            }
        });
        pupMenu.add(button2);
        // プルダウンメニューの表示
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pupMenu.show(addButton, 0, addButton.getHeight());

            }
        });

        // CSVエクスポートボタン 🔴編集途中
        csvExportButton = new JButton("CSV出力");
        csvExportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        csvExportButton.setEnabled(false);// チェックボックスが押されるまで非アクティブ
        controlPanel.add(csvExportButton);

        // 削除ボタン 🔴編集途中
        deleButton = new JButton("削除");
        deleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDeleteDialog();
            }
        });
        deleButton.setEnabled(false);// チェックボックスが押されるまで非アクティブ
        controlPanel.add(deleButton);

        // 選択全件選択
        selectAllButton = new JButton("全件選択");
        selectAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 処理

                for (int row = 0; row < employeeTable.getRowCount(); row++) {
                    employeeTable.setValueAt(true, row, 0);
                }
            }
        });
        controlPanel.add(selectAllButton);

        // 選択解除
        cancelAllButton = new JButton("選択解除");
        cancelAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 処理
                for (int row = 0; row < employeeTable.getRowCount(); row++) {
                    employeeTable.setValueAt(false, row, 0);
                }
                updateCheckboxDependentButtons(); // 状態更新
            }
        });
        cancelAllButton.setEnabled(false);// チェックボックスが押されるまで非アクティブ
        controlPanel.add(cancelAllButton);

        JButton Button1 = new JButton("更新テスト");
        Button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadEmployeeTable();
            }
        });
        controlPanel.add(Button1);

        // 件数表示
        countLabel = new JLabel();
        controlPanel.add(countLabel);

        // JTable
        String[] columnNames = { "選択", "社員ID", "氏名", "年齢", "エンジニア歴", "扱える言語", "データ作成日", "最終更新日" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {

                // 年齢とエンジニア暦を数値にして・他を文字にする
                if (columnIndex == 0) { // チェックボックス列
                    return Boolean.class;
                } else if (columnIndex == 3 || columnIndex == 4) {
                    return Integer.class;
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

        for (EmployeeInfo emp : manager.getEmployeeList()) {
            Object[] rowData = new Object[] {
                    false, // チェックボックス
                    emp.getEmployeeId(), // 社員ID
                    emp.getName(), // 氏名
                    getAge(emp), // 年齢
                    calculateEngineerYears(emp), // エンジニア暦
                    emp.getLanguages(), // 言語
                    emp.getCreationDate(), // 作成日
                    emp.getLastUpdatedDate(),// 更新日
            };
            model.addRow(rowData);
        }

        employeeTable = new JTable(model);
        updateEmployeeCountLabel();

        // ソート機能を追加🟢
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        sorter.setSortable(0, false); // チェックボックス列はソートしない
        employeeTable.setRowSorter(sorter);

        // チェックボックスの変更を監視
        employeeTable.getModel().addTableModelListener(new javax.swing.event.TableModelListener() {
            @Override
            public void tableChanged(javax.swing.event.TableModelEvent e) {
                updateCheckboxDependentButtons();
            }
        });
        employeeTable.setRowHeight(60);
        employeeTable.getTableHeader().setReorderingAllowed(false); // ヘッダーの列の入れ替えを無効にする

        JScrollPane scrollPane = new JScrollPane(employeeTable);// スクロールパネルにする

        int maxVisibleRows = 10;
        int tableHeight = 60 * maxVisibleRows;
        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, tableHeight));

        panel.add(scrollPane);

        // フレームにパネルを追加
        frame.add(panel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.NORTH);

        // フレームを表示
        frame.setVisible(true);

        System.out.println("ここには" + model.getRowCount());
    }

    // 削除ダイアログ
    void showDeleteDialog() {
        List<String> selectedIds = getSelectedEmployeeIds();

        // 渡せてるかテスト
        StringBuilder sb = new StringBuilder();
        for (String id : selectedIds) {
            sb.append(id).append("\n");
        }
        JOptionPane.showMessageDialog(frame, sb.toString(), "選択された従業員ID", JOptionPane.INFORMATION_MESSAGE);

        int result = JOptionPane.showConfirmDialog(frame, "削除したデータは復元できません。削除しますか。", "確認", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            // 処理中ダイアログ作成（モーダル・タイトルなし・×なし）
            JDialog processingDialog = new JDialog(frame, "削除中...", true);
            processingDialog.setUndecorated(true);
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.add(new JLabel("削除中です。しばらくお待ちください。"), BorderLayout.CENTER);
            processingDialog.getContentPane().add(panel);
            processingDialog.pack();
            processingDialog.setLocationRelativeTo(frame);

            // null チェック
            if (employeeDeleter == null) {
            System.err.println("null");
        }
            // 削除処理を別スレッドで実行
            Thread deleteThread = new Thread(() -> {
                final boolean[] allSuccess = { true };

                for (String employeeId : selectedIds) {
                    boolean success = employeeDeleter.deleteEmployee(employeeId);
                    if (!success) {
                        allSuccess[0] = false;
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame,
                                "従業員ID " + employeeId + " の削除に失敗しました。", "エラー", JOptionPane.ERROR_MESSAGE));
                    }
                }

                SwingUtilities.invokeLater(() -> {
                    processingDialog.dispose();
                    displayEmployees();
                    if (allSuccess[0]) {
                        JOptionPane.showMessageDialog(frame, "選択された従業員を削除しました。", "完了", JOptionPane.INFORMATION_MESSAGE);
                    }
                });
            });

            deleteThread.start();
            // モーダルダイアログを表示（このスレッドはここで一旦停止）
            processingDialog.setVisible(true);
        }
    }

    /**
     * 年齢を取得
     */
    private int getAge(EmployeeInfo emp) {
        LocalDate birthDate = emp.getBirthDate();
        LocalDate now = LocalDate.now();

        int age = now.getYear() - birthDate.getYear();

        // まだ誕生日来てなければ1引く
        if (now.getDayOfYear() < birthDate.getDayOfYear()) {
            age--;
        }
        return age;
    }

    /**
     * 暦を取得
     */
    private int calculateEngineerYears(EmployeeInfo emp) {
        int startYear = emp.getEngineerStartYear().getValue();
        int years = LocalDate.now().getYear() - startYear;

        return years;
    }

    /**
     * 従業員人数表示メソッド
     */
    private void updateEmployeeCountLabel() {
        //employeeTableの件数を渡す
        int rowCount = employeeTable.getRowCount();
        countLabel.setText("表示件数: " + rowCount + " 件");
    }

    /**
     * 一覧画面更新メソッド
     */
    private void reloadEmployeeTable() {
        //null チェック
        if (employeeTable == null) {
            System.err.println("null");
        }
        DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();

        model.setRowCount(0); // 一旦クリア

        for (EmployeeInfo emp : manager.getEmployeeList()) {
            Object[] rowData = new Object[] {
                    false, // チェックボックス
                    emp.getEmployeeId(), // 社員ID
                    emp.getName(), // 氏名
                    getAge(emp), // 年齢
                    calculateEngineerYears(emp), // エンジニア暦
                    emp.getLanguages(), // 言語
                    emp.getCreationDate(), // 作成日
                    emp.getLastUpdatedDate(),// 更新日
            };
            model.addRow(rowData);
        }
        updateEmployeeCountLabel();
    }

    /**
     * チェックボックス 選択後の処理
     */
    private void updateCheckboxDependentButtons() {
        boolean isAnyChecked = false;
        boolean isAllChecked = true;

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
        deleButton.setEnabled(isAnyChecked);
        csvExportButton.setEnabled(isAnyChecked);

    }

    /**
     * チェックボックスで選択された従業員のIDを取得するメソッド
     */
    private List<String> getSelectedEmployeeIds() {
        // 空のリストを作成
        List<String> selectedIds = new ArrayList<>();
        // テーブルの中身を取得
        DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
        // テーブルのすべての行で処理を行う
        for (int viewRow = 0; viewRow < employeeTable.getRowCount(); viewRow++) {
            int modelRow = employeeTable.convertRowIndexToModel(viewRow); // ソート対応
            Boolean isChecked = (Boolean) model.getValueAt(modelRow, 0);
            // trueの時だけ処理
            if (isChecked != null && isChecked) {
                // 二行目だけ取得(ID)
                String employeeId = model.getValueAt(modelRow, 1).toString();
                selectedIds.add(employeeId);
            }
        }

        return selectedIds;
    }

    /**
     * 検索メソッド
     */
    void keywordSearch(String keyword) {
        SearchCriteria filter = new SearchCriteria(keyword);

        // DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
        // model.setRowCount(0); // 既存の行を削除

        List<EmployeeInfo> filteredList = new ArrayList<>();
        for (EmployeeInfo emp : manager.getEmployeeList()) {
            if (filter.matches(emp)) {
                filteredList.add(emp);
            }
        }

        System.out.println("=== filteredList の内容 ===");
        for (EmployeeInfo emp : filteredList) {
            System.out.println("社員ID: " + emp.getEmployeeId()
                    + ", 氏名: " + emp.getName()
                    + ", 氏名カナ: " + emp.getPhonetic());
        }
        System.out.println("========================");
        System.out.println("処理完了");

        DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
        // model.setRowCount(0);

        for (EmployeeInfo emp : filteredList) {
            Object[] rowData = new Object[] {
                    false, // チェックボックス
                    emp.getEmployeeId(),
                    emp.getName(),
                    getAge(emp),
                    calculateEngineerYears(emp),
                    emp.getLanguages(),
                    emp.getCreationDate(),
                    emp.getLastUpdatedDate(),
            };
            model.addRow(rowData);
        }
        updateEmployeeCountLabel(); // 件数表示も更新
    }
}
