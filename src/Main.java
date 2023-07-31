import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        File[] inputFloorPlans = new File("./src").listFiles();
        for(File f : inputFloorPlans){
            if(f.getName().contains(".txt")) {
                System.out.println("FloorPlan:- "+f.getName());
                Solution.solve(f);
            }
        }
    }
}


class Solution {

    public static final String ANSI_RESET = "\u001B[0m";
    private String[] colors = {"\u001B[41m", "\u001B[42m", "\u001B[43m", "\u001B[44m", "\u001B[45m", "\u001B[46m", "\u001B[47m"};
    private Map<Integer, String> colorTracker = new HashMap<>();

    private String getRoomColor(int index) {
        if (index >= colors.length) {
            return colors[index % colors.length];
        } else {
            return colors[index];
        }
    }

    public List<List<Character>> readFloorPlan(File file) {
        List<List<Character>> floorPlan = new ArrayList<>(); //Use list to dynamically allocate the memory instead of Array.
        try (FileReader fr = new FileReader(file)) {
            int content;
            List<Character> wall = new ArrayList<>();
            while ((content = fr.read()) != -1) {
                if (content == 13) {
                } else if (content == 10) {
                    floorPlan.add(new ArrayList<>(wall));
                    wall.clear();
                } else {
                    wall.add((char) content);
                }
            }
            floorPlan.add(new ArrayList<>(wall));
            wall.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return floorPlan;
    }

    public void colorFloors(List<List<Character>> plan) {
        String roomColor = null;
        int colorIndex = 0;
        for (int i = 0; i < plan.size(); i++) {
            for (int j = 0; j < plan.get(i).size(); j++) {
                if (plan.get(i).get(j).charValue() == ' ') { //Room space
                    if (plan.get(i).get(j - 1).charValue() == '#' && plan.get(i).get(j + 1).charValue() == '#') { //Check for room entrance
                        System.out.print(plan.get(i).get(j));
                        roomColor = null;
                    } else if (plan.get(i - 1).get(j).charValue() == '#' && plan.get(i + 1).get(j).charValue() == '#') { //Check for room entrance
                        System.out.print(plan.get(i).get(j));
                        roomColor = null;
                    } else {
                        if ((plan.get(i).get(j - 1).charValue() == '#' && plan.get(i - 1).get(j).charValue() == '#') ||
                                (plan.get(i+1).get(j - 1).charValue() == '#' && plan.get(i - 1).get(j).charValue() == '#')) { //Check for beginning of room
                            /**
                             * Assumption :- Anytime # is there before and above/below the space it is considered as beginning of new room.
                             */
                            colorTracker.put(j, getRoomColor(colorIndex)); // Room color is tracked to reuse in the subsequent iteration
                            colorIndex++;
                        }
                        if (roomColor == null) {
                            roomColor = colorTracker.get(j);
                        }
                        System.out.print(roomColor + plan.get(i).get(j) + ANSI_RESET);
                    }
                } else {
                    System.out.print(plan.get(i).get(j));
                    roomColor = null;
                }
            }
            System.out.println();
        }
    }

    public static void solve(File floorPlan) {
        Solution solution = new Solution();
        solution.colorFloors(solution.readFloorPlan(floorPlan));
    }
}