<template>
  <div class="customer-login">
    <div class="login-left">
      <div class="image-overlay"></div>
      <div class="welcome-text">
        <h1>关爱宠物健康</h1>
        <div class="slogan-list">
          <p>专业医疗团队 · 全心全意服务</p>
          <p>Pet Care & Love</p>
        </div>
      </div>
    </div>
    <div class="login-content">
      <div class="back-button" @click="$router.push('/')">← 返回</div>
      <div class="form-container">
        <h2>{{ isLogin ? '欢迎回来' : '创建账号' }}</h2>
        <p class="subtitle">{{ isLogin ? '请输入您的账号信息' : '注册成为我们的会员' }}</p>

        <form @submit.prevent="handleSubmit">
          <template v-if="!isLogin">
            <div class="avatar-upload">
              <div class="avatar-preview" :class="{ 'has-avatar': avatarUrl }">
                <img v-if="avatarUrl" :src="avatarUrl" alt="头像预览">
                <div v-else class="upload-icon">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/>
                    <polyline points="17 8 12 3 7 8"/>
                    <line x1="12" y1="3" x2="12" y2="15"/>
                  </svg>
                </div>
              </div>
              <input
                  type="file"
                  ref="fileInput"
                  @change="handleAvatarChange"
                  accept="image/*"
                  class="file-input"
              >
              <button type="button" class="upload-btn" @click="triggerFileInput">
                {{ avatarUrl ? '更换头像' : '上传头像' }}
              </button>
            </div>
          </template>

          <div class="input-group">
            <input type="text" v-model="form.username" placeholder="用户名" required>
          </div>
          <div class="input-group">
            <input type="password" v-model="form.password" placeholder="密码" required>
          </div>

          <template v-if="!isLogin">
            <div class="input-group">
              <input type="password" v-model="form.confirmPassword" placeholder="确认密码" required>
            </div>
            <div class="input-group">
              <input type="email" v-model="form.email" placeholder="电子邮箱" required>
            </div>
          </template>

          <button type="submit" class="submit-btn">
            {{ isLogin ? '登 录' : '注 册' }}
          </button>
        </form>

        <p class="toggle-form" @click="isLogin = !isLogin">
          {{ isLogin ? '还没有账号？立即注册' : '已有账号？立即登录' }}
        </p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CustomerLogin',
  data() {
    return {
      isLogin: true,
      avatarUrl: '',
      form: {
        username: '',
        password: '',
        confirmPassword: '',
        email: '',
        avatar: null
      }
    }
  },
  methods: {
    triggerFileInput() {
      this.$refs.fileInput.click()
    },
    handleAvatarChange(event) {
      const file = event.target.files[0]
      if (file) {
        if (file.size > 5 * 1024 * 1024) {
          alert('图片大小不能超过 5MB')
          return
        }

        const reader = new FileReader()
        reader.onload = (e) => {
          this.avatarUrl = e.target.result
          this.form.avatar = file
        }
        reader.readAsDataURL(file)
      }
    },
    async handleSubmit() {
      try {
        const formData = new FormData();
        formData.append('username', this.form.username);
        formData.append('password', this.form.password);

        const response = await fetch('http://localhost:8080/api/auth/login', {
          method: 'POST',
          body: formData,
          credentials: 'include'
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result = await response.json();

        if (result.code === 200) {
          // 保存token和用户信息
          localStorage.setItem('token', result.data.token);
          localStorage.setItem('userInfo', JSON.stringify({
            username: result.data.username,
            avatarUrl: result.data.avatarUrl,
            email: result.data.email
          }));

          // 跳转到首页
          this.$router.push('/home');
        } else {
          alert(result.message);
        }
      } catch (error) {
        console.error('Error:', error);
        alert('登录失败，请稍后重试');
      }
    }
  }
}
</script>

<style scoped>
.customer-login {
  min-height: 100vh;
  display: flex;
  background: white;
}

.login-left {
  flex: 1;
  position: relative;
  background-image: url('/images/dog.png');
  background-size: cover;
  background-position: center;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background:
      linear-gradient(45deg, rgba(250, 173, 20, 0.9), rgba(212, 136, 6, 0.9)),
      url("data:image/svg+xml,%3Csvg width='50' height='50' viewBox='0 0 50 50' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M25 10c-1.7 0-3 1.3-3 3s1.3 3 3 3 3-1.3 3-3-1.3-3-3-3zm-7 7c-1.7 0-3 1.3-3 3s1.3 3 3 3 3-1.3 3-3-1.3-3-3-3zm14 0c-1.7 0-3 1.3-3 3s1.3 3 3 3 3-1.3 3-3-1.3-3-3-3zm-7 7c-1.7 0-3 1.3-3 3s1.3 3 3 3 3-1.3 3-3-1.3-3-3-3z' fill='rgba(255,255,255,0.1)'/%3E%3C/svg%3E");
  background-size: 100%, 50px;
  background-repeat: no-repeat, repeat;
}

.welcome-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: white;
  text-align: center;
  z-index: 1;
  width: 80%;
}

.welcome-text h1 {
  font-size: 3em;
  margin-bottom: 0.5em;
  font-weight: 600;
  letter-spacing: 2px;
  text-shadow: 0 2px 4px rgba(0,0,0,0.1);
  animation: fadeInDown 0.8s ease-out;
}

.slogan-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.slogan-list p {
  font-size: 1.2em;
  opacity: 0.9;
  letter-spacing: 1px;
  text-shadow: 0 1px 2px rgba(0,0,0,0.1);
  animation: fadeInUp 0.8s ease-out;
}

.login-content {
  flex: 1;
  padding: 60px;
  display: flex;
  flex-direction: column;
  position: relative;
  background: white;
}

.back-button {
  position: absolute;
  top: 30px;
  left: 40px;
  color: #666;
  cursor: pointer;
  font-size: 1.1em;
  transition: color 0.3s;
}

.back-button:hover {
  color: #faad14;
}

.form-container {
  max-width: 400px;
  margin: auto;
  width: 100%;
}

h2 {
  font-size: 1.8em;
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
  font-size: 1em;
  transition: all 0.3s;
}

input:focus {
  border-color: #faad14;
  outline: none;
  box-shadow: 0 0 0 2px rgba(250, 173, 20, 0.2);
}

.avatar-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 24px;
}

.avatar-preview {
  width: 120px;
  height: 120px;
  border-radius: 60px;
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 12px;
  overflow: hidden;
  border: 2px dashed #d9d9d9;
  transition: all 0.3s;
  cursor: pointer;
}

.avatar-preview:hover {
  border-color: #faad14;
}

.avatar-preview.has-avatar {
  border: 2px solid #faad14;
}

.avatar-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.upload-icon {
  color: #999;
}

.file-input {
  display: none;
}

.upload-btn {
  padding: 8px 16px;
  background: transparent;
  border: 1px solid #faad14;
  color: #faad14;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 0.9em;
}

.upload-btn:hover {
  background: #faad14;
  color: white;
}

.submit-btn {
  width: 100%;
  padding: 12px;
  background: #faad14;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1.1em;
  cursor: pointer;
  transition: all 0.3s;
  margin-top: 10px;
}

.submit-btn:hover {
  background: #d48806;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(250, 173, 20, 0.3);
}

.toggle-form {
  text-align: center;
  margin-top: 20px;
  color: #faad14;
  cursor: pointer;
  transition: color 0.3s;
}

.toggle-form:hover {
  color: #d48806;
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 768px) {
  .customer-login {
    flex-direction: column;
  }

  .login-left {
    height: 200px;
  }

  .login-content {
    padding: 30px 20px;
  }

  .welcome-text h1 {
    font-size: 2em;
  }

  .slogan-list p {
    font-size: 1em;
  }

  .avatar-preview {
    width: 100px;
    height: 100px;
  }
}
</style>