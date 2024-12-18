<template>
  <div class="worker-login">
    <!-- 左侧装饰区域 -->
    <div class="login-left">
      <div class="left-overlay"></div>
      <div class="welcome-text">
        <div class="pharmacy-icon">💊</div>
        <h1>智慧药房管理</h1>
        <p>Smart Pharmacy Management</p>
      </div>
      <!-- 背景装饰元素 -->
      <div class="decoration-container">
        <div class="pill-pattern-1"></div>
        <div class="pill-pattern-2"></div>
        <div class="pill-pattern-3"></div>
      </div>
    </div>

    <!-- 右侧登录区域 -->
    <div class="login-content">
      <div class="back-button" @click="$router.push('/')">← 返回</div>
      <div class="form-container">
        <h2>{{ isLogin ? '药房工作站' : '工作人员注册' }}</h2>
        <p class="subtitle">{{ isLogin ? '请输入工号和密码' : '请填写注册信息' }}</p>

        <form @submit.prevent="handleSubmit">
          <div class="input-group">
            <input type="text" v-model="form.workId" placeholder="工号" required>
          </div>
          <div class="input-group">
            <input type="password" v-model="form.password" placeholder="密码" required>
          </div>

          <template v-if="!isLogin">
            <div class="input-group">
              <input type="password" v-model="form.confirmPassword" placeholder="确认密码" required>
            </div>
            <div class="input-group">
              <input type="text" v-model="form.name" placeholder="姓名" required>
            </div>
            <div class="input-group">
              <input type="email" v-model="form.email" placeholder="工作邮箱" required>
            </div>
          </template>

          <button type="submit" class="submit-btn">
            {{ isLogin ? '登 录' : '注 册' }}
          </button>
        </form>

        <p class="toggle-form" @click="isLogin = !isLogin">
          {{ isLogin ? '新员工？点击注册' : '已有账号？点击登录' }}
        </p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'WorkerLogin',
  data() {
    return {
      isLogin: true,
      form: {
        workId: '',
        password: '',
        confirmPassword: '',
        name: '',
        email: ''
      }
    }
  },
  methods: {
    handleSubmit() {
      console.log('Form submitted:', this.form)
    }
  }
}
</script>

<style scoped>
.worker-login {
  min-height: 100vh;
  display: flex;
  background: white;
}

.login-left {
  flex: 1;
  position: relative;
  overflow: hidden;
  background: #722ed1;
}

.left-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, #722ed1, #391085);
  opacity: 0.95;
}

.welcome-text {
  position: relative;
  z-index: 2;
  color: white;
  text-align: center;
  padding-top: 100px;
}

.pharmacy-icon {
  font-size: 64px;
  margin-bottom: 20px;
}

.welcome-text h1 {
  font-size: 36px;
  margin-bottom: 10px;
  font-weight: 600;
}

.welcome-text p {
  font-size: 18px;
  opacity: 0.9;
}

/* 药丸装饰图案 */
.decoration-container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 1;
  overflow: hidden;
}

.pill-pattern-1,
.pill-pattern-2,
.pill-pattern-3 {
  position: absolute;
  border-radius: 50px;
  background: rgba(255, 255, 255, 0.1);
}

.pill-pattern-1 {
  width: 200px;
  height: 80px;
  top: 20%;
  left: -40px;
  transform: rotate(-45deg);
}

.pill-pattern-2 {
  width: 150px;
  height: 60px;
  bottom: 30%;
  right: -30px;
  transform: rotate(45deg);
}

.pill-pattern-3 {
  width: 100px;
  height: 40px;
  top: 60%;
  left: 40%;
  transform: rotate(30deg);
}

.login-content {
  flex: 1;
  padding: 40px;
  position: relative;
  display: flex;
  align-items: center;
}

.back-button {
  position: absolute;
  top: 20px;
  left: 30px;
  color: #666;
  cursor: pointer;
  transition: color 0.3s;
}

.back-button:hover {
  color: #722ed1;
}

.form-container {
  max-width: 400px;
  margin: 0 auto;
  width: 100%;
}

h2 {
  font-size: 28px;
  color: #333;
  margin-bottom: 10px;
}

.subtitle {
  color: #666;
  margin-bottom: 30px;
}

.input-group {
  margin-bottom: 20px;
}

input {
  width: 100%;
  padding: 12px 15px;
  border: 2px solid #eee;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s;
}

input:focus {
  border-color: #722ed1;
  outline: none;
  box-shadow: 0 0 0 2px rgba(114, 46, 209, 0.2);
}

.submit-btn {
  width: 100%;
  padding: 12px;
  background: #722ed1;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s;
}

.submit-btn:hover {
  background: #531dab;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(114, 46, 209, 0.3);
}

.toggle-form {
  text-align: center;
  margin-top: 20px;
  color: #722ed1;
  cursor: pointer;
}

.toggle-form:hover {
  color: #531dab;
}

@media (max-width: 768px) {
  .worker-login {
    flex-direction: column;
  }

  .login-left {
    height: 200px;
  }

  .welcome-text {
    padding-top: 40px;
  }

  .form-container {
    padding: 20px;
  }
}
</style>