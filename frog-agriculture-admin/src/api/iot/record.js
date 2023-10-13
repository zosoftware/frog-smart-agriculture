import request from '@/utils/request'

export function getDevRecord(deviceId,channelId,query) {
  return request({
    url: '/sip/record/devquery/' + deviceId + "/" + channelId,
    method: 'get',
    params: query
  })
}

export function getRecord(channelId,sn) {
  return request({
    url: '/sip/record/query/' + channelId + "/" + sn,
    method: 'get',
  })
}
