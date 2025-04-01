import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 生年月日を管理するクラス。
 * 生年月日のバリデーションを行い、正しい場合に生年月日を保持。
 */
class BirthDate extends EmployeeInfoValidator {
    private LocalDate birthDate;

    /**
     * 生年月日を設定するコンストラクタ。
     * 生年月日のバリデーションを行い、無効な場合は例外をスロー。
     *
     * @param birthDate 生年月日（YYYY/MM/DD形式）
     * @throws IllegalArgumentException 生年月日が無効な場合（YYYY/MM/DD形式で入力されていない）
     */
    public BirthDate(String birthDate) {
        if (!validateInput(birthDate)) {
            throw new IllegalArgumentException("生年月日はYYYY/MM/DD形式で入力してください。");
        }
        this.birthDate = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    /**
     * 生年月日のバリデーションを行う。
     * 生年月日はYYYY/MM/DD形式。
     *
     * @param birthDate 生年月日（文字列）
     * @return 生年月日が有効かどうか（YYYY/MM/DD形式であればtrue）
     */
    @Override
    protected boolean validateInput(String birthDate) {
        return validateFormat(birthDate, "YYYY/MM/DD");
    }

    /**
     * 生年月日を取得するメソッド。
     *
     * @return 生年月日（LocalDate型）
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }
}
