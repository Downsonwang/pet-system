import { createRouter, createWebHistory } from 'vue-router'
import RoleSelect from '../views/RoleSelect.vue'
import AdminLogin from '../views/AdminLogin.vue'
import CustomerLogin from '../views/CustomerLogin.vue'
import DoctorLogin from '../views/DoctorLogin.vue'
import FrontLogin from '../views/FrontLogin.vue'
import WorkerLogin from '../views/WorkerLogin.vue'
import Home from '../views/Home.vue'
import DoctorDashboard from '../views/DoctorDashboard.vue'
import PharmacyDashboard from '../views/pharmacist/PharmacistDashboard.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'roleSelect',
      component: RoleSelect
    },
    {
      path: '/home',
      name: 'home',
      component: Home,
      meta: { requiresAuth: true }
    },
    {
      path: '/doctor/dashboard',
      name: 'doctorDashboard',
      component: DoctorDashboard,
      meta: { requiresAuth: true, role: 'DOCTOR' }
    },
    {
      path: '/pharmacy/dashboard',
      name: 'pharmacyDashboard',
      component: PharmacyDashboard,
      meta: { requiresAuth: true, role: 'PHARMACIST' }
    },
    {
      path: '/admin-login',
      name: 'adminLogin',
      component: AdminLogin
    },
    {
      path: '/customer-login',
      name: 'customerLogin',
      component: CustomerLogin
    },
    {
      path: '/doctor-login',
      name: 'doctorLogin',
      component: DoctorLogin
    },
    {
      path: '/front-login',
      name: 'frontLogin',
      component: FrontLogin
    },
    {
      path: '/worker-login',
      name: 'workerLogin',
      component: WorkerLogin
    },
    {
      path: '/worker/dashboard',  // 添加药房工作台路由
      name: 'workerDashboard',
      component: PharmacyDashboard,
      meta: {
        requiresAuth: true,
        role: 'PHARMACIST'  // 指定药房工作人员角色
      }
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth) {
    const token = localStorage.getItem('token')
    const userInfo = localStorage.getItem('userInfo')

    if (!token || !userInfo) {
      next('/')
      return
    }

    // 检查角色权限
    if (to.meta.role) {
      const user = JSON.parse(userInfo)
      if (user.role !== to.meta.role) {
        next('/')
        return
      }
    }
    next()
  } else {
    next()
  }
})

export default router