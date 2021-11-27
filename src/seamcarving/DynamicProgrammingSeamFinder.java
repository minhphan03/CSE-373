package seamcarving;

import java.util.ArrayList;
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
        double[][] energyTable = new double[picture.width()][picture.height()];


        // compute the energy of every single pixel
        for (int x = 0; x < picture.width(); x+=1) {
            for (int y = 0; y < picture.height(); y+=1) {
                energyTable[x][y] = f.apply(picture, x, y);

            }
        }
        double[][] seamDynamic = new double[picture.width()][picture.height()];
        /**
         * backtracker is used to track the possible paths for later tracking:
         *  -1: first column have no previous pixels to trace
         *  0: lower left of column y-1
         *  1: left of current column y
         *  2: upper left of column y+1
         */
        int[][] backtrace = new int[picture.width()][picture.height()];

        List<Integer> seam = new ArrayList<>();
        double min;

        // mark backtracking
        for (int x = 0; x < picture.width(); x+=1) {
            for (int y = 0; y < picture.height(); y+=1) {
                if (x == 0) {
                    seamDynamic[x][y] = energyTable[x][y];
                    backtrace[x][y] = -1;
                } else {
                    // for the middle columns: use the min algorithm to trace the least expensive route

                    //at the bottom (y=0)
                    if (y == 0) {
                        min = Math.min(seamDynamic[x-1][y], seamDynamic[x-1][y+1]);
                        // if min is the direct left one
                        if (min == seamDynamic[x-1][y]) {
                            backtrace[x][y] = 1;
                        } else {
                            backtrace[x][y] = 2;
                        }
                    }
                    //if it is at the top
                    else if (y == picture.height()-1) {
                        min = Math.min(seamDynamic[x - 1][y], seamDynamic[x - 1][y - 1]);
                        if (min == seamDynamic[x - 1][y]) {
                            backtrace[x][y] = 1;
                        } else {
                            backtrace[x][y] = 0;
                        }
                    }
                    //otherwise, check all three previous pixels
                    else {
                        min = Math.min(seamDynamic[x - 1][y], Math.min(seamDynamic[x - 1][y - 1], seamDynamic[x-1][y+1]));
                        if (min == seamDynamic[x - 1][y]) {
                            backtrace[x][y] = 1;
                        } else if (min == seamDynamic[x - 1][y-1]) {
                            backtrace[x][y] = 0;
                        } else {
                            backtrace[x][y] = 2;
                        }
                    }
                    seamDynamic[x][y] = energyTable[x][y]+ min;
                }

            }
        }

        //back-trace, starting at the end

        //find min seam value
        double min_num = seamDynamic[picture.width()-1][0];
        int min_idx = 0;
        for (int y = 0; y < picture.height(); y+=1) {
            if (min_num > seamDynamic[picture.width() - 1][y]) {
                min_idx = y;
                min_num = seamDynamic[picture.width() - 1][y];
            }
        }

        seam.add(min_idx);
        int x = picture.width()-1;
        int y = min_idx;

        int backtrack;
        while (x > 0) {
            backtrack = backtrace[x][y];
            if (backtrack==0) {
                y = y-1;
            } else if (backtrack== 2) {
                y = y+1;
            }
            // add from the left
            seam.add(0,y);
        }
        // first column: add itself
        seam.add(0,y);


        return seam;
        //throw new UnsupportedOperationException("Not implemented yet");
    }
}