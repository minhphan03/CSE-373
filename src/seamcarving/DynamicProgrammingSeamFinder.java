package seamcarving;

import java.util.List;

/**
 * Dynamic programming implementation of the {@link SeamFinder} interface.
 *
 * @see SeamFinder
 * @see SeamCarver
 */
public class DynamicProgrammingSeamFinder implements SeamFinder {

    @Override
    public List<Integer> findSeam(Picture picture, EnergyFunction f) {
        double[][] arr = new double[picture.width()][picture.height()];

        // compute the energy of the first row
        for (int y = 0; y < picture.height(); i+=1) {
            arr[0][y] = f.apply(picture, 0, y);
        }

        //recursion ig?


        throw new UnsupportedOperationException("Not implemented yet");
    }
}
