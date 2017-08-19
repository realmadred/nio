package com.lf;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @auther Administrator
 * @date 2017/8/19
 * @description 描述
 */
public class SelectorNio {

    private int ports[];

    public SelectorNio(int ports[]) throws IOException {
        this.ports = ports;
        start();
    }

    private void start() throws IOException {
        // 1. 创建一个selector
        Selector selector = Selector.open();
        // 为每个端口打开一个监听, 并把这些监听注册到selector中
        for (int i = 0; i < ports.length; ++i) {
            //2. 打开一个ServerSocketChannel
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);//设置为非阻塞
            ssc.socket().bind(new InetSocketAddress(ports[i]));
            //3. 注册到selector server断感兴趣的是接受连接
            ssc.register(selector, SelectionKey.OP_ACCEPT);
        }
        //4. 开始循环，我们已经注册了一些IO兴趣事件
        while (true) {
            // select() 方法将返回所发生的事件的数量。
            int num = selector.select();
            if (num <= 0) continue;
            //返回发生了事件的 SelectionKey 对象的一个 集合
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            //我们通过迭代 SelectionKeys 并依次处理每个 SelectionKey 来处理事件
            Iterator<SelectionKey> iterator = selectedKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                //5. 监听新连接。程序执行到这里，我们仅注册了 ServerSocketChannel
                if (key.isAcceptable()) {
                    //6. 接收了一个新连接。因为我们知道这个服务器套接字上有一个传入连接在等待
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    // 7. 讲新连接注册到selector。将新连接的 SocketChannel 配置为非阻塞的
                    //而且由于接受这个连接的目的是为了读取来自套接字的数据，所以我们还必须将 SocketChannel 注册到 Selector上
                    sc.register(selector, SelectionKey.OP_READ);
                    // 这里需要移除，已经正常处理的key
                    iterator.remove();
                } else if (key.isReadable()) {
                    // Read the data
                    SocketChannel sc = (SocketChannel) key.channel();
                    // Echo data
                    int bytesEchoed = 0;
                    ByteBuffer echoBuffer = ByteBuffer.allocate(1024);
                    while (true) {
                        echoBuffer.clear();
                        if (sc.read(echoBuffer) <= 0) break;
                        echoBuffer.flip();
                        sc.write(echoBuffer);
                    }
                    System.out.println("Echoed " + bytesEchoed + " from " + sc);
                    // 删除
                    iterator.remove();
                }
            }
            // System.out.println( "going to clear" );
            // selectedKeys.clear();
            // System.out.println( "cleared" );
        }
    }

    static public void main(String args2[]) throws Exception {
        String args[] = {"9001", "9002", "9003"};
        if (args.length <= 0) {
            System.err.println("Usage: java MultiPortEcho port [port port ...]");
            System.exit(1);
        }
        int ports[] = new int[args.length];
        for (int i = 0; i < args.length; ++i) {
            ports[i] = Integer.parseInt(args[i]);
        }
        new SelectorNio(ports);
    }
}
