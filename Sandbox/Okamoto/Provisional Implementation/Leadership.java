public class Leadership {
    private int leadership; // リーダーシップの評価（1-5の範囲）

    public Leadership(int leadership) {
        if (leadership < 1 || leadership > 5) {
            throw new IllegalArgumentException("リーダーシップは1から5の範囲でなければなりません。");
        }
        this.leadership = leadership;
    }

    public int getLeadership() {
        return leadership;
    }
}
