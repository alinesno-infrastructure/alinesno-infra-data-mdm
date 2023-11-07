package com.alinesno.infra.data.mdm.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.data.mdm.entity.IndustryClassifyEntity;
import com.alinesno.infra.data.mdm.mapper.IndustryClassifyMapper;
import com.alinesno.infra.data.mdm.service.IIndustryClassifyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 行业分类ServiceImpl接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */

@Service
public class IndustryClassifyServiceImpl extends IBaseServiceImpl<IndustryClassifyEntity, IndustryClassifyMapper> implements IIndustryClassifyService {
    private static final Logger log = LoggerFactory.getLogger(IndustryClassifyServiceImpl.class);

    @Autowired
    private IndustryClassifyMapper  industryClassifyMapper ;


    //检查同一个用户下，行业分类是否存在，如已存在，则不允许保存。确同一个用户下行业分类唯一
    @Override
    public AjaxResult checkIfExist(IndustryClassifyEntity classify){

        QueryWrapper<IndustryClassifyEntity> wrapper = new QueryWrapper<>();

        //如果是修改，则查询其他记录，是否有同名
        if ( classify.getId() != null &&  !classify.getId().equals("") )
        {
            wrapper.ne("id", classify.getId());
        }
        wrapper.eq("name", classify.getName());
        wrapper.eq("operator_id", classify.getOperatorId());
        List<IndustryClassifyEntity> classifyList = this.list(wrapper);
        if ( classifyList != null && classifyList.size() > 0 )	{
            return AjaxResult.error("已存在同名的行业分类!") ;
        } else {
            return AjaxResult.success() ;
        }

    };

}
