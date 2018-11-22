import axios from 'axios'
import {message} from 'antd'
// import qs from 'qs'
// import {SystemConfig} from '@config/SystemConfig'; // 需要序列化的前提下使用 qs.stringify(data)

/**
 * TODO 暂不考虑Token的情况和form-data
 */

const HttpClient = axios.create({
    baseURL: '/', // 可能本地使用代理这样就ok了
    timeout: 10000, // 超时时间10秒
    responseType: 'json',
    withCredentials: false, // 跨域请求时是否需要使用凭证
    headers: {
        'Content-Type': 'application/json;charset=UTF-8'
    }

})
// Request拦截器
HttpClient.interceptors.request.use(config => {
    // 在发送请求前做的事情 比如：所有的请求都带上Token 若提交不能接受json格式则序列化
    /*
    if (config.method === 'post') {
       // 序列化
       config.data = qs.stringify(config.data)
    }

    if (localStorage.getItem(SystemConfig.xAuthTokenName)) {
        config.headers.Authorization = localStorage.getItem(SystemConfig.xAuthTokenName)
    }
    */
    if (localStorage.getItem('accessToken')) {
        config.headers.Authorization = localStorage.getItem('accessToken')
    }
    
    return config
}, error => {
    // 对请求错误做些事情
    // return Promise.reject(error)
    return new Promise(() => {
        console.log('看什么看，程序员小哥哥又有活干了')
    })
})

// Response拦截器
HttpClient.interceptors.response.use(response => {
    // 对响应数据做点事情
    if (response.status === 200) {
        return response
    } else {
        return new Promise(() => {
            console.log('看什么看，程序员小哥哥又有活干了')
        })
    }
}, error => {
    // 对响应错误做点什么
    const errCode = error.response.status
    switch (errCode) {
        case 400:
            console.log('错误请求')
            break
        case 401:
            // 权限处理 重新登录 清空token
            window.location.replace('/#/login')
            message.error('未授权，请重新登录')
            console.log('未授权，请重新登录')
            break
        case 403:
            message.error('请检查用户名或密码')
            break
        case 404:
            message.error('请求错误,未找到该资源')
            console.log('请求错误,未找到该资源')
            break
        case 405:
            console.log('请求方法未允许')
            break
        case 408:
            console.log('请求超时')
            break
        case 500:
            message.error('服务器端出错')
            console.log('服务器端出错')
            break
        case 501:
            console.log('网络未实现')
            break
        case 502:
            console.log('网络错误')
            break
        case 503:
            console.log('服务不可用')
            break
        case 504:
            console.log('网络超时')
            break
        default:
            message.error('未知错误')
    }


    return new Promise(() => {
        console.log('看什么看，程序员小哥哥又有活干了')
    })
})

export default HttpClient
