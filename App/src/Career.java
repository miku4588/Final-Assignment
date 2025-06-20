class Career extends EmployeeInfoValidator {
    private String career;
    private String input;

    public Career(String career) {
        this.input = career;
    }

    @Override
    protected void validate() {
        if (!validateInput(input)) {
            throw new IllegalArgumentException("経歴情報は1〜500文字以内で入力してください。");
        }
        this.career = input;
    }

    @Override
    protected boolean validateInput(String career) {
        return career != null && validateLength(career, 1, 500);
    }

    @Override
    protected boolean validateLength(String value, int min, int max) {
        return value != null && value.length() >= min && value.length() <= max;
    }

    public String getCareer() {
        return career;
    }
}
