import java.time.Year;

/**
 * 従業員のエンジニアとしての開始年を管理するクラス。
 * 開始年のバリデーションを行い、正しい場合に開始年を保持。
 */
class EngineerStartYear extends EmployeeInfoValidator {
    private Year engineerStartYear;

    public EngineerStartYear(String engineerStartYear) {
        this.engineerStartYear = Year.parse(engineerStartYear);
        validate();  // ここで入力チェックを実施
    }

    @Override
    protected void validate() {
        if (!validateInput(engineerStartYear.toString())) {
            throw new IllegalArgumentException("エンジニアとしての開始年は過去〜現在の範囲の西暦(YYYY)で入力してください。");
        }
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
