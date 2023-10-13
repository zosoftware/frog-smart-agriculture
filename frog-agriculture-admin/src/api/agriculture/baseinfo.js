import request from '@/utils/request'

// 查询基地信息列表
export function listBaseinfo(query) {
  return request({
    url: '/agriculture/baseinfo/list',
    method: 'get',
    params: query
  })
}

// 查询基地信息详细
export function getBaseinfo(baseId) {
  return request({
    url: '/agriculture/baseinfo/' + baseId,
    method: 'get'
  })
}

// 查询第一条基地信息详细
export function getBaseinfoLimitOne() {
  return request({
    url: '/agriculture/baseinfo',
    method: 'get'
  })
}

// 新增基地信息
export function addBaseinfo(data) {
  return request({
    url: '/agriculture/baseinfo',
    method: 'post',
    data: data
  })
}

// 修改基地信息
export function updateBaseinfo(data) {
  return request({
    url: '/agriculture/baseinfo',
    method: 'put',
    data: data
  })
}

// 删除基地信息
export function delBaseinfo(baseId) {
  return request({
    url: '/agriculture/baseinfo/' + baseId,
    method: 'delete'
  })
}
