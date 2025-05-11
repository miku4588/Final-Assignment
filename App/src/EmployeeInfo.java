import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    private Languages languages;

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
                        Remarks remarks,
                        Languages languages
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
        this.languages = languages;
    }

    @Override
    public String toString() {
        return employeeId.getEmployeeId() + "," +
               name.getName() + "," +
               phonetic.getPhonetic() + "," +
               birthDate.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "," +
               joinYearMonth.getJoinYearMonth().format(DateTimeFormatter.ofPattern("yyyy/MM")) + "," +
               engineerStartYear.getEngineerStartYear() + "," +
               technicalSkill.getTechnicalSkill() + "," +
               attitude.getAttitude() + "," +
               communicationSkill.getCommunicationSkill() + "," +
               leadership.getLeadership() + "," +
               career.getCareer() + "," +
               trainingHistory.getTrainingHistory() + "," +
               remarks.getRemarks() + "," +
               String.join(",", languages.getLanguages());
    }

    /**
     * EmployeeInfoからEmployeeIdを取得
     * @return 社員ID
     */
    public String getEmployeeId() {
        return employeeId.getEmployeeId();
    }
    
    /**
     * EmployeeInfoからNameを取得
     * @return 氏名
     */
    public String getName() {
        return name.getName();
    }

    /**
     * EmployeeInfoからPhoneticを取得
     * @return 氏名カナ
     */
    public String getPhonetic() {
        return phonetic.getPhonetic();
    }

    /**
     * EmployeeInfoからBirthDateを取得
     * @return 誕生日
     */
    public LocalDate getBirthDate() {
        return birthDate.getBirthDate();
    }

    /**
     * EmployeeInfoからJoinYearMonthを取得
     * @return 入社年月
     */
    public YearMonth getJoinYearMonth() {
        return joinYearMonth.getJoinYearMonth();
    }

    /**
     * EmployeeInfoからEngineerStartYearを取得
     * @return エンジニア開始年
     */
    public Year getEngineerStartYear() {
        return engineerStartYear.getEngineerStartYear();
    }

    /**
     * EmployeeInfoからTechnicalSkillを取得
     * @return 技術スキル
     */
    public double getTechnicalSkill() {
        return technicalSkill.getTechnicalSkill();
    }

    /**
     * EmployeeInfoからAttitudeを取得
     * @return 態度
     */
    public double getAttitude() {
        return attitude.getAttitude();
    }

    /**
     * EmployeeInfoからCommunicationSkillを取得
     * @return コミュニケーション能力
     */
    public double getCommunicationSkill() {
        return communicationSkill.getCommunicationSkill();
    }

    /**
     * EmployeeInfoからLeadershipを取得
     * @return リーダーシップ
     */
    public double getLeadership() {
        return leadership.getLeadership();
    }

    /**
     * EmployeeInfoからCareerを取得
     * @return 経歴
     */
    public String getCareer() {
        return career.getCareer();
    }

    /**
     * EmployeeInfoからTrainingHistoryを取得
     * @return 研修履歴
     */
    public String getTrainingHistory() {
        return trainingHistory.getTrainingHistory();
    }

    /**
     * EmployeeInfoからRemarksを取得
     * @return 備考
     */
    public String getRemarks() {
        return remarks.getRemarks();
    }

    /**
     * EmployeeInfoからLanguagesを取得
     * @return
     */
    public List<String> getLanguages() {
        return languages.getLanguages();
    }
}