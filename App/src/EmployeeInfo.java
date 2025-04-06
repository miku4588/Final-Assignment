/**
 * 社員情報の各項目をまとめるクラス
 */
class EmployeeInfo {
    private EmployeeId employeeId;
    private Name name;
    private Phonetic phonetic;
    private BirthDate birthDate;
    private JoinYearMonth joinYearMonth;
    private EngineerStartYear engineerStartYear;
    private TechnicalSkill technicalSkill;
    private Attitude attitude;
    private CommunicationSkill communicationSkill;
    private Leadership leadership;
    private Career career;
    private TrainingHistory trainingHistory;
    private Remarks remarks;
    // private Languages Languages;

    public EmployeeInfo(EmployeeId employeeId,
                        Name name,
                        Phonetic phonetic,
                        BirthDate birthDate,
                        JoinYearMonth joinYearMonth,
                        EngineerStartYear engineerStartYear,
                        TechnicalSkill technicalSkill,
                        Attitude attitude,
                        CommunicationSkill communicationSkill,
                        Leadership leadership,
                        Career career,
                        TrainingHistory trainingHistory,
                        Remarks remarks //,
                        // Languages languages
                        ) {
        this.employeeId = employeeId;
        this.name = name;
        this.phonetic = phonetic;
        this.birthDate = birthDate;
        this.joinYearMonth = joinYearMonth;
        this.engineerStartYear = engineerStartYear;
        this.technicalSkill = technicalSkill;
        this.attitude = attitude;
        this.communicationSkill = communicationSkill;
        this.leadership = leadership;
        this.career = career;
        this.trainingHistory = trainingHistory;
        this.remarks = remarks;
        // this.Languages = languages;
    }

    @Override
    public String toString() {
        return employeeId.getEmployeeId() + "," +
               name.getName() + "," +
               phonetic.getPhonetic() + "," +
               birthDate.getBirthDate() + "," +
               joinYearMonth.getJoinYearMonth() + "," +
               engineerStartYear.getEngineerStartYear() + "," +
               technicalSkill.getTechnicalSkill() + "," +
               attitude.getAttitude() + "," +
               communicationSkill.getCommunicationSkill() + "," +
               leadership.getLeadership() + "," +
               career.getCareer() + "," +
               trainingHistory.getTrainingHistory() + "," +
               remarks.getRemarks(); // + "," +
            //    languages.getLanguages();
    }
}
