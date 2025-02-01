public class Remarks {
    private String remarks; // 備考（改行可能、500文字以内）

    public Remarks(String remarks) {
        if (remarks.length() > 500) {
            throw new IllegalArgumentException("備考は500文字以内でなければなりません。");
        }
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }
}
