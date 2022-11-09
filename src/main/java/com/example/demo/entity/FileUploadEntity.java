package com.example.demo.entity;

import lombok.Data;

import java.io.File;
import java.io.Serializable;

/**
 * @Description: 文件上传的文件信息实体类
 * @Author: walking
 * @Date: 2019年10月29日16:53:23
 */
@Data
public class FileUploadEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private File file;// 文件
    private String fileName;// 文件名
    private byte[] bytes;// 文件字节数组
    private int dataLength;// 数据长度

	//getter and setter
	//overwrite toString()
}
