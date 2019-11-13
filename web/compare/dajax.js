import log from './log.js'
import setting from './setting.js'

let host
let unloginCount = 0 //多个请求只跳转一次到登录页
let modalShow = false //控制只弹一个modal
let isExp = false

function init(app, _host) {
  host = _host
  Object.assign(app, this)
  // 判断token
}

function setUnloginCount(_unloginCount) {
  unloginCount = _unloginCount
}

function request(param) {
  let url = param.url
  let jwt = wx.getStorageSync('baqi_jwt')
  let layer = param.layer
  let layerText = param.layerText
  layerText = layerText === undefined ? '提交中' : layerText
  let params = param.params
  let contentType = param.contentType
  if (layer) {
    wx.showLoading({
      title: layerText,
      mask: true
    })
  }
  let ajaxPromise = new Promise((resolve, reject) => {
    let startTime = new Date().getTime()
    wx.request({
      url: host + url,
      method: 'POST',
      header: {
        'content-type': contentType,
        'Cookie': 'baqi_jwt=' + jwt,
        'imfrom': 'MiniProgram'
      },
      data: params,
      success: res => {
        if (res.statusCode === 200) { // http请求200
          if (res.data.retcode === '0000') { // 后端接口正常返回
            resolve(res.data)
          } else if (res.data.retcode == '399' || res.data.retcode == '1000') { // 未登录的请求
            //  需要登录
            // 已经在执行登录则不再执行登录 login请求complete之后将该值置为0
            if (unloginCount > 0 || isExp) return
            login().then(loginResult => {
              let pages = getCurrentPages()
              if (loginResult.login !== 'login') {
                // 只有登录状态直接更新jwt 去注册页
                // 当前页面的路径
                let currentPage = pages[pages.length - 1]
                let url = currentPage.route
                let query = currentPage.options || {}
                let params = ''
                // 拼接参数
                Object.keys(query).forEach(key => {
                  params += key + '=' + query[key] + '&'
                })
                url = '/' + url + (params.length > 0 ? '?' + params.substring(0, params.length - 1) : '')
                wx.reLaunch({
                  url: '/pages/login/login?url=' + encodeURIComponent(url)
                })
              } else {
                // 取消了之前的请求 现在重新执行当前页面的请求
                var prevPage = pages[pages.length - 1]
                // 执行Im登录 和检测是否添加新绑定的家人
                getApp().onShow()
                getApp().onLaunch()
                if (prevPage) {
                  prevPage.onShow()
                  prevPage.onLoad(prevPage.options)
                }
              }
            })
          } else { // 其他返回码
            // handleError(res, reject)
            reject(res)
          }
        } else if (res.statusCode === 404) { // http请求404
          wx.navigateTo({
            url: '/pages/common/404',
          })
        } else { // http请求500
          // handleError(res, reject)
          reject(res)
        }
      },
      fail: err => {
        if (modalShow === false) {
          modalShow = true
          wx.showModal({
            title: '提示',
            content: '网络忙，请稍后再试',
            showCancel: false,
            success: function(res) {
              console.log(res)
              if (res.confirm) {
                modalShow = false
              }
            }
          })
        }
      },
      complete: cmp => {
        // let duration = new Date().getTime() - startTime
        // log.info('请求url：' + url, '请求参数：' + (params ? JSON.stringify(params) : '无'), '耗时：' + duration, 'statusCode：' + cmp.statusCode, '返回数据：' + JSON.stringify(cmp.data))
        if (layer) {
          wx.hideLoading()
        }
      }
    })
  })
  let oldThen = Promise.prototype.then
  ajaxPromise.then = (onFulfilled, onRejected) => {
    // 本来应该是 return oldThen.call(ajaxPromise, onFulfilled, onRejected)
    // 这里重写了onFulfilled 和 onRejected
    // 对onFulfilled进行try catch 捕获异常
    // onRejected若没有，则执行默认的异常处理
    return oldThen.call(ajaxPromise, function(res) {
      try {
        onFulfilled(res)
      } catch (e) {
        wx.hideLoading()
        // 开发测试环境alert错误
        if (['dev', 'test'].includes(setting().profile)) {
          wx.showModal({
            title: '前端错误',
            content: e.message,
          })
        }
        // 记录错误信息
        let pages = getCurrentPages()
        let page = pages.length > 0 && pages[pages.length - 1]
        let webLog = {
          page: page && page.__route__,
          stack: e.stack,
          errInfo: e.message
        }
        log.uploadError(webLog, wx.getSystemInfoSync())
        log.error('发生了错误！' + JSON.stringify(webLog))
      }
    }, onRejected || function(res) {
      wx.showToast({
        title: res.data.errMsg || '遇到了未知异常，请联系管理员！',
        icon: 'none',
        duration: 2000
      })
    });
  }
  return ajaxPromise
}

function formRequest(url, params) {
  return request({
    url: url,
    params: params,
    contentType: 'application/x-www-form-urlencoded'
  })
}

async function formRequestSync(url, params) {
  let result = await request({
    url: url,
    params: params,
    contentType: 'application/x-www-form-urlencoded'
  })
  return result
}

function formSubmitRequest(url, params, layerText) {
  if (layerText === undefined && typeof params === 'string') {
    layerText = params
    params = undefined
  }
  return request({
    layer: true,
    layerText: layerText,
    url: url,
    params: params,
    contentType: 'application/x-www-form-urlencoded'
  })
}

function jsonRequest(url, params) {
  return request({
    url: url,
    params: params,
    contentType: 'application/json'
  })
}

function jsonSubmitRequest(url, params, layerText) {
  if (layerText === undefined && typeof params === 'string') {
    layerText = params
    params = undefined
  }
  return request({
    layer: true,
    layerText: layerText,
    url: url,
    params: params,
    contentType: 'application/json'
  })
}

/**
 * login 直接登录
 * register 需要注册
 * needUserInfo 信息缺失 需要用户授权获取信息
 * fail 接口错误
 */
function login() {
  unloginCount++
  return new Promise(resolve => {
    wx.showLoading({
      title: '登陆中...',
    })
    wx.login({
      success(res) {
        if (res.code) {
          // 发起网络请求
          wx.request({
            url: host + '/auth/doctor/mini/login',
            method: 'POST',
            header: {
              'content-type': 'application/x-www-form-urlencoded',
              'imfrom': 'MiniProgram'
            },
            data: {
              code: res.code
            },
            success: (rs) => {
              if (rs.data.retcode == "0000") { //登录成功
                var regStatus = rs.data.body.registered;
                if (regStatus) { //已注册,更新jwt
                  wx.setStorageSync('baqi_jwt', rs.data.body.jwt);
                  wx.setStorageSync('role', rs.data.body.role);
                  // 直接登录
                  resolve({
                    login: 'login'
                  })
                } else {
                  let loginObj = rs.data.body
                  var param = {
                    data: loginObj,
                    login: 'register'
                  }
                  // 判断有没有unionid 如果没有需要前端再次授权获取用户信息
                  if (!loginObj.unionid) {
                    param.login = 'needUserInfo'
                  }
                  resolve(param)
                }
                wx.hideLoading();
              } else {
                wx.showModal({
                  title: '登录失败',
                  content: rs.errMsg,
                  cancelColor: '#4A4A4A',
                  confirmColor: '#775979',
                })
                resolve({
                  login: 'fail'
                })
              }
            },
            complete() {
              unloginCount = 0
              wx.hideLoading()
            }
          })
        } else {
          console.log('登录失败！' + res.errMsg)
        }
      }
    })
  })
}

/**体验一下 */
async function toExperience() {

  wx.setStorageSync('experienceAccount', '1');
  isExp = true
  let result = await formRequestSync('/auth/doctor/mini/tasteLogin', {
    mobile: '15522299999'
  })
  wx.setStorageSync('baqi_jwt', result.body.baqi_jwt);
  wx.setStorageSync('role', result.body.role);
  unloginCount = 0

  let app = getApp()
  app.onLaunch()
  app.onShow()
  wx.reLaunch({
    url: '/pages/task/index',
  })
  return {
    login: 'login'
  }
}

function changeExp() {
  isExp = !isExp
}

function getDoctorInfo(flag) {
  return new Promise(resolve => {
    // let doctorInfo = wx.getStorageSync('doctorInfo')
    // if (!doctorInfo || flag) {
    formRequest('/chronic/doctor/doctorInfo').then(res => {
      // doctorInfo = res.body
      // wx.setStorage({
      //   key: 'doctorInfo',
      //   data: doctorInfo,
      // })
      resolve(res.body)
    })
    // } else {
    //   resolve(doctorInfo)
    // }
  })
}

module.exports = {
  init: init,
  formRequest: formRequest,
  formSubmitRequest: formSubmitRequest,
  jsonRequest: jsonRequest,
  jsonSubmitRequest: jsonSubmitRequest,
  setUnloginCount: setUnloginCount,
  login: login,
  toExperience: toExperience,
  changeExp: changeExp,
  getDoctorInfo: getDoctorInfo,
  formRequestSync: formRequestSync
}
