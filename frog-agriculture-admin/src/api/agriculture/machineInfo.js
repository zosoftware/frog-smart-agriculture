import request from '@/utils/request'

// 查询机械信息列表
export function listMachineInfo(query) {
  return request({
    url: '/agriculture/machineInfo/list',
    method: 'get',
    params: query
  })
}

// 查询机械信息详细
export function getMachineInfo(machineId) {
  return request({
    url: '/agriculture/machineInfo/' + machineId,
    method: 'get'
  })
}

// 新增机械信息
export function addMachineInfo(data) {
  return request({
    url: '/agriculture/machineInfo',
    method: 'post',
    data: data
  })
}

// 修改机械信息
export function updateMachineInfo(data) {
  return request({
    url: '/agriculture/machineInfo',
    method: 'put',
    data: data
  })
}

// 删除机械信息
export function delMachineInfo(machineId) {
  return request({
    url: '/agriculture/machineInfo/' + machineId,
    method: 'delete'
  })
}
