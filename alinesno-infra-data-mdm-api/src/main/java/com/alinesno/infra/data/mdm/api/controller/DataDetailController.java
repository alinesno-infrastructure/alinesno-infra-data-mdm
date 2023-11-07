package com.alinesno.infra.data.mdm.api.controller;

import com.alinesno.infra.common.core.constants.SpringInstanceScope;
import com.alinesno.infra.common.facade.pageable.DatatablesPageBean;
import com.alinesno.infra.common.facade.pageable.TableDataInfo;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.facade.wrapper.RpcWrapper;
import com.alinesno.infra.common.web.adapter.plugins.TranslateCode;
import com.alinesno.infra.common.web.adapter.rest.BaseController;
import com.alinesno.infra.data.mdm.entity.DataDetailEntity;
import com.alinesno.infra.data.mdm.entity.DataDetailLogEntity;
import com.alinesno.infra.data.mdm.service.IDataDetailLogService;
import com.alinesno.infra.data.mdm.service.IDataDetailService;
import com.alinesno.infra.data.mdm.vo.ResponseBean;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 处理与DataDetail相关的请求的Controller。
 * 继承自BaseController类并实现IDataDetailService接口。
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Api(tags = "DataDetail")
@RestController
@Scope(SpringInstanceScope.PROTOTYPE)
@RequestMapping("/api/mdm/dataDetail")
public class DataDetailController extends BaseController<DataDetailEntity, IDataDetailService> {

    // 日志记录
    private static final Logger log = LoggerFactory.getLogger(DataDetailController.class);

    @Autowired
    private IDataDetailService service;

    @Autowired
    private IDataDetailLogService dataDetailLogService;

    /**
     * 获取DataDetail的DataTables数据。
     *
     * @param request HttpServletRequest对象。
     * @param model Model对象。
     * @param page DatatablesPageBean对象。
     * @return 包含DataTables数据的TableDataInfo对象。
     */
    @TranslateCode(plugin = "DataDetailPlugin")
    @ResponseBody
    @PostMapping("/datatables")
    public TableDataInfo datatables(HttpServletRequest request, Model model, DatatablesPageBean page) {
        log.debug("page = {}", ToStringBuilder.reflectionToString(page));
        this.setConditions(page);
        return this.toPage(model, this.getFeign(), page);
    }

    @Override
    public IDataDetailService getFeign() {
        return this.service;
    }

    /**
     * 导出模板文件
     *
     * @param request
     * @param response
     * @param model
     * @throws IOException
     */
    @GetMapping("/exportTemplate")
    public ResponseEntity<byte[]> exportTemplate(HttpServletRequest request, HttpServletResponse response,
                                                 Model model) throws  IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL url = classLoader.getResource("");
        // 获得服务器路径
        String srcTemplateFilePath = url.getPath();
        // 文件的存放路径
        String filePath=srcTemplateFilePath+ File.separator+"template" + File.separator + "dataDetailImportTemplate.xlsx";
        byte[] body = null;

        //InputStream is =  new FileInputStream(data.getSrcPath() + File.separator + fileName);// 文件的存放路径
        InputStream is =  new FileInputStream(filePath);// 文件的存放路径
        body = is.readAllBytes();
        //设置头信息
        HttpHeaders headers = new HttpHeaders();
        //设置下载的附件 (myFileName必须处理中文名称!)
        String fileName =  "主数据导入模板" + ".xlsx";
        //处理中文编码
        String myFileName = new String(fileName.getBytes("utf-8"), "iso-8859-1");
        headers.setContentDispositionFormData("attachment", myFileName);
        //设置MIME类型
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        //用 FileUpload 组件的 FileUtils 读取文件，并构建成 ResponseEntity<byte[]>返回给浏览器
        return new ResponseEntity<byte[]>(body, headers, HttpStatus.CREATED);
    }

    /**
     * 上传文件
     * @param request
     * @param file
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/upload")
    public AjaxResult uploadFile(HttpServletRequest request, MultipartFile file)  {
        return this.service.importDataDetail(file);
    }

    /**
     * 检查同一个用户，行业分类下,数据目录下的数据标准是否存在，如已存在，则不允许保存。确同一个用户、行业分类、数据目录下数据目录唯一
     *
     */
    @ResponseBody
    @PostMapping("/checkMdIfExist")
    public AjaxResult checkMdIfExist(HttpServletRequest request, @RequestBody DataDetailEntity dataDetail)  {
        // 设置用户
//        ManagerAccountEntity account = CurrentAccountJwt.get() ;
//        if (account != null) {
//            dataDetail.setOperatorId(account.getId());
//        }

        ResponseBean checkResult = service.checkIfExist(dataDetail);
        if ( checkResult.getCode() == 400 ) {
            return AjaxResult.error(checkResult.getMessage()) ;

        }else {
            return AjaxResult.success();
        }

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
    @TranslateCode(plugin = "DataDetailPlugin")
    @ResponseBody
    @PostMapping({"listTreeNavInfo"})
    public TableDataInfo listTreeNavInfo(HttpServletRequest request, Model model, DatatablesPageBean page,String[] ids ,int pageNum,int pageSize ) {

        RpcWrapper<DataDetailEntity> wrapper =   new RpcWrapper();
        wrapper.in("cata_id",ids) ;
        wrapper.builderCondition(page.getCondition());
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);

        Page<DataDetailEntity> pageableResult = null;

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


    //由于前端界面存在勾选多条记录删除的功能，需要在后台将删除的数据迁移到历史表

    @ApiOperation("通过id删除")
    @ResponseBody
    @DeleteMapping({"delete/{ids}"})
    public AjaxResult delete(@PathVariable String ids) {
//        log.debug("delete ids:{}", ToStringBuilder.reflectionToString(ids));
        if (StringUtils.isBlank(ids)) {
            return AjaxResult.error();
        } else {

            //获取待删除的数据目录信息，复制信息到历史表
            List<String> idList = Arrays.asList(ids.split(","));
            List<DataDetailLogEntity> detailLogList = new ArrayList<DataDetailLogEntity>();
            List<DataDetailEntity> detailList = service.findByIds(idList);
            if ( detailList.size() == 0 ) {
                return AjaxResult.error("操作失败！没有找到需要删除的数据!");
            }
            for (DataDetailEntity detail : detailList) {
                DataDetailLogEntity detailLog = new DataDetailLogEntity();
                BeanUtils.copyProperties(detailLog,detail);
                detailLog.setOldId(detail.getId());
                detailLog.setId(null);
                detailLogList.add(detailLog);

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
            dataDetailLogService.saveOrUpdateBatch(detailLogList);

            return AjaxResult.success();
        }
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
