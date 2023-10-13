import request from '@/utils/request'

// 查询机械工时列表
export function listCostMachine(query) {
  return request({
    url: '/agriculture/costMachine/list',
    method: 'get',
    params: query
  })
}

// 查询机械工时详细
export function getCostMachine(costId) {
  return request({
    url: '/agriculture/costMachine/' + costId,
    method: 'get'
  })
}

// 新增机械工时
export function addCostMachine(data) {
  return request({
    url: '/agriculture/costMachine',
    method: 'post',
    data: data
  })
}

// 修改机械工时
export function updateCostMachine(data) {
  return request({
    url: '/agriculture/costMachine',
    method: 'put',
    data: data
  })
}

// 删除机械工时
export function delCostMachine(costId) {
  return request({
    url: '/agriculture/costMachine/' + costId,
    method: 'delete'
  })
}
