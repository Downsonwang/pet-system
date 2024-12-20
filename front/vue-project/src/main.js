import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import * as ElementPlusIcons from '@element-plus/icons-vue'



const app = createApp(App)
app.use(router)
app.use(ElementPlus)  // 添加 Element Plus
app.mount('#app')