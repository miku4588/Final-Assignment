/**
 * 従業員のリーダーシップ評価を管理するクラス。
 * リーダーシップ評価を保持。
 */
class Leadership extends EmployeeInfoValidator {
    private double leadership;

    public Leadership(String leadership) {
        this.leadership = Double.parseDouble(leadership);
    }

    @Override
    protected boolean validateInput(String leadership) {
        try {
            double value = Double.parseDouble(leadership);
            return value >= 1 && value <= 5;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public double getLeadership() {
        return leadership;
    }

}
