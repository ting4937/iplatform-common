package cn.hollycloud.iplatform.common.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Cloud on 2017/5/27.
 */
public class RandomUtils {
    private static SecureRandom secureRandom;

    static {
        try {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    //产生6位数验证码
    public static String generateValidCode() {
        //为了安全使用SecureRandom
        int code = 100000 + secureRandom.nextInt(1000000 - 100000);
        return code + "";
    }

    /**
     * 返回最大为[0-endExclusive)的随机数
     *使用ThreadLocalRandom在多线程下性能更高
     * @param endExclusive
     * @return
     */
    public static int nextInt(int endExclusive) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int code = random.nextInt(0, endExclusive);
        return code;
    }
}
