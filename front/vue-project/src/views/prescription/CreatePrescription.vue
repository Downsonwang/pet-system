<template>
  <div class="prescription-page">
    <!-- 头部 -->
    <div class="page-header">
      <div class="icon-box">
        <i class="icon-prescription"></i>
      </div>
      <div class="header-title">电子处方</div>
    </div>

    <!-- 处方表单 -->
    <div class="prescription-form">
      <!-- 就诊信息展示 -->
      <div class="medical-info">
        <div class="info-item">
          <span class="label">宠物名称：</span>
          <span class="value">{{ medicalRecord.petName }}</span>
        </div>
        <div class="info-item">
          <span class="label">就诊时间：</span>
          <span class="value">{{ formatDate(medicalRecord.visitTime) }}</span>
        </div>
      </div>

      <!-- 药品列表 -->
      <div class="medicine-list">
        <div v-for="(item, index) in prescriptionItems" :key="index" class="medicine-item">
          <el-select
              v-model="item.medicineId"
              placeholder="选择药品"
              @change="handleMedicineSelect($event, index)"
          >
            <el-option
                v-for="medicine in medicineList"
                :key="medicine.id"
                :label="medicine.name"
                :value="medicine.id"
            >
              <span>{{ medicine.name }}</span>
              <span class="medicine-stock">库存: {{ medicine.stockQuantity }}</span>
            </el-option>
          </el-select>

          <el-input-number
              v-model="item.quantity"
              :min="1"
              placeholder="数量"
          />

          <el-input
              v-model="item.usage"
              placeholder="用法用量"
          />

          <el-button
              type="danger"
              icon="el-icon-delete"
              @click="removeMedicine(index)"
          />
        </div>

        <el-button
            type="primary"
            icon="el-icon-plus"
            @click="addMedicineItem"
        >
          添加药品
        </el-button>
      </div>

      <!-- 处方备注 -->
      <div class="prescription-notes">
        <el-input
            type="textarea"
            v-model="notes"
            placeholder="处方备注"
            :rows="3"
        />
      </div>

      <!-- 提交按钮 -->
      <div class="form-actions">
        <el-button type="primary" @click="submitPrescription">开具处方</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'CreatePrescription',
  props: {
    medicalRecordId: {
      type: Number,
      required: true
    }
  },

  setup(props) {
    // 药品列表
    const medicineList = ref([])
    // 就诊记录
    const medicalRecord = ref({})
    // 处方药品项
    const prescriptionItems = ref([])
    // 处方备注
    const notes = ref('')

    // 获取药品列表
    const fetchMedicineList = async () => {
      try {
        const response = await fetch('/api/medicines')
        medicineList.value = await response.json()
      } catch (error) {
        ElMessage.error('获取药品列表失败')
      }
    }

    // 获取就诊记录
    const fetchMedicalRecord = async () => {
      try {
        const response = await fetch(`/api/medical-records/${props.medicalRecordId}`)
        medicalRecord.value = await response.json()
      } catch (error) {
        ElMessage.error('获取就诊记录失败')
      }
    }

    // 添加药品项
    const addMedicineItem = () => {
      prescriptionItems.value.push({
        medicineId: null,
        quantity: 1,
        usage: ''
      })
    }

    // 移除药品项
    const removeMedicine = (index) => {
      prescriptionItems.value.splice(index, 1)
    }

    // 提交处方
    const submitPrescription = async () => {
      try {
        const prescription = {
          medicalRecordId: props.medicalRecordId,
          notes: notes.value,
          items: prescriptionItems.value
        }

        const response = await fetch('/api/prescriptions', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(prescription)
        })

        if (response.ok) {
          ElMessage.success('处方开具成功')
          // 可以跳转到处方列表页
        }
      } catch (error) {
        ElMessage.error('处方开具失败')
      }
    }

    return {
      medicineList,
      medicalRecord,
      prescriptionItems,
      notes,
      addMedicineItem,
      removeMedicine,
      submitPrescription
    }
  }
}
</script>

<style scoped>
.prescription-page {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
}

.icon-box {
  width: 48px;
  height: 48px;
  background: #8b5cf6;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}

.icon-prescription {
  color: white;
  font-size: 24px;
}

.header-title {
  font-size: 24px;
  font-weight: 600;
  color: #333;
}

.medical-info {
  margin-bottom: 30px;
  padding: 15px;
  background: #f9fafb;
  border-radius: 8px;
}

.info-item {
  margin-bottom: 10px;
}

.info-item .label {
  color: #666;
  margin-right: 10px;
}

.medicine-list {
  margin-bottom: 30px;
}

.medicine-item {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
}

.medicine-stock {
  float: right;
  color: #666;
  font-size: 13px;
}

.prescription-notes {
  margin-bottom: 30px;
}

.form-actions {
  text-align: center;
}
</style>