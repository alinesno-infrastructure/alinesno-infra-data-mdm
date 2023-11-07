package com.alinesno.infra.data.mdm.api.plugins;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.common.web.adapter.plugins.TranslateCode;
import com.alinesno.infra.common.web.adapter.plugins.TranslatePlugin;
import com.alinesno.infra.data.mdm.entity.IndustryClassifyEntity;
import com.alinesno.infra.data.mdm.service.IIndustryClassifyService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 行业分类转换插件
 *
 * @author luoxiaodong
 * @since 1.0.0
 */
@Component("IndustryClassifyPlugin")
public class IndustryClassifyPlugin implements TranslatePlugin {

    private final String PARENTID = "parentId";
    private final String PARENTNAME = "parentName";

    @Autowired
    private IIndustryClassifyService industryClassifyService;

    private Map<String, IndustryClassifyEntity> industryClassifyMap = null ;

    @Override
    public void translate(List<JSONObject> node, TranslateCode convertCode) {


        //获取数据目录列表
        List<String> parentIds = this.extractIds(node, PARENTID);
        if(!CollectionUtils.isEmpty(parentIds)) {
			List<IndustryClassifyEntity> industryClassifylist = this.industryClassifyService.findByIds(parentIds);
            industryClassifyMap = this.toEntityMap(industryClassifylist, node);
        }

        //转换逻辑
        node.forEach(jsonObject -> {

            //从返回的列表中获取行业分类id
            String parentId = jsonObject.getString(PARENTID);
            //从查找的列表里拿到对应的取数据目录
            if(industryClassifyMap != null) {
                IndustryClassifyEntity industryClassify = industryClassifyMap.get(parentId);
				if (industryClassify != null) {
					//设置返回值
					jsonObject.put(PARENTNAME + LABEL_SUFFER, industryClassify.getName());
				}
                else {

                    //设置返回值
                    jsonObject.put(PARENTNAME + LABEL_SUFFER, "无");
                }
            }

        });

    }
}
