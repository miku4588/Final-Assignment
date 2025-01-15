public class Career {
    private String career; // 経歴情報（改行可能、500文字以内）

    public Career(String career) {
        if (career.length() > 500) {
            throw new IllegalArgumentException("経歴は500文字以内でなければなりません。");
        }
        this.career = career;
    }

    public String getCareer() {
        return career;
    }
}
