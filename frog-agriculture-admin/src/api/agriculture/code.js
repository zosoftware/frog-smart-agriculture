import request from '@/utils/request'

//生成溯源码
export function generateCode(data){
    return request({
        url: '/agriculture/code/generate',
        method: 'post',
        data: data
    })
}
// 查询溯源码列表
export function listCode(query) {
  return request({
    url: '/agriculture/code/list',
    method: 'get',
    params: query
  })
}

// 查询溯源码详细
export function getCode(codeId) {
  return request({
    url: '/agriculture/code/' + codeId,
    method: 'get'
  })
}

// 新增溯源码
export function addCode(data) {
  return request({
    url: '/agriculture/code',
    method: 'post',
    data: data
  })
}

// 修改溯源码
export function updateCode(data) {
  return request({
    url: '/agriculture/code',
    method: 'put',
    data: data
  })
}

// 删除溯源码
export function delCode(codeId) {
  return request({
    url: '/agriculture/code/' + codeId,
    method: 'delete'
  })
}
