<template>
  <!--
    【数据目录】 功能列表

    @author luoxiaodong
    @since 1.0.0
  -->
  <div class="app-container">
    <el-row>
      <el-col :span="5" >
        <span>数据目录</span>
        <el-tree :data="DataCatagoryList_tree" :props="defaultProps" @node-click="handleNodeClick"
                 :expand-on-click-node="false" :defaultExpandAll="true"
        ></el-tree>
      </el-col>
      <el-col :span="19" ><div class="grid-content bg-purple-light" width="1850px"></div>
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px"  @submit.native.prevent>
          <el-form-item label="目录名称" prop="cataName">
            <el-input
              v-model="queryParams.cataName"
              ref="queryParams.cataName"
              placeholder="请输入目录名称"
              clearable
              
              wrapper="eq"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" icon="Search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="Plus"
              size="mini"
              @click="handleAdd"
            >新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              plain
              icon="Edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
            >修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="Delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
            >删除</el-button>
          </el-col>
<!--          <el-col :span="1.5">-->
<!--            <el-button-->
<!--              type="warning"-->
<!--              plain-->
<!--              icon="el-icon-download"-->
<!--              size="mini"-->
<!--              @click="export2Excel"-->
<!--            >导出</el-button>-->
<!--          </el-col>-->
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="DataCatagoryList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="目录名称" align="left" prop="cataName" />
          <el-table-column label="目录标识" align="left" prop="identity" />
          <el-table-column label="行业分类" align="left" prop="classifyNameLabel"   />
          <el-table-column label="父目录" align="left" prop="parentCataNameLabel" />
          <el-table-column label="操作" width="230" align="center" class-name="small-padding fixed-width">
            <template  #default="scope">
              <el-button
                size="mini"
                type="text"
                icon="Edit"
                @click="handleUpdate(scope.row)"
              >修改</el-button>
              <el-button
                size="mini"
                type="text"
                icon="Delete"
                @click="handleDelete(scope.row)"
              >删除</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-view"
                @click="handleDesc(scope.row)"
              >详情</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
            v-show="total > 0"
            :total="total"
            v-model:current-page="queryParams.pageNum"
            v-model:page-size="queryParams.pageSize"
            @pagination="getList"
        />
    </el-col>
    </el-row>


    <!-- 添加或修改【数据目录】对话框 -->
    <el-dialog :title="title" v-model="open" width="480px" append-to-body :close-on-click-modal="false">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="行业分类" prop="classifyId"  >
          <el-select clearable v-model="form.classifyId" placeholder="请选择行业分类"  style="width:360px" filterable>
            <el-option  v-for="item in industryClassifyList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="父目录" prop="parentCataId">
          <el-select clearable v-model="form.parentCataId" placeholder="请选择父目录" style="width:360px" filterable>
            <el-option  v-for="item in DataCatagoryList_all"
                        :key="item.id"
                        :label="item.cataName"
                        :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="目录标识" prop="identity">
          <el-input v-model="form.identity" placeholder="请输入目录标识" maxlength="36" show-word-limit/>
        </el-form-item>
        <el-form-item label="目录名称" prop="cataName">
          <el-input v-model="form.cataName" placeholder="请输入目录名称" maxlength="200" show-word-limit/>
        </el-form-item>
        <el-form-item label="命名规范" prop="namingConvention">
          <el-input v-model="form.namingConvention" placeholder="请输入命名规范" maxlength="200" show-word-limit/>
        </el-form-item>

        <el-form-item label="来源系统" prop="scSysId"  >
          <el-select clearable v-model="form.scSysId" placeholder="请选择来源系统"  style="width:360px"  filterable>
            <el-option  v-for="item in businessSystemList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :autosize="{minRows:3}" placeholder="请输入内容" maxlength="512" show-word-limit/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 数据目录详情弹出框 -->
    <el-dialog title="数据目录详情" v-model="detailOpen"   width="540px" append-to-body :close-on-click-modal="false">
      <el-form ref="formDesc" :model="detailDscForm"   label-width="80px">
        <el-form-item label="行业分类" prop="classifyNameLabel" >
          <el-input v-model="detailDscForm.classifyNameLabel" type="textarea" autosize  readonly: true placeholder="请选择行业分类" />
        </el-form-item>
        <el-form-item label="父目录" prop="parentCataNameLabel" >
          <el-input v-model="detailDscForm.parentCataNameLabel"  readonly: true placeholder="请选择父目录" />
        </el-form-item>
        <el-form-item label="标识" prop="identity" >
          <el-input v-model="detailDscForm.identity" readonly: true placeholder="请输入目录标识" />
        </el-form-item>
        <el-form-item label="目录名称" prop="cataName" >
          <el-input v-model="detailDscForm.cataName"  readonly: true placeholder="请输入目录名称"/>
        </el-form-item>
        <el-form-item label="命名规范" prop="namingConvention" >
          <el-input v-model="detailDscForm.namingConvention"  readonly: true placeholder="请输入命名规范"/>
        </el-form-item>
        <el-form-item label="来源系统" prop="scSysNameLabel" >
          <el-input v-model="detailDscForm.scSysNameLabel"  readonly: true placeholder="请选择来源系统" />
        </el-form-item>
        <el-form-item label="备注" prop="remark" >
          <el-input v-model="detailDscForm.remark" type="textarea" autosize  readonly: true placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="添加时间" prop="addTime" >
          <el-input v-model="detailDscForm.addTime"  type="textarea" autosize readonly: true  />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailCancel">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script   setup name="DataCatagory">
import { ref, reactive, onMounted} from 'vue';
import { useRouter, useRoute } from 'vue-router';

import {
  listDataCatagory,
  getDataCatagory,
  delDataCatagory,
  addDataCatagory,
  changeDataCatagoryField,
  changeStatusDataCatagory,
  updateDataCatagory,
  exportDataCatagory,listTreeNavInfo,checkDataCatagoryIfExist,checkIfUsed } from "@/api/mdm/DataCatagory";
import {listBusinessSystem}  from "@/api/mdm/BusinessSystem";
import  Condition  from "@/api/Search/Condition";
import  searchParam  from "@/api/Search/searchform";
import {listIndustryClassify} from "@/api/mdm/IndustryClassify";

const router=useRouter() ;
const route=useRoute()   ;
const { proxy } = getCurrentInstance();

// 遮罩层
const loading = ref(true);

// 选中数组
const names = ref([]);

// 选中数组
const ids = ref([]);

const detailOpen = ref(false);

// 非单个禁用
const single = ref(true);

// 非多个禁用
const multiple = ref(true);

// 状态
const statusOptions = ref([]);

// 显示搜索条件
const showSearch = ref(true);
// 搜索参数
const searchParams = ref([]);

// 【数据目录】表格数据
const DataCatagoryList = ref([]);

//业务系统下拉框功能
const businessSystemList = ref([]);

const industryClassifyList = ref([]);

const DataCatagoryList_all = ref([]);

const DataCatagoryList_tree = ref([]);

const searchParamTem = ref([]);

//导出清单
const exportDataCatagoryList = ref([]);

// 弹出层标题
const title = ref( "");

// 是否显示弹出层
const open = ref(false);

const data = reactive({
    total:0,

    defaultProps: {
      children: 'children',
      label: 'cataName'
    },

    // 查询参数
    queryParams: {
      pageNum: 1,
      pageSize: 10,
      identity: null,
      cataName: null,
      namingConvention: null,
      scSysId: null,
      classifyId: null,
      parentCataId: null
    },

    //搜索参数字段类型配置，配置对象的key必须和搜索字段同名
    // 未做配置定义的字段将会原样输出到后端，通常分页参数不需要配置
    // Condition 对象封装了快捷函数直接生成匹配规则，需要import后使用
    queryParamsConfig: {
      identity: Condition.like(),
      cataName: Condition.like(),
      namingConvention: Condition.like(),
      scSysId: Condition.eq(),
      classifyId: Condition.eq(),
      parentCataId: Condition.eq()
    },

    // 表单参数
    form: {},

    detailDscForm : {},

    // 表单校验
    rules: {
      identity: [
        { required: true, message: "目录标识不能为空", trigger: "blur" }
      ],
      cataName: [
        { required: true, message: "目录名称不能为空", trigger: "blur" }
      ],
      namingConvention: [
        { required: false, message: "命名规范不能为空", trigger: "blur" }
      ],
      scSysId: [
        { required: false, message: "来源系统不能为空", trigger: "blur" }
      ],
      classifyId: [
        { required: false, message: "行业分类不能为空", trigger: "blur" }
      ],
      parentCataId: [
        { required: false, message: "父目录不能为空", trigger: "blur" }
      ],
      remark: [
        { required: false, message: "备注不能为空", trigger: "blur" }
      ]
    },

    Params: {
      pageNum : 1,
      pageSize: 1000,
      hasStatus:  0
    },

    ParamsConfig:{
      hasStatus:Condition.eq()
    },

    // 查询参数
    exportqueryParams: {
      pageNum: 1,
      pageSize: 100000,
      identity: null,
      cataName: null,
      namingConvention: null,
      scSysId: null,
      classifyId: null,
      parentCataId: null
    }

});


const { total, defaultProps, queryParams,   queryParamsConfig, form, detailDscForm, rules, Params, ParamsConfig, exportqueryParams  } = toRefs(data);


// 页面加载后触发
onMounted(() => {
  initSearchParamTem();
  getBusinessSystemList();        //增加系统系统下拉框功能
  getDataCatagoryUI();
  getIndustryClassify();
})


function  initSearchParamTem(){
    searchParamTem.value = searchParam(ParamsConfig.value, Params.value);
}

function getIndustryClassify(){
  listIndustryClassify(searchParamTem.value).then(response => {
    industryClassifyList.value = response.rows;
  });
}

function getDataCatagoryUI(){
  listDataCatagory(searchParamTem.value).then(response => {
    DataCatagoryList_all.value = response.rows;
    DataCatagoryList_tree.value = proxy.handleTree(response.rows,"id","parentCataId" ) ;
  });
}

/** 查询【数据目录】列表 */
function getList() {
  loading.value = true;
  // 使用全局的搜索参数处理方法预处理
  searchParams.value = searchParam(queryParamsConfig.value, queryParams.value);
  listDataCatagory(searchParams.value).then(response => {
    total.value = response.total;
    DataCatagoryList.value = response.rows;
    DataCatagoryList_all.value = response.rows;
    DataCatagoryList_tree.value = proxy.handleTree(response.rows,"id","parentCataId" ) ;
    loading.value = false;
  });
}

//增加系统系统下拉框功能
function getBusinessSystemList() {
  listBusinessSystem(searchParamTem.value).then(response => {
    businessSystemList.value = response.rows;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    standardId: null,
    identity: null,
    cataName: null,
    namingConvention: null,
    scSysId: null,
    classifyId: null,
    parentCataId: null,
    remark: null
  };
  proxy.resetForm("formRef");
  getDataCatagoryUI();
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryForm");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id) ;
  names.value = selection.map(item => item.cataName) ;
  single.value = selection.length!==1 ;
  multiple.value = !selection.length ;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加【数据目录】";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  // reset();
  const identity = row.id || ids.value
  getDataCatagory(identity).then(response => {
    if (response.data) {
      form.value = response.data;
      open.value = true;
      title.value = "修改【数据目录】";
    } else {
      proxy.$modal.msgError("没有找到需要修改的数据!");
    }
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["formRef"].validate(valid => {
    if (valid) {
      checkDataCatagoryIfExist(form.value).then( res => {
        if ( res.code == 200 ) {
            if ( form.value.id != null ) {
              updateDataCatagory(form.value).then(response => {
                proxy.$modal.msgSuccess("修改成功");
                open.value = false;
                getList();
              });
            } else {
              addDataCatagory(form.value).then(response => {
                proxy.$modal.msgSuccess("新增成功");
                open.value = false;
                getList();
              });
            }
        } else {
          proxy.$modal.$message.error(res.msg)
        }

      });
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
    const identitys = row.id || ids.value;
    let cataNames = row.cataName || names.value;
    //避免弹出窗数据太长，只显示前15条数据
    if ( cataNames.length > 15 ) {
      cataNames = cataNames.slice(0,15);
    }

    checkIfUsed(identitys).then(res => {
      if ( res.code == 200 ) {
        proxy.$modal.$confirm('是否确认删除【数据目录】名称为"' + cataNames + '"的数据项?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function() {
            return delDataCatagory(identitys);
          }).then(() => {
              getList();
              proxy.$modal.msgSuccess("删除成功");
          }).catch(error => {
              proxy.$modal.msgError("没有找到需要删除的数据!");
        })
      } else {
        proxy.$modal.$message.error(res.msg)
      }
    }).catch(error => {
      proxy.$modal.msgError("没有找到需要删除的数据!");
    })
}

/** 状态修改 **/
function handleStatusChange(row) {
    return changeStatusDataCatagory(row.id, row.status).then(response=>{
      if(response.code == 200){
        proxy.$modal.msgSuccess("操作成功");
      }
    });
}

/** 修改字段状态 **/
function chanageFile(value , filed , id){
  return changeDataCatagoryField(value , filed , id).then(response =>{
    if(response.code == 200){
      proxy.$modal.msgSuccess("操作成功");
    }
  }) ;
}

/** 导出按钮操作 */
function handleExport() {
  const queryParams = queryParams.value;
  proxy.$modal.$confirm('是否确认导出所有【数据目录】数据项?', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }).then(function() {
      return exportDataCatagory(queryParams);
    }).then(response => {
      download(response.msg);
    })
}

function export2Excel() {
  searchParams.value = searchParam(queryParamsConfig.value, exportqueryParams.value);
  loading.value = true;
  listDataCatagory(searchParams.value).then(response => {
    exportDataCatagoryList.value =  response.rows  ;
    loading.value = false;
    require.ensure([], () => {
      const { export_json_to_excel } = require('@/excel/Export2Excel');
      const tHeader = ['目录名称', '目录标识', '命名规范', '来源系统', '父目录', '行业分类', '添加时间'];
      // 上面设置Excel的表格第一行的标题
      const filterVal = ['cataName', 'identity', 'namingConvention','scSysNameLabel','parentCataNameLabel','classifyNameLabel','addTime'];
      // 上面的index、phone_Num、school_Name是tableData里对象的属性
      const list = exportDataCatagoryList.value;  //把data里的tableData存到list
      const data = formatJson(filterVal, list);
      export_json_to_excel(tHeader, data, '数据目录列表');
    })
  }).catch(loading.value = false);

}

function formatJson(filterVal, jsonData) {
   return jsonData.map(v => filterVal.map(j => v[j]))
}

function handleNodeClick(node) {
  debugger

  let treeIdArrs = [] ;
  // 获取当前节点信息
  treeIdArrs.push(node.id)
  // 遍历当前节点的所有子节点
  if (node.children) {
    for (let i = 0; i < node.children.length; i++) {
      treeIdArrs.push(node.children[i].id)

    }

  }

  searchParams.value = searchParam(queryParamsConfig.value, queryParams.value);
  loading.value = true;
  listTreeNavInfo(treeIdArrs, searchParams.value.pageNum, searchParams.value.pageSize, JSON.stringify(searchParams.value)).then(res => {
    debugger
    DataCatagoryList.value = res.rows;
    loading.value = false;
  })
}

/** 详情按钮操作 */
function handleDesc(row) {
    detailDscForm.value.cataName = row.cataName;
    detailDscForm.value.identity = row.identity;
    detailDscForm.value.namingConvention = row.namingConvention;
    detailDscForm.value.scSysNameLabel = row.scSysNameLabel;
    detailDscForm.value.classifyNameLabel = row.classifyNameLabel;
    detailDscForm.value.parentCataNameLabel = row.parentCataNameLabel;
    detailDscForm.value.remark = row.remark;
    detailDscForm.value.addTime = row.updateTime;
    detailOpen.value = true;
}

function detailCancel()
{
  detailOpen.value = false ;
}


/** created */

getList();
// 查询公共状态
// proxy.getDicts("has_status").then(response => {
//   statusOptions.value = response.data;
// });

/** created */
</script>
