import request from '@/utils/request'

// 查询溯源产品列表
export function listSellpro(query) {
  return request({
    url: '/agriculture/sellpro/list',
    method: 'get',
    params: query
  })
}

// 查询溯源产品详细
export function getSellpro(sellproId) {
  return request({
    url: '/agriculture/sellpro/' + sellproId,
    method: 'get'
  })
}

// 新增溯源产品
export function addSellpro(data) {
  return request({
    url: '/agriculture/sellpro',
    method: 'post',
    data: data
  })
}

// 修改溯源产品
export function updateSellpro(data) {
  return request({
    url: '/agriculture/sellpro',
    method: 'put',
    data: data
  })
}

// 删除溯源产品
export function delSellpro(sellproId) {
  return request({
    url: '/agriculture/sellpro/' + sellproId,
    method: 'delete'
  })
}
