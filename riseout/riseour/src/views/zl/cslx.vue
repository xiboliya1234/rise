<template>
  <div style="margin-top: 20px;" >
  <div>

    <el-button type="primary" style="margin-left: 10px;" @click="add()">新增</el-button>
    <el-popconfirm title="确定删除这些数据吗？" @confirm="deleteAll()">
      <el-button slot="reference" type="danger" style="margin-left: 5px">批量删除</el-button>
    </el-popconfirm>
    <el-button type="success" style="margin-left: 10px" @click="exp()">全量导出报表</el-button>
    <el-upload action="http://localhost:23333/vue/dz/cslx/upload" style="display: inline-block; margin-left: 10px" :show-file-list="false" :on-success="successUpload">
      <el-button size="small" type="primary">批量导入</el-button>
    </el-upload>
  </div>
  <div  style="margin-top: 20px;overflow-y: auto" >

    <el-table :data="tableData" border style="width: 100%"  height="500px" :row-key="getRowKeys" @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="id" label="id" ></el-table-column>
      <el-table-column prop="lxmc" label="场所类型"></el-table-column>
      <el-table-column  label="操作">
        <template v-slot="operate">
          <el-button  size="mini" @click="edit(operate.row)">编辑</el-button>
          <el-popconfirm   title="确定删除吗？" @confirm="deleteById(operate.row.id)">
            <el-button slot="reference" size="mini">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

  </div>


      <div style="margin-top: 10px">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="params.pagenum"
            :page-sizes="[5, 10, 15, 20]"
            :page-size="params.pageSize"
            layout="total, sizes, prev, pager, next"
            :total="total" style="position: fixed;  bottom: 10px;">
        </el-pagination>


    </div>
    <div>

      <el-dialog title="请填写信息" :visible.sync="dialogFormVisible" width="30%">
        <el-form :model="form">
          <el-form-item label="场所名称" label-width="20%">
            <el-input v-model="form.lxmc" autocomplete="off" style="margin-left: 20px;width: 60%"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary"  @click="submit()">确 定</el-button>
        </div>
      </el-dialog>

    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import getDataCommon from "@/utils/getDataCommon";
import qs from "qs";

export default {
  data() {
    return {
      input: '',
      params: {
        pagenum:1,
        pagesize:10
      },
      total:0,
      tableData: [],
      dialogFormVisible:false,
      form: {},
      options: [],
      loading: true,
      multipleSelection: [],
    }
  },
  //在vue中created相当于初始化方法，在挂载元素之前执行，也就是onload函数
  created() {
    this.getData();


  }
  ,
  methods: {
    getData() {
      // 发送请求获取数据
      request.get('/vue/dz/getsblx',{params:this.params}).then(res => {
          this.tableData = res.data.list;
          this.total = res.data.total;
          this.loading =false;
      })
    },

    add(){
      this.form = {};
      this.dialogFormVisible=true;
    },
    edit(obj){
    // console.log(obj.sbmc);
      this.form = obj;
      this.dialogFormVisible=true;
    },
    deleteById(obj){
      console.log(obj);
      request.delete('/vue/dz/Cslx/'+obj).then(res => {
        if (res.code === "0") {
          this.$message({
            message: '删除成功',
            type: 'success'
          });
          this.getData();
        }
      })
    },
    submit(){

      console.log("sbmc"+this.form.id);
      request.post('/vue/dz/saveCslx',this.form).then(res => {
        if (res.code === "0") {
          this.$message({
            message: '插入成功',
            type: 'success'
          });

          this.dialogFormVisible=false;
          this.getData();
        }
      })
    },


    reset(){
      this.params={}
    },
    handleSizeChange(pagesize){
      this.params.pagesize = pagesize;
      request.get('/vue/dz/getsblx',{params:this.params}).then(res => {
        if (res.code === "0") {
          console.log(res.data);
          this.tableData = res.data.list;
          this.total = res.data.total;
        }
      })
    },
    handleCurrentChange(pagenum){
      this.params.pagenum = pagenum;
      request.get('/vue/dz/getsblx',{params:this.params}).then(res => {
        if (res.code === "0") {
          console.log(res.data);
          this.tableData = res.data.list;
          this.total = res.data.total;
        }
      })
    },
    deleteAll(){
      if( this.multipleSelection.length===0) {
        this.$message({
          message: '请勾选要删除的选项',
          type: 'warning'
        });
      }
      request.post('/vue/dz/deleteCslxBatchIds',this.multipleSelection).then(res => {
        if (res.code === "0") {
          this.$message({
            message: '删除成功',
            type: 'success'
          });


          this.getData();
        }
      })
    },
    handleSelectionChange(val){
      this.multipleSelection = val;
      console.log(this.multipleSelection)
    },
    getRowKeys(row) {
      return row.id;
    },
    exp(){
      alert(request.baseURL);
      let user = localStorage.getItem("user");
      location.href = 'http://localhost:23333/vue/dz/cslx/export?token=' + JSON.parse(user).token
    },
    successUpload(res) {
      if (res.code === '0') {
        this.$message.success("批量导入成功")
        this.getData()
      } else {
        this.$message.error(res.message)
      }
    },
}}
</script>