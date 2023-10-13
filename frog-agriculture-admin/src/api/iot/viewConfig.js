import request from '@/utils/request'

// 查询界面可视化配置列表
export function listViewConfig(query) {
  return request({
    url: '/iot/viewConfig/list',
    method: 'get',
    params: query
  })
}

// 查询界面可视化配置详细
export function getViewConfig(viewId) {
  return request({
    url: '/iot/viewConfig/' + viewId,
    method: 'get'
  })
}

// 新增界面可视化配置
export function addViewConfig(data) {
  return request({
    url: '/iot/viewConfig',
    method: 'post',
    data: data
  })
}

// 修改界面可视化配置
export function updateViewConfig(data) {
  return request({
    url: '/iot/viewConfig',
    method: 'put',
    data: data
  })
}

// 删除界面可视化配置
export function delViewConfig(viewId) {
  return request({
    url: '/iot/viewConfig/' + viewId,
    method: 'delete'
  })
}
