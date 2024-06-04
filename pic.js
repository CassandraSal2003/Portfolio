let profilePic = document.querySelector(".profilePic"); //DOM query 
profilePic.addEventListener('click', changePic);

function changePic() {
  
    if (profilePic.src.endsWith('bananas.jpeg')) {
        profilePic.src = 'win.png';
        changeHeadingColor('lightpink'); 
    
    } else if (profilePic.src.endsWith('win.png')){
        profilePic.src = 'lose.png';
    } else {
        profilePic.src = 'bananas.jpeg';
        changeHeadingColor('yellow'); 
    }
}

function changeHeadingColor(color) {
    let heading = document.querySelector('h1'); //DOM query. select heading 
    heading.style.color = color; 
}


