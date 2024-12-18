<template>
  <div class="login-container">
    <div class="login-box">
      <!-- 左侧装饰 -->
      <div class="login-left">
        <div class="left-content">
          <h1>宠物医院管理系统</h1>
          <p>Professional Pet Hospital Management System</p>
          <div class="decoration">
            <div class="circle circle-1"></div>
            <div class="circle circle-2"></div>
            <div class="circle circle-3"></div>
          </div>
        </div>
      </div>

      <!-- 右侧表单 -->
      <div class="login-right">
        <div class="form-container">
          <h2>{{ isLogin ? '欢迎回来' : '账号注册' }}</h2>
          <p class="sub-title">{{ isLogin ? '请输入您的账号信息' : '创建一个新的账号' }}</p>

          <form @submit.prevent="handleSubmit">
            <div class="form-group">
              <input
                  type="text"
                  v-model="form.username"
                  placeholder="用户名"
                  required
              />
            </div>

            <div class="form-group">
              <input
                  type="password"
                  v-model="form.password"
                  placeholder="密码"
                  required
              />
            </div>

            <template v-if="!isLogin">
              <div class="form-group">
                <input
                    type="password"
                    v-model="form.confirmPassword"
                    placeholder="确认密码"
                    required
                />
              </div>

              <div class="form-group">
                <input
                    type="email"
                    v-model="form.email"
                    placeholder="电子邮箱"
                    required
                />
              </div>

              <div class="form-group">
                <select v-model="form.role">
                  <option value="CUSTOMER">宠物主人</option>
                  <option value="DOCTOR">医生</option>
                </select>
              </div>
            </template>

            <button type="submit" class="submit-btn">
              {{ isLogin ? '登 录' : '注 册' }}
            </button>

            <div class="switch-form">
              <span @click="isLogin = !isLogin">
                {{ isLogin ? '没有账号？点击注册' : '已有账号？点击登录' }}
              </span>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LoginView',
  data() {
    return {
      isLogin: true,
      form: {
        username: '',
        password: '',
        confirmPassword: '',
        email: '',
        role: 'CUSTOMER'
      }
    }
  },
  methods: {
    handleSubmit() {
      if (this.isLogin) {
        console.log('登录表单提交', this.form)
      } else {
        console.log('注册表单提交', this.form)
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-box {
  width: 1000px;
  height: 600px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
  display: flex;
  overflow: hidden;
}

.login-left {
  flex: 1;
  background: linear-gradient(135deg, #1890ff 0%, #1677ff 100%);
  padding: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  position: relative;
  overflow: hidden;
}

.left-content {
  position: relative;
  z-index: 1;
  text-align: center;
}

.left-content h1 {
  font-size: 32px;
  margin-bottom: 20px;
  font-weight: 600;
}

.left-content p {
  font-size: 16px;
  opacity: 0.9;
}

.decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
}

.circle-1 {
  width: 200px;
  height: 200px;
  top: -100px;
  right: -100px;
}

.circle-2 {
  width: 300px;
  height: 300px;
  bottom: -150px;
  left: -150px;
}

.circle-3 {
  width: 150px;
  height: 150px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.login-right {
  flex: 1;
  padding: 40px;
  display: flex;
  align-items: center;
}

.form-container {
  width: 100%;
  padding: 0 40px;
}

h2 {
  font-size: 28px;
  color: #333;
  margin-bottom: 10px;
}

.sub-title {
  color: #666;
  margin-bottom: 30px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 12px 20px;
  border: 1px solid #e8e8e8;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s;
  background: #f5f5f5;
}

.form-group input:focus,
.form-group select:focus {
  background: white;
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
  outline: none;
}

.submit-btn {
  width: 100%;
  padding: 12px;
  background: #1890ff;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.submit-btn:hover {
  background: #096dd9;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.3);
}

.switch-form {
  text-align: center;
  margin-top: 20px;
}

.switch-form span {
  color: #1890ff;
  cursor: pointer;
  font-size: 14px;
}

.switch-form span:hover {
  color: #096dd9;
}

@media (max-width: 768px) {
  .login-box {
    flex-direction: column;
    height: auto;
    width: 100%;
    max-width: 500px;
  }

  .login-left {
    padding: 30px;
  }

  .left-content h1 {
    font-size: 24px;
  }

  .form-container {
    padding: 0 20px;
  }
}
</style>