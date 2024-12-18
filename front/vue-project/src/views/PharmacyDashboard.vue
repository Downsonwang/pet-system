<template>
  <div class="app-container">
    <!-- 顶部导航栏 -->
    <nav class="top-nav">
      <div class="nav-container">
        <div class="nav-content">
          <div class="nav-title">
            <h1>宠物医院药房工作站</h1>
          </div>

          <!-- 右侧用户信息 -->
          <div class="user-section">
            <div class="dropdown">
              <button @click="toggleDropdown" class="user-info-btn">
                <div class="avatar">{{ userInfo.username?.[0]?.toUpperCase() || 'P' }}</div>
                <div class="user-details">
                  <div class="username">{{ userInfo.username }}</div>
                  <div class="role">药房工作人员</div>
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
            <h2>欢迎回来，药房</h2>
            <p>{{ currentDate }}</p>
          </div>
        </div>

        <!-- 功能区域 -->
        <div class="features-grid">
          <!-- 药品信息管理 -->
          <div class="feature-card">
            <div class="feature-header">
              <div class="feature-icon blue">
                <svg viewBox="0 0 24 24">
                  <path d="M19 21H5a2 2 0 01-2-2V5a2 2 0 012-2h11l5 5v11a2 2 0 01-2 2z" />
                </svg>
              </div>
              <h3>药品信息管理</h3>
            </div>
            <p>查看和管理药品信息</p>
            <button class="feature-btn blue" @click="showMedicineInfo">
              管理药品
            </button>
          </div>

          <!-- 药品库存管理 -->
          <div class="feature-card">
            <div class="feature-header">
              <div class="feature-icon green">
                <svg viewBox="0 0 24 24">
                  <path d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v7a2 2 0 01-2 2H6a2 2 0 01-2-2v-7m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" />
                </svg>
              </div>
              <h3>药品库存管理</h3>
            </div>
            <p>管理药品入库和库存</p>
            <button class="feature-btn green" @click="showInventory">
              管理库存
            </button>
          </div>

          <!-- 处方处理 -->
          <div class="feature-card">
            <div class="feature-header">
              <div class="feature-icon purple">
                <svg viewBox="0 0 24 24">
                  <path d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4" />
                </svg>
              </div>
              <h3>处方处理</h3>
            </div>
            <p>处理医生开具的电子处方</p>
            <button class="feature-btn purple" @click="showPrescriptions">
              处理处方
            </button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
export default {
  name: 'PharmacyDashboard',
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
      })
    }
  },
  created() {
    this.loadUserInfo()
  },
  methods: {
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
    showMedicineInfo() {
      // 实现药品信息管理逻辑
    },
    showInventory() {
      // 实现库存管理逻辑
    },
    showPrescriptions() {
      // 实现处方处理逻辑
    }
  }
}
</script>