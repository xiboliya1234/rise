package com.rise.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rise.dao.CslxDao;
import com.rise.exception.CustomException;
import com.rise.model.fileModel;
import com.rise.model.sblxModel;
import com.rise.model.userModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CslxService {

    @Resource
    CslxDao cd;

    public void deleteCslx(Integer id) {
        cd.deleteById(id);
    }
    public void deleteCslxBatchIds(List<Integer> id) {
        cd.deleteBatchIds(id);
    }

    public Long findusernameByusername(@Param("ew") sblxModel params){
        QueryWrapper<sblxModel> queryWrapper = new QueryWrapper();
        queryWrapper.eq("lxmc", params.getLxmc());//tom_age必须是数据库中的字段
        return cd.selectCount(queryWrapper);
    }

    public void saveCslx(sblxModel params) {
        int list = Math.toIntExact(findusernameByusername(params));
        if(list >0){
            throw new CustomException("类型"+params.getLxmc()+"已存在");

        }
        if(StrUtil.isEmpty(params.getLxmc())){
            throw new CustomException("类型名称不能为空");
        }

        params.setLxid(params.getLxmc());
        cd.insert(params);
    }

    public void updateCslx(@Param("et") sblxModel params) {

        cd.updateById(params);
    }

    public List<sblxModel> selectList() {

        return cd.selectList(null);
    }


}
