package OldCode.Main.Testing;

import OldCode.Main.Object.Rendering.Animation;
import OldCode.Main.Object.Rendering.Animator;
import OldCode.Main.Object.Rendering.AnimatorException;
import org.junit.Assert;

/**
 *
 * @author Raaaaaaay
 */
public class AnimatorTest {

    /**
     * @param args arguments
     */
    public static void main(String[] args) {
        new AnimatorTest();
    }
    private Animation animation1;
    private Animation animation2;
    private Animation animation3;
    private Animator animator1;

    public AnimatorTest() {
        this.preInit();
        this.test();
    }

    public void preInit() {
        //[1]  [2]  [3]  [4]
        //[200][300][100][10]
        int[][] animArray1 = new int[][]{
            {1, 2, 3, 4},
            {200, 300, 100, 10}};

        //[5]  [6]  [7]  [8]
        //[10] [0]  [0]  [10]
        int[][] animArray2 = new int[][]{
            {5, 6, 7, 8},
            {10, 0, 0, 10}
        };

        //[9]  [10]  [11] [12]
        //[10] [100] [10] [999999] 
        int[][] animArray3 = new int[][]{
            {9, 10, 11, 12},
            {10, 100, 10, 999999}
        };

        animation1 = new Animation("looping", animArray1, true);
        animation2 = new Animation("not looping", animArray2, false);
        animation3 = new Animation("not looping", animArray3, false);

        animator1 = new Animator();

        try {
            animator1.addAnimation(animation1, "idle");
            animator1.addAnimation(animation2, "1");
            animator1.addAnimation(animation3, "2");
        } catch (AnimatorException e) {
            System.out.println(e.getMessage());
        }
    }

    public void test() {
        try {
//            Assert.assertEquals(1, 2);
            animator1.playAnimation("1", true);
            Assert.assertEquals(5, animator1.getFrame());
            Assert.assertEquals(8, animator1.animate(20));
            animator1.playAnimation("idle", false);
            Assert.assertEquals(1, animator1.getFrame());
            Assert.assertEquals(1, animator1.animate(0.1f));
            Assert.assertEquals(2, animator1.animate(199.9f));
            animator1.playAnimation("2", false);
            Assert.assertEquals(9, animator1.getFrame());
            Assert.assertEquals(10, animator1.animate(10f));
            try {
                animator1.playAnimation("1", false);
            } catch (AnimatorException e) {
                e.appendToErrorMessage("This is expected");
                System.out.println(e.getMessage());
            }
            Assert.assertEquals(10, animator1.getFrame());
        } catch (AnimatorException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Test Success!");
    }
}
