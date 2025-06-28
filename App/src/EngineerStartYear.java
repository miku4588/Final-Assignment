import java.time.Year;

/**
 * 従業員のエンジニアとしての開始年を管理するクラス。
 * 開始年のバリデーションを行い、正しい場合に開始年を保持。
 */
class EngineerStartYear extends EmployeeInfoValidator {
    private Year engineerStartYear;

   public EngineerStartYear(String year) {
        this.engineerStartYear = Year.parse(year);
    }



    @Override
    protected boolean validateInput(String engineerStartYear) {
        try {
            Year parsed = Year.parse(engineerStartYear);
            int currentYear = Year.now().getValue();
            int value = parsed.getValue();
            return value >= 1950 && value <= currentYear;
        } catch (Exception e) {
            return false;
        }
    }

    public Year getEngineerStartYear() {
        return engineerStartYear;
    }
}
