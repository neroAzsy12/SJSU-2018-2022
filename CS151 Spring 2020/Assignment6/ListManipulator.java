/**
 * @author azsy
 *
 */
import java.util.*;
public class ListManipulator {
	
	public ArrayList<Integer> sort(ArrayList<Integer> myLst, boolean ascending){
		if(ascending) {
			Collections.sort(myLst);
		}else {
			Collections.sort(myLst, new Comparator<Integer>() {
				public int compare(Integer a, Integer b) {
					return b - a;
				}
			});
		}
		return myLst;
	}
	
	public ArrayList<Integer> swapLargestSmallest(ArrayList<Integer> myLst){
		int min = myLst.get(0);
		int min_index = 0;
		int max = myLst.get(0);
		int max_index = 0;
		
		for(int i = 1; i < myLst.size(); i++) {
			if(myLst.get(i) > max) {
				max = myLst.get(i);
				max_index = i;
			}
			if(myLst.get(i) < min) {
				min = myLst.get(i);
				min_index = i;
			}
		}
		myLst.set(min_index, max);
		myLst.set(max_index, min);
		return myLst;
	}
	
	public void table(ArrayList<Integer> myLst) {
		HashMap<Integer, Integer> nums = new HashMap<>();
		for(Integer tmp : myLst) {
			if(nums.containsKey(tmp)) {
				nums.put(tmp, nums.get(tmp) + 1);
			}else {
				nums.put(tmp, 1);
			}
		}
		
		ArrayList<Map.Entry<Integer, Integer>> temp = new ArrayList<Map.Entry<Integer, Integer>>(nums.entrySet());
		Collections.sort(temp,  new Comparator<Map.Entry<Integer, Integer>>(){
			public int compare(Map.Entry<Integer, Integer> a, Map.Entry<Integer, Integer> b) {
				return a.getKey() - b.getKey();
			}
		});
		
		for(Map.Entry<Integer, Integer> tmp : temp) {
			System.out.println("Number: " + tmp.getKey() + ", Appears: " + tmp.getValue() + " time(s)");
		}
	}
}