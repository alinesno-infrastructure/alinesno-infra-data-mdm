package com.alinesno.infra.data.mdm.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.mdm.entity.DataChangeLogEntity;
import com.alinesno.infra.data.mdm.mapper.DataChangeLogMapper;
import com.alinesno.infra.data.mdm.service.IDataChangeLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 数据变更日志ServiceImpl接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */

@Service
public class DataChangeLogServiceImpl extends IBaseServiceImpl<DataChangeLogEntity, DataChangeLogMapper> implements IDataChangeLogService {
    private static final Logger log = LoggerFactory.getLogger(DataChangeLogServiceImpl.class);
}
