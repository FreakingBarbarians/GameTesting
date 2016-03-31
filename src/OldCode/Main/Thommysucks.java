/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OldCode.Main;

/**
 *
 * @author 3101209
 */
public class Thommysucks {

    public static final float[][] data = {
        {-4, -0.182f},
        {-2, -0.056f},
        {0, 0.097f},
        {2, 0.238f},
        {4, 0.421f},
        {6, 0.479f},
        {8, 0.654f},
        {10, 0.792f},
        {12, 0.924f},
        {14, 1.035f},
        {15, 1.076f},
        {16, 1.103f},
        {17, 1.120f},
        {18, 1.121f},
        {19, 1.121f},
        {20, 1.099f},
        {21, 1.059f}
    };

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        float max = data[data.length][0];
        float min = data[0][0];
        float[][] slopes = new float[data.length - 1][2]; // Left heavy
        float[] offsets = new float[slopes.length];

        for (int i = 1; i < data.length; i++) {
            slopes[i][0] = data[i][0];
            slopes[i][1] = (data[i][1] - data[i - 1][1])
                    / (data[i][0] - data[i - 1][0]); // (y2-y1)/(x2-x1)
        }
        for (int i = 0; i < slopes.length; i++) {
            offsets[i] = data[i][1] - slopes[i][1] * data[i][0];
        }
    }

}
