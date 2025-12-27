<template>
  <div class="home">
    <header class="header">
      <h1>LBYTECH AI Agent</h1>
      <p>选择您要使用的AI应用</p>
    </header>

    <main class="main">
      <div class="app-grid">
        <div class="app-card" @click="$router.push('/love-app')">
          <div class="app-icon">💕</div>
          <h2>AI恋爱大师</h2>
          <p>专业的恋爱咨询助手，为您提供贴心的情感建议</p>
        </div>

        <div class="app-card" @click="$router.push('/manus-app')">
          <div class="app-icon">🤖</div>
          <h2>AI超级智能体</h2>
          <p>强大的AI助手，助您解决各种复杂问题</p>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
export default {
  name: 'Home'
}
</script>

<style scoped>
.home {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  flex-direction: column;
}

.header {
  text-align: center;
  padding: 60px 20px 40px;
  color: white;
}

.header h1 {
  font-size: 3rem;
  margin-bottom: 10px;
  font-weight: 300;
}

.header p {
  font-size: 1.2rem;
  opacity: 0.9;
}

.main {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.app-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
  max-width: 800px;
  width: 100%;
}

.app-card {
  background: white;
  border-radius: 20px;
  padding: 40px 30px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.app-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
}

.app-icon {
  font-size: 4rem;
  margin-bottom: 20px;
}

.app-card h2 {
  font-size: 1.5rem;
  margin-bottom: 15px;
  color: #333;
}

.app-card p {
  color: #666;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .header h1 {
    font-size: 2rem;
  }

  .app-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .app-card {
    padding: 30px 20px;
  }
}
</style>
