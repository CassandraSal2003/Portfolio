let myVideo = document.querySelector("video"); //DOM queries
let playBtn = document.querySelector("polygon");
let pauseBtn = document.querySelector("rect");

function playVideo() { //function that is called in response to a DOM event
  myVideo.play();
  changeHeadingColor('lightpink'); 
}

function pauseVideo() { //function that is called in response to a DOM event
  myVideo.pause();
  changeHeadingColor('black'); 
}

playBtn.addEventListener("click", playVideo);
pauseBtn.addEventListener("click", pauseVideo);



function changeHeadingColor(color) {
  let heading = document.querySelector('h1'); //DOM query. select heading 
  heading.style.color = color; 
}

