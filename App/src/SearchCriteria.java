
/*
 * 検索クラス
 */

import java.util.Set;

public class SearchCriteria {

    private String keyword;

    public SearchCriteria(String keyword) {
        this.keyword = keyword != null ? keyword.trim() : "";
    }

    public boolean matches(EmployeeInfo employee) {
        if (keyword.isEmpty())
            return true;

        return contains(employee.getEmployeeId()) ||
                contains(employee.getName()) ||
                contains(employee.getPhonetic()) ||
                containsInLanguages(employee.getLanguages());
    }

    private boolean contains(String field) {
        return field != null && field.toLowerCase().contains(keyword.toLowerCase());
    }

    private boolean containsInLanguages(Set<String> languages) {
        if (languages == null)
            return false;
        String lowerKeyword = keyword.toLowerCase();
        for (String lang : languages) {
            if (lang != null && lang.toLowerCase().contains(lowerKeyword)) {
                return true;
            }
        }
        return false;
    }
}
