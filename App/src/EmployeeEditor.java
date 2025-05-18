import java.util.List;

/**
 * 社員情報を編集するためのクラス
 */
public class EmployeeEditor {
    private EmployeeManager employeeManager; // 社員管理を担当するオブジェクト
    private CSVHandler csvHandler; // CSVファイルの読み書きを担当するオブジェクト

    public EmployeeEditor(EmployeeManager employeeManager, CSVHandler csvHandler) {
        this.employeeManager = employeeManager;
        this.csvHandler = csvHandler;
    }

    /**
     * 社員情報を更新するメソッド
     * 
     * @param targetId 更新対象の社員ID
     * @param newName 新しい名前
     * @param newPhonetic 新しいフリガナ
     * @param newBirthDate 新しい生年月日
     * @return 更新が成功した場合はtrue、失敗した場合はfalse
     */
    public boolean editEmployee(String targetId, String newName, String newPhonetic, String newBirthDate) {
        List<EmployeeInfo> employeeList = employeeManager.getEmployeeList();

        for (EmployeeInfo employee : employeeList) {
            if (employee.getEmployeeId().getEmployeeId().equals(targetId)) {
                try {
                    Name name = new Name(newName);
                    Phonetic phonetic = new Phonetic(newPhonetic);
                    BirthDate birthDate = new BirthDate(newBirthDate);

                    employee.setName(name);
                    employee.setPhonetic(phonetic);
                    employee.setBirthDate(birthDate);

                    // 書き込み処理を実行
                    csvHandler.writeCSV(employeeList);

                    // 書き込み後に確認のためにログを表示
                    System.out.println("社員情報を更新しました。");
                    return true; // 更新成功
                } catch (IllegalArgumentException e) {
                    System.out.println("入力エラー: " + e.getMessage());
                    return false; // 更新失敗
                }
            }
        }
        System.out.println("該当する社員IDが見つかりませんでした。");
        return false; // 更新失敗
    }
}
