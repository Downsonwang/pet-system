<template>
  <div class="dialog-overlay" v-if="visible">
    <div class="dialog-content">
      <div class="dialog-header">
        <h2>诊断记录列表</h2>
        <button class="close-btn" @click="closeDialog">&times;</button>
      </div>

      <div class="dialog-body">
        <div v-if="loading" class="loading">加载中...</div>
        <div v-else-if="records.length === 0" class="no-data">
          暂无诊断记录
        </div>
        <div v-else class="records-list">
          <div v-for="record in records"
               :key="record.id"
               class="record-card">
            <div class="record-header">
              <h3>就诊时间：{{ formatDateTime(record.visitTime) }}</h3>
            </div>

            <div class="record-info">
              <p><label>宠物名称：</label>{{ record.petName }}</p>
              <p><label>宠物种类：</label>{{ record.petSpecies }}</p>
              <p><label>主人姓名：</label>{{ record.ownerName }}</p>
              <p><label>诊断结果：</label>{{ record.diagnosis }}</p>
              <p><label>治疗方案：</label>{{ record.treatment }}</p>
              <p><label>处方：</label>{{ record.prescription }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'

export default {
  props: {
    visible: Boolean
  },

  data() {
    return {
      loading: false,
      records: []
    }
  },

  methods: {
    async loadRecords() {
      this.loading = true
      try {
        const response = await fetch('http://localhost:8080/api/medical/doctor/records', {
          method:'GET',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        });

        const result = await response.json();
        if (result.code === 200) {
          this.records = result.data;
        } else {
          ElMessage.error(result.message || '获取记录失败');
        }
      } catch (error) {
        console.error('获取诊断记录失败:', error);
        ElMessage.error('获取记录失败');
      } finally {
        this.loading = false;
      }
    },

    formatDateTime(datetime) {
      return new Date(datetime).toLocaleString('zh-CN');
    },

    closeDialog() {
      this.$emit('update:visible', false);
    }
  },

  watch: {
    visible(newVal) {
      if (newVal) {
        this.loadRecords();
      }
    }
  }
}
</script>

<style scoped>
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  backdrop-filter: blur(4px);
}

.dialog-content {
  background: white;
  border-radius: 16px;
  width: 90%;
  max-width: 800px;
  max-height: 85vh;
  overflow-y: auto;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  animation: slideIn 0.3s ease;
}

.dialog-header {
  padding: 20px 24px;
  background: #f8f9fa;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: sticky;
  top: 0;
  z-index: 1;
  backdrop-filter: blur(10px);
}

.dialog-header h2 {
  margin: 0;
  color: #2c3e50;
  font-size: 1.5rem;
  font-weight: 600;
}

.close-btn {
  width: 32px;
  height: 32px;
  border: none;
  background: #e9ecef;
  color: #495057;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  transition: all 0.2s;
}

.close-btn:hover {
  background: #dee2e6;
  transform: rotate(90deg);
}

.dialog-body {
  padding: 24px;
}

.loading, .no-data {
  text-align: center;
  padding: 40px;
  color: #6c757d;
  font-size: 1.1rem;
}

.records-list {
  display: grid;
  gap: 20px;
}

.record-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: transform 0.2s, box-shadow 0.2s;
  border: 1px solid #e9ecef;
}

.record-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.record-header {
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 2px solid #e9ecef;
}

.record-header h3 {
  margin: 0;
  color: #2c3e50;
  font-size: 1.1rem;
  display: flex;
  align-items: center;
}

.record-header h3::before {
  content: '';
  display: inline-block;
  width: 8px;
  height: 8px;
  background: #4CAF50;
  border-radius: 50%;
  margin-right: 8px;
}

.record-info {
  display: grid;
  gap: 12px;
}

.record-info p {
  margin: 0;
  padding: 8px 12px;
  background: #f8f9fa;
  border-radius: 6px;
  color: #495057;
}

.record-info label {
  font-weight: 500;
  color: #343a40;
  margin-right: 8px;
  display: inline-block;
  min-width: 80px;
}

/* 滚动条美化 */
.dialog-content::-webkit-scrollbar {
  width: 8px;
}

.dialog-content::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.dialog-content::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

.dialog-content::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 动画效果 */
@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 加载动画 */
.loading::after {
  content: '';
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 2px solid #f3f3f3;
  border-top: 2px solid #3498db;
  border-radius: 50%;
  margin-left: 8px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 响应式调整 */
@media (max-width: 768px) {
  .dialog-content {
    width: 95%;
    margin: 20px;
  }

  .record-info {
    grid-template-columns: 1fr;
  }
}
</style>