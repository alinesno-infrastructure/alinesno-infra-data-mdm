package com.alinesno.infra.data.mdm.mapper;

import com.alinesno.infra.common.facade.mapper.repository.IBaseMapper;
import com.alinesno.infra.data.mdm.entity.IndustryClassifyEntity;
import com.alinesno.infra.data.mdm.vo.IndustryClassifyIFVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 行业分类Mapper接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Repository
public interface IndustryClassifyMapper extends IBaseMapper<IndustryClassifyEntity> {

    /**
     * 查询行业分类清单
     * @param operatorId 操作者ID
     * @return 返回行业分类清单VO
     */
    public List<IndustryClassifyIFVO> getClassifyList(String operatorId);


    /**
     * 通过分类ID获取行业分类信息
     * @param operatorId 操作者ID
     * @param classifyId 行业分类ID
     * @return 返回行业分类清单VO
     */
    public IndustryClassifyIFVO getClassifyByID(String operatorId, String classifyId);
}
