import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;

public class CSVHandler {
    // ロガーを取得
    private static final EmployeeInfoLogger LOGGER = EmployeeInfoLogger.getInstance();
    private static List<EmployeeInfo> employeeList = new ArrayList<>(); // EmployeeInfoのList

    /**
     * CSVファイルを読み込み、EmployeeInfo型に変換したデータのListを返す
     * 
     * @param filePath 読み込むCSVファイルのパス
     * @return EmployeeInfo型のデータが格納されたList
     */
    public static List<EmployeeInfo> readCSV(String filePath) {
        List<String> parseLineList = parseLine(filePath); // CSVの形を整えて読み込めるようにしたList
        List<String> errorMessages = new ArrayList<>(); // バリデーションエラー時のエラーメッセージのList

        LOGGER.logOutput("CSVファイルのバリデーションチェック開始。");
        validateCSV(parseLineList, errorMessages);

        if (errorMessages.isEmpty()) {
            LOGGER.logOutput("バリデーションチェック完了。");
            LOGGER.logOutput("CSVファイルの読み込み開始。");
            loadCSV(parseLineList);
        } else {
            LOGGER.logOutput("CSV読み込みに失敗しました。");
            ErrorHandler.handleError(String.join("\n", errorMessages));
        }
        
        LOGGER.logOutput("CSVファイルの読み込み完了。");
        return employeeList;
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

    /**
     * 読み込んだデータにバリデーションチェックをかける
     * @param parseLineList CSVの形を整えて読み込めるようにしたList（参照コピー）
     * @param errorMessages エラーメッセージのList（参照コピー）
     */
    private static void validateCSV(List<String> parseLineList, List<String> errorMessages) {
        for (String line : parseLineList) {
            String[] data = line.split(","); // カンマで区切って各フィールドを取り出す

            if (data[0].equals("\uFEFFNo.") || // BOM付きの場合先頭に\uFEFFが付く
                data[0].equals("No.") ||
                data[0].equals("入力例") ||
                data[0].equals("ここから入力↓↓↓↓↓↓↓↓↓↓")) {

            } else {
                for(int i = 1; i < data.length; i++) {
                    switch (i) {
                        case 1 -> System.out.println("💡追加・更新の項目は実装途中です！");
                        case 2 -> addErrorMessage(data[0], data[i], EmployeeId::new, errorMessages);
                        case 3 -> addErrorMessage(data[0], data[i], Name::new, errorMessages);
                        case 4 -> addErrorMessage(data[0], data[i], Phonetic::new, errorMessages);
                        case 5 -> addErrorMessage(data[0], data[i], BirthDate::new, errorMessages);
                        case 6 -> addErrorMessage(data[0], data[i], JoinYearMonth::new, errorMessages);
                        case 7 -> addErrorMessage(data[0], data[i], EngineerStartYear::new, errorMessages);
                        // case 8 -> addErrorMessage(data[0], data[i], TechnicalSkill::new, errorMessages); // 引数がDouble型
                        // case 9 -> addErrorMessage(data[0], data[i], Attitude::new, errorMessages); // 引数がDouble型
                        // case 10 -> addErrorMessage(data[0], data[i], CommunicationSkill::new, errorMessages); // 引数がDouble型
                        // case 11 -> addErrorMessage(data[0], data[i], Leadership::new, errorMessages); // 引数がDouble型
                        case 8 -> System.out.println("💡技術力の項目は実装途中です！"); // 引数がDouble型
                        case 9 -> System.out.println("💡受講態度の項目は実装途中です！"); // 引数がDouble型
                        case 10 -> System.out.println("💡コミュニケーション能力の項目は実装途中です！"); // 引数がDouble型
                        case 11 -> System.out.println("💡リーダーシップの項目は実装途中です！"); // 引数がDouble型
                        case 12 -> addErrorMessage(data[0], data[i], Career::new, errorMessages);
                        case 13 -> addErrorMessage(data[0], data[i], TrainingHistory::new, errorMessages);
                        case 14 -> addErrorMessage(data[0], data[i], Remarks::new, errorMessages);
                        // case 15 -> addErrorMessage(data[0], data[i], Languages::new, errorMessages);
                        default -> errorMessages.add(data[0] + "行目　" + i + "列目　「" + data[i] + "」の項目名が見つかりません。");
                    }
                }
            }
        }
    }

    /**
     * バリデーションエラーのメッセージを生成する
     * @param <T> データ型
     * @param row CSV上のNo.列の値
     * @param input バリエーションチェックする値
     * @param constructor Stringを受け取って(Type=型)を返すメソッド
     * @param errorMessages エラーメッセージのList（参照コピー）
     */
    private static <T> void addErrorMessage(String row, String input, Function<String, T> constructor, List<String> errorMessages) {
        try {
            constructor.apply(input);
        } catch (IllegalArgumentException e) {
            errorMessages.add(row + "行目　" + e.getMessage());
        }
    }

    /**
     * バリデーションチェック通過後、データを読み込む
     * @param parseLineList
     * @return
     */
    private static void loadCSV(List<String> parseLineList) {
    
        for (String line : parseLineList) {
            String[] data = line.split(","); // カンマで区切って各フィールドを取り出す

            if (data[0].equals("\uFEFFNo.") || // BOM付きの場合先頭に\uFEFFが付く
                data[0].equals("No.") ||
                data[0].equals("入力例") ||
                data[0].equals("ここから入力↓↓↓↓↓↓↓↓↓↓")) {

            } else {
                EmployeeId employeeId = new EmployeeId(data[2]);
                Name name = new Name(data[3]);
                Phonetic phonetic = new Phonetic(data[4]);
                BirthDate birthDate = new BirthDate(data[5]);
                JoinYearMonth joinYearMonth = new JoinYearMonth(data[6]);
                EngineerStartYear engineerStartYear = new EngineerStartYear(data[7]);
                // TechnicalSkill technicalSkill = new TechnicalSkill(data[8]);
                // Attitude attitude = new Attitude(data[9]);
                // CommunicationSkill communicationSkill = new CommunicationSkill(data[10]);
                // Leadership leadership = new Leadership(data[11]);
                TechnicalSkill technicalSkill = new TechnicalSkill(1);
                Attitude attitude = new Attitude(1);
                CommunicationSkill communicationSkill = new CommunicationSkill(1);
                Leadership leadership = new Leadership(1);
                Career career = new Career(data[12]);
                TrainingHistory trainingHistory = new TrainingHistory(data[13]);
                Remarks remarks = new Remarks(data[14]);
                // Languages languages = null;

                EmployeeInfo employeeInfo = new EmployeeInfo(
                employeeId,name,phonetic,birthDate,joinYearMonth,
                engineerStartYear,technicalSkill,attitude,
                communicationSkill,leadership,career,
                trainingHistory,remarks //,languages
                );
                employeeList.add(employeeInfo);
            }
        }
    }
}