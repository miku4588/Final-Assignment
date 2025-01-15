import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTable;

public class DetailViewUI  {
    private JTable employeeInfoTable;
    private JButton editButton;
    private JButton deleteButton;
    private JButton backButton;
    private EmployeeInfo employeeInfo;

    public DetailViewUI(EmployeeInfo employeeInfo) {
        this.employeeInfo = employeeInfo;
        initializeUI();
    }

    private void initializeUI() {
        // UI初期化コード
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 編集処理
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 削除処理
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 一覧画面に戻る処理
            }
        });
    }

    public void displayDetail() {
        // 詳細情報を表示する処理
    }
}
