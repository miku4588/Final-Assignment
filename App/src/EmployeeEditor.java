import java.time.LocalDate;
import java.util.List;

/**
 * 社員情報を編集するためのクラス
 */
public class EmployeeEditor {
    // 💡ロガーをメンバーとして持っておいてください
    private static final EmployeeInfoLogger LOGGER = EmployeeInfoLogger.getInstance();

    /**
     * 社員情報を更新するメソッド
     * 
     * @param targetId 更新対象の社員ID
     * @param newName 新しい名前
     * @param newPhonetic 新しいフリガナ
     * @param newBirthDate 新しい生年月日
     * @return 更新が成功した場合はtrue、失敗した場合はfalse
     */
    public static boolean editEmployee(String targetId, String newName, String newPhonetic, String newBirthDate) { // 💡他の項目も全部引数で受け取る
        LOGGER.logOutput("社員ID：" + targetId + "　" + newName + "の社員情報を更新します。");
        List<EmployeeInfo> employeeList = EmployeeManager.getEmployeeList();

        for (EmployeeInfo employee : employeeList) {
            if (employee.getEmployeeId().equals(targetId)) {
                try {
                    // 💡お手数ですが全項目を初期化していただいて…（今セットしてる値は全部仮です！）
                    EmployeeId employeeId = new EmployeeId(employee.getEmployeeId());
                    Name name = new Name(newName);
                    Phonetic phonetic = new Phonetic(newPhonetic);
                    BirthDate birthDate = new BirthDate(newBirthDate);
                    JoinYearMonth joinYearMonth = new JoinYearMonth("2025/01");
                    EngineerStartYear engineerStartYear = new EngineerStartYear("2025");
                    TechnicalSkill technicalSkill = new TechnicalSkill("5");
                    Attitude attitude = new Attitude("5");
                    CommunicationSkill communicationSkill = new CommunicationSkill("5");
                    Leadership leadership = new Leadership("5");
                    Career career = new Career("けいれき");
                    TrainingHistory trainingHistory = new TrainingHistory("じゅこう歴");
                    Remarks remarks = new Remarks("びこう");
                    Languages languages = new Languages();
                    
                    // 💡EmployeeInfoを初期化
                    EmployeeInfo UpdatedEmployee = new EmployeeInfo(employeeId, name, phonetic, birthDate,joinYearMonth, engineerStartYear, technicalSkill, attitude, communicationSkill, leadership, career, trainingHistory, remarks, languages, employee.getCreationDate(), LocalDate.now());

                    // 書き込み処理を実行
                    // 💡staticなのでCSVHandlenのインスタンスは不要。更新なのでisNewEmployeeDataはfalse
                    CSVHandler.writeCSV(UpdatedEmployee, false);

                    // 書き込み後に確認のためにログを表示
                    LOGGER.logOutput("社員ID：" + targetId + "　" + newName + "の情報を更新しました。");
                    return true; // 更新成功
                } catch (IllegalArgumentException e) {
                    LOGGER.logException("入力エラー。", e);
                    return false; // 更新失敗
                }
            }
        }
        LOGGER.logOutput("該当する社員IDが見つかりませんでした。");
        return false; // 更新失敗
    }
}
