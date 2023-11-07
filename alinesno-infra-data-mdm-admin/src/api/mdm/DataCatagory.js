import request from '@/utils/request'

/**
 * 【请填写功能名称】 接口文件
 *
 * @author luoxiaodong
 * @since 1.0.0
 */

// 接口配置项
var prefix = '/api/mdm/dataCatagory/' ;
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
    statistics: prefix + "statistics",
    treeNavInfo: prefix + "listTreeNavInfo",
    checkIfExist: prefix + "checkDataCatagoryIfExist",
    checkIfUsed: prefix + "checkCatagoryIfUsed"
}

// 查询【请填写功能名称】列表
export function listDataCatagory(query , data) {
  return request({
    url: managerUrl.datatables ,
    method: 'post',
    params: query ,
    data: data
  })
}

// 查询【请填写功能名称】详细
export function getDataCatagory(id) {
  return request({
    url: managerUrl.detailUrl + '/' + id ,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addDataCatagory(data) {
  return request({
    url: managerUrl.saveUrl,
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateDataCatagory(data) {
  return request({
    url: managerUrl.updateUrl,
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delDataCatagory(id) {
  return request({
    url: managerUrl.removeUrl + '/' + id ,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportDataCatagory(query) {
  return request({
    url: managerUrl.exportUrl,
    method: 'get',
    params: query
  })
}

// 状态【请填写功能名称】修改
export function changeStatusDataCatagory(id , status) {
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
export function changeDataCatagoryField(value , field , id){
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
//statistics
//统计数据明细行数、数据及索引占用的磁盘空间
export function statisticsCata(query , data) {
  return request({
    url: managerUrl.datatables ,
    method: 'post',
    params: query ,
    data: data
  })
}


//查询树形导航条当前节点和下级节点及符合查询条件的数据
export function listTreeNavInfo(ids,pageNum,pageSize, data){
  debugger
  return request({
    url: managerUrl.treeNavInfo+"?ids=" +ids+"&pageNum="+pageNum+"&pageSize="+pageSize,
    method: 'post',
    params: data,
    data: data
  })
}


//检查同一个用户，行业分类下,数据目录是否存在，如已存在，则不允许保存。确同一个用户、行业分类下数据目录唯一
export function checkDataCatagoryIfExist(data) {
  return request({
    url: managerUrl.checkIfExist,
    method: 'post',
    data: data
  })
}


//检查数据目录是否已被引用
export function checkIfUsed(ids) {
  return request({
    url: managerUrl.checkIfUsed + '?ids=' + ids ,
    method: 'get'
  })
}
