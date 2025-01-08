public class EmployeeAdd {
    private EmployeeInfo newEmployeeInfo; // 新しく追加する従業員の情報
    private ErrorHandler errorHandler; // ErrorHandler インスタンス

    public EmployeeAdd(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void inputEmployee() {
        // 従業員情報を入力するロジックを実装
    }

    public void saveEmployee(EmployeeManager manager) {
        // 入力された情報を保存するロジックを実装
        if (newEmployeeInfo.validateData()) {
            manager.addEmployee(newEmployeeInfo);
        } else {
            errorHandler.handleError("従業員情報のバリデーションに失敗しました。");
        }
    }

    public boolean validateEmployeeInfo() {
        // 新しい従業員情報のバリデーションを行うロジックを実装
        return true;
    }
}
