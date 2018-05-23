package a;

/**
 * Created by ofer on 10/04/18.
 */
import java.util.*;


// BEGIN DEFINITIONS
// DO NOT MODIFY THIS SECTION

class EmployeeStats {
    public int employees;
    public int employeesWithOutsideFriends;

    public EmployeeStats(int employees, int employeesWithOutsideFriends) {
        this.employees = employees;
        this.employeesWithOutsideFriends = employeesWithOutsideFriends;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EmployeeStats)) {
            return false;
        }
        EmployeeStats other = (EmployeeStats)o;
        return
                employees == other.employees &&
                        employeesWithOutsideFriends == other.employeesWithOutsideFriends;
    }

    @Override
    public int hashCode() {
        return employees ^ employeesWithOutsideFriends;
    }
}

class Helpers {

    static class Pair <T1, T2> {
        private T1 first;
        private T2 second;

        public Pair(T1 first, T2 second) {
            this.first = first;
            this.second = second;
        }

        public T1 getFirst() { return first; }
        public T2 getSecond() { return second; }
    }

    @SafeVarargs
    public static <K, V> Map<K, V> asMap(Pair<K, V>... args) {
        Map<K, V> result = new HashMap<>();
        for (Pair<K, V> entry : args) {
            result.put(entry.getFirst(), entry.getSecond());
        }
        return result;
    }

    public static <T1, T2> Pair<T1, T2> asPair(T1 first, T2 second) {
        return new Pair<>(first, second);
    }

}

// END DEFINITIONS


class Solution {

    public static Map<String, EmployeeStats> getEmployeeStats(List<String> employees, List<String> friendships) {
        // IMPLEMENTATION GOES HERE
        Map<String, EmployeeStats> friendshipMap = new HashMap<>();
        Map<String, String> empMap = new HashMap<String, String>();
        Set<String> empSet = new HashSet<>();
        Map<String, Integer> depatmentMap = new HashMap<String, Integer>();
        Map<String, Integer> outsideMap = new HashMap<String, Integer>();

            //count people per dep
            for (String employee : employees) {
                String[] strings1 = employee.split(",");
                String id = strings1[0];
                String dep = strings1[2];

                //count employees per department
                Integer numberOfEmployeesInDep = depatmentMap.get(dep);
                if (numberOfEmployeesInDep == null) {
                    depatmentMap.put(dep, 1);
                } else {
                    depatmentMap.put(dep, numberOfEmployeesInDep + 1);
                }
                //build id dep map;
                empMap.put(id, dep);
            }
        System.out.println(depatmentMap.toString());
                //check emp pairs
        for (String friendship : friendships) {
            String[] strings = friendship.split(",");
            Helpers.Pair<String, String> pair = new Helpers.Pair<>(strings[0], strings[1]);
//            Helper.Pair<?,?> pair = new Helper.Pair<>("a","a");

            //check if pair are in the same department
            String depFirst = empMap.get(pair.getFirst().toString());
            String depSec = empMap.get(pair.getSecond().toString());

            //if not - increase friendship for each dep
            if (!depFirst.equals(depSec)) {
                Integer outside1 = outsideMap.get(depFirst);
                if (outside1 == null) {
                    outsideMap.put(depFirst, 1);
                } else {
                    if (empSet.contains(pair.getFirst().toString())){
                        System.out.println("Emp was already counted");
                    } else {
                        outsideMap.put(depFirst, outside1 + 1);
                    }
                }

                Integer outside2 = outsideMap.get(depSec);
                if (outside2 == null) {
                    outsideMap.put(depSec, 1);
                } else {
                    if (empSet.contains(pair.getSecond().toString())){
                        System.out.println("Emp was already counted");
                    } else {
                        outsideMap.put(depSec, outside2 + 1);
                    }
                }
            }
            empSet.add(pair.getFirst().toString());
            empSet.add(pair.getSecond().toString());
        }
        System.out.println(outsideMap.toString());
            //build friendship map
            for (Map.Entry<String, Integer> entry : depatmentMap.entrySet()) {
                int friendsFromOutSide = 0;
                if (outsideMap.get(entry.getKey()) == null){
                    friendsFromOutSide = 0;
            } else {
                    friendsFromOutSide = (outsideMap.get(entry.getKey()));
                }
                friendshipMap.put(entry.getKey(), new EmployeeStats(entry.getValue(), friendsFromOutSide));
                System.out.println(entry.getKey()+ ": number Of employees: " + entry.getValue()+ " NumOfFriendsWithOutSide: " +friendsFromOutSide);
            }

        System.out.println(friendshipMap.toString());
        return friendshipMap;
    }

//    {
//        Engineering: {employees: 3, employees_with_outside_friends: 2}, //Carla does not have friends outside the department
//        HR: {employees: 1, employees_with_outside_friends: 1},
//        Business: {employees: 1, employees_with_outside_friends: 1},
//        Directors: {employees: 1, employees_with_outside_friends: 0} //Laurie does not have friends outside the department
//    }
    // START TEST CASES
    //
    // You can add test cases below. Each test case should be an instance of Test
    // constructed with:
    //
    // Test(String name, List<String> employees, List<String> friendships, Map<String, EmployeeStats> expectedOutput);
    //


    private static final List<Test> tests = Arrays.asList(
            new Test(
                    // name
                    "sample input",
                    // employees
                    Arrays.asList(
                            "1,Richard,Engineering",
                            "2,Erlich,HR",
                            "3,Monica,Business",
                            "4,Dinesh,Engineering",
                            "6,Carla,Engineering",
                            "9,Laurie,Directors"
                    ),
                    // friendships
                    Arrays.asList(
                            "1,2",
                            "1,3",
                            "1,6",
                            "2,4"
                    ),
                    // expected output
                    Helpers.asMap(
                            Helpers.asPair("Engineering", new EmployeeStats(3, 2)),
                            Helpers.asPair("HR", new EmployeeStats(1, 1)),
                            Helpers.asPair("Business", new EmployeeStats(1, 1)),
                            Helpers.asPair("Directors", new EmployeeStats(1, 0))
                    )
            )
    );


    // END TEST CASES
    // DO NOT MODIFY BELOW THIS LINE

    private static class Test {

        public String name;
        public List<String> employees;
        public List<String> friendships;
        public Map<String, EmployeeStats> expectedOutput;

        public Test(String name, List<String> employees, List<String> friendships, Map<String, EmployeeStats> expectedOutput) {
            this.name = name;
            this.employees = employees;
            this.friendships = friendships;
            this.expectedOutput = expectedOutput;
        }
    }

    private static boolean equalOutputs(Map<String, EmployeeStats> a, Map<String, EmployeeStats> b) {
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }

    public static void main(String[] args) {
        int passed = 0;
        for (Test test : tests) {
            try {
                System.out.printf("==> Testing %s...\n", test.name);
                Map<String, EmployeeStats> actualOutput = getEmployeeStats(test.employees, test.friendships);
                if (equalOutputs(actualOutput, test.expectedOutput)) {
                    System.out.println("PASS");
                    passed++;
                } else {
                    System.out.println("FAIL");
                }
            } catch (Exception e) {
                System.out.println("FAIL");
                System.out.println(e);
            }
        }
        System.out.printf("==> Passed %d of %d tests\n", passed, tests.size());
    }
}
