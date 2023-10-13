import request from '@/utils/request'

// 查询种质介绍列表
export function listIntro(query) {
  return request({
    url: '/agriculture/intro/list',
    method: 'get',
    params: query
  })
}

// 查询种质介绍详细
export function getIntro(introId) {
  return request({
    url: '/agriculture/intro/' + introId,
    method: 'get'
  })
}

// 新增种质介绍
export function addIntro(data) {
  return request({
    url: '/agriculture/intro',
    method: 'post',
    data: data
  })
}

// 修改种质介绍
export function updateIntro(data) {
  return request({
    url: '/agriculture/intro',
    method: 'put',
    data: data
  })
}

// 删除种质介绍
export function delIntro(introId) {
  return request({
    url: '/agriculture/intro/' + introId,
    method: 'delete'
  })
}
