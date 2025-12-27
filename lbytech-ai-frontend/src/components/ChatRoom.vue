<template>
  <div class="chat-room">
  <header class="chat-header">
    <button @click="$router.push('/')" class="back-btn">← 返回主页</button>
    <h1>{{ title }}</h1>
    <div class="chat-info">
      <div class="chat-id">聊天ID: {{ chatId }}</div>
      <button @click="testConnection" class="test-btn" :disabled="isTesting">测试连接</button>
    </div>
  </header>

    <div class="chat-messages" ref="messagesContainer">
      <div
        v-for="(message, index) in messages"
        :key="index"
        :class="['message', message.type]"
      >
        <div class="message-content">
          <div class="message-text">
            {{ message.text }}
            <span v-if="message.isTyping" class="typing-cursor">|</span>
          </div>
          <div v-if="message.isTyping && typingQueue.length > 0" class="stream-indicator">
            🔄 正在接收AI回复... 
          </div>
          <div class="message-time">{{ formatTime(message.timestamp) }}</div>
        </div>
      </div>

    </div>

    <div class="chat-input">
      <form @submit.prevent="sendMessage" class="input-form">
        <input
          v-model="inputMessage"
          type="text"
          placeholder="输入您的问题..."
          :disabled="isTyping"
          class="message-input"
          ref="messageInput"
        />
        <button
          type="submit"
          :disabled="!inputMessage.trim() || isTyping"
          class="send-btn"
        >
          发送
        </button>
      </form>
    </div>
  </div>
</template>

<script>
import { aiApi } from '../services/api.js'

export default {
  name: 'ChatRoom',
  props: {
    title: {
      type: String,
      required: true
    },
    chatType: {
      type: String,
      required: true,
      validator: (value) => ['love', 'manus'].includes(value)
    }
  },
  data() {
    return {
      chatId: '',
      messages: [],
      inputMessage: '',
      isTyping: false,
      eventSource: null,
      isTesting: false,
      typingQueue: [], // 待显示的字符队列
      typingSpeed: 30, // 打字速度（毫秒）
      typingTimer: null
    }
  },
  mounted() {
    this.generateChatId()
    this.scrollToBottom()
  },
  beforeUnmount() {
    this.closeConnection()
    this.stopTypingEffect()
    this.typingQueue = [] // 清空打字队列
  },
  methods: {
    generateChatId() {
      this.chatId = 'chat_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
    },

    sendMessage() {
      const message = this.inputMessage.trim()
      if (!message || this.isTyping) return

      // 添加用户消息
      this.addMessage(message, 'user')

      // 清空输入框
      this.inputMessage = ''

      // 开始AI回复
      this.startAIResponse(message)

      this.$nextTick(() => {
        this.scrollToBottom()
      })
    },

    startAIResponse(userMessage) {
      this.isTyping = true
      this.typingQueue = []

      // 添加一个空的AI消息到消息列表
      const aiMessage = { text: '', type: 'ai', timestamp: new Date(), isTyping: true }
      this.messages.push(aiMessage)

      const onMessage = (data) => {
        if (data && data !== '[DONE]') {
          console.log('🔄 收到SSE数据块:', data)

          // 在处理新数据块前，添加一个短暂的停顿，让用户看到分块效果
          setTimeout(() => {
            // 将收到的数据拆分为字符并添加到队列
            for (let char of data) {
              this.typingQueue.push(char)
            }
            console.log('📝 添加到打字队列，当前队列长度:', this.typingQueue.length)
            // 开始或继续打字效果
            this.processTypingQueue(aiMessage)
          }, 200) // 200ms的停顿，让分块更明显
        }
      }

      const onError = (error) => {
        console.error('Chat error:', error)
        this.isTyping = false
        this.stopTypingEffect()

        // 检查连接状态并提供更具体的错误信息
        const readyState = this.eventSource ? this.eventSource.readyState : null
        let errorMessage = '抱歉，无法连接到AI服务。'

        if (readyState === 2) {
          errorMessage += '\n\n可能的原因：\n1. 后端服务未启动\n2. 端口8123被占用\n3. CORS跨域问题\n4. 接口路径不匹配'
        } else if (readyState === 0) {
          errorMessage += '\n\n正在连接中，请稍等...'
        }

        aiMessage.text = errorMessage
        aiMessage.isTyping = false
        this.closeConnection()
      }

      const onComplete = () => {
        console.log('Chat stream completed successfully')
        this.isTyping = false
        // 等待打字队列处理完成
        this.waitForTypingComplete(aiMessage)
      }

      // 根据聊天类型调用不同的API
      if (this.chatType === 'love') {
        this.eventSource = aiApi.chatWithLoveApp(userMessage, this.chatId, onMessage, onError, onComplete)
      } else if (this.chatType === 'manus') {
        this.eventSource = aiApi.chatWithManus(userMessage, onMessage, onError, onComplete)
      }
    },

    addMessage(text, type) {
      this.messages.push({
        text,
        type,
        timestamp: new Date()
      })
    },

    closeConnection() {
      if (this.eventSource) {
        this.eventSource.close()
        this.eventSource = null
      }
    },

    scrollToBottom() {
      const container = this.$refs.messagesContainer
      if (container) {
        container.scrollTop = container.scrollHeight
      }
    },

    formatTime(timestamp) {
      return timestamp.toLocaleTimeString('zh-CN', {
        hour: '2-digit',
        minute: '2-digit'
      })
    },

    processTypingQueue(message) {
      if (this.typingTimer || this.typingQueue.length === 0) {
        return // 已经在处理或者队列为空
      }

      this.typingTimer = setInterval(() => {
        if (this.typingQueue.length > 0) {
          const nextChar = this.typingQueue.shift()
          message.text += nextChar
          this.$nextTick(() => {
            this.scrollToBottom()
          })
        } else {
          this.stopTypingEffect()
        }
      }, this.typingSpeed)
    },

    stopTypingEffect() {
      if (this.typingTimer) {
        clearInterval(this.typingTimer)
        this.typingTimer = null
      }
    },

    waitForTypingComplete(message) {
      // 如果还有正在处理的打字效果，等待完成
      if (this.typingQueue.length > 0 || this.typingTimer) {
        setTimeout(() => {
          this.waitForTypingComplete(message)
        }, 100)
      } else {
        // 打字效果完成
        message.isTyping = false
        this.closeConnection()
        this.$nextTick(() => {
          this.scrollToBottom()
        })
      }
    },

    testConnection() {
      this.isTesting = true

      // 添加测试消息
      this.addMessage('正在测试与后端服务的连接...', 'user')

      // 根据聊天类型选择不同的API
      let testConnection
      if (this.chatType === 'love') {
        testConnection = aiApi.chatWithLoveApp
      } else if (this.chatType === 'manus') {
        testConnection = aiApi.chatWithManus
      }

      if (testConnection) {
        const eventSource = this.chatType === 'love'
          ? testConnection(
              '测试连接',
              this.chatId,
              (data) => {
                // 只在第一次收到数据时显示成功消息
                if (!this.messages[this.messages.length - 1].text.includes('连接成功')) {
                  this.addMessage('连接成功！AI服务响应正常 ✅', 'ai')
                  this.isTesting = false
                }
              },
              (error) => {
                console.error('Connection test failed:', error)
                this.addMessage('连接失败，请检查后端服务状态 ❌', 'ai')
                this.isTesting = false
              },
              () => {
                this.isTesting = false
                console.log('Connection test completed')
              }
            )
          : testConnection(
              '测试连接',
              (data) => {
                // 只在第一次收到数据时显示成功消息
                if (!this.messages[this.messages.length - 1].text.includes('连接成功')) {
                  this.addMessage('连接成功！AI服务响应正常 ✅', 'ai')
                  this.isTesting = false
                }
              },
              (error) => {
                console.error('Connection test failed:', error)
                this.addMessage('连接失败，请检查后端服务状态 ❌', 'ai')
                this.isTesting = false
              },
              () => {
                this.isTesting = false
                console.log('Connection test completed')
              }
            )

        // 5秒后超时
        setTimeout(() => {
          if (this.isTesting) {
            eventSource.close()
            this.isTesting = false
            this.addMessage('连接测试超时，请检查后端服务是否运行在 http://localhost:8123 ❌', 'ai')
          }
        }, 5000)
      }
    }
  }
}
</script>

<style scoped>
.chat-room {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f5f5;
}

.chat-header {
  background: white;
  padding: 15px 20px;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.back-btn {
  background: #667eea;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.back-btn:hover {
  background: #5a67d8;
}

.chat-header h1 {
  margin: 0;
  color: #333;
  font-size: 1.2rem;
  flex: 1;
}

.chat-info {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 5px;
}

.chat-id {
  font-size: 12px;
  color: #666;
  background: #f0f0f0;
  padding: 4px 8px;
  border-radius: 10px;
}

.test-btn {
  background: #28a745;
  color: white;
  border: none;
  padding: 4px 8px;
  border-radius: 10px;
  font-size: 12px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.test-btn:hover:not(:disabled) {
  background: #218838;
}

.test-btn:disabled {
  background: #6c757d;
  cursor: not-allowed;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message {
  display: flex;
  margin-bottom: 12px;
}

.message.user {
  justify-content: flex-end;
}

.message.ai {
  justify-content: flex-start;
}

.message-content {
  max-width: 70%;
  padding: 12px 16px;
  border-radius: 18px;
  position: relative;
}

.message.user .message-content {
  background: #667eea;
  color: white;
  border-bottom-right-radius: 4px;
}

.message.ai .message-content {
  background: white;
  color: #333;
  border-bottom-left-radius: 4px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.message-text {
  white-space: pre-wrap;
  word-wrap: break-word;
  line-height: 1.4;
}

.message-time {
  font-size: 11px;
  opacity: 0.7;
  margin-top: 4px;
}

.typing-cursor {
  color: #667eea;
  font-weight: bold;
  animation: blink 1s infinite;
}

.stream-indicator {
  font-size: 12px;
  color: #666;
  margin-top: 4px;
  padding: 4px 8px;
  background: #f0f0f0;
  border-radius: 12px;
  display: inline-block;
  animation: pulse 2s infinite;
}

@keyframes blink {
  0%, 50% {
    opacity: 1;
  }
  51%, 100% {
    opacity: 0;
  }
}

@keyframes pulse {
  0%, 100% {
    opacity: 0.7;
  }
  50% {
    opacity: 1;
  }
}

.chat-input {
  background: white;
  padding: 20px;
  border-top: 1px solid #e0e0e0;
}

.input-form {
  display: flex;
  gap: 12px;
  max-width: 800px;
  margin: 0 auto;
}

.message-input {
  flex: 1;
  padding: 12px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 25px;
  font-size: 16px;
  outline: none;
  transition: border-color 0.3s;
}

.message-input:focus {
  border-color: #667eea;
}

.message-input:disabled {
  background: #f5f5f5;
  cursor: not-allowed;
}

.send-btn {
  padding: 12px 24px;
  background: #667eea;
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
  white-space: nowrap;
}

.send-btn:hover:not(:disabled) {
  background: #5a67d8;
}

.send-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .chat-header {
    padding: 10px 15px;
    gap: 10px;
  }

  .chat-header h1 {
    font-size: 1rem;
  }

  .chat-messages {
    padding: 15px;
  }

  .message-content {
    max-width: 85%;
  }

  .chat-input {
    padding: 15px;
  }

  .input-form {
    gap: 8px;
  }

  .send-btn {
    padding: 12px 16px;
    font-size: 14px;
  }
}
</style>
