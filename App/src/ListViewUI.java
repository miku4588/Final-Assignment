import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class ListViewUI {

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
     * テスト用Min
     */
    public static void main(String[] args) {
        new ListViewUI();
    }

    /*
     * 一覧画面表示
     */
    public ListViewUI() {
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
        // 検索ボックス/検索ボタン
        searchBox = new JTextField(15);
        controlPanel.add(searchBox);
        searchButton = new JButton("検索");
        controlPanel.add(searchButton);
        //検索処理
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchBox.getText(); // 検索ボックスの内容を取得
                // 🔴検索の処理
                    System.out.println("検索文字列: " + searchText);
                    JOptionPane.showMessageDialog(frame, "検索: " + searchText);
                
            }
        });

        // 絞り込みボタン
        sortButton = new JButton("絞り込み");
        controlPanel.add(sortButton);
        // 新規追加ボタン
        addButton = new JButton("新規追加");
        controlPanel.add(addButton);
        // ポップアップメニューの作成
        JPopupMenu pupMenu = new JPopupMenu();
        
        JMenuItem button1 = new JMenuItem("CSV読込");
        button1.addActionListener(e -> {
        // CSV読み込みが押された時の処理
            JOptionPane.showConfirmDialog(frame, "CSV読み込みが押されました", "", JOptionPane.NO_OPTION);
        });
        pupMenu.add(button1);
        JMenuItem button2 = new JMenuItem("1人追加");
        // 1人追加が押された時の処理
        button2.addActionListener(e -> {
            frame.setVisible(false);//フレームを非表示
            new AddEditUI();
        });
        pupMenu.add(button2);
        //プルダウンメニューの表示
        addButton.addActionListener(e -> pupMenu.show(addButton, 0, addButton.getHeight()));

        // CSVエクスポートボタン
        csvExportButton = new JButton("CSV出力");
        csvExportButton.addActionListener(e -> showCSVExportDialog());
        csvExportButton.setEnabled(false);//チェックボックスが押されるまで非アクティブ
        controlPanel.add(csvExportButton);
        // 削除ボタン
        deleButton = new JButton("削除");
        deleButton.addActionListener(e -> showDeleteDialog());
        deleButton.setEnabled(false);//チェックボックスが押されるまで非アクティブ
        controlPanel.add(deleButton);

        // 選択解除
        selectAllButton = new JButton("全件選択");
        selectAllButton.addActionListener(e -> {
            // 🔴全件選択が押された時の処理
        });
        selectAllButton.setEnabled(false);//チェックボックスが押されるまで非アクティブ
        controlPanel.add(selectAllButton);
        // 選択解除
        cancelAllButton = new JButton("全件解除");
        cancelAllButton.addActionListener(e -> {
            // 🔴全件解除が押された時の処理
        });
        cancelAllButton.setEnabled(false);//チェックボックスが押されるまで非アクティブ
        controlPanel.add(cancelAllButton);

        // JTable
        DefaultTableModel model = new DefaultTableModel();
        employeeTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(employeeTable);//スクロールパネルにする
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
}
