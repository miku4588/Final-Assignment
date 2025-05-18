import java.util.List;
import java.util.Iterator;

/**
 * 社員情報を削除するクラス
 */
public class EmployeeDeleter {
    private CSVHandler csvHandler;

    public EmployeeDeleter(CSVHandler csvHandler) {
        this.csvHandler = csvHandler;
    }

    /**
     * 指定された社員IDの社員情報を削除します。
     * 
     * @param employeeId 削除対象の社員ID
     * @return 削除が成功した場合はtrue、失敗した場合はfalse
     */
    public boolean deleteEmployee(String employeeId) {
        List<EmployeeInfo> employeeList = csvHandler.readCSV(null);
        if (employeeList == null) {
            System.out.println("社員情報の読み込みに失敗しました。");
            return false;
        }
    
        for (Iterator<EmployeeInfo> iterator = employeeList.iterator(); iterator.hasNext();) {
            EmployeeInfo employee = iterator.next();
            if (employee.getEmployeeId() != null && employee.getEmployeeId().equals(employeeId)) {
                iterator.remove();
                System.out.println("社員情報が削除されました。");
    
                // 書き込み処理を実行するが、戻り値は確認できない
                csvHandler.writeCSV(employeeList);
                return true;
            }
        }
        System.out.println("社員IDが見つかりませんでした。");
        return false;
    }
    
}
