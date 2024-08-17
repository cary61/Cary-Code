package top.cary61.carycode.oss.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.cary61.carycode.commons.entity.Result;
import top.cary61.carycode.oss.entity.vo.ImageInfoVO;
import top.cary61.carycode.oss.util.Utils;

import java.io.File;
import java.io.IOException;

@Controller
public class ImageController {

    private final String imageBaseDirectory;

    public ImageController(@Value("${oss.base-directory}") String baseDirectory,
                           @Value("${oss.image-prefix}") String imagePrefix) {
        this.imageBaseDirectory = baseDirectory + imagePrefix;
    }

    @GetMapping(value = "/image/{path}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> getImage(@PathVariable String path) {
        File file = getLocalFile(path);
        if (!file.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new FileSystemResource(file), HttpStatus.OK);
    }

    // TO-DO unit test
    @PostMapping("/image")
    @ResponseBody
    public Result uploadImage(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();
        if (!contentType.startsWith("image/")) {
            return Result.fail("文件不是图片");
        }
        String randomFileName = Utils.hash();
        String fileName = randomFileName + multipartFile.getOriginalFilename();
        File file = getLocalFile(fileName);
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            return Result.fail("服务器错误，无法存储图片");
        }
        return Result.ok(
                ImageInfoVO.builder().path(fileName)
        );
    }

    private File getLocalFile(String path) {
        return new File(imageBaseDirectory + path);
    }
}
