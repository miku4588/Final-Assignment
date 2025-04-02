import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 従業員が扱えるプログラミング言語のリストを管理し、バリデーションを行うクラス。
 */
public class Languages extends EmployeeInfoValidator {
    private List<String> languages;
    private static final List<String> VALID_LANGUAGES = Arrays.asList(
        "Java", "Python", "JavaScript", "C++", "C#", "Go", "Ruby", "Swift", "Kotlin", "PHP", "TypeScript"
    );

    /**
     * コンストラクタ
     */
    public Languages() {
        this.languages = new ArrayList<>();
    }

    /**
     * プログラミング言語を追加するメソッド。
     * 追加する言語が適切な形式かつ有効な言語かを検証し、適切ならリストに追加。
     *
     * @param language 追加するプログラミング言語
     * @return 追加成功なら true、失敗なら false
     */
    public boolean addLanguage(String language) {
        if (validateInput(language) && VALID_LANGUAGES.contains(language)) {
            languages.add(language);
            return true;
        }
        System.out.println("無効な言語です: " + language);
        return false;
    }

    /**
     * 複数のプログラミング言語を一括追加するメソッド。
     *
     * @param languagesList 追加するプログラミング言語のリスト
     * @return 追加成功した言語のリスト
     */
    public List<String> addLanguages(List<String> languagesList) {
        List<String> addedLanguages = new ArrayList<>();
        for (String language : languagesList) {
            if (addLanguage(language)) {
                addedLanguages.add(language);
            }
        }
        return addedLanguages;
    }

    /**
     * 扱えるプログラミング言語のリストを取得するメソッド。
     *
     * @return プログラミング言語のリスト
     */
    public List<String> getLanguages() {
        return new ArrayList<>(languages);
    }
}
