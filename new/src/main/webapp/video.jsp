<!DOCTYPE html>
<html lang='zh'>
<head>
    <meta charset='UTF-8'>
    <title>mm</title>
    <style>
        *{
            margin:0;
            padding:0;
            list-style:none;
            border:none;
        }
        .icon {
            width: 1em;
            height: 1em;
            vertical-align: -0.15em;
            fill: currentColor;
            color:#fff;
            overflow: hidden;
            font-size:1.5em;
        }
        #wrap{
            position:relative;
            overflow:hidden;
            width:100%;
            background-color:rgb(0, 0, 0);
        }
        /* 标题 */
        h3{
            position:absolute;
            top:-40px;
            left:0;
            width:100%;
            height:30px;
            text-align:center;
            line-height:30px;
            font-family:"楷体";
            color:#fff;
            background:rgba(0,0,0,.3);
            transition:0.5s;
        }
        #wrap:hover h3{
            top:0;
        }
        #video{
            width: 100%;
        }
        #video video{
            height:100%;
            display:block;
            margin:0 auto;
        }
        /* 控制台 */
        .controls-wrap{
            position:absolute;
            bottom:-40px;
            left:0;
            width:100%;
            height:40px;
            background-color:rgba(0, 0, 0, .3);
            transition:0.5s;
        }
        #wrap:hover .controls-wrap{
            bottom:0px;
        }
        #controls{
            display:flex;
            justify-content:space-around;
            align-items: center;
        }
        /* 播放按钮 */
        #controls .play{
            width:40px;
            height:40px;
            background:rgba(0, 0, 0, 0);
        }
        /* 音量 */
        #controls .volume{
            position:relative;
            width:40px;
            height:40px;
        }
        /* 音量按钮 */
        #controls .volume .volume-btn{
            display:block;
            width:40px;
            height:40px;
            background-color:rgb(0, 0, 0, 0);
        }
        /* 音量条 */
        #controls .volume .volume-item{
            display:none;
            position:absolute;
            top:-130px;
            left:0px;
            width:40px;
            height:130px;
            background:rgba(0, 0, 0, 0.3);
            border-radius:8px;
        }
        #controls .volume .volume-item .title{
            position:absolute;
            top:5px;
            left:0px;
            width:100%;
            height:15px;
            text-align:center;
            line-height:15px;
            color:#fff;
            font-size:12px;
        }
        #controls .volume .volume-bar{
            position:absolute;
            bottom:5px;
            left:18px;
            width:5px;
            height:100px;
            background:linear-gradient(green 100%,#ccc 0%);
            border-radius:10px;
        }
        #controls .volume:hover .volume-item{
            display:block;
        }
        /* 进度条 */
        #controls .progress{
            width:100%;
            height:5px;
            border-radius:10px 10px;
            background:linear-gradient(to right,green 0%,#ccc 0%);
        }
        /* 文件 */
        #controls #file{
            width:40px;
            margin-left:10px;
        }
        #controls #file input{
            position:absolute;
            right:0;
            width: 40px;
            height: 40px;
            opacity:0;
        }
        /* 暂停视频 */
        #play-video{
            display:none;
            position:absolute;
            top:30%;
            left:45%;
            width:100px;
            height:100px;
            background-color:rgba(0, 0, 0, .3);
            border-radius:50%;
            font-size:40px;
        }
        #play-video svg{
            margin:20% 25%;
        }
    </style>
</head>
    <div id="wrap">
        <!-- 标题 -->
        <h3>我的视频</h3>
        <!-- 音频 -->
        <div id="video">
            <video src="static/The.Last.of.Us.S01E01.Episode.1.1080p.HMAX.WEB-DL.DDP5.1.Atmos.H.264-SMURF.chs.eng.mp4" loop ></video>
        </div>
        <!-- 控制台 -->
        <div class="controls-wrap">
            <div id="controls">
                <!-- 播放按钮 -->
                <button class="play">
                    <svg class="icon" aria-hidden="true">
                        <use xlink:href="#icon-bofang4"></use>
                    </svg>
                </button>
                <!-- 音量控制 -->
                <div class="volume">
                    <!-- 音量按钮 -->
                    <button class="volume-btn">
                        <svg class="icon" aria-hidden="true">
                            <use xlink:href="#icon-yinliang"></use>
                        </svg>
                    </button>
                    <!-- 音量条 -->
                    <div class="volume-item">
                        <div class="title">100%</div>
                        <div class="volume-bar"></div>
                    </div>
                </div>
                <!-- 视频进度条 -->
                <div class="progress"></div>
                <!-- 文件 -->
                <div id="file">
                    <svg class="icon" aria-hidden="true">
                        <use xlink:href="#icon-wenjian"></use>
                    </svg>
                    <input type="file" id="io">
                </div>
            </div>
        </div>
        <!-- 暂停 -->
        <div id="play-video">
            <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-bofang4"></use>
            </svg>
        </div>
    </div>
<body>
    <script>
        let videoBox = document.getElementById("video");
 
        //每次窗口改变时更改视频的高度
        (function getVideoHeight(){
            videoBox.style.height = document.documentElement.clientHeight * 1 / 1 + "px";//视频的高度为浏览器的窗口3:4
            window.onresize = getVideoHeight;//每次窗口改变时就会触发重新获取
        }());
 
 
        //播放视频按钮
        let video = document.querySelector("#video video");
        let aPlay = document.querySelector("#controls .play");
        let playIco = document.querySelector("#controls .play use");
        let playVideoBox = document.getElementById("play-video");
        let playState = false;//暂停
        aPlay.onclick = playVideo;
        video.onclick = playVideo;
        playVideoBox.onclick = playVideo;
        function playVideo(){
            playState ? video.pause() : video.play();
            playIco.attributes[0].nodeValue = playState ?  "#icon-bofang4" : "#icon-zanting";
            playVideoBox.style.display = playState ? "block" : "none";
            playState = !playState;            
        };
        
 
        //进度条
        let progress = document.querySelector("#controls .progress");
        video.ontimeupdate = function(){
            let duration = video.duration,
                currentTime = video.currentTime,
                rate = currentTime / duration * 100 + "%";
            progress.style.backgroundImage = `linear-gradient(to right,green ${rate},#ccc 0%)`;
        };
 
        //改变进度条
        progress.onmousedown = changeProgress;
        progress.onmousemove = changeProgress;
        progress.onmouseup = changeProgress;
        let toggle = false;//状态开关,用来判断当前是否已经在元素下按下 false表示没按下
        function changeProgress(e){
            switch(e.type){
                case "mousedown" : 
                    toggle = true;
                    video.currentTime = video.duration / progress.clientWidth * e.offsetX;
                    break;
                case "mousemove" :
                    if(toggle) video.currentTime = video.duration / progress.clientWidth * e.offsetX; break;
                case "mouseup" :
                    toggle = false;
                    break;
            }
            // 当前时长 = 总时长/总路程*当前路程 video.currentTime = video.duration / e.clientWidth * e.offsetX
        }
        
        
        //音量
        let volumeBtn = document.querySelector(".volume .volume-btn"),
            volumeIco = document.querySelector(".volume .volume-btn use"),
            volumeBar = document.querySelector(" .volume-bar"),
            volBarTitle = document.querySelector(".volume-item .title");
            volumeState = false;//音量按钮是否按键
        volumeBtn.onclick = function(){
            volumeIco.attributes[0].nodeValue = volumeState ?  "#icon-yinliang" : "#icon-jingyin";
            video.volume = volumeState ? 1 : 0;
            volumeBar.style.backgroundImage = volumeState ? "linear-gradient(green 100%, #ccc 0%)" : "linear-gradient(#ccc 100%, red 0%)";
            volBarTitle.innerHTML = volumeState ? "100%" : "0%";
            volumeState = !volumeState; 
        }
        
        //音量条改变
        let volBarState = false;//音量条是否按键
        volumeBar.onmousedown = changeVolumeBar;
        volumeBar.mousemove = changeVolumeBar;
        volumeBar.onmouseup = changeVolumeBar;
        console.log(volBarTitle.innerHTML);
        function changeVolumeBar(e){
            let volBarBackground = 100 / this.offsetHeight * e.offsetY + "%";
            this.style.backgroundImage = `linear-gradient(#ccc ${volBarBackground}, green 0%)`;
            let vol = 100 / this.offsetHeight * (100-e.offsetY) / 100;
            switch(e.type){
                case "mousedown" : 
                    video.volume = vol;
                    volBarState = true;
                    break;
                case "mousemove" :
                    if(volBarState) video.volume = vol; break;
                case "mouseup" :
                    volBarState = false;
                    break;
            }
            volBarTitle.innerHTML = Math.round(vol * 100) + "%";
        }
 
        
        //文件上传
        let fileInput = document.querySelector("#controls #file #io")
            title = document.querySelector("#wrap h3");
        fileInput.onchange = function(){
            title.innerHTML = this.files[0].name;
            video.setAttribute("src",URL.createObjectURL(this.files[0]));
            progress.style.backgroundImage = "linear-gradient(to right,green 0%,#ccc 0%)";
            video.play();
            playState = true;
        }
 
 
        //播放结束
        video.onended = function(){
            aPlay.style.backgroundImage = "url(./img/source/images/play.png)";
            playState = false;
        } 
 
 
    </script>
 
    <script src="./img/download/font_tz1p0vnxq7m/iconfont.js"></script>
</body>
</html>