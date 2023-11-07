package com.alinesno.infra.data.mdm.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.mdm.entity.BusinessSystemEntity;
import com.alinesno.infra.data.mdm.mapper.BusinessSystemMapper;
import com.alinesno.infra.data.mdm.service.IBusinessSystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 业务系统ServiceImpl接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */

@Service
public class BusinessSystemServiceImpl extends IBaseServiceImpl<BusinessSystemEntity, BusinessSystemMapper> implements IBusinessSystemService {
    private static final Logger log = LoggerFactory.getLogger(BusinessSystemServiceImpl.class);
}
