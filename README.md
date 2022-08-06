# 欢迎使用 File-Assistant

[![Maven](https://img.shields.io/badge/Maven-v2.0.0-blue)](https://search.maven.org/search?q=g:cool.doudou%20a:file-assistant-*)
[![License](https://img.shields.io/badge/License-Apache%202-4EB1BA.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0)
![SpringBoot](https://img.shields.io/badge/SpringBoot-v2.7.2-blue)

## 简介

File助手 - 简化配置，注解带飞！

## 特点

> 配置灵活，基于GridFs||AliyunOSS||MiniIO包，没有改变任何框架结构，只为简化； 简单注解，即可实现文件操作

## 使用指引

### 引入依赖

```kotlin
implementation("cool.doudou:file-assistant-boot-starter:latest")
```

### 使用方式

> 上传文件大小，需要引入profiles：file

- 单个文件：50MB
- 总上传数据：100MB

```yaml
spring:
  profiles:
    include: file
```

> 文件存储方式

- local：存储在服务器本地
- gridFS：存储在MongoDB的GridFS文件模块
- aliYun：存储在阿里云OSS
- monIO：存储在MonIO

```yaml
file:
  storage-mode: local
```

### 属性配置

#### Local配置

```yaml
file:
  local:
    path: /home/assets/file
```

#### GridFS配置

> 依赖spring自动注入MongoClient、GridFSBucket，配置属性如下：

```yaml
file:
  grid-fs:
    server-uri: mongodb://admin:1234.abcd@127.0.0.1:27017
    database: files
    bucket-name: default
```

#### AliYun配置

> 依赖spring自动注入OSS，配置如下：

```yaml
file:
  ali-yun:
    endpoint: https://oss-cn-hangzhou.aliyuncs.com
    access-key-id: admin
    access-key-secret: 1234.abcd
    bucket-name: default
```

#### MinIO配置

```yaml
file:
  min-io:
    endpoint: http://127.0.0.1:9000
    access-key: admin
    secret-key: 1234.abcd
    bucket-name: default
```

### 方法说明

```java
public interface FileHelper {
    /**
     * 上传
     *
     * @param multipartFile 表单格式文件
     * @return FileResult
     */
    FileResult upload(MultipartFile multipartFile);

    /**
     * 上传
     *
     * @param file 文件
     * @return FileResult
     */
    FileResult upload(File file);

    /**
     * 上传
     *
     * @param multipartFile 表单格式文件
     * @param category      类别
     * @return FileResult
     */
    FileResult upload(MultipartFile multipartFile, String category);

    /**
     * 上传
     *
     * @param file     文件
     * @param category 类别
     * @return FileResult
     */
    FileResult upload(File file, String category);

    /**
     * 下载
     *
     * @param key      键值
     * @param response 请求响应
     */
    void download(String key, HttpServletResponse response);

    /**
     * 下载
     *
     * @param key      键值
     * @param category 类别
     * @param response 请求响应
     */
    void download(String key, String category, HttpServletResponse response);

    /**
     * 预览
     *
     * @param key      键值
     * @param response 请求响应
     */
    void preview(String key, HttpServletResponse response);

    /**
     * 预览
     *
     * @param key      键值
     * @param category 类别
     * @param response 请求响应
     */
    void preview(String key, String category, HttpServletResponse response);

    /**
     * 删除
     *
     * @param key 键值
     * @return true-成功；false-失败
     */
    boolean delete(String key);

    /**
     * 删除
     *
     * @param key      键值
     * @param category 类别
     * @return true-成功；false-失败
     */
    boolean delete(String key, String category);
}
```

## 版权

[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)

## 鼓励一下，喝杯咖啡

> 欢迎提出宝贵意见，不断完善 File-Assistant

![鼓励一下，喝杯咖啡](https://user-images.githubusercontent.com/21210629/172556529-544b2581-ea34-4530-932b-148198b1b265.jpg)
