import request from '@/utils/request'

// 查询批次任务日志列表
export function listLog(query) {
  return request({
    url: '/agriculture/log/list',
    method: 'get',
    params: query
  })
}

// 查询批次任务日志详细
export function getLog(logId) {
  return request({
    url: '/agriculture/log/' + logId,
    method: 'get'
  })
}

// 新增批次任务日志
export function addLog(data) {
  return request({
    url: '/agriculture/log',
    method: 'post',
    data: data
  })
}

// 修改批次任务日志
export function updateLog(data) {
  return request({
    url: '/agriculture/log',
    method: 'put',
    data: data
  })
}

// 删除批次任务日志
export function delLog(logId) {
  return request({
    url: '/agriculture/log/' + logId,
    method: 'delete'
  })
}
