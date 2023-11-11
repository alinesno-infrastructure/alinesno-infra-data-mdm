package com.alinesno.infra.data.mdm.api.plugins;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.common.web.adapter.plugins.TranslateCode;
import com.alinesno.infra.common.web.adapter.plugins.TranslatePlugin;
import com.alinesno.infra.data.mdm.entity.DataCategoryEntity;
import com.alinesno.infra.data.mdm.entity.IndustryClassifyEntity;
import com.alinesno.infra.data.mdm.enums.HasStatusEnum;
import com.alinesno.infra.data.mdm.service.IDataCategoryService;
import com.alinesno.infra.data.mdm.service.IIndustryClassifyService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 数据标准转换插件
 *
 * @author luoxiaodong
 * @since 1.0.0
 */
@Component("DataDetailPlugin")
public class DataDetailPlugin implements TranslatePlugin {

    @Autowired
    private IDataCategoryService dataCategoryService;

    private final String DATACATEGORY_ID   = "cataId";
    private final String DATACATEGORY_NAME = "cataName";

    @Autowired
    private IIndustryClassifyService industryClassifyService;

    private final String CLASSIFY_ID   = "classifyId";
    private final String CLASSIFY_NAME = "classifyName";

    private final String HAS_STATUS   = "hasStatus";
    private final String STATUS_NAME  = "status";

    private Map<String, DataCategoryEntity> cateMap = null ;

    private Map<String, IndustryClassifyEntity> classifyMap = null ;


    @Override
    public void translate(ArrayNode node, TranslateCode convertCode) {


        //获取数据目录列表
        List<String> cataIds = this.extractIds(node, DATACATEGORY_ID);
        if( !CollectionUtils.isEmpty(cataIds) ) {
			List<DataCategoryEntity> catalist = this.dataCategoryService.findByIds(cataIds);
            cateMap = this.toEntityMap(catalist);
        }

        //获取行业分类列表
        List<String> classifyIds = this.extractIds(node, CLASSIFY_ID);
        List<IndustryClassifyEntity> classifylist = this.industryClassifyService.findByIds(classifyIds);
        classifyMap = this.toEntityMap(classifylist);

        //转换逻辑
        node.forEach(jsonObject -> {

            //从返回的列表中获取数据目录id
            String cataId = jsonObject.get(DATACATEGORY_ID).asText();
            ObjectNode rootNode = (ObjectNode) jsonObject;

            //从查找的列表里拿到对应的取数据目录
            if(cateMap != null) {
                DataCategoryEntity dataCategoryEntity = cateMap.get(cataId);
				if (dataCategoryEntity != null) {
					//设置返回值
                    rootNode.put(DATACATEGORY_NAME + LABEL_SUFFER, dataCategoryEntity.getCataName());
				}else {

                    //设置返回值
                    rootNode.put(DATACATEGORY_NAME + LABEL_SUFFER, "无");
                }
            }

            //从返回的列表中获取行业分类id
            String classifyId = jsonObject.get(CLASSIFY_ID).asText();

            //从查找的列表里拿到对应的行业分类
            if(classifyMap != null) {
                IndustryClassifyEntity industryClassifyEntity = classifyMap.get(classifyId);
                if (industryClassifyEntity != null) {
                    //设置返回值
                    rootNode.put(CLASSIFY_NAME + LABEL_SUFFER, industryClassifyEntity.getName());
                }else {

                    //设置返回值
                    rootNode.put(CLASSIFY_NAME + LABEL_SUFFER, "无");
                }
            }

            rootNode.put(STATUS_NAME + LABEL_SUFFER, HasStatusEnum.getEnumDesc(jsonObject.get(HAS_STATUS).asInt()));

        });

    }
}
