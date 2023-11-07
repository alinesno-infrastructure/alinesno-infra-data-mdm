package com.alinesno.infra.data.mdm.api.controller;

import com.alinesno.infra.common.core.constants.SpringInstanceScope;
import com.alinesno.infra.common.facade.pageable.DatatablesPageBean;
import com.alinesno.infra.common.facade.pageable.TableDataInfo;
import com.alinesno.infra.common.web.adapter.plugins.TranslateCode;
import com.alinesno.infra.common.web.adapter.rest.BaseController;
import com.alinesno.infra.data.mdm.entity.DataDetailLogEntity;
import com.alinesno.infra.data.mdm.service.IDataDetailLogService;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 处理与DataDetailLog相关的请求的Controller。
 * 继承自BaseController类并实现IDataDetailLogService接口。
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Api(tags = "DataDetailLog")
@RestController
@Scope(SpringInstanceScope.PROTOTYPE)
@RequestMapping("/api/mdm/dataDetailLog")
public class DataDetailLogController extends BaseController<DataDetailLogEntity, IDataDetailLogService> {

    // 日志记录
    private static final Logger log = LoggerFactory.getLogger(DataDetailLogController.class);

    @Autowired
    private IDataDetailLogService service;

    /**
     * 获取DataDetailLog的DataTables数据。
     *
     * @param request HttpServletRequest对象。
     * @param model Model对象。
     * @param page DatatablesPageBean对象。
     * @return 包含DataTables数据的TableDataInfo对象。
     */
    @TranslateCode(plugin = "DataDetailLogPlugin")
    @ResponseBody
    @PostMapping("/datatables")
    public TableDataInfo datatables(HttpServletRequest request, Model model, DatatablesPageBean page) {
        log.debug("page = {}", ToStringBuilder.reflectionToString(page));
        this.setConditions(page);
        return this.toPage(model, this.getFeign(), page);
    }

    @Override
    public IDataDetailLogService getFeign() {
        return this.service;
    }

    private void setConditions(DatatablesPageBean page) {
        Map<String, Object> condition = page.getCondition();
        if (MapUtils.isNotEmpty(condition)) {
            //设置通过数据目录、数据标准编码进行排序
            condition.put("cataId|orderBy", "false");
            condition.put("code|orderBy", "true");
            page.setCondition(condition);
        }
    }
}
