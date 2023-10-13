import request from '@/utils/request'

// 查询雇员列表
export function listEmployee(query) {
  return request({
    url: '/agriculture/employee/list',
    method: 'get',
    params: query
  })
}

// 查询雇员详细
export function getEmployee(employeeId) {
  return request({
    url: '/agriculture/employee/' + employeeId,
    method: 'get'
  })
}

// 新增雇员
export function addEmployee(data) {
  return request({
    url: '/agriculture/employee',
    method: 'post',
    data: data
  })
}

// 修改雇员
export function updateEmployee(data) {
  return request({
    url: '/agriculture/employee',
    method: 'put',
    data: data
  })
}

// 删除雇员
export function delEmployee(employeeId) {
  return request({
    url: '/agriculture/employee/' + employeeId,
    method: 'delete'
  })
}
