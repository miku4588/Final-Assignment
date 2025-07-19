/**
 * 氏名カナを管理するクラス。
 * 氏名カナのバリデーションを行い、正しい場合に氏名カナを保持。
 */
class Phonetic extends EmployeeInfoValidator {
    private String phonetic;

    public Phonetic(String phonetic) {
        if (phonetic == null || phonetic.trim().isEmpty()) {
            throw new IllegalArgumentException("氏名カナは必須です");
        }
        if (!validateInput(phonetic)) {
            throw new IllegalArgumentException("氏名カナは1〜30文字の範囲で、全角カナ・全角スペース・半角スペースのみ使用できます。");
        }
        this.phonetic = phonetic;
    }

    @Override
    protected boolean validateInput(String phonetic) {
        if (phonetic == null) {
            return false;
        }
        return validateLength(phonetic, 1, 30) && validateCharacterType(phonetic);
    }

    /**
     * 氏名カナのキャラクタータイプをバリデートするメソッド。
     * 全角カナ・全角スペース・半角スペースのみ許可。
     *
     * @param phonetic 氏名カナ
     * @return 有効な文字タイプの場合はtrue
     */
    protected boolean validateCharacterType(String phonetic) {
        return phonetic.matches("[\\u30A0-\\u30FF\\u3000\\s]+"); // 全角カナ、全角スペース、半角スペース
    }

    public String getPhonetic() {
        return phonetic;
    }

}
