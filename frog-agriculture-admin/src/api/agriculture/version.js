import request from '@/utils/request'

// 查询溯源版本列表
export function listVersion(query) {
  return request({
    url: '/agriculture/version/list',
    method: 'get',
    params: query
  })
}

// 查询溯源版本详细
export function getVersion(versionId) {
  return request({
    url: '/agriculture/version/' + versionId,
    method: 'get'
  })
}

// 新增溯源版本
export function addVersion(data) {
  return request({
    url: '/agriculture/version',
    method: 'post',
    data: data
  })
}

// 修改溯源版本
export function updateVersion(data) {
  return request({
    url: '/agriculture/version',
    method: 'put',
    data: data
  })
}

// 删除溯源版本
export function delVersion(versionId) {
  return request({
    url: '/agriculture/version/' + versionId,
    method: 'delete'
  })
}
