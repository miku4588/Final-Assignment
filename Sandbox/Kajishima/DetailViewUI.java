import javax.swing.*;
import java.awt.*;

public class DetailViewUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("社員情報");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLayout(new BorderLayout());

            // 上部パネル
            JPanel topPanel = new JPanel(new BorderLayout());
            JPanel leftTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel employeeIdLabel = new JLabel("社員ID: 12345");
            JButton backButton = new JButton("一覧に戻る");
            leftTopPanel.add(employeeIdLabel);
            leftTopPanel.add(backButton);

            JPanel rightTopPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton editButton = new JButton("編集");
            JButton deleteButton = new JButton("削除");
            rightTopPanel.add(editButton);
            rightTopPanel.add(deleteButton);

            topPanel.add(leftTopPanel, BorderLayout.WEST);
            topPanel.add(rightTopPanel, BorderLayout.EAST);

            // 中央の情報表示エリア
            JPanel infoPanel = new JPanel(new GridLayout(1, 2, 10, 10));
            JPanel leftInfoPanel = new JPanel(new GridLayout(3, 1));
            leftInfoPanel.add(new JLabel("氏名: 山田 太郎"));
            leftInfoPanel.add(new JLabel("生年月日: 1990-01-01"));
            leftInfoPanel.add(new JLabel("入社年月: 2015-04"));

            JTextArea careerTextArea = new JTextArea("経歴:\n2015-2018: 営業部\n2018-2021: 開発部\n2021-現在: 管理職");
            careerTextArea.setEditable(false);
            careerTextArea.setLineWrap(true);
            careerTextArea.setWrapStyleWord(true);
            JScrollPane careerScrollPane = new JScrollPane(careerTextArea);
            
            infoPanel.add(leftInfoPanel);
            infoPanel.add(careerScrollPane);

            // 下部パネル
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JLabel createdDateLabel = new JLabel("データ作成日: 2023-01-01");
            JLabel updatedDateLabel = new JLabel("最終更新日: 2024-01-01");
            bottomPanel.add(createdDateLabel);
            bottomPanel.add(updatedDateLabel);

            // フレームにコンポーネントを追加
            frame.add(topPanel, BorderLayout.NORTH);
            frame.add(infoPanel, BorderLayout.CENTER);
            frame.add(bottomPanel, BorderLayout.SOUTH);

            frame.setVisible(true);
        });
    }
}
