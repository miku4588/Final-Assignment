import java.util.List;

public class EmployeeEditor {
    private EmployeeManager employeeManager;
    private CSVHandler csvHandler;

    public EmployeeEditor(EmployeeManager employeeManager, CSVHandler csvHandler) {
        this.employeeManager = employeeManager;
        this.csvHandler = csvHandler;
    }

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

                    csvHandler.writeCSV(employeeList);
                    System.out.println("社員情報を更新しました。");
                    return true;
                } catch (IllegalArgumentException e) {
                    System.out.println("入力エラー: " + e.getMessage());
                    return false;
                }
            }
        }
        System.out.println("該当する社員IDが見つかりませんでした。");
        return false;
    }
}
