<template>
  <div style="margin-top: 20px;" >
  <div>
    <el-input v-model="params.videoname" style="width: 200px" placeholder="请输入视频名称"></el-input>
    <el-input v-model="params.videocode" style="width: 200px" placeholder="请输入视频代码"></el-input>
    <el-button type="primary" style="margin-left: 10px;" @click="getData()">查询</el-button>
    <el-button type="primary" style="margin-left: 10px;" @click="reset()">清空</el-button>
    <el-button type="primary" style="margin-left: 10px;" @click="add()">新增</el-button>
  </div>



  <div  style="margin-top: 20px;overflow-y: auto" >

    <el-table :data="tableData" border style="width: 100%"  height="500px">
      <el-table-column prop="id" label="id" ></el-table-column>
      <el-table-column prop="videoname" label="视频名称" width="180"></el-table-column>
      <el-table-column prop="videocode" label="视频代码" width="180"></el-table-column>
      <el-table-column prop="videomsg" label="视频信息"></el-table-column>
      <el-table-column prop="videodate" label="视频代码"></el-table-column>
      <el-table-column prop="videoactor" label="视频演员"></el-table-column>

      <el-table-column  label="视频封面">

        <template v-slot="scope">
          <el-image
              style="width: 70px; height: 70px; border-radius: 50%"
              :src="'http://localhost:12345/vue/files/'+ scope.row.videoimg ">
          </el-image>
        </template>

      </el-table-column>
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
          <el-form-item label="视频名称" label-width="20%">
            <el-input v-model="form.videoname" autocomplete="off" style="margin-left: 20px;width: 60%"></el-input>
          </el-form-item>

          <el-form-item label="视频编码" label-width="20%">
            <el-input v-model="form.videocode" autocomplete="off" style="margin-left: 20px;width: 60%"></el-input>
          </el-form-item>
          <el-form-item label="视频演员" label-width="20%">
            <el-input v-model="form.videoactor" autocomplete="off" style="margin-left: 20px;width: 60%"></el-input>
          </el-form-item>
          <el-form-item label="视频封面" label-width="20%" v-model="form.videoimg">
            <el-upload
                action="http://localhost:12345/vue/files/upload"
                :on-success="successupload">
              <el-button size="small" type="primary">点击上传</el-button>
            </el-upload>
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
    }
  },
  //在vue中created相当于初始化方法，在挂载元素之前执行，也就是onload函数
  created() {
    this.getData();
    //this.getvideo();

  }
  ,
  methods: {
    getData() {
      // 发送请求获取数据
      console.log("11"+this.params.pagesize);
      request.get('/vue/video/getVideo',{params:this.params}).then(res => {
        if (res.code === "0") {
          console.log(res.data);

          this.tableData = res.data.list;
          this.total = res.data.total;
          this.loading =false;
        }
      })
    },
    successupload(res){
        console.log(res);
        this.form.videoimg = res.data;


    },



    getvideo() {
      // 发送请求获取数据
      console.log("11"+this.params.pagesize);
      request.get('/vue/video/getVideo',{params:this.params}).then(res => {
        if (res.code === "0") {
          //console.log(res.data);

          //this.tableData = res.data.list;
          //this.total = res.data.total;
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
      request.delete('/vue/video/'+obj).then(res => {
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
      request.post('/vue/video/saveVideo',this.form).then(res => {
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
    getDataBysbmc(){

       request.get('/vue/video/getVideo',{params:this.params}).then(res => {
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
      request.get('/vue/video/getvideo',{params:this.params}).then(res => {
        if (res.code === "0") {
          console.log(res.data);
          this.tableData = res.data.list;
          this.total = res.data.total;
        }
      })
    },
    handleCurrentChange(pagenum){
      this.params.pagenum = pagenum;
      request.get('/vue/video/getvideo',{params:this.params}).then(res => {
        if (res.code === "0") {
          console.log(res.data);
          this.tableData = res.data.list;
          this.total = res.data.total;
        }
      })
    },
}}
</script>
