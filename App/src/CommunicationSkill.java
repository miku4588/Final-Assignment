class CommunicationSkill extends EmployeeInfoValidator {
    private double communicationSkill;
    private String input;

    public CommunicationSkill(String communicationSkill) {
        this.input = communicationSkill;
    }

    @Override
    protected void validate() {
        if (!validateInput(input)) {
            throw new IllegalArgumentException("コミュニケーションスキル評価は1〜5の範囲でなければなりません。");
        }
        this.communicationSkill = Double.parseDouble(input);
    }

    @Override
    protected boolean validateInput(String communicationSkill) {
        try {
            double value = Double.parseDouble(communicationSkill);
            return value >= 1 && value <= 5;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public double getCommunicationSkill() {
        return communicationSkill;
    }
}
