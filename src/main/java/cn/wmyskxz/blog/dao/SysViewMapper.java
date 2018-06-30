package cn.wmyskxz.blog.dao;

import cn.wmyskxz.blog.entity.SysView;
import cn.wmyskxz.blog.entity.SysViewExample;

import java.util.List;

public interface SysViewMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysView record);

    int insertSelective(SysView record);

    List<SysView> selectByExample(SysViewExample example);

    SysView selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysView record);

    int updateByPrimaryKey(SysView record);
}