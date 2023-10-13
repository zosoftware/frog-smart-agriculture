import request from '@/utils/request'

// 查询农资类别列表
export function listMaterialType(query) {
  return request({
    url: '/agriculture/materialType/list',
    method: 'get',
    params: query
  })
}

// 查询农资类别详细
export function getMaterialType(materialTypeId) {
  return request({
    url: '/agriculture/materialType/' + materialTypeId,
    method: 'get'
  })
}

// 新增农资类别
export function addMaterialType(data) {
  return request({
    url: '/agriculture/materialType',
    method: 'post',
    data: data
  })
}

// 修改农资类别
export function updateMaterialType(data) {
  return request({
    url: '/agriculture/materialType',
    method: 'put',
    data: data
  })
}

// 删除农资类别
export function delMaterialType(materialTypeId) {
  return request({
    url: '/agriculture/materialType/' + materialTypeId,
    method: 'delete'
  })
}
