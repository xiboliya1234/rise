<template>
  <div class="qr-container">
    <h1>文字转二维码工具</h1>

    <div class="input-area">
      <textarea
          v-model="text"
          placeholder="请输入要转换成二维码的文字"
          @input="handleInput"
      ></textarea>
      <p v-if="text.length > 0" class="char-count">已输入 {{ text.length }} 个字符</p>
    </div>

    <div class="qrcode-area" v-if="showQrCode">
      <div class="qrcode-wrapper" ref="qrcodeWrapper"></div>
      <button class="download-btn" @click="downloadQrCode">下载二维码</button>
    </div>

    <p v-if="text.length > 0 && !showQrCode" class="error-msg">
      {{ errorMsg || '内容过长，无法生成二维码' }}
    </p>
  </div>
</template>

<script>
import QRCode from 'qrcodejs2';

export default {
  name: 'QrCodeGenerator',
  data() {
    return {
      text: '',
      qrCode: null,
      showQrCode: false,
      maxChars: 2000, // 设置最大字符限制
      errorMsg: '' // 错误信息
    };
  },
  methods: {
    handleInput() {
      // 清除错误信息
      this.errorMsg = '';

      // 输入时先清除旧二维码
      if (this.qrCode) {
        this.qrCode.clear();
        this.showQrCode = false;
      }

      // 检查字符数是否超过限制
      if (this.text.length <= this.maxChars) {
        // 使用nextTick确保DOM更新后再生成二维码
        this.$nextTick(() => {
          this.generateQrCode();
        });
      } else {
        this.showQrCode = false;
        this.errorMsg = `输入内容超过最大限制(${this.maxChars}字符)`;
      }
    },
    generateQrCode() {
      if (!this.text) {
        this.showQrCode = false;
        return;
      }

      try {
        console.log('生成二维码的文本内容:', this.text); // 调试输出

        // 如果已存在二维码实例，先清除
        if (this.qrCode) {
          this.qrCode.clear();
        }

        // 创建新的二维码
        this.qrCode = new QRCode(this.$refs.qrcodeWrapper, {
          text: this.text,
          width: 200,
          height: 200,
          colorDark: '#000000',
          colorLight: '#ffffff',
          correctLevel: QRCode.CorrectLevel.H
        });

        this.showQrCode = true;
      } catch (error) {
        console.error('生成二维码失败:', error);
        this.showQrCode = false;
        this.errorMsg = '生成二维码失败，请检查输入内容';
      }
    },
    downloadQrCode() {
      if (!this.qrCode) return;

      try {
        // 获取二维码图片
        const canvas = this.$refs.qrcodeWrapper.querySelector('canvas');
        if (!canvas) {
          this.errorMsg = '无法获取二维码图像';
          return;
        }

        // 创建下载链接
        const link = document.createElement('a');
        link.download = 'qrcode.png';
        link.href = canvas.toDataURL('image/png');
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      } catch (error) {
        console.error('下载二维码失败:', error);
        this.errorMsg = '下载失败，请稍后重试';
      }
    }
  },
  beforeDestroy() {
    // 组件销毁前清除二维码实例
    if (this.qrCode) {
      this.qrCode.clear();
    }
  }
}
</script>

<style scoped>
.qr-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

h1 {
  text-align: center;
  color: #333;
}

.input-area {
  margin-bottom: 20px;
}

textarea {
  width: 100%;
  height: 120px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
  font-size: 16px;
}

.char-count {
  margin-top: 5px;
  text-align: right;
  color: #666;
  font-size: 14px;
}

.qrcode-area {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 20px;
}

.qrcode-wrapper {
  margin-bottom: 15px;
}

.download-btn {
  padding: 8px 16px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.download-btn:hover {
  background-color: #45a049;
}

.error-msg {
  color: #f44336;
  text-align: center;
}
</style>