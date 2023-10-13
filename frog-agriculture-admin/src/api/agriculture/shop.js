import request from '@/utils/request'

// 查询店铺列表
export function listShop(query) {
  return request({
    url: '/agriculture/shop/list',
    method: 'get',
    params: query
  })
}

// 查询店铺详细
export function getShop(shopId) {
  return request({
    url: '/agriculture/shop/' + shopId,
    method: 'get'
  })
}

// 新增店铺
export function addShop(data) {
  return request({
    url: '/agriculture/shop',
    method: 'post',
    data: data
  })
}

// 修改店铺
export function updateShop(data) {
  return request({
    url: '/agriculture/shop',
    method: 'put',
    data: data
  })
}

// 删除店铺
export function delShop(shopId) {
  return request({
    url: '/agriculture/shop/' + shopId,
    method: 'delete'
  })
}
