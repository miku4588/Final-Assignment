import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchFunction {
    private Map<String, String> searchCriteria;

    public SearchFunction(Map<String, String> searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public List<EmployeeInfo> keywordSearch(List<EmployeeInfo> employeeList) {
        String keyword = searchCriteria.get("keyword");
        return employeeList.stream()
                .filter(emp -> emp.getName().getName().contains(keyword) ||
                               emp.getEmployeeId().getEmployeeId().contains(keyword))
                .collect(Collectors.toList());
    }

    public List<EmployeeInfo> filterSearch(List<EmployeeInfo> employeeList) {
        return employeeList.stream()
                .filter(emp -> {
                    boolean matches = true;
                    // フィルター処理
                    return matches;
                })
                .collect(Collectors.toList());
    }
}
