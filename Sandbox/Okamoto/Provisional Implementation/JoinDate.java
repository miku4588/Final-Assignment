import java.text.ParseException;//日付の解析中に発生する可能性のある例外を扱うためにParseExceptionをインポート
import java.text.SimpleDateFormat;//日付のフォーマットを指定して解析するためにSimpleDateFormatをインポート
import java.util.Date;//日付と時刻を表すためのDateクラスをインポート

public class JoinDate {
    //フィールド
    private Date joinDate; // 従業員の入社日（YYYY/MM形式）

    //コンストラクタ
    //throws ParseExceptionは、日付の解析中に例外が発生する可能性があることを示す
    public JoinDate(String joinDate) throws ParseException {
        //SimpleDateFormatのインスタンスsdfを作成し、フォーマットを"yyyy/MM"に設定。年と月の形式で日付を解析できる
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
        this.joinDate = sdf.parse(joinDate);
    }

    //ゲッターメソッド
    public Date getJoinDate() {
        return joinDate;
    }
}
