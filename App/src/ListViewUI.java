import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class ListViewUI {
    // 従業員情報
    private EmployeeManager manager;
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
                // 入力された文字列を取得
                String keyword = searchBox.getText();
                System.out.println(keyword);
            }
        });

        // 絞り込みボタン 🔴編集途中
        sortButton = new JButton("絞り込み");
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 処理
            }
        });
        controlPanel.add(sortButton);

        // 新規追加ボタン  🔴編集途中
        addButton = new JButton("新規追加");
        controlPanel.add(addButton);
        // ポップアップメニューの作成
        JPopupMenu pupMenu = new JPopupMenu();
        // ポップアップ１
        JMenuItem button1 = new JMenuItem("CSV読込");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 処理
                JOptionPane.showConfirmDialog(frame, "CSV読み込みが押されました", "", JOptionPane.NO_OPTION);
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

        // CSVエクスポートボタン  🔴編集途中
        csvExportButton = new JButton("CSV出力");
        csvExportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCSVExportDialog();
            }
        });
        csvExportButton.setEnabled(false);// チェックボックスが押されるまで非アクティブ
        controlPanel.add(csvExportButton);

        // 削除ボタン  🔴編集途中
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
        cancelAllButton = new JButton("全件解除");
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


        // JTable
        String[] columnNames = { "選択", "社員ID", "氏名", "年齢", "エンジニア暦", "扱える言語", "データ作成日", "最終更新日" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {

                //年齢とエンジニア暦を数値にして・他を文字にする
                if (columnIndex == 0) { // チェックボックス列
                    return Boolean.class;
                } else if (columnIndex == 3 || columnIndex == 4) {
                    return Integer.class;
                } else {
                    return String.class;
                }
            }
            //🔳上記のコード変更前（ソートできない状態）
            //     // 先頭の「選択」列はチェックボックスにする
            //     return columnIndex == 0 ? Boolean.class : String.class;
            // }
            // @Override
            // public boolean isCellEditable(int row, int column) {
            //     return column == 0; // チェックボックスだけ編集可能
            // }

        };

        for (EmployeeInfo emp : manager.getEmployeeList()) {
            Object[] rowData = new Object[] {
                    false,                                  // チェックボックス
                    emp.getEmployeeId(),    // 社員ID
                    emp.getName(),                // 氏名
                    getAge(emp),                            // 年齢
                    calculateEngineerYears(emp),            // エンジニア暦
                    // 作成日
                    // 更新日
            };
            model.addRow(rowData);
        }

        employeeTable = new JTable(model);


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
    }

    // 削除ダイアログを表示するメソッド
    void showDeleteDialog() {
        int choice = JOptionPane.showConfirmDialog(frame, "入力した情報が破棄されます。よろしいですか？", "警告", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            // 🔴はいの処理

        } else if (choice == JOptionPane.NO_OPTION) {
            // 🔴いいえの処理
        }
    }

    // CSVエクスポート確認ダイアログを表示するメソッド
    void showCSVExportDialog() {
        int choice = JOptionPane.showConfirmDialog(frame, "選択されたデータを出力します。", "", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            // 🔴はいの処理
            JOptionPane.showMessageDialog(frame, "保存中です。", "処理中", JOptionPane.INFORMATION_MESSAGE);
            // 🔴処理
            JOptionPane.showMessageDialog(frame, "保存しました。", "完了", JOptionPane.INFORMATION_MESSAGE);

        } else if (choice == JOptionPane.NO_OPTION) {
            // 🔴いいえの処理

        }

    }


    // 年齢と暦計算メソッド 4/22追加 相談未

    // 年齢を取得（表示用）
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

    // 暦を取得（表示用）
    private int calculateEngineerYears(EmployeeInfo emp) {
        int startYear = emp.getEngineerStartYear().getValue();
        int years = LocalDate.now().getYear() - startYear;

        return years;
    }

    //チェックボックス 選択後の処理
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
}