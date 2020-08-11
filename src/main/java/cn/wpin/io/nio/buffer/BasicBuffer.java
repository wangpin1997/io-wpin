package cn.wpin.io.nio.buffer;

import java.nio.IntBuffer;

/**
 * NIO buffer 组件测试
 * @author wangpin
 */
public class BasicBuffer {

    public static void main(String[] args) {
        IntBuffer intBuffer=IntBuffer.allocate(5);
        for (int i = 0; i <intBuffer.capacity() ; i++) {
            intBuffer.put(i);
        }
        //capacity 容量，初始化的时候传入
        //  limit = position;   最大值（容量大小）
        //  position = 0;       游标（每次put就会改变，小于等于 limit）
        //  mark = -1;
        intBuffer.flip();


        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
