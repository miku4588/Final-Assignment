public class MainClass {
    public static void main(String[] args) {
        Logger logger = new Logger();
        ErrorHandler errorHandler = new ErrorHandler(logger);
        EmployeeManager employeeManager = new EmployeeManager();

        // UIを表示
        ListViewUI listViewUI = new ListViewUI(employeeManager);
        listViewUI.displayEmployees();
    }
}
