import request from '@/utils/request'

// 查询产品布局列表
export function listLayout(query) {
  return request({
    url: '/agriculture/layout/list',
    method: 'get',
    params: query
  })
}


// 新增产品布局
export function addLayout(data) {
  return request({
    url: '/agriculture/layout',
    method: 'post',
    data: data
  })
}



// 删除产品布局
export function delLayout(productId) {
  return request({
    url: '/agriculture/layout/' + productId,
    method: 'delete'
  })
}
