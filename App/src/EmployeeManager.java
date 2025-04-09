import java.util.ArrayList;
import java.util.List;

/**
 *EmployeeInfoのリストを管理するクラス
 */
public class EmployeeManager {

    // EmployeeInfoのList
    private List<EmployeeInfo> employeeList = new ArrayList<>();


    /**
     * コンストラクタ
     */
    public EmployeeManager(List<EmployeeInfo> employeeList) {
        this.employeeList = employeeList;
    }

    /**
     * ゲッター
     * @return employeeList
     */
    public List<EmployeeInfo> getEmployeeList() {
        return employeeList;
    }

    /**
     * セッター
     * @param employeeList
     */
    public void setEmployeeList(List<EmployeeInfo> employeeList) {
        this.employeeList = employeeList;
    }
}