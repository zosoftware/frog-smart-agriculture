import request from '@/utils/request'

// 查询溯源码查询记录列表
export function listRecord(query) {
  return request({
    url: '/agriculture/record/list',
    method: 'get',
    params: query
  })
}

// 查询溯源码查询记录详细
export function getRecord(recordId) {
  return request({
    url: '/agriculture/record/' + recordId,
    method: 'get'
  })
}

// 新增溯源码查询记录
export function addRecord(data) {
  return request({
    url: '/agriculture/record/add',
    method: 'post',
    data: data
  })
}

// 修改溯源码查询记录
export function updateRecord(data) {
  return request({
    url: '/agriculture/record',
    method: 'put',
    data: data
  })
}

// 删除溯源码查询记录
export function delRecord(recordId) {
  return request({
    url: '/agriculture/record/' + recordId,
    method: 'delete'
  })
}

// 查询溯源码统计列表
export function listCount(query) {
    return request({
      url: '/agriculture/record/countList',
      method: 'get',
      params: query
    })
  }
