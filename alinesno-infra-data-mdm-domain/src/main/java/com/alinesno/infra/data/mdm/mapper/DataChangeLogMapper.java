package com.alinesno.infra.data.mdm.mapper;

import com.alinesno.infra.common.facade.mapper.repository.IBaseMapper;
import com.alinesno.infra.data.mdm.entity.DataChangeLogEntity;
import org.springframework.stereotype.Repository;

/**
 * 数据变更日志Mapper接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Repository
public interface DataChangeLogMapper extends IBaseMapper<DataChangeLogEntity> {
}
