package com.alinesno.infra.data.mdm.api.controller;

import com.alinesno.infra.common.core.constants.SpringInstanceScope;
import com.alinesno.infra.common.facade.pageable.DatatablesPageBean;
import com.alinesno.infra.common.facade.pageable.TableDataInfo;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.web.adapter.rest.BaseController;
import com.alinesno.infra.data.mdm.entity.BusinessSystemEntity;
import com.alinesno.infra.data.mdm.entity.DataCategoryEntity;
import com.alinesno.infra.data.mdm.service.IBusinessSystemService;
import com.alinesno.infra.data.mdm.service.IDataCategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 处理与BusinessSystem相关的请求的Controller。
 * 继承自BaseController类并实现IBusinessSystemService接口。
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Api(tags = "BusinessSystem")
@RestController
@Scope(SpringInstanceScope.PROTOTYPE)
@RequestMapping("/api/mdm/businessSystem")
public class BusinessSystemController extends BaseController<BusinessSystemEntity, IBusinessSystemService> {

    // 日志记录
    private static final Logger log = LoggerFactory.getLogger(BusinessSystemController.class);

    @Autowired
    private IBusinessSystemService service;

    @Autowired
    private IDataCategoryService dataCategoryService;

    /**
     * 获取BusinessSystem的DataTables数据。
     *
     * @param request HttpServletRequest对象。
     * @param model Model对象。
     * @param page DatatablesPageBean对象。
     * @return 包含DataTables数据的TableDataInfo对象。
     */
    @ResponseBody
    @PostMapping("/datatables")
    public TableDataInfo datatables(HttpServletRequest request, Model model, DatatablesPageBean page) {
        log.debug("page = {}", ToStringBuilder.reflectionToString(page));
        return this.toPage(model, this.getFeign(), page);
    }

    @Override
    public IBusinessSystemService getFeign() {
        return this.service;
    }

    /**
     * 检查业务系统是否已被数据目录引用，如被引用，则不能删除
     *
     */
    @ResponseBody
    @GetMapping("/checkSystemIfUsed")
    public AjaxResult checkSystemIfUsed(HttpServletRequest request, String ids)  {
        if ( ids == null ) {
            return AjaxResult.error("请求删除的id为空!");
        }

        List<String> idList = Arrays.asList(ids.split(","));

        StringBuffer msg = new StringBuffer();

//        Long operatorId = null ;
        // 设置用户
//        ManagerAccountEntity account = CurrentAccountJwt.get() ;
//        if ( account != null ) {
//            operatorId = account.getId();
//        }

        //检查是否已被数据目录引用
        QueryWrapper<DataCategoryEntity> cataWrapper = new QueryWrapper<>();
        cataWrapper.in("sc_sys_id",idList);
//        cataWrapper.eq("operator_id", operatorId);
        List<DataCategoryEntity> catelist = dataCategoryService.list(cataWrapper);
        if ( catelist != null && catelist.size() > 0 )	{
            Set<Long> cataSet = new HashSet<Long>() ;
            List<String> usedList = new ArrayList<String>();
            for (DataCategoryEntity cateEntity : catelist) {
                cataSet.add( cateEntity.getId() ) ;
            }

            for( Long catagoryID :cataSet ){
                usedList.add(String.valueOf(catagoryID)) ;
            }

            List<DataCategoryEntity> catagorylist = dataCategoryService.findByIds( usedList );
            for (DataCategoryEntity dataCatagoryEntity : catagorylist) {
                msg.append(dataCatagoryEntity.getCataName()).append(",");
            }
            return AjaxResult.error("业务系统已被\""+ msg.deleteCharAt( msg.length() - 1 )+"\"引用,不能删除!") ;
        } else {
            return AjaxResult.success() ;
        }



    }
}
