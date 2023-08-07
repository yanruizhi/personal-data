package com.superme.filemanager.controller;

import com.superme.common.beans.Result;
import com.superme.filemanager.service.FileService;
import com.superme.filemanager.utils.FileUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * FileController
 * 作者: yanruizhi
 * 时间: 2023/7/31 9:42
 */
@RestController
@RequestMapping("file")
public class FileController {
    @Resource
    private FileService fileService;

    /**
     * 单文件上传
     */
    @PostMapping("upload/single")
    public Result<Object> uploadSingle(MultipartFile file, String description) {
        return fileService.uploadSingle(file, description);
    }

    /**
     * 多文件上传
     */
    @PostMapping("upload/multi")
    public Result<Object> uploadMulti(List<MultipartFile> files, String description) {
        return fileService.uploadMulti(files, description);
    }

    /**
     * Java普通方式--上传文件
     *
     * @param request
     * @param name
     * @param file3
     * @param photo
     * @return
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping(value = "/upload/up", method = RequestMethod.POST)
    @ResponseBody
    public String readFile(HttpServletRequest request, @RequestParam("name") String name, @RequestPart("file1") MultipartFile file3, @RequestPart("photo") MultipartFile photo) throws IOException, ServletException {

        File directory = new File(""); //实例化一个File对象。参数不同时，获取的最终结果也不同

        String canonicalPath = directory.getCanonicalPath();//获取标准路径。该方法需要放置在try/catch块中，或声明抛出异常

        directory.getAbsolutePath(); //获取绝对路径。

        String path = canonicalPath;
        //        String path = "I:\\spring\\spring-mybatis-plus\\src\\main\\resources\\public\\static\\";

        System.out.println(name);
        //        第一种 ： 使用 MultipartFile 封装好的 transferTo() 方法保存文件
        photo.transferTo(new File(path + photo.getOriginalFilename()));
        //
        //        第二种 ：  使用 MultipartFile 字节流保存文件
        FileUtil.save(file3, String.valueOf(path));
        //
        //		第三种 ：  使用 Part 接收文件字节流
        Part file2 = request.getPart("file2");
        file2.write(path + file2.getSubmittedFileName());

        // request.getParts() 获取的是全部参数（name,age,file1,file2），包括文件参数和非文件参数
        for (Part part : request.getParts()) {
            // 获取文件类型
            part.getContentType();
            // 获取文件大小
            part.getSize();
            // 获取文件名
            part.getSubmittedFileName();
            // 获取参数名 （name,age,file1,file2）
            part.getName();


            if (part.getContentType() != null) {
                part.write(path + part.getSubmittedFileName());
            } else {
                // 可以获取文本参数值,文本参数 part.getContentType() 为 null
                request.getParameter(part.getName());
            }
        }
        return "success";
    }

    @PostMapping("file")
    public Result<Object> uploadFile(MultipartFile file) throws IOException {
        String path = this.getClass().getClassLoader().getResource("").getPath();

        File directory = new File(""); //实例化一个File对象。参数不同时，获取的最终结果也不同

        String canonicalPath = directory.getCanonicalPath();//获取标准路径。该方法需要放置在try/catch块中，或声明抛出异常

        byte[] bytes = file.getBytes();
        String fileName = canonicalPath + file.getOriginalFilename();
        System.out.println("fileName = " + fileName);
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
        outputStream.write(bytes);
        outputStream.close();
        return Result.OK("文件上传成功");
    }


    @GetMapping("getDir")
    public Result<Object> getDir() {
        String path1 = this.getClass().getClassLoader().getResource("").getPath();
        String path2 = System.getProperty("user.dir");//jar包所在目录
        System.out.println(path1);
        System.out.println(path2);
        HashMap<String, Object> pathMap = new HashMap<>();
        pathMap.put("path1", path1);
        pathMap.put("path2", path2);
        return Result.OK(pathMap);
    }

    @PostMapping("upload-a")
    public Result<Object> upload(MultipartFile file) {
        return Result.OK();
    }
}
