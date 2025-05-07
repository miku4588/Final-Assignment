import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;

public class CSVHandler {
    // ロガーを取得
    private static final EmployeeInfoLogger LOGGER = EmployeeInfoLogger.getInstance();
    // CSVファイルのパス
    private String filePath;
    // CSVの形を整えて読み込めるようにしたString型のList
    private List<String> parseLineList;
    // バリデーションエラー時のメッセージ
    private List<String> errorMessages = new ArrayList<>();
    // EmployeeInfoのList
    private List<EmployeeInfo> employeeList = new ArrayList<>(); // EmployeeInfoのList
    // テンプレートファイルのヘッダー
    private List<String> templateHeaders = new ArrayList<>();
    
    /**
     * コンストラクタ
     * @param filePath 取り扱うCSVファイルのパス
     */
    public CSVHandler(String filePath) {
        this.filePath = filePath;
        generateTemplateHeaders();
    }
    
    /**
     * CSVファイルを読み込み、EmployeeInfo型に変換したデータのListを返す
     * @return EmployeeInfoのList
     */
    public List<EmployeeInfo> readCSV() {
        LOGGER.logOutput(filePath + "　CSVファイル読み込み開始。");

        // データCSVを読み込むときはバリデーションチェックのみ実施
        if(filePath == "data/EmployeeInfo.csv") {
            if(isValidCSV()) {
                loadCSV(); // CSV読み込み処理
                LOGGER.logOutput("CSVファイル読み込み完了。");
                return employeeList;
            } else {
                ErrorHandler.handleError("データファイルが不正のため、データを読み込めませんでした。\nログファイルを確認してください。");
                return null;
            }
        }
        
        // データCSV以外を読み込むときは3つのチェックを実施
        if(!isCSVFile()) {
            ErrorHandler.handleError("CSVファイルを選択してください。");
            return null;
        } else if(!isSameLayout()) {
            ErrorHandler.handleError("CSVファイルのレイアウトが異なります。");
            return null;
        } else if(!isValidCSV()) {
            ErrorHandler.handleError(String.join("\n", errorMessages)); // バリデーションエラーを一覧で出力
            return null;
        } else {
            loadCSV(); // CSV読み込み処理
            LOGGER.logOutput("CSVファイル読み込み完了。");
            return employeeList;
        }
    }

    /**
     * CSVファイルに社員データを書き込む
     * @param employeeList
     */
//💡💡💡💡💡まだできてません
    public void writeCSV(List<EmployeeInfo> employeeList) {
        LOGGER.logOutput(filePath + "　CSVファイルへの書き込みを開始。");

        // BufferedWriterとFileWriterで1行ずつ書き込む
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // 1. ヘッダーを書き込む（例: id,name,age,skill）
            writer.write("id,name,age,skill");
            writer.newLine();

            // 2. 各EmployeeInfoのデータを書き込む
            for (EmployeeInfo employee : employeeList) {
                writer.write(employee.toString());
                writer.newLine();
            }
            LOGGER.logOutput("CSVファイルへの書き込み完了。");
        } catch (IOException e) {
            LOGGER.logOutput("CSVファイルへの書き込み失敗: " + e.getMessage());
        }
        LOGGER.logOutput("CSVファイルへの書き込み完了。");
    }

    /**
     * ヘッダー行を作成する
     */
    private void generateTemplateHeaders() {
        templateHeaders.add("No.,追加・更新,社員ID,氏名,氏名カナ,生年月日,入社年月,エンジニア開始年,技術力,受講態度,コミュニケーション能力,リーダーシップ,経歴,研修の受講歴,備考,扱える言語,,");
        templateHeaders.add("入力例,更新,F10000,大阪 太郎,オオサカ タロウ,2000/01/01,2024/04,2020,3.5,4,5,4.5,\"これは経歴です。\n" +
                            "改行も可能です。\",\"これは研修の受講歴です。\n" +
                            "改行も可能です。\",\"これは備考です。\n" +
                            "改行も可能です。\",HTML,CSS,Java");
        templateHeaders.add("ここから入力↓↓↓↓↓↓↓↓↓↓,,,,,,,,,,,,,,,,,");
    }

    /**
     * CSVファイルかどうか判定する
     * @param filePath
     * @return CSVファイルならtrue、CSVではないならfalse
     */
    public boolean isCSVFile() {
        LOGGER.logOutput(filePath + "　CSVファイルのファイル形式チェック開始");
        if (filePath == null || filePath.isEmpty()) {
            LOGGER.logOutput("ファイル形式チェックNG。ファイルが選択されていません。");
            return false;
        } else if(!filePath.toLowerCase().endsWith(".csv")) {
            LOGGER.logOutput("ファイル形式チェックNG。異なる形式のファイルが選択されています。");
            return false;
        }
        LOGGER.logOutput("ファイル形式チェックOK。");
        return true;
    }

    /**
     * ヘッダーがテンプレートファイルと一致するか判定する
     * @return
     */
    public boolean isSameLayout() {
        LOGGER.logOutput(filePath + "CSVファイルのレイアウトチェック開始。");

        // targetHeadersに最初の3行を格納
        List<String> targetHeaders = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            for(int i = 0; i < 3; i++) {
                line = br.readLine();
                targetHeaders.add(line);
            }
        } catch(IOException e) {
//💡💡💡💡💡エラー処理考え中
            LOGGER.logException(filePath, e);
            ErrorHandler.handleError("CSVファイルのレイアウトチェックに失敗しました。");
            return false;
        }

        // targetHeadersが3行なければfalse
        if (targetHeaders.size() < 3) {
            LOGGER.logOutput("レイアウトチェックNG。ヘッダーがテンプレートと異なります。");
            return false;
        }

        // 1～3行目をテンプレートと比較
        for(int i = 0; i < 3; i++) {

            // 「扱える言語」より後ろを除外
            String templateLine = templateHeaders.get(i);
            String targetLine = targetHeaders.get(i);
            String[] templateColumns = templateLine.split(",", -1);
            String[] targetColumns = targetLine.split(",", -1);

            // 各行の各フィールドをひとつずつ比較
            // 0番目（No.）から15番目（扱える言語）まで
            for (int j = 0; j < 15; j++) {
                if (!templateColumns[j].equals(targetColumns[j])) {
                    LOGGER.logOutput("レイアウトチェックNG。ヘッダーがテンプレートと異なります。");
                    return false;
                }
            }
        }

        LOGGER.logOutput("レイアウトチェックOK。");
        return true;
    }
    
    /**
     * 読み込んだCSVがバリデーションチェックOKかどうか判定する
     * @return OKならtrue、NGならfalse
     */
    public boolean isValidCSV() {
        parseLineList = parseLine(); // 指定されたCSVデータの中身をListに格納
        LOGGER.logOutput(filePath + "　CSVファイルのバリデーションチェック開始。");

        for (String line : parseLineList) {
            String[] data = line.split(","); // カンマで区切って各フィールドを取り出す

            if (data[0].equals("\uFEFFNo.") || // BOM付きの場合先頭に\uFEFFが付く
                data[0].equals("No.") ||
                data[0].equals("入力例") ||
                data[0].equals("ここから入力↓↓↓↓↓↓↓↓↓↓")) {

            } else {
                for(int i = 1; i < data.length; i++) {
                    // switchはアロー構文で書くとbreakなくてもswitch抜けられる！
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

            if(input.isEmpty()) {
                constructor.apply("");

            } else {
                constructor.apply(input);
            }
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
                EmployeeId employeeId = new EmployeeId(getValueOrEmpty(data, 2));
                Name name = new Name(getValueOrEmpty(data, 3));
                Phonetic phonetic = new Phonetic(getValueOrEmpty(data, 4));
                BirthDate birthDate = new BirthDate(getValueOrEmpty(data, 5));
                JoinYearMonth joinYearMonth = new JoinYearMonth(getValueOrEmpty(data, 6));
                EngineerStartYear engineerStartYear = new EngineerStartYear(getValueOrEmpty(data, 7));
                // TechnicalSkill technicalSkill = new TechnicalSkill(getValueOrEmpty(data, 8));
                // Attitude attitude = new Attitude(getValueOrEmpty(data, 9));
                // CommunicationSkill communicationSkill = new CommunicationSkill(getValueOrEmpty(data, 10));
                // Leadership leadership = new Leadership(getValueOrEmpty(data, 11));
                TechnicalSkill technicalSkill = new TechnicalSkill(1);
                Attitude attitude = new Attitude(1);
                CommunicationSkill communicationSkill = new CommunicationSkill(1);
                Leadership leadership = new Leadership(1);
                Career career = new Career(getValueOrEmpty(data, 12));
                TrainingHistory trainingHistory = new TrainingHistory(getValueOrEmpty(data, 13));
                Remarks remarks = new Remarks(getValueOrEmpty(data, 14));
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

    /**
     * 値がなければ""（空文字）を返す
     * @param data parseLineListをカンマで区切ったList
     * @param index Listの何個目の要素か
     * @return 値そのまま返すか、空文字を返す
     */
    private String getValueOrEmpty(String[] data, int index) {

        // indexがデータの外にない、かつ値がnullでない、かつ長さが0でない
        if (data.length > index && data[index] != null && !data[index].trim().isEmpty()) {
            return data[index];
        }

        // 上記の条件以外の場合は必ず空文字を返す
        return "";
    }
}