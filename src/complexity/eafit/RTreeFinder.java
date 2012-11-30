package complexity.eafit;

import java.util.List;

import com.newbrightidea.util.RTree;

/**
 * Offers the search service on a RTree data structure. This implementations is
 * supported on the external library {@link https://github.com/rweeks/util}.
 * <br/><br/>
 * Additional resources: {@link http://www-db.deis.unibo.it/courses/SI-LS/papers/Gut84.pdf}
 * 
 * @author Camilo Vieira
 * @author Juan Diego Restrepo
 * 
 */
public class RTreeFinder extends RTree<Integer> implements NDimensionalSearch {

	private Element[] elements;
	private RTree<Integer> rtree;

	@Override
	public void initialize(Element[] elements, int numCoords) {
		this.elements = elements;

		rtree = new RTree<Integer>(elements.length, elements.length, numCoords);
		for (Element elementIt : elements) {
			rtree.insert(elementIt.getCoords(), (elementIt.getId()));
		}
	}

	@Override
	public List<Integer> search(Element pointToSearch, float tolerance) {

		float[] coords = pointToSearch.getCoords();
		float[] dims = new float[coords.length];

		for (int i = 0; i < coords.length; i++) {
			coords[i] -= tolerance;
			dims[i] = tolerance * 2;
		}

		return rtree.search(coords, dims);
	}

}
