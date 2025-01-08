import java.util.ArrayList;
import java.util.List;

public class EmployeeManager {
    // 従業員情報のリスト
    private List<EmployeeInfo> employeeList = new ArrayList<>();

    // CSVファイルから従業員情報をインポートするメソッド
    // 従業員情報を外部ファイルから読み込み、employeeList に追加することを想定
    public void importCSV() {
        // CSVインポートロジックを実装
    }

    // 従業員情報をCSVファイルとしてエクスポートするメソッド
    // employeeList の内容をCSV形式でファイルに書き出すことを想定
    public void exportCSV() {
        // CSVエクスポートロジックを実装
    }

    // 新しい従業員を追加するメソッド
    public boolean addEmployee(EmployeeInfo emp) {
        if (Validation.validateEmployeeInfo(emp)) {
            employeeList.add(emp);
            return true;
        }
        return false; // バリデーション失敗
    }

    // 従業員を削除するメソッド
    public boolean deleteEmployee(EmployeeInfo emp) {
        return employeeList.remove(emp);
    }

    // 現在の従業員リストを取得するメソッド
    public List<EmployeeInfo> getEmployeeList() {
        return employeeList;
    }
}

/* 役割: 従業員情報の管理を行う。従業員の追加、削除、CSVのインポート・エクスポートなどの機能を提供する */