package com.sharkgulf.soloera.tool;

/**
 * Created by user on 2019/11/1
 */
public class BleTool {

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src
     *            byte数组
     * @param offset
     *            从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] src, int offset) {
        if (src == null) {
            return -1;
        }else{
            int value;
            value = (int) ((src[offset] & 0xFF)
                    | ((src[offset+1] & 0xFF)<<8));
            return value;
        }

    }

    /**
     * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2（）配套使用
     */
    public static int bytesToInt2(byte[] src, int offset) {
        int value;
        value = (int) ( ((src[offset] & 0xFF)<<24)
                |((src[offset+1] & 0xFF)<<16)
        );
        return value;
    }
}
