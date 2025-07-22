class Career extends EmployeeInfoValidator {
    private String career;


    /**
     * 経歴
     * 経歴のバリデーションを行い、無効な場合は例外をスロー。
     *
     * @param career 経歴（500文字以内）
     * @throws IllegalArgumentException 経歴が無効な場合（500文字を超える場合）
     */
    public Career(String career) {
        if (!validateInput(career)) {
            throw new IllegalArgumentException("経歴は500文字以内で入力してください。");
        }
        this.career = career;
    }

    /**
     * 経歴のバリデーションを行う。
     * 経歴は1〜500文字以内。
     *
     * @param career 経歴
     * @return 経歴が有効かどうか（0〜500文字以内ならtrue）
     */
    @Override
    protected boolean validateInput(String career) {
        String careerForValid = career;

        // 改行を含む場合に対応
        if (career.contains("\n")) {
            careerForValid = career.replace("\n", ""); // 改行文字を消す

            if (career.length() > 2 && career.startsWith("\"") && career.endsWith("\""))
                careerForValid =  careerForValid.substring(1, careerForValid.length() - 1); // ダブルクォーテーションで囲まれてたらそれを外す
        }
        
        return validateLength(careerForValid, 0, 500);
    }


    public String getCareer() {
        return career;
    }
}
