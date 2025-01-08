public class Name {
    private String name; // 従業員の名前（20文字以内）

    /**
     * コンストラクタ
     */
    public Name(String name) {
        //名前の長さチェック
        if (name.length() > 20) {
            throw new IllegalArgumentException("名前は20文字以内でなければなりません。");
        }
        this.name = name;
    }

    /**
     * 名前を返すメソッド
     * @return
     */
    public String getName() {
        return name;
    }
}


/* Nameクラスのインスタンスを作成する際に、名前を引数として渡します。
コンストラクタは、その名前の長さをチェックします。
名前が20文字を超えている場合、例外をスローしてインスタンスの作成を中止します。
名前が20文字以内であれば、インスタンス変数にその名前を設定します。*/
