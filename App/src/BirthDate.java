import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 生年月日を管理するクラス。
 * 生年月日のバリデーションを行い、正しい場合に生年月日を保持。
 */
class BirthDate extends EmployeeInfoValidator {
    private LocalDate birthDate;
    private String input;

    /**
     * コンストラクタ：文字列入力を受け取って内部保存
     */
    public BirthDate(String birthDate) {
        this.input = birthDate;
    }

    /**
     * validate: 入力をチェックして LocalDate に変換
     */
    @Override
    protected void validate() {
        if (!validateInput(input)) {
            throw new IllegalArgumentException("生年月日は yyyy/MM/dd 形式で入力してください。");
        }
        this.birthDate = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    /**
     * 生年月日のフォーマットを検証
     */
    @Override
    protected boolean validateInput(String dateStr) {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    @Override
    protected boolean validateLength(String value, int min, int max) {
        return value != null && value.length() >= min && value.length() <= max;
    }
}
