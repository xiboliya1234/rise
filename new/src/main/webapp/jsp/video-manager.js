document.addEventListener('DOMContentLoaded', () => {
  const input = document.getElementById('video-input');
  const list = document.getElementById('video-list');

  input.addEventListener('change', () => {
    list.innerHTML = '';
    Array.from(input.files).forEach((file) => {
      const container = document.createElement('div');
      container.classList.add('video-container');
      
      const video = document.createElement('video');
      video.setAttribute('controls', '');
      video.src = URL.createObjectURL(file);
      
      const title = document.createElement('p');
      title.textContent = file.name;
      
      container.appendChild(video);
      container.appendChild(title);
      list.appendChild(container);
    });
  });
});
