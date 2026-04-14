<template>
  <div style="margin-top: 20px;" >
  <div>
    <el-input v-model="params.sbmc" style="width: 200px" placeholder="请输入场所名称"></el-input>
    <el-input v-model="params.lx" style="width: 200px" placeholder="请输入场所类型"></el-input>
    <el-button type="primary" style="margin-left: 10px;" @click="getDataBysbmc()">查询</el-button>
    <el-button type="primary" style="margin-left: 10px;" @click="reset()">清空</el-button>
    <el-button type="primary" style="margin-left: 10px;" @click="add()">新增</el-button>
    <el-button type="success" style="margin-left: 10px" @click="toMap()">地图</el-button>
  </div>
  <div  style="margin-top: 20px;overflow-y: auto" >

    <el-table :data="tableData" border style="width: 100%"  height="500px">
      <el-table-column prop="id" label="id" ></el-table-column>
      <el-table-column prop="sbmc" label="场所名称" width="180"></el-table-column>
      <el-table-column prop="sbbm" label="场所编码" width="180"></el-table-column>
      <el-table-column prop="dz" label="场所地址"></el-table-column>
      <el-table-column prop="x" label="经度"></el-table-column>
      <el-table-column prop="y" label="纬度"></el-table-column>
      <el-table-column prop="lx" label="场所类型"></el-table-column>
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
            <el-input v-model="form.sbmc" autocomplete="off" style="margin-left: 20px;width: 60%"></el-input>
          </el-form-item>

          <el-form-item label="场所经度" label-width="20%">
            <el-input v-model="form.x" autocomplete="off" style="margin-left: 20px;width: 60%"></el-input>
          </el-form-item>
          <el-form-item label="场所纬度" label-width="20%">
            <el-input v-model="form.y" autocomplete="off" style="margin-left: 20px;width: 60%"></el-input>
          </el-form-item>
          <el-form-item label="场所类型" label-width="20%">
            <el-select v-model="form.lxid" placeholder="请选择" style="width: 90%">
              <el-option v-for="item in sblx" :key="item.lxid" :label="item.lxmc" :value="item.id"></el-option>
            </el-select>
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
      sblx: [],
    }
  },
  //在vue中created相当于初始化方法，在挂载元素之前执行，也就是onload函数
  created() {
    this.getData();
    this.getsblx();

  }
  ,
  methods: {
    getData() {
      // 发送请求获取数据
      //console.log("11"+this.params.pagesize);
      request.get('/vue/dz/getDataBysbmc',{params:this.params}).then(res => {
        if (res.code === "0") {
          console.log(res.data);

          this.tableData = res.data.list;
          this.total = res.data.total;
          this.loading =false;
        }
      })
    },
    getsblx() {
      // 发送请求获取数据

      request.get('/vue/dz/getsblx',{params:this.params}).then(res => {
        if (res.code === "0") {
          console.log(res.data);

          this.sblx = res.data.list;

        }
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
      request.delete('/vue/dz/'+obj).then(res => {
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
      request.post('/vue/dz/savedz',this.form).then(res => {
        if (res.code === "0") {
          this.$message({
            message: '插入成功',
            type: 'success'
          });
          this.dialogFormVisible=false;
        }
      })
    },
    getDataBysbmc(){

       request.get('/vue/dz/getDataBysbmc',{params:this.params}).then(res => {
             if (res.code === "0") {
               console.log(res.data);
               this.tableData = res.data.list;
               this.total = res.data.total;
             }
           })
       },

    reset(){
      this.params={}
    },
    handleSizeChange(pagesize){
      this.params.pagesize = pagesize;
      request.get('/vue/dz/getDataBysbmc',{params:this.params}).then(res => {
        if (res.code === "0") {
          console.log(res.data);
          this.tableData = res.data.list;
          this.total = res.data.total;
        }
      })
    },
    handleCurrentChange(pagenum){
      this.params.pagenum = pagenum;
      request.get('/vue/dz/getDataBysbmc',{params:this.params}).then(res => {
        if (res.code === "0") {
          console.log(res.data);
          this.tableData = res.data.list;
          this.total = res.data.total;
        }
      })
    },
    toMap(){

    }
}}
</script>