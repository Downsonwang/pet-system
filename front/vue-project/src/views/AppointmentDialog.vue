<template>
  <div class="dialog-overlay" v-if="visible" @click.self="closeDialog">
    <div class="dialog-content">
      <div class="dialog-header">
        <h2>在线预约</h2>
        <button class="close-btn" @click="closeDialog" type="button">&times;</button>
      </div>

      <div class="dialog-body">
        <div class="steps">
          <div
              v-for="(step, index) in steps"
              :key="index"
              class="step"
              :class="{ active: currentStep >= index + 1 }"
          >
            {{ step }}
          </div>
        </div>

        <form @submit.prevent="submitForm">
          <!-- 步骤1：宠物信息 -->
          <div v-if="currentStep === 1" class="form-step">
            <div class="form-group">
              <label>宠物照片</label>
              <div class="image-upload">
                <div v-if="form.petImage" class="upload-preview">
                  <img :src="form.petImage" alt="宠物照片预览" />
                  <button
                      type="button"
                      class="remove-image"
                      @click.prevent="removePetImage"
                  >
                    &times;
                  </button>
                </div>
                <div v-else class="upload-placeholder">
                  <input
                      type="file"
                      ref="fileInput"
                      @change="handleImageUpload"
                      accept="image/*"
                      class="file-input"
                  />
                  <svg class="upload-icon" viewBox="0 0 24 24">
                    <path d="M12 5v14M5 12h14" stroke="currentColor" stroke-width="2" fill="none"/>
                  </svg>
                  <span>点击上传宠物照片</span>
                </div>
              </div>
            </div>

            <div class="form-group">
              <label for="petName">宠物名称</label>
              <input
                  id="petName"
                  v-model.trim="form.petName"
                  type="text"
                  required
                  placeholder="请输入宠物名称"
              />
            </div>

            <div class="form-group">
              <label for="species">宠物种类</label>
              <select id="species" v-model="form.species" required>
                <option value="">请选择宠物种类</option>
                <option
                    v-for="option in speciesOptions"
                    :key="option.value"
                    :value="option.value"
                >
                  {{ option.label }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label for="breed">品种</label>
              <input
                  id="breed"
                  v-model.trim="form.breed"
                  type="text"
                  placeholder="请输入宠物品种"
              />
            </div>

            <div class="form-group">
              <label for="age">年龄</label>
              <input
                  id="age"
                  v-model.number="form.age"
                  type="number"
                  min="0"
                  max="100"
                  required
                  placeholder="请输入宠物年龄"
              />
            </div>

            <div class="form-group">
              <label for="gender">性别</label>
              <select id="gender" v-model="form.gender" required>
                <option value="">请选择性别</option>
                <option
                    v-for="option in genderOptions"
                    :key="option.value"
                    :value="option.value"
                >
                  {{ option.label }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label for="weight">体重(kg)</label>
              <input
                  id="weight"
                  v-model.number="form.weight"
                  type="number"
                  min="0"
                  step="0.1"
                  placeholder="请输入宠物体重"
              />
            </div>
          </div>

          <!-- 步骤2：选择医生 -->
          <div v-if="currentStep === 2" class="form-step">
            <div v-if="loading" class="loading-spinner">
              加载中...
            </div>
            <div v-else-if="doctors.length" class="doctor-list">
              <div
                  v-for="doctor in doctors"
                  :key="doctor.id"
                  class="doctor-card"
                  :class="{ active: form.doctorId === doctor.id }"
                  @click="selectDoctor(doctor)"
              >
                <img
                    :src="doctor.avatar"
                    :alt="doctor.realName"
                    class="doctor-avatar"
                    @error="handleImageError($event, doctor.id)"
                />
                <div class="doctor-info">
                  <h3>{{ doctor.realName }}</h3>
                  <p class="doctor-title">{{ doctor.title }}</p>
                  <p class="doctor-specialty">{{ doctor.specialization }}</p>
                </div>
              </div>
            </div>
            <div v-else class="no-doctors">
              暂无可预约医生
            </div>
          </div>

          <!-- 步骤3：选择时间 -->
          <div v-if="currentStep === 3" class="form-step">
            <div class="form-group">
              <label for="appointmentDate">选择日期</label>
              <input
                  id="appointmentDate"
                  type="date"
                  v-model="form.appointmentDate"
                  required
                  :min="minDate"
                  :max="maxDate"
                  @change="loadTimeSlots"
              />
            </div>

            <div class="form-group">
              <label for="symptoms">症状描述</label>
              <textarea
                  id="symptoms"
                  v-model.trim="form.symptoms"
                  rows="4"
                  placeholder="请简要描述宠物症状或就诊目的"
                  class="form-textarea"
                  maxlength="500"
              ></textarea>
              <div class="textarea-counter">
                {{ form.symptoms.length }}/500
              </div>
            </div>

            <div v-if="loading" class="loading-spinner">
              加载时间段...
            </div>
            <div v-else class="time-slots">
              <div v-if="timeSlots.length" class="time-slot-group">
                <div class="slot-buttons">
                  <button
                      v-for="slot in timeSlots"
                      :key="slot.time"
                      type="button"
                      :class="['time-slot-btn', {
                      active: form.appointmentTime === slot.time,
                      disabled: !slot.available
                    }]"
                      :disabled="!slot.available"
                      @click="selectTimeSlot(slot.time)"
                  >
                    {{ slot.time }}
                  </button>
                </div>
              </div>
              <div v-else class="no-slots">
                当前日期暂无可预约时段
              </div>
            </div>
          </div>

          <!-- 导航按钮 -->
          <div class="form-navigation">
            <button
                type="button"
                class="prev-btn"
                v-if="currentStep > 1"
                @click="prevStep"
            >
              上一步
            </button>
            <button
                type="button"
                class="next-btn"
                v-if="currentStep < 3"
                :disabled="!canProceed"
                @click="nextStep"
            >
              下一步
            </button>
            <button
                type="submit"
                class="submit-btn"
                v-if="currentStep === 3"
                :disabled="!canSubmit"
            >
              确认预约
            </button>
          </div>
        </form>


      </div>
    </div>
  </div>
</template>



<script>
import axios from 'axios';

export default {
  name: "AppointmentDialog",
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      currentStep: 1,
      appointments:[],
      loading: false,
      steps: ['宠物信息', '选择医生', '选择时间'],
      form: {
        petImage: null,
        petName: "",
        species: "",
        breed: "",
        age: null,
        gender: "",
        weight: null,
        doctorId: null,
        appointmentDate: "",
        appointmentTime: "",
        symptoms: ""
      },
      doctors: [],
      timeSlots: [],
      speciesOptions: [
        { value: 'DOG', label: '狗' },
        { value: 'CAT', label: '猫' },
        { value: 'OTHER', label: '其他' }
      ],
      genderOptions: [
        { value: 'MALE', label: '公' },
        { value: 'FEMALE', label: '母' }
      ]
    };
  },
  computed: {
    minDate() {
      const today = new Date();
      return today.toISOString().split('T')[0];
    },
    maxDate() {
      const maxDate = new Date();
      maxDate.setDate(maxDate.getDate() + 30);
      return maxDate.toISOString().split('T')[0];
    },
    canProceed() {
      if (this.currentStep === 1) {
        return this.form.petName &&
            this.form.species &&
            this.form.age !== null;
      }
      if (this.currentStep === 2) {
        return this.form.doctorId !== null;
      }
      return true;
    },
    canSubmit() {
      return this.form.appointmentDate &&
          this.form.appointmentTime &&
          this.form.symptoms.trim();
    }
  },
  methods: {
    async handleImageUpload(event) {
      const file = event.target.files[0];
      if (file) {
        if (file.size > 5 * 1024 * 1024) {
          alert('图片大小不能超过5MB');
          return;
        }
        try {
          this.form.petImage = URL.createObjectURL(file);
        } catch (error) {
          console.error('图片上传失败:', error);
          alert('图片上传失败，请重试');
        }
      }
    },
    removePetImage() {
      if (this.form.petImage) {
        URL.revokeObjectURL(this.form.petImage);
      }
      this.form.petImage = null;
      if (this.$refs.fileInput) {
        this.$refs.fileInput.value = '';
      }
    },
    handleImageError(event, doctorId) {
      event.target.src = `/default-avatar.jpg`;
    },
    selectDoctor(doctor) {
      this.form.doctorId = doctor.id;
    },
    async loadTimeSlots() {
      if (!this.form.appointmentDate) return;

      this.loading = true;
      try {
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 1000));
        this.timeSlots = [
          { time: '09:00', available: true },
          { time: '09:30', available: true },
          { time: '10:00', available: false },
          { time: '10:30', available: true },
          { time: '14:00', available: true },
          { time: '14:30', available: true },
          { time: '15:00', available: false },
          { time: '15:30', available: true }
        ];
      } catch (error) {
        console.error('加载时间段失败:', error);
        alert('加载时间段失败，请重试');
      } finally {
        this.loading = false;
      }
    },
    selectTimeSlot(time) {
      this.form.appointmentTime = time;
    },
    async nextStep() {
      if (this.currentStep < 3) {
        this.currentStep++;
        if (this.currentStep === 2) {
          await this.loadDoctors();
        }
      }
    },
    prevStep() {
      if (this.currentStep > 1) {
        this.currentStep--;
      }
    },
    // 获取医生列表
    async loadDoctors() {
      this.loading = true;
      try {
        const response = await fetch('http://localhost:8080/api/doctors', {
          method: 'GET',
          credentials: 'include'
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result = await response.json();
        if (result.code === 200) {
          this.doctors = result.data.map(doctor => ({
            id: doctor.userId,  // 假设后端返回的是 userId
            realName: doctor.title, // 使用 title 作为医生名字
            title: doctor.title,
            specialization: doctor.specialization,
            avatar: doctor.avatarUrl || '/default-avatar.jpg'
          }));
        } else {
          throw new Error(result.message);
        }
      } catch (error) {
        console.error('加载医生列表失败:', error);
        this.$message.error('加载医生列表失败，请重试');
      } finally {
        this.loading = false;
      }
    },

    // 提交预约表单
    async submitForm() {
      try {
        this.loading = true;

        const formData = new FormData();
        formData.append('petName', this.form.petName);
        formData.append('species', this.form.species);
        formData.append('breed', this.form.breed);
        formData.append('age', this.form.age);
        formData.append('gender', this.form.gender);  // 应该是 'MALE' 或 'FEMALE'
        formData.append('weight', this.form.weight);
        formData.append('doctorId', this.form.doctorId);
        formData.append('appointmentDate', this.form.appointmentDate);
        formData.append('appointmentTime', this.form.appointmentTime);
        formData.append('symptoms', this.form.symptoms);

        if (this.form.petImage) {
          formData.append('petImage', this.form.petImage);
        }

        const response = await fetch('http://localhost:8080/api/appointments/create', {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          },
          body: formData,
          credentials: 'include'
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result = await response.json();
        if (result.code === 200) {
          this.$message.success('预约成功！');
          this.closeDialog();
          this.$emit('appointment-created', result.data);
          await this.loadAppointments();
        } else {
          throw new Error(result.message);
        }
      } catch (error) {
        console.error('预约失败:', error);
        this.$message.error(error.message || '预约失败，请稍后重试');
      } finally {
        this.loading = false;
      }
    },

    // 获取预约列表
    async loadAppointments() {
      try {
        this.loading = true;
        const response = await fetch('http://localhost:8080/api/appointments/user', {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          },
          credentials: 'include'
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result = await response.json();
        if (result.code === 200) {
          this.appointments = result.data;
        } else {
          throw new Error(result.message);
        }
      } catch (error) {
        console.error('获取预约列表失败:', error);
        this.$message.error('获取预约列表失败，请稍后重试');
      } finally {
        this.loading = false;
      }
    },
    // 添加图片转Base64方法
    async convertImageToBase64(imageUrl) {
      return new Promise((resolve, reject) => {
        const canvas = document.createElement('canvas');
        const img = new Image();
        img.crossOrigin = 'Anonymous';

        img.onload = () => {
          canvas.width = img.width;
          canvas.height = img.height;
          const ctx = canvas.getContext('2d');
          ctx.drawImage(img, 0, 0);

          // 移除 data:image/jpeg;base64, 前缀
          const base64 = canvas.toDataURL('image/jpeg').split(',')[1];
          resolve(base64);
        };

        img.onerror = reject;
        img.src = imageUrl;
      });
    },
    // 添加获取预约列表方法

    formatStatus(status) {
      const statusMap = {
        'PENDING': '待确认',
        'CONFIRMED': '已确认',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      };
      return statusMap[status] || status;
    },
    async created() {
      if (this.visible) {
        await this.loadAppointments();
      }
    },

    closeDialog() {
      this.$emit("update:visible", false);
      this.resetForm();
    },
    resetForm() {
      if (this.form.petImage) {
        URL.revokeObjectURL(this.form.petImage);
      }
      this.currentStep = 1;
      this.form = {
        petImage: null,
        petName: "",
        species: "",
        breed: "",
        age: null,
        gender: "",
        weight: null,
        doctorId: null,
        appointmentDate: "",
        appointmentTime: "",
        symptoms: ""
      };
      this.timeSlots = [];
      this.doctors = [];
    }
  },
  beforeDestroy() {
    // 清理图片URL对象
    if (this.form.petImage) {
      URL.revokeObjectURL(this.form.petImage);
    }
  },
  watch: {
    visible(newVal) {
      if (newVal) {
        this.loadAppointments();
      }
    }
  }
};
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
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.dialog-content {
  background: white;
  border-radius: 8px;
  width: 90%;
  max-width: 800px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
}

.dialog-header {
  padding: 20px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: sticky;
  top: 0;
  background: white;
  z-index: 1;
}

.dialog-header h2 {
  margin: 0;
  font-size: 20px;
  color: #333;
  font-weight: 600;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  color: #666;
  cursor: pointer;
  padding: 4px;
  line-height: 1;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.close-btn:hover {
  background: #f5f5f5;
  color: #333;
}

.dialog-body {
  padding: 20px;
}

/* 步骤指示器样式 */
.steps {
  display: flex;
  justify-content: space-between;
  margin-bottom: 30px;
  position: relative;
}

.step {
  flex: 1;
  text-align: center;
  padding: 12px;
  background: #f5f5f5;
  color: #666;
  position: relative;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.step.active {
  background: #4A90E2;
  color: white;
}

.step:not(:last-child)::after {
  content: '';
  position: absolute;
  right: -15px;
  top: 50%;
  transform: translateY(-50%);
  border-left: 15px solid #f5f5f5;
  border-top: 15px solid transparent;
  border-bottom: 15px solid transparent;
  z-index: 1;
  transition: all 0.3s ease;
}

.step.active:not(:last-child)::after {
  border-left-color: #4A90E2;
}

/* 表单样式 */
.form-step {
  animation: fadeIn 0.3s ease;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #333;
  font-weight: 500;
  font-size: 14px;
}

.form-group input:not([type="file"]),
.form-group select,
.form-textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  transition: all 0.3s ease;
  background: white;
}

.form-group input:focus,
.form-group select:focus,
.form-textarea:focus {
  outline: none;
  border-color: #4A90E2;
  box-shadow: 0 0 0 3px rgba(74, 144, 226, 0.1);
}

.form-textarea {
  resize: vertical;
  min-height: 100px;
}

.textarea-counter {
  text-align: right;
  color: #999;
  font-size: 12px;
  margin-top: 4px;
}

/* 图片上传样式 */
.image-upload {
  border: 2px dashed #ddd;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
  cursor: pointer;
  position: relative;
  transition: all 0.3s ease;
}

.image-upload:hover {
  border-color: #4A90E2;
}

.upload-placeholder {
  position: relative;
  min-height: 120px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.file-input {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  cursor: pointer;
}

.upload-icon {
  width: 40px;
  height: 40px;
  margin-bottom: 10px;
  color: #666;
}

.upload-preview {
  position: relative;
  padding: 10px;
}

.upload-preview img {
  max-width: 100%;
  max-height: 200px;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.remove-image {
  position: absolute;
  top: 0;
  right: 0;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: #ff4444;
  color: white;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
}

.remove-image:hover {
  background: #ff6666;
  transform: scale(1.1);
}

/* 医生列表样式 */
.doctor-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.doctor-card {
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 15px;
  display: flex;
  align-items: center;
  gap: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: white;
}

.doctor-card:hover {
  border-color: #4A90E2;
  box-shadow: 0 2px 12px rgba(74, 144, 226, 0.15);
  transform: translateY(-2px);
}

.doctor-card.active {
  border-color: #4A90E2;
  background: #f0f7ff;
}

.doctor-avatar {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #eee;
  transition: all 0.3s ease;
}

.doctor-card:hover .doctor-avatar {
  border-color: #4A90E2;
}

.doctor-info {
  flex: 1;
}

.doctor-info h3 {
  margin: 0 0 5px 0;
  font-size: 16px;
  color: #333;
  font-weight: 600;
}

.doctor-title {
  color: #666;
  font-size: 14px;
  margin: 0 0 5px 0;
}

.doctor-specialty {
  color: #999;
  font-size: 12px;
  margin: 0;
}

/* 时间选择样式 */
.time-slots {
  margin-top: 20px;
}

.slot-buttons {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(90px, 1fr));
  gap: 12px;
}

.time-slot-btn {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background: white;
  color: #333;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
  text-align: center;
}

.time-slot-btn:hover:not(.disabled) {
  border-color: #4A90E2;
  color: #4A90E2;
  transform: translateY(-1px);
}

.time-slot-btn.active {
  background: #4A90E2;
  color: white;
  border-color: #4A90E2;
}

.time-slot-btn.disabled {
  background: #f5f5f5;
  color: #999;
  cursor: not-allowed;
  border-color: #eee;
}

/* 导航按钮样式 */
.form-navigation {
  margin-top: 30px;
  display: flex;
  justify-content: space-between;
  gap: 15px;
}

.prev-btn,
.next-btn,
.submit-btn {
  padding: 12px 24px;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
  min-width: 100px;
}

.prev-btn {
  background: white;
  border: 1px solid #ddd;
  color: #666;
}

.prev-btn:hover {
  background: #f5f5f5;
  border-color: #ccc;
}

.next-btn,
.submit-btn {
  background: #4A90E2;
  border: none;
  color: white;
}

.next-btn:hover:not(:disabled),
.submit-btn:hover:not(:disabled) {
  background: #357ABD;
  transform: translateY(-1px);
}

.next-btn:disabled,
.submit-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
  transform: none;
}

/* 加载状态 */
.loading-spinner {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  color: #666;
  font-size: 14px;
}

/* 空状态 */
.no-doctors,
.no-slots {
  text-align: center;
  padding: 40px;
  color: #999;
  font-size: 14px;
  background: #f9f9f9;
  border-radius: 4px;
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式布局 */
@media (max-width: 768px) {
  .dialog-content {
    width: 95%;
    margin: 10px;
    max-height: 95vh;
  }

  .dialog-header {
    padding: 15px;
  }

  .dialog-body {
    padding: 15px;
  }

  .steps {
    font-size: 12px;
  }

  .doctor-list {
    grid-template-columns: 1fr;
  }

  .slot-buttons {
    grid-template-columns: repeat(auto-fill, minmax(70px, 1fr));
  }

  .form-navigation {
    flex-direction: column;
  }

  .prev-btn,
  .next-btn,
  .submit-btn {
    width: 100%;
  }
}

/* 预约列表样式 */
.appointments-list {
  margin-top: 30px;
  border-top: 1px solid #eee;
  padding-top: 20px;
}

.appointments-list h3 {
  margin-bottom: 20px;
  color: #333;
  font-size: 18px;
}

.appointment-cards {
  display: grid;
  gap: 20px;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
}

.appointment-card {
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 15px;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.appointment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.appointment-time {
  color: #666;
  font-size: 14px;
}

.appointment-status {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.appointment-status.pending {
  background: #fff7e6;
  color: #d46b08;
}

.appointment-status.confirmed {
  background: #e6f7ff;
  color: #0958d9;
}

.appointment-status.completed {
  background: #f6ffed;
  color: #389e0d;
}

.appointment-status.cancelled {
  background: #fff1f0;
  color: #cf1322;
}

.appointment-content {
  display: grid;
  gap: 15px;
}

.appointment-content h4 {
  margin: 0 0 8px 0;
  color: #333;
  font-size: 14px;
}

.appointment-content p {
  margin: 0;
  color: #666;
  font-size: 14px;
  line-height: 1.5;
}

.symptoms {
  border-top: 1px dashed #eee;
  padding-top: 15px;
}

.no-appointments {
  text-align: center;
  padding: 40px;
  color: #999;
  font-size: 14px;
  background: #f9f9f9;
  border-radius: 4px;
}

@media (max-width: 768px) {
  .appointment-cards {
    grid-template-columns: 1fr;
  }
}
</style>