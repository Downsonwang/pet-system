<template>
  <div class="login-container">
    <!-- 左侧保持不变 -->
    <div class="login-box">
      <div class="login-left">
        <!-- 左侧内容不变 -->
      </div>

      <!-- 右侧表单 -->
      <div class="login-right">
        <div class="form-container">
          <!-- 添加角色选择按钮组 -->
          <div class="role-selector" v-if="!selectedRole">
            <h2>选择您的身份</h2>
            <div class="role-buttons">
              <button class="role-btn" @click="selectRole('CUSTOMER')">
                <span class="role-icon">🏠</span>
                宠物主人
              </button>
              <button class="role-btn" @click="selectRole('DOCTOR')">
                <span class="role-icon">👨‍⚕️</span>
                医生
              </button>
              <button class="role-btn" @click="selectRole('PHARMACIST')">
                <span class="role-icon">💊</span>
                药房工作人员
              </button>
              <button class="role-btn" @click="selectRole('RECEPTIONIST')">
                <span class="role-icon">💁‍♀️</span>
                前台接待
              </button>
              <button class="role-btn" @click="selectRole('ADMIN')">
                <span class="role-icon">⚙️</span>
                系统管理员
              </button>
            </div>
          </div>

          <!-- 登录/注册表单 -->
          <div v-if="selectedRole" class="login-form">
            <div class="role-header">
              <button class="back-btn" @click="selectedRole = null">返回</button>
              <h2>{{ getRoleTitle }}</h2>
            </div>
            <p class="sub-title">{{ isLogin ? '请输入您的账号信息' : '创建一个新的账号' }}</p>

            <form @submit.prevent="handleSubmit">
              <!-- 表单内容基本保持不变 -->
              <div class="form-group">
                <input type="text" v-model="form.username" placeholder="用户名" required />
              </div>

              <div class="form-group">
                <input type="password" v-model="form.password" placeholder="密码" required />
              </div>

              <template v-if="!isLogin">
                <div class="form-group">
                  <input type="password" v-model="form.confirmPassword" placeholder="确认密码" required />
                </div>
                <div class="form-group">
                  <input type="email" v-model="form.email" placeholder="电子邮箱" required />
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
  </div>
</template>

<script>
export default {
  name: 'LoginView',
  data() {
    return {
      isLogin: true,
      selectedRole: null,
      form: {
        username: '',
        password: '',
        confirmPassword: '',
        email: ''
      }
    }
  },
  computed: {
    getRoleTitle() {
      const titles = {
        CUSTOMER: '宠物主人登录',
        DOCTOR: '医生登录',
        PHARMACIST: '药房工作人员登录',
        RECEPTIONIST: '前台接待登录',
        ADMIN: '系统管理员登录'
      }
      return titles[this.selectedRole] || ''
    }
  },
  methods: {
    selectRole(role) {
      this.selectedRole = role
      this.form.role = role
    },
    handleSubmit() {
      if (this.isLogin) {
        console.log('登录表单提交', {...this.form, role: this.selectedRole})
      } else {
        console.log('注册表单提交', {...this.form, role: this.selectedRole})
      }
    }
  }
}
</script>

<style scoped>
/* 添加新的样式 */
.role-selector {
  text-align: center;
  padding: 20px;
}

.role-buttons {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-top: 30px;
}

.role-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  border: 2px solid #e8e8e8;
  border-radius: 12px;
  background: white;
  cursor: pointer;
  transition: all 0.3s;
}

.role-btn:hover {
  border-color: #1890ff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.15);
}

.role-icon {
  font-size: 32px;
  margin-bottom: 10px;
}

.role-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.back-btn {
  padding: 8px 16px;
  border: none;
  background: transparent;
  color: #1890ff;
  cursor: pointer;
  margin-right: 20px;
}

.back-btn:hover {
  color: #096dd9;
}

/* 其他原有样式保持不变 */
</style>