package com.alinesno.infra.data.mdm.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.mdm.entity.DataDetailLogEntity;
import com.alinesno.infra.data.mdm.mapper.DataDetailLogMapper;
import com.alinesno.infra.data.mdm.service.IDataDetailLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 数据明细日志ServiceImpl接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */

@Service
public class DataDetailLogServiceImpl extends IBaseServiceImpl<DataDetailLogEntity, DataDetailLogMapper> implements IDataDetailLogService {
    private static final Logger log = LoggerFactory.getLogger(DataDetailLogServiceImpl.class);
}
