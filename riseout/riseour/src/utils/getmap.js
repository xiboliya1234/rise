/**
 * 加载地图
 * @param {Function} callback 回调函数名称，回调函数将会挂载到window上，例如：window.initBaiduMapScript，在方法中就能拿到BMap对象
 */
export function loadBMap(callback) {
    var script = document.createElement('script')
    script.src = 'http://api.map.baidu.com/api?v=3.0&ak=Q9j18GjHKbdhRu5Qar59AMOEriyvt7ud&callback=' + callback
    document.body.appendChild(script)
}