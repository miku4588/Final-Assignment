public class CommunicationSkill {
    private int communicationSkill; // コミュニケーションスキルの評価（1-5の範囲）

    public CommunicationSkill(int communicationSkill) {
        if (communicationSkill < 1 || communicationSkill > 5) {
            throw new IllegalArgumentException("コミュニケーションスキルは1から5の範囲でなければなりません。");
        }
        this.communicationSkill = communicationSkill;
    }

    public int getCommunicationSkill() {
        return communicationSkill;
    }
}
