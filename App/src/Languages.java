import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 従業員が扱えるプログラミング言語のリストを管理し、バリデーションを行うクラス。
 */
public class Languages extends EmployeeInfoValidator {
    private List<String> languages; // プログラミング言語のリスト
    public static final List<String> VALID_LANGUAGES = Arrays.asList("Java", "Python", "C++", "JavaScript"); // 有効な言語リスト

    /**
     * コンストラクタ
     */
    public Languages() {
        this.languages = new ArrayList<>(); // 空のリストを初期化
    }

    /**
     * プログラミング言語を追加するメソッド。
     *
     * @param language 追加するプログラミング言語
     * @return 追加成功なら true、失敗なら false
     */
    public boolean addLanguage(String language) {
        if (validateInput(language) && VALID_LANGUAGES.contains(language)) {
            languages.add(language); // 言語をリストに追加
            return true; // 追加成功
        }
        System.out.println("無効な言語です: " + language); // エラーメッセージ
        return false; // 追加失敗
    }

    /**
     * 複数のプログラミング言語を一括追加するメソッド。
     *
     * @param languagesList 追加するプログラミング言語のリスト
     * @return 追加成功した言語のリスト
     */
    public List<String> addLanguages(List<String> languagesList) {
        List<String> addedLanguages = new ArrayList<>(); // 追加成功した言語のリスト
        for (String language : languagesList) {
            if (addLanguage(language)) {
                addedLanguages.add(language); // 追加成功した言語をリストに追加
            }
        }
        return addedLanguages; // 追加成功した言語のリストを返す
    }

    /**
     * 扱えるプログラミング言語のリストを取得するメソッド。
     *
     * @return プログラミング言語のリスト
     */
    public List<String> getLanguages() {
        return new ArrayList<>(languages); // 言語のリストを返す
    }
}
