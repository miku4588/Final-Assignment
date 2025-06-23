import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Set;

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
    private LocalDate creationDate;
    private LocalDate lastUpdatedDate;

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
                        Languages languages,
                        LocalDate creationDate,
                        LocalDate lastUpdatedDate
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
        this.creationDate = creationDate;
        this.lastUpdatedDate = lastUpdatedDate;
    }


    /**
     * カンマ区切りで出力するメソッド（作成日・更新日付き）
     */
    @Override
    public String toString() {
        String birthDateString = "";
        if(getBirthDate() != null) {birthDateString = getBirthDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));}

        String joinYearMonthString = "";
        if(getJoinYearMonth() != null) {joinYearMonthString = getJoinYearMonth().format(DateTimeFormatter.ofPattern("yyyy/MM"));}

        String engineerStartYearString = "";
        if(getEngineerStartYear() != null) {engineerStartYearString = getEngineerStartYear().format(DateTimeFormatter.ofPattern("yyyy"));}

        String creationDateString = "";
        if(getCreationDate() != null) {creationDateString = getCreationDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));}

        String lastUpdatedDateString = "";
        if(getLastUpdatedDate() != null) {lastUpdatedDateString = getLastUpdatedDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));}


        // 1列目(No.)と2列目(追加・更新)をデータ作成日と最終更新日に使う
        return creationDateString + "," +
               lastUpdatedDateString + "," +
               getEmployeeId() + "," +
               getName() + "," +
               getPhonetic() + "," +
               birthDateString + "," +
               joinYearMonthString + "," +
               engineerStartYearString + "," +
               getTechnicalSkill() + "," +
               getAttitude() + "," +
               getCommunicationSkill() + "," +
               getLeadership() + "," +
               getCareer() + "," +
               getTrainingHistory() + "," +
               getRemarks() + "," +
               String.join(",", getLanguages());
    }


    /**
     * カンマ区切りで出力するメソッド（作成日・更新日なし）
     * @return 社員ID,氏名,氏名カナ…の形式の文字列
     */
    public String toStringUserFields() {

        // 1列目(No.)と2列目(追加・更新)以外を出力
        return getEmployeeId() + "," +
               getName() + "," +
               getPhonetic() + "," +
               getBirthDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "," +
               getJoinYearMonth().format(DateTimeFormatter.ofPattern("yyyy/MM")) + "," +
               getEngineerStartYear() + "," +
               getTechnicalSkill() + "," +
               getAttitude() + "," +
               getCommunicationSkill() + "," +
               getLeadership() + "," +
               getCareer() + "," +
               getTrainingHistory() + "," +
               getRemarks() + "," +
               String.join(",", getLanguages());
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
    public Set<String> getLanguages() {
        return languages.getLanguages();
    }

    /**
     * EmployeeInfoからcreationDateを取得
     * @return データ作成日
     */
    public LocalDate getCreationDate() {
        if(creationDate == null) {
            return null;
        } else {
            return creationDate;
        }
    }
    
    /**
     * EmployeeInfoからlastUpdatedDateを取得
     * @return 最終更新日
     */
    public LocalDate getLastUpdatedDate() {
        if(lastUpdatedDate == null) {
            return null;
        } else {
            return lastUpdatedDate;
        }
    }
}