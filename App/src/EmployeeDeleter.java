import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 社員情報を削除するクラス
 */
public class EmployeeDeleter {
    // 💡ロガーをメンバーとして持っておいてください
    private static final EmployeeInfoLogger LOGGER = EmployeeInfoLogger.getInstance();

    /**
     * 指定された社員IDの社員情報を削除します。
     * 
     * @param employeeId 削除対象の社員ID
     * @return 削除が成功した場合はtrue、失敗した場合はfalse
     */
    public static boolean deleteEmployee(String employeeId) {
        LOGGER.logOutput("社員情報の削除を開始。");
        if (EmployeeManager.getEmployeeList() == null) {
            LOGGER.logOutput("社員情報の読み込みに失敗しました。");
            ErrorHandler.showErrorDialog("社員情報の読み込みに失敗しました。");
            return false;
        }
        
        List<EmployeeInfo> employeeList = new ArrayList<>(EmployeeManager.getEmployeeList());
        
        for (Iterator<EmployeeInfo> iterator = employeeList.iterator(); iterator.hasNext();) {
            EmployeeInfo employee = iterator.next();
            if (employee.getEmployeeId() != null && employee.getEmployeeId().equals(employeeId)) {
                iterator.remove();
                LOGGER.logOutput("社員ID：" + employeeId + "　" + employee.getName() + "の社員情報を削除します。");

                // 書き込み処理を実行するが、戻り値は確認できない
                CSVHandler.writeCSV(employeeList);
                return true;
            }
        }
        LOGGER.logOutput("社員IDが見つかりませんでした。");
        return false;
    }
}
