import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class CSVHandler {
    // ロガーを取得
    private static final EmployeeInfoLogger LOGGER = EmployeeInfoLogger.getInstance();

    /**
     * CSVファイルを読み込み、EmployeeInfo型に変換したデータのListを返す
     * 
     * @param filePath 読み込むCSVファイルのパス
     * @return EmployeeInfo型のデータが格納されたList
     */
    public static List<EmployeeInfo> readCSV(String filePath) {
        List<String> parseLineList = parseLine(filePath); // CSVの形を整えて読み込めるようにしたList
        List<String> errorMessages = new ArrayList<>(); // バリデーションエラーのList
        List<EmployeeInfo> employees = new ArrayList<>(); // EmployeeInfoのList

        for (String line : parseLineList) {
            String[] data = line.split(","); // カンマで区切って各フィールドを取り出す
            EmployeeId employeeId = new EmployeeId("NewId");
            Name name = new Name("サンプル太郎");
            Phonetic phonetic = new Phonetic("サンプルタロウ");
            BirthDate birthDate = new BirthDate("1900/01/01");
            JoinYearMonth joinYearMonth = new JoinYearMonth("1900/01");
            EngineerStartYear engineerStartYear = new EngineerStartYear("1900");
            TechnicalSkill technicalSkill = new TechnicalSkill(1);
            Attitude attitude = new Attitude(1);
            CommunicationSkill communicationSkill = new CommunicationSkill(1);
            Leadership leadership = new Leadership(1);
            Career career = new Career("これは経歴です。");
            TrainingHistory trainingHistory = new TrainingHistory("これは研修受講歴です。");
            Remarks remarks = new Remarks("これは備考です。");
            // Languages languages = new Languages("HTML");

            
            if (data[0].equals("\uFEFFNo.") || // BOM付きの場合先頭に\uFEFFが付く
                data[0].equals("No.") ||
                data[0].equals("入力例") ||
                data[0].equals("ここから入力↓↓↓↓↓↓↓↓↓↓")) {

            } else {
                // バリデーションチェック
                try {
                    employeeId = new EmployeeId(data[2]);
                } catch (IllegalArgumentException e) {
                    errorMessages.add(data[0] + "行目　" + e.getMessage());
                }
                try {
                    name = new Name(data[3]);
                } catch (IllegalArgumentException e) {
                    errorMessages.add(data[0] + "行目　" + e.getMessage());
                }
                try {
                    phonetic = new Phonetic(data[4]);
                } catch (IllegalArgumentException e) {
                    errorMessages.add(data[0] + "行目　" + e.getMessage());
                }
                try {
                    birthDate = new BirthDate(data[5]);
                } catch (IllegalArgumentException e) {
                    errorMessages.add(data[0] + "行目　" + e.getMessage());
                }
                try {
                    joinYearMonth = new JoinYearMonth(data[6]);
                } catch (IllegalArgumentException e) {
                    errorMessages.add(data[0] + "行目　" + e.getMessage());
                }
                try {
                    engineerStartYear = new EngineerStartYear(data[7]);
                } catch (IllegalArgumentException e) {
                    errorMessages.add(data[0] + "行目　" + e.getMessage());
                }
                try {
                    technicalSkill = new TechnicalSkill(Double.parseDouble(data[8]));
                } catch (IllegalArgumentException e) {
                    errorMessages.add(data[0] + "行目　" + e.getMessage());
                }
                try {
                    attitude = new Attitude(Double.parseDouble(data[9]));
                } catch (IllegalArgumentException e) {
                    errorMessages.add(data[0] + "行目　" + e.getMessage());
                }
                try {
                    leadership = new Leadership(Double.parseDouble(data[10]));
                } catch (IllegalArgumentException e) {
                    errorMessages.add(data[0] + "行目　" + e.getMessage());
                }
                try {
                    career = new Career(data[11]);
                } catch (IllegalArgumentException e) {
                    errorMessages.add(data[0] + "行目　" + e.getMessage());
                }
                try {
                    trainingHistory = new TrainingHistory(data[12]);
                } catch (IllegalArgumentException e) {
                    errorMessages.add(data[0] + "行目　" + e.getMessage());
                }
                try {
                    remarks = new Remarks(data[13]);
                } catch (IllegalArgumentException e) {
                    errorMessages.add(data[0] + "行目　" + e.getMessage());
                }
                // try {
                //     languages = new Languages(data[14]);
                // } catch (IllegalArgumentException e) {
                //     errorMessages.add(data[0] + "行目　" + e.getMessage());
                // }

                // バリデーションチェック通ったらEmployeeInfo生成
                if (errorMessages.isEmpty()) {
                    EmployeeInfo employeeInfo = new EmployeeInfo(
                        employeeId,name,phonetic,birthDate,joinYearMonth,
                        engineerStartYear,technicalSkill,attitude,
                        communicationSkill,leadership,career,
                        trainingHistory,remarks //,languages
                    );
                    System.err.println(employeeInfo);
                    employees.add(employeeInfo);
                } else {
                    ErrorHandler.handleError(String.join("\n", errorMessages));
                    LOGGER.logOutput("CSV読み込みに失敗しました。");
                    errorMessages.clear();
                }
            }
        }

        return employees;
    }

    /**
     * CSVの各行を抽出するメソッド
     * 
     * @param filePath
     * @return 抽出した各行を格納したList
     */
    private static List<String> parseLine(String filePath) {
        List<String> parseLineList = new ArrayList<>();

        // ファイル読み込み時にエラーが出るためキャッチ
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            StringBuilder buffer = new StringBuilder(); // 複数行に分かれている行を保持するバッファ
            String line; // 読み込み中の行

            // ファイルを1行ずつ読み込み
            while ((line = br.readLine()) != null) {
                buffer.append(line); // 現在の行をバッファに追加
                int quoteCount = buffer.toString().replaceAll("[^\"]", "").length(); // ダブルクォートの数をカウント

                // ダブルクォートが偶数個かどうか
                if (quoteCount % 2 == 0) {
                    parseLineList.add(buffer.toString()); // Listに格納
                    buffer.setLength(0); // バッファを空にする
                } else {
                    buffer.append("\n"); // 改行して行を連結
                }
            }
        } catch (IOException e) {
            ErrorHandler.handleError("CSVファイルの読み込み中にエラーが発生しました");
            LOGGER.logException(e);
        }

        return parseLineList;
    }
}