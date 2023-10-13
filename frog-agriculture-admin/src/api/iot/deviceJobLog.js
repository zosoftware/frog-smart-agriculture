import request from '@/utils/request'

// 查询设备定时任务日志列表
export function listLog(query) {
  return request({
    url: '/iot/deviceJobLog/list',
    method: 'get',
    params: query
  })
}

// 查询设备定时任务日志详细
export function getLog(jobLogId) {
  return request({
    url: '/iot/deviceJobLog/' + jobLogId,
    method: 'get'
  })
}




