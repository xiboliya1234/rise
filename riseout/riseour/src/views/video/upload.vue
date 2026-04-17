<template>
  <div style="margin-top: 20px;">
    <el-card>
      <div slot="header">
        <span>视频信息登记（仅保存地址元数据）</span>
      </div>

      <el-alert
        title="该模块不会上传视频二进制文件，只会保存“前缀 + 文件名”生成的视频地址。"
        type="info"
        :closable="false"
        style="margin-bottom: 16px;"
      ></el-alert>

      <el-form :model="form" label-width="120px">
        <el-form-item label="文件夹前缀">
          <el-select
            v-model="selectedPrefixId"
            placeholder="请选择前缀（从数据库读取）"
            style="width: 460px;"
            @change="onPrefixChange"
          >
            <el-option
              v-for="item in prefixOptions"
              :key="item.id"
              :label="formatPrefixLabel(item)"
              :value="item.id"
            ></el-option>
          </el-select>
          <el-button type="text" @click="fetchPrefixOptions">刷新前缀</el-button>
        </el-form-item>

        <el-form-item label="本地视频选择">
          <input
            ref="localFileInput"
            type="file"
            accept="video/*"
            style="display: none;"
            @change="onLocalFileChange"
          />
          <el-button type="primary" plain @click="pickLocalFile">选择本地视频</el-button>
          <span v-if="selectedFileName" style="margin-left: 10px; color: #606266;">{{ selectedFileName }}</span>
          <el-button style="margin-left: 12px;" type="success" @click="buildUrl">生成地址</el-button>
          <el-button style="margin-left: 8px;" type="primary" :loading="submitting" @click="submit">上传并保存</el-button>
          <div style="margin-top: 6px; color: #909399; font-size: 12px; line-height: 1.5;">
            地址规则：所选前缀 + 文件名，例如：<code>C://file/demo.mp4</code>
          </div>
        </el-form-item>

        <el-form-item label="视频名称">
          <el-input v-model="form.videoname" placeholder="请输入视频名称"></el-input>
        </el-form-item>

        <el-form-item label="视频代码">
          <el-input v-model="form.videocode" placeholder="请输入视频代码（唯一）"></el-input>
        </el-form-item>

        <el-form-item label="视频地址">
          <el-input
            v-model="form.videourl"
            readonly
            placeholder="选择前缀和本地文件后自动生成"
          ></el-input>
        </el-form-item>

        <el-form-item label="封面地址">
          <el-input
            v-model="form.videoimg"
            placeholder="例如：http://localhost:12345/static/img/a.jpg"
          ></el-input>
          <div v-if="coverPreview" style="margin-top: 10px;">
            <el-image :src="coverPreview" style="width: 150px; height: 90px;" fit="cover"></el-image>
          </div>
        </el-form-item>

        <el-form-item label="演员信息">
          <el-input v-model="form.videoactor" placeholder="多个演员用逗号分隔"></el-input>
        </el-form-item>

        <el-form-item label="视频标签">
          <el-input v-model="form.videotag" placeholder="例如：动作,悬疑"></el-input>
        </el-form-item>

        <el-form-item label="视频日期">
          <el-input v-model="form.videodate" placeholder="例如：2026"></el-input>
        </el-form-item>

        <el-form-item label="视频简介">
          <el-input type="textarea" :rows="3" v-model="form.videomsg" placeholder="请输入视频简介"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="submit">上传并保存</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "VideoUpload",
  data() {
    return {
      submitting: false,
      selectedFileName: "",
      selectedPrefixId: null,
      prefixOptions: [],
      form: {
        videoname: "",
        videocode: "",
        videoactor: "",
        videotag: "",
        videomsg: "",
        videourl: "",
        videoimg: "",
        videodate: ""
      }
    };
  },
  computed: {
    coverPreview() {
      const v = this.form.videoimg || "";
      if (!v) {
        return "";
      }
      if (v.startsWith("http://") || v.startsWith("https://") || v.startsWith("/")) {
        return v;
      }
      return "";
    }
  },
  created() {
    this.fetchPrefixOptions();
  },
  methods: {
    fetchPrefixOptions() {
      request.get("/vue/video/prefix/list").then((res) => {
        if (res.code !== "0") {
          this.$message.error(res.message || "前缀列表获取失败");
          return;
        }
        const rows = Array.isArray(res.data) ? res.data : [];
        this.prefixOptions = rows.filter((item) => item && item.prefixValue);
        if (this.prefixOptions.length === 0) {
          this.selectedPrefixId = null;
          this.$message.warning("数据库里没有可用前缀，请先配置");
          return;
        }
        if (!this.selectedPrefixId || !this.getSelectedPrefix()) {
          this.selectedPrefixId = this.prefixOptions[0].id;
        }
        this.buildUrl();
      });
    },
    formatPrefixLabel(item) {
      if (!item) {
        return "";
      }
      if (item.prefixName) {
        return `${item.prefixName} (${item.prefixValue})`;
      }
      return item.prefixValue;
    },
    getSelectedPrefix() {
      return this.prefixOptions.find((item) => item.id === this.selectedPrefixId) || null;
    },
    joinPrefixAndName(prefix, fileName) {
      if (!prefix) {
        return fileName || "";
      }
      if (!fileName) {
        return prefix;
      }
      const hasSlash = prefix.endsWith("/") || prefix.endsWith("\\");
      return hasSlash ? `${prefix}${fileName}` : `${prefix}/${fileName}`;
    },
    onPrefixChange() {
      this.buildUrl();
    },
    pickLocalFile() {
      if (this.$refs.localFileInput) {
        this.$refs.localFileInput.click();
      }
    },
    onLocalFileChange(event) {
      const input = event && event.target;
      const file = input && input.files && input.files[0] ? input.files[0] : null;
      if (!file) {
        return;
      }

      this.selectedFileName = file.name || "";

      if (!this.form.videoname) {
        const dot = file.name.lastIndexOf(".");
        this.form.videoname = dot > 0 ? file.name.substring(0, dot) : file.name;
      }

      if (!this.form.videocode) {
        const ts = Date.now();
        const cleanName = (this.form.videoname || "video").replace(/\s+/g, "_");
        this.form.videocode = `${cleanName}_${ts}`;
      }

      this.buildUrl();
    },
    buildUrl() {
      if (!this.selectedFileName) {
        this.form.videourl = "";
        return;
      }
      const selected = this.getSelectedPrefix();
      if (!selected || !selected.prefixValue) {
        this.form.videourl = "";
        return;
      }
      this.form.videourl = this.joinPrefixAndName(selected.prefixValue.trim(), this.selectedFileName);
    },
    validate() {
      if (!this.selectedPrefixId) {
        this.$message.warning("请先选择文件夹前缀");
        return false;
      }
      if (!this.selectedFileName) {
        this.$message.warning("请先选择本地视频文件");
        return false;
      }
      if (!this.form.videoname) {
        this.$message.warning("请填写视频名称");
        return false;
      }
      if (!this.form.videocode) {
        this.$message.warning("请填写视频代码");
        return false;
      }
      if (!this.form.videourl) {
        this.$message.warning("视频地址生成失败，请检查前缀配置");
        return false;
      }
      return true;
    },
    submit() {
      if (!this.validate()) {
        return;
      }
      this.submitting = true;
      request
        .post("/vue/video", this.form)
        .then((res) => {
          if (res.code === "0") {
            this.$message.success("视频信息已入库");
            this.resetForm();
            return;
          }
          this.$message.error(res.message || "保存失败");
        })
        .finally(() => {
          this.submitting = false;
        });
    },
    resetForm() {
      this.form = {
        videoname: "",
        videocode: "",
        videoactor: "",
        videotag: "",
        videomsg: "",
        videourl: "",
        videoimg: "",
        videodate: ""
      };
      this.selectedFileName = "";
      if (this.$refs.localFileInput) {
        this.$refs.localFileInput.value = "";
      }
    }
  }
};
</script>