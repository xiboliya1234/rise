package com.rise.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rise.dao.videoDao;
import com.rise.dao.videoPathPrefixDao;
import com.rise.exception.CustomException;
import com.rise.model.videoPathPrefixModel;
import com.rise.model.videoinfoModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class videoService {

    @Resource
    videoDao vd;
    @Resource
    videoPathPrefixDao vpd;

    public PageInfo<videoinfoModel> getDataBysbmc(videoinfoModel params) {
        int pageNum = params.getPagenum() == null || params.getPagenum() < 1 ? 1 : params.getPagenum();
        int pageSize = params.getPagesize() == null || params.getPagesize() < 1 ? 10 : params.getPagesize();
        pageSize = Math.min(pageSize, 100);

        PageHelper.startPage(pageNum, pageSize);
        List<videoinfoModel> vm = vd.getVideoinfo(params);
        return PageInfo.of(vm);
    }

    public videoinfoModel findById(Integer id) {
        return vd.selectById(id);
    }

    public void validateVideoCodeUnique(String videocode, Integer currentId) {
        if (StrUtil.isBlank(videocode)) {
            throw new CustomException("视频代码不能为空");
        }
        QueryWrapper<videoinfoModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("videocode", videocode);
        videoinfoModel existing = vd.selectOne(queryWrapper);
        if (existing != null && (currentId == null || !existing.getId().equals(currentId))) {
            throw new CustomException("视频代码已存在，请更换");
        }
    }

    public void savevideo(videoinfoModel params) {
        validateVideoCodeUnique(params.getVideocode(), null);
        vd.insert(params);
    }

    public void updatevideo(videoinfoModel params) {
        validateVideoCodeUnique(params.getVideocode(), params.getId());
        vd.updateById(params);
    }

    public void deletevideo(Integer id) {
        vd.deleteById(id);
    }

    public List<videoPathPrefixModel> listEnabledPrefixes() {
        QueryWrapper<videoPathPrefixModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enabled", 1)
                .orderByAsc("sort_num")
                .orderByAsc("id");
        return vpd.selectList(queryWrapper);
    }
}
