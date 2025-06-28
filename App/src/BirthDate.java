import java.time.LocalDate;
//import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 生年月日を管理するクラス。
 * 生年月日のバリデーションを行い、正しい場合に生年月日を保持。
 */
class BirthDate extends EmployeeInfoValidator {
    private LocalDate birthDate;

 public BirthDate(String birthString) {
   // if (!validateInput(birthString)) {
        //throw new IllegalArgumentException("生年月日のフォーマットが正しくありません");
//}
    this.birthDate = LocalDate.parse(birthString, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
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

}
