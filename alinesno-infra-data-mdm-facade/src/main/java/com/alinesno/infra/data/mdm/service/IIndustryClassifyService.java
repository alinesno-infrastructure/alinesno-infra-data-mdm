package com.alinesno.infra.data.mdm.service;

import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.facade.services.IBaseService;
import com.alinesno.infra.data.mdm.entity.IndustryClassifyEntity;

/**
 * 行业分类实体Service接口
 *
 * @version 1.0.0 
 * @author luoxiaodong
 */
public interface IIndustryClassifyService extends IBaseService<IndustryClassifyEntity> {

    //检查同一个用户下，行业分类是否存在，如已存在，则不允许保存。确同一个用户下行业分类唯一
    AjaxResult checkIfExist(IndustryClassifyEntity classify);

}
