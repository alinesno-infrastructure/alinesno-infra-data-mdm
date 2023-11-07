import request from '@/utils/request'

/**
 * 【行业分类】 接口文件
 *
 * @author luoxiaodong
 * @since 1.0.0
 */

// 接口配置项
var prefix = '/api/mdm/industryClassify/' ;
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
    treeNavInfo: prefix + "listTreeNavInfo",
    checkIfExist: prefix + "checkClassifyIfExist",
    checkIfUsed: prefix + "checkClassifyIfUsed"
}

// 查询【行业分类】列表
export function listIndustryClassify(query , data) {
  return request({
    url: managerUrl.datatables ,
    method: 'post',
    params: query ,
    data: data
  })
}

// 查询【行业分类】详细
export function getIndustryClassify(id) {
  return request({
    url: managerUrl.detailUrl + '/' + id ,
    method: 'get'
  })
}

// 新增【行业分类】
export function addIndustryClassify(data) {
  return request({
    url: managerUrl.saveUrl,
    method: 'post',
    data: data
  })
}

// 修改【行业分类】
export function updateIndustryClassify(data) {
  return request({
    url: managerUrl.updateUrl,
    method: 'put',
    data: data
  })
}

// 删除【行业分类】
export function delIndustryClassify(id) {
  return request({
    url: managerUrl.removeUrl + '/' + id ,
    method: 'delete'
  })
}

// 导出【行业分类】
export function exportIndustryClassify(query) {
  return request({
    url: managerUrl.exportUrl,
    method: 'get',
    params: query
  })
}

// 状态【行业分类】修改
export function changeStatusIndustryClassify(id , status) {
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

// 修改【行业分类】单个字段值
export function changeIndustryClassifyField(value , field , id){
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


//查询树形导航条当前节点和下级节点及符合查询条件的数据
export function listTreeNavInfo(ids,pageNum,pageSize, data){
  return request({
    url: managerUrl.treeNavInfo+"?ids=" +ids+"&pageNum="+pageNum+"&pageSize="+pageSize,
    method: 'post',
    params: data,
    data: data
  })
}


//检查同一个用户下，行业分类是否存在，如已存在，则不允许保存。确同一个用户下行业分类唯一
export function checkClassifyIfExist(data) {
  return request({
    url: managerUrl.checkIfExist,
    method: 'post',
    data: data
  })
}


//检查行业分类是否已被引用
export function checkIfUsed(ids) {
  return request({
    url: managerUrl.checkIfUsed + '?ids=' + ids ,
    method: 'get'
  })
}
