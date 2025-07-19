import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

class JoinYearMonth extends EmployeeInfoValidator {
    private YearMonth joinYearMonth;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM");

    public JoinYearMonth(String joinYearMonth) {
        String normalized = joinYearMonth.replace("-", "/");
        this.joinYearMonth = YearMonth.parse(normalized, FORMATTER);
    }

    @Override
    public String toString() {
        return joinYearMonth.format(FORMATTER);
    }

    @Override
    protected boolean validateInput(String value) {
        if (value == null)
            return false;
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
