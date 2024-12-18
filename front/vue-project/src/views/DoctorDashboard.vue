<template>
  <div class="app-container">
    <!-- 顶部导航栏 -->
    <nav class="top-nav">
      <div class="nav-container">
        <div class="nav-content">
          <div class="nav-title">
            <h1>宠物医院工作站</h1>
          </div>

          <!-- 右侧用户信息 -->
          <div class="user-section">
            <div class="dropdown">
              <button @click="toggleDropdown" class="user-info-btn">
                <div class="avatar">{{ userInfo.username?.[0]?.toUpperCase() || 'D' }}</div>
                <div class="user-details">
                  <div class="username">{{ userInfo.username }}</div>
                  <div class="role">医生</div>
                </div>
              </button>

              <div v-if="isDropdownOpen" class="dropdown-menu">
                <div class="dropdown-header">
                  <p class="dropdown-username">{{ userInfo.username }}</p>
                  <p class="dropdown-email">{{ userInfo.email }}</p>
                </div>
                <a href="#" class="dropdown-item">个人资料</a>
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
        <div class="welcome-card">
          <div class="welcome-content">
            <h2>欢迎回来，医生</h2>
            <p>{{ currentDate }}</p>
          </div>
        </div>

        <!-- 功能区域 -->
        <div class="features-grid">
          <!-- 预约列表 -->
          <div class="feature-card">
            <div class="feature-header">
              <div class="feature-icon blue">
                <svg viewBox="0 0 24 24">
                  <path d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
              </div>
              <h3>预约列表</h3>
            </div>
            <p>查看和处理患者预约请求</p>
            <button class="feature-btn blue" @click="showAppointments">
              查看预约
            </button>
          </div>

          <!-- 诊断记录 -->
          <div class="feature-card">
            <div class="feature-header">
              <div class="feature-icon green">
                <svg viewBox="0 0 24 24">
                  <path d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                </svg>
              </div>
              <h3>诊断记录</h3>
            </div>
            <p>查看诊断记录</p>
            <button class="feature-btn green" @click="showAllMedicalRecords">
              查看记录
            </button>
          </div>

          <!-- 电子处方 -->
          <div class="feature-card">
            <div class="feature-header">
              <div class="feature-icon purple">
                <svg viewBox="0 0 24 24">
                  <path d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4" />
                </svg>
              </div>
              <h3>电子处方</h3>
            </div>
            <p>开具电子处方</p>
            <button class="feature-btn purple" @click="showPrescription">
              开具处方
            </button>
          </div>

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
            <p>处理患者在线咨询</p>
            <button class="feature-btn orange" @click="showConsultations">
              查看咨询
            </button>
          </div>
        </div>
      </div>
    </main>


    <!-- 预约列表对话框 -->
    <appointment-list
        v-model:visible="isAppointmentListVisible"
        @create-record="showMedicalRecords"
    />


    <!-- 诊断记录对话框 -->
    <medical-record-dialog
        v-if="isMedicalRecordVisible"
        v-model:visible="isMedicalRecordVisible"
        :appointmentId="selectedAppointmentId"
        @success="handleRecordSuccess"
    />
    <doctor-records-list
        v-model:visible="isRecordsListVisible"
    />
  </div>
</template>

<script>
import AppointmentList from './AppointmentList.vue'
import MedicalRecordDialog from './MedicalRecordDialog.vue'
import DoctorRecordsList from './doctor/DoctorRecordsList.vue'


export default {
  name: 'DoctorDashboard',
  components:{
    AppointmentList,
    MedicalRecordDialog,
    DoctorRecordsList
  },
  data() {
    return {
      userInfo: {
        username: '',
        email: ''
      },
      isMedicalRecordVisible: false,
      isPrescriptionVisible: false,
      selectedAppointmentId: null,
      isAppointmentListVisible: false,
      isDropdownOpen: false,
      isRecordsListVisible: false,
      currentDate: new Date().toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        weekday: 'long'
      })
    }
  },
  created() {
    this.loadUserInfo()
  },
  methods: {
    showMedicalRecords(appointmentId) {
      console.log('Opening medical record with appointmentId:', appointmentId);
      this.selectedAppointmentId = appointmentId;
      this.isMedicalRecordVisible = true;
    },

    // 在 methods 中添加
    showAllMedicalRecords() {
      this.isRecordsListVisible = true;
    },

    handleRecordSuccess() {
      this.isMedicalRecordVisible = false
      this.isAppointmentListVisible = true;

      // 刷新预约列表
      this.$refs.appointmentList?.loadAppointments()
    },
    showPrescription(appointmentId) {
      this.selectedAppointmentId = appointmentId;
      this.isPrescriptionVisible = true;
    },
    loadUserInfo() {
      const storedUserInfo = localStorage.getItem('userInfo')
      if (storedUserInfo) {
        this.userInfo = JSON.parse(storedUserInfo)
      } else {
        this.$router.push('/login')
      }
    },
    toggleDropdown() {
      this.isDropdownOpen = !this.isDropdownOpen
    },
    logout() {
      localStorage.removeItem('userInfo')
      localStorage.removeItem('token')
      this.$router.push('/login')
    },
    showAppointments() {

        this.isAppointmentListVisible = true; // 显示预约列表对话框

    },

    showConsultations() {
      // 实现在线咨询展示逻辑
    }
  }
}
</script>


<style scoped>
.app-container {
  min-height: 100vh;
  background-color: #f5f7fa;
}

/* 顶部导航栏 */
.top-nav {
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08);
  position: fixed;
  width: 100%;
  top: 0;
  z-index: 100;
}

.nav-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.nav-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 64px;
}

.nav-title h1 {
  margin: 0;
  font-size: 20px;
  color: #333;
}

/* 用户信息区域 */
.user-section {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info-btn {
  display: flex;
  align-items: center;
  gap: 12px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 6px;
  transition: background-color 0.2s;
}

.user-info-btn:hover {
  background-color: #f5f7fa;
}

.avatar {
  width: 36px;
  height: 36px;
  background-color: #4f46e5;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
}

.user-details {
  text-align: left;
}

.username {
  font-weight: 500;
  color: #333;
}

.role {
  font-size: 12px;
  color: #666;
}

/* 下拉菜单 */
.dropdown {
  position: relative;
}

.dropdown-menu {
  position: absolute;
  right: 0;
  top: 100%;
  margin-top: 8px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  width: 200px;
  padding: 8px 0;
}

.dropdown-header {
  padding: 12px 16px;
  border-bottom: 1px solid #eee;
}

.dropdown-username {
  margin: 0;
  font-weight: 500;
  color: #333;
}

.dropdown-email {
  margin: 4px 0 0 0;
  font-size: 12px;
  color: #666;
}

.dropdown-item {
  display: block;
  padding: 8px 16px;
  color: #333;
  text-decoration: none;
  transition: background-color 0.2s;
}

.dropdown-item:hover {
  background-color: #f5f7fa;
}

.logout-btn {
  width: 100%;
  padding: 8px 16px;
  border: none;
  background: none;
  color: #dc2626;
  text-align: left;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.2s;
}

.logout-btn:hover {
  background-color: #fef2f2;
}

/* 主要内容区域 */
.main-content {
  padding-top: 84px;
  padding-bottom: 40px;
}

.content-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.welcome-card {
  background-color: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.welcome-content h2 {
  margin: 0;
  color: #111827;
  font-size: 24px;
}

.welcome-content p {
  margin: 8px 0 0 0;
  color: #6b7280;
}

/* 功能卡片网格 */
.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 24px;
}

.feature-card {
  background-color: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s, box-shadow 0.2s;
}

.feature-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.feature-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;
}

.feature-icon {
  width: 40px;
  height: 40px;
  padding: 8px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.feature-icon svg {
  width: 24px;
  height: 24px;
  stroke: currentColor;
  stroke-width: 2;
  fill: none;
}

.feature-icon.blue {
  background-color: #e0f2fe;
  color: #0284c7;
}

.feature-icon.green {
  background-color: #dcfce7;
  color: #16a34a;
}

.feature-icon.purple {
  background-color: #f3e8ff;
  color: #9333ea;
}

.feature-icon.orange {
  background-color: #ffedd5;
  color: #ea580c;
}

.feature-card h3 {
  margin: 0;
  font-size: 18px;
  color: #111827;
}

.feature-card p {
  margin: 8px 0 16px 0;
  color: #6b7280;
}

.feature-btn {
  width: 100%;
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: opacity 0.2s;
}

.feature-btn:hover {
  opacity: 0.9;
}

.feature-btn.blue {
  background-color: #0284c7;
  color: white;
}

.feature-btn.green {
  background-color: #16a34a;
  color: white;
}

.feature-btn.purple {
  background-color: #9333ea;
  color: white;
}

.feature-btn.orange {
  background-color: #ea580c;
  color: white;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .nav-title h1 {
    font-size: 18px;
  }

  .content-container {
    padding: 0 16px;
  }

  .features-grid {
    grid-template-columns: 1fr;
  }
}
</style>