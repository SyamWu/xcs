package syamwu.xchushi.fw.arithmetic.loadbalanc.balance;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * 
 * 随机均衡器，根据权值比进行随机选目标
 * 
 * @author: syam_wu
 * @date: 2018
 */
public class RandomBalance<T> implements Balance<T> {
    
    /**
     * 默认精度100
     */
    private static int DEFAULT_SCALE_BASE = 100;

    @Override
    public int balance(int scaleBase, int[] loads) {
        if (loads == null || loads.length < 1) {
            return -1;
        }
        scaleBase = scaleBase < DEFAULT_SCALE_BASE ? DEFAULT_SCALE_BASE : scaleBase;
        int loadsSum = 0;
        for (int i = 0; i < loads.length; i++) {
            if (loads[i] < 0) {
                continue;
            }
            loadsSum = loadsSum + loads[i];
        }
        int[] loadsScales = new int[loads.length];
        for (int i = 0; i < loads.length; i++) {
            loadsScales[i] = BigDecimal.valueOf(loads[i]).divide(new BigDecimal(loadsSum), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(scaleBase)).intValue();
        }
        int randNum = new Random().nextInt(scaleBase) + 1;
        int chooseIndex = -1;
        for (int i = 0, chooseNum = 0; i < loadsScales.length; i++) {
            if (loadsScales[i] > 0) {
                chooseNum = chooseNum + loadsScales[i];
                chooseIndex = i;
                if (randNum <= chooseNum) {
                    break;
                }
            }
        }
        return chooseIndex;
    }

}
