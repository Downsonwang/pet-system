<template>
  <div class="dialog-overlay" v-if="visible" @click.self="closeDialog">
    <div class="dialog-content">
      <div class="dialog-header">
        <h2>预约记录</h2>
        <button class="close-btn" @click="closeDialog">&times;</button>
      </div>
      <div class="dialog-body">
        <div v-if="loading" class="loading">加载中...</div>
        <div v-else-if="appointments.length === 0" class="no-data">
          暂无预约记录
        </div>
        <div v-else class="appointments-list">
          <div v-for="appointment in appointments" :key="appointment.id" class="appointment-card">
            <div class="appointment-info">
              <h3>{{ appointment.pet.name }}</h3>
              <p>预约时间：{{ formatDateTime(appointment.appointmentTime) }}</p>
              <p>医生：{{ appointment.doctor.title }}</p>
              <p>状态：{{ formatStatus(appointment.status) }}</p>
              <p>症状：{{ appointment.symptoms }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AppointmentRecordDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: false,
      appointments: []
    }
  },
  methods: {
    async loadAppointments() {
      this.loading = true;
      try {
        const response = await fetch('http://localhost:8080/api/appointments/user', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        });
        if (response.ok) {
          const result = await response.json();
          if (result.code === 200) {
            this.appointments = result.data;
          }
        }
      } catch (error) {
        console.error('获取预约记录失败:', error);
      } finally {
        this.loading = false;
      }
    },
    formatDateTime(datetime) {
      return new Date(datetime).toLocaleString('zh-CN');
    },
    formatStatus(status) {
      const statusMap = {
        'PENDING': '待确认',
        'CONFIRMED': '已确认',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      };
      return statusMap[status] || status;
    },
    closeDialog() {
      this.$emit('update:visible', false);
    }
  },
  watch: {
    visible(newVal) {
      if (newVal) {
        this.loadAppointments();
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
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.dialog-content {
  background: white;
  border-radius: 8px;
  width: 90%;
  max-width: 800px;
  max-height: 80vh;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
}

.dialog-header {
  padding: 16px 24px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dialog-header h2 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.close-btn {
  border: none;
  background: none;
  font-size: 24px;
  cursor: pointer;
  color: #999;
}

.close-btn:hover {
  color: #666;
}

.dialog-body {
  padding: 24px;
  overflow-y: auto;
  max-height: calc(80vh - 60px);
}

.loading {
  text-align: center;
  padding: 20px;
  color: #666;
}

.no-data {
  text-align: center;
  padding: 40px;
  color: #999;
}

.appointments-list {
  display: grid;
  gap: 16px;
}

.appointment-card {
  border: 1px solid #eee;
  border-radius: 4px;
  padding: 16px;
  background: #f9f9f9;
}

.appointment-info h3 {
  margin: 0 0 8px 0;
  color: #333;
  font-size: 16px;
}

.appointment-info p {
  margin: 4px 0;
  color: #666;
  font-size: 14px;
}

.status-pending {
  color: #faad14;
}

.status-confirmed {
  color: #52c41a;
}

.status-completed {
  color: #1890ff;
}

.status-cancelled {
  color: #ff4d4f;
}
</style>