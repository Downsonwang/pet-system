<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="auth-header">
        <h2>{{ isLogin ? '前台工作站' : '创建账号' }}</h2>
        <p>{{ isLogin ? '欢迎使用宠物医院管理系统' : '注册成为新的工作人员' }}</p>
      </div>

      <transition name="fade" mode="out-in">
        <form
            :key="isLogin ? 'login' : 'register'"
            @submit.prevent="handleSubmit"
        >
          <!-- 登录表单 -->
          <template v-if="isLogin">
            <div class="input-group">
              <input
                  type="text"
                  placeholder="工号"
                  v-model="username"
                  required
              >
              <input
                  type="password"
                  placeholder="密码"
                  v-model="password"
                  required
              >
            </div>
            <button type="submit" class="submit-btn">登录</button>
            <p class="toggle-text">
              没有账号？
              <a href="#" @click.prevent="isLogin = false">立即注册</a>
            </p>
          </template>

          <!-- 注册表单 -->
          <template v-else>
            <div class="input-group">
              <input
                  type="text"
                  placeholder="姓名"
                  v-model="name"
                  required
              >
              <input
                  type="text"
                  placeholder="工号"
                  v-model="username"
                  required
              >
              <input
                  type="password"
                  placeholder="设置密码"
                  v-model="password"
                  required
              >
              <input
                  type="password"
                  placeholder="确认密码"
                  v-model="confirmPassword"
                  required
              >
            </div>
            <button type="submit" class="submit-btn">注册</button>
            <p class="toggle-text">
              已有账号？
              <a href="#" @click.prevent="isLogin = true">立即登录</a>
            </p>
          </template>
        </form>
      </transition>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isLogin: true,
      username: '',
      password: '',
      name: '',
      confirmPassword: ''
    }
  },
  methods: {
    handleSubmit() {
      if (this.isLogin) {
        this.login()
      } else {
        this.register()
      }
    },
    login() {
      // 登录逻辑
      if (!this.username || !this.password) {
        alert('请输入工号和密码')
        return
      }

      // 模拟登录 - 实际项目中替换为真实的 API 调用
      console.log('登录', this.username)
      // TODO: 实现真实的登录请求
    },
    register() {
      // 注册逻辑
      if (this.password !== this.confirmPassword) {
        alert('两次密码输入不一致')
        return
      }

      if (!this.name || !this.username || !this.password) {
        alert('请填写完整注册信息')
        return
      }

      // 模拟注册 - 实际项目中替换为真实的 API 调用
      console.log('注册', this.name, this.username)
      // TODO: 实现真实的注册请求
    }
  }
}
</script>

<style scoped>
.auth-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #eb2f96, #c41d7f);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.auth-card {
  background: white;
  padding: 40px;
  border-radius: 20px;
  width: 400px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}

.auth-header {
  text-align: center;
  margin-bottom: 30px;
}

.auth-header h2 {
  color: #333;
  margin-bottom: 10px;
}

.auth-header p {
  color: #666;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-bottom: 20px;
}

.input-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.input-group input:focus {
  outline: none;
  border-color: #eb2f96;
  box-shadow: 0 0 0 2px rgba(235, 47, 150, 0.1);
}

.submit-btn {
  width: 100%;
  padding: 12px;
  background-color: #eb2f96;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.submit-btn:hover {
  background-color: #c41d7f;
}

.toggle-text {
  text-align: center;
  margin-top: 15px;
  color: #666;
}

.toggle-text a {
  color: #eb2f96;
  text-decoration: none;
  font-weight: bold;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>