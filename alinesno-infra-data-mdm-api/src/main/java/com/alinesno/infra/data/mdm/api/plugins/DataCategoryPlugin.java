package com.alinesno.infra.data.mdm.api.plugins;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.common.web.adapter.plugins.TranslateCode;
import com.alinesno.infra.common.web.adapter.plugins.TranslatePlugin;
import com.alinesno.infra.data.mdm.entity.BusinessSystemEntity;
import com.alinesno.infra.data.mdm.entity.DataCategoryEntity;
import com.alinesno.infra.data.mdm.entity.IndustryClassifyEntity;
import com.alinesno.infra.data.mdm.service.IBusinessSystemService;
import com.alinesno.infra.data.mdm.service.IDataCategoryService;
import com.alinesno.infra.data.mdm.service.IIndustryClassifyService;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 数据目录转换插件
 *
 * @author luoxiaodong
 * @since 1.0.0
 */
@Component("DataCategoryPlugin")
public class DataCategoryPlugin implements TranslatePlugin {

    @Autowired
    private IBusinessSystemService businessSystemService;

    @Autowired
    private IDataCategoryService dataCategoryService;

    @Autowired
    private IIndustryClassifyService industryClassifyService;

    private final String BUSSINESS_SYSTEM_ID = "scSysId";
    private final String BUSSINESS_SYSTEM_NAME = "scSysName";


    private final String PARENT_CATA_ID   = "parentCataId";
    private final String PARENT_CATE_NAME = "parentCataName";

    private final String CLASSIFY_ID   = "classifyId";
    private final String CLASSIFY_NAME = "classifyName";



    @Override
    public void translate(ArrayNode node, TranslateCode convertCode) {

        if (!node.isEmpty()) {
            //获取来源系统列表
            List<String> scSysIds = this.extractIds(node, BUSSINESS_SYSTEM_ID);
            List<BusinessSystemEntity> businessSystemlist = this.businessSystemService.findByIds(scSysIds);
            Map<String, BusinessSystemEntity> businessSystemMap = this.toEntityMap(businessSystemlist);

            //获取父目录数据源列表
            List<String> parentCataIds = this.extractIds(node, PARENT_CATA_ID);
            List<DataCategoryEntity> dataCategorylist = this.dataCategoryService.findByIds(parentCataIds);
            Map<String, DataCategoryEntity> dataCategoryMap = this.toEntityMap(dataCategorylist);

            //获取行业分类列表
            List<String> classifyIds = this.extractIds(node, CLASSIFY_ID);
            List<IndustryClassifyEntity> classiflist = this.industryClassifyService.findByIds(classifyIds);
            Map<String, IndustryClassifyEntity> classifMap = this.toEntityMap(classiflist);

            //转换逻辑
            node.forEach(jsonObject -> {
                //从返回的列表中获取业务系统id
                String businessSystemID = jsonObject.get(BUSSINESS_SYSTEM_ID).asText();
                ObjectNode rootNode = (ObjectNode) jsonObject;

                //从查找的列表里拿到对应的业务系统
                BusinessSystemEntity businessSystemEntity = businessSystemMap.get(businessSystemID);
                if (businessSystemEntity != null) {
                    //设置返回值
                    rootNode.put(BUSSINESS_SYSTEM_NAME + LABEL_SUFFER, businessSystemEntity.getName());
                }

                //从返回的列表中获取父目录id
                String parentCataId = jsonObject.get(PARENT_CATA_ID).asText();

                //从查找的列表里拿到对应的父目录
                DataCategoryEntity dataCategoryEntity = dataCategoryMap.get(parentCataId);
                if (dataCategoryEntity != null) {
                    //设置返回值
                    rootNode.put(PARENT_CATE_NAME + LABEL_SUFFER, dataCategoryEntity.getCataName());
                }else {

                    //设置返回值
                    rootNode.put(PARENT_CATE_NAME + LABEL_SUFFER, "无");
                }

                //从返回的列表中获取行业分类id
                String classifyId = jsonObject.get(CLASSIFY_ID).asText();

                //从查找的列表里拿到对应的行业分类
                IndustryClassifyEntity classifyEntity = classifMap.get(classifyId);
                if (classifyEntity != null) {
                    //设置返回值
                    rootNode.put(CLASSIFY_NAME + LABEL_SUFFER, classifyEntity.getName());
                }else {

                    //设置返回值
                    rootNode.put(CLASSIFY_NAME + LABEL_SUFFER, "无");
                }


            });
        }
    }
}
