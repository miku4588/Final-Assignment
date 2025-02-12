import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;

public class ListViewUI {
    
    public ListViewUI() {
        // JFrameの設定
        JFrame frame = new JFrame("人材管理アプリ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null); // 画面中央に配置

        // JPanelを作成し、フレームに追加
        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        // ラベル
        JLabel text = new JLabel("エンジニア人材管理");
        text.setBounds(50,30,300,30);
        panel.add(text);

        // 検索テキスト入力欄
        JTextField searchBox = new JTextField(100);
        searchBox.setBounds(200, 30, 250, 30); // 位置とサイズを指定
        panel.add(searchBox);

        // 検索ボタンの作成
        JButton deleButton = new JButton("検索");
        deleButton.setBounds(450, 30, 100, 30);
        panel.add(deleButton);

        // 新規追加 (csv読込・1人追加)
        JButton addButton = new JButton("新規追加");
        addButton.setBounds(650,30,100,30);
        panel.add(addButton);

            // ポップアップメニューの作成
            JPopupMenu pupMenu = new JPopupMenu();

            // CSV読み込みをプルダウンメニューに追加
            JMenuItem button1 = new JMenuItem("CSV読込");
            pupMenu.add(button1);

            // 1人追加をプルダウンメニューに追加
            JMenuItem button2 = new JMenuItem("1人追加");
            pupMenu.add(button2);

            // ボタンをクリックした時にポップアップメニューを表示
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pupMenu.show(addButton, 0, addButton.getHeight());
                }
            });

        // CSVエクスポート
        JButton csvExportButton = new JButton("CSV出力");
        csvExportButton.setBounds(750,30,100,30);
        panel.add(csvExportButton);

        // 削除ボタン
        JButton deleteButton = new JButton("削除");
        deleteButton.setBounds(850,30,100,30);
        panel.add(deleteButton);

        // 従業員テーブル
        JTable employeeTable = new JTable();
        employeeTable.setBounds(50,150,900,500);
        panel.add(employeeTable);

        // フレームの可視化
        frame.setVisible(true);
    }
    
}
