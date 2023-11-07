package com.alinesno.infra.data.mdm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.facade.response.ResultCodeEnum;
import com.alinesno.infra.common.facade.wrapper.RpcWrapper;
import com.alinesno.infra.data.mdm.entity.DataCategoryEntity;
import com.alinesno.infra.data.mdm.entity.DataDetailEntity;
import com.alinesno.infra.data.mdm.mapper.DataDetailMapper;
import com.alinesno.infra.data.mdm.service.IDataCategoryService;
import com.alinesno.infra.data.mdm.service.IDataDetailService;
import com.alinesno.infra.data.mdm.vo.DataDetailVO;
import com.alinesno.infra.data.mdm.vo.ResponseBean;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据明细ServiceImpl接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */

@Service
public class DataDetailServiceImpl extends IBaseServiceImpl<DataDetailEntity, DataDetailMapper> implements IDataDetailService {
    private static final Logger log = LoggerFactory.getLogger(DataDetailServiceImpl.class);

    @Autowired
    private IDataCategoryService dataCategoryService;

    @Autowired
    private DataDetailMapper dataDetailMapper;


    private Map<String,Long> getDataCatagoryId(String dataCatagoryName, Long operatorId) {
        Map<String,Long> cataInfo = new HashMap<String,Long>();
        RpcWrapper<DataCategoryEntity> dataCatagoryQuery = new RpcWrapper<>();
        dataCatagoryQuery.eq("cata_name", dataCatagoryName);
        if ( operatorId != null ) {
            dataCatagoryQuery.eq("operator_id", operatorId);
        }
        List<DataCategoryEntity> catagoryList = dataCategoryService.findAll(dataCatagoryQuery);
        if ( catagoryList != null && catagoryList.size() > 0 ) {
            cataInfo.put("catagoryID",catagoryList.get(0).getId()) ;
            cataInfo.put("classifyId",catagoryList.get(0).getClassifyId()) ;
        }else{
            cataInfo.put("catagoryID",null) ;
            cataInfo.put("classifyId",null) ;
        }
        return cataInfo;
    }


    /**
     * 导入参数
     *  @param file excel文件 @RequestParam("file")
     * @return
     */
    @Override
    public AjaxResult importDataDetail(MultipartFile file) {
        StringBuffer msg = new StringBuffer("导入失败,请检查文件!");
        StringBuffer successMsg = new StringBuffer("导入成功!");
        Long operatorId = null;

        //获取用户
//        ManagerAccountEntity account = CurrentAccountJwt.get() ; // CurrentAccountSession.get(request);
//        if (account != null) {
//            operatorId = account.getId() ;
//        }else{
//            return AjaxResult.error("没有导入权限!") ;
//        }

        try {
            Workbook wb = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = wb.getSheetAt(0);
            if (sheet == null) {
                return AjaxResult.error("文件不存在!") ;

            }



            //第一行为字段中文名称
            Row row = sheet.getRow(0);

            if (   row.getPhysicalNumberOfCells() != 9
                    || !row.getCell(0).getStringCellValue().equals("数据目录")
                    || !row.getCell(1).getStringCellValue().equals("标识")
                    || !row.getCell(2).getStringCellValue().equals("名称")
                    || !row.getCell(3).getStringCellValue().equals("编码")
                    || !row.getCell(4).getStringCellValue().equals("说明")
                    || !row.getCell(5).getStringCellValue().equals("类型")
                    || !row.getCell(6).getStringCellValue().equals("长度")
                    || !row.getCell(7).getStringCellValue().equals("质量标准")
                    || !row.getCell(8).getStringCellValue().equals("备注")
            ){
                return AjaxResult.error("导入文件的列数、列名和导入模板不一致!请下载导入模板填写数据!");

            };


//            使用importExcel时报错误
//            ImportParams importParams = new ImportParams();
//            importParams.setHeadRows(1);
//            List<DataDetailVO> list1 = ExcelImportUtil.importExcel(file.getInputStream(), DataDetailVO.class, importParams);

            //使用读取原始excel表格的方式获取导入内容
            int lastRowNum = sheet.getLastRowNum();
            List<DataDetailVO> list = new ArrayList<>() ;
            for ( int i=1; i <= lastRowNum ; i++ ) {
                int last = sheet.getRow(i).getLastCellNum() ;
                for ( int j = 0 ; j < last ; j ++ ) {
                    log.debug("{}列单元格类型:{}",j , sheet.getRow(i).getCell(j).getCellType());
                    if ( sheet.getRow(i).getCell(j).getCellType() != CellType.STRING  ) {
                        //如单元格内容不是String,会报Cannot get a STRING value from a NUMERIC cell。此处统一设置为String类型
                        sheet.getRow(i).getCell(j).setCellType(CellType.STRING);
                    }

                }

                DataDetailVO  dataDetailTmp = new DataDetailVO();
                dataDetailTmp.setCataName( sheet.getRow(i).getCell(0).getStringCellValue() );
                dataDetailTmp.setIdentity( sheet.getRow(i).getCell(1).getStringCellValue() );
                dataDetailTmp.setStandardName( sheet.getRow(i).getCell(2).getStringCellValue() );
                dataDetailTmp.setStandardCode(  sheet.getRow(i).getCell(3).getStringCellValue() );
                dataDetailTmp.setStandarDesc( sheet.getRow(i).getCell(4).getStringCellValue() );
                dataDetailTmp.setType( sheet.getRow(i).getCell(5).getStringCellValue() );
                dataDetailTmp.setLength( sheet.getRow(i).getCell(6).getStringCellValue() );
                dataDetailTmp.setQuality( sheet.getRow(i).getCell(7).getStringCellValue() );
                dataDetailTmp.setRemark( sheet.getRow(i).getCell(8).getStringCellValue() );
                list.add(dataDetailTmp) ;
            }


            if (CollUtil.isEmpty(list)) {
                return AjaxResult.error(msg.toString());

            }
            Boolean ifRepeat = false;

            StringBuffer msgRepeat = new StringBuffer();

            // 两个循环(取出重复数据)
            for (int i = 0; i < list.size() - 1; i++) {
                for (int j = list.size() - 1; j > i; j--) {
                    if ( list.get(j).getCataName().equals(list.get(i).getCataName()) && list.get(j).getStandardName().equals(list.get(i).getStandardName())  )
                    {
                        ifRepeat = true;
                        msgRepeat.append("第"+(i+1)+"行与第"+(j+1)+"行的"+list.get(i).getCataName()+"-"+list.get(i).getStandardName()+" 名称存在重复值!请检查文件!要求同一个数据目录下的数据标准名称唯一!");

                    }

                    if ( list.get(j).getCataName().equals(list.get(i).getCataName()) && list.get(j).getStandardCode().equals(list.get(i).getStandardCode())  )
                    {
                        ifRepeat = true;
                        msgRepeat.append("第"+(i+1)+"行与第"+(j+1)+"行的"+list.get(i).getCataName()+"-"+list.get(i).getStandardCode()+" 编码存在重复值!请检查文件!要求同一个数据目录下的数据标准名称编码唯一!");
                    }

                }

            }
            //如果导入文件中，同一个数据目录下的数据标准名称不唯一或者同一个数据目录下的数据标准编码不唯一，则不允许导入
            if(ifRepeat){
                return AjaxResult.error(msgRepeat.toString());

            }


            Integer row_id =1 ;
            List<DataDetailEntity> dataDetailList = new ArrayList<>();
            Boolean loadSuccess = true ;
            for (DataDetailVO vo : list) {

                log.info("开始读取导入文件的数据:{}", JSON.toJSONString(vo));
                DataDetailEntity dataDetail = new DataDetailEntity();

                //数据目录
                String cataName = vo.getCataName();
                if (StrUtil.isBlank(cataName)) {
                    msg.append("第" + row_id.toString() + "行数据目录不能为空!");
                    loadSuccess = false ;

                }
                Map<String, Long> catagoryMap = getDataCatagoryId(cataName,operatorId);
                Long catagoryIdTmp = catagoryMap.get("catagoryID") ;
                Long classifyIdTmp = catagoryMap.get("classifyId") ;



                if ( catagoryIdTmp != null && catagoryIdTmp > 0) {
                    msg.append("第" + row_id.toString() + "行数据目录在系统中不存在!");
                    loadSuccess = false ;

                }
                dataDetail.setCataId(catagoryIdTmp);
                dataDetail.setClassifyId(classifyIdTmp);

                //标识
                dataDetail.setIdentity(vo.getIdentity());

                //数据标准名称
                String standardName = vo.getStandardName();
                if (StrUtil.isBlank(standardName)) {
                    msg.append("第" + row_id.toString() + "行数据标准名称不能为空!");
                    loadSuccess = false ;

                }
                dataDetail.setStandardName(standardName);


                //编码
                String code = vo.getStandardCode();
                if (StrUtil.isBlank(code)) {
                    msg.append("第" + row_id.toString() + "行编码不能为空!");
                    loadSuccess = false ;

                }
                dataDetail.setCode(code);

                //说明
                dataDetail.setStandardDesc(vo.getStandarDesc());

                //类型
                dataDetail.setType(vo.getType());

                //长度
                dataDetail.setLength(vo.getLength());

                //质量标准
                dataDetail.setQuality(vo.getQuality());

                //备注
                dataDetail.setRemark(vo.getRemark());

                //操作员id
                dataDetail.setOperatorId(operatorId);

                ResponseBean checkResult = checkIfExist(dataDetail);

                if ( checkResult.getCode() == 400 ){
                    msg.append("第" + row_id.toString() + "行数据目录："+cataName+" 数据标准名称："+standardName+" 或编码："+code+"已存在!不能重复录入");
                    log.error(msg.toString());
                    loadSuccess = false ;
                }

                dataDetailList.add(dataDetail);
                row_id=row_id+1;
            }
            if ( loadSuccess ) {
                this.saveBatch(dataDetailList);
                return AjaxResult.success(successMsg.toString()) ;
            } else {
                return AjaxResult.error(msg.toString());

            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.error(msg.toString()+e.getMessage());
        }
    }

    @Override
    public ResponseBean checkIfExist(DataDetailEntity dataDetail){

        ResponseBean result = new ResponseBean();



        QueryWrapper<DataDetailEntity> wrapper = new QueryWrapper<>();
        //如果是修改，则查询其他记录，是否有同名
        if ( dataDetail.getId() != null &&  !dataDetail.getId().equals("") )
        {
            wrapper.ne("id", dataDetail.getId());
        }
        wrapper.eq("standard_name", dataDetail.getStandardName());
        wrapper.eq("cata_id", dataDetail.getCataId());
        wrapper.eq("operator_id", dataDetail.getOperatorId());
        List<DataDetailEntity> dataDetailList = this.list(wrapper);
        if ( dataDetailList != null && dataDetailList.size() > 0 )	{
            result.setCode(ResultCodeEnum.FAIL);
            result.setMessage("同一个行业分类、数据目录下，已存在同名的数据标准名称:"+dataDetail.getStandardName());
        } else {
            result.setCode(ResultCodeEnum.SUCCESS);
        }


        QueryWrapper<DataDetailEntity> wrapperCode = new QueryWrapper<>();
        //如果是修改，则查询其他记录，是否有相同的编码
        if ( dataDetail.getId() != null &&  !dataDetail.getId().equals("") )
        {
            wrapperCode.ne("id", dataDetail.getId());
        }
        wrapperCode.eq("code", dataDetail.getCode());
        wrapperCode.eq("cata_id", dataDetail.getCataId());
        wrapperCode.eq("operator_id", dataDetail.getOperatorId());
        List<DataDetailEntity> dataDetailListCode = this.list(wrapperCode);
        if ( dataDetailListCode != null && dataDetailListCode.size() > 0 )	{
            result.setCode(ResultCodeEnum.FAIL);
            result.setMessage("同一个行业分类、数据目录下，已存在相同的编码:"+dataDetail.getCode());
        } else {
            result.setCode(ResultCodeEnum.SUCCESS);
        }

        return result ;


    };
}
