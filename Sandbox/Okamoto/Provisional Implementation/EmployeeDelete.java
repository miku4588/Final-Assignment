public class EmployeeDelete {
    private EmployeeInfo selectedEmployee; // 削除対象の従業員情報
    private ErrorHandler errorHandler; // ErrorHandler インスタンス

    public EmployeeDelete(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void selectEmployee(EmployeeInfo employee) {
        this.selectedEmployee = employee; // 削除する従業員を選択
    }

    public void deleteEmployee(EmployeeManager manager) {
        // 選択した従業員を削除するロジックを実装
        if (!manager.deleteEmployee(selectedEmployee)) {
            errorHandler.handleError("従業員の削除に失敗しました。");
        }
    }
}
