import request from '@/utils/request'

// 查询标准作业任务列表
export function listStandardJob(query) {
  return request({
    url: '/agriculture/standardJob/list',
    method: 'get',
    params: query
  })
}

// 查询标准作业任务详细
export function getStandardJob(jobId) {
  return request({
    url: '/agriculture/standardJob/' + jobId,
    method: 'get'
  })
}

// 新增标准作业任务
export function addStandardJob(data) {
  return request({
    url: '/agriculture/standardJob',
    method: 'post',
    data: data
  })
}

// 修改标准作业任务
export function updateStandardJob(data) {
  return request({
    url: '/agriculture/standardJob',
    method: 'put',
    data: data
  })
}

// 删除标准作业任务
export function delStandardJob(jobId) {
  return request({
    url: '/agriculture/standardJob/' + jobId,
    method: 'delete'
  })
}
