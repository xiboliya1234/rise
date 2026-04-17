<template>
  <div style="margin-top: 20px;">
    <el-card>
      <div slot="header">
        <span>视频信息管理</span>
      </div>

      <div>
        <el-input v-model="params.videoname" style="width: 220px; margin-right: 10px;" placeholder="按视频名称查询"></el-input>
        <el-input v-model="params.videocode" style="width: 220px; margin-right: 10px;" placeholder="按视频代码查询"></el-input>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button @click="reset">重置</el-button>
      </div>

      <el-table :data="tableData" border style="width: 100%; margin-top: 15px;">
        <el-table-column prop="id" label="ID" width="70"></el-table-column>
        <el-table-column prop="videoname" label="视频名称" min-width="180"></el-table-column>
        <el-table-column prop="videocode" label="视频代码" min-width="140"></el-table-column>
        <el-table-column prop="videoactor" label="演员" min-width="180"></el-table-column>
        <el-table-column prop="videotag" label="标签" min-width="150"></el-table-column>
        <el-table-column label="封面" width="110">
          <template v-slot="scope">
            <el-image v-if="scope.row._coverUrl" :src="scope.row._coverUrl" style="width: 80px; height: 45px;" fit="cover"></el-image>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template v-slot="scope">
            <el-button type="text" @click="edit(scope.row)">编辑</el-button>
            <el-popconfirm title="确认删除该视频吗？" @confirm="remove(scope.row.id)">
              <el-button slot="reference" type="text" style="color: #f56c6c;">删除</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 12px;">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="params.pagenum"
          :page-sizes="[10, 20, 50]"
          :page-size="params.pagesize"
          layout="total, sizes, prev, pager, next"
          :total="total"
        ></el-pagination>
      </div>
    </el-card>

    <el-dialog title="编辑视频信息" :visible.sync="dialogVisible" width="40%">
      <el-form :model="form" label-width="100px">
        <el-form-item label="视频名称">
          <el-input v-model="form.videoname"></el-input>
        </el-form-item>
        <el-form-item label="视频代码">
          <el-input v-model="form.videocode"></el-input>
        </el-form-item>
        <el-form-item label="演员信息">
          <el-input v-model="form.videoactor"></el-input>
        </el-form-item>
        <el-form-item label="视频标签">
          <el-input v-model="form.videotag"></el-input>
        </el-form-item>
        <el-form-item label="视频简介">
          <el-input type="textarea" :rows="3" v-model="form.videomsg"></el-input>
        </el-form-item>
        <el-form-item label="视频地址">
          <el-input v-model="form.videourl"></el-input>
        </el-form-item>
        <el-form-item label="封面地址">
          <el-input v-model="form.videoimg"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "VideoManage",
  data() {
    return {
      backendBase: "http://localhost:12345",
      params: {
        pagenum: 1,
        pagesize: 10,
        videoname: "",
        videocode: ""
      },
      total: 0,
      tableData: [],
      dialogVisible: false,
      form: {}
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      request.get('/vue/video/page', { params: this.params }).then(res => {
        if (res.code === "0") {
          const rows = res.data?.list || [];
          this.tableData = rows.map(this.normalizeRow);
          this.total = res.data?.total || 0;
          return;
        }
        this.$message.error(res.message || "查询失败");
      });
    },
    normalizeRow(row) {
      return {
        ...row,
        _coverUrl: this.normalizeMediaUrl(row.videoimg, true),
      };
    },
    normalizeMediaUrl(value, isCover) {
      if (!value) {
        return "";
      }
      if (typeof value !== "string") {
        return "";
      }
      if (value.startsWith("http://") || value.startsWith("https://")) {
        return value;
      }
      if (value.startsWith("/")) {
        return `${this.backendBase}${value}`;
      }
      return isCover ? `${this.backendBase}/vue/files/${value}` : value;
    },
    search() {
      this.params.pagenum = 1;
      this.fetchData();
    },
    reset() {
      this.params = {
        pagenum: 1,
        pagesize: 10,
        videoname: "",
        videocode: ""
      };
      this.fetchData();
    },
    edit(row) {
      this.form = { ...row };
      this.dialogVisible = true;
    },
    submit() {
      if (!this.form.id) {
        this.$message.warning("缺少视频ID");
        return;
      }
      request.put(`/vue/video/${this.form.id}`, this.form).then(res => {
        if (res.code === "0") {
          this.$message.success("保存成功");
          this.dialogVisible = false;
          this.fetchData();
          return;
        }
        this.$message.error(res.message || "保存失败");
      });
    },
    remove(id) {
      request.delete(`/vue/video/${id}`).then(res => {
        if (res.code === "0") {
          this.$message.success("删除成功");
          this.fetchData();
          return;
        }
        this.$message.error(res.message || "删除失败");
      });
    },
    handleSizeChange(size) {
      this.params.pagesize = size;
      this.params.pagenum = 1;
      this.fetchData();
    },
    handleCurrentChange(page) {
      this.params.pagenum = page;
      this.fetchData();
    }
  }
}
</script>
