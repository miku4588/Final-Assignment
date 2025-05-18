import java.util.ArrayList;
import java.util.List;

/**
 *EmployeeInfoのリストを管理するクラス
 */
public class EmployeeManager {

    // EmployeeInfoのList
    private List<EmployeeInfo> employeeList = new ArrayList<>();
    // EmployeeManagerのインスタンス
    private static EmployeeManager instance;


    /**
     * コンストラクタ
     */
    private EmployeeManager(List<EmployeeInfo> employeeList) {
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
     * インスタンスを呼び出す（なければ生成）
     * @param employeeList
     * @return EmployeeManagerのインスタンス
     */
    public static EmployeeManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("EmployeeManagerはまだ初期化されていません。");
        }
        return instance;
    }


    /**
     * EmployeeManagerの初期化
     * @param employeeList
     */
    public static void initializeEmployeeManager(List<EmployeeInfo> employeeList) {
        if (instance == null) {
            instance = new EmployeeManager(employeeList);
        }
    }
}