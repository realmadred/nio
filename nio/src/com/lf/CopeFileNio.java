package com.lf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @auther Administrator
 * @date 2017/8/19
 * @description 描述
 */
public class CopeFileNio {

    public static void main(String[] args) {
        final String fileIn = "CopeFileNioIn.txt";
        final String fileOut = "CopeFileNioOut.txt";
        copyTransfer(fileIn,fileOut);
    }

    public static void copy(final String src,final String det){
        try (FileChannel inChannel = new FileInputStream(src).getChannel();
             FileChannel outChannel = new FileOutputStream(det).getChannel()) {
            // 创建buff
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            while (true) {
                // 需要清理buffer
                buffer.clear();
                if (inChannel.read(buffer) <= 0) break;
                // 压缩准备读取
                buffer.flip();
                outChannel.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 可以实现零拷贝
     * @param src
     * @param det
     */
    public static void copyTransfer(final String src,final String det){
        try (FileChannel inChannel = new FileInputStream(src).getChannel();
             FileChannel outChannel = new FileOutputStream(det).getChannel()) {
            inChannel.transferTo(0,inChannel.size(),outChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
