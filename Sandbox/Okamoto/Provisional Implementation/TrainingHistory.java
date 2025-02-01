public class TrainingHistory {
    private String trainingHistory; // 研修履歴（改行可能、500文字以内）

    public TrainingHistory(String trainingHistory) {
        if (trainingHistory.length() > 500) {
            throw new IllegalArgumentException("研修履歴は500文字以内でなければなりません。");
        }
        this.trainingHistory = trainingHistory;
    }

    public String getTrainingHistory() {
        return trainingHistory;
    }
}
