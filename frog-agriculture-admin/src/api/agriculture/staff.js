import request from '@/utils/request'

// 查询溯源人员列表
export function listStaff(query) {
  return request({
    url: '/agriculture/staff/list',
    method: 'get',
    params: query
  })
}

// 查询溯源人员详细
export function getStaff(staffId) {
  return request({
    url: '/agriculture/staff/' + staffId,
    method: 'get'
  })
}

// 新增溯源人员
export function addStaff(data) {
  return request({
    url: '/agriculture/staff',
    method: 'post',
    data: data
  })
}

// 修改溯源人员
export function updateStaff(data) {
  return request({
    url: '/agriculture/staff',
    method: 'put',
    data: data
  })
}

// 删除溯源人员
export function delStaff(staffId) {
  return request({
    url: '/agriculture/staff/' + staffId,
    method: 'delete'
  })
}
