import java.time.Year;

/**
 * 従業員のエンジニアとしての開始年を管理するクラス。
 * 開始年のバリデーションを行い、正しい場合に開始年を保持。
 */
class EngineerStartYear extends EmployeeInfoValidator {
    private Year engineerStartYear;

    /**
     * エンジニアとしての開始年を設定するコンストラクタ。
     *
     * @param engineerStartYear エンジニアとしての開始年（YYYY形式）
     */
    public EngineerStartYear(String engineerStartYear) {
        this.engineerStartYear = Year.parse(engineerStartYear);
    }

    /**
     * エンジニアとしての開始年を取得するメソッド。
     *
     * @return エンジニアとしての開始年（Year型）
     */
    public Year getEngineerStartYear() {
        return engineerStartYear;
    }
}
