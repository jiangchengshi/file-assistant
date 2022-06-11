# 欢迎使用 File-Assistant

[![Maven](https://img.shields.io/badge/Maven-v1.0.0-blue)](https://search.maven.org/search?q=g:cool.doudou%20a:mqtt-assistant-*)
[![License](https://img.shields.io/badge/License-Apache%202-4EB1BA.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0)

## 简介

File助手 - 简化配置，注解带飞！

## 特点

> 配置灵活，基于GridFs||AliyunOSS||MiniIO包，没有改变任何框架结构，只为简化； 简单注解，即可实现文件操作

## 使用指引

### 引入依赖

```kotlin
implementation("cool.doudou:file-assistant:latest")
```

### 使用方式

> 上传文件大小，需要引入profiles：file

- 单个文件：10MB
- 总上传数据：50MB

```yaml
spring:
  profiles:
    include: file
```

> 文件存储方式

- local：存储在服务器本地
- gridFS：存储在MongoDB的GridFS文件模块
- aliYun：存储在阿里云OSS
- monIO：MonIO（开发中）

```yaml
file:
  storage-mode: local
```

### GridFS配置

> 依赖spring自动注入MongoClient、GridFSBucket，配置属性如下：

```yaml
file:
  grid-fs:
    server-uri: mongodb://admin:1234.abcd@127.0.0.1:27017
    database: files
    bucket: default
```

## 版权

[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)

## 鼓励一下，喝杯咖啡

> 欢迎提出宝贵意见，不断完善 File-Assistant

![鼓励一下，喝杯咖啡](https://user-images.githubusercontent.com/21210629/172556529-544b2581-ea34-4530-932b-148198b1b265.jpg)
