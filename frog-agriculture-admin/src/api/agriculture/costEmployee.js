import request from '@/utils/request'

// 查询人工工时列表
export function listCostEmployee(query) {
  return request({
    url: '/agriculture/costEmployee/list',
    method: 'get',
    params: query
  })
}

// 查询人工工时详细
export function getCostEmployee(costId) {
  return request({
    url: '/agriculture/costEmployee/' + costId,
    method: 'get'
  })
}

// 新增人工工时
export function addCostEmployee(data) {
  return request({
    url: '/agriculture/costEmployee',
    method: 'post',
    data: data
  })
}

// 修改人工工时
export function updateCostEmployee(data) {
  return request({
    url: '/agriculture/costEmployee',
    method: 'put',
    data: data
  })
}

// 删除人工工时
export function delCostEmployee(costId) {
  return request({
    url: '/agriculture/costEmployee/' + costId,
    method: 'delete'
  })
}
