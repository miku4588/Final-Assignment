class Career extends EmployeeInfoValidator {
    private String career;

    public Career(String career) {
        this.career = career;
    }

    @Override
    protected boolean validateInput(String career) {
        return career != null && validateLength(career, 0, 500);
    }

    public String getCareer() {
        return career;
    }
}
