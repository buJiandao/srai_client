package com.example.demo.server;

import com.example.demo.entity.FileUploadEntity;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

/**
 * @Description: 文件上传demo 客户端
 * @Author: walking
 * @Date: 2019年10月29日16:50:25
 */
public class FileUploadClient {

    private static String file_name="C:\\Users\\30417\\Desktop\\b.jpg";

    public void connect(int port, String host, final FileUploadEntity fileUploadEntity ) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)));
                            ch.pipeline().addLast(new FileUploadClientHandler(fileUploadEntity));
                        }
                    });
            ChannelFuture f = b.connect(host, port).sync();
            System.out.println("客户端启动...");
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = 8000;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        try {
            //构建上传文件对象
            FileUploadEntity uploadFile = new FileUploadEntity();
            File file = new File(file_name);

            String fileName = file.getName();// 文件名
            uploadFile.setFile(file);
            uploadFile.setFileName(fileName);

            //连接到服务器 并上传
            new FileUploadClient().connect(port, "192.168.0.107", uploadFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
