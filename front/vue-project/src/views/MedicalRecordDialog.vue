<template>
  <div class="dialog-overlay" v-if="visible">
    <div class="dialog-content">
      <div class="dialog-header">
        <h2>创建诊断记录</h2>
        <button class="close-btn" @click="closeDialog">&times;</button>
      </div>

      <div class="dialog-body">
        <form @submit.prevent="submitRecord">
          <div class="form-group">
            <label>诊断结果</label>
            <textarea
                v-model="form.diagnosis"
                placeholder="请输入诊断结果"
                required
            ></textarea>
          </div>

          <div class="form-group">
            <label>治疗方案</label>
            <textarea
                v-model="form.treatment"
                placeholder="请输入治疗方案"
                required
            ></textarea>
          </div>

          <div class="form-group">
            <label>处方</label>
            <textarea
                v-model="form.prescription"
                placeholder="请输入处方内容"
                required
            ></textarea>
          </div>

          <div class="form-actions">
            <button type="submit" class="btn submit">提交记录</button>
            <button type="button" class="btn cancel" @click="closeDialog">取消</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'

export default {
  name: 'MedicalRecordDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    appointmentId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      form: {
        diagnosis: '',
        treatment: '',
        prescription: ''
      }
    }
  },
  methods: {
    // MedicalRecordDialog.vue
    async submitRecord() {
      if (!this.appointmentId) {
        ElMessage.error('预约ID不能为空');
        return;
      }
      try {
        // 检查appointmentId是否为number类型
        const appointmentId = Number(this.appointmentId);

        const recordData = {
          appointmentId: appointmentId,  // 确保是数字类型
          diagnosis: this.form.diagnosis,
          treatment: this.form.treatment,
          prescription: this.form.prescription
        };

        console.log('Sending data:', recordData); // 调试用

        const response = await fetch('http://localhost:8080/api/medical/record/create', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          },
          body: JSON.stringify(recordData)
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result = await response.json();
        if (result.code === 200) {
          ElMessage.success('诊断记录创建成功');
          this.closeDialog();
          this.$emit('success');
        } else {
          ElMessage.error(result.message || '创建失败');
        }
      } catch (error) {
        console.error('创建诊断记录失败:', error);
        ElMessage.error('创建失败，请重试');
      }
    },
    closeDialog() {
      this.$emit('update:visible', false);
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
  max-width: 600px;
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

.close-btn {
  border: none;
  background: none;
  font-size: 24px;
  cursor: pointer;
  color: #999;
}

.dialog-body {
  padding: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
}

.form-group textarea {
  width: 100%;
  min-height: 100px;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 20px;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
}

.submit {
  background: #52c41a;
  color: white;
}

.cancel {
  background: #f5f5f5;
  color: #666;
}
</style>