/**
 * 従業員の研修履歴を管理するクラス。
 * 研修履歴のバリデーションを行い、正しい場合に研修履歴を保持。
 */
class TrainingHistory extends EmployeeInfoValidator {
    private String trainingHistory;

    /**
     * 研修履歴を設定するコンストラクタ。
     * 研修履歴のバリデーションを行い、無効な場合は例外をスロー。
     *
     * @param trainingHistory 研修履歴（500文字以内）
     * @throws IllegalArgumentException 研修履歴が無効な場合（500文字を超える場合）
     */
    public TrainingHistory(String trainingHistory) {
        if (!validateInput(trainingHistory)) {
            throw new IllegalArgumentException("研修履歴は500文字以内で入力してください。");
        }
        this.trainingHistory = trainingHistory;
    }

    /**
     * 研修履歴のバリデーションを行う。
     * 研修履歴は1〜500文字以内。
     *
     * @param trainingHistory 研修履歴
     * @return 研修履歴が有効かどうか（1〜500文字以内ならtrue）
     */
    @Override
    protected boolean validateInput(String trainingHistory) {
        String historyForValid = trainingHistory;

        // 改行を含む場合に対応
        if (trainingHistory.contains("\n")) {
            historyForValid = trainingHistory.replace("\n", ""); // 改行文字を消す
            
            if (trainingHistory.length() > 2 && trainingHistory.startsWith("\"") && trainingHistory.endsWith("\""))
                historyForValid = historyForValid.substring(1, historyForValid.length() - 1); // ダブルクォーテーションで囲まれてたらそれを外す
        }
        return validateLength(historyForValid, 0, 500);
    }

    /**
     * 研修履歴を取得するメソッド。
     *
     * @return 研修履歴
     */
    public String getTrainingHistory() {
        return trainingHistory;
    }
}
