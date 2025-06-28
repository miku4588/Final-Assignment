import java.time.LocalDate;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * 社員情報を編集するためのクラス
 */
public class EmployeeEditor {

    // ロガー取得
    private static final EmployeeInfoLogger LOGGER = EmployeeInfoLogger.getInstance();

    /**
     * 社員情報を更新するメソッド
     *
     * @param targetId         更新対象の社員ID
     * @param newName          新しい名前
     * @param newPhonetic      新しいフリガナ
     * @param newBirthDate     新しい生年月日
     * @param JoinYearMonth    入社年月
     * @param EngineerStartYear エンジニア開始年
     * @param TechnicalSkill   技術スキル
     * @param Attitude         態度
     * @param CommunicationSkill コミュニケーションスキル
     * @param Leadership       リーダーシップ
     * @param TrainingHistory  研修履歴
     * @param Career           キャリア
     * @param Remarks          備考
     * @param Languages        言語
     * @param saveAction       書き込み処理
     * @return 更新が成功した場合はtrue、失敗した場合はfalse
     */
    public static boolean editEmployee(
            String targetId,
            String newName,
            String newPhonetic,
            String newBirthDate,
            String JoinYearMonth,
            String EngineerStartYear,
            String TechnicalSkill,
            String Attitude,
            String CommunicationSkill,
            String Leadership,
            String TrainingHistory,
            String Career,
            String Remarks,
            String Languages,
            BiConsumer<EmployeeInfo, Boolean> saveAction
    ) {
        LOGGER.logOutput("社員ID：" + targetId + " の社員情報を更新します。");

        List<EmployeeInfo> employeeList = EmployeeManager.getEmployeeList();

        for (EmployeeInfo employee : employeeList) {
            if (employee.getEmployeeId().equals(targetId)) {
                try {
                    // 各項目を初期化
                    EmployeeId employeeId = new EmployeeId(employee.getEmployeeId());
                    Name name = new Name(newName);
                    Phonetic phonetic = new Phonetic(newPhonetic);
                    BirthDate birthDate = new BirthDate(newBirthDate);
                    JoinYearMonth joinYearMonth = new JoinYearMonth(JoinYearMonth);
                    EngineerStartYear engineerStartYear = new EngineerStartYear(EngineerStartYear);
                    TechnicalSkill technicalSkill = new TechnicalSkill(TechnicalSkill);
                    Attitude attitude = new Attitude(Attitude);
                    CommunicationSkill communicationSkill = new CommunicationSkill(CommunicationSkill);
                    Leadership leadership = new Leadership(Leadership);
                    Career career = new Career(Career);
                    TrainingHistory trainingHistory = new TrainingHistory(TrainingHistory);
                    Remarks remarks = new Remarks(Remarks);
                    Languages languages = new Languages(Languages);

                    // EmployeeInfoを初期化
                    EmployeeInfo updatedEmployee = new EmployeeInfo(
                            employeeId,
                            name,
                            phonetic,
                            birthDate,
                            joinYearMonth,
                            engineerStartYear,
                            technicalSkill,
                            attitude,
                            communicationSkill,
                            leadership,
                            career,
                            trainingHistory,
                            remarks,
                            languages,
                            employee.getCreationDate(),
                            LocalDate.now()
                    );

                    // 書き込み処理を実行（新規ではないのでfalse）
                    saveAction.accept(updatedEmployee, false);

                    LOGGER.logOutput("社員ID：" + targetId + " の情報を更新しました。");
                    return true; // 更新成功
                } catch (IllegalArgumentException e) {
                    System.out.println("入力エラー: " + e.getMessage());
                    return false; // 更新失敗
                }
            }
        }

        LOGGER.logOutput("該当する社員IDが見つかりませんでした。");
        return false; // 更新失敗
    }
}
