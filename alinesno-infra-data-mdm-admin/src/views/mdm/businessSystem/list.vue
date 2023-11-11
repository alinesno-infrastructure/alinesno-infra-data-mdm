<template>
  <!--
    【业务系统】 功能列表

    @author luoxiaodong
    @since 1.0.0
  -->
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="标识" prop="identity" >
        <el-input
          v-model="queryParams.identity"
          ref="queryParams.identity"
          placeholder="请输入标识"
          clearable
          
          wrapper="eq"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="名称" prop="name" >
        <el-input
          v-model="queryParams.name"
          ref="queryParams.name"
          placeholder="请输入名称"
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

    <el-table v-loading="loading" :data="BusinessSystemList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="标识" align="left" prop="identity" />
      <el-table-column label="名称" align="left" prop="name" />
      <el-table-column label="备注" align="left" prop="remark" />
      <el-table-column label="添加时间" align="center" prop="addTime" :width=150>
        <template  #default="scope">
          <span>{{ parseTime(scope.row.addTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center" class-name="small-padding fixed-width">
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

    <!-- 添加或修改【业务系统】对话框 :close-on-click-modal="false"-->
    <el-dialog :title="title" v-model="open" width="480px" append-to-body >
      <el-form  :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="标识" prop="identity" >
          <el-input v-model="form.identity" placeholder="请输入业务系统标识" maxlength="36" show-word-limit/>
        </el-form-item>
        <el-form-item label="名称" prop="name" >
          <el-input v-model="form.name" placeholder="请输入业务系统名称" maxlength="200" show-word-limit/>
        </el-form-item>
        <el-form-item label="备注" prop="remark" >
          <el-input v-model="form.remark" type="textarea" :autosize="{minRows:3}" placeholder="请输入内容" />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script   setup name="BusinessSystem">
import { ref, reactive, onMounted} from 'vue';
import { useRouter, useRoute } from 'vue-router';


import {
  listBusinessSystem,
  getBusinessSystem,
  delBusinessSystem,
  addBusinessSystem,
  changeBusinessSystemField,
  changeStatusBusinessSystem,
  updateBusinessSystem,
  exportBusinessSystem,checkIfUsed } from "@/api/mdm/BusinessSystem";
import  Condition  from "@/api/Search/Condition";
import  searchParam  from "@/api/Search/searchform";
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

// 选中数组
const names = ref([]);

// 选中数组
const ids = ref([]);

// 状态
const statusOptions = ref([]);

// 【业务系统】表格数据
const BusinessSystemList = ref([]);

//导出清单
const exportBusinessSystemList = ref([]);

// 搜索参数
const searchParams = ref([]);


// 弹出层标题
const title = ref("");

const data = reactive({
    total :  10 ,
    // 查询参数
    queryParams: {
      pageNum: 1,
      pageSize: 10,
      identity: null,
      name: null,
      remark: null
    },

    //搜索参数字段类型配置，配置对象的key必须和搜索字段同名
    // 未做配置定义的字段将会原样输出到后端，通常分页参数不需要配置
    // Condition 对象封装了快捷函数直接生成匹配规则，需要import后使用
    queryParamsConfig: {
      identity: Condition.like(),
      name: Condition.like(),
      remark: Condition.like(),
    },

    // 表单参数
    form: {},
    // 表单校验
    rules: {
      identity: [
        { required: true, message: "业务系统标识不能为空", trigger: "blur" }
      ],
      name: [
        { required: true, message: "业务系统名称不能为空", trigger: "blur" }
      ],
      remark: [
        { required: false, message: "备注不能为空", trigger: "blur" }
      ]
    },

    //导出查询参数
    exportqueryParams: {
      pageNum: 1,
      pageSize: 100000,
      identity: null,
      name: null,
      remark: null
    }
});


const { total,queryParams, queryParamsConfig, form, rules, exportqueryParams  } = toRefs(data);

// 页面加载后触发
onMounted(() => {
  getList();
})




/** 查询【业务系统】列表 */
function getList() {
  loading.value = true;
  // 使用全局的搜索参数处理方法预处理
  searchParams.value = searchParam(queryParamsConfig.value, queryParams.value);
  listBusinessSystem(searchParams.value).then(response => {
    total.value = response.total;
    BusinessSystemList.value = response.rows;
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
    identity: null,
    name: null,
    remark: null
  };
  proxy.resetForm("formRef");
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
  names.value = selection.map(item => item.name) ;
  single.value = selection.length!==1 ;
  multiple.value = !selection.length ;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加【业务系统】";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  // reset();
  const enName = row.id || ids.value
  getBusinessSystem(enName).then(response => {
    if (response.data) {
      form.value = response.data;
      open.value = true;
      title.value = "修改【业务系统】";
    } else {
      proxy.$modal.msgError("没有找到需要修改的数据!");
    }
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["formRef"].validate(valid => {
    if (valid) {
      if ( form.value.id != null ) {
        updateBusinessSystem(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addBusinessSystem(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const enNames = row.id || ids.value;
  let nameslist = row.name || names.value;
  //避免弹出窗数据太长，只显示前15条数据
  if ( nameslist.length > 15 ) {
    nameslist = nameslist.slice(0,15);
  }

  checkIfUsed(enNames).then(res => {
    if ( res.code == 200 ) {
        proxy.$modal.$confirm('是否确认删除【业务系统】名称为"' + nameslist + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delBusinessSystem(enNames);
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
  return changeStatusBusinessSystem(row.id, row.status).then(response=>{
    if(response.code == 200){
      proxy.$modal.msgSuccess("操作成功");
    }
  });
}

/** 修改字段状态 **/
function chanageFile(value , filed , id){
  debugger
  return changeBusinessSystemField(value , filed , id).then(response =>{
    if(response.code == 200){
      proxy.$modal.msgSuccess("操作成功");
    }
  }) ;
}

/** 导出按钮操作 */
function handleExport() {
  const queryParams = queryParams.value;
  proxy.$modal.$confirm('是否确认导出所有【业务系统】数据项?', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }).then(function() {
      return exportBusinessSystem(queryParams);
    }).then(response => {
      download(response.msg);
    })
}

function export2Excel() {
  loading.value = true;
  // 使用全局的搜索参数处理方法预处理
  searchParams.value = searchParam(queryParamsConfig.value, exportqueryParams.value);
  listBusinessSystem(searchParams.value).then(response => {
    exportBusinessSystemList.value = response.rows;
    loading.value = false;
    require.ensure([], () => {
      const { export_json_to_excel } = require('@/excel/Export2Excel');
      const tHeader = ['业务系统标识', '业务系统名称', '备注', '添加时间'];
      // 上面设置Excel的表格第一行的标题
      const filterVal = ['identity', 'name', 'remark', 'addTime'];
      // 上面的index、phone_Num、school_Name是tableData里对象的属性
      const list = exportBusinessSystemList.value;  //把data里的tableData存到list
      const data = formatJson(filterVal, list);
      export_json_to_excel(tHeader, data, '业务系统');
    })
  }).catch(loading.value = false);


}

function formatJson(filterVal, jsonData) {
  return jsonData.map(v => filterVal.map(j => v[j]))
}


/** created */

// getList();
// 查询公共状态
// proxy.getDicts("has_status").then(response => {
//   statusOptions.value = response.data;
// });

/** created */



</script>
