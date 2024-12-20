<template>
  <div class="dashboard-layout">
    <!-- 左侧导航 -->
    <aside class="sidebar">
      <div class="brand">
        <div class="brand-logo">💊</div>
        <h1>智慧药房管理</h1>
      </div>

      <!-- 主导航菜单 -->
      <nav class="nav-menu">
        <div
            v-for="item in menuItems"
            :key="item.key"
            class="nav-item"
            :class="{ active: currentMenu === item.key }"
            @click="switchMenu(item.key)"
        >
          <i class="nav-icon" :class="item.icon"></i>
          <span class="nav-label">{{ item.label }}</span>
          <span v-if="item.badge" class="nav-badge">{{ item.badge }}</span>
        </div>
      </nav>

      <!-- 底部用户信息 -->
      <div class="user-panel">
        <div class="user-avatar">
          <img :src="userInfo.avatar || '/default-avatar.jpg'" :alt="userInfo.username">
          <div class="user-status"></div>
        </div>
        <div class="user-info">
          <div class="user-name">{{ userInfo.username }}</div>
          <!-- 将department改为pharmacyDepartment -->
          <div class="user-role">{{ userInfo.department }}</div>
        </div>
      </div>
    </aside>

    <!-- 主内容区 -->
    <main class="main-container">
      <!-- 顶部栏 -->
      <header class="main-header">
        <div class="header-left">
          <h2>{{ currentMenuTitle }}</h2>
          <div class="breadcrumb">
            首页 / {{ currentMenuTitle }}
          </div>
        </div>

        <div class="header-right">
          <div class="search-box">
            <el-input
                v-model="searchQuery"
                placeholder="搜索..."
                prefix-icon="el-icon-search"
                clearable
            />
          </div>

          <div class="header-actions">
            <el-tooltip content="待处理处方" placement="bottom">
              <el-badge :value="5" class="action-item">
                <i class="el-icon-bell"></i>
              </el-badge>
            </el-tooltip>

            <el-dropdown trigger="click">
              <div class="action-item">
                <i class="el-icon-setting"></i>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>个人设置</el-dropdown-item>
                  <el-dropdown-item>系统设置</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </header>

      <!-- 核心内容区 -->
      <div class="main-content">
        <!-- 快捷统计卡片 -->
        <div class="stat-cards" v-if="currentMenu === 'medicines'">
          <div class="stat-card">
            <div class="stat-icon" style="background: #e6f7ff">
              <i class="el-icon-medicine-box" style="color: #1890ff"></i>
            </div>
            <div class="stat-info">
              <div class="stat-label">药品总数</div>
              <div class="stat-value">2,458</div>
              <div class="stat-trend up">
                <i class="el-icon-top-right"></i>
                较上月增长 12%
              </div>
            </div>
          </div>

          <div class="stat-card">
            <div class="stat-icon" style="background: #fff7e6">
              <i class="el-icon-warning" style="color: #faad14"></i>
            </div>
            <div class="stat-info">
              <div class="stat-label">库存预警</div>
              <div class="stat-value">18</div>
              <div class="stat-trend down">
                <i class="el-icon-bottom-right"></i>
                较上月减少 5%
              </div>
            </div>
          </div>

          <div class="stat-card">
            <div class="stat-icon" style="background: #f6ffed">
              <i class="el-icon-document" style="color: #52c41a"></i>
            </div>
            <div class="stat-info">
              <div class="stat-label">待处理处方</div>
              <div class="stat-value">5</div>
              <div class="stat-trend neutral">
                <i class="el-icon-right"></i>
                与上月持平
              </div>
            </div>
          </div>
        </div>

        <!-- 组件内容区 -->
        <div class="component-container">
          <keep-alive>
            <component :is="currentComponent"></component>
          </keep-alive>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'

import MedicineList from './MedicineList.vue'
import StockManagement from './components/StockManagement.vue'
import PrescriptionHandling from './components/PrescriptionHandling.vue'

const userInfo = ref({
  username: '',
  department: '',
  avatar: null
})

const router = useRouter()

onMounted(() => {
  const storedUserInfo = localStorage.getItem('userInfo')
  if (storedUserInfo) {
    userInfo.value = JSON.parse(storedUserInfo)
  } else {
    // 如果没有存储信息，可选择跳转回登录页
    // router.push('/login')
  }
})

// 导航菜单配置
const menuItems = [
  {
    key: 'medicines',
    label: '药品信息管理',
    icon: 'el-icon-medicine-box',
    component: MedicineList
  },
  {
    key: 'stock',
    label: '库存管理',
    icon: 'el-icon-box',
    component: StockManagement,
    badge: '18'
  },
  {
    key: 'prescriptions',
    label: '处方处理',
    icon: 'el-icon-document',
    component: PrescriptionHandling,
    badge: '5'
  }
]

const currentMenu = ref('medicines')

const currentMenuTitle = computed(() =>
    menuItems.find(item => item.key === currentMenu.value)?.label || ''
)

const currentComponent = computed(() =>
    menuItems.find(item => item.key === currentMenu.value)?.component
)

const switchMenu = (key) => {
  currentMenu.value = key
}

const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    router.push('/login')
  } catch {
    // 用户取消退出
  }
}
</script>

<style scoped>
/* 全局布局 */
.dashboard-layout {
  display: flex;
  min-height: 100vh;
  background-color: #f0f2f5;
}

/* 侧边栏样式 */
.sidebar {
  width: 260px;
  background: #001529;
  display: flex;
  flex-direction: column;
  transition: all 0.3s ease;
}

.brand {
  height: 64px;
  padding: 0 24px;
  display: flex;
  align-items: center;
  gap: 12px;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}

.brand-logo {
  font-size: 28px;
}

.brand h1 {
  color: white;
  font-size: 18px;
  margin: 0;
  white-space: nowrap;
}

/* 导航菜单 */
.nav-menu {
  flex: 1;
  padding: 16px 0;
}

.nav-item {
  padding: 12px 24px;
  display: flex;
  align-items: center;
  gap: 12px;
  color: rgba(255,255,255,0.65);
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
}

.nav-item:hover {
  color: white;
  background: rgba(255,255,255,0.1);
}

.nav-item.active {
  color: white;
  background: #1890ff;
}

.nav-badge {
  position: absolute;
  right: 24px;
  background: #ff4d4f;
  color: white;
  padding: 0 6px;
  border-radius: 10px;
  font-size: 12px;
}

/* 用户面板 */
.user-panel {
  padding: 16px 24px;
  border-top: 1px solid rgba(255,255,255,0.1);
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  position: relative;
}

.user-avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.user-status {
  position: absolute;
  bottom: 2px;
  right: 2px;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #52c41a;
  border: 2px solid #001529;
}

.user-info {
  color: white;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
}

.user-role {
  font-size: 12px;
  opacity: 0.65;
}

/* 主容器 */
.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* 顶部栏 */
.main-header {
  height: 64px;
  background: white;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 4px rgba(0,21,41,0.08);
}

.header-left {
  display: flex;
  flex-direction: column;
}

.header-left h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 500;
}

.breadcrumb {
  font-size: 12px;
  color: #8c8c8c;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.search-box {
  width: 200px;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.action-item {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s;
}

.action-item:hover {
  background: #f5f5f5;
}

/* 主内容区 */
.main-content {
  flex: 1;
  padding: 24px;
  overflow: auto;
}

/* 统计卡片 */
.stat-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 24px;
  margin-bottom: 24px;
}

.stat-card {
  background: white;
  padding: 24px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  transition: all 0.3s;
}

.stat-card:hover {
  box-shadow: 0 4px 16px rgba(0,0,0,0.1);
  transform: translateY(-2px);
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.stat-info {
  flex: 1;
}

.stat-label {
  color: #8c8c8c;
  font-size: 14px;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  margin: 4px 0;
}

.stat-trend {
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.stat-trend.up {
  color: #52c41a;
}
.stat-trend.down {
  color: #ff4d4f;
}
.stat-trend.neutral {
  color: #8c8c8c;
}

/* 组件容器 */
.component-container {
  background: white;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}
</style>
