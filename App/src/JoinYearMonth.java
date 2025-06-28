import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

class JoinYearMonth extends EmployeeInfoValidator {
    private YearMonth joinYearMonth;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM");

    public JoinYearMonth(String joinYearMonth) {
        if (!validateInput(joinYearMonth)) {
            throw new IllegalArgumentException("入社年月はYYYY/MM形式で入力してください。");
        }
        this.joinYearMonth = YearMonth.parse(joinYearMonth, FORMATTER);
    }

    @Override
    protected boolean validateInput(String value) {
        if (value == null) return false;
        try {
            YearMonth.parse(value, FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }


    public YearMonth getJoinYearMonth() {
        return joinYearMonth;
    }


}
