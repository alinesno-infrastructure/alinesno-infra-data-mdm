<template>
  <!--
    【目录历史】 功能列表

    @author luoxiaodong
    @since 1.0.0
  -->
  <div class="app-container">
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
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="export2Excel"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="DataChangeLogList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="目录标识" align="left" prop="identity" />
      <el-table-column label="目录名称" align="left" prop="cataName"  width="300"/>
      <el-table-column label="命名规范" align="left" prop="namingConvention" />
      <el-table-column label="来源系统" align="left" prop="scSysNameLabel"  />
      <el-table-column label="行业分类" align="left" prop="classifyNameLabel"  />
      <el-table-column label="父目录" align="left" prop="parentCataNameLabel" width="200"/>
      <el-table-column label="备注" align="left" prop="remark" />
      <el-table-column label="添加时间" align="left" prop="addTime" :width=150 >
        <template  #default="scope">
          <span>{{ parseTime(scope.row.addTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="80" align="center" class-name="small-padding fixed-width">
        <template  #default="scope">
          <el-button
            size="mini"
            type="text"
            icon="Delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
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


  </div>
</template>

<script   setup name="DataChangeLog">
import { ref, reactive, onMounted} from 'vue';
import { useRouter, useRoute } from 'vue-router';

import {
  listDataChangeLog,
  getDataChangeLog,
  delDataChangeLog,
  addDataChangeLog,
  changeDataChangeLogField,
  changeStatusDataChangeLog,
  updateDataChangeLog,
  exportDataChangeLog } from "@/api/mdm/DataChangeLog";
import {listBusinessSystem}  from "@/api/mdm/BusinessSystem";
import  Condition  from "@/api/Search/Condition";
import  searchParam  from "@/api/Search/searchform";
import {listDataCatagory} from "@/api/mdm/DataCatagory";
import {parseTime} from "@/utils/ruoyi";
const router=useRouter() ;
const route=useRoute()   ;
const { proxy } = getCurrentInstance();


// 遮罩层
const loading = ref(true);

// 非单个禁用
const single = ref(true);

// 非多个禁用
const multiple = ref(true);

// 显示搜索条件
const showSearch = ref(true);

// 是否显示弹出层
const open = ref(false);

// 弹出层标题
const title = ref( "");

// 选中数组
const names = ref([]);

// 选中数组
const ids = ref([]);

// 状态
const statusOptions = ref([]);

// 【目录历史】表格数据
const DataChangeLogList = ref([]);

//增加业务系统下拉框功能
const businessSystemList = ref([]);

//主数据目录清单
const DataCatagoryList = ref([]);

// 搜索参数
const searchParams = ref([]);

const searchParamTem = ref([]);

//导出清单
const exportDataChangeLogList = ref([]);

const data = reactive({
    total:0,
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

    // 表单校验
    rules: {
      identity: [
        { required: false, message: "目录标识不能为空", trigger: "blur" }
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

const { total ,queryParams, queryParamsConfig, form, rules, Params, ParamsConfig, exportqueryParams  } = toRefs(data);

// 页面加载后触发
onMounted(() => {
  initSearchParamTem();

  //增加系统系统下拉框功能
  getBusinessSystemList();

  //获取主数据目录清单下拉框功能
  getDataCatagoryList();
})


function initSearchParamTem(){
  searchParamTem.value =  searchParam(ParamsConfig.value, Params.value);
}

/** 查询【目录历史】列表 */
function getList() {
  loading.value = true;
  // 使用全局的搜索参数处理方法预处理
  searchParams.value = searchParam(queryParamsConfig.value, queryParams.value);
  listDataChangeLog(searchParams.value).then(response => {
    total.value = response.total;
    DataChangeLogList.value = response.rows;
    loading.value = false;
  });
}

//增加主数据目录下拉框功能
function getDataCatagoryList() {
  listDataCatagory(searchParamTem.value).then(response => {
    DataCatagoryList.value = response.rows;
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
  cataEnname: null,
  cataName: null,
  namingConvention: null,
  version: null,
  scSysId: null,
  cateId: null,
  remark: null
};
this.resetForm("form");
}

/** 搜索按钮操作 */
function handleQuery() {
// 获取参数
//this.searchParams = this.searchParam(this, this.$refs.queryParams, this.queryParams);

this.queryParams.pageNum = 1;
this.getList();
}

/** 重置按钮操作 */
function resetQuery() {
this.resetForm("queryForm");
this.handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
this.ids = selection.map(item => item.id)
this.names = selection.map(item => item.cataName)
this.single = selection.length!==1
this.multiple = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
this.reset();
this.open = true;
this.title = "添加【目录历史】";
}

/** 修改按钮操作 */
function handleUpdate(row) {
this.reset();
const cataEnname = row.id || this.ids
getDataChangeLog(cataEnname).then(response => {
  this.form = response.data;
  this.open = true;
  this.title = "修改【目录历史】";
});
}

/** 提交按钮 */
function submitForm() {
    this.$refs["form"].validate(valid => {
      if (valid) {
        if (form.vule.id != null) {
          updateDataChangeLog(form.vule).then(response => {
            proxy.$modal.msgSuccess("修改成功");
            open.vule = false;
            getList();
          });
        } else {
          addDataChangeLog(this.form.vule).then(response => {
            proxy.$modal.msgSuccess("新增成功");
            open.vule = false;
            getList();
          });
        }
      }
    });
}

/** 删除按钮操作 */
function handleDelete(row) {
    const cataEnnames = row.id || ids.value;
    let cataNames = row.cataName || names.value;
    //避免弹出窗数据太长，只显示前15条数据
    if ( cataNames.length > 15 ) {
      cataNames = cataNames.slice(0,15);
    }

    proxy.$modal.$confirm('是否确认删除【目录历史】名称为"' + cataNames + '"的数据项?', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }).then(function() {
      return delDataChangeLog(cataEnnames);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(error => {
      proxy.$modal.msgError("没有找到需要删除的数据!");
    })
}

/** 状态修改 **/
function handleStatusChange(row) {
  return changeStatusDataChangeLog(row.id, row.status).then(response=>{
    if(response.code == 200){
      proxy.$modal.msgSuccess("操作成功");
    }
  });
}

/** 修改字段状态 **/
function chanageFile(value , filed , id){
  return changeDataChangeLogField(value , filed , id).then(response =>{
    if(response.code == 200){
      proxy.$modal.msgSuccess("操作成功");
    }
  }) ;
}

/** 导出按钮操作 */
function handleExport() {
  const queryParams = this.queryParams;
    proxy.$modal.$confirm('是否确认导出所有【目录历史】数据项?', "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(function() {
    return exportDataChangeLog(queryParams);
  }).then(response => {
    download(response.msg);
  })
}

function export2Excel() {
  searchParams.value = this.searchParam(queryParamsConfig.value, exportqueryParams.value);
  loading.value = true;
  listDataChangeLog(searchParams.value).then(response => {
    exportDataChangeLogList.value = response.rows;
    loading.value = false;
    require.ensure([], () => {
      const { export_json_to_excel } = require('@/excel/Export2Excel');
      const tHeader = ['目录名称', '目录标识', '命名规范', '来源系统', '父目录', '行业分类', '添加时间'];
      // 上面设置Excel的表格第一行的标题
      const filterVal = ['cataName', 'identity', 'namingConvention','scSysNameLabel','parentCataNameLabel','classifyNameLabel','addTime'];
      // 上面的index、phone_Num、school_Name是tableData里对象的属性
      const list = exportDataChangeLogList.value;  //把data里的tableData存到list
      const data = formatJson(filterVal, list);
      export_json_to_excel(tHeader, data, '数据目录历史列表');
    })
  }).catch(loading.value= false);

}

function formatJson(filterVal, jsonData) {
   return jsonData.map(v => filterVal.map(j => v[j]))
}

/** created */

  getList();
// 查询公共状态
// proxy.getDicts("has_status").then(response => {
//   statusOptions.value = response.data;
// });

/** created */


</script>
