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
import com.alinesno.infra.data.mdm.entity.IndustryClassifyEntity;
import com.alinesno.infra.data.mdm.service.IDataCategoryService;
import com.alinesno.infra.data.mdm.service.IIndustryClassifyService;
import com.alinesno.infra.data.mdm.vo.ResponseBean;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 * 处理与IndustryClassify相关的请求的Controller。
 * 继承自BaseController类并实现IIndustryClassifyService接口。
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Api(tags = "IndustryClassify")
@RestController
@Scope(SpringInstanceScope.PROTOTYPE)
@RequestMapping("/api/mdm/industryClassify")
public class IndustryClassifyController extends BaseController<IndustryClassifyEntity, IIndustryClassifyService> {

    // 日志记录
    private static final Logger log = LoggerFactory.getLogger(IndustryClassifyController.class);

    @Autowired
    private IIndustryClassifyService service;

    @Autowired
    private IDataCategoryService dataCategoryService;

    /**
     * 获取IndustryClassify的DataTables数据。
     *
     * @param request HttpServletRequest对象。
     * @param model Model对象。
     * @param page DatatablesPageBean对象。
     * @return 包含DataTables数据的TableDataInfo对象。
     */
    @TranslateCode(plugin = "IndustryClassifyPlugin")
    @ResponseBody
    @PostMapping("/datatables")
    public TableDataInfo datatables(HttpServletRequest request, Model model, DatatablesPageBean page) {
        log.debug("page = {}", ToStringBuilder.reflectionToString(page));
        return this.toPage(model, this.getFeign(), page);
    }

    @Override
    public IIndustryClassifyService getFeign() {
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
//    @DataFilter
    @TranslateCode(plugin = "IndustryClassifyPlugin")
    @ResponseBody
    @PostMapping({"listTreeNavInfo"})
    public TableDataInfo listTreeNavInfo(HttpServletRequest request, Model model, DatatablesPageBean page,String[] ids ,int pageNum,int pageSize ) {

        RpcWrapper<IndustryClassifyEntity> wrapper =   new RpcWrapper();
        wrapper.in("id",ids) ;
        wrapper.builderCondition(page.getCondition());
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);

        Page<IndustryClassifyEntity> pageableResult = null;

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
     * 检查同一个用户下，行业分类是否存在，如已存在，则不允许保存。确同一个用户下行业分类唯一
     *
     */
    @ResponseBody
    @PostMapping("/checkClassifyIfExist")
    public AjaxResult checkClassifyIfExist(HttpServletRequest request, @RequestBody IndustryClassifyEntity classify)  {
        // 设置用户
//        ManagerAccountEntity account = CurrentAccountJwt.get() ;
//        if (account != null) {
//            classify.setOperatorId(account.getId());
//        }

        return service.checkIfExist(classify);
    }

    /**
     * 检查行业分类是否已被数据目录引用，如被引用，则不能删除
     *
     */
    @ResponseBody
    @GetMapping("/checkClassifyIfUsed")
    public AjaxResult checkClassifyIfUsed(HttpServletRequest request, String ids)  {
        if ( ids == null ) {
            return AjaxResult.error("请求删除的id为空!");
        }

        List<String> idList = Arrays.asList(ids.split(","));

        StringBuffer msg = new StringBuffer();

        Long operatorId = null ;
//        // 设置用户
//        ManagerAccountEntity account = CurrentAccountJwt.get() ;
//        if ( account != null ) {
//            operatorId = account.getId();
//        }

        //检查是否有子分类，如有子分类，则不允许删除
        ResponseBean responseBean = checkHasChildren(idList, operatorId);
        if ( responseBean.getCode() == 400 ){
            return AjaxResult.error(responseBean.getMessage()) ;
        }

        //检查是否被数据目录引用，如有记录，则不允许删除
        QueryWrapper<DataCategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.in("classify_id",idList);
//        wrapper.eq("operator_id", operatorId);
        List<DataCategoryEntity> dataCatagoryList = dataCategoryService.list(wrapper);
        if ( dataCatagoryList != null && dataCatagoryList.size() > 0 )	{
            int i = 0 ;
            for ( DataCategoryEntity dataCatagoryEntity : dataCatagoryList ) {
                //避免提示信息太长，只取前4个的名称
                if ( i == 3 ) {
                    msg.deleteCharAt( msg.length() - 1 ) ;
                    msg.append("等");
                    break;
                }
                msg.append(dataCatagoryEntity.getCataName());
                msg.append(",");
                i = i + 1 ;

            }
            if (   msg.lastIndexOf(",") == msg.length() - 1   ) {
                msg.deleteCharAt( msg.length() - 1 ) ;
            }

            return AjaxResult.error("行业分类已被数据目录\"" + msg.toString() + "\"引用,不能删除!") ;
        } else {
            return AjaxResult.success() ;
        }

    }

    private ResponseBean checkHasChildren(List<String> idList, Long operatorId){
        ResponseBean result = new ResponseBean() ;
        StringBuffer msg = new StringBuffer();
        //检查是否存在子行业分类
        QueryWrapper<IndustryClassifyEntity> classifyWrapper = new QueryWrapper<>();
        classifyWrapper.in("parent_id",idList);
//        classifyWrapper.eq("operator_id", operatorId);
        List<IndustryClassifyEntity> classifylist = service.list(classifyWrapper);
        if ( classifylist != null && classifylist.size() > 0 )	{
            Set<Long> classifySet = new HashSet<Long>() ;
            List<String> usedList = new ArrayList<String>();
            for (IndustryClassifyEntity  classifyEntity : classifylist) {
                classifySet.add( classifyEntity.getId() ) ;
            }

            for( Long classifyID :classifySet ){
                usedList.add(String.valueOf(classifyID)) ;
            }

            List<IndustryClassifyEntity> classifyChildlist = service.findByIds( usedList );
            int i = 0 ;
            for (IndustryClassifyEntity classifyEntity : classifyChildlist) {
                //避免提示信息太长，只取前4个的名称
                if ( i == 3 ) {
                    msg.deleteCharAt( msg.length() - 1 ) ;
                    msg.append("等");
                    break;
                }
                msg.append(classifyEntity.getName());
                msg.append(",");
                i = i + 1 ;
            }
            if (   msg.lastIndexOf(",") == msg.length() - 1   ) {
                msg.deleteCharAt( msg.length() - 1 ) ;
            }
            result.setCode(ResultCodeEnum.FAIL);
            result.setMessage("行业分类已被\"" +  msg.toString() + "\"引用,不能删除!") ;
        } else {
            result.setCode(ResultCodeEnum.SUCCESS);

        }
        return  result ;
    }
}
