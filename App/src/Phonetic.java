/**
 * 氏名カナを管理するクラス。
 * 氏名カナのバリデーションを行い、正しい場合に氏名カナを保持。
 */
class Phonetic extends EmployeeInfoValidator {
    private String phonetic;

    /**
     * 氏名カナを設定するコンストラクタ。
     * 氏名カナのバリデーションを行い、無効な場合は例外をスロー。
     *
     * @param phonetic 氏名カナ（全角カナ・全角スペース・半角スペースのみ使用可能）
     * @throws IllegalArgumentException 氏名カナが無効な場合（1〜30文字の範囲内でのみ使用可能）
     */
    public Phonetic(String phonetic) {
        if (!validateInput(phonetic)) {
            throw new IllegalArgumentException("氏名カナは1〜30文字の範囲で、全角カナ・スペースのみ使用できます。");
        }
        this.phonetic = phonetic;
    }

    /**
     * 氏名カナのバリデーションを行う。
     * 氏名カナは1〜30文字以内で、全角カナ・全角スペース・半角スペースのみ使用可能。
     *
     * @param phonetic 氏名カナ
     * @return 氏名カナが有効かどうか（1〜30文字の範囲内で全角カナ・スペースのみならtrue）
     */
    @Override
    protected boolean validateInput(String phonetic) {
        return validateLength(phonetic, 1, 30) && validateCharacterType(phonetic, "KANA");
    }

    /**
     * 氏名カナを取得するメソッド。
     *
     * @return 氏名カナ
     */
    public String getPhonetic() {
        return phonetic;
    }
}
