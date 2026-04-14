package com.rise.util;


import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageToBase64Util {

    /***  本地文件(图片、excel等)转换成Base64字符串 */
    /*public static String convertFileToBase64(String imgPath) {
        //读取图片字节数组
        byte[] data = null;
        try {
            InputStream in = new FileInputStream(imgPath);
            System.out.println("文件大小(字节)=" + in.available());
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对字节数组进行Base64编码，得到Base64编码的字符串
        return new String(Objects.requireNonNull(Base64.encodeBase64(data)));
    }

    *//*** 将base64字符串，生成文件 *//*
    public static File convertBase64ToFile(String fileBase64String, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            //判断文件目录是否存在
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            byte[] bfile = Base64.decodeBase64(fileBase64String);
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    *//*** MultipartFile转成InputStream 将图片转换成Base64编码 *//*
    public static String imgToBase64(MultipartFile uploadFiles) {
        InputStream in;
        byte[] data = null;
        //读取图片字节数组
        try {
            byte[] byteArr = uploadFiles.getBytes();
            in = new ByteArrayInputStream(byteArr);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e1) {
            e1.getMessage();
            e1.printStackTrace();
        }
        System.out.println(data);
        return new String(Objects.requireNonNull(Base64.encodeBase64(data)));
    }
*/
    private static byte[] convertToByteArray(MultipartFile file) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            return bytes;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }


    public static String updateUserAvatar(MultipartFile file) {
        //将file文件转为Base64
        byte[] bytes = new byte[0];
        try {
            bytes = convertToByteArray(file);
        } catch (IOException e) {
            return "";
        }
        String avatar =  Base64.getEncoder().encodeToString(bytes);

        return avatar;
    }

    public static String imgToText(File file){
        Map<String, Object> arguments = new HashMap<>();
        String exePath = "C:/PaddleOCR-json_v.1.3.1/PaddleOCR-json.exe";
        String ocrResult = "";

        try (Ocr ocr = new Ocr(new File(exePath), arguments)) {

            //String imgPath = "C:/Users/13798/Desktop/ocr测试.png";
            OcrResponse resp = ocr.runOcr(file);
            // 读取结果
            if (resp.code == OcrCode.OK) {
                for (OcrEntry entry : resp.data) {
                    ocrResult=ocrResult+entry.text;

                }
                return ocrResult;
            } else {
                return "error: code=" + resp.code + " msg=" + resp.msg;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ocrResult;
    }


    public static void main(String[] args) {
        // 可选的配置项
        Map<String, Object> arguments = new HashMap<>();
        // arguments.put("use_angle_cls", true);

        // 初始化 OCR：使用本地进程或者套接字服务器
        // 本地进程: new Ocr(new File(exePath), arguments)
        String exePath = "C:/Users/13798/Desktop/ocr/PaddleOCR-json_v.1.3.1/PaddleOCR-json.exe"; // paddleocr_json 的可执行文件所在路径
        try (Ocr ocr = new Ocr(new File(exePath), arguments)) {
//        使用套接字服务器（仅作为客户端，不启动服务）
//        try (Ocr ocr = new Ocr(serverAddr, serverPort, arguments)) {



            // 对一张图片进行 OCR（使用路径）
            String imgPath = "C:/Users/13798/Desktop/ocr测试.png";
           // OcrResponse resp = ocr.runOcr(new File(imgPath));

            // 或者使用图片数据（二进制或 base64）
           // byte[] fileBytes = Files.readAllBytes(Paths.get(""));
           //OcrResponse resp = ocr.runOcrOnImgBytes(fileBytes);
            OcrResponse resp = ocr.runOcrOnImgBase64("iVBORw0KGgoAAAANSUhEUgAAAHMAAAAeCAIAAACQS7Y1AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAEnQAABJ0Ad5mH3gAAAWDSURBVGhD7Zjfa6NKFMfvXxsKUghBKAklLkhA8pAFKUi5EjYszUIt3BZqoRbWZclSzEOgCPHCRViEWoqlIBchCCF3fhx");

            // 或者直接识别剪贴板中的图片
//             OcrResponse resp = ocr.runOcrOnClipboard();
            String ocrResult = "";
            // 读取结果
            if (resp.code == OcrCode.OK) {
                for (OcrEntry entry : resp.data) {
                    ocrResult=ocrResult+entry.text;

                }
                System.out.println("ocr识别结果:"+ocrResult);
            } else {
                System.out.println("error: code=" + resp.code + " msg=" + resp.msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static File transferToFile(MultipartFile multipartFile) {
//        选择用缓冲区来实现这个转换即使用java 创建的临时文件 使用 MultipartFile.transferto()方法 。
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String[] filename = originalFilename.split("\\.");
            file=File.createTempFile(filename[0], filename[1] + ".");
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

}
