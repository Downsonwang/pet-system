<template>
  <div v-if="userInfo.username" class="app-container">
    <!-- 顶部导航栏 -->
    <nav class="top-nav">
      <div class="nav-container">
        <div class="nav-content">
          <!-- 左侧标题 -->
          <div class="nav-title">
            <h1>宠物医院管理系统</h1>
          </div>

          <!-- 右侧用户信息 -->
          <div class="user-section">
            <!-- 通知铃铛 -->
            <button class="notification-btn">
              <svg class="bell-icon" viewBox="0 0 24 24">
                <path d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"></path>
              </svg>
            </button>

            <!-- 用户信息下拉菜单 -->
            <div class="dropdown">
              <button @click="toggleDropdown" class="user-info-btn">
                <div class="avatar">{{ userInfo.username?.[0]?.toUpperCase() || 'U' }}</div>
                <div class="user-details">
                  <div class="username">{{ userInfo.username }}</div>
                  <div class="email">{{ userInfo.email }}</div>
                </div>
                <svg class="dropdown-arrow" viewBox="0 0 24 24">
                  <path d="M19 9l-7 7-7-7"/>
                </svg>
              </button>

              <!-- 下拉菜单内容 -->
              <div v-if="isDropdownOpen" class="dropdown-menu">
                <div class="dropdown-header">
                  <p class="dropdown-username">{{ userInfo.username }}</p>
                  <p class="dropdown-email">{{ userInfo.email }}</p>
                </div>

                <a href="#" class="dropdown-item">个人资料</a>

               <a href="#" class="dropdown-item" @click="goToAppointments">预约记录</a>
                <a href="#" class="dropdown-item">账户设置</a>
                <button @click="logout" class="logout-btn">退出登录</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </nav>

    <!-- 主要内容区域 -->
    <main class="main-content">
      <div class="content-container">
        <!-- 欢迎消息 -->
        <div class="welcome-card">
          <div class="welcome-content">
            <h2>欢迎回来，{{ userInfo.username }}</h2>
            <p>今天是 {{ currentDate }}</p>
          </div>
        </div>

        <!-- 功能区域 -->
        <div class="features-grid">
          <!-- 在线预约 -->
          <div class="feature-card">
            <div class="feature-header">
              <div class="feature-icon blue">
                <svg viewBox="0 0 24 24">
                  <path d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
              </div>
              <h3>在线预约</h3>
            </div>
            <p>选择医生和就诊时间进行预约挂号，快速便捷地安排就诊时间。</p>
            <button class="feature-btn blue" @click="showAppointmentDialog">
              立即预约
            </button>
          </div>

          <!-- 就诊记录 -->
          <div class="feature-card">
            <div class="feature-header">
              <div class="feature-icon green">
                <svg viewBox="0 0 24 24">
                  <path d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                </svg>
              </div>
              <h3>就诊记录</h3>
            </div>
            <p>查看宠物的历次就诊记录，了解治疗过程和健康历史。</p>
            <button class="feature-btn green" @click="showMedicalRecords">查看记录</button>
          </div>

          <!-- 费用查询 -->
          <div class="feature-card">
            <div class="feature-header">
              <div class="feature-icon purple">
                <svg viewBox="0 0 24 24">
                  <path d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </div>
              <h3>费用查询</h3>
            </div>
            <p>查询宠物就诊费用明细，了解每次就诊的具体花费。</p>
            <button class="feature-btn purple" @click="showExpenseDialog">查询费用</button>
          </div>

          <!-- 添加对话框组件 -->
          <medical-record-dialog
              v-model:visible="isShowMedicalRecord"
              :petId="currentPetId"
          />

          <medical-expense-dialog
              v-model:visible="isShowExpense"
              :recordId="selectedRecordId"
          />
          <!-- 在线咨询 -->
          <div class="feature-card">
            <div class="feature-header">
              <div class="feature-icon orange">
                <svg viewBox="0 0 24 24">
                  <path d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
                </svg>
              </div>
              <h3>在线咨询</h3>
            </div>
            <p>随时与医生在线交流，获取专业的诊疗建议和健康指导。</p>
            <button class="feature-btn orange">开始咨询</button>
          </div>
        </div>
      </div>
    </main>
    <appointment-dialog
        v-model:visible="isAppointmentDialogVisible"
        @success="handleAppointmentSuccess"
    />
  </div>
  <div v-else class="loading">加载中...</div>
  <appointment-record-dialog
      v-model:visible="isAppointmentRecordVisible"
      ref="appointmentRecordDialog"
  />
</template>

<script>
import AppointmentDialog from './AppointmentDialog.vue'
import AppointmentRecordDialog from './AppointmentRecordDialog.vue'
import MedicalRecordDialog from './MedicalRecordDialog.vue'
import MedicalExpenseDialog from './MedicalExpenseDialog.vue'

export default {
  name: 'Home',
  components: {
    AppointmentDialog,
    AppointmentRecordDialog,
    MedicalRecordDialog,
    MedicalExpenseDialog
  },
  data() {
    return {
      userInfo: {
        username: '',
        email: ''
      },
      isDropdownOpen: false,
      currentDate: new Date().toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        weekday: 'long'
      }),
      isAppointmentDialogVisible: false,
      isAppointmentRecordVisible: false,  // 添加预约记录弹窗控制
      isShowMedicalRecord: false,
      isShowExpense: false,
      currentPetId: null,
      selectedRecordId: null
    }
  },
  created() {
    try {
      // 从 localStorage 获取用户信息
      const storedUserInfo = localStorage.getItem('userInfo')
      if (storedUserInfo) {
        this.userInfo = JSON.parse(storedUserInfo)
      } else {
        // 如果没有用户信息，跳转回登录页
        this.$router.push('/login')
      }
    } catch (error) {
      console.error('Error loading user info:', error)
      this.$router.push('/login')
    }

    // 添加点击外部关闭下拉菜单
    document.addEventListener('click', this.handleClickOutside)
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside)
  },
  methods: {
    showMedicalRecords() {
      this.currentPetId = this.userInfo.petId; // 或者从其他地方获取当前宠物ID
      this.isShowMedicalRecord = true;
    },

    showExpenseDialog() {
      this.isShowExpense = true;
    },
    async goToAppointments() {
      this.isAppointmentRecordVisible = true;
      this.isDropdownOpen = false;
      // 获取用户预约记录
      const token = localStorage.getItem('token');
      try {
        const response = await fetch('http://localhost:8080/api/appointments/user', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.ok) {
          const data = await response.json();
          // 显示预约记录
          this.$emit('show-appointments', data);
        }
      } catch (error) {
        console.error('获取预约记录失败:', error);
      }
    },
    toggleDropdown() {
      this.isDropdownOpen = !this.isDropdownOpen
    },
    handleClickOutside(event) {
      const dropdown = document.querySelector('.dropdown')
      if (dropdown && !dropdown.contains(event.target)) {
        this.isDropdownOpen = false
      }
    },
    logout() {
      localStorage.removeItem('userInfo')
      localStorage.removeItem('token')
      this.$router.push('/login')
    },
    showAppointmentDialog() {
      this.isAppointmentDialogVisible = true
    },
    handleAppointmentSuccess(appointmentData) {
      // 这里处理预约成功后的逻辑
      console.log('预约成功:', appointmentData)
      // 可以显示成功提示
      this.$message.success('预约成功！')
      if (this.$refs.appointmentRecordDialog) {
        this.$refs.appointmentRecordDialog.loadAppointments()
      }
      // 可以刷新预约列表等
    }

  }
}
</script>

<style scoped>
.app-container {
  min-height: 100vh;
  background: linear-gradient(to bottom, #f8fafc, white);
}

/* 导航栏样式 */
.top-nav {
  background: white;
  border-bottom: 1px solid #e5e7eb;
  position: fixed;
  width: 100%;
  top: 0;
  z-index: 50;
}

.nav-container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 1rem;
}

.nav-content {
  display: flex;
  justify-content: space-between;
  height: 4rem;
  align-items: center;
}

.nav-title h1 {
  font-size: 1.25rem;
  font-weight: 600;
  color: #111827;
}

.user-section {
  display: flex;
  align-items: center;
  gap: 1rem;
}

/* 通知按钮样式 */
.notification-btn {
  padding: 0.5rem;
  color: #6b7280;
  background: none;
  border: none;
  border-radius: 9999px;
  cursor: pointer;
  transition: all 0.2s;
}

.notification-btn:hover {
  color: #374151;
  background: #f3f4f6;
}

.bell-icon {
  width: 1.5rem;
  height: 1.5rem;
  fill: none;
  stroke: currentColor;
  stroke-width: 2;
}

/* 用户信息样式 */
.dropdown {
  position: relative;
}

.user-info-btn {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.5rem;
  background: white;
  border: none;
  border-radius: 0.5rem;
  cursor: pointer;
  transition: all 0.2s;
}

.user-info-btn:hover {
  background: #f9fafb;
}

.avatar {
  width: 2rem;
  height: 2rem;
  background: linear-gradient(to right, #3b82f6, #2563eb);
  color: white;
  border-radius: 9999px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 500;
}

.dropdown-menu {
  position: absolute;
  right: 0;
  margin-top: 0.5rem;
  width: 12rem;
  background: white;
  border-radius: 0.5rem;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.dropdown-header {
  padding: 0.75rem 1rem;
  border-bottom: 1px solid #e5e7eb;
}

.dropdown-username {
  font-size: 0.875rem;
  font-weight: 500;
  color: #111827;
}

.dropdown-email {
  font-size: 0.75rem;
  color: #6b7280;
  overflow: hidden;
  text-overflow: ellipsis;
}

.dropdown-item {
  display: block;
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
  color: #374151;
  text-decoration: none;
  transition: background-color 0.2s;
}

.dropdown-item:hover {
  background: #f9fafb;
}

.logout-btn {
  width: 100%;
  text-align: left;
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
  color: #dc2626;
  background: none;
  border: none;
  cursor: pointer;
}

.logout-btn:hover {
  background: #fee2e2;
}

/* 主要内容区域样式 */
.main-content {
  padding-top: 4rem;
}

.content-container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 2rem 1rem;
}

/* 欢迎卡片样式 */
.welcome-card {
  background: white;
  border-radius: 0.5rem;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
  padding: 1.5rem;
  margin-bottom: 2rem;
}

.welcome-card h2 {
  font-size: 1.5rem;
  font-weight: 700;
  color: #111827;
}

.welcome-card p {
  margin-top: 0.25rem;
  color: #6b7280;
}

/* 功能区域样式 */
.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 1.5rem;
}

.feature-card {
  background: white;
  border-radius: 0.5rem;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
  padding: 1.5rem;
  transition: box-shadow 0.3s;
}

.feature-card:hover {
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.feature-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1rem;
}

.feature-icon {
  padding: 0.5rem;
  border-radius: 0.5rem;
  width: 2.5rem;
  height: 2.5rem;
}

<style scoped>
   /* ... previous styles ... */

 .feature-icon svg {
   width: 1.5rem;
   height: 1.5rem;
   fill: none;
   stroke: currentColor;
   stroke-width: 2;
 }

.feature-icon.blue {
  background: #eff6ff;
  color: #3b82f6;
}

.feature-icon.green {
  background: #f0fdf4;
  color: #22c55e;
}

.feature-icon.purple {
  background: #faf5ff;
  color: #a855f7;
}

.feature-icon.orange {
  background: #fff7ed;
  color: #f97316;
}

.feature-header h3 {
  font-size: 1.125rem;
  font-weight: 600;
  color: #111827;
}

.feature-card p {
  color: #6b7280;
  margin-bottom: 1rem;
}

.feature-btn {
  width: 100%;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
  color: white;
  cursor: pointer;
  transition: all 0.2s;
}

.feature-btn:hover {
  transform: translateY(-1px);
}

.feature-btn:active {
  transform: translateY(0);
}

.feature-btn.blue {
  background: #3b82f6;
}

.feature-btn.blue:hover {
  background: #2563eb;
}

.feature-btn.green {
  background: #22c55e;
}

.feature-btn.green:hover {
  background: #16a34a;
}

.feature-btn.purple {
  background: #a855f7;
}

.feature-btn.purple:hover {
  background: #9333ea;
}

.feature-btn.orange {
  background: #f97316;
}

.feature-btn.orange:hover {
  background: #ea580c;
}

/* 加载状态样式 */
.loading {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #6b7280;
}

/* 响应式布局 */
@media (max-width: 768px) {
  .nav-title {
    display: none;
  }

  .user-details {
    display: none;
  }

  .feature-card {
    padding: 1rem;
  }

  .welcome-card {
    padding: 1rem;
  }
}

/* 添加平滑滚动 */
html {
  scroll-behavior: smooth;
}

/* 优化滚动条 */
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #555;
}
</style>