import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;

public class CSVHandler {
    // ロガーを取得
    private static final EmployeeInfoLogger LOGGER = EmployeeInfoLogger.getInstance();
    // CSVファイルのパス
    private String filePath = null;
    // CSVの形を整えて読み込めるようにしたString型のList
    private List<String> parseLineList;
    // バリデーションエラー時のメッセージ
    private List<String> errorMessages = new ArrayList<>();
    // EmployeeInfoのList
    private static List<EmployeeInfo> employeeList = new ArrayList<>(); // EmployeeInfoのList
    
    /**
     * コンストラクタ
     * @param filePath 取り扱うCSVファイルのパス
     */
    public CSVHandler(String filePath) {
        this.filePath = filePath;
    }
    
    /**
     * CSVファイルを読み込み、EmployeeInfo型に変換したデータのListを返す<p>
     * 💡先にisValidCSVを実行してから呼び出してください！
     * @return EmployeeInfoのList
     */
    public List<EmployeeInfo> readCSV() {
        LOGGER.logOutput(filePath + "　CSVファイル読み込み開始。");
        
        if(parseLineList == null) {
            LOGGER.logOutput("バリデーションチェックが実施されていません。");
            ErrorHandler.handleError("バリデーションチェックが実施されていません。");
            return null;
        } else {
            loadCSV(); // CSV読み込み処理
            LOGGER.logOutput("CSVファイル読み込み完了。");
            return employeeList;
        }
    }

    public void writeCSV(List<EmployeeInfo> employeeList) {
        LOGGER.logOutput(filePath + "　CSVファイルへの書き込みを開始。");
        // 💡ファイルハンドラーか何かでCSVファイルを開いて直接書き込む
        // 💡書き込みが終わったらデータリストを更新するためにデータCSV読み込んでリストを返す？？？
        System.out.println("ここまだ出来てないです！！！");
        LOGGER.logOutput("CSVファイルへの書き込み完了。");
    }
    
    /**
     * 読み込んだCSVがバリデーションチェックOKかどうか
     * @return OKならtrue、NGならfalse
     */
    public boolean isValidCSV() {
        parseLineList = parseLine(); // 指定されたCSVデータの中身をListに格納
        LOGGER.logOutput(filePath + "　CCSVファイルのバリデーションチェック開始。");

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
                        case 2 -> addErrorMessage(data[0], data[i], EmployeeId::new);
                        case 3 -> addErrorMessage(data[0], data[i], Name::new);
                        case 4 -> addErrorMessage(data[0], data[i], Phonetic::new);
                        case 5 -> addErrorMessage(data[0], data[i], BirthDate::new);
                        case 6 -> addErrorMessage(data[0], data[i], JoinYearMonth::new);
                        case 7 -> addErrorMessage(data[0], data[i], EngineerStartYear::new);
                        // case 8 -> addErrorMessage(data[0], data[i], TechnicalSkill::new); // 引数がDouble型
                        // case 9 -> addErrorMessage(data[0], data[i], Attitude::new); // 引数がDouble型
                        // case 10 -> addErrorMessage(data[0], data[i], CommunicationSkill::new); // 引数がDouble型
                        // case 11 -> addErrorMessage(data[0], data[i], Leadership::new); // 引数がDouble型
                        case 8 -> System.out.println("💡技術力の項目は実装途中です！"); // 引数がDouble型
                        case 9 -> System.out.println("💡受講態度の項目は実装途中です！"); // 引数がDouble型
                        case 10 -> System.out.println("💡コミュニケーション能力の項目は実装途中です！"); // 引数がDouble型
                        case 11 -> System.out.println("💡リーダーシップの項目は実装途中です！"); // 引数がDouble型
                        case 12 -> addErrorMessage(data[0], data[i], Career::new);
                        case 13 -> addErrorMessage(data[0], data[i], TrainingHistory::new);
                        case 14 -> addErrorMessage(data[0], data[i], Remarks::new);
                        // case 15 -> addErrorMessage(data[0], data[i], Languages::new);
                        default -> errorMessages.add(data[0] + "行目　" + i + "列目　「" + data[i] + "」の項目名が見つかりません。");
                    }
                }
            }
        }

        if (errorMessages.isEmpty()) {
            LOGGER.logOutput("バリデーションチェック完了。");
            return true;
        } else {
            LOGGER.logOutput("バリデーションチェックNG。");
            LOGGER.logOutput(String.join("\n", errorMessages)); // 改行(\n)で区切ってerrorMessagesを羅列
            return false;
        }
    }

    /**
     * 指定されたCSVファイルの各行をListに格納する<p>
     * ※改行を含むフィールドがある場合は行を連結する
     * @param filePath 
     * @return 抽出した各行を格納したList
     */
    private List<String> parseLine() {
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
            ErrorHandler.handleError("CSVファイルの読み込み中にエラーが発生しました。\nログファイルを確認してください。");
            LOGGER.logException("CSVファイルの読み込み中にエラーが発生しました。", e);
        }

        return parseLineList;
    }
    
    /**
     * バリデーションエラーのメッセージを生成する
     * @param <T> データ型
     * @param row CSV上のNo.列の値
     * @param input バリエーションチェックする値
     * @param constructor Stringを受け取って(Type=型)を返すメソッド
     */
    private <T> void addErrorMessage(String row, String input, Function<String, T> constructor) {
        try {
            constructor.apply(input);
        } catch (IllegalArgumentException e) {
            errorMessages.add(row + "行目　" + e.getMessage());
        }
    }

    /**
     * parseLineListを読み込む
     */
    private void loadCSV() {
    
        for (String line : parseLineList) {
            String[] data = line.split(","); // カンマで区切って各フィールドを取り出す

            if (data[0].equals("\uFEFFNo.") || // BOM付きの場合先頭に\uFEFFが付く
                data[0].equals("No.") ||
                data[0].equals("入力例") ||
                data[0].equals("ここから入力↓↓↓↓↓↓↓↓↓↓")) {

            } else {
                //　各項目のインスタンスを作成
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

                // EmployeeInfoのインスタンスを作成
                EmployeeInfo employeeInfo = new EmployeeInfo(
                employeeId,name,phonetic,birthDate,joinYearMonth,
                engineerStartYear,technicalSkill,attitude,
                communicationSkill,leadership,career,
                trainingHistory,remarks //,languages
                );

                // employeeListに追加
                employeeList.add(employeeInfo);
            }
        }
    }
}