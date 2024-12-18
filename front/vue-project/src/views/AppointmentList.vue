<template>
  <div class="dialog-overlay" v-if="visible">
    <div class="dialog-content">
      <div class="dialog-header">
        <h2>预约列表</h2>
        <button class="close-btn" @click="closeDialog">&times;</button>
      </div>

      <div class="dialog-body">
        <div v-for="appointment in appointments"
             :key="appointment.id"
             class="appointment-card">
          <div class="appointment-header">
            <h3>预约号：{{ appointment.id }}</h3>
            <span :class="['status', getStatusClass(appointment.status)]">
             {{ formatStatus(appointment.status) }}
           </span>
          </div>

          <div class="appointment-info">
            <p><label>预约时间：</label>{{ formatDateTime(appointment.appointmentTime) }}</p>
            <p><label>主人：</label>{{ appointment.ownerName }}</p>
            <p><label>症状：</label>{{ appointment.symptoms }}</p>
          </div>


          <div class="appointment-actions" v-if="appointment.status === 'PENDING'">
            <button class="btn accept" @click="confirmAppointment(appointment.id)">接受预约</button>
            <button class="btn reject" @click="rejectAppointment(appointment.id)">拒绝预约</button>
          </div>

          <div class="appointment-actions" v-if="appointment.status === 'CONFIRMED'">
            <button class="btn accept" @click="createMedicalRecord(appointment.id)">创建诊断记录</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'

export default {
  name: 'AppointmentList',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },

  data() {
    return {
      appointments: []
    }
  },

  methods: {
    createMedicalRecord(appointmentId) {
      this.$emit('create-record', appointmentId);
      this.closeDialog();
    },
    async loadAppointments() {
      try {
        const response = await fetch('http://localhost:8080/api/appointments/doctor', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        });
        const result = await response.json();
        if (result.code === 200) {
          this.appointments = result.data;
        }
      } catch (error) {
        console.error('获取预约列表失败:', error);
        ElMessage.error('获取预约列表失败');
      }
    },

    async confirmAppointment(id) {
      try {
        const response = await fetch(`http://localhost:8080/api/appointments/${id}/confirm`, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json'
          }
        });

        const result = await response.json();
        if (result.code === 200) {
          ElMessage.success('预约已接受');
          await this.loadAppointments();
        } else {
          ElMessage.error(result.message || '操作失败');
        }
      } catch (error) {
        console.error('确认预约失败:', error);
        ElMessage.error('操作失败，请重试');
      }
    },

    async rejectAppointment(id) {
      try {
        const response = await fetch(`http://localhost:8080/api/appointments/${id}/reject`, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json'
          }
        });

        const result = await response.json();
        if (result.code === 200) {
          ElMessage.success('预约已拒绝');
          await this.loadAppointments();
        } else {
          ElMessage.error(result.message || '操作失败');
        }
      } catch (error) {
        console.error('拒绝预约失败:', error);
        ElMessage.error('操作失败，请重试');
      }
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

    getStatusClass(status) {
      return status.toLowerCase();
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
  background: rgba(0, 0, 0, 0.5);
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
  overflow-y: auto;
}

.dialog-header {
  padding: 16px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dialog-header h2 {
  margin: 0;
  font-size: 20px;
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
  padding: 16px;
}

.appointment-card {
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
  background: white;
  transition: all 0.3s ease;
}

.appointment-card:hover {
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.appointment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.appointment-header h3 {
  margin: 0;
  color: #333;
  font-size: 16px;
}

.status {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 14px;
}

.status.pending {
  background: #e6f7ff;
  color: #1890ff;
}

.status.confirmed {
  background: #f6ffed;
  color: #52c41a;
}

.status.completed {
  background: #f9f0ff;
  color: #722ed1;
}

.status.cancelled {
  background: #fff1f0;
  color: #f5222d;
}

.appointment-info {
  margin-bottom: 16px;
}

.appointment-info p {
  margin: 8px 0;
  color: #666;
}

.appointment-info label {
  font-weight: 500;
  color: #333;
  margin-right: 8px;
}

.appointment-actions {
  display: flex;
  gap: 12px;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
  transition: opacity 0.2s;
}

.btn:hover {
  opacity: 0.8;
}

.accept {
  background: #52c41a;
  color: white;
}

.reject {
  background: #ff4d4f;
  color: white;
}

@media (max-width: 768px) {
  .appointment-actions {
    flex-direction: column;
  }

  .btn {
    width: 100%;
    margin-bottom: 8px;
  }
}
</style>