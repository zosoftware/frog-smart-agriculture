import request from '@/utils/request'

// 查询地块列表
export function listLand(query) {
  return request({
    url: '/agriculture/land/cropLandList',
    method: 'get',
    params: query
  })
}

// 查询地块详细
export function getLand(landId) {
  return request({
    url: '/agriculture/land/' + landId,
    method: 'get'
  })
}

// 新增地块
export function addLand(data) {
  return request({
    url: '/agriculture/land',
    method: 'post',
    data: data
  })
}

// 修改地块
export function updateLand(data) {
  return request({
    url: '/agriculture/land',
    method: 'put',
    data: data
  })
}

// 删除地块
export function delLand(landId) {
  return request({
    url: '/agriculture/land/' + landId,
    method: 'delete'
  })
}
