import java.util.Calendar;

public class EmployeeInfo {
    private EmployeeId employeeId;
    private Name name;
    private BirthDate birthDate;
    private JoinDate joinDate;
    private EngineerStartYear engineerStartYear;
    private TechnicalSkill technicalSkill;
    private Attitude attitude;
    private CommunicationSkill communicationSkill;
    private Leadership leadership;
    private Career career;
    private TrainingHistory trainingHistory;
    private Remarks remarks;

    public EmployeeInfo(EmployeeId employeeId, Name name, BirthDate birthDate, 
                        JoinDate joinDate, EngineerStartYear engineerStartYear, 
                        TechnicalSkill technicalSkill, Attitude attitude, 
                        CommunicationSkill communicationSkill, Leadership leadership, 
                        Career career, TrainingHistory trainingHistory, Remarks remarks) {
        this.employeeId = employeeId;
        this.name = name;
        this.birthDate = birthDate;
        this.joinDate = joinDate;
        this.engineerStartYear = engineerStartYear;
        this.technicalSkill = technicalSkill;
        this.attitude = attitude;
        this.communicationSkill = communicationSkill;
        this.leadership = leadership;
        this.career = career;
        this.trainingHistory = trainingHistory;
        this.remarks = remarks;
    }

    // 各フィールドのゲッター
    public EmployeeId getEmployeeId() { return employeeId; }
    public Name getName() { return name; }
    public BirthDate getBirthDate() { return birthDate; }
    public JoinDate getJoinDate() { return joinDate; }
    public EngineerStartYear getEngineerStartYear() { return engineerStartYear; }
    public TechnicalSkill getTechnicalSkill() { return technicalSkill; }
    public Attitude getAttitude() { return attitude; }
    public CommunicationSkill getCommunicationSkill() { return communicationSkill; }
    public Leadership getLeadership() { return leadership; }
    public Career getCareer() { return career; }
    public TrainingHistory getTrainingHistory() { return trainingHistory; }
    public Remarks getRemarks() { return remarks; }

    // toStringメソッドをオーバーライドしてCSV形式を返す
    @Override
    public String toString() {
        return employeeId.getEmployeeId() + "," + 
               name.getName() + "," + 
               birthDate.getBirthDate() + "," + 
               joinDate.getJoinDate() + "," + 
               engineerStartYear.getEngineerStartYear() + "," + 
               technicalSkill.getTechnicalSkill() + "," + 
               attitude.getAttitude() + "," + 
               communicationSkill.getCommunicationSkill() + "," + 
               leadership.getLeadership() + "," + 
               career.getCareer() + "," + 
               trainingHistory.getTrainingHistory() + "," + 
               remarks.getRemarks();
    }


    
 public int calculateAge(BirthDate birthDate) {
    if (birthDate == null || birthDate.getBirthDate() == null) {
        throw new IllegalArgumentException("生年月日が設定されていません。");
    }

    Calendar birth = Calendar.getInstance();
    birth.setTime(birthDate.getBirthDate());

    Calendar today = Calendar.getInstance();
    int age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR);

    if (today.get(Calendar.MONTH) < birth.get(Calendar.MONTH) ||
        (today.get(Calendar.MONTH) == birth.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) < birth.get(Calendar.DAY_OF_MONTH))) {
        age--;
    }

    return age;
}

   // 勤続年数を計算するメソッド
public int calculateYears(JoinDate joinDate) {
    if (joinDate == null || joinDate.getJoinDate() == null) {
        throw new IllegalArgumentException("入社日が設定されていません。");
    }

    Calendar join = Calendar.getInstance();
    join.setTime(joinDate.getJoinDate());

    Calendar today = Calendar.getInstance();
    int years = today.get(Calendar.YEAR) - join.get(Calendar.YEAR);

    if (today.get(Calendar.MONTH) < join.get(Calendar.MONTH) ||
        (today.get(Calendar.MONTH) == join.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) < join.get(Calendar.DAY_OF_MONTH))) {
        years--;
    }

    return years;
}



}


/*EmployeeInfo 
役割: 従業員の詳細情報を管理。
属性: EmployeeId, Name, BirthDate, JoinDate, など、従業員に関するさまざまな情報を含む。
メソッド:toString(): オーバーライドされ、カンマ区切りの文字列を返す。*/