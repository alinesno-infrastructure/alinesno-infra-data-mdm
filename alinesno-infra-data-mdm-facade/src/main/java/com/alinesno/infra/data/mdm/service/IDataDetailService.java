package com.alinesno.infra.data.mdm.service;

import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.facade.services.IBaseService;
import com.alinesno.infra.data.mdm.entity.DataDetailEntity;
import com.alinesno.infra.data.mdm.vo.ResponseBean;
import org.springframework.web.multipart.MultipartFile;

/**
 * 数据明细实体Service接口
 *
 * @version 1.0.0 
 * @author luoxiaodong
 */
public interface IDataDetailService extends IBaseService<DataDetailEntity> {

    /**
     * 导入参数
     *  @param file excel文件
     * @return
     */
    AjaxResult importDataDetail(MultipartFile file) ;

    //检查同一个用户，行业分类下,数据目录下的数据标准是否存在，如已存在，则不允许保存。确同一个用户、行业分类、数据目录下数据标准名称唯一
    ResponseBean checkIfExist(DataDetailEntity dataDetail);
}
