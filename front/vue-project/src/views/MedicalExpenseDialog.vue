<template>
  <div class="dialog-overlay" v-if="visible" @click.self="closeDialog">
    <div class="dialog-content">
      <div class="dialog-header">
        <h2>费用明细</h2>
        <button class="close-btn" @click="closeDialog">&times;</button>
      </div>

      <div class="dialog-body">
        <div v-if="loading" class="loading-spinner">
          <div class="spinner"></div>
          <p>加载中...</p>
        </div>

        <div v-else-if="expenses.length === 0" class="no-data">
         <img src="#" alt="暂无数据" class="empty-icon">
          <p>暂无费用记录</p>
        </div>

        <div v-else class="expense-table-wrapper">
          <table class="expense-table">
            <thead>
            <tr>
              <th>项目名称</th>
              <th>类型</th>
              <th>单价</th>
              <th>数量</th>
              <th>小计</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="expense in expenses" :key="expense.id" class="expense-row">
              <td>{{ expense.itemName }}</td>
              <td>
                  <span :class="getTypeClass(expense.itemType)">
                    {{ formatType(expense.itemType) }}
                  </span>
              </td>
              <td>¥{{ formatPrice(expense.amount) }}</td>
              <td>{{ expense.quantity }}</td>
              <td>¥{{ formatPrice(expense.amount * expense.quantity) }}</td>
            </tr>
            </tbody>
            <tfoot>
            <tr class="total-row">
              <td colspan="4" class="total-label">总计</td>
              <td class="total-amount">¥{{ formatPrice(totalAmount) }}</td>
            </tr>
            </tfoot>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ExpenseDialog',
  props: {
    visible: Boolean,
    recordId: Number
  },
  data() {
    return {
      loading: false,
      expenses: [],
    }
  },
  computed: {
    totalAmount() {
      return this.expenses.reduce((sum, expense) =>
          sum + (expense.amount * expense.quantity), 0
      )
    }
  },
  methods: {
    formatPrice(price) {
      return price.toFixed(2)
    },
    formatType(type) {
      const typeMap = {
        'DIAGNOSIS': '诊疗',
        'MEDICINE': '药品',
        'TEST': '检查'
      }
      return typeMap[type] || type
    },
    getTypeClass(type) {
      return `type-tag ${type.toLowerCase()}`
    },
    closeDialog() {
      this.$emit('update:visible', false)
    },
    async loadExpenses() {
      if (!this.recordId) return

      this.loading = true
      try {
        const response = await fetch(`http://localhost:8080/api/medical/expenses/${this.recordId}`, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })
        const result = await response.json()
        if (result.code === 200) {
          this.expenses = result.data
        }
      } catch (error) {
        console.error('获取费用明细失败:', error)
      } finally {
        this.loading = false
      }
    }
  },
  watch: {
    visible(newVal) {
      if (newVal) {
        this.loadExpenses()
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
  border-radius: 12px;
  width: 90%;
  max-width: 800px;
  max-height: 80vh;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.dialog-header {
  padding: 20px;
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
  background: none;
  border: none;
  font-size: 24px;
  color: #999;
  cursor: pointer;
  padding: 4px;
  transition: color 0.3s;
}

.close-btn:hover {
  color: #666;
}

.dialog-body {
  padding: 20px;
  max-height: calc(80vh - 80px);
  overflow-y: auto;
}

.expense-table {
  width: 100%;
  border-collapse: collapse;
}

.expense-table th {
  background: #f6f8fa;
  padding: 12px;
  text-align: left;
  font-weight: 500;
  color: #666;
}

.expense-table td {
  padding: 16px 12px;
  border-bottom: 1px solid #eee;
}

.expense-row:hover {
  background: #f9f9f9;
}

.type-tag {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.type-tag.diagnosis {
  background: #e6f7ff;
  color: #1890ff;
}

.type-tag.medicine {
  background: #f6ffed;
  color: #52c41a;
}

.type-tag.test {
  background: #fff7e6;
  color: #fa8c16;
}

.total-row {
  font-weight: 600;
}

.total-label {
  text-align: right;
}

.total-amount {
  color: #f5222d;
}

.loading-spinner {
  text-align: center;
  padding: 40px;
}

.spinner {
  width: 40px;
  height: 40px;
  margin: 0 auto 16px;
  border: 3px solid #f0f0f0;
  border-top: 3px solid #722ed1;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

.no-data {
  text-align: center;
  padding: 40px;
  color: #999;
}

.empty-icon {
  width: 80px;
  height: 80px;
  margin-bottom: 16px;
  opacity: 0.5;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>