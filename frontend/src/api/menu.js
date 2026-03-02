import axios from 'axios'

/**
 * 菜单管理 API 服务
 */

// API 基础路径
const BASE_URL = '/menu'

/**
 * 获取菜单树形结构
 * @returns {Promise} 菜单树数据
 */
export function getMenuTree() {
  return axios.get(`${BASE_URL}/tree`)
}

/**
 * 获取菜单详情
 * @param {Number} id 菜单ID
 * @returns {Promise} 菜单详情
 */
export function getMenuById(id) {
  return axios.get(`${BASE_URL}/${id}`)
}

/**
 * 获取子菜单列表
 * @param {Number} parentId 父菜单ID
 * @returns {Promise} 子菜单列表
 */
export function getMenuChildren(parentId) {
  return axios.get(`${BASE_URL}/children/${parentId}`)
}

/**
 * 添加菜单
 * @param {Object} data 菜单数据
 * @returns {Promise} 添加结果
 */
export function addMenu(data) {
  return axios.post(BASE_URL, data)
}

/**
 * 更新菜单
 * @param {Number} id 菜单ID
 * @param {Object} data 菜单数据
 * @returns {Promise} 更新结果
 */
export function updateMenu(id, data) {
  return axios.post(`${BASE_URL}/update`, { ...data, id })
}

/**
 * 删除菜单
 * @param {Number} id 菜单ID
 * @returns {Promise} 删除结果
 */
export function deleteMenu(id) {
  return axios.post(`${BASE_URL}/delete`, { id })
}

/**
 * 更新菜单状态
 * @param {Number} id 菜单ID
 * @param {String} status 状态 ('0'-正常, '1'-停用)
 * @returns {Promise} 更新结果
 */
export function updateMenuStatus(id, status) {
  return axios.post(`${BASE_URL}/status`, { id, status })
}
