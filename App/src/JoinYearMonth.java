import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/**
 * 従業員の入社年月を管理するクラス。
 * 入社年月のバリデーションを行い、正しい場合に入社年月を保持。
 */
class JoinYearMonth extends EmployeeInfoValidator {
    private YearMonth joinYearMonth;

    /**
     * 入社年月を設定するコンストラクタ。
     * 入社年月のフォーマットが無効な場合は例外をスロー。
     *
     * @param joinYearMonth 入社年月（YYYY/MM形式）
     */
    public JoinYearMonth(String joinYearMonth) {
        this.joinYearMonth = YearMonth.parse(joinYearMonth, DateTimeFormatter.ofPattern("yyyy/MM"));
    }

    /**
     * 入社年月を取得するメソッド。
     *
     * @return 入社年月（YearMonth型）
     */
    public YearMonth getJoinYearMonth() {
        return joinYearMonth;
    }
}
