import request from '@/utils/request'

/**
 * 【请填写功能名称】 接口文件
 *
 * @author luoxiaodong
 * @since 1.0.0
 */

// 接口配置项
var prefix = '/api/mdm/dataDetail/' ;
var managerUrl = {
    datatables : prefix +"datatables" ,
    createUrl: prefix + 'add' ,
    saveUrl: prefix + 'save' ,
    updateUrl: prefix +"modify" ,
    statusUrl: prefix +"changeStatus" ,
    cleanUrl: prefix + "clean",
    detailUrl: prefix +"detail",
    removeUrl: prefix + "delete" ,
    exportUrl: prefix + "exportExcel",
    changeField: prefix + "changeField",
    exportTemplate: prefix + "exportTemplate",
    statistics: prefix + "statistics",
    checkMdIfExist: prefix + "checkMdIfExist",
    uploadDetail: prefix + "upload",
    treeNavInfo: prefix + "listTreeNavInfo"
}

// 查询【请填写功能名称】列表
export function listDataDetail(query , data) {
  return request({
    url: managerUrl.datatables ,
    method: 'post',
    params: query ,
    data: data
  })
}

// 查询【请填写功能名称】详细
export function getDataDetail(id) {
  return request({
    url: managerUrl.detailUrl + '/' + id ,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addDataDetail(data) {
  return request({
    url: managerUrl.saveUrl,
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateDataDetail(data) {
  return request({
    url: managerUrl.updateUrl,
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delDataDetail(id) {
  return request({
    url: managerUrl.removeUrl + '/' + id ,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportDataDetail(query) {
  return request({
    url: managerUrl.exportUrl,
    method: 'get',
    params: query
  })
}

// 状态【请填写功能名称】修改
export function changeStatusDataDetail(id , status) {
  const data = {
    id ,
    status
  }
  return request({
    url: managerUrl.statusUrl,
    method: 'put',
    data: data
  })
}

// 修改【请填写功能名称】单个字段值
export function changeDataDetailField(value , field , id){
  const data = {
    value ,
    field ,
    id
  }
  return request({
    url: managerUrl.changeField ,
    method: 'post',
    data: data
  })
}

//下载导入模板
export function exportTemplate(query , data) {
  return request({
    url: managerUrl.exportTemplate ,
    method: 'get',
    responseType:'blob'
  })
}


//  statistics
//统计数据明细行数、数据及索引占用的磁盘空间
export function statistics(query , data) {
  return request({
    url: managerUrl.datatables ,
    method: 'post',
    params: query ,
    data: data
  })
}

//检查同一个用户，行业分类下,数据目录下的数据标准是否存在，如已存在，则不允许保存。确同一个用户、行业分类、数据目录下数据目录唯一
export function checkMdIfExist(data) {
  return request({
    url: managerUrl.checkMdIfExist ,
    method: 'post',
    data: data
  })
}


export function uploadDetail(data) {
  debugger
  return request({
    // headers: {
    //   "Content-Type": "multipart/form-data"
    // },
    url: managerUrl.uploadDetail ,
    method: 'post',
    data: data
  })
}

//查询树形导航条当前节点和下级节点及符合查询条件的数据
export function listTreeNavInfo(ids,pageNum,pageSize, data){
  return request({
    url: managerUrl.treeNavInfo+"?ids=" +ids+"&pageNum="+pageNum+"&pageSize="+pageSize,
    method: 'post',
    params: data,
    data: data
  })
}
