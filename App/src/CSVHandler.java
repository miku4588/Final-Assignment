import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

public class CSVHandler {
    // ロガーを取得
    private static final EmployeeInfoLogger LOGGER = EmployeeInfoLogger.getInstance();
    // 読み込むCSVファイルのパス
    private String filePath;
    // CSVの形を整えて読み込めるようにしたString型のList
    private List<String> parseLineList;
    // バリデーションエラー時のメッセージ
    private List<String> errorMessages = new ArrayList<>();
    // EmployeeInfoのList
    private List<EmployeeInfo> employeeList = new ArrayList<>(); // EmployeeInfoのList
    // テンプレートファイルのヘッダー
    private static List<String> templateHeaders = new ArrayList<>();
    // ロック用のオブジェクト
    private static final Object LOCK = new Object();
    
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
     * @param isEmployeeInfoCSV データCSVを読み込むとき（起動時）ならtrue
     * @return　EmployeeInfoのリスト
     */
    public List<EmployeeInfo> readCSV(Boolean isEmployeeInfoCSV) {

        // スレッドを定義
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<EmployeeInfo>> threadReadCSV = executor.submit(() -> {
            LOGGER.logOutput(filePath + "　CSVファイル読み込み開始。");
    
            // データCSVを読み込むときはバリデーションチェックのみ実施
            if(isEmployeeInfoCSV) {
    
                if(isValidCSV(true)) {
                    loadCSV(); // CSV読み込み処理
                    LOGGER.logOutput("CSVファイル読み込み完了。");
                    return employeeList;
                } else {
                    ErrorHandler.showErrorDialog("データファイルが不正のため、読み込めませんでした。\nログファイルを確認してください。");
                    return null;
                }
            }
            
            // データCSV以外を読み込むときは3つのチェックを実施
            try {
                if(!isCSVFile()) {
                    ErrorHandler.showErrorDialog("UTF-8(BOM付き)形式のCSVファイルを選択してください。");
                    return null;
                }
            } catch (Exception e) {
                LOGGER.logException("CSVファイルの形式チェック中にエラーが発生しました。", e);
                ErrorHandler.showErrorDialog("CSVファイルの形式チェック中にエラーが発生しました。");
                return null;
            }
            if(!isSameLayout()) {
                ErrorHandler.showErrorDialog("CSVファイルのレイアウトが異なります。");
                return null;
            } else if(!isValidCSV(false)) {
                ErrorHandler.showErrorDialog(String.join("\n", errorMessages)); // 改行(\n)で区切ってerrorMessagesを羅列
                return null;
            } else {
                loadCSV(); // CSV読み込み処理
                LOGGER.logOutput("CSVファイル読み込み完了。");
                return employeeList;
            }
        });

        // スレッドを実行
        List<EmployeeInfo> result;

        try {
            result = threadReadCSV.get();
        } catch (Exception e) {
            LOGGER.logException("CSVファイルの形式チェック中にエラーが発生しました。", e);
            ErrorHandler.showErrorDialog("CSVファイルの形式チェック中にエラーが発生しました。");
            result = null;
        }
        
        return result;

    }


    /**
     * CSVファイルに社員データを書き込む
     * @param inputEmployeeList
     */
    public static void writeCSV(List<EmployeeInfo> inputEmployeeList) {
        LOGGER.logOutput("データCSVファイルへの書き込みを開始。");
        
        // スレッドを定義
        Thread threadWriteCSV = new Thread(() -> {

            // データCSVのパスと、バックアップファイルのパスを定義
            Path originalPath = Paths.get(MainApp.DATA_FILE);
            Path backupPath = Paths.get(originalPath + ".bak");
            
            // Files.moveとFiles.writeはIOExceptionになる可能性があるため囲う
            try {

                // ロックを取得して、データCSVをリネーム（バックアップのため）
                synchronized (LOCK) {
                    Files.move(originalPath, backupPath, StandardCopyOption.REPLACE_EXISTING); // REPLACE_EXISTING…ファイルが既存なら上書き
                }

                // 最終的にCSVに書き込みたいStringリストを定義
                List<EmployeeInfo> finalEmployeeList = new ArrayList<>(EmployeeManager.getEmployeeList());

                // 各要素をfinalEmployeeListに追加（更新の場合は既存データと差し替え）
                for (EmployeeInfo inputEmployee : inputEmployeeList) {
                    if (inputEmployee.getLastUpdatedDate() == null) { // 最終更新日がnullなら新規追加
                        finalEmployeeList.add(inputEmployee);
                    } else {
                        finalEmployeeList.removeIf(
                                removeEmployee -> removeEmployee.getEmployeeId().equals(inputEmployee.getEmployeeId()));
                        finalEmployeeList.add(inputEmployee);
                    }
                }

                // finalEmployeeListをString型に変換
                List<String> finalEmployeeCSVLines = new ArrayList<>();
                for (EmployeeInfo finalEmployee : finalEmployeeList) {
                    finalEmployeeCSVLines.add(finalEmployee.toString());
                }

                // ロックを取得して、データCSVに書き込み
                synchronized (LOCK) {
                    Files.write(originalPath, finalEmployeeCSVLines, StandardCharsets.UTF_8);
                }

                // EMployeeManagerのリストも更新する
                LOGGER.logOutput("データCSVファイルへの書き込み完了。");
                LOGGER.logOutput("データリストを最新の情報に更新します。");
                EmployeeManager.setEmployeeList(finalEmployeeList);
                LOGGER.logOutput("データリストの更新完了。");

            } catch (Exception e) {
                LOGGER.logException("データCSVへの書き込み中にエラーが発生しました。", e);
                ErrorHandler.showErrorDialog("データCSVへの書き込み中にエラーが発生しました。\n書き込み前のデータを復元します。");

                // バックアップから復元
                LOGGER.logOutput("データCSVファイルの復元を開始。");
                
                try {
                    synchronized (LOCK) {
                        if (Files.exists(backupPath)) {
                            Files.move(backupPath, originalPath, StandardCopyOption.REPLACE_EXISTING); // REPLACE_EXISTING…ファイルが既存なら上書き
                        }
                    }
                    LOGGER.logOutput("バックアップからCSVファイルを復元しました。");
                } catch (Exception ex) {
                    LOGGER.logException("バックアップからの復元に失敗しました。", ex);
                    ErrorHandler.showErrorDialog("バックアップからの復元に失敗しました。");
                }
            }
        }, "CSVWriter");

        threadWriteCSV.start();
    }


    /**
     * ヘッダー行を作成する
     */
    private void generateTemplateHeaders() {
        templateHeaders.add("No.,追加・更新,社員ID,氏名,氏名カナ,生年月日,入社年月,エンジニア開始年,技術力,受講態度,コミュニケーション能力,リーダーシップ,経歴,研修の受講歴,備考,扱える言語,,");
        templateHeaders.add("入力例,更新,F10000,大阪 太郎,オオサカ タロウ,2000/01/01,2024/04,2020,3.5,4,5,4.5,これは経歴です。改行も可能です。,これは研修の受講歴です。改行も可能です。,これは備考です。改行も可能です。,HTML,CSS,Java");
        templateHeaders.add("ここから入力↓↓↓↓↓↓↓↓↓↓,,,,,,,,,,,,,,,,,");
    }


    /**
     * CSVテンプレートファイルを生成する
     */
    public static void exportTemplateCSV(List<EmployeeInfo> exportEmployeeList) {
        LOGGER.logOutput("社員データ（CSVテンプレート）の出力を開始。");

        // ダウンロードフォルダのパスを取得
        String userHome = System.getProperty("user.home");
        Path downloadDirectory = Paths.get(userHome, "Downloads");

        // ダウンロードフォルダの存在確認
        // Files.exists…存在するならtrue、存在しない場合や例外発生時はfalseを返す
        if (!Files.exists(downloadDirectory)) {
            LOGGER.logOutput("ダウンロードフォルダが見つからないため処理を中止します。");
            ErrorHandler.showErrorDialog("ダウンロードフォルダが見つからないため処理を中止します。");
            return;
        }
        
        // テンプレートのファイル名を生成
        Path templateFilePath = generateTemplateFilePath(exportEmployeeList, downloadDirectory);
        
        // テンプレートファイルを作成し、内容を書き込む
        try (OutputStream out = Files.newOutputStream(templateFilePath);
             OutputStreamWriter osw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
             BufferedWriter templateWriter = new BufferedWriter(osw)) {
            
            LOGGER.logOutput(templateFilePath.toString() + "　空のCSVファイルを生成。");

            // BOMを書き込む
            out.write(0xEF);
            out.write(0xBB);
            out.write(0xBF);

            // ヘッダーを書き込む
            LOGGER.logOutput("テンプレートのヘッダーを入力。");
            templateWriter.write(String.join("\n", templateHeaders));
            templateWriter.newLine();

            // 社員情報リストがあれば書き込む
            LOGGER.logOutput("社員データを入力。");
            if (exportEmployeeList != null) {
                for (EmployeeInfo employee : exportEmployeeList) {
                    templateWriter.write(employee.toString());
                    templateWriter.newLine();
                }
            }
            
            LOGGER.logOutput("社員データ（CSVテンプレート）の出力完了。");
        } catch (IOException e) {
            LOGGER.logException("CSVテンプレートファイルを作成中にエラーが発生しました。", e);
        }
    }



    /**
     * CSVテンプレートのファイル名を生成する
     * @param exportEmployeeList 出力する社員のリスト
     * @param downloadDirectory データを出力する保存先
     * @return 生成したファイル名のパスファイル
     */
    private static Path generateTemplateFilePath(List<EmployeeInfo> exportEmployeeList, Path downloadDirectory) {
        
        // リストが空の場合と中身ありの場合でファイル名を分ける
        String baseFileName;
        if (exportEmployeeList == null || exportEmployeeList.isEmpty()) {
            baseFileName = "エンジニア情報一括取り込みテンプレート.csv";
        } else {
            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            baseFileName = "エンジニア情報一括出力_" + timeStamp + ".csv";
        }
        
        // ファイル名がダウンロードフォルダ内に存在しない場合はそのまま返す
        Path csvPath = downloadDirectory.resolve(baseFileName);
        if (!Files.exists(csvPath)) {
            return csvPath;
        }
        
        // ファイル名がダウンロードフォルダ内に存在する場合は末尾に連番を付与
        int count = 1;
        String newFileName;
        do {
            String fileNameWithoutExt = baseFileName.replace(".csv", "");
            newFileName = fileNameWithoutExt + " (" + count + ").csv";
            csvPath = downloadDirectory.resolve(newFileName);
            count++;
        } while (Files.exists(csvPath));

        return csvPath;
    }


    /**
     * CSVファイルかどうか判定する
     * @param filePath
     * @return CSVファイルならtrue、CSVではないならfalse
     */
    public boolean isCSVFile() throws IOException {
        LOGGER.logOutput("CSVファイルのファイル形式チェック開始");
        if (filePath == null || filePath.isEmpty()) {
            LOGGER.logOutput("ファイル形式チェックNG。ファイルが選択されていません。");
            return false;
        } else if(!filePath.toLowerCase().endsWith(".csv")) {
            LOGGER.logOutput("ファイル形式チェックNG。異なる形式のファイルが選択されています。");
            return false;
        } else {
            // BufferedeReaderはIOExceptionを投げる場合があるので、呼び出し元でキャッチする。
            BufferedReader br = Files.newBufferedReader(Paths.get(filePath));
            String firstLine = br.readLine();
            br.close();
            if (firstLine == null || !firstLine.startsWith("\uFEFF")) {
                LOGGER.logOutput("ファイル形式チェックNG。BOM付きUTF-8以外のCSVファイルが指定されました。");
                return false;
            }
        }
        LOGGER.logOutput("ファイル形式チェックOK。");
        return true;
    }


    /**
     * ヘッダーがテンプレートファイルと一致するか判定する
     * @return
     */
    public boolean isSameLayout() {
        LOGGER.logOutput("CSVファイルのレイアウトチェック開始。");

        // targetHeadersに最初の3行を格納
        List<String> targetHeaders = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            for(int i = 0; i < 3; i++) {
                line = br.readLine();

                // 先頭が"\uFEFF"だった場合は1文字目を削除
                if (line != null && line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }

                targetHeaders.add(line);
            }
        } catch(IOException e) {
            LOGGER.logException("CSVファイルのレイアウトチェックに失敗しました。", e);
            ErrorHandler.showErrorDialog("CSVファイルのレイアウトチェックに失敗しました。");
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
            if (!templateLine.equals(targetLine)) {
                LOGGER.logOutput("レイアウトチェックNG。ヘッダーがテンプレートと異なります。");
                return false;
            }
        }

        LOGGER.logOutput("レイアウトチェックOK。");
        return true;
    }
    

    /**
     * 読み込んだCSVがバリデーションチェックOKかどうか判定する
     * @return OKならtrue、NGならfalse
     */
    public boolean isValidCSV(Boolean isEmployeeInfoCSV) {
        parseLineList = parseLine(); // 指定されたCSVデータの中身をListに格納
        LOGGER.logOutput("CSVファイルのバリデーションチェック開始。");

        for (String line : parseLineList) {
            String[] data = line.split(","); // カンマで区切って各フィールドを取り出す

            if (data[0].equals("No.") ||
                data[0].equals("入力例") ||
                data[0].equals("ここから入力↓↓↓↓↓↓↓↓↓↓")) {

            } else {
                // 先にLanguagesのインスタンスを用意
                Languages languages = new Languages();
                
                for(int i = 1; i < data.length; i++) {
                    // switchはアロー構文で書くとbreakなくてもブロックから抜けられる！
                    switch (i) {

                        // 1個目（追加or更新）
                        case 1 -> {
                            if(isEmployeeInfoCSV == true) {
                                // データCSVを読み込んでいるときは追加・更新の判定不要
                            }else if(data[i].equals("追加")) {
                                if(isEmployeeIdExists(data[2]) == true) {
                                    errorMessages.add(data[0] + "行目　社員ID「" + data[2] + "」は既に存在します。");
                                }
                            }else if(data[i].equals("更新")) {
                                if(isEmployeeIdExists(data[2]) == false) {
                                    errorMessages.add(data[0] + "行目　社員ID「" + data[2] + "」と一致する社員が見つかりません。");
                                }
                            } else {
                                errorMessages.add(data[0] + "行目　" + (i + 1) + "列目には「追加」もしくは「更新」と入れてください。");
                            }
                        }

                        // 2個目（社員ID）～14個目（備考）
                        case 2 -> addErrorMessage(data[0], data[2], EmployeeId::new);
                        case 3 -> addErrorMessage(data[0], data[i], Name::new);
                        case 4 -> addErrorMessage(data[0], data[i], Phonetic::new);
                        case 5 -> addErrorMessage(data[0], data[i], BirthDate::new);
                        case 6 -> addErrorMessage(data[0], data[i], JoinYearMonth::new);
                        case 7 -> addErrorMessage(data[0], data[i], EngineerStartYear::new);
                        case 8 -> addErrorMessage(data[0], data[i], TechnicalSkill::new);
                        case 9 -> addErrorMessage(data[0], data[i], Attitude::new);
                        case 10 -> addErrorMessage(data[0], data[i], CommunicationSkill::new);
                        case 11 -> addErrorMessage(data[0], data[i], Leadership::new);
                        case 12 -> addErrorMessage(data[0], data[i], Career::new);
                        case 13 -> addErrorMessage(data[0], data[i], TrainingHistory::new);
                        case 14 -> addErrorMessage(data[0], data[i], Remarks::new);

                        // 15個目以降（扱える言語）
                        default -> {
                            Boolean isValidLanguage = languages.addLanguage(data[i]);
                            if(!isValidLanguage) {
                                errorMessages.add(data[0] + "行目　「" + data[i] + "」は有効な言語ではありません。");
                            }
                        }
                    }
                }
            }
        }

        if (errorMessages.isEmpty()) {
            LOGGER.logOutput("バリデーションチェック完了。");
            return true;
        } else {
            LOGGER.logOutput("バリデーションチェックNG。");
            LOGGER.logOutput("バリデーションエラーの一覧を出力します。\n" + String.join("\n", errorMessages)); // 改行(\n)で区切ってerrorMessagesを羅列
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

                // 先頭が"\uFEFF"だった場合は1文字目を削除
                if (line != null && line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }

                // 現在の行をバッファに追加して、ダブルクォートの数をカウント
                buffer.append(line); 
                int quoteCount = buffer.toString().replaceAll("[^\"]", "").length();

                // ダブルクォートが偶数個ならListへ、そうでないなら次の行を連結
                if (quoteCount % 2 == 0) {
                    parseLineList.add(buffer.toString()); // Listに格納
                    buffer.setLength(0); // バッファを空にする
                } else {
                    buffer.append("\n"); // 改行して行を連結
                }
            }

            if(parseLineList.size() == 1 && parseLineList.get(0) == "") {
                parseLineList.remove(0);
            }
        } catch (IOException e) {
            ErrorHandler.showErrorDialog("CSVファイルの読み込み中にエラーが発生しました。\nログファイルを確認してください。");
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
        } catch (Exception e) {
            errorMessages.add(row + "行目　" + e.getMessage());
        }
    }


    /**
     * parseLineListを読み込む
     */
    private void loadCSV() {

        if (parseLineList.isEmpty()) {
            LOGGER.logOutput("データが1件もないため読み込み処理を終了します。");
            return;
        }
    
        for (String line : parseLineList) {
            String[] data = line.split(","); // カンマで区切って各フィールドを取り出す

            if (data[0].equals("\uFEFFNo.") || // BOM付きの場合先頭に\uFEFFが付く
                data[0].equals("No.") ||
                data[0].equals("入力例") ||
                data[0].equals("ここから入力↓↓↓↓↓↓↓↓↓↓")) {

            } else {
                // 各項目のインスタンスを作成
                EmployeeId employeeId = new EmployeeId(getValueOrEmpty(data, 2));
                Name name = new Name(getValueOrEmpty(data, 3));
                Phonetic phonetic = new Phonetic(getValueOrEmpty(data, 4));
                BirthDate birthDate = new BirthDate(getValueOrEmpty(data, 5));
                JoinYearMonth joinYearMonth = new JoinYearMonth(getValueOrEmpty(data, 6));
                EngineerStartYear engineerStartYear = new EngineerStartYear(getValueOrEmpty(data, 7));
                TechnicalSkill technicalSkill = new TechnicalSkill(getValueOrEmpty(data, 8));
                Attitude attitude = new Attitude(getValueOrEmpty(data, 9));
                CommunicationSkill communicationSkill = new CommunicationSkill(getValueOrEmpty(data, 10));
                Leadership leadership = new Leadership(getValueOrEmpty(data, 11));
                Career career = new Career(getValueOrEmpty(data, 12));
                TrainingHistory trainingHistory = new TrainingHistory(getValueOrEmpty(data, 13));
                Remarks remarks = new Remarks(getValueOrEmpty(data, 14));
                Languages languages = new Languages();

                // 扱える言語のListを作成
                int languagesCount = data.length;
                for(int i = 15; i < languagesCount; i++) {
                    languages.addLanguage(data[i]);
                }

                // データ作成日と最終更新日を設定
                // CSV読み込み用テンプレートの場合…0個目がNo.、1個目が追加or更新
                // データCSVの場合…0個目がデータ作成日、1個目が最終更新日
                LocalDate creationDate = null;
                LocalDate lastUpdatedDate = null;
                if(data[1].equals("追加")) {
                    creationDate = LocalDate.now();
                } else if(data[1].equals("更新")) {
                    creationDate = getCreationDateByEmployeeId(data[2]); // 社員IDが一致するデータの作成日と同じ日を設定
                    lastUpdatedDate = LocalDate.now();
                } else {
                    // 0個目には必ずデータ作成日が入っているのでそのまま設定
                    creationDate = LocalDate.parse(data[0], DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                    // 1個目はブランクがありえるため、ブランクでない場合に最終更新日を設定（ブランクの場合はnull）
                    if(data[1] != "") {
                        lastUpdatedDate = LocalDate.parse(data[1], DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                    }
                }
                
                // EmployeeInfoのインスタンスを作成
                EmployeeInfo employeeInfo = new EmployeeInfo(
                employeeId,name,phonetic,birthDate,joinYearMonth,
                engineerStartYear,technicalSkill,attitude,
                communicationSkill,leadership,career,
                trainingHistory,remarks,languages,creationDate,lastUpdatedDate
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


    /**
     * 社員IDがデータリストの中に存在するかどうか
     * @param inputEmployeeId
     * @return データリストの中に存在したらtrue、存在しなければfalse
     */
    private Boolean isEmployeeIdExists(String inputEmployeeId) {
        for(EmployeeInfo employee : EmployeeManager.getEmployeeList()) {
            if(inputEmployeeId.equals(employee.getEmployeeId())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 社員IDが一致するデータのデータ作成日を返す
     * @param inputEmployeeId
     * @return データ作成日
     */
    private LocalDate getCreationDateByEmployeeId(String inputEmployeeId) {
        for(EmployeeInfo employee : EmployeeManager.getEmployeeList()) {
            if(inputEmployeeId.equals(employee.getEmployeeId())) {
                return employee.getCreationDate();
            }
        }
        return null;
    }
}