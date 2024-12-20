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

    <!-- 右侧登录/注册区域 -->
    <div class="login-content">
      <div class="back-button" @click="$router.push('/')">← 返回</div>
      <div class="form-container">
        <h2>{{ isLogin ? '药房工作站' : '工作人员注册' }}</h2>
        <p class="subtitle">{{ isLogin ? '请输入用户名和密码' : '请填写注册信息' }}</p>

        <!-- 登录表单 -->
        <form @submit.prevent="handleSubmit" v-if="isLogin">
          <div class="input-group">
            <input type="text" v-model="form.username" placeholder="用户名" required>
          </div>
          <div class="input-group">
            <input type="password" v-model="form.password" placeholder="密码" required>
          </div>
          <button type="submit" class="submit-btn">登 录</button>
        </form>

        <!-- 注册表单 -->
        <form @submit.prevent="handleSubmit" v-else>
          <div class="input-group">
            <input type="text" v-model="form.username" placeholder="用户名" required>
          </div>
          <div class="input-group">
            <input type="password" v-model="form.password" placeholder="密码" required>
          </div>
          <div class="input-group">
            <input type="password" v-model="form.confirmPassword" placeholder="确认密码" required>
          </div>
          <div class="input-group">
            <input type="text" v-model="form.name" placeholder="姓名" required>
          </div>
          <div class="input-group">
            <input type="text" v-model="form.pharmacyDepartment" placeholder="药房部门" required>
          </div>
          <div class="input-group">
            <input type="email" v-model="form.email" placeholder="电子邮箱" required>
          </div>
          <div class="input-group">
            <input type="file" @change="handleFileChange" accept="image/*">
          </div>
          <button type="submit" class="submit-btn">注 册</button>
        </form>

        <!-- 切换登录/注册按钮 -->
        <div class="toggle-form">
          <span @click="toggleForm">
            {{ isLogin ? '新员工？点击注册' : '已有账号？点击登录' }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import {ElMessage} from "element-plus";

export default {
name: 'WorkerLogin',
data() {
return {
isLogin: true,
form: {
username: '',
password: '',
confirmPassword: '',
name: '',
pharmacyDepartment: '',  // 药房部门
email: '',
avatar: null
}
}
},
methods: {
  toggleForm() {
    this.isLogin = !this.isLogin;
    this.resetForm();
  },

  handleFileChange(e) {
    const file = e.target.files[0];
    if (file) {
      this.form.avatar = file;
    }
  },
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
this.$router.push('/worker/dashboard'); // 导航到药房工作台
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
formData.append('role', 'PHARMACIST');  // 使用枚举值
formData.append('pharmacyDepartment', this.form.pharmacyDepartment);
formData.append('name', this.form.name);

// 如果没有上传头像，使用默认头像
if (!this.form.avatar) {
const defaultAvatar = await this.createDefaultAvatarFile();
formData.append('avatar', defaultAvatar);
} else {
formData.append('avatar', this.form.avatar);
}

const response = await axios.post('http://localhost:8080/api/auth/register', formData);

if (response.data.code === 200) {
  localStorage.setItem('token', response.data.data.token);
  localStorage.setItem('userInfo', JSON.stringify(response.data.data));
  ElMessage.success('登录成功');
  this.$router.push('/worker/dashboard');  // 使用新的路由路径
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
pharmacyDepartment: '',
email: '',
avatar: null
};
},

async createDefaultAvatarFile() {
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

.toggle-form {
  text-align: center;
  margin-top: 20px;
}

.toggle-form span {
  color: #722ed1;
  cursor: pointer;
  transition: color 0.3s;
}

.toggle-form span:hover {
  color: #531dab;
  text-decoration: underline;
}
</style>