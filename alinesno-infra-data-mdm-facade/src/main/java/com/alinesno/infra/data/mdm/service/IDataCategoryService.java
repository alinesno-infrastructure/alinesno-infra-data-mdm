package com.alinesno.infra.data.mdm.service;

import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.facade.services.IBaseService;
import com.alinesno.infra.data.mdm.entity.DataCategoryEntity;

/**
 * 数据分类实体Service接口
 *
 * @version 1.0.0 
 * @author luoxiaodong
 */
public interface IDataCategoryService extends IBaseService<DataCategoryEntity> {

    //检查同一个用户，行业分类下,数据目录是否存在，如已存在，则不允许保存。确同一个用户、行业分类下数据目录唯一
    AjaxResult checkIfExist(DataCategoryEntity dataCatagory);
}
