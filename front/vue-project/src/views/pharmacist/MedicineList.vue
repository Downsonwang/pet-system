<!-- MedicineList.vue -->
<template>
  <div class="medicine-list">
    <!-- 操作栏 -->
    <div class="action-bar">
      <el-button type="primary" @click="showAddMedicineDialog">
        新增药品
      </el-button>
      <el-button type="success" @click="importMedicines">
        批量导入
      </el-button>
    </div>

    <!-- 药品表格 -->
    <el-table :data="medicines" style="width: 100%">
      <el-table-column label="药品名称" prop="name" />
      <el-table-column label="规格" prop="specification" />
      <el-table-column label="单价" prop="unitPrice">
        <template #default="scope">
          ¥{{ scope.row.unitPrice }}
        </template>
      </el-table-column>
      <el-table-column label="库存" prop="stockQuantity">
        <template #default="scope">
          <span :class="{ 'low-stock': scope.row.stockQuantity < scope.row.minStock }">
            {{ scope.row.stockQuantity }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="status">
        <template #default="scope">
          <el-tag :type="scope.row.status ? 'success' : 'info'">
            {{ scope.row.status ? '已上架' : '已下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <!-- 移除 el-button-group 以防兼容问题 -->
          <el-button size="small" @click="editMedicine(scope.row)">编辑</el-button>
          <el-button
              size="small"
              :type="scope.row.status ? 'warning' : 'success'"
              @click="toggleStatus(scope.row)"
          >
            {{ scope.row.status ? '下架' : '上架' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 药品编辑对话框 -->
    <el-dialog
        :title="dialogTitle"
        v-model="dialogVisible"
        width="500px"
    >
      <el-form :model="medicineForm" label-width="100px">
        <el-form-item label="药品名称">
          <el-input v-model="medicineForm.name" />
        </el-form-item>
        <el-form-item label="规格">
          <el-input v-model="medicineForm.specification" />
        </el-form-item>
        <el-form-item label="单价">
          <el-input-number v-model="medicineForm.unitPrice" :precision="2" :step="0.1" />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="medicineForm.stockQuantity" :min="0" />
        </el-form-item>
        <el-form-item label="最小库存">
          <el-input-number v-model="medicineForm.minStock" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveMedicine">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';

export default {
  setup() {
    const medicines = ref([]);
    const dialogVisible = ref(false);
    const dialogTitle = ref('');
    const medicineForm = reactive({
      id: null,
      name: '',
      specification: '',
      unitPrice: 0,
      stockQuantity: 0,
      minStock: 0,
      status: true,
    });

    const importMedicines = () => {
      // 暂时不实现或稍后补齐逻辑
      console.log('importMedicines clicked');
    };

    const fetchMedicines = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/medicines/query', { method: 'GET' });
        if (!response.ok) throw new Error('Failed to fetch medicines');
        const data = await response.json();

        // 后端返回的数据包含 content 字段
        // 如果content存在则使用content，否则使用空数组
        medicines.value = Array.isArray(data.content) ? data.content : [];
      } catch (error) {
        console.error('Error fetching medicines:', error);
        ElMessage.error(`获取药品列表失败: ${error.message}`);
        medicines.value = []; // 出错时也赋一个空数组，保证类型正确
      }
    };


    const showAddMedicineDialog = () => {
      dialogTitle.value = '新增药品';
      Object.assign(medicineForm, {
        id: null,
        name: '',
        specification: '',
        unitPrice: 0,
        stockQuantity: 0,
        minStock: 0,
        status: true,
      });
      dialogVisible.value = true;
    };

    const saveMedicine = async () => {
      try {

        medicineForm.status = medicineForm.status ? 1 : 0;

        // 始终使用 POST 方法添加新的药品
        const url = 'http://localhost:8080/api/medicines/add';
        const response = await fetch(url, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('token')
          },
          body: JSON.stringify(medicineForm),
        });

        if (!response.ok) throw new Error('Save failed');

        ElMessage.success('保存成功');
        dialogVisible.value = false;
        fetchMedicines();
      } catch (error) {
        console.error('Error saving medicine:', error);
        ElMessage.error(`保存失败: ${error.message}`);
      }
    };


    const editMedicine = (medicine) => {
      dialogTitle.value = '编辑药品';
      Object.assign(medicineForm, medicine);
      dialogVisible.value = true;
    };

    const toggleStatus = async (medicine) => {
      try {
        const response = await fetch(
            `http://localhost:8080/api/medicines/${medicine.id}/toggle-status`,
            {
              method: 'PATCH',
              headers: { 'Content-Type': 'application/json' },
            }
        );

        if (!response.ok) throw new Error('Failed to toggle status');

        ElMessage.success(`药品已${medicine.status ? '下架' : '上架'}`);
        fetchMedicines();
      } catch (error) {
        console.error('Error toggling status:', error);
        ElMessage.error(`切换状态失败: ${error.message}`);
      }
    };

    // 初始化获取药品列表
    fetchMedicines();

    return {
      medicines,
      dialogVisible,
      dialogTitle,
      medicineForm,
      showAddMedicineDialog,
      saveMedicine,
      editMedicine,
      toggleStatus,
      importMedicines
    };
  },
};
</script>

<style scoped>
.medicine-list {
  background: white;
  padding: 20px;
  border-radius: 8px;
}

.action-bar {
  margin-bottom: 20px;
}

.low-stock {
  color: #ff4d4f;
}
</style>
