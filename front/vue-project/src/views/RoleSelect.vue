<template>
  <div class="role-container">
    <h1 class="role-title">选择您的身份</h1>
    <div class="role-grid">
      <div
          v-for="role in roles"
          :key="role.type"
          class="role-card"
          @click="selectRole(role.type)"
      >
        <div class="card-content">
          <div class="role-icon">{{ role.icon }}</div>
          <div class="role-name">{{ role.name }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RoleSelect',
  data() {
    return {
      roles: [
        { type: 'customer', name: '宠物主人', icon: '🏠', path: '/customer-login' },
        { type: 'doctor', name: '医生', icon: '👨‍⚕️', path: '/doctor-login' },
        { type: 'pharmacist', name: '药房工作人员', icon: '💊', path: '/worker-login' },
        { type: 'receptionist', name: '前台接待', icon: '💁‍♀️', path: '/front-login' },
        { type: 'admin', name: '系统管理员', icon: '⚙️', path: '/admin-login' }
      ]
    }
  },
  methods: {
    selectRole(roleType) {
      // 找到对应的路由路径
      const role = this.roles.find(r => r.type === roleType);
      if (role) {
        this.$router.push(role.path);
      }
    }
  }
}
</script>

<style scoped>
.role-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.role-title {
  font-size: 32px;
  color: #333;
  margin-bottom: 60px;
  text-align: center;
}

.role-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;
  max-width: 800px;
  width: 100%;
}

.role-card {
  background: white;
  border-radius: 12px;
  padding: 40px 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #eee;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.role-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: transparent;
  transition: all 0.3s ease;
}

.role-card:nth-child(1)::before { background: #1890ff; }  /* 宠物主人-蓝色 */
.role-card:nth-child(2)::before { background: #52c41a; }  /* 医生-绿色 */
.role-card:nth-child(3)::before { background: #722ed1; }  /* 药房-紫色 */
.role-card:nth-child(4)::before { background: #eb2f96; }  /* 前台-粉色 */
.role-card:nth-child(5)::before { background: #faad14; }  /* 管理员-金色 */

.role-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.1);
  border-color: transparent;
}

.role-icon {
  font-size: 48px;
  margin-bottom: 16px;
  transition: transform 0.3s ease;
}

.role-card:hover .role-icon {
  transform: scale(1.1);
}

.role-name {
  font-size: 18px;
  color: #333;
  font-weight: 500;
}

@media (max-width: 768px) {
  .role-grid {
    grid-template-columns: 1fr;
    max-width: 400px;
  }

  .role-title {
    font-size: 24px;
    margin-bottom: 40px;
  }
}
</style>