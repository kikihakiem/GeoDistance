package complexity.eafit;

import java.util.List;


public interface NDimensionalSearch {

	void initialize(Element[] elements, int numCoords);
	
	List<Integer> search(Element objElement, float tolerance);
	
}
