# LBYTECH AI Agent 前端项目

基于Vue3开发的AI聊天应用前端，支持两个AI应用：AI恋爱大师和AI超级智能体。

## 功能特性

- 🏠 **主页**: 优雅的应用选择界面
- 💕 **AI恋爱大师**: 专业的恋爱咨询助手，支持实时流式对话
- 🤖 **AI超级智能体**: 强大的AI助手，支持复杂问题解答
- 📱 **响应式设计**: 支持桌面端和移动端
- 🔄 **SSE流式响应**: 实时显示AI回复内容
- 🎨 **现代化UI**: 美观的聊天室界面设计

## 技术栈

- **Vue 3**: 现代化的JavaScript框架
- **Vue Router**: 官方路由管理器
- **Axios**: HTTP请求库
- **Vite**: 快速的构建工具
- **SSE (Server-Sent Events)**: 服务器推送技术

## 项目结构

```
src/
├── components/          # 公共组件
│   └── ChatRoom.vue     # 聊天室组件
├── views/              # 页面组件
│   ├── Home.vue        # 主页
│   ├── LoveApp.vue     # AI恋爱大师页面
│   └── ManusApp.vue    # AI超级智能体页面
├── services/           # 服务层
│   └── api.js          # API接口封装
├── router/             # 路由配置
│   └── index.js        # 路由定义
├── App.vue             # 根组件
└── main.js             # 应用入口
```

## 安装和运行

### 1. 安装依赖

```bash
npm install
```

### 2. 启动开发服务器

```bash
npm run dev
```

项目将在 `http://localhost:3000` 启动。

### 3. 构建生产版本

```bash
npm run build
```

### 4. 预览生产构建

```bash
npm run preview
```

## 后端接口

项目需要配合后端API服务运行。后端服务应运行在 `http://localhost:8123`。

### 接口说明

#### AI恋爱大师聊天
- **接口**: `GET /api/ai/love_app/chat/sse`
- **参数**:
  - `message`: 用户消息
  - `chatId`: 聊天室ID
- **返回**: SSE流式响应

#### AI超级智能体聊天
- **接口**: `GET /api/ai/manus/chat`
- **参数**:
  - `message`: 用户消息
- **返回**: SSE流式响应

## 使用说明

1. 启动后端服务，确保运行在 `http://localhost:8123`
2. 启动前端开发服务器
3. 在主页选择要使用的AI应用
4. 在聊天界面输入问题，AI会实时回复
5. 每个会话都会生成唯一的聊天ID

## 开发说明

- 聊天室组件支持两种AI类型，通过 `chat-type` 属性区分
- SSE连接在组件卸载时自动关闭
- 消息列表会自动滚动到最新消息
- 输入框在AI回复时会被禁用

## 注意事项

- 确保后端服务正常运行在 `http://localhost:8123`
- SSE功能需要浏览器支持Server-Sent Events
- 移动端体验已优化，但建议在现代浏览器中使用
- 如果连接失败，请使用聊天界面中的"测试连接"按钮进行诊断

## 故障排除

### SSE连接失败

1. **检查后端服务状态**
   ```bash
   # 检查端口8123是否被监听
   netstat -ano | findstr :8123
   ```

2. **测试API连接**
   - 在聊天界面点击"测试连接"按钮
   - 查看浏览器控制台的调试信息

3. **常见问题**
   - 后端服务未启动
   - 端口8123被其他程序占用
   - CORS跨域限制
   - 接口路径不匹配

4. **后端配置要求**
   ```java
   @GetMapping(value = "/ai/love_app/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
   @GetMapping("/ai/manus/chat")
   // 需要支持CORS跨域请求
   ```
