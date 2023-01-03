package com.pzhu.acp.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzhu.acp.model.entity.OriginUser;
import com.pzhu.acp.model.vo.OriginUserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

/**
* @author Administrator
* @description 针对表【origin_user】的数据库操作Mapper
* @createDate 2022-11-21 21:34:21
* @Entity com.pzhu.acp.model.OriginUser
*/
public interface OriginUserMapper extends BaseMapper<OriginUser> {

    IPage<OriginUserVO> selectOriginUserVO(Page<OriginUserVO> originUserVOPage, @Param("oid") Long oid);
}




