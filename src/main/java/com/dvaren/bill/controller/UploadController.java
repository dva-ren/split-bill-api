package com.dvaren.bill.controller;

import com.dvaren.bill.domain.entity.UploadConfig;
import com.dvaren.bill.utils.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@RestController
public class UploadController {
    @Resource
    private UploadConfig uploadConfig;

    @PostMapping("/upload")
    public ResponseResult upload(MultipartFile file) {
        //图片校验（图片是否为空,图片大小，上传的是不是图片、图片类型（例如只能上传png）等等）
        if (file == null || file.isEmpty()) {
            return ResponseResult.failed("图片为空");
        }
        if (file.getSize() > uploadConfig.getMaxSize() * 1000){
            return ResponseResult.failed("图片应小于500KB");
        }
        //可以自己加一点校验 例如上传的是不是图片或者上传的文件是不是png格式等等 这里省略
        //TODO: 图片格式验证
        //获取原来的文件名和后缀
        String originalFilename = file.getOriginalFilename();
//        String ext = "." + FilenameUtils.getExtension(orgFileName); --需要导依赖
        assert originalFilename != null;
        String ext = "."+ originalFilename.split("\\.")[1];
        //生成一个新的文件名（以防有重复的名字存在导致被覆盖）
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String newName = uuid + ext;
        String filePath = uploadConfig.getFilePath();
        //拼接图片上传的路径 url+图片名
        String pre = filePath.endsWith("/")? filePath: filePath + "/";
        String path = pre + newName;
        try {
            file.transferTo(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap<String, String> res = new HashMap<>();
        String domain = uploadConfig.getDomain();
        domain = domain.endsWith("/")? domain: domain + "/";
        res.put("path",path);
        res.put("url",domain + newName);
        res.put("name",newName);
        return ResponseResult.ok(res);
    }
}
