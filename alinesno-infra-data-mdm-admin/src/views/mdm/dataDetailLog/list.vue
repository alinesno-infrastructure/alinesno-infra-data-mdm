<template>
  <!--
    【主数据历史】 功能列表

    @author luoxiaodong
    @since 1.0.0
  -->
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="名称" prop="standardName" >
        <el-input
          v-model="queryParams.standardName"
          ref="queryParams.standardName"
          placeholder="请输入数据标准名称"
          clearable
          
          wrapper="eq"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="代码" prop="code" >
        <el-input
          v-model="queryParams.code"
          ref="queryParams.code"
          placeholder="请输入代码"
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

    <el-table v-loading="loading" :data="DataDetailLogList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="行业分类" align="left" prop="classifyNameLabel"  :sortable="'custom'"     />
      <el-table-column label="数据目录" align="left" prop="cataNameLabel"  :sortable="'custom'"/>
      <el-table-column label="名称" align="left" prop="standardName"    :sortable="'custom'"    />
      <el-table-column label="标识" align="left" prop="identity"  :sortable="'custom'"   />
      <el-table-column label="编码" align="left" prop="code"  :sortable="'custom'"   />
      <el-table-column label="添加时间" align="center" prop="addTime" :width=150>
        <template  #default="scope">
          <span>{{ parseTime(scope.row.addTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100" align="center" class-name="small-padding fixed-width">
        <template  #default="scope">
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

    <!-- 数据标准详情弹出框 -->
    <el-dialog title="数据标准详情" v-model="open" width="540px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="detailDscForm"   label-width="80px">
        <el-form-item label="行业分类" prop="classifyNameLabel" >
          <el-input v-model="detailDscForm.classifyNameLabel"  readonly: true />
        </el-form-item>
        <el-form-item label="数据目录" prop="cataNameLabel" >
          <el-input v-model="detailDscForm.cataNameLabel"   readonly: true/>
        </el-form-item>
        <el-form-item label="标识" prop="identity" >
          <el-input v-model="detailDscForm.identity" readonly: true />
        </el-form-item>
        <el-form-item label="名称" prop="standardName" >
          <el-input v-model="detailDscForm.standardName"  readonly: true />
        </el-form-item>
        <el-form-item label="编码" prop="code" >
          <el-input v-model="detailDscForm.code"  readonly: true />
        </el-form-item>
        <el-form-item label="说明" prop="standarDesc" >
          <el-input v-model="detailDscForm.standarDesc" type="textarea" autosize  readonly: true placeholder="请输入数据标准的说明，如 上年度职工月平均工资" />
        </el-form-item>
        <el-form-item label="类型" prop="type" >
          <el-input v-model="detailDscForm.type"  readonly: true placeholder="请输入数据标准的数据类型，如 浮点型" />
        </el-form-item>
        <el-form-item label="长度" prop="length" >
          <el-input v-model="detailDscForm.length"  readonly: true placeholder="请输入数据标准的数据长度，如 10,2" />
        </el-form-item>
        <el-form-item label="质量标准" prop="quality" >
          <el-input v-model="detailDscForm.quality"  type="textarea" autosize readonly: true placeholder="请输入数据标准的质量标准，如 应大于0" />
        </el-form-item>
        <el-form-item label="备注" prop="remark" >
          <el-input v-model="detailDscForm.remark" type="textarea" autosize readonly: true placeholder="请输入数据标准的质量标准，如 应大于0" />
        </el-form-item>
        <el-form-item label="状态" prop="statusLabel" :width=80>
          <el-input v-model="detailDscForm.statusLabel"  readonly: true />
        </el-form-item>
        <el-form-item label="添加时间" prop="addTime" :width=150 >
          <el-input v-model="detailDscForm.addTime"  readonly: true />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="detailCancel">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script   setup name="DataDetailLog">
import { ref, reactive, onMounted} from 'vue';
import { useRouter, useRoute } from 'vue-router';
import {parseTime} from "@/utils/ruoyi";
import {
  listDataDetailLog,
  getDataDetailLog,
  delDataDetailLog,
  addDataDetailLog,
  changeDataDetailLogField,
  changeStatusDataDetailLog,
  updateDataDetailLog,
  exportDataDetailLog } from "@/api/mdm/DataDetailLog";
import  Condition  from "@/api/Search/Condition";
import  searchParam  from "@/api/Search/searchform";

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

const detailOpen = ref(false);

// 弹出层标题
const title = ref("");

// 状态
const statusOptions = ref([]);

// 选中数组
const names = ref([]);

// 选中数组
const ids = ref([]);

// 【主数据历史】表格数据
const DataDetailLogList = ref([]);


//导出清单
const exportDataDetailLogList = ref([]);

// 搜索参数
const searchParams = ref([]);

const data = reactive({
    total:0,
    // 查询参数
    queryParams: {
      pageNum: 1,
      pageSize: 10,
      identity: null,
      shortName: null,
      standardName: null,
      code: null,
      type: null,
      length: null,
      standarDesc: null,
      quality: null,
      remark: null,
      classifyId: null,
      cataId: null
    },

    // 查询参数配置对象
    queryParamsConfig: {
      identity: Condition.like(),
      shortName: Condition.like(),
      standardName: Condition.like(),
      code: Condition.like(),
      type: Condition.like(),
      length: Condition.like(),
      standarDesc: Condition.like(),
      quality: Condition.like(),
      remark: Condition.like(),
      classifyId: Condition.eq(),
      cataId: Condition.eq()
    },

    // 表单参数
    form: {},

    // 表单校验
    rules: {
      identity: [
        { required: true, message: "标识不能为空", trigger: "blur" }
      ],
      shortName: [
        { required: false, message: "简称不能为空", trigger: "blur" }
      ],
      standardName: [
        { required: true, message: "名称不能为空", trigger: "blur" }
      ],
      code: [
        { required: true, message: "代码不能为空", trigger: "blur" }
      ],
      type: [
        { required: false, message: "数据类型不能为空", trigger: "blur" }
      ],
      length: [
        { required: false, message: "数据长度不能为空", trigger: "blur" }
      ],
      standarDesc: [
        { required: false, message: "描述不能为空", trigger: "blur" }
      ],
      quality: [
        { required: false, message: "质量规范不能为空", trigger: "blur" }
      ],
      remark: [
        { required: false, message: "备注不能为空", trigger: "blur" }
      ],
      classifyId: [
        { required: true, message: "行业分类不能为空", trigger: "blur" }
      ],
      cataId: [
        { required: true, message: "数据目录不能为空", trigger: "blur" }
      ]
    },

    // 查询参数
    exportqueryParams: {
      pageNum: 1,
      pageSize: 100000,
      identity: null,
      shortName: null,
      name: null,
      namingCode: null,
      type: null,
      length: null,
      desc: null,
      quality: null,
      remark: null,
      classifyId: null,
      cataId: null
    },

    detailDscForm : {}
});

const { total,queryParams, queryParamsConfig, form, rules, exportqueryParams, detailDscForm  } = toRefs(data);

// 页面加载后触发
onMounted(() => {

})


/** 查询【主数据历史】列表 */
function getList() {
  searchParams.value = searchParam(queryParamsConfig.value, queryParams.value);
  loading.value = true;
  listDataDetailLog(searchParams.value).then(response => {
    total.value = response.total;
    DataDetailLogList.value = response.rows;
    loading.value = false;
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
    declarationId: null,
    enname: null,
    name: null,
    namingCode: null,
    releaseVersion: null,
    scSysId: null,
    scTable: null,
    scTableField: null,
    hasPublish: null,
    remark: null,
    standardId: null,
    cataId: null
  };
  proxy.resetForm("form");
}

/** 搜索按钮操作 */
function handleQuery() {
  // 获取参数
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
   names.value = selection.map(item => item.standardName);
   single.value = selection.length!==1;
   multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
    reset();
    open.value = true;
    title.value = "添加【主数据历史】";
}

/** 修改按钮操作 */
function  handleUpdate(row) {
    // reset();
    const declarationId = row.id || ids.value
    getDataDetailLog(declarationId).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = "修改【主数据历史】";
    });
}

/** 提交按钮 */
function  submitForm() {
    proxy.$refs["form"].validate(valid => {
      if (valid) {
        if (form.value.id != null) {
          updateDataDetailLog(form.value).then(response => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          });
        } else {
          addDataDetailLog(form.value).then(response => {
            proxy.$modal.msgSuccess("新增成功");
            open.value = false;
            getList();
          });
        }
      }
    });
}

/** 删除按钮操作 */
function   handleDelete(row) {
    const declarationIds = row.id ||  ids.value;
    let nameList = row.standardName || names.value;
    //避免弹出窗数据太长，只显示前15条数据
    if ( nameList.length > 15 ) {
      nameList = nameList.slice(0,15);
    }

    proxy.$modal.$confirm('是否确认删除【主数据历史】名称为"' + nameList + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
    }).then(function() {
        return delDataDetailLog(declarationIds);
    }).then(() => {
        getList();
        proxy.$modal.msgSuccess("删除成功");
    }).catch(error => {
      proxy.$modal.msgError("没有找到需要删除的数据!");
    })
}

/** 状态修改 **/
function   handleStatusChange(row) {
  return changeStatusDataDetailLog(row.id, row.status).then(response=>{
    if(response.code == 200){
      proxy.$modal.msgSuccess("操作成功");
    }
  });
}

/** 修改字段状态 **/
function   chanageFile(value , filed , id){
  return changeDataDetailLogField(value , filed , id).then(response =>{
    if(response.code == 200){
      proxy.$modal.msgSuccess("操作成功");
    }
  }) ;
}

/** 导出按钮操作 */
function   handleExport() {
    const queryParams = queryParams.value;
      proxy.$modal.$confirm('是否确认导出所有【主数据历史】数据项?', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }).then(function() {
      return exportDataDetailLog(queryParams);
    }).then(response => {
      download(response.msg);
    })
}

function   export2Excel() {
  loading.value = true;
  searchParams.value = searchParam(queryParamsConfig.value, exportqueryParams.value);
  listDataDetailLog(searchParams.value).then(response => {
    exportDataDetailLogList.value = response.rows;
    loading.value = false;
    require.ensure([], () => {
      const { export_json_to_excel } = require('@/excel/Export2Excel');
      const tHeader = ['行业分类', '数据目录', '名称', '标识', '编码', '说明','类型','长度','质量标准', '备注', "状态", '添加时间'];
      // 上面设置Excel的表格第一行的标题
      const filterVal = ['classifyNameLabel', 'cataNameLabel', 'standardName','identity','code', 'standarDesc','type','length','quality', 'remark', 'statusLabel','addTime'];
      // 上面的index、phone_Num、school_Name是tableData里对象的属性
      const list = exportDataDetailLogList.value;  //把data里的tableData存到list
      const data = formatJson(filterVal, list);
      export_json_to_excel(tHeader, data, '数据历史列表');
    })
  }).catch(loading.value = false);

}

function   formatJson(filterVal, jsonData) {
   return jsonData.map(v => filterVal.map(j => v[j]))
}


/** 详情按钮操作 */
function   handleDesc(row) {
    detailDscForm.value.identity = row.identity;
    detailDscForm.value.shortName = row.shortName;
    detailDscForm.value.standardName = row.standardName;
    detailDscForm.value.code = row.code;
    detailDscForm.value.type = row.type;
    detailDscForm.value.length = row.length;
    detailDscForm.value.standarDesc = row.standarDesc;
    detailDscForm.value.quality = row.quality;
    detailDscForm.value.remark = row.remark;
    detailDscForm.value.classifyNameLabel = row.classifyNameLabel;
    detailDscForm.value.cataNameLabel = row.cataNameLabel;
    detailDscForm.value.statusLabel = row.statusLabel;
    detailDscForm.value.addTime = row.addTime;
    detailOpen.value=true;
}

function   detailCancel()
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
