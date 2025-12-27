import axios from 'axios'

const API_BASE_URL = 'http://localhost:8123/api'

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000
})

// SSE流式请求处理函数
export const createSSEConnection = (url, params = {}, onMessage, onError, onComplete) => {
  // 手动构建查询字符串，确保参数正确编码
  const queryParams = new URLSearchParams()
  Object.keys(params).forEach(key => {
    if (params[key] !== null && params[key] !== undefined) {
      queryParams.append(key, params[key])
    }
  })

  const fullUrl = url + '?' + queryParams.toString()
  console.log('SSE URL:', fullUrl) // 调试用

  const eventSource = new EventSource(fullUrl)
  let hasReceivedData = false
  let connectionOpened = false

  eventSource.onopen = () => {
    console.log('SSE connection opened')
    connectionOpened = true
  }

  eventSource.onmessage = (event) => {
    console.log('SSE message received:', event.data)
    hasReceivedData = true
    if (onMessage) {
      onMessage(event.data)
    }
  }

  eventSource.onerror = (error) => {
    console.error('SSE Error event triggered:', error)
    console.error('SSE readyState:', eventSource.readyState)
    console.error('SSE URL:', eventSource.url)
    console.error('Connection opened:', connectionOpened)
    console.error('Has received data:', hasReceivedData)

    // 如果连接曾经打开过且收到过数据，那么这个error可能是正常的流结束
    if (connectionOpened && hasReceivedData) {
      console.log('SSE stream appears to have completed normally (received data and connection was opened)')
      if (onComplete) {
        onComplete()
      }
      eventSource.close()
    } else {
      // 真正的连接错误
      console.error('SSE connection failed - no data received or connection never opened')
      if (onError) {
        onError(error)
      }
      eventSource.close()
    }
  }

  eventSource.addEventListener('complete', () => {
    console.log('SSE stream completed')
    if (onComplete) {
      onComplete()
    }
    eventSource.close()
  })

  eventSource.addEventListener('end', () => {
    console.log('SSE stream ended')
    if (onComplete) {
      onComplete()
    }
    eventSource.close()
  })

  // 添加超时机制，如果长时间没有收到数据，认为连接失败
  setTimeout(() => {
    if (!hasReceivedData && eventSource.readyState === 0) {
      console.error('SSE connection timeout - no data received within 10 seconds')
      eventSource.close()
      if (onError) {
        onError(new Error('Connection timeout'))
      }
    }
  }, 10000)

  return eventSource
}

// AI聊天接口
export const aiApi = {
  // AI恋爱大师聊天 (SSE流式)
  chatWithLoveApp: (message, chatId, onMessage, onError, onComplete) => {
    const url = `${API_BASE_URL}/ai/love_app/chat/sse`
    return createSSEConnection(url, { message, chatId }, onMessage, onError, onComplete)
  },

  // AI超级智能体聊天 (SSE流式)
  chatWithManus: (message, onMessage, onError, onComplete) => {
    const url = `${API_BASE_URL}/ai/manus/chat`
    return createSSEConnection(url, { message }, onMessage, onError, onComplete)
  }
}

export default api
