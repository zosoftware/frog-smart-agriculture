import request from '@/utils/request'

// 查询批次任务工人列表
export function listTaskEmployee(query) {
  return request({
    url: '/agriculture/taskEmployee/list',
    method: 'get',
    params: query
  })
}

// 查询批次任务工人详细
export function getTaskEmployee(id) {
  return request({
    url: '/agriculture/taskEmployee/' + id,
    method: 'get'
  })
}

// 新增批次任务工人
export function addTaskEmployee(data) {
  return request({
    url: '/agriculture/taskEmployee',
    method: 'post',
    data: data
  })
}

// 修改批次任务工人
export function updateTaskEmployee(data) {
  return request({
    url: '/agriculture/taskEmployee',
    method: 'put',
    data: data
  })
}

// 根据id删除批次任务工人
export function delTaskEmployee(id) {
  return request({
    url: '/agriculture/taskEmployee/' + id,
    method: 'delete'
  })
}

// 根据taskId和employeeId删除批次任务工人
export function delTaskEmployeeByTaskIdAndEmployeeId(taskId,employeeId) {
  return request({
    url: '/agriculture/taskEmployee/' + taskId+'/'+employeeId,
    method: 'delete'
  })
}
