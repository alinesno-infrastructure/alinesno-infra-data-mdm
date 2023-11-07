package com.alinesno.infra.data.mdm.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.data.mdm.entity.DataCategoryEntity;
import com.alinesno.infra.data.mdm.mapper.DataCategoryMapper;
import com.alinesno.infra.data.mdm.service.IDataCategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据分类ServiceImpl接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */

@Service
public class DataCategoryServiceImpl extends IBaseServiceImpl<DataCategoryEntity, DataCategoryMapper> implements IDataCategoryService {
    private static final Logger log = LoggerFactory.getLogger(DataCategoryServiceImpl.class);

    @Autowired
    private DataCategoryMapper dataCategoryMapper;

    //检查同一个用户，行业分类下,数据目录是否存在，如已存在，则不允许保存。确同一个用户、行业分类下数据目录唯一
    @Override
    public AjaxResult checkIfExist(DataCategoryEntity dataCategory){
        QueryWrapper<DataCategoryEntity> wrapper = new QueryWrapper<>();

        //如果是修改，则查询其他记录，是否有同名
        if ( dataCategory.getId() != null &&  !dataCategory.getId().equals("") )
        {
            wrapper.ne("id", dataCategory.getId());
        }
        wrapper.eq("cata_name", dataCategory.getCataName());
        wrapper.eq("classify_id", dataCategory.getClassifyId());
//        wrapper.eq("operator_id", dataCategory.getOperatorId());
        List<DataCategoryEntity> dataCatagoryList = this.list(wrapper);
        if ( dataCatagoryList != null && dataCatagoryList.size() > 0 )	{
            return AjaxResult.error("同一个行业分类下，已存在同名的数据目录!") ;
        } else {
            return AjaxResult.success() ;
        }

    };
}
