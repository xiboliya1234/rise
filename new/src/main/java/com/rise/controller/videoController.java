package com.rise.controller;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageInfo;
import com.rise.common.Result;
import com.rise.exception.CustomException;
import com.rise.model.videoinfoModel;
import com.rise.service.videoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/vue/video")
public class videoController {

    @Autowired
    private videoService vs;
    @Value("${video.player.enable-local-player:true}")
    private boolean enableLocalPlayer;
    @Value("${video.player.potplayer-path:C:/Program Files/DAUM/PotPlayer/PotPlayerMini64.exe}")
    private String potPlayerPath;

    @GetMapping("/page")
    public Result page(videoinfoModel params) {
        PageInfo<videoinfoModel> pageInfo = vs.getDataBysbmc(params);
        return Result.success(pageInfo);
    }

    @GetMapping("/prefix/list")
    public Result listPrefix() {
        return Result.success(vs.listEnabledPrefixes());
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        videoinfoModel model = vs.findById(id);
        if (model == null) {
            throw new CustomException("视频不存在");
        }
        return Result.success(model);
    }

    @GetMapping("/play/{id}")
    public ResponseEntity<Resource> play(@PathVariable Integer id) throws IOException {
        videoinfoModel model = vs.findById(id);
        if (model == null) {
            throw new CustomException("视频不存在");
        }
        if (StrUtil.isBlank(model.getVideourl())) {
            throw new CustomException("视频地址为空");
        }

        String source = model.getVideourl().trim();

        if (source.startsWith("http://") || source.startsWith("https://")) {
            return ResponseEntity.status(302).header(HttpHeaders.LOCATION, source).build();
        }

        Path target = resolveLocalPath(source);
        if (!Files.exists(target) || !Files.isRegularFile(target)) {
            throw new CustomException("视频文件不存在：" + target);
        }

        Resource resource = toResource(target);
        String contentType = Files.probeContentType(target);
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @PostMapping("/open-potplayer/{id}")
    public Result openPotPlayer(@PathVariable Integer id) {
        if (!enableLocalPlayer) {
            throw new CustomException("本地播放器功能已禁用");
        }
        videoinfoModel model = vs.findById(id);
        if (model == null) {
            throw new CustomException("视频不存在");
        }
        if (StrUtil.isBlank(model.getVideourl())) {
            throw new CustomException("视频地址为空");
        }
        if (StrUtil.isBlank(potPlayerPath)) {
            throw new CustomException("未配置PotPlayer路径");
        }

        Path playerExecutable = Paths.get(potPlayerPath).toAbsolutePath().normalize();
        if (!Files.exists(playerExecutable) || !Files.isRegularFile(playerExecutable)) {
            throw new CustomException("PotPlayer不存在: " + playerExecutable);
        }

        String source = model.getVideourl().trim();
        String playTarget = source;
        if (!source.startsWith("http://") && !source.startsWith("https://")) {
            Path target = resolveLocalPath(source);
            if (!Files.exists(target) || !Files.isRegularFile(target)) {
                throw new CustomException("视频文件不存在: " + target);
            }
            playTarget = target.toString();
        }

        List<String> command = new ArrayList<>();
        command.add(playerExecutable.toString());
        command.add(playTarget);

        try {
            new ProcessBuilder(command).start();
        } catch (IOException e) {
            throw new CustomException("调用PotPlayer失败: " + e.getMessage());
        }
        return Result.success("已调用PotPlayer播放");
    }

    @PostMapping
    public Result create(@RequestBody videoinfoModel params) {
        validatePayload(params, false);
        vs.savevideo(params);
        return Result.success(params);
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @RequestBody videoinfoModel params) {
        params.setId(id);
        validatePayload(params, true);
        if (vs.findById(id) == null) {
            throw new CustomException("视频不存在");
        }
        vs.updatevideo(params);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result deletevideo(@PathVariable Integer id) {
        if (vs.findById(id) == null) {
            throw new CustomException("视频不存在");
        }
        vs.deletevideo(id);
        return Result.success();
    }

    // Keep legacy endpoints for old frontend calls.
    @RequestMapping({"/getVideo", "/getvideo"})
    public Result getVideo(videoinfoModel params) {
        return page(params);
    }

    // Keep legacy endpoints for old frontend calls.
    @RequestMapping({"/saveVideo", "/savevideo"})
    public Result savevideo(@RequestBody videoinfoModel params) {
        if (params.getId() != null) {
            return update(params.getId(), params);
        }
        return create(params);
    }

    @PostMapping("/upload/video")
    public Result uploadVideo(@RequestParam("file") MultipartFile file) {
        throw new CustomException("当前视频模块只保存元数据，不支持上传二进制文件");
    }

    @PostMapping("/upload/cover")
    public Result uploadCover(@RequestParam("file") MultipartFile file) {
        throw new CustomException("当前视频模块只保存元数据，不支持上传二进制文件");
    }

    private void validatePayload(videoinfoModel params, boolean isUpdate) {
        if (isUpdate && params.getId() == null) {
            throw new CustomException("缺少视频ID");
        }
        if (StrUtil.isBlank(params.getVideoname())) {
            throw new CustomException("视频名称不能为空");
        }
        if (StrUtil.isBlank(params.getVideocode())) {
            throw new CustomException("视频代码不能为空");
        }
        if (StrUtil.isBlank(params.getVideourl())) {
            throw new CustomException("请填写视频地址");
        }
    }

    private Path resolveLocalPath(String rawPath) {
        try {
            String value = rawPath.trim();
            if (value.startsWith("file:/")) {
                return Paths.get(URI.create(value)).toAbsolutePath().normalize();
            }
            value = value.replace("\\", "/");
            value = value.replaceAll("^([A-Za-z]):/{2,}", "$1:/");
            return Paths.get(value).toAbsolutePath().normalize();
        } catch (Exception e) {
            throw new CustomException("视频地址格式不正确：" + rawPath);
        }
    }

    private Resource toResource(Path target) {
        try {
            return new UrlResource(target.toUri());
        } catch (MalformedURLException e) {
            throw new CustomException("视频路径无效：" + target);
        }
    }
}
