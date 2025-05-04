import java.util.List;
import java.util.Iterator;

public class EmployeeDeleter {
    private CSVHandler csvHandler;

    public EmployeeDeleter(CSVHandler csvHandler) {
        this.csvHandler = csvHandler;
    }

    // 社員情報を削除する
    public boolean deleteEmployee(String employeeId) {
        // EmployeeInfoリストを取得
        List<EmployeeInfo> employeeList = csvHandler.readCSV();
        if (employeeList == null) {
            System.out.println("社員情報の読み込みに失敗しました。");
            return false;
        }

        // 指定された社員IDに該当する社員情報を削除
        for (Iterator<EmployeeInfo> iterator = employeeList.iterator(); iterator.hasNext();) {
            EmployeeInfo employee = iterator.next();
            if (employee.getEmployeeId().equals(employeeId)) {
                iterator.remove();
                System.out.println("社員情報が削除されました。");

                // 更新後にCSVファイルに書き込み
                csvHandler.writeCSV(employeeList);
                return true;
            }
        }
        System.out.println("社員IDが見つかりませんでした。");
        return false;
    }
}
