class CommunicationSkill extends EmployeeInfoValidator {
    private double communicationSkill;

    public CommunicationSkill(String communicationSkill) {
        this.communicationSkill = Double.parseDouble(communicationSkill);
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
