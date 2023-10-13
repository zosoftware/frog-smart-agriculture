import request from '@/utils/request'


export function selectBaseInfo() {
    return request({
      url: '/agriculture/statistics/selectBaseInfo',
      method: 'get'
    })
}
export function selectDeviceInfo() {
    return request({
      url: '/agriculture/statistics/selectDeviceInfo',
      method: 'get'
    })
}
export function selectDeviceJobInfo() {
    return request({
      url: '/agriculture/statistics/selectDeviceJobInfo',
      method: 'get'
    })
}
export function selectTraceInfo() {
    return request({
      url: '/agriculture/statistics/selectTraceInfo',
      method: 'get'
    })
}
export function selectTaskInfo() {
    return request({
      url: '/agriculture/statistics/selectTaskInfo',
      method: 'get'
    })
}
export function selectAreaInfo() {
    return request({
      url: '/agriculture/statistics/selectAreaInfo',
      method: 'get'
    })
}
// 朔源统计分析
export function selectRecordStatistics() {
  return request({
    url: '/agriculture/statistics/selectRecordStatistics',
    method: 'get'
  })
}
// 朔源扫码TOps
export function selectRecordGroupBySellpro() {
  return request({
    url: '/agriculture/statistics/selectRecordGroupBySellpro',
    method: 'get'
  })
}
// 朔源扫码占比
export function selectRecordGroupByCity() {
  return request({
    url: '/agriculture/statistics/selectRecordGroupByCity',
    method: 'get'
  })
}
