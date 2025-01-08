import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SortFunction {
    private Map<String, Boolean> sortCriteria;

    public SortFunction(Map<String, Boolean> sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    public List<EmployeeInfo> sortByEmployeeId(List<EmployeeInfo> employeeList) {
        return employeeList.stream()
                .sorted(Comparator.comparing(emp -> emp.getEmployeeId().getEmployeeId()))
                .collect(Collectors.toList());
    }

    public List<EmployeeInfo> sortByName(List<EmployeeInfo> employeeList) {
        return employeeList.stream()
                .sorted(Comparator.comparing(emp -> emp.getName().getName()))
                .collect(Collectors.toList());
    }

    public List<EmployeeInfo> sortByAge(List<EmployeeInfo> employeeList) {
        return employeeList.stream()
                .sorted(Comparator.comparingInt(emp -> emp.calculateAge(emp.getBirthDate())))
                .collect(Collectors.toList());
    }

    public List<EmployeeInfo> sortByYears(List<EmployeeInfo> employeeList) {
        return employeeList.stream()
                .sorted(Comparator.comparingInt(emp -> emp.calculateYears(emp.getJoinDate())))
                .collect(Collectors.toList());
    }
}
