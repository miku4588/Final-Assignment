import java.util.HashSet;
import java.util.Set;

/**
 * ユーザーが任意のプログラミング言語を追加し、管理するクラス。
 */
public class Languages {
    private Set<String> languages; // プログラミング言語のセット

    /**
     * コンストラクタ
     */
    public Languages() {
        this.languages = new HashSet<>(); // 空のセットを初期化
    }

    /**
     * プログラミング言語を追加するメソッド。
     *
     * @param language 追加するプログラミング言語
     * @return 追加成功なら true、失敗なら false
     */
    public boolean addLanguage(String language) {
        if (language != null && !language.trim().isEmpty()) {
            languages.add(language.trim()); // 言語をセットに追加（前後の空白をトリム）
            return true; // 追加成功
        }
        System.out.println("無効な言語です: " + language); // エラーメッセージ
        return false; // 追加失敗
    }

    /**
     * 複数のプログラミング言語を一括追加するメソッド。
     *
     * @param languagesList 追加するプログラミング言語のセット
     * @return 追加成功した言語のセット
     */
    public Set<String> addLanguages(Set<String> languagesList) {
        Set<String> addedLanguages = new HashSet<>(); // 追加成功した言語のセット
        for (String language : languagesList) {
            if (addLanguage(language)) {
                addedLanguages.add(language); // 追加成功した言語をセットに追加
            }
        }
        return addedLanguages; // 追加成功した言語のセットを返す
    }

    /**
     * 扱えるプログラミング言語のセットを取得するメソッド。
     *
     * @return プログラミング言語のセット
     */
    public Set<String> getLanguages() {
        return new HashSet<>(languages); // 言語のセットを返す
    }
}
