import java.util.ArrayList;
import java.util.HashMap;
import java.lang.*;
public class GameBoard {

    static ArrayList<ArrayList<ArrayList<Room>>> board = new ArrayList<>();

    static void init_game(int x_axis, int y_axis, int z_axis) {
        for (Integer i = 0; i < x_axis; i++) {
            board.add(new ArrayList<ArrayList<Room>>(y_axis)); //create list for each floor level
            for (Integer j = 0; j < y_axis; j++) {
                board.get(i).add(new ArrayList<Room>(z_axis)); //create list for each row
                if(i == 0 && j== 1) {
                    board.get(i).get(1).add(null); // adds a null index to board[0][1][0] to get to board[0][1][1] since room's are decided by indices of board (i.e board[1][0][1] = 1-0-1)
                    Integer[] start = {1,1,1}; //first room to move to
                    board.get(i).get(1).add(new Room(null, null, null, null, start, null)); //if floor is ground only create center room
                }
                else if (i != 0){
                    for (Integer h = 0; h < z_axis; h++) {
                        //adjacent rooms for center room
                        if(h == 1 && j == 1) {
                            Integer[] up = {i, 0, 1};
                            Integer[] down = {i, 2, 1};
                            Integer[] right = {i, 1, 2};
                            Integer[] left = {i, 1, 0};
                            Integer[] below = {i-1, 1, 1};
                            if (i == 1){
                                below = null; // makes it so board[0][1][1] can't be accessed again
                            }
                            if(i < 4) {Integer[] above = {i+1, 1, 1};
                                board.get(i).get(j).add(new Room(up, down, right, left, above, below));}
                            else {Integer[] above = null;
                                board.get(i).get(j).add(new Room(up, down, right, left, above, below));}

                        }
                        //adjacent rooms for right of center room
                        else if(h == 2 && j == 1) {
                            Integer[] up = {i, 0, 2};
                            Integer[] down = {i, 2, 2};
                            Integer[] right = null;
                            Integer[] left = {i, 1, 1};
                            Integer[] above = null;
                            Integer[] below = null;
                            board.get(i).get(j).add(new Room(up, down, right, left, above, below));
                        }
                        //adjacent rooms right diagonal of center
                        else if(h == 2 && j == 0) {
                            Integer[] up = null;
                            Integer[] down = {i, 1, 2};
                            Integer[] right = null;
                            Integer[] left = {i, 0, 1};
                            Integer[] above = null;
                            Integer[] below = null;
                            board.get(i).get(j).add(new Room(up, down, right, left, above, below));
                        }
                        //adjacent rooms for above center room
                        else if(h == 1 && j == 0) {
                            Integer[] up = null;
                            Integer[] down = {i, 1, 1};
                            Integer[] right = {i, 0, 2};
                            Integer[] left = {i, 0, 0};
                            Integer[] above = null;
                            Integer[] below = null;
                            board.get(i).get(j).add(new Room(up, down, right, left, above, below));
                        }
                        //adjacent rooms for left diagonal center room
                        else if(h == 0 && j == 0) {
                            Integer[] up = null;
                            Integer[] down = {i, 1, 0};
                            Integer[] right = {i, 0, 1};
                            Integer[] left = null;
                            Integer[] above = null;
                            Integer[] below = null;
                            board.get(i).get(j).add(new Room(up, down, right, left, above, below));
                        }
                        //adjacent rooms for left of center room
                        else if(h == 0 && j == 1) {
                            Integer[] up = {i, 0, 0};
                            Integer[] down = {i, 2, 0};
                            Integer[] right = {i, 1, 1};
                            Integer[] left = null;
                            Integer[] above = null;
                            Integer[] below = null;
                            board.get(i).get(j).add(new Room(up, down, right, left, above, below));
                        }
                        //adjacent rooms for bottom left diagonal center room
                        else if(h == 2 && j == 0) {
                            Integer[] up = {i, 1, 0};
                            Integer[] down = null;
                            Integer[] right = {i, 2, 1};
                            Integer[] left = null;
                            Integer[] above = null;
                            Integer[] below = null;
                            board.get(i).get(j).add(new Room(up, down, right, left, above, below));
                        }
                        //adjacent rooms for below center room
                        else if(h == 2 && j == 1) {
                            Integer[] up = {i, 1, 1};
                            Integer[] down = null;
                            Integer[] right = {i, 2, 2};
                            Integer[] left = {i, 2, 0};
                            Integer[] above = null;
                            Integer[] below = null;
                            board.get(i).get(j).add(new Room(up, down, right, left, above, below));
                        }
                        //adjacent rooms for bottom right diagonal center room
                        else if(h == 2 && j == 2) {
                            Integer[] up = {i, 1, 2};
                            Integer[] down = null;
                            Integer[] right = null;
                            Integer[] left = {i, 2, 1};
                            Integer[] above = null;
                            Integer[] below = null;
                            board.get(i).get(j).add(new Room(up, down, right, left, above, below));
                        }
                    }
                }
            }
        }
    }

    /**
    * @param currRoom - An integer array representing the current room. i.e {1, 1, 2} = 1-1-2 = board[1][1][2]
    * @return The Room object that currRoom was representing
    */
    Room getCurrentRoom(Integer[] currRoom){
        return board.get(currRoom[0]).get(currRoom[1]).get(currRoom[2]);
    }

    /**
    * @param currRoom - An integer array representing the current room. i.e {1, 1, 2} = 1-1-2 = board[1][1][2]
    * @return A HashMap containing all adjacent rooms to currRoom. Keys are direction and values are integer arrays representing the room's location
    */
    HashMap<String, Integer[]> getAdjacentRooms(Integer[] currRoom){
        return board.get(currRoom[0]).get(currRoom[1]).get(currRoom[2]).adjacent;
    }

    public static void main(String args[]) { //main method for the program
        init_game(5,3,3);
        System.out.println(board.get(1).get(1).get(0).adjacent.get("Down")[1]); //example will be deleted
    }
}