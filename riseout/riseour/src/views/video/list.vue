<template>
  <div class="video-list-page">
    <el-card>
      <div slot="header" class="header-title">视频列表查看</div>

      <div class="toolbar">
        <el-input
          v-model="params.videoname"
          class="toolbar-input"
          placeholder="按视频名称查询"
          clearable
          @keyup.enter.native="search"
        ></el-input>
        <el-input
          v-model="params.videocode"
          class="toolbar-input"
          placeholder="按视频代码查询"
          clearable
          @keyup.enter.native="search"
        ></el-input>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button @click="reset">重置</el-button>
      </div>

      <div class="result-tip">共 {{ total }} 条，当前页 {{ tableData.length }} 条</div>

      <div v-if="tableData.length > 0" class="waterfall">
        <div v-for="item in tableData" :key="item.id" class="video-card">
          <div class="cover-box is-clickable" @click="play(item)">
            <el-image
              v-if="item._coverUrl"
              class="cover-image"
              :src="item._coverUrl"
              fit="cover"
            >
              <div slot="error" class="cover-placeholder">暂无封面</div>
            </el-image>
            <div v-else class="cover-placeholder">暂无封面</div>
          </div>

          <div class="card-body">
            <div class="video-title is-clickable" :title="item.videoname" @click="play(item)">
              {{ item.videoname || "未命名视频" }}
            </div>
            <div class="video-meta">
              <span class="meta-code">{{ item.videocode || "-" }}</span>
              <span class="meta-date">{{ item.videodate || "-" }}</span>
            </div>
            <div class="card-actions">
              <el-button type="text" @click="openWithPotPlayer(item)">PotPlayer播放</el-button>
              <el-button type="text" @click="gotoManage">修改信息</el-button>
            </div>
          </div>
        </div>
      </div>

      <el-empty v-else description="暂无视频数据"></el-empty>

      <div class="pager-wrap">
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

    <el-dialog
      :title="currentVideo.videoname || '视频播放'"
      :visible.sync="playDialogVisible"
      width="65%"
      @close="onPlayDialogClose"
      @closed="onPlayDialogClosed"
    >
      <video
        ref="videoPlayer"
        v-if="currentVideo._videoUrl"
        :src="currentVideo._videoUrl"
        controls
        autoplay
        style="width: 100%; max-height: 520px;"
      ></video>
      <div v-else class="dialog-empty">该视频暂无可播放地址</div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "VideoList",
  data() {
    return {
      backendBase: "http://localhost:12345",
      params: {
        pagenum: 1,
        pagesize: 20,
        videoname: "",
        videocode: ""
      },
      total: 0,
      tableData: [],
      playDialogVisible: false,
      currentVideo: {}
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      request.get("/vue/video/page", { params: this.params }).then((res) => {
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
        _coverUrl: this.normalizeCoverUrl(row.videoimg),
        _videoUrl: this.normalizeVideoUrl(row)
      };
    },
    isLocalPath(value) {
      if (!value || typeof value !== "string") {
        return false;
      }
      const v = value.trim();
      return /^[a-zA-Z]:[\\/]/.test(v) || /^[a-zA-Z]:\/\//.test(v) || v.startsWith("file:/");
    },
    normalizeVideoUrl(row) {
      const value = row?.videourl;
      if (!value || typeof value !== "string") {
        return "";
      }
      const v = value.trim();
      if (!v) {
        return "";
      }
      if (v.startsWith("http://") || v.startsWith("https://")) {
        return v;
      }
      if (this.isLocalPath(v)) {
        return `${this.backendBase}/vue/video/play/${row.id}`;
      }
      if (v.startsWith("/")) {
        return `${this.backendBase}${v}`;
      }
      return v;
    },
    normalizeCoverUrl(value) {
      if (!value || typeof value !== "string") {
        return "";
      }
      const v = value.trim();
      if (!v) {
        return "";
      }
      if (v.startsWith("http://") || v.startsWith("https://")) {
        return v;
      }
      if (v.startsWith("/")) {
        return `${this.backendBase}${v}`;
      }
      return `${this.backendBase}/vue/files/${v}`;
    },
    search() {
      this.params.pagenum = 1;
      this.fetchData();
    },
    reset() {
      this.params = {
        pagenum: 1,
        pagesize: 20,
        videoname: "",
        videocode: ""
      };
      this.fetchData();
    },
    play(row) {
      this.stopVideoPlayback();
      this.currentVideo = row;
      this.playDialogVisible = true;
    },
    openWithPotPlayer(row) {
      if (!row || !row.id) {
        this.$message.warning("缺少视频ID");
        return;
      }
      this.stopVideoPlayback();
      this.playDialogVisible = false;
      this.currentVideo = {};
      request.post(`/vue/video/open-potplayer/${row.id}`).then((res) => {
        if (res.code === "0") {
          this.$message.success("已调用 PotPlayer 播放");
          return;
        }
        this.$message.error(res.message || "调用 PotPlayer 失败");
      });
    },
    stopVideoPlayback() {
      const player = this.$refs.videoPlayer;
      if (player && typeof player.pause === "function") {
        player.pause();
        player.currentTime = 0;
      }
    },
    onPlayDialogClose() {
      this.stopVideoPlayback();
    },
    onPlayDialogClosed() {
      this.stopVideoPlayback();
      this.currentVideo = {};
    },
    gotoManage() {
      this.$router.push("/video/manage");
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
};
</script>

<style scoped>
.video-list-page {
  margin-top: 20px;
  height: calc(100vh - 120px);
  overflow-y: auto;
  overflow-x: hidden;
  padding-right: 4px;
}

.header-title {
  font-size: 16px;
  font-weight: 600;
}

.toolbar {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 8px;
}

.toolbar-input {
  width: 200px;
}

.result-tip {
  margin-bottom: 10px;
  color: #909399;
  font-size: 12px;
}

.waterfall {
  display: grid;
  grid-template-columns: repeat(6, minmax(0, 1fr));
  gap: 12px;
}

.video-card {
  display: block;
  width: 100%;
  margin: 0;
  border: 1px solid #e6e6e6;
  border-radius: 4px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.cover-box {
  position: relative;
  width: 100%;
  padding-top: 132%;
  background: #f2f3f5;
}

.cover-image {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
}

.cover-placeholder {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
  font-size: 13px;
  background: #f2f3f5;
}

.card-body {
  padding: 8px 10px 6px;
}

.video-title {
  font-size: 14px;
  line-height: 1.45;
  color: #222;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  min-height: 40px;
}

.video-meta {
  margin-top: 6px;
  display: flex;
  justify-content: space-between;
  gap: 8px;
  font-size: 12px;
  color: #c33;
}

.meta-code,
.meta-date {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-actions {
  margin-top: 6px;
  border-top: 1px dashed #eee;
  padding-top: 4px;
  display: flex;
  justify-content: space-between;
}

.is-clickable {
  cursor: pointer;
}

.pager-wrap {
  margin-top: 12px;
}

.dialog-empty {
  color: #909399;
}
</style>
