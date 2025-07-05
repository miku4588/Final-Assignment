import java.util.HashSet;
import java.util.Set;

public class Languages {

    public static final String[] VALID_LANGUAGES = new String[0];
    private Set<String> languages; // インスタンス変数（プログラミング言語のセット）

    // コンストラクタ
    public Languages() {
        this.languages = new HashSet<>(); // 初期化
    }
    public Languages(String language) {
    this(); // 引数なしコンストラクタを呼んでセット初期化
    addLanguage(language);
}

    // 1つの言語を追加
    public boolean addLanguage(String language) {
        if (language != null && !language.trim().isEmpty()) {
            languages.add(language.trim());
            return true;
        }
        System.out.println("無効な言語です: " + language);
        return false;
    }

    // 複数言語を追加
    public Set<String> addLanguages(Set<String> languagesList) {
        Set<String> addedLanguages = new HashSet<>();
        for (String language : languagesList) {
            if (addLanguage(language)) {
                addedLanguages.add(language);
            }
        }
        return addedLanguages;
    }

    // 登録済み言語を取得
    public Set<String> getLanguages() {
        return new HashSet<>(languages); // コピーを返す（外部からの書き換え防止）
    }
}
