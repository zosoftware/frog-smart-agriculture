import request from '@/utils/request'

// 查询种植方法列表
export function listMethod(query) {
  return request({
    url: '/agriculture/method/list',
    method: 'get',
    params: query
  })
}

// 查询种植方法详细
export function getMethod(methodId) {
  return request({
    url: '/agriculture/method/' + methodId,
    method: 'get'
  })
}

// 新增种植方法
export function addMethod(data) {
  return request({
    url: '/agriculture/method',
    method: 'post',
    data: data
  })
}

// 修改种植方法
export function updateMethod(data) {
  return request({
    url: '/agriculture/method',
    method: 'put',
    data: data
  })
}

// 删除种植方法
export function delMethod(methodId) {
  return request({
    url: '/agriculture/method/' + methodId,
    method: 'delete'
  })
}
