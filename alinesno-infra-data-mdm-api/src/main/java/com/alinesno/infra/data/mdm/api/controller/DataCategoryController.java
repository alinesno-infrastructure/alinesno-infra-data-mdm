package com.alinesno.infra.data.mdm.api.controller;

import com.alinesno.infra.common.core.constants.SpringInstanceScope;
import com.alinesno.infra.common.facade.pageable.DatatablesPageBean;
import com.alinesno.infra.common.facade.pageable.TableDataInfo;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.facade.response.ResultCodeEnum;
import com.alinesno.infra.common.facade.wrapper.RpcWrapper;
import com.alinesno.infra.common.web.adapter.plugins.TranslateCode;
import com.alinesno.infra.common.web.adapter.rest.BaseController;
import com.alinesno.infra.data.mdm.entity.DataCategoryEntity;
import com.alinesno.infra.data.mdm.entity.DataChangeLogEntity;
import com.alinesno.infra.data.mdm.entity.DataDetailEntity;
import com.alinesno.infra.data.mdm.service.IDataCategoryService;
import com.alinesno.infra.data.mdm.service.IDataChangeLogService;
import com.alinesno.infra.data.mdm.service.IDataDetailService;
import com.alinesno.infra.data.mdm.vo.ResponseBean;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 处理与DataCategory相关的请求的Controller。
 * 继承自BaseController类并实现IBusinessSystemService接口。
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Api(tags = "DataCategory")
@RestController
@Scope(SpringInstanceScope.PROTOTYPE)
@RequestMapping("/api/mdm/dataCatagory")
public class DataCategoryController extends BaseController<DataCategoryEntity, IDataCategoryService> {

    // 日志记录
    private static final Logger log = LoggerFactory.getLogger(DataCategoryController.class);

    @Autowired
    private IDataCategoryService service;

    @Autowired
    private IDataDetailService dataDetailService;

    @Autowired
    private IDataChangeLogService dataChangeLogService;

    /**
     * 获取DataCategory的DataTables数据。
     *
     * @param request HttpServletRequest对象。
     * @param model Model对象。
     * @param page DatatablesPageBean对象。
     * @return 包含DataTables数据的TableDataInfo对象。
     */
    @TranslateCode(plugin = "DataCategoryPlugin")
    @ResponseBody
    @PostMapping("/datatables")
    public TableDataInfo datatables(HttpServletRequest request, Model model, DatatablesPageBean page) {
        log.debug("page = {}", ToStringBuilder.reflectionToString(page));
        return this.toPage(model, this.getFeign(), page);
    }

    @Override
    public IDataCategoryService getFeign() {
        return this.service;
    }

    /**
     * 查询树形导航条当前节点和下级节点及符合查询条件的数据
     *
     * @param request
     * @param model
     * @param page
     * @param ids         树形导航条当前节点和下级节点的id值
     * @param pageNum     预防查询面板没有设置查询条件时，page中没有值
     * @param pageSize    预防查询面板没有设置查询条件时，page中没有值
     */
    //@DataFilter
    @TranslateCode(plugin = "DataCatagoryPlugin")
    @ResponseBody
    @PostMapping({"listTreeNavInfo"})
    public TableDataInfo listTreeNavInfo(HttpServletRequest request, Model model, DatatablesPageBean page,String[] ids ,int pageNum,int pageSize ) {

        RpcWrapper<DataCategoryEntity> wrapper =   new RpcWrapper();
        wrapper.in("id",ids) ;
        wrapper.builderCondition(page.getCondition());
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);

        Page<DataCategoryEntity> pageableResult = null;

        if (pageableResult == null) {
            pageableResult = new Page((long)page.getPageNum(), (long)page.getPageSize());
            pageableResult = this.getFeign().pageRpc(pageableResult, wrapper);
        }

        TableDataInfo dInfo = new TableDataInfo();
        dInfo.setCode(200);
        dInfo.setMsg("查询成功");
        dInfo.setRows(pageableResult.getRecords());
        dInfo.setTotal((long)((int)pageableResult.getTotal()));
        return dInfo;

    }

    /**
     * 检查同一个用户，同一个行业分类下,数据目录是否存在，如已存在，则不允许保存。确同一个用户、行业分类下数据目录唯一
     *
     */
    @ResponseBody
    @PostMapping("/checkDataCatagoryIfExist")
    public AjaxResult checkDataCatagoryIfExist(HttpServletRequest request, @RequestBody DataCategoryEntity dataCategory)  {
//        // 设置用户
//        ManagerAccountEntity account = CurrentAccountJwt.get() ;
//        if (account != null) {
//            dataCatagory.setOperatorId(account.getId());
//        }

        return service.checkIfExist(dataCategory);
    }


    /**
     * 检查数据目录是否已被数据标准引用，如被引用，则不能删除
     *
     */
    @ResponseBody
    @GetMapping("/checkCatagoryIfUsed")
    public AjaxResult checkCatagoryIfUsed(HttpServletRequest request, String ids)  {
        if ( ids == null ) {
            return AjaxResult.error("请求删除的id为空!");
        }

        List<String> idList = Arrays.asList(ids.split(","));

        List<String> usedIdList = new ArrayList<String>();

        StringBuffer msg = new StringBuffer();

        Set<String> catagorySet = new HashSet<String>() ;

        Long operatorId = null ;
//        // 设置用户
//        ManagerAccountEntity account = CurrentAccountJwt.get() ;
//        if ( account != null ) {
//            operatorId = account.getId();
//        }

        ResponseBean responseBean = checkHasChildren(idList, operatorId);
        if ( responseBean.getCode() == 400 ){
            return AjaxResult.error(responseBean.getMessage()) ;
        }

        //检查是否存在数据标准
        QueryWrapper<DataDetailEntity> wrapper = new QueryWrapper<>();
        wrapper.in("cata_id",idList);
//        wrapper.eq("operator_id", operatorId);
        List<DataDetailEntity> detaillist = dataDetailService.list(wrapper);
        if ( detaillist != null && detaillist.size() > 0 )	{
            int i = 0 ;
            for (DataDetailEntity dataDetailEntity : detaillist) {
                //避免提示信息太长，只取前4个的名称
                if ( i == 3 ) {
                    msg.deleteCharAt( msg.length() - 1 ) ;
                    msg.append("等");
                    break;
                }
                msg.append(dataDetailEntity.getStandardName());
                msg.append(",");
                i = i + 1 ;
            }
            if (   msg.lastIndexOf(",") == msg.length() - 1   ) {
                msg.deleteCharAt( msg.length() - 1 ) ;
            }

            return AjaxResult.error("数据目录已被数据标准\""+ msg.deleteCharAt( msg.length() - 1 )+"\"引用,不能删除!") ;
        } else {
            return AjaxResult.success() ;
        }

    }

    private ResponseBean checkHasChildren(List<String> idList, Long operatorId){
        ResponseBean result = new ResponseBean() ;
        StringBuffer msg = new StringBuffer();
        //检查是否存在子目录
        QueryWrapper<DataCategoryEntity> cataWrapper = new QueryWrapper<>();
        cataWrapper.in("parent_cata_id",idList);
//        cataWrapper.eq("operator_id", operatorId);
        List<DataCategoryEntity> catalist = service.list(cataWrapper);
        if ( catalist != null && catalist.size() > 0 )	{
            Set<Long> cataSet = new HashSet<Long>() ;
            List<String> usedList = new ArrayList<String>();
            for (DataCategoryEntity cataEntity : catalist) {
                cataSet.add( cataEntity.getId() ) ;
            }

            for( Long catagoryID :cataSet ){
                usedList.add(String.valueOf(catagoryID)) ;
            }

            List<DataCategoryEntity> categorylist = service.findByIds( usedList );
            for (DataCategoryEntity dataCategoryEntity : categorylist) {
                msg.append(dataCategoryEntity.getCataName()).append(",");
            }
            result.setCode(ResultCodeEnum.FAIL) ;
            result.setMessage("数据目录已被\""+ msg.deleteCharAt( msg.length() - 1 )+"\"引用,不能删除!");

        } else {
            result.setCode(ResultCodeEnum.SUCCESS) ;

        }
        return result;

    }

    //由于前端界面存在勾选多条记录删除的功能，需要在后台将删除的数据迁移到历史表
    @ApiOperation("通过id删除")
    @ResponseBody
    @DeleteMapping({"delete/{ids}"})
    public AjaxResult delete(@PathVariable String ids) {
        log.debug("delete ids:{}", ToStringBuilder.reflectionToString(ids));
        if (StringUtils.isBlank(ids)) {
            return AjaxResult.error();
        } else {

            //获取待删除的数据目录信息，复制信息到历史表
            List<String> idList = Arrays.asList(ids.split(","));
            List<DataChangeLogEntity> catagoryLogList = new ArrayList<DataChangeLogEntity>();
            List<DataCategoryEntity> catagoryList = service.findByIds(idList);
            for (DataCategoryEntity catagory : catagoryList) {
                DataChangeLogEntity catagoryLog = new DataChangeLogEntity();
                BeanUtils.copyProperties(catagoryLog,catagory);
                catagoryLog.setOldId(catagory.getId());
                catagoryLog.setId(null);
                catagoryLogList.add(catagoryLog);

            }

            //准备好数据后，开始删除和保存
            String[] rowsId = ids.split(",");
            if (rowsId != null && rowsId.length > 0) {

                Long[] tmpIds = new Long[rowsId.length];
                for (int i = 0; i < rowsId.length; i++) {
                    tmpIds[i] = Long.parseLong(rowsId[i]);
                }
                this.getFeign().deleteByIds(tmpIds);
            }

            dataChangeLogService.saveOrUpdateBatch(catagoryLogList);

            return AjaxResult.success();
        }
    }

}
