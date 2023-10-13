import request from '@/utils/request'

// 查询机械类别列表
export function listMachineType(query) {
  return request({
    url: '/agriculture/machineType/list',
    method: 'get',
    params: query
  })
}

// 查询机械类别详细
export function getMachineType(machineTypeId) {
  return request({
    url: '/agriculture/machineType/' + machineTypeId,
    method: 'get'
  })
}

// 新增机械类别
export function addMachineType(data) {
  return request({
    url: '/agriculture/machineType',
    method: 'post',
    data: data
  })
}

// 修改机械类别
export function updateMachineType(data) {
  return request({
    url: '/agriculture/machineType',
    method: 'put',
    data: data
  })
}

// 删除机械类别
export function delMachineType(machineTypeId) {
  return request({
    url: '/agriculture/machineType/' + machineTypeId,
    method: 'delete'
  })
}
