<template>
  <!--
    【数据标准】 功能列表

    @author luoxiaodong
    @since 1.0.0
  -->
  <div class="app-container">
    <el-row>
      <el-col :span="4" >
        <span>数据目录</span>
        <el-tree :data="DataCatagorylist_tree" :props="defaultProps" @node-click="handleNodeClick"
                 :expand-on-click-node="false" :defaultExpandAll="true"
                  ></el-tree>
      </el-col>

    <el-col :span="20" ><div class="grid-content bg-purple-light" width="1850px"></div>
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="行业分类" prop="classifyId"  label-width="150px" >
        <el-select  v-model="queryParams.classifyId" placeholder="请选择行业分类" @change="$forceUpdate()"   clearable >
          <el-option
            v-for="item in classifyList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="名称" prop="standardName" label-width="150px" >
        <el-input
          v-model="queryParams.standardName"
          ref="queryParams.standardName"
          placeholder="请输入数据标准名称"
          clearable
          
          wrapper="eq"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="编码" prop="code" label-width="150px">
        <el-input
          v-model="queryParams.code"
          ref="queryParams.code"
          placeholder="请输入编码"
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
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="exportTemplateUI"
        >模板下载</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          size="mini"
          icon="el-icon-upload2"
          @click="dataDetailImport"
        >导入数据</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          size="mini"-->
<!--          icon="el-icon-download"-->
<!--          @click="export2Excel"-->
<!--        >导出数据</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
        <el-table v-loading="loading"  :data="DataDetailList" @selection-change="handleSelectionChange" :default-sort = "{prop: 'namingCode', order: 'ascending '}" @sort-change="changeTableSort" >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="名称" align="left" prop="standardName"    :sortable="'custom'"    />
          <el-table-column label="标识" align="left" prop="identity"  :sortable="'custom'"   />
          <el-table-column label="编码" align="left" prop="code"  :sortable="'custom'"   />
          <el-table-column label="行业分类" align="left" prop="classifyNameLabel"  :sortable="'custom'"     />
          <el-table-column label="状态" :width=70 prop="hasStatus" align="center">
            <template  #default="scope">
              <el-switch
                v-model="scope.row.hasStatus"
                :active-value="0"
                :inactive-value="1"
                @change="handleStatusChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
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

    <!-- 添加或修改【数据标准】对话框 -->
    <el-dialog :title="title" v-model="open" width="480px" append-to-body :close-on-click-modal="false">
      <el-form ref="formDef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="数据目录" prop="cataId" >
          <el-select v-model="form.cataId" placeholder="请选择数据目录"  style="width:360px" clearable  filterable >
            <el-option  v-for="item in DataCatagorylist"
                        :key="item.id"
                        :label="item.cataName"
                        :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标识" prop="identity" >
          <el-input v-model="form.identity" placeholder="请输入标识" maxlength="64" show-word-limit/>
        </el-form-item>
        <el-form-item label="名称" prop="standardName" >
          <el-input v-model="form.standardName" placeholder="请输入名称" type="textarea" autosize  maxlength="200" show-word-limit/>
        </el-form-item>
        <el-form-item label="编码" prop="code" >
          <el-input v-model="form.code" placeholder="请输入编码" type="textarea" autosize maxlength="200" show-word-limit/>
        </el-form-item>
        <el-form-item label="说明" prop="standarDesc" >
          <el-input v-model="form.standarDesc" placeholder="请输入说明" type="textarea"  :autosize="{minRows:3}" maxlength="255" show-word-limit/>
        </el-form-item>
        <el-form-item label="类型" prop="type" >
          <el-input v-model="form.type"  placeholder="请输入数据标准的数据类型，如 浮点型" maxlength="20" show-word-limit/>
        </el-form-item>
        <el-form-item label="长度" prop="length" >
          <el-input v-model="form.length"    placeholder="请输入数据标准的数据长度，如 10,2" maxlength="10" show-word-limit/>
        </el-form-item>
        <el-form-item label="质量标准" prop="quality" >
          <el-input v-model="form.quality"  type="textarea"   :autosize="{minRows:3}"   placeholder="请输入数据标准的质量标准，如 应大于0" maxlength="255" show-word-limit/>
        </el-form-item>
        <el-form-item label="备注" prop="remark" >
          <el-input v-model="form.remark" type="textarea" :autosize="{minRows:3}" placeholder="请输入备注" maxlength="512" show-word-limit/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--数据标准导入弹出窗 :headers="upload.headers"-->
    <el-dialog title="数据标准导入窗"  v-model="dataDetailImportOpen" width="450px" append-to-body @close="dataDetailImportClose">
      <el-upload ref="upload" :limit="1" accept=".xlsx"
                 action=""
                 :multiple="false"
                 :http-request="diyUploadFile"
                 :file-list="fileList"
                 :on-change="onChangeFile"
                 :before-remove="beforeRemove"
                 :on-remove="onRemoveFile"
                 :on-progress="handleFileUploadProgress"
                 :on-success="handleFileSuccess"
                 :auto-upload="false"
        drag>
<!--      <el-upload ref="upload"  action="#"-->
<!--                 :limit="1"-->
<!--                 :on-error="onErrorFile"-->
<!--                 :on-success="onSuccessFile"-->
<!--                 :http-request="uploadDetail"-->
<!--                 drag>-->
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <!--div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" />
            是否更新已经存在的检查任务数据
          </div-->
          <span>仅允许导入xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;"
                   @click="exportTemplate">下载模板
          </el-link>
        </div>
      </el-upload>
      <br>
      <el-form ref="importForm" :model="importForm"   label-width="0px" v-show="showImportError">
        <el-form-item label="" prop="remark"   >
          <el-input v-model="importForm.remark" type="textarea" :autosize="{maxRows : 4}"  :readonly=true placeholder="请输入备注" />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">导 入</el-button>
        <el-button @click="dataDetailImportClose">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 数据标准详情弹出框 -->
    <el-dialog title="数据标准详情" v-model="detailOpen"   width="540px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="detailDscForm"   label-width="80px">
        <el-form-item label="行业分类" prop="classifyNameLabel" >
          <el-input v-model="detailDscForm.classifyNameLabel"  :readonly=true />
        </el-form-item>
        <el-form-item label="数据目录" prop="cataNameLabel" >
          <el-input v-model="detailDscForm.cataNameLabel"   :readonly=true />
        </el-form-item>
        <el-form-item label="标识" prop="identity" >
          <el-input v-model="detailDscForm.identity" :readonly=true />
        </el-form-item>
        <el-form-item label="名称" prop="standardName" >
          <el-input v-model="detailDscForm.standardName"  :readonly=true />
        </el-form-item>
        <el-form-item label="编码" prop="code" >
          <el-input v-model="detailDscForm.code"  :readonly=true />
        </el-form-item>
        <el-form-item label="说明" prop="standarDesc" >
          <el-input v-model="detailDscForm.standarDesc" type="textarea" autosize  :readonly=true placeholder="请输入数据标准的说明，如 上年度职工月平均工资" />
        </el-form-item>
        <el-form-item label="类型" prop="type" >
          <el-input v-model="detailDscForm.type"  :readonly=true placeholder="请输入数据标准的数据类型，如 浮点型" />
        </el-form-item>
        <el-form-item label="长度" prop="length" >
          <el-input v-model="detailDscForm.length"  :readonly=true placeholder="请输入数据标准的数据长度，如 10,2" />
        </el-form-item>
        <el-form-item label="质量标准" prop="quality" >
          <el-input v-model="detailDscForm.quality"  type="textarea" autosize :readonly=true true placeholder="请输入数据标准的质量标准，如 应大于0" />
        </el-form-item>
        <el-form-item label="备注" prop="remark" >
          <el-input v-model="detailDscForm.remark" type="textarea" autosize :readonly=true placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="状态" prop="statusLabel" :width=80>
          <el-input v-model="detailDscForm.statusLabel"  :readonly=true />
        </el-form-item>
        <el-form-item label="添加时间" prop="addTime" :width=150 >
          <el-input v-model="detailDscForm.addTime"  :readonly=true />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="detailCancel">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script   setup name="DataDetail">
import { ref, reactive, onMounted} from 'vue';
import { useRouter, useRoute } from 'vue-router';

import {
  listDataDetail,
  getDataDetail,
  delDataDetail,
  addDataDetail,
  changeDataDetailField,
  changeStatusDataDetail,
  updateDataDetail,
  exportDataDetail,
  exportTemplate,
  checkMdIfExist,
  uploadDetail,
  listTreeNavInfo
} from "@/api/mdm/DataDetail";
import {listDataCatagory} from "@/api/mdm/DataCatagory";
import  Condition  from "@/api/Search/Condition";
import  searchParam  from "@/api/Search/searchform";
import {listIndustryClassify}  from "@/api/mdm/IndustryClassify";
// import { Loading } from '@element-plus/icons-vue';

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

const dataDetailImportOpen = ref(false);

const detailOpen = ref(false);

const showImportError = ref(false);

const dialogLoad = ref(false);

// 弹出层标题
const title= ref("");

// 选中数组
const names = ref([]);

// 选中数组
const ids = ref([]);

// 状态
const statusOptions = ref([]);

// 【数据标准】表格数据
const DataDetailList = ref([]);

//数据目录清单
const DataCatagorylist = ref([]);

//数据目录清单
const DataCatagorylist_tree = ref([]);

//当前数的子节点
const childArr = ref([]);

const classifyList = ref([]);

const searchParamTem = ref([]);

const pageSizes_new= ref([10, 100, 200, 300]);

//导出清单
const exportDataDetailList = ref([]);

//上传文件列表
const fileList = ref([]);

//文件File 上传参数
const upFile = ref([]);

//文件File列表 上传参数
const upFileList = ref([]);

// 搜索参数
const searchParams = ref([]);

const data = reactive({
    defaultProps: {
    children: 'children',
        label: 'cataName'
  },

  total:0,

  //详情
  detailDscForm : {} ,

  //导入错误显示框
  importForm:{},

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

  //搜索参数字段类型配置，配置对象的key必须和搜索字段同名
  // 未做配置定义的字段将会原样输出到后端，通常分页参数不需要配置
  // Condition 对象封装了快捷函数直接生成匹配规则，需要import后使用
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
      { required: true, message: "编码不能为空", trigger: "blur" }
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

  upload: {
    // 是否显示弹出层
    open: false,
    // 弹出层标题（会员导入）
    title: "数据明细导入",
    // 是否禁用上传
    isUploading: false,
    // 是否更新已经存在的参数数据
    updateSupport: 0,
    // 设置上传的请求头部
    //headers: {
    //   Authorization: "Bearer "
    //},
    // 上传的地址
    // url: process.env.VUE_APP_BASE_API + "/api/mdm/DataDetail/upload"
    url: uploadDetail
  },

  Params: {
    pageNum : 1,
    pageSize: 1000,
    hasStatus:  0
  },

  ParamsConfig:{
    hasStatus:Condition.eq()
  },

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

});

const { defaultProps, total, detailDscForm, importForm, queryParams, queryParamsConfig, form, rules, upload, Params, ParamsConfig, exportqueryParams  } = toRefs(data);

// 页面加载后触发
onMounted(() => {
  initSearchParamTem();
  getDataCatagorylist();         //增加数据目录下拉框功能
  getClassifyList();             //行业分类下拉框功能
})


function   initSearchParamTem(){
  searchParamTem.value = searchParam(ParamsConfig.value, Params.value);
}

function   getClassifyList(){
  listIndustryClassify(searchParamTem.value).then(response => {
    classifyList.value = response.rows;
  });
}

/** 查询【数据标准】列表 */
function   getList() {
  loading.value = true;
  // 使用全局的搜索参数处理方法预处理
  searchParams.value = searchParam(queryParamsConfig.value, queryParams.value);
  listDataDetail(searchParams.value).then(response => {
    total.value = response.total;
    DataDetailList.value = response.rows;
    loading.value = false;
  });
}

function   handleNodeClick(node) {
  treeIdArrs.value = [] ;
  // 获取当前节点信息
  treeIdArrs.value.push(node.id)
  // 遍历当前节点的所有子节点
  if (node.children) {
    for (let i = 0; i < node.children.length; i++) {
      treeIdArrs.value.push(node.children[i].id)

    }
  }

  searchParams.value = searchParam(queryParamsConfig.value, queryParams.value);
  loading.value = true;
  listTreeNavInfo(treeIdArrs.value, searchParams.value.pageNum, searchParams.value.pageSize, searchParams.value ).then(res => {
    DataDetailList.value = res.rows;
    loading.value = false;
  })

}

//增加数据目录清单下拉框功能
function   getDataCatagorylist() {
  listDataCatagory(searchParamTem.value).then(response => {
    DataCatagorylist.value = response.rows;
    DataCatagorylist_tree.value = proxy.handleTree(response.rows,"id","parentCataId" ) ;
  });
}

// 取消按钮
function   cancel() {
  open.value = false;
  reset();
}

// 表单重置
function   reset() {
  form.value = {
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
  };
  proxy.resetForm("formDef");
}

/** 搜索按钮操作 */
function   handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function   resetQuery() {
  proxy.resetForm("queryForm");
  handleQuery();
}

// 多选框选中数据
function   handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id) ;
  names.value = selection.map(item => item.standardName) ;
  single.value = selection.length!==1 ;
  multiple.value = !selection.length ;
}

/** 新增按钮操作 */
function   handleAdd() {
  reset();
  open.value = true;
  title.value = "添加【数据标准】";
}

/** 修改按钮操作 */
function   handleUpdate(row) {
  // reset();
  const declarationId = row.id || ids.value
  getDataDetail(declarationId).then(response => {
    if (response.data) {
      form.value = response.data;
      open.value = true;
      title.value = "修改【数据标准】";
    } else {
      proxy.$modal.msgError("没有找到需要修改的数据!");
    }
  });
}

/** 提交按钮 */
function   submitForm() {
    proxy.$refs["formDef"].validate(valid => {
        if (valid) {
          checkMdIfExist(form.value).then(res => {
            if ( res.code == 200 ) {

                DataCatagorylist.value.filter((item) => {
                  if ( item.id === form.value.cataId)
                  {
                    form.value.classifyId = item.classifyId
                  }
                });

                if (form.value.id != null) {
                  updateDataDetail(form.value).then(response => {
                    proxy.$modal.msgSuccess("修改成功");
                    open.value = false;
                    getList();
                  });
                } else {
                  addDataDetail(form.value).then(response => {
                    proxy.$modal.msgSuccess("新增成功");
                    open.value = false;
                    getList();
                  });
                }
            }
            else {
              proxy.$modal.$message.error(res.msg)
            }
          }).catch(error => {

          });


        }
    });
}

/** 删除按钮操作 */
function   handleDelete(row) {
  debugger
  const declarationIds = row.id || ids.value;
  let nameList = row.standardName || names.value;
  //避免弹出窗数据太长，只显示前15条数据
  if ( nameList.length > 15 ) {
    nameList = nameList.slice(0,15);
  }
  console.log(nameList);
  proxy.$confirm('是否确认删除【数据标准】数据项?', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
      lockScroll:"true"
    }).then(function() {
      return delDataDetail(declarationIds);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(error => {
        // proxy.$modal.msgError("没有找到需要删除的数据!");
    })
}

/** 状态修改 **/
function   handleStatusChange(row) {
  return changeStatusDataDetail(row.id, row.status).then(response=>{
    if(response.code == 200){
      proxy.$modal.msgSuccess("操作成功");
    }
  });
}

/** 修改字段状态 **/
function   chanageFile(value , filed , id){
  return changeDataDetailField(value , filed , id).then(response =>{
    if(response.code == 200){
      proxy.$modal.msgSuccess("操作成功");
    }
  }) ;
}

/** 导出按钮操作 */
function   handleExport() {
  const queryParams = queryParams.value;
      proxy.$modal.$confirm('是否确认导出所有【数据标准】数据项?', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }).then(function() {
      return exportDataDetail(queryParams);
    }).then(response => {
      download(response.msg);
    })
}

function   dataDetailImport()
{
  importForm.value = {
    remark: null
  };
  showImportError.value = false ;
  proxy.resetForm("importForm");
  dataDetailImportOpen.value=true;
}

function   dataDetailImportClose()
{
  dataDetailImportOpen.value=false;
  handleQuery();
  // fileList.value = [] ;   //上传文件列表
  // upFile.value = [] ;      //文件File 上传参数
  // upFileList.value = [] ;   //文件File列表 上传参数
}

function   exportTemplateUI(){
  exportTemplate().then((res) => {
    const blob = new Blob([res]);
    const fileName = "数据标准导入模板.xlsx";
    if ("download" in document.createElement("a")) {
      // 非IE下载
      const _link = document.createElement("a");
      _link.download = fileName;
      _link.style.display = "none";
      _link.href = URL.createObjectURL(blob);
      document.body.appendChild(_link);
      _link.click();
      URL.revokeObjectURL(_link.href); // 释放URL 对象
      document.body.removeChild(_link);
    } else {
      // IE10+下载
      navigator.msSaveBlob(blob, fileName);
    }
  })
}

function   submitFileForm(){
  proxy.$refs.upload.submit();
}

function   cancelSubmitFileForm(){
   dataDetailImportOpen.value=false;
   proxy.$refs.upload.clearFiles();
}

// 文件上传中处理
function   handleFileUploadProgress(event, file, fileList) {
  upload.value.isUploading = true;
}

// 文件上传成功处理
function   handleFileSuccess(response, file, fileList) {
  upload.value.open = false;
  upload.value.isUploading = false;
  proxy.$refs.upload.clearFiles();
  proxy.$modal.$alert(response.message, "导入结果", {
    dangerouslyUseHTMLString: true
  });
  getList();
}

function   export2Excel() {
  loading.value = true;
  // 使用全局的搜索参数处理方法预处理
  searchParams.value = searchParam(queryParamsConfig.value, exportqueryParams.value);
  //listDataDetail(this.queryParams , this.searchParams).then(response => {
  listDataDetail(searchParams.value).then(response => {
    exportDataDetailList.value = response.rows;
    loading.value = false;
    require.ensure([], () => {
      const { export_json_to_excel } = require('@/excel/Export2Excel');
      const tHeader = ['行业分类', '数据目录', '名称', '标识', '编码', '说明','类型','长度','质量标准', '备注', "状态", '添加时间'];
      // 上面设置Excel的表格第一行的标题
      const filterVal = ['classifyNameLabel', 'cataNameLabel', 'standardName', 'identity', 'code', 'standarDesc','type','length','quality', 'remark', 'statusLabel', 'addTime'];
      // 上面的index、phone_Num、school_Name是tableData里对象的属性
      const list = exportDataDetailList.value;  //把data里的tableData存到list
      const data = formatJson(filterVal, list);
      export_json_to_excel(tHeader, data, '数据标准');
    })
  }).catch(loading.value = false);

}

function   formatJson(filterVal, jsonData) {
  return jsonData.map(v => filterVal.map(j => v[j]))
}

function   changeTableSort(column) {
  //打印看看参数有哪些？
  // console.log("排序", column.prop, column.order);
  currpage.value = 1; // 排序后返回第一页
  proptype.value = column.prop; // 将键名prop赋值给变量proptype
  DataDetailList.value = DataDetailList.value.sort(
    SortFun(column.prop, column.order === "descending")
  );
  /* if (column.order === "descending") {
    //大到小
    this.tableData = this.tableData.sort(this.SortFun(column.prop, 1)); //从大到小
  } else if (column.order === "ascending") {
    this.tableData = this.tableData.sort(this.SortFun(column.prop, -1)); //从小到大
  } */
}

function   SortFun(attr, rev) {
  //第一个参数传入info里的prop表示排的是哪一列，第二个参数是升还是降排序
  if (rev == undefined) {
    rev = 1;
  } else {
    rev = rev ? 1 : -1;
    //rev = rev>0 ? 1 : -1;
  }
  return function (a, b) {
    a = a[attr];
    b = b[attr];
    if (a < b) {
      return rev * -1;
    }
    if (a > b) {
      return rev * 1;
    }
    return 0;
  };
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
    detailDscForm.value.addTime = row.updateTime;
    detailOpen.value=true;
}

function   detailCancel()
{
   detailOpen.value = false ;
}

// 选择上传文件
function   onChangeFile (file, fileList) {
const isLt100M = file.size / 1024 / 1024 < 100
if (!isLt100M) {
  this.$msgbox.alert('上传文件大小不能超过 100MB!')
  return false
}

  upFileList.value = []
  for (let x of fileList) {
    if (x.raw) {
      upFileList.value.push(x.raw)
    }
  }
}

// 移除文件之前
function   beforeRemove (file, fileList) {
return this.$msgbox.alert(`确定移除 ${file.name}？`)
}

// 移除文件
function   onRemoveFile (file, fileList) {
this.upFileList = []
for (let x of fileList) {
  if (x.raw) {
    this.upFileList.push(x.raw)
  }
}
}

//vue界面将文件发送到后端
function   diyUploadFile () {
  // dialogLoad.value = Loading.service({
  //   lock: true,
  //   text: '数据校验、入库中......',
  //   spinner: 'el-icon-loading',
  //   background: 'rgba(0, 0, 0, 0.7)'
  // })
  // ;
  upFile.value = upFileList.value[0];
  let uploadForm = new FormData()
  uploadForm.append('file', upFile.value)
  uploadDetail(uploadForm).then(response => {
    if ( response.code == 200 ) {
      this.$message.success(response.msg)
    }else{
      this.$message.error(response.msg)
      this.showImportError = true ;
      this.importForm.remark = response.msg;
    }
    // dialogLoad.close();
    return false

  }).catch(error => {
    showImportError.value = true ;
    importForm.value.remark = error.toString();
    // dialogLoad.value.close();
    return false
  })
}

/** created */

  getList();
// 查询公共状态
// proxy.getDicts("has_status").then(response => {
//   statusOptions.value = response.data;
// });

/** created */



</script>
