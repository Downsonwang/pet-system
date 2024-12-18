<template>
  <div class="doctor-login">
    <div class="login-box">
      <!-- 左侧装饰区域 -->
      <div class="login-left">
        <div class="left-overlay"></div>
        <div class="welcome-text">
          <div class="doctor-icon">👨‍⚕️</div>
          <h1>医生工作站</h1>
          <p>Veterinary Workstation</p>
        </div>
      </div>

      <!-- 右侧登录区域 -->
      <div class="login-content">
        <div class="back-button" @click="$router.push('/')">← 返回</div>
        <div class="form-container">
          <h2>{{ isLogin ? '医生登录' : '医生注册' }}</h2>
          <p class="subtitle">{{ isLogin ? '欢迎回来，请输入您的工作信息' : '请填写注册信息' }}</p>

          <form @submit.prevent="handleSubmit">
            <div class="input-group">
              <input type="text" v-model="form.username" placeholder="工号" required>
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
                <input type="text" v-model="form.department" placeholder="科室" required>
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
            {{ isLogin ? '新医生？点击注册' : '已有账号？点击登录' }}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import { ref } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

export default {
  name: 'DoctorLogin',
  data() {
    return {
      isLogin: true,
      form: {
        username: '',
        password: '',
        confirmPassword: '',
        name: '',
        department: '',
        email: '',
        avatar: null  // 添加头像字段
      }
    }
  },
  methods: {
    async handleSubmit() {
      try {
        if (this.isLogin) {
          // 登录逻辑
          const formData = new FormData();
          formData.append('username', this.form.username);
          formData.append('password', this.form.password);

          const response = await axios.post('http://localhost:8080/api/auth/login', formData);

          if (response.data.code === 200) {
            // 保存token和用户信息
            localStorage.setItem('token', response.data.data.token);
            localStorage.setItem('userInfo', JSON.stringify(response.data.data));

            ElMessage.success('登录成功');
            this.$router.push('/doctor/dashboard'); // 导航到医生工作台
          } else {
            ElMessage.error(response.data.message || '登录失败');
          }
        } else {
          // 注册逻辑
          // 密码确认验证
          if (this.form.password !== this.form.confirmPassword) {
            ElMessage.error('两次输入的密码不一致');
            return;
          }

          const formData = new FormData();
          formData.append('username', this.form.username);
          formData.append('password', this.form.password);
          formData.append('email', this.form.email);
          formData.append('role', 'DOCTOR');  // 使用枚举值
          formData.append('specialization', this.form.department);
          formData.append('title', this.form.name);  // 这里使用name作为职称
          formData.append('introduction', '');  // 可以后续在个人资料中补充

          // 如果没有上传头像，使用默认头像
          if (!this.form.avatar) {
            // 创建一个默认头像的File对象
            const defaultAvatar = await this.createDefaultAvatarFile();
            formData.append('avatar', defaultAvatar);
          } else {
            formData.append('avatar', this.form.avatar);
          }

          const response = await axios.post('http://localhost:8080/api/auth/register', formData);

          if (response.data.code === 200) {
            ElMessage.success('注册成功，请登录');
            this.isLogin = true;
            this.resetForm();
          } else {
            ElMessage.error(response.data.message || '注册失败');
          }
        }
      } catch (error) {
        console.error('Error:', error);
        ElMessage.error(error.response?.data?.message || '操作失败，请重试');
      }
    },

    resetForm() {
      this.form = {
        username: '',
        password: '',
        confirmPassword: '',
        name: '',
        department: '',
        email: '',
        avatar: null
      };
    },

    async createDefaultAvatarFile() {
      // 创建一个空白的1x1像素的透明PNG
      const canvas = document.createElement('canvas');
      canvas.width = 1;
      canvas.height = 1;
      const ctx = canvas.getContext('2d');
      ctx.fillStyle = 'transparent';
      ctx.fillRect(0, 0, 1, 1);

      return new Promise((resolve) => {
        canvas.toBlob((blob) => {
          resolve(new File([blob], 'default-avatar.png', { type: 'image/png' }));
        }, 'image/png');
      });
    }
  }
}
</script>

<style scoped>
/* 之前的样式保持不变 */
.input-group {
  margin-bottom: 15px;
}

input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  transition: border-color 0.3s;
}

input:focus {
  border-color: #4CAF50;
  outline: none;
}

.submit-btn {
  width: 100%;
  padding: 12px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
}

.submit-btn:hover {
  background-color: #45a049;
}

.toggle-form {
  margin-top: 20px;
  text-align: center;
  color: #666;
  cursor: pointer;
}

.toggle-form:hover {
  color: #4CAF50;
}

.doctor-login {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f2f5;
  padding: 20px;
}

.login-box {
  display: flex;
  width: 1000px;
  height: 600px;
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
}

.login-left {
  flex: 1;
  position: relative;
  background-image: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M30 5L35 25H25L30 5zM30 35L25 55H35L30 35z' fill='rgba(255,255,255,0.1)'/%3E%3C/svg%3E");
  background-color: #52c41a;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.left-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #52c41a, #237804);
  opacity: 0.9;
}

.welcome-text {
  position: relative;
  z-index: 1;
  text-align: center;
}

.doctor-icon {
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

.login-content {
  flex: 1;
  padding: 40px;
  position: relative;
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
  color: #52c41a;
}

.form-container {
  max-width: 400px;
  margin: 40px auto;
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
  border-color: #52c41a;
  outline: none;
  box-shadow: 0 0 0 2px rgba(82, 196, 26, 0.2);
}

.submit-btn {
  width: 100%;
  padding: 12px;
  background: #52c41a;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s;
}

.submit-btn:hover {
  background: #389e0d;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(82, 196, 26, 0.3);
}

.toggle-form {
  text-align: center;
  margin-top: 20px;
  color: #52c41a;
  cursor: pointer;
}

.toggle-form:hover {
  color: #389e0d;
}

@media (max-width: 768px) {
  .login-box {
    flex-direction: column;
    height: auto;
    width: 90%;
  }

  .login-left {
    padding: 40px 20px;
  }

  .form-container {
    margin: 20px auto;
  }
}
</style>